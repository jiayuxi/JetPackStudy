# channelFlow
Channel在概念上类似于BlockQueue，并发安全的缓冲队列（先进先出），实现多个协程间的通信。
Channel 实际上是一个并发安全的队列，它可以用来连接协程，实现不同协程的通信

Channel内的发送数据和接收数据默认都是挂起函数。
对于同一个Channel对象，允许多个协程发送数据，也允许多个协程接收数据。
区别于Flow ，Channel是一个热流，但其并不支持数据流操作。
即使没有订阅消费，生产端同样也会开始发送数据，并且始终处于运行状态。


# channel 基础 
```kotlin
public fun <E> Channel(
capacity: Int = RENDEZVOUS,//缓冲队列的容量 
onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND,// 被压的策略
onUndeliveredElement: ((E) -> Unit)? = null // 在元素被发送但未交付给消费者时调用代码块
): Channel<E>
```
 capacity 参数为缓冲区容量，通常是以 Channel 中定义的常量为值

 RENDEZVOUS：默认无锁、无缓冲区，只有消费端调用时，才会发送数据，否则挂起发送操作。
             当缓存策略不为BufferOverflow.SUSPEND时，会创建缓冲区容量为1的ArrayChannel。
 CONFLATED： 队列容量仅为一，且onBufferOverflow参数只能为BufferOverflow.SUSPEND。
             缓冲区满时，永远用最新元素替代，之前的元素将被废弃。
             创建实现类ConflatedChannel
             内部会使用ReentrantLock对发送与接收元素操作进行加锁，线程安全。
 UNLIMITED： 无限制容量，缓冲队列满后，会直接扩容，直到OOM。
             内部无锁，永远不会挂起
 BUFFERED： 默认创建64位容量的缓冲队列，当缓存队列满后，会挂起发送数据，直到队列有空余。
            创建实现类ArrayChannel，内部会使用ReentrantLock对发送与接收元素操作进行加锁，线程安全。

 自定义容量：当capacity容量为1，且onBufferOverflow为BufferOverflow.DROP_OLDEST时，
           由于与CONFLATED工作原理相同，会直接创建为实现类ConflatedChannel。
           其他情况都会创建为实现类ArrayChannel。

# 生产者 

SendChannel : 是发送数据的生产者
1、send : 挂起函数，向队列中添加元素，在缓冲队列满时，会挂起协程，暂停存入元素，直到队列容量满足存入需求，恢复协程
          public suspend fun send(element: E)

2、trySend : 尝试向队列中添加元素，返回 ChannelResult 操作结果
             public fun trySend(element: E): ChannelResult<Unit> 

3、close : 关闭队列，幂等操作，后续操作都无效，只允许存在一个
           public fun close(cause: Throwable? = null): Boolean

4、isClosedForSend ：实验性质API，为ture时表示Channel已经关闭，停止发送。

ProducerScope : SendChannel还有个子接口ProducerScope，表示允许发送数据的协程作用域。
                同时官方还提供了一个CoroutineScope的拓展函数produce，用于快速启动生产者协程，并返回ReceiveChannel   

1、awaitClose : 挂起函数，ProducerScope中的拓展函数，会挂起等待协程关闭，在关闭前执行操作，通常用于资源回收。
                调用awaitClose后，需要外部手动调用SendChannel的close进行关闭，否则协程会一直挂起等待关闭，直到协程作用域被关闭。

# 消费者
ReceiveChannel ：表示接收数据的消费者，其只有一个收集数据的作用

1、receive ：挂起函数，从缓冲队列中接收并移除元素，如果缓冲队列为空，则挂起协程。
            public suspend fun receive(): E
            如果在Channel被关闭后，调用receive去取值，会抛出ClosedReceiveChannelException异常。

2、receiveCatching ：挂起函数，功能与receive相同，只是防止在缓冲队列关闭时突然抛出异常导致程序崩溃，会返回ChannelResult包裹取出的元素值，同时表示当前操作的状态
                    public suspend fun receiveCatching(): ChannelResult<E>

3、tryReceive ：尝试从缓冲队列中拉取元素，返回ChannelResult包裹取出的元素，并表示操作结果。
               public fun tryReceive(): ChannelResult<E>

4、cancel ：缓冲队列的接收端停止接收数据，会移除缓冲队列的所有元素，并停止SendChannel发送数据，内部会调用SendChannel的close函数。
           谨慎调用该函数，通常Channel应该由发送端SendChannel来主导通道是否关闭。 
           毕竟很少会有老师还在【台上发言】，下面学生就已经说【我听完了】的场景。

5、iterator ：挂起函数，接收Channel时，允许使用for循环进行迭代
             public operator fun iterator(): ChannelIterator<E>
             ReceiverChannel的迭代器是挂起函数，只能在协程中使用。
             public interface ChannelIterator<out E> {
             public suspend operator fun hasNext(): Boolean
              ...
             }

6、consume ：ReceiveChannel的拓展函数，在Channel出现异常或结束后，调用cancel关闭Channel接收端。

7、ChannelResult ：是一个内联类，仅用于表示Channel操作的结果，并携带元素
   
   
小结：Channel目前版本仅作为生产者-消费者模型缓冲队列，多协程间通信的基础设施而存在。
