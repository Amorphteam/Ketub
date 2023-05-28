package com.amorphteam.ketub.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemSearchBinding
import com.amorphteam.ketub.model.SearchModel
import com.amorphteam.ketub.utility.Keys

class SearchListAdapter(val clickListener: SearchClickListener) :
    ListAdapter<SearchModel, SearchListAdapter.ViewHolder>(SearchDiffCallback()) , Filterable {
    private var originalList: List<SearchModel> = emptyList()
    private var firstTime = true
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



    // ... rest of the code ...

    override fun submitList(list: List<SearchModel>?) {
        if (firstTime) {
            originalList = list ?: emptyList()
        }
        super.submitList(list)
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
                    values = if (constraint.isNullOrEmpty()) {
                        originalList
                    }
                    else {
                        onFilter(originalList, constraint.toString())
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                firstTime = false

                submitList(results?.values as? List<SearchModel>)

            }
        }
    }

    fun onFilter(list: List<SearchModel>, constraint: String): List<SearchModel> {
        val filteredList = list.filter {
            it.bookTitle?.lowercase()!!.contains(constraint.lowercase())
        }

        return filteredList
    }

}

class SearchDiffCallback() : DiffUtil.ItemCallback<SearchModel>() {

    override fun areItemsTheSame(p0: SearchModel, p1: SearchModel): Boolean {
        return p0.pageId == p1.pageId
    }

    override fun areContentsTheSame(p0: SearchModel, p1: SearchModel): Boolean {
        return p0 == p1
    }

}

class SearchClickListener(val clickListener: (searchData: SearchModel) -> Unit) {
    fun onClick(searchData: SearchModel) = clickListener(searchData)

}

