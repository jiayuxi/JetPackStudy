package com.jiayx.coil.tool

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation
import com.jiayx.coil.tool.Util.safeConfig

/**
 * 使用另一个可绘制的遮罩变换。
 */
class MaskTransformation(
    private val context: Context,
    @DrawableRes val maskDrawableRes: Int
) : Transformation {

    companion object {
        private val paint = Paint()
            .apply {
                xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            }
    }

    override val cacheKey: String = "${MaskTransformation::class.java.name}-$maskDrawableRes"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {

        val output = createBitmap(input.width, input.height, input.safeConfig)
        output.setHasAlpha(true)

        val mask = getMaskDrawable(context.applicationContext, maskDrawableRes)

        output.applyCanvas {
            mask.setBounds(0, 0, width, height)
            mask.draw(this)
            drawBitmap(input, 0f, 0f, paint)
        }
        return output
    }

    private fun getMaskDrawable(context: Context, maskId: Int): Drawable {
        return ResourcesCompat.getDrawable(context.resources, maskId, null)
            ?: throw IllegalArgumentException("maskId is invalid")
    }
}