package com.jiayx.component.viewpage.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 *  author : Jia yu xi
 *  date : 2022/12/20 16:22:22
 *  description :
 *  Fragment 懒加载
 */
class LazyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    

}