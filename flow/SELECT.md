# 选择表达式 select
说到Channel就不得不提Kotlin Coroutine中的特殊机制——选择表达式。
在Kotlin Coroutine中存在一个特殊的挂起函数——select，其允许同时等待多个挂起的结果，并且只取用其中最快完成的作为函数恢复的值。


