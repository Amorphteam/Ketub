package com.amorphteam.ketub.ui.main.tabs.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemMainTocBinding
import com.amorphteam.ketub.ui.main.tabs.library.model.MainToc

class MainTocAdapter(val clickListener: MainTocClickListener) : ListAdapter<MainToc, MainTocAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder private constructor(val binding: ItemMainTocBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MainToc, clickListener: MainTocClickListener) {
            binding.item = item
            binding.mainTocClickListener = clickListener
            binding.title.text = item.title
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemMainTocBinding.inflate(layoutInflater, parent, false)
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
class DiffCallback() : DiffUtil.ItemCallback<MainToc>() {

    override fun areItemsTheSame(p0: MainToc, p1: MainToc): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: MainToc, p1: MainToc): Boolean {
        return p0 == p1
    }

}

class MainTocClickListener(val clickListener: (Id: Int) -> Unit) {
    fun onClick(tocData: MainToc) = clickListener(tocData.id)

}