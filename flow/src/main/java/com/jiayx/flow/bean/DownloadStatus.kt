package com.jiayx.flow.bean

import java.io.File

/**
 *Created by yuxi_
on 2022/4/1
 密封类
 */
sealed class DownloadStatus {
    object None : DownloadStatus() // 空状态
    data class Progress(val value: Int) : DownloadStatus() //下载进度
    data class Error(val throwable: Throwable) : DownloadStatus() //错误
    data class Done(val file: File) : DownloadStatus() //完成
}
