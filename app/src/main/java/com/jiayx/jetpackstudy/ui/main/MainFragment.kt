package com.jiayx.jetpackstudy.ui.main

import android.bluetooth.le.AdvertisingSetParameters
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiayx.jetpackstudy.adapter.MyAdapter
import com.jiayx.jetpackstudy.databinding.MainActivityBinding
import com.jiayx.jetpackstudy.databinding.MainFragmentBinding
import com.jiayx.jetpackstudy.room.bean.JStudentBean
import com.jiayx.jetpackstudy.room.bean.StudentBean

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val items: List<StudentBean> by lazy { arrayListOf() }
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(MainViewModel::class.java)
    }
    private val binding: MainFragmentBinding by lazy {
        MainFragmentBinding.inflate(layoutInflater)
    }
    private val adapter: MyAdapter by lazy {
        MyAdapter(requireContext(), items)
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
            val bean = StudentBean("小明", 20)
            val bean2 = StudentBean("王五", 24)
            viewModel.insertStudent(bean, bean2)
        }
        binding.buttonUpdate.setOnClickListener {
            val bean = StudentBean("赵柳", 24)
            viewModel.updateStudent(bean)
        }
        binding.buttonClear.setOnClickListener {
            viewModel.deleteStudent(StudentBean(2))
        }
        binding.buttonClearAll.setOnClickListener {
            viewModel.deleteAllStudent()
        }
        viewModel.getAllStudents()?.observe(viewLifecycleOwner, Observer {
            it?.let { items ->
                adapter.updateData(items)
            }
        })

    }
}