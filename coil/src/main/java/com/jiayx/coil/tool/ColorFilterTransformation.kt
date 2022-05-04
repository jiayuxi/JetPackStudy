package com.jiayx.coil.tool

import android.graphics.*
import androidx.annotation.ColorInt
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation
import com.jiayx.coil.tool.Util.safeConfig

/**
 *Created by yuxi_
on 2022/5/4
添加蒙层
 */
class ColorFilterTransformation(@ColorInt private val color: Int) : Transformation {
    override val cacheKey: String = "${ColorFilterTransformation::class.java.name}-$color"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val output = createBitmap(input.width, input.height, input.safeConfig)
        val canvas = Canvas(output)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        canvas.drawBitmap(input, 0f, 0f, paint)
        return output

    }

}