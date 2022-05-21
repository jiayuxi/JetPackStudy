package com.jiayx.gesturedetector.activity

import android.app.Service
import android.media.AudioManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jiayx.gesturedetector.R
import com.jiayx.gesturedetector.databinding.ActivityGestureVideoBinding
import com.jiayx.gesturedetector.tool.BrightnessHelper
import com.jiayx.gesturedetector.view.VideoGestureRelativeLayout
import java.math.RoundingMode.valueOf


/**
 *Created by yuxi_
on 2022/5/21
 */
class GestureVideoActivity : AppCompatActivity(), VideoGestureRelativeLayout.VideoGestureListener {
    private val binding: ActivityGestureVideoBinding by lazy {
        ActivityGestureVideoBinding.inflate(layoutInflater)
    }

    private lateinit var mAudioManager: AudioManager
    private var maxVolume = 0
    private var oldVolume = 0
    private var newProgress = 0
    private var oldProgress: Int = 0
    private lateinit var mBrightnessHelper: BrightnessHelper
    private var brightness = 1f
    private lateinit var mWindow: Window
    private lateinit var mLayoutParams: WindowManager.LayoutParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lyVG.setVideoGestureListener(this)
        //初始化获取音量属性
        mAudioManager = getSystemService(Service.AUDIO_SERVICE) as AudioManager
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        //初始化亮度调节
        mBrightnessHelper = BrightnessHelper(this)

        //下面这是设置当前APP亮度的方法配置
        mWindow = window
        mLayoutParams = mWindow.attributes
        brightness = mLayoutParams.screenBrightness

    }

    /**
     * 亮度计算
     */
    override fun onBrightnessGesture(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ) {
        if (e1 == null || e2 == null) return
        //这是直接设置系统亮度的方法
//        if (Math.abs(distanceY) > ly_VG.getHeight()/255){
//            if (distanceY > 0){
//                setBrightness(4);
//            }else {
//                setBrightness(-4);
//            }
//        }
        //下面这是设置当前APP亮度的方法
        var newBrightness: Float = (e1.y - e2.y) / binding.lyVG.height
        newBrightness += brightness
        if (newBrightness < 0) {
            newBrightness = 0f
        } else if (newBrightness > 1) {
            newBrightness = 1f
        }
        mLayoutParams.screenBrightness = newBrightness
        mWindow.attributes = mLayoutParams
        binding.scl.setProgress((newBrightness * 100).toInt())
        binding.scl.setImageResource(R.drawable.brightness_w)
        binding.scl.show()
    }

    //这是直接设置系统亮度的方法
    private fun setBrightness(brightness: Int) {
        //要是有自动调节亮度，把它关掉
        mBrightnessHelper.offAutoBrightness()
        val oldBrightness = mBrightnessHelper.getBrightness()
        val newBrightness = oldBrightness + brightness
        //设置亮度
        mBrightnessHelper.setSystemBrightness(newBrightness)
        //设置显示
        binding.scl.setProgress((java.lang.Float.valueOf(newBrightness.toFloat()) / mBrightnessHelper.getMaxBrightness() * 100).toInt())
        binding.scl.setImageResource(R.drawable.brightness_w)
        binding.scl.show()
    }

    /**
     * 声音计算
     */
    override fun onVolumeGesture(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ) {
        if (e1 == null || e2 == null) return
        val value: Int = binding.lyVG.height / maxVolume
        val newVolume = ((e1.y - e2.y) / value + oldVolume).toInt()

        mAudioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            newVolume,
            AudioManager.FLAG_PLAY_SOUND
        )

//        int newVolume = oldVolume;
        //另外一种调音量的方法，感觉体验不好，就没采用
//        if (distanceY > value){
//            newVolume = 1 + oldVolume;
//            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
//        }else if (distanceY < -value){
//            newVolume = oldVolume - 1;
//            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
//        }
        //另外一种调音量的方法，感觉体验不好，就没采用
//        if (distanceY > value){
//            newVolume = 1 + oldVolume;
//            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
//        }else if (distanceY < -value){
//            newVolume = oldVolume - 1;
//            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
//        }
        //要强行转Float类型才能算出小数点，不然结果一直为0
        val volumeProgress =
            (newVolume / maxVolume.toFloat() * 100).toInt()
        if (volumeProgress >= 50) {
            binding.scl.setImageResource(R.drawable.volume_higher_w)
        } else if (volumeProgress > 0) {
            binding.scl.setImageResource(R.drawable.volume_lower_w)
        } else {
            binding.scl.setImageResource(R.drawable.volume_off_w)
        }
        binding.scl.setProgress(volumeProgress)
        binding.scl.show()
    }

    /**
     * 进度调试
     */
    override fun onFF_REWGesture(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ) {
        if (e1 == null || e2 == null) return
        val offset = e2.x - e1.x
        //根据移动的正负决定快进还是快退
        //根据移动的正负决定快进还是快退
        if (offset > 0) {
            binding.scl.setImageResource(R.drawable.ff)
            newProgress = (oldProgress + offset / binding.lyVG.width * 100).toInt()
            if (newProgress > 100) {
                newProgress = 100
            }
        } else {
            binding.scl.setImageResource(R.drawable.fr)
            newProgress = (oldProgress + offset / binding.lyVG.width * 100).toInt()
            if (newProgress < 0) {
                newProgress = 0
            }
        }

        binding.scl.setProgress(newProgress)
        binding.scl.show()
    }

    /**
     * 单击事件
     */
    override fun onSingleTapGesture(e: MotionEvent?) {
        makeToast("SingleTap")
    }

    /**
     * 双击事件
     */
    override fun onDoubleTapGesture(e: MotionEvent?) {
        makeToast("DoubleTap")
    }

    /**
     * 按下事件
     */
    override fun onDown(e: MotionEvent?) {
        //每次按下的时候更新当前亮度和音量，还有进度
        oldProgress = newProgress
        oldVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        brightness = mLayoutParams.screenBrightness
        if (brightness == -1f) {
            //一开始是默认亮度的时候，获取系统亮度，计算比例值
            brightness = mBrightnessHelper.getBrightness() / 255f
        }
    }

    override fun onEndFF_REW(e: MotionEvent?) {
        makeToast("设置进度为 $newProgress")
    }

    private fun makeToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}