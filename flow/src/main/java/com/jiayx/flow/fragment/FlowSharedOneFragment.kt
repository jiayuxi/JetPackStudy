package com.jiayx.flow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.expandVertically
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jiayx.flow.databinding.FragmentFlowSharedOneBinding
import com.jiayx.flow.utils.LocalEventBus

/**
 *Created by yuxi_
on 2022/4/1
 */
class FlowSharedOneFragment : Fragment() {
    private val binding: FragmentFlowSharedOneBinding by lazy {
        FragmentFlowSharedOneBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            LocalEventBus.events.collect { value ->
                binding.flowSharedOneText.text = "${value.timestamp}"
            }
        }
    }
}