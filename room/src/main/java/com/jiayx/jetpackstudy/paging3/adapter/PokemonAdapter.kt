package com.jiayx.jetpackstudy.paging3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.jiayx.jetpackstudy.R
import com.jiayx.jetpackstudy.binding.DataBindingViewHolder
import com.jiayx.jetpackstudy.databinding.RecycleItemPokemonBinding
import com.jiayx.jetpackstudy.paging3.model.PokemonItemModel
import com.jiayx.jetpackstudy.ui.main.utils.doWithTry

class PokemonAdapter :
    PagingDataAdapter<PokemonItemModel, PokemonViewModel>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PokemonItemModel>() {
            override fun areItemsTheSame(
                oldItem: PokemonItemModel,
                newItem: PokemonItemModel
            ): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(
                oldItem: PokemonItemModel,
                newItem: PokemonItemModel
            ): Boolean =
                oldItem == newItem
        }
    }
    override fun onBindViewHolder(holder: PokemonViewModel, position: Int) {
        doWithTry {
            val data = getItem(position)
            data?.let {
                holder.bindData(data, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewModel {
        val view = inflateView(parent, R.layout.recycle_item_pokemon)
        return PokemonViewModel(view)
    }

    private fun inflateView(viewGroup: ViewGroup, @LayoutRes viewType: Int): View {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return layoutInflater.inflate(viewType, viewGroup, false)
    }
}

class PokemonViewModel(view: View) : DataBindingViewHolder<PokemonItemModel>(view) {
    private val mBinding: RecycleItemPokemonBinding by viewHolderBinding(view)

    override fun bindData(data: PokemonItemModel, position: Int) {
        mBinding.apply {
            data.id = "#${position + 1}"
            pokemon = data
            executePendingBindings()
        }
    }

}