package com.jiayx.jetpackstudy.paging3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jiayx.jetpackstudy.databinding.ItemRvArticleBinding
import com.jiayx.jetpackstudy.paging3.model.ArticleData


/**
 * @date：2021/5/21
 * @author fuusy
 * @instruction： 首页多typeAdapter，包含Banner和文章列表
 */
class ArticleMultiPagingAdapter :
    PagingDataAdapter<ArticleData, RecyclerView.ViewHolder>(differCallback) {

    companion object {
        const val TYPE_ARTICLE = 1

        val differCallback = object : DiffUtil.ItemCallback<ArticleData>() {
            override fun areItemsTheSame(oldItem: ArticleData, newItem: ArticleData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArticleData, newItem: ArticleData): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            (holder as ArticleVH).bindData(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val articleVH =
            ArticleVH(
                ItemRvArticleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        articleVH.itemView.setOnClickListener {
            val data = getItem(articleVH.layoutPosition)
        }
        return articleVH
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_ARTICLE
    }

    internal class ArticleVH(val binding: ItemRvArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //绑定文章列表
        fun bindData(data: ArticleData) {
            setText(binding.tvArticleTitle, data.title)
            setText(binding.btHealthInfoType, data.superChapterName)
            setText(binding.tvHomeInfoTime, data.niceDate)

            if (data.author.isEmpty()) {
                setText(binding.tvArticleAuthor, data.shareUser)
            } else {
                setText(binding.tvArticleAuthor, data.author)
            }
        }

        private fun setText(view: TextView, text: String) {
            view.text = text
        }
    }
}