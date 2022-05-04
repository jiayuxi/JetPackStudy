package com.jiayx.coil.tool

import coil.size.Dimension

/**
 *Created by yuxi_
on 2022/5/4
 */
/**
 * If this is a [Dimension.Pixels] value, return its number of pixels. Else, invoke and return
 * the value from [block].
 */
internal inline fun Dimension.pxOrElse(block: () -> Int): Int {
    return if (this is Dimension.Pixels) px else block()
}