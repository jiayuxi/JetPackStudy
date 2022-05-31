package com.jiayx.jetpackstudy.ui.main.paging3

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.jiayx.jetpackstudy.databinding.PokemonActivityBinding
import com.jiayx.jetpackstudy.paging3.adapter.PokemonAdapter
import com.jiayx.jetpackstudy.paging3.footer.FooterAdapter
import com.jiayx.jetpackstudy.paging3.viewmodel.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

/**
 *Created by yuxi_
on 2022/5/31
 */
@AndroidEntryPoint
class PokemonActivity : AppCompatActivity() {
    private val binding: PokemonActivityBinding by lazy {
        PokemonActivityBinding.inflate(layoutInflater)
    }
    private val viewModel: PokemonViewModel by viewModels()
    private val mPokemonAdapter by lazy { PokemonAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            recyleView.adapter = mPokemonAdapter.withLoadStateFooter(FooterAdapter() {
                mPokemonAdapter.retry()
            })
            lifecycleOwner = this@PokemonActivity
        }

        lifecycleScope.launchWhenCreated {
            mPokemonAdapter.loadStateFlow.collectLatest { state ->
                binding.swiperRefresh.isRefreshing = state.refresh is LoadState.Loading
            }
        }
        viewModel.postOfData().observe(this, Observer {
            mPokemonAdapter.submitData(lifecycle, it)
            binding.swiperRefresh.isRefreshing = false
        })
    }
}