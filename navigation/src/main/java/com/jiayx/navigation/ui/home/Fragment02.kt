package com.jiayx.navigation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jiayx.navigation.R
import com.jiayx.navigation.databinding.Fragment01Binding
import com.jiayx.navigation.databinding.Fragment02Binding

/**
 *Created by yuxi_
on 2022/4/19
 */
// Type mismatch: inferred type is Bundle? but Bundle was expected  类型转换错误
// 要对 参数进行是否 为 null 的判断
class Fragment02 : Fragment() {
    private val binding: Fragment02Binding by lazy {
        Fragment02Binding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // requireArguments 判断 是否为空
        val args = Fragment01Args.fromBundle(requireArguments())
        binding.textView.text = "${args?.userName} - ${args?.age}"
        binding.button3.setOnClickListener {
//            findNavController().navigate(R.id.action_fragment02_to_fragment01)
            findNavController().popBackStack()
        }
    }
}