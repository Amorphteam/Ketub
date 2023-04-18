package com.amorphteam.ketub.ui.main.tabs.index.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemIndexBinding
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexModel

class RVIndexListAdapter(val clickListener: IndexClickListener) :
    ListAdapter<IndexModel, RVIndexListAdapter.ViewHolder>(DiffCallback()) {

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