package com.jiayx.flow.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jiayx.flow.databinding.FragmentFlowSharedOneBinding
import com.jiayx.flow.utils.LocalEventBus
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/4/1
 SharedFlow 共享的数据流
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

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                LocalEventBus.collectionFlow.collect { value ->
                    Log.d("state_flow_log", "one - onViewCreated: sharedFlow 监听start数据")
                    binding.flowSharedOneText.text = "${value.timestamp}"
                }
                //todo StateFlow订阅者所在的协程，最好使用独立协程，collect会一直挂起，协程内的后续操作不会执行
//                Log.d("state_flow_log", "onViewCreated: sharedFlow 监听end数据")
            }
        }
    }
}