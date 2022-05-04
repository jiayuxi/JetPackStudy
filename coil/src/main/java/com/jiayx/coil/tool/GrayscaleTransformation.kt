package com.jiayx.coil.tool

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.core.graphics.applyCanvas
import coil.size.Size
import coil.transform.Transformation
import com.jiayx.coil.tool.Util.safeConfig

/**
 * 将图片转化为 灰度的
 */
class GrayscaleTransformation : Transformation {

    override val cacheKey: String = GrayscaleTransformation::class.java.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        paint.colorFilter = COLOR_FILTER

        val output = createBitmap(input.width, input.height, input.safeConfig)
        output.applyCanvas {
            drawBitmap(input, 0f, 0f, paint)
        }
        return output
    }

    override fun equals(other: Any?) = other is GrayscaleTransformation

    override fun hashCode() = javaClass.hashCode()

    override fun toString() = "GrayscaleTransformation()"

    private companion object {
        val COLOR_FILTER = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
    }
}