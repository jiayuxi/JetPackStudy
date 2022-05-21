# flow 与 其它方式的区别
1、名为 flow 的Flow 类型构建器函数
2、flow {...} 构建块中的代码可以挂起
3、函数 flow 不在标有suspend 修饰符
4、流使用 emit 函数发射值
5、流使用 collect 函数收集值

# flow 冷流
是一种类似于序列的冷流 ，flow构建器中的代码直到流被收集的时候才开始运行
runBlocking {
     val flow = flow<Int> {
     emit(1)
    }
    coroutineScope {
    flow.collect {
    println("flow value : $it")
    }
}
# flow 的连续性
1、流的每次单独收集都是按顺序执行的，除非使用特殊操作符
2、从上游到下游每个过滤操作符都会处理每个发射出的值，然后在交给末端操作符
  runBlocking {
    (1..3).asFlow().flow.filter {
       it % 2 == 0 
     }.map{ it ->
      "string $it"
    } .collect{ value ->
     println("Collect $value")  
   } 
 }

# flow 的构建器
1、flowOf 构建器定义了一个发射固定值集的流
2、使用 .asFlow 扩展函数，可以将各种集合与序列转换为 Flow流
 runBlocking {
   val flow2 = flowOf(1, 2, 3)
    coroutineScope {
    flow2.collect {
    println("flowOf value: $it")
    }
  }
  println()
  // asFlow
  val flow3 = listOf(1, 2, 3).asFlow()
  coroutineScope {
  flow3.collectLatest {
  println("asFlow value: $it")
  }
 }
}

# Flow 流上下文
1、流的收集总是在调用协程的上下文中发生，流的该属性称为上下文保存
2、flow{...} 构建器中的代码必须遵循上下文保存属性，并且不允许从其他上下文中发射(emit)
3、flowOn 操作符，该函数用于更改流发射的上下文

# Flow 启动流
使用 launchIn 替换 collect 我们可以在单独的协程中启动流的收集
launchIn 指定协程作用域 中运行

# Flow 流的取消
流采用与协程同样的协作取消。像往常一样，流的收集可以是当流在一个可以取消的挂起函数（例如delay）中挂起的时候取消

流的取消检测：
1、为了方便起见，流构建器对每个发射值执行附加的 ensureActive 检测已进行取消，这就意味着从 flow{...}发射的繁忙循环是可以取消的
2、出于性能原因，大多数其它流操作不会自行执行其它取消检测，在协程处于循环繁忙的情况下，必须明确检测是否取消
3、通过cancelable 操作符来执行此操作

# Flow 背压
1、buffer ：并发运行流中发射元素的代码
2、conflate ：合并发射，不对每个值进行处理
3、collectLatest : 取消并重新发射最后一个值
4、当必须更改 CoroutineDispatcher时，flowOn 操作符使用了相同的缓冲机制，但是 buffer 函数显示的请求缓冲而不改变执行上下文

# Flow 操作符

# Flow 组合操作符
1、就像Kotlin 标准库中的Sequence.zip 扩展函数一样，流拥有一个 zip 操作符用于组合两个流中的相关值

# Flow 展平流
1、flatMapConcat : 连接模式
2、flatMapMerge : 合并模式
3、flatMapLatest: 最新展平模式

# Flow 流的异常处理,当运算符中的发射器或代码抛出异常时，有集中处理异常的方法，
1、try/catch 块
2、catch 函数

# Flow 流的完成
1、当流收集完成时(普通情况或异常情况)，他可能需要执行一个动作
  1.1、命令是 finally 块
  1.2、onCompletion 声明式处理





