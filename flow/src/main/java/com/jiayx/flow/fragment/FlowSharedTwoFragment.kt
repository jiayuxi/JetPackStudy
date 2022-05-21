package com.jiayx.flow.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jiayx.flow.databinding.FragmentFlowSharedOneBinding
import com.jiayx.flow.utils.LocalEventBus

/**
 *Created by yuxi_
on 2022/4/1
 SharedFlow 共享的数据流
 */
class FlowSharedTwoFragment : Fragment() {
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
            Log.d("state_flow_log", "onViewCreated: sharedFlow 监听start数据")
            LocalEventBus.collectionFlow.collect { value ->
                binding.flowSharedOneText.text = "${value.timestamp}"
            }
            //todo StateFlow订阅者所在的协程，最好使用独立协程，collect会一直挂起，协程内的后续操作不会执行
            Log.d("state_flow_log", "onViewCreated: sharedFlow 监听end数据")
        }
    }
}