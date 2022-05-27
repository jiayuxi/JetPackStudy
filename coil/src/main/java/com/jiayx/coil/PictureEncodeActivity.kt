package com.jiayx.coil

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jiayx.coil.databinding.ActivityPictureBinding
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 *Created by yuxi_
on 2022/5/27
图片转化为 String 字符串
 */
class PictureEncodeActivity : AppCompatActivity() {
    val binding: ActivityPictureBinding by lazy {
        ActivityPictureBinding.inflate(layoutInflater)
    }
    private var encodeStr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.image.setImageResource(R.mipmap.a123456)
        encodeStr = bitmapToString(R.mipmap.a123456)
        binding.button.setOnClickListener {
            encodeStr?.let {
                val bitmap = base64ToBitmap(it)
                binding.image2.setImageBitmap(bitmap)
            }
        }
    }

    private fun bitmapToString(resId: Int): String? {
        val bitmap = BitmapFactory.decodeResource(resources, resId)
        var string: String? = null
        val btString = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, btString)
        val bytes: ByteArray = btString.toByteArray()
        string = Base64.encodeToString(bytes, Base64.URL_SAFE)
        return compress(string)
    }

    fun base64ToBitmap(base64String: String): Bitmap? {
        val uncompress = uncompress(base64String)
        val decode = Base64.decode(
            uncompress?.trim { it <= ' ' },
            Base64.URL_SAFE
        )
        return BitmapFactory.decodeByteArray(decode, 0, decode.size)
    }

    /**
     * @param input 须要压缩的字符串
     * @return 压缩后的字符串
     * @throws IOException IO
     */
    @Throws(IOException::class)
    fun compress(input: String?): String? {
        input ?: return null
        val out = ByteArrayOutputStream()
        val gzipOs = GZIPOutputStream(out)
        gzipOs.write(input.toByteArray())
        gzipOs.close()
        return out.toString("ISO-8859-1")
    }

    /**
     * @param zippedStr 压缩后的字符串
     * @return 解压缩后的
     * @throws IOException IO
     */
    @Throws(IOException::class)
    fun uncompress(zippedStr: String?): String? {
        var out: ByteArrayOutputStream? = null
        try {
            Log.d("jia_", "uncompress: $zippedStr")
            zippedStr ?: return zippedStr
            out = ByteArrayOutputStream()
            val `in` = ByteArrayInputStream(
                zippedStr
                    .toByteArray(charset("ISO-8859-1"))
            )
            val gzipIs = GZIPInputStream(`in`)
            val buffer = ByteArray(256)
            var n: Int
            while (gzipIs.read(buffer).also { n = it } >= 0) {
                out.write(buffer, 0, n)
            }
            gzipIs.close()
            `in`.close()
            // toString()应用平台默认编码，也能够显式的指定如toString("GBK")
            return out.toString()
        } finally {
            out?.close()
        }
    }
}