package com.jiayx.flow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jiayx.flow.databinding.FragmentSharedFlowBinding

/**
 *Created by yuxi_
on 2022/4/1
 */
class FlowSharedFragment : Fragment() {
    private val binding: FragmentSharedFlowBinding by lazy {
        FragmentSharedFlowBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root
}