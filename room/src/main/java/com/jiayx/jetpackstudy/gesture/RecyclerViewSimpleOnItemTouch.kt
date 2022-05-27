package com.jiayx.jetpackstudy.gesture

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import com.jiayx.jetpackstudy.R
import com.jiayx.jetpackstudy.interfaces.OnItemClickListener


/**
 *Created by yuxi_
on 2022/5/21
 */
class RecyclerViewSimpleOnItemTouch(
    private val mListener: OnItemClickListener,
    private val idList: List<Int>?
) :
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
        Log.d("jia_itemClick", "onRequestDisallowInterceptTouchEvent: $disallowIntercept")
    }

    private fun isTouchPointInView(view: View, x: Int, y: Int): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + view.measuredWidth
        val bottom = top + view.measuredHeight
        return view.isClickable && y >= top && y <= bottom && x >= left && x <= right
    }

    private fun isCatchID(viewGroup: ViewGroup, loopID: Int, e: MotionEvent): Boolean {
        if (idList?.size == 0) return false
        for (i in 0 until viewGroup.childCount) {
            val viewchild = viewGroup.getChildAt(i)
            val catchID = viewchild.id
            // 子控件的ID，
            val targetID = R.id.item_time
            val ageID = R.id.item_age
            if (isCheckId(catchID)) {
                val touches = viewchild.touchables
                touches?.forEach { tmpV ->
                    if (isTouchPointInView(tmpV, e.rawX.toInt(), e.rawY.toInt())) {
                        return true
                    }
                }
            } else {
                if (viewchild is ViewGroup) {
                    var id = loopID
                    if (isCatchID(viewchild, ++id, e)) {
                        return true
                    }
                }
            }

        }
        return false
    }

    private fun isCheckId(id: Int): Boolean {
        idList?:return false
        return idList.stream().filter {
            it == id
        }.findAny().isPresent
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
                        if (childView is ViewGroup) {
                            if (isCatchID(childView, 0, e)) {
                                Log.d(
                                    "jia_itemClick",
                                    "onSingleTapConfirmed ====>Catch need act id"
                                )
                                return false
                            }
                        }
                        if (childView != null) {
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
                        if (childView is ViewGroup) {
                            if (isCatchID(childView, 0, e)) {
                                Log.d("jia_itemClick", "onLongPress ====>Catch need act id")
                                return
                            }
                        }
                        if (childView != null) {
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
                            if (childView is ViewGroup) {
                                if (isCatchID(childView, 0, e)) {
                                    Log.d(
                                        "jia_itemClick",
                                        " onDoubleTapEvent ====>Catch need act id"
                                    )
                                    return false
                                }
                            }
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