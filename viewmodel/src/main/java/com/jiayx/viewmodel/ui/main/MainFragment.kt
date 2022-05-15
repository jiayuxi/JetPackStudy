package com.jiayx.viewmodel.ui.main

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import com.jiayx.viewmodel.databinding.MainFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.StringBuilder

class MainFragment : Fragment() {
    // 在 Fragment 之间共享数据
    //请注意，这两个 Fragment 都会检索包含它们的 Activity。这样，
    // 当这两个 Fragment 各自获取 ViewModelProvider 时，
    // 它们会收到相同的 SharedViewModel 实例（其范围限定为该 Activity）。
    // todo 此方法具有以下优势：
    //Activity 不需要执行任何操作，也不需要对此通信有任何了解。
    //除了 SharedViewModel 约定之外，Fragment 不需要相互了解。如果其中一个 Fragment 消失，另一个 Fragment 将继续照常工作。
    //每个 Fragment 都有自己的生命周期，而不受另一个 Fragment 的生命周期的影响。如果一个 Fragment 替换另一个 Fragment，界面将继续工作而没有任何问题。
    private val viewModel: MainViewModel by activityViewModels()

    private val binding: MainFragmentBinding by lazy {
        MainFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // todo 声明周期感知协程范围
        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            val stringBinding = StringBuilder()
            it?.let { list ->
                list.forEach { user ->
                    stringBinding.append(user.toString() + "\r\n")
                }
            }
//            binding.message.text = stringBinding.toString()
        })
        viewLifecycleOwner.lifecycleScope.launch {
            val params = TextViewCompat.getTextMetricsParams(binding.message)
            val precomputedText = withContext(Dispatchers.Default) {
                PrecomputedTextCompat.create("123456", params)
            }
            TextViewCompat.setPrecomputedText(binding.message, precomputedText)
        }
        //todo 可重启生命周期感知型协程
        // 第一种方式
        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collect {
                binding.message.text = "$it"
            }
        }
        // 第二种方式
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect {
                    binding.message.text = "$it"
                }
            }
        }
        // 第三种方式
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect {
                    binding.message.text = "$it"
                }
            }
        }
        //todo 生命周期感知型数据流收集
        //如果您只需要对单个数据流执行生命周期感知型收集，可以使用 Flow.flowWithLifecycle() 方法简化代码：
        // 第四种方式
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                binding.message.text = "$it"
            }
        }
        // 第五种方式
        viewModel.stateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                binding.message.text = "$it"
            }.launchIn(lifecycleScope)//运行在主线程的协程作用域，在视图销毁时自动取消作用域
        // todo
        // 但是，如果您需要并行对多个数据流执行生命周期感知型收集，则必须在不同的协程中收集每个数据流。
        // 在这种情况下，直接使用 repeatOnLifecycle() 会更加高效：
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.stateFlow.collect {

                    }
                }
                launch {
                    viewModel.stateFlow.collect {

                    }
                }
            }
        }
        //todo 挂起生命周期感知型协程
        lifecycleScope.launch {
            whenCreated {
                Log.d("scope_log", "whenCreated: ")
            }
            whenStarted {
                Log.d("scope_log", "whenStarted: ")
            }
            whenResumed {
                Log.d("scope_log", "whenResumed: ")
            }
        }
        //如果在协程处于活动状态时通过某种 when 方法销毁了 Lifecycle，协程会自动取消。
        // 在以下示例中，一旦 Lifecycle 状态变为 DESTROYED，finally 块即会运行：
        lifecycleScope.launchWhenStarted {
            try {
                // Call some suspend functions.
            } finally {
                // This line might execute after Lifecycle is DESTROYED.
                if (lifecycle.currentState >= Lifecycle.State.STARTED) {
                    // Here, since we've checked, it is safe to run any
                    // Fragment transactions.
                }
            }
        }
        //注意：尽管这些方法为使用 Lifecycle 提供了便利，但只有当信息在 Lifecycle
        // 的范围（例如预计算文本）内有效时才应使用它们。请注意，协程不会随着 activity 重启而重启。
        //todo 警告：倾向于使用 repeatOnLifecycle API 收集数据流，
        // 而不是在 launchWhenX API 内部进行收集。由于后面的 API 会挂起协程，
        // 而不是在 Lifecycle 处于 STOPPED 状态时取消。上游数据流会在后台保持活跃状态，并可能会发出新的项并耗用资源。

        // liveData 与 协程
        viewModel.getLiveDataValue().observe(viewLifecycleOwner, Observer {
            binding.message.text = "$it"
            Log.d("scope_log", "live data : $it")
        })

    }

}