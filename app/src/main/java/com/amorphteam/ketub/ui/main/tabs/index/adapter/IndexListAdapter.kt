package com.amorphteam.ketub.ui.main.tabs.index.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemIndexBinding
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexModel
import com.amorphteam.ketub.utility.TempData

class IndexListAdapter(val clickListener: IndexClickListener) :
    ListAdapter<IndexModel, IndexListAdapter.ViewHolder>(DiffCallback()), Filterable {

    class ViewHolder private constructor(val binding: ItemIndexBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: IndexModel, clickListener: IndexClickListener) {
            binding.item = item
            binding.indexClickListener = clickListener
            //TODO: IT MUST LOAD FROM VIEWMODEL
            binding.bookTitle.text = item.bookTitle
            binding.bookName.text = item.bookName
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemIndexBinding.inflate(layoutInflater, parent, false)
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
                return FilterResults().apply {
                    values = if (constraint.isNullOrEmpty())
                    //TODO: IT MUST LOAD FROM VIEWMODEL

                    TempData.bookIndex
                    else
                        onFilter(TempData.bookIndex, constraint.toString())
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as? List<IndexModel>)

            }
        }
    }

    fun onFilter(list: List<IndexModel>, constraint: String): List<IndexModel> {
        val filteredList = list.filter {
            it.bookTitle.lowercase().contains(constraint.lowercase())
        }

        return filteredList
    }
}

class DiffCallback() : DiffUtil.ItemCallback<IndexModel>() {

    override fun areItemsTheSame(p0: IndexModel, p1: IndexModel): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: IndexModel, p1: IndexModel): Boolean {
        return p0 == p1
    }

}

class IndexClickListener(val clickListener: (Id: Int) -> Unit) {
    fun onClick(indexData: IndexModel) = clickListener(indexData.id)

}