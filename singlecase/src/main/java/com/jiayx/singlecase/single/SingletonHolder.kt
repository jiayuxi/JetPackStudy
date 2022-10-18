package com.jiayx.singlecase.single

/**
 *  author : Jia yu xi
 *  date : 2022/10/18 21:02:02
 *  description : 单例携带参数
 */
open class SingletonHolder<out T, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator

    @Volatile
    private var instance: T? = null

    fun getInstance(args: A): T {
        val i = instance
        if (i != null) {
            return i
        }
        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                return i2
            } else {
                val created = creator!!(args)
                instance = created
                creator = null
                created
            }
        }
    }

    /**
     *  对上述方法的一种更简洁的写法
     */
    fun getInstance2(args: A): T = instance ?: synchronized(this) {
        instance ?: creator!!(args).apply {
            instance = this
        }
    }
}