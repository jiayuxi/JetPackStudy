package com.jiayx.jetpackstudy.ui.main.paging3

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jiayx.jetpackstudy.databinding.Paging3ActivityBinding
import com.jiayx.jetpackstudy.paging3.adapter.GalleryAdapter
import com.jiayx.jetpackstudy.paging3.footer.FooterAdapter
import com.jiayx.jetpackstudy.paging3.viewmodel.PagingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/5/29
 */
@OptIn(ExperimentalPagingApi::class)
@AndroidEntryPoint
class Paging3MediatorActivity : AppCompatActivity() {
    private val binding: Paging3ActivityBinding by lazy {
        Paging3ActivityBinding.inflate(layoutInflater)
    }
    private val galleryAdapter: GalleryAdapter by lazy {
        GalleryAdapter()
    }
    private val viewModel: PagingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.paging3Recycler.apply {
            adapter = galleryAdapter.withLoadStateFooter(FooterAdapter {
                galleryAdapter.retry()
            })
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllImages.collectLatest {
                    galleryAdapter.submitData(it)
                }
            }
        }
        binding.swipeRefresh.setOnRefreshListener {
            galleryAdapter.refresh()
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                galleryAdapter.loadStateFlow.collectLatest { state ->
                    when (state.refresh) {
                        is LoadState.NotLoading -> binding.swipeRefresh.isRefreshing = false
                        is LoadState.Error -> binding.swipeRefresh.isRefreshing = false
                        else -> {}
                    }
                }
            }
        }
    }
}