# 什么是响应式编程
响应式编程基于观察者模式，是一种面向数据流和变化传播的声明式编程方式。
换个说法就是：响应式编程是使用异步数据流进行编程

# StateFlow与SharedFlow有什么区别？

它们的区别如下:
1、SharedFlow 配置更为灵活，支持配置 replay ，缓冲区大小等，stateFlow 是 sharedFlow 的特化版本，replay固定为1，缓冲区大小默认为0。
2、StateFlow与LiveData类似，支持通过myFlow.value获取当前状态，如果有这个需求，必须使用StateFlow
3、SharedFlow支持发出和收集重复值，而StateFlow当value重复时，不会回调collect
4、对于新的订阅者，StateFlow只会重播当前最新值，SharedFlow可配置重播元素个数（默认为0，即不重播）

可以看出,StateFlow为我们做了一些默认的配置，在SharedFlow上添加了一些默认约束，这些配置可能并不符合我们的要求
它忽略重复的值，并且是不可配置的。这会带来一些问题，比如当往List中添加元素并更新时，StateFlow会认为是重复的值并忽略 它需要一个初始值，
并且在开始订阅时会回调初始值，这有可能不是我们想要的
它默认是粘性的，新用户订阅会获得当前的最新值，而且是不可配置的,而SharedFlow可以修改replay
StateFlow施加在SharedFlow上的约束可能不是最适合您，如果不需要访问myFlow.value，并且享受SharedFlow的灵活性， 可以选择考虑使用SharedFlow

# SharedFlow
SharedFlow是热流，而collect是个挂起函数，会一直等待上游数据，不论上游是否发送数据。
所以对于SharedFlow需要注意消费者所在的协程内，后续任务是不会执行的。
SharedFlow的collect内是个无限循环，会一直尝试从缓存中取值，所以collect会一直处于挂起状态，直到所在协程关闭。

# StateFlow
StateFlow实际上是SharedFlow的子类，同样也拥有只读与可读可写的两种类型，StateFlow与MutableStateFlow。
同样是利用同名工厂函数的进行创建，只是相比SharedFlow，StateFlow必须设置默认初始值。

@Suppress("FunctionName")
public fun <T> MutableStateFlow(value: T): MutableStateFlow<T> = StateFlowImpl(value ?: NULL)

而且MutableStateFlow是无法配置缓冲区的，或者说固定永远只有一个，只会缓存最新的值。

StateFlow订阅者所在的协程，最好使用独立协程，collect会一直挂起，协程内的后续操作不会执行

# channelFlow
Channel在概念上类似于BlockQueue，并发安全的缓冲队列（先进先出），实现多个协程间的通信。
Channel内的发送数据和接收数据默认都是挂起函数。
对于同一个Channel对象，允许多个协程发送数据，也允许多个协程接收数据。
区别于Flow ，Channel是一个热流，但其并不支持数据流操作。
即使没有订阅消费，生产端同样也会开始发送数据，并且始终处于运行状态。

# 总结：

我们应该根据自己的需求合理选择组件的使用 如果你的数据流比较简单，不需要进行线程切换与复杂的数据变换，LiveData对你来说相信已经足够了
如果你的数据流比较复杂，需要切换线程等操作，不需要发送重复值，需要获取myFlow.value，StateFlow对你来说是个好的选择
如果你的数据流比较复杂，同时不需要获取myFlow.value，需要配置新用户订阅重播无素的个数，或者需要发送重复的值，可以考虑使用SharedFlow

# flow 的分类

1、flow 为冷流，没有消费者，不会产生数据。 
2、sharedFlow,stateFlow 为热流，无观察者时，也会产生数据。

# flow 一般的 flow 
flow{}为上游数据提供方，
并通过emit()发送一个或多个数据，当发送多个数据时，数据流整体是有序的，即先发送先接收；
另外发送的数据必须来自同一个协程内，不允许来自多个CoroutineContext，
所以默认不能在flow{}中创建新协程或通过withContext()切换协程。
如需切换上游的CoroutineContext，可以通过flowOn()进行切换。
collect{}为下游数据使用方，collect是一个扩展函数，且是一个非阻塞式挂起函数(使用suspend修饰)，
所以Flow只能在kotlin协程中使用。
其他操作符可以认为都是服务于整个数据流的，包括对上游数据处理、异常处理等
 

val testFlow = flow<String> { 
emit("hello")
emit("一般 flow 冷流")
} 
coroutineScope.launch{ 
testFlow.collect { 
     println("collect value : $it")
  } 
testFlow.collectLatest { 
     println("collectLatest value:$it")
 } 
}

# stateFlow

有状态的Flow ，可以有多个观察者，热流 构造时需要传入初始值 : initialState 常用作与UI相关的数据观察，类比LiveData

val stateFlow = MutableStateFlow(0)
coroutineScope {
stateFlow.collect {
println("stateFlow 有状态的flow: value:$it")
}
}
stateFlow.value = 3
}

# SharedFlow
可定制化的StateFlow，可以有多个观察者，热流.
无需初始值，有三个可选参数：
replay - 重播给新订阅者的值的数量（不能为负，默认为零）。
extraBufferCapacity - 除了replay之外缓冲的值的数量。 当有剩余缓冲区空间时， emit不会挂起（可选，不能为负，默认为零）。
onBufferOverflow - 配置缓冲区溢出的操作（可选，默认为暂停尝试发出值）

//创建
val signEvent = MutableSharedFlow<String>()
coroutineScope {
signEvent.collect {
println("sharedFlow value: $it")
}
}
signEvent.tryEmit("hello")
signEvent.tryEmit("sharedFlow")
}

# flow 操作符 
1、创建操作符
flow：创建Flow的操作符。
flowOf：构造一组数据的Flow进行发送。
asFlow：将其他数据转换成Flow，一般是集合向Flow的转换，如listOf(1,2,3).asFlow()。
callbackFlow：将基于回调的 API 转换为Flow数据流e 

2、回调操作符
onStart：上游flow{}开始发送数据之前执行
onCompletion：flow数据流取消或者结束时执行
onEach：上游向下游发送数据之前调用
onEmpty：当流完成却没有发出任何元素时执行。如emptyFlow<String>().onEmpty {}
onSubscription：SharedFlow 专用操作符，建立订阅之后回调。
和onStart的区别：因为SharedFlow是热流，因此如果在onStart发送数据，下游可能接收不到，因为提前执行了

3、变换操作符
map：对上游发送的数据进行变换，collect最后接收的是变换之后的值
mapLatest：类似于collectLatest，当emit发送新值，会取消掉map上一次转换还未完成的值。
mapNotNull：仅发送map之后不为空的值。
transform：对发出的值进行变换 。不同于map的是，经过transform之后可以重新发送数据，甚至发送多个数据，因为transform内部又重新构建了flow。
transformLatest：类似于mapLatest，当有新值发送时，会取消掉之前还未转换完成的值。
transformWhile：返回值是一个Boolean，当为true时会继续往下执行；反之为false，本次发送的流程会中断。
asSharedFlow： MutableStateFlow 转换为 StateFlow ，即从可变状态变成不可变状态。
asStateFlow：MutableSharedFlow 转换为 SharedFlow ，即从可变状态变成不可变状态。
receiveAsFlow：Channel 转换为Flow ，上游与下游是一对一的关系。如果有多个下游观察者，可能会轮流收到值。
consumeAsFlow：Channel 转换为Flow ，有多个下游观察者时会crash。
withIndex：将数据包装成IndexedValue类型，内部包含了当前数据的Index。
scan(initial: R, operation: suspend (accumulator: R, value: T) -> R)：把initial初始值和每一步的操作结果发送出去。
produceIn：转换为Channel的 ReceiveChannel
runningFold(initial, operation: (accumulator: R, value: T) -> R)：initial值与前面的流共同计算后返回一个新流，将每步的结果发送出去。
runningReduce*：返回一个新流，将每步的结果发送出去，默认没有initial值。
shareIn：flow 转化为 SharedFlow，后面会详细介绍。
stateIn：flow转化为StateFlow，后面会详细介绍。
 
4、过滤操作符
filter
filterInstance
filterNot
filterNotNull
drop
dropWhile
take
takeWhile
debounce
sample
distinctUntilChangedBy
distinctUntilXhanged

5、组合操作符
combine：组合两个Flow流最新发出的数据，直到两个流都结束为止。扩展：在kotlinx-coroutines-core-jvm中的FlowKt中，
可以将更多的flow结合起来返回一个Flow<Any>，典型应用场景：多个筛选条件选中后，展示符合条件的数据。
如果后续某个筛选条件发生了改变，只需要通过发生改变的Flow的flow.value = newValue重新发送， combine就会自动构建出新的Flow<Any>，这样UI层会接收到新的变化条件进行刷新即可。
combineTransform： combine + transform操作
merge：listOf(flow1, flow2).merge()，多个流合并为一个流。
flattenConcat：以顺序方式将给定的流展开为单个流 。
flattenMerge：作用和 flattenConcat 一样，但是可以设置并发收集流的数量。
flatMapContact：相当于 map + flattenConcat , 通过 map 转成一个流，在通过 flattenConcat发送。
flatMapLatest：当有新值发送时，会取消掉之前还未转换完成的值。
flatMapMerge：相当于 map + flattenMerge ，参数concurrency: Int 来限制并发数。
zip：组合两个Flow流最新发出的数据，不同于combine的是，当其中一个流结束时，那整个过程就结束了。

6、功能性操作符
cancellable
catch：对此操作符之前的流发生的异常进行捕获，对此操作符之后的流无影响。当发生异常时，默认collect{}中lambda将不会再执行。当然，可以自行通过emit()继续发送。
retryWhen
retry
buffer
conflate : 仅保留最新值, 内部就是 buffer``(``CONFLATED``)
flowOn：flowOn 会更改上游数据流的 CoroutineContext，且只会影响flowOn之前（或之上）的任何中间运算符。下游数据流（晚于 flowOn 的中间运算符和使用方）不会受到影响。如果有多个 flowOn 运算符，每个运算符都会更改当前位置的上游数据流。
 
7、末端操作符
collect：数据收集操作符，默认的flow是冷流，即当执行collect时，上游才会被触发执行。
collectIndexed：带下标的收集操作，如collectIndexed{ index, value -> }。
collectLatest：与collect的区别：当新值从上游发出时，如果上个收集还未完成，会取消上个值得收集操作。
toCollection、toList、toSet：将flow{}结果转化为集合
