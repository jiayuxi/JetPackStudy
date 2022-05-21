package com.jiayx.gesturedetector.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.widget.RelativeLayout
import androidx.annotation.IntDef
import kotlin.math.abs


/**
 *Created by yuxi_
on 2022/5/21
 */
@SuppressLint("ClickableViewAccessibility")
class VideoGestureRelativeLayout(context: Context, attrs: AttributeSet) :
    RelativeLayout(context, attrs) {

    private companion object {
        private const val TAG = "jia_gesture"
        private const val NONE = 0
        private const val VOLUME = 1
        private const val BRIGHTNESS = 2
        private const val FF_REW = 3
    }

    @IntDef(*[NONE, VOLUME, BRIGHTNESS, FF_REW])
    @Retention(value = AnnotationRetention.SOURCE)
    annotation class ScrollMode

    @ScrollMode
    var mScrollMode = NONE

    private var mOnGestureListener: VideoPlayerOnGestureListener? = null
    private var mGestureDetector: GestureDetector? = null
    private var mVideoGestureListener: VideoGestureListener? = null

    //横向偏移检测，让快进快退不那么敏感
    private val offsetX = 1
    private var hasFF_REW = false

    fun setVideoGestureListener(videoGestureListener: VideoGestureListener) {
        mVideoGestureListener = videoGestureListener
    }

    init {
        mOnGestureListener = VideoPlayerOnGestureListener(this)
        mGestureDetector = GestureDetector(context, mOnGestureListener)
        //取消长按，不然会影响滑动
        mGestureDetector?.setIsLongpressEnabled(false)
        setOnTouchListener { _, event ->
            //Log.d(TAG, "onTouch: event:" + event.toString());
            if (event.action == MotionEvent.ACTION_UP) {
                if (hasFF_REW) {
                    mVideoGestureListener?.onEndFF_REW(event)
                    hasFF_REW = false
                }
            }
            //监听触摸事件
            return@setOnTouchListener mGestureDetector?.onTouchEvent(event) == true
        }

//        setOnGenericMotionListener(new OnGenericMotionListener() {
//            @Override
//            public boolean onGenericMotion(View v, MotionEvent event) {
//                Log.d(TAG, "onGenericMotion: " + event.toString());
//                //监听鼠标点击事件
//                return mGestureDetector.onGenericMotionEvent(event);
//            }
//        });
    }

    inner class VideoPlayerOnGestureListener(private val videoGestureRelativeLayout: VideoGestureRelativeLayout) :
        SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            Log.d(TAG, "onDown: ")
            hasFF_REW = false
            //每次按下都重置为NONE
            mScrollMode = NONE
            mVideoGestureListener?.onDown(e)
            return true
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            Log.d(TAG, "onScroll: e1:" + e1.x + "," + e1.y)
            Log.d(TAG, "onScroll: e2:" + e2.x + "," + e2.y)
            Log.d(TAG, "onScroll: X:$distanceX  Y:$distanceY")
            when (mScrollMode) {
                NONE -> {
                    Log.d(TAG, "NONE: ")
                    //offset是让快进快退不要那么敏感的值
                    mScrollMode = if (abs(distanceX) - abs(distanceY) > offsetX) {
                        FF_REW // 进度
                    } else {
                        if (e1.x < width / 2) {
                            BRIGHTNESS // 亮度
                        } else {
                            VOLUME // 声音
                        }
                    }
                }
                VOLUME -> {
                    mVideoGestureListener?.onVolumeGesture(e1, e2, distanceX, distanceY)
                    Log.d(TAG, "VOLUME: ")
                }
                BRIGHTNESS -> {
                    mVideoGestureListener?.onBrightnessGesture(e1, e2, distanceX, distanceY)
                    Log.d(TAG, "BRIGHTNESS: ")
                }
                FF_REW -> {
                    mVideoGestureListener?.onFF_REWGesture(e1, e2, distanceX, distanceY)
                    hasFF_REW = true
                    Log.d(TAG, "FF_REW: ")
                }
            }
            return true
        }

        override fun onContextClick(e: MotionEvent): Boolean {
            Log.d(TAG, "onContextClick: ")
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            Log.d(TAG, "onDoubleTap: ")
            mVideoGestureListener?.onDoubleTapGesture(e)
            return super.onDoubleTap(e)
        }

        override fun onLongPress(e: MotionEvent) {
            Log.d(TAG, "onLongPress: ")
            super.onLongPress(e)
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            Log.d(TAG, "onDoubleTapEvent: ")
            return super.onDoubleTapEvent(e)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            Log.d(TAG, "onSingleTapUp: ")
            return super.onSingleTapUp(e)
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            Log.d(TAG, "onFling: ")
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        override fun onShowPress(e: MotionEvent) {
            Log.d(TAG, "onShowPress: ")
            super.onShowPress(e)
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            Log.d(TAG, "onSingleTapConfirmed: ")
            mVideoGestureListener?.onSingleTapGesture(e)
            return super.onSingleTapConfirmed(e)
        }
    }

    /**
     * 用于提供给外部实现的视频手势处理接口
     */
    interface VideoGestureListener {
        //亮度手势，手指在Layout左半部上下滑动时候调用
        fun onBrightnessGesture(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        )

        //音量手势，手指在Layout右半部上下滑动时候调用
        fun onVolumeGesture(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float)

        //快进快退手势，手指在Layout左右滑动的时候调用
        fun onFF_REWGesture(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float)

        //单击手势，确认是单击的时候调用
        fun onSingleTapGesture(e: MotionEvent?)

        //双击手势，确认是双击的时候调用
        fun onDoubleTapGesture(e: MotionEvent?)

        //按下手势，第一根手指按下时候调用
        fun onDown(e: MotionEvent?)

        //快进快退执行后的松开时候调用
        fun onEndFF_REW(e: MotionEvent?)
    }
}