package com.jiayx.flow.utils

import java.io.InputStream
import java.io.OutputStream

/**
 *Created by yuxi_
on 2022/4/1
顶层函数
 */

const val DEFAULT_BUFFER_SIZE = 1024

/**
 * InputStream 的扩展函数
 */
inline fun InputStream.copyTo(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    progress: (Long) -> Unit
): Long {
    var bytesCopied: Long = 0
    val buffer = ByteArray(bufferSize)
    var bytes = read(buffer)
    while (bytes >= 0) {
        out.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
        progress(bytesCopied) //在最后调用内联函数
    }
    return bytesCopied
}

