package com.jiayx.flow.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.geometry.Offset
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jiayx.flow.databinding.FragmentFlowRetrofitBinding
import com.jiayx.flow.viewmodel.ArticleViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/4/1
 flow 冷流
 */
class FlowRetrofitFragment : Fragment() {
    private val binding: FragmentFlowRetrofitBinding by lazy {
        FragmentFlowRetrofitBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<ArticleViewModel>()
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
        // 测试参数
        lifecycleScope.launchWhenCreated {
            // flow 是冷流，如果Flow有了订阅者Collector以后，发射出来的值，才会实实在在的存在与内存之中，这根懒加载的概念很像。
            flow {
                for (i in 1..5) {
                    kotlinx.coroutines.delay(500)
                    emit(i)
                }
            }.flowOn(Dispatchers.IO)
                .catch { e -> e.printStackTrace() }
                .collect {
                    Log.d("flow_retrofit_log", "initAction: 收集到的参数：$it")
                }
        }

        lifecycleScope.launchWhenCreated {
            binding.flowRetrofitEdit.textWatcherFlow().collect {
                Log.d("flow_retrofit_log", "collect keywords：$it")
                viewModel.searchArticle(it)
            }
        }
        /**
         * 多个edit 监听互不影响
         */
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                binding.flowOtherEdit.textWatcherFlow().collect{
                    Log.d("flow_retrofit_log", "other collect keywords：$it")
                }
            }
        }
        context?.let {
            viewModel.articles.observe(viewLifecycleOwner) { articles ->
                Log.d("flow_retrofit_log", "initAction: $articles")
                binding.flowRetrofitText.text = articles.toString()
            }
        }
    }

    /**
     * 获取关键字
     */
    private fun TextView.textWatcherFlow(): Flow<String> = callbackFlow {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                trySend(p0.toString()).isSuccess
            }
        }
        addTextChangedListener(textWatcher)
        awaitClose {
            removeTextChangedListener(textWatcher)
        }
    }
}