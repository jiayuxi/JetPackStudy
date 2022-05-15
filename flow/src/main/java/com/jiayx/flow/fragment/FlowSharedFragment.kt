package com.jiayx.flow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jiayx.flow.R
import com.jiayx.flow.databinding.FragmentSharedFlowBinding
import com.jiayx.flow.viewmodel.SharedFlowViewModel

/**
 *Created by yuxi_
on 2022/4/1
 */
class FlowSharedFragment : Fragment() {
    private val binding: FragmentSharedFlowBinding by lazy {
        FragmentSharedFlowBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<SharedFlowViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.flowSharedBtnStart.setOnClickListener {
            viewModel.startRefresh()
        }
        binding.flowSharedBtnStop.setOnClickListener {
            viewModel.stopRefresh()
        }

        binding.flowToTwo.setOnClickListener {
            findNavController().navigate(R.id.action_flowSharedFragment_to_flowValueFragment)
        }
    }
}