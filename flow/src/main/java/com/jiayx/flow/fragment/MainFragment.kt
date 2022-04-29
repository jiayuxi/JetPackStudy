package com.jiayx.flow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jiayx.flow.R
import com.jiayx.flow.databinding.FragmentMainBinding

/**
 *Created by yuxi_
on 2022/4/1
 */
class MainFragment : Fragment() {
    private val binding: FragmentMainBinding by lazy {
        FragmentMainBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
    }

    private fun initAction() {
        binding.buttonFlowDownload.setOnClickListener {
            findNavController(R.id.action_mainFragment_to_flowDownloadFragment)
        }
        binding.buttonFlowRetrofit.setOnClickListener {
            findNavController(R.id.action_mainFragment_to_flowRetrofitFragment)
        }
        binding.buttonFlowState.setOnClickListener {
            findNavController(R.id.action_mainFragment_to_flowStateFragment)
        }
        binding.buttonFlowShared.setOnClickListener {
            findNavController(R.id.action_mainFragment_to_flowSharedFragment)
        }
        binding.buttonException.setOnClickListener {
            findNavController(R.id.action_mainFragment_to_coroutineExceptionFragment)
        }
        binding.buttonFlowState01.setOnClickListener {
            findNavController(R.id.action_mainFragment_to_flowStateFragment1)
        }
    }

    private fun findNavController(id: Int) {
        findNavController().navigate(id)
    }
}