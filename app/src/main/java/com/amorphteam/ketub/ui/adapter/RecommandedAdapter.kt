package com.amorphteam.ketub.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemBookmarkBinding
import com.amorphteam.ketub.databinding.ItemRecommandedBinding
import com.amorphteam.ketub.model.RecommandedTocModel

class RecommandedAdapter(
    private val clickListener: RecItemClickListener,
) : ListAdapter<RecommandedTocModel, RecommandedAdapter.ViewHolder>(RecommandedDiffCallback()), Filterable {

    private var originalList: List<RecommandedTocModel> = emptyList()

    class ViewHolder private constructor(val binding: ItemRecommandedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: RecommandedTocModel,
            clickListener: RecItemClickListener,
        ) {
            binding.item = item
            binding.itemClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRecommandedBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                if (constraint.isNullOrEmpty()) {
                    results.values = originalList.toList()
                } else {
                    results.values = onFilter(originalList, constraint.toString())
                }
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val filteredList = results?.values as? List<RecommandedTocModel> ?: emptyList()
                submitList(filteredList)
            }
        }
    }

    fun onFilter(list: List<RecommandedTocModel>, constraint: String): List<RecommandedTocModel> {
        val filteredList = list.filter {
            it.title!!.lowercase().contains(constraint.lowercase()) || it.bookName!!.lowercase().contains(constraint.lowercase())
        }
        return filteredList
    }

    fun setData(data: List<RecommandedTocModel>) {
        originalList = data
        submitList(data)
    }
}

class RecommandedDiffCallback() : DiffUtil.ItemCallback<RecommandedTocModel>() {

    override fun areItemsTheSame(p0: RecommandedTocModel, p1: RecommandedTocModel): Boolean {
        return p0.id == p1.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(p0: RecommandedTocModel, p1: RecommandedTocModel): Boolean {
        return p0 == p1
    }

}

class RecItemClickListener(val clickListener: (RecommandedTocModel: RecommandedTocModel) -> Unit) {
    fun onClick(bookmarkData: RecommandedTocModel) = clickListener(bookmarkData)

}
