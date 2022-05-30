package com.jiayx.jetpackstudy.ui.main.paging3

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.jiayx.jetpackstudy.databinding.ArticleActivityBinding
import com.jiayx.jetpackstudy.paging3.adapter.ArticleMultiPagingAdapter
import com.jiayx.jetpackstudy.paging3.footer.FooterAdapter
import com.jiayx.jetpackstudy.paging3.viewmodel.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 *Created by yuxi_
on 2022/5/30
 */
@AndroidEntryPoint
class ArticleActivity : AppCompatActivity() {
    private val binding: ArticleActivityBinding by lazy {
        ArticleActivityBinding.inflate(layoutInflater)
    }
    private val viewModel: ArticleViewModel by viewModels()
    private val mArticlePagingAdapter: ArticleMultiPagingAdapter by lazy {
        ArticleMultiPagingAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            rvHomeArticle.adapter =
                mArticlePagingAdapter.withLoadStateFooter(FooterAdapter {
                    mArticlePagingAdapter.retry()
                })
            swipeLayout?.setOnRefreshListener {
                mArticlePagingAdapter.refresh()
            }
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.articlePagingFlow().collectLatest {
                        mArticlePagingAdapter.submitData(it)
                        binding.swipeLayout.isRefreshing = false
                    }
                }
            }
            lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    mArticlePagingAdapter.loadStateFlow.collectLatest { state ->
                        when (state.refresh) {
                            is LoadState.NotLoading -> binding.swipeLayout.isRefreshing = false
                            is LoadState.Error -> binding.swipeLayout.isRefreshing = false
                            else -> {}
                        }
                    }
                }
            }
        }

    }
}