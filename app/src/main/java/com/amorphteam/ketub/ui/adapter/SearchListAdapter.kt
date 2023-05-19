package com.amorphteam.ketub.ui.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemSearchBinding
import com.amorphteam.ketub.model.SearchInfoHolder
import com.amorphteam.ketub.model.SearchModel
import com.amorphteam.ketub.utility.Keys
import com.amorphteam.ketub.utility.TempData

class SearchListAdapter(val clickListener: SearchClickListener) :
    ListAdapter<SearchInfoHolder, SearchListAdapter.ViewHolder>(DiffCallback()) , Filterable {
    private var originalList: List<SearchInfoHolder> = emptyList()
    private var firstTime = true
    class ViewHolder private constructor(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SearchInfoHolder, clickListener: SearchClickListener) {
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

    override fun submitList(list: List<SearchInfoHolder>?) {
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
                        Log.i(Keys.LOG_NAME, originalList.size.toString())
                    }
                    else {
                        onFilter(originalList, constraint.toString())
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                firstTime = false
                Log.i(Keys.LOG_NAME, "onFilter: is ${(results?.values as? List<SearchInfoHolder>)?.size}")

                submitList(results?.values as? List<SearchInfoHolder>)

            }
        }
    }

    fun onFilter(list: List<SearchInfoHolder>, constraint: String): List<SearchInfoHolder> {
        val filteredList = list.filter {
            it.bookTitle?.lowercase()!!.contains(constraint.lowercase())
        }

        return filteredList
    }

}

class DiffCallback() : DiffUtil.ItemCallback<SearchInfoHolder>() {

    override fun areItemsTheSame(p0: SearchInfoHolder, p1: SearchInfoHolder): Boolean {
        return p0.pageId == p1.pageId
    }

    override fun areContentsTheSame(p0: SearchInfoHolder, p1: SearchInfoHolder): Boolean {
        return p0 == p1
    }

}

class SearchClickListener(val clickListener: (Id: Int) -> Unit) {
    fun onClick(searchData: SearchInfoHolder) = clickListener(searchData.pageId!!.toInt())

}

