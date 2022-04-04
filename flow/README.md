# CoroutinesScope
协程作用域
1 、定义协程必须指定 CoroutinesScope，它会跟踪所有协程，同样它还可以取消它所启动的所有协程
2、常用相关的API
   2.1、GlobalScope ：声明周期是process级别的，即使 activity ，fragment 销毁，协程仍然在执行
   2.2、MainScope : 在 activity 中使用，可以在 onDestroy 中取消
   2.3、viewModelScope : 只能在 viewModel 中使用，绑定viewModel的生命周期
   2.4、lifecycleScope : 只能在 activity，fragment 中使用，绑定 activity，fragment 的声明周期
# 协程作用域构建器
 coroutinesScope 与 runBlocking 
 1、runBlocking 是常规函数，而 coroutinesScope 是挂起函数
 2、它们都等待其携程提以及所有子协程结束，主要区别在与 runBlocking 方法会阻塞当前线程来等待，而 coroutinesScope挂起函数，会释放底层线程用于其他用途 
 3、coroutineScope 与  supervisorScope
    3.1、coroutineScope : 一个协程失败，其他兄弟协程也会被取消
    3.2、supervisorScope ： 一个协程失败，不影响其他兄弟协程
# 协程调度器
所有协程必须在调度器中运行，即使它们在主线程中运行也是如此
1、Dispatchers.main : android 上的主线程，用来处理UI交互和一些轻量级任务（调用 suspend 函数，UI 函数，更新 LiveData）
2、Dispatchers.IO : 非主线程，专为 磁盘和网络进行了优化(数据库，文件读写，网络处理)
3、Dispatchers.Default : 非主线程，专为 CPU 密集型任务进行了优化(数组排序，JSON 数据解析，处理差异判断)

# 协程构建器
launch 与 async 都是来启动协程的
1、launch ：返回一个 Job 并且不附带任务结果值
2、async ： 返回一个 Deferred,Deferred 也是一个Job,可以使用.await() 在一个延期的值上得到一个最终结果值

等待一个作业：
1、job 与 await
2、组合并发

# 协程解决的问题
1、处理耗时任务，这种任务常常会阻塞主线程
2、保证主线程安全，即确保安全的从主线程调用任何 suspend 函数

# 协程是什么 协程基于线程，它是轻量级的线程
1、协程让异步逻辑同步化，杜绝回调地狱
2、协程最核心的点是，函数或一段程序能够被挂起，稍后再在挂起的位置恢复

# 协程的挂起与恢复
1、suspend - 也称为挂起或暂停，用于暂停当前的协程，并保存所有局部变量
2、resume - 用于让已暂停的协程从暂停处继续执行


# 结构化并发
结构化并发可以做到：
1、取消任务：当某项任务不在需要时取消它
2、追踪任务：当任务正在执行，追踪它
3、发出错误信号：当协程失败时，发出错误信号表明有错误发生

# 协程启动模式
1、DEFAULT ：协程创建后，立即开始调度，在调度前如果协程被取消，其将直接进入响应取消的状态
2、ATOMIC ：协程创建后，立即开始调度，协程执行到第一个挂起点之前不响应取消
3、LAZY ：只有协程被需要时，包括主动调用协程的 start ,await 或者 join 等函数时才开始调度，如果调度前就被取消，那么该协程就会直接进入异常结束状态
4、UNDISPATCHED ：协程被创建后，立即在当前函数调用栈中执行，知道遇到第一个真正挂起的点

# job 对象
 对于每一个创建的协程(通过launch 或 async ) ，会返回一个Job实例，该实例是 协程的 唯一标识，并且负责管理协程的声明周期
 
一个任务可以包含一系列的状态
   新建(new),活跃(Active),完成中(Completing),已完成(Completed),取消中(Cancelling),已取消(Cancelled)
   虽然我们无法访问这些状态，但是我们可以访问 Job 的属性：isActive ,isCancelled,isCompleted

# job 生命周期
   如果协程处于活跃状态，协程运行出错或者调用job.cancel()
   都会将当前任务置为取消中（Cancelling）状态(isActive = false,isCancelled = true)
   当所有子协程都完成后，协程进入已取消（Cancelled）状态，此时 isCompleted = true 

    new
      |(stat) 
     Active
      | (complete)         ---->  (cancel/fial) Cancelling(await children)  --- > Cancelled (isCompleted = true,isCancelled = true)
    Completing(await children)
       |(finish)
    Completed 
        (isCompleted = true,isCancelled = false)

# 协程取消
 1、取消作用域，会取消它的子协程
 2、被取消的子协程不会影响其余的兄弟协程
 3、协程抛出一个特殊的异常CancellationException来处理取消操作
 4、所有kotlin.coroutines中挂起的函数（withContext,delay等），都是可以被取消的

# CPU 密集型任务不能被取消
 1、isActive 是一个可以被使用在 CoroutinesScope 中的扩展属性，检查Job 是否处于活跃状态
 2、ensureActive(),如果 job 处于非活跃状态，这个方法会立即抛出异常
 3、yield 函数会检查所在协程的状态，如果已经取消，则抛出CancellationException予与响应，此外，它还会尝试让出线程的执行权，给其它协程提供执行机会
         
# 协程上下文是什么
 1、Job：控制协程的声明周期
 2、CoroutineDispatcher ：向合适的线程分发任务 
 3、CoroutineName ：协程的名称，调试的时候 很有用
 4、CoroutineExceptionHandler ：处理未捕获的异常

# 协程上下文的继承
  继承公式：协程上下文 = 默认值 + 继承的CoroutineContext + 参数
  1、一些元素 包含默认值: Dispatcher.Default 是默认的 CoroutineContext,已经 “coroutine” 作为默认 CoroutineName
  2、继承的 CoroutineContext 是 CoroutineScope 或者是 父协程的 CoroutineContext
  3、传入协程构建器的参数的优先级高于继承的上下文参数，因此会覆盖对应的参数值
  
# 协程的异常传播
  1、协程构建器有两种形式：自动传播异常(launch与 actor) ，向用户暴露异常(async与produce)当这些构建器用于创建
     一个根协程时(该协程不是另一个协程的子协程时)，前者这类构建器，异常会在它发生的第一时间抛出，而后者则依赖用户
     最终消费异常，例如：通过 await 或 receive
# 协程异常的传播特性
  1、当一个协程由于异常而运行失败时，它会传播这个异常并传播给它的父级。
   接下来父级会进行下面几步操作：
   1.1、取消它自己的子集
   1.2、取消它自己
   1.3、将异常传播并传递给它的父级

# 协程的异常捕获
  1、使用 CoroutineExceptionHandler 对协程的异常 进行捕获
     一下条件满足时，异常就会被捕获：
     1.1、时机：异常是被自动抛出异常的协程所抛出的（是 launch ，而不是 async 时）
     1.2、位置：在CoroutineScope的CoroutineContext中或在一个根协程中(CoroutineScope 或者 supervisorScope 的直接子协程)中

# 协程的取消与异常
  1、取消与异常紧密相关，协程内部使用CancellationException来进行取消，这个异常会被忽略
  2、当子协程被取消时，不会取消它的父协程
  3、如果一个协程遇到了CancellationException 以外的异常。它将使用该异常取消它的父协程。当父协程的所有子协程都结束后，异常才会被父协程处理



   
