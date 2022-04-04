package com.jiayx.flow.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.jiayx.coroutinesCamp.log
import com.jiayx.flow.databinding.FragmentExceptionBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/4/4
 */
class CoroutineExceptionFragment : Fragment() {
    private val binding:FragmentExceptionBinding by lazy {
        FragmentExceptionBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val handler = CoroutineExceptionHandler { _, throwable ->
            Log.d("exception_log", "Caught : $throwable")
        }
        binding.exceptionBtn.setOnClickListener {
             viewLifecycleOwner.lifecycleScope.launch(handler){
                 throw  IllegalAccessException("抛出异常")
             }
        }
    }
}