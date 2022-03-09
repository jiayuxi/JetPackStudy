package com.jiayx.jetpackstudy.ui.main.paging

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.jiayx.jetpackstudy.adapter.PagingAdapter
import com.jiayx.jetpackstudy.databinding.PagingFragmentBinding
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
            this,
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
                binding.swipeRefresh.isRefreshing = state.refresh is LoadState.Loading
            }
        }
    }
}