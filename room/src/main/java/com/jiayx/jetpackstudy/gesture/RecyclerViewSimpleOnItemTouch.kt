package com.jiayx.jetpackstudy.gesture

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import com.jiayx.jetpackstudy.interfaces.OnItemClickListener


/**
 *Created by yuxi_
on 2022/5/21
 */
class RecyclerViewSimpleOnItemTouch(private val mListener: OnItemClickListener) :
    RecyclerView.SimpleOnItemTouchListener() {
    private var mGestureDetector: GestureDetectorCompat? = null

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        if (mGestureDetector == null) {
            initGestureDetector(rv)
        }
         mGestureDetector?.onTouchEvent(e)
        return super.onInterceptTouchEvent(rv, e)
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        super.onTouchEvent(rv, e)
        mGestureDetector?.onTouchEvent(e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        super.onRequestDisallowInterceptTouchEvent(disallowIntercept)
    }

    private fun initGestureDetector(recyclerView: RecyclerView) {
        mGestureDetector = GestureDetectorCompat(recyclerView.context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent?): Boolean {
                    return true
                }

                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    e?.let {
                        val childView = recyclerView.findChildViewUnder(e.x, e.y)
                        if (childView != null && mListener != null) {
                            mListener.onItemClick(
                                childView,
                                recyclerView.getChildLayoutPosition(childView)
                            )
                            return true
                        }
                    }
                    return super.onSingleTapConfirmed(e)
                }

                override fun onLongPress(e: MotionEvent?) {
                    super.onLongPress(e)
                    e?.let {
                        val childView = recyclerView.findChildViewUnder(e.x, e.y)
                        if (childView != null && mListener != null) {
                            mListener.onItemLongClick(
                                childView,
                                recyclerView.getChildLayoutPosition(childView)
                            )
                        }
                    }
                }

                override fun onDoubleTap(e: MotionEvent?): Boolean {
                    return super.onDoubleTap(e)
                }

                override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
                    e?.let {
                        val action = e.action
                        if (action == MotionEvent.ACTION_UP) {
                            val childView = recyclerView.findChildViewUnder(e.x, e.y)
                            if (childView != null && mListener != null) {
                                mListener.onItemDoubleClick(
                                    childView,
                                    recyclerView.getChildLayoutPosition(childView)
                                )
                                return true
                            }
                        }
                    }
                    return super.onDoubleTapEvent(e)
                }
            })
    }
}