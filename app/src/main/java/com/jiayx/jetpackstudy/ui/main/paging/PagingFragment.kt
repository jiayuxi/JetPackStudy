package com.jiayx.jetpackstudy.ui.main.paging

import android.bluetooth.le.AdvertisingSetParameters
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiayx.jetpackstudy.R
import com.jiayx.jetpackstudy.adapter.PagingAdapter
import com.jiayx.jetpackstudy.databinding.PagingFragmentBinding
import com.jiayx.jetpackstudy.room.bean.Person
import com.jiayx.jetpackstudy.ui.main.viewmodel.PersonViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 *Created by yuxi_
on 2022/3/8
 */
class PagingFragment : Fragment() {
    private val binding: PagingFragmentBinding by lazy {
        PagingFragmentBinding.inflate(layoutInflater)
    }
    private val adapter: PagingAdapter by lazy {
        PagingAdapter(requireContext())
    }

    private val pagingViewModel: PersonViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(PersonViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initAction()
    }

    private fun initView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter
            swipeRefresh.setOnRefreshListener {
                adapter.refresh()
            }
        }
    }

    private fun initAction() {
        lifecycleScope.launchWhenCreated {
            pagingViewModel.allPerson.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { state ->
                when (state.refresh) {
                    is LoadState.NotLoading -> binding.swipeRefresh.isRefreshing = false
                    is LoadState.Error -> binding.swipeRefresh.isRefreshing = false
                    else -> {}
                }
            }
        }
        //设置点击事件
        adapter.setOnItemClickListener { adapter, _, position ->
            val data = adapter.getData(position) as Person
            data?.let { person ->
                pagingViewModel.updatePerson(Person(person.id, person.name, !person.isSelect))
            }
        }
        pagingViewModel.manageListener = {
            updateManage()
        }
        lifecycleScope.launchWhenCreated {
            pagingViewModel.getSelectCount().collectLatest {
                Log.d("count_log", "initAction: count : $it")
                binding.pagingDelete.isEnabled = it > 0
            }
        }
        binding.pagingDelete.setOnClickListener {
            pagingViewModel.deleteSelect()
        }
    }

    private fun updateManage() {
        adapter.isDelete = !adapter.isDelete
        if (!adapter.isDelete) pagingViewModel.updateAll(false)
        binding.pagingBottomLinear.visibility = if (adapter.isDelete) VISIBLE else GONE
        adapter.notifyDataSetChanged()
    }
}