package com.jiayx.coil.tool

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation
import com.jiayx.coil.tool.Util.safeConfig

/**
 *Created by yuxi_
on 2022/5/4
 添加水印
 */
class WatermarkTransformation (private val watermark: String, @ColorInt private val textColor: Int, private val textSize: Float) :
    Transformation {
    
    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val width = input.width
        val height = input.height
        val config = input.safeConfig
        val output = createBitmap(width, height, config)
        val canvas = Canvas(output)
        val paint = Paint()
        paint.isAntiAlias = true
        canvas.drawBitmap(input, 0f, 0f, paint)

        canvas.rotate(40f, width / 2f, height / 2f)

        paint.textSize = textSize
        paint.color = textColor

        val textWidth = paint.measureText(watermark)

        canvas.drawText(watermark, (width - textWidth) / 2f, height / 2f, paint)

        return output
    }

    override val cacheKey: String  =
        "${WatermarkTransformation::class.java.name}-${watermark}-${textColor}-${textSize}"

}