package com.jiayx.jetpackstudy.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jiayx.jetpackstudy.databinding.PagingFragmentBinding

/**
 *Created by yuxi_
on 2022/3/8
 */
class PagingFragment : Fragment() {
    private val binding: PagingFragmentBinding by lazy {
        PagingFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}