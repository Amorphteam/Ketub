package com.amorphteam.ketub.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemSearchBinding
import com.amorphteam.ketub.model.SearchModel
import com.amorphteam.ketub.utility.TempData

class SearchListAdapter(val clickListener: SearchClickListener) :
    ListAdapter<SearchModel, SearchListAdapter.ViewHolder>(DiffCallback()) , Filterable {

    class ViewHolder private constructor(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchModel, clickListener: SearchClickListener) {
            binding.item = item
            binding.searchClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemSearchBinding.inflate(layoutInflater, parent, false)
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

                        TempData.searchResult
                    else
                        onFilter(TempData.searchResult, constraint.toString())
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as? List<SearchModel>)

            }
        }
    }

    fun onFilter(list: List<SearchModel>, constraint: String): List<SearchModel> {
        val filteredList = list.filter {
            it.bookName.lowercase().contains(constraint.lowercase())
        }

        return filteredList
    }

}

class DiffCallback() : DiffUtil.ItemCallback<SearchModel>() {

    override fun areItemsTheSame(p0: SearchModel, p1: SearchModel): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: SearchModel, p1: SearchModel): Boolean {
        return p0 == p1
    }

}

class SearchClickListener(val clickListener: (Id: Int) -> Unit) {
    fun onClick(searchData: SearchModel) = clickListener(searchData.id)

}

