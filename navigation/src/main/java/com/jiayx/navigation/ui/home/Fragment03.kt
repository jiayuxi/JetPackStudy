package com.jiayx.navigation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jiayx.navigation.R
import com.jiayx.navigation.databinding.Fragment01Binding
import com.jiayx.navigation.databinding.Fragment03Binding

/**
 *Created by yuxi_
on 2022/4/19
 */
class Fragment03 : Fragment() {
    private val binding: Fragment03Binding by lazy {
        Fragment03Binding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 判断是否为空
        arguments?.let {
            val args = Fragment01Args.fromBundle(it)
            binding.textView2.text = "${args.userName} - ${args.age}"
        }
        binding.button4.setOnClickListener {
            findNavController().navigate(R.id.action_fragment03_to_fragment01)
        }
    }
}