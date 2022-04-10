package com.jiayx.jetpackstudy.ui.main.room

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiayx.jetpackstudy.adapter.RoomAdapter
import com.jiayx.jetpackstudy.databinding.MainFragmentBinding
import com.jiayx.jetpackstudy.room.bean.StudentBean
import com.jiayx.jetpackstudy.ui.main.utils.transToString
import com.jiayx.jetpackstudy.ui.main.viewmodel.MainViewModel

class RoomFragment : Fragment() {

    companion object {
        fun newInstance() = RoomFragment()
    }

    private val items: List<StudentBean> by lazy { arrayListOf() }
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[MainViewModel::class.java]
    }
    private val binding: MainFragmentBinding by lazy {
        MainFragmentBinding.inflate(layoutInflater)
    }
    private val adapter: RoomAdapter by lazy {
        RoomAdapter(requireContext(), items)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initAction()
    }

    private fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun initAction() {
        binding.buttonInsert.setOnClickListener {
            val bean = StudentBean("小明", 20,System.currentTimeMillis())
            val bean2 = StudentBean("王五", 24,System.currentTimeMillis())
            viewModel.insertStudent(bean, bean2)
        }
        binding.buttonUpdate.setOnClickListener {
            Log.d("model_log", "initAction: time: ${System.currentTimeMillis()}")
            Log.d("model_log", "initAction: time: ${transToString(System.currentTimeMillis())}")
            viewModel.updateStudent(System.currentTimeMillis(),"小明")
        }
        binding.buttonClear.setOnClickListener {
            viewModel.deleteToName("小明")
        }
        binding.buttonClearAll.setOnClickListener {
            viewModel.deleteAllStudent()
        }
        lifecycleScope.launchWhenCreated {
            viewModel.getFlowAll()?.collect() {
                adapter.updateData(it)
            }
        }
    }
}