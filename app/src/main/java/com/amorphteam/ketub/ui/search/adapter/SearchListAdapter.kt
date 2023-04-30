package com.amorphteam.ketub.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemSearchBinding
import com.amorphteam.ketub.ui.search.model.SearchModel

class SearchListAdapter(val clickListener: SearchClickListener) :
    ListAdapter<SearchModel, SearchListAdapter.ViewHolder>(DiffCallback()) {

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

