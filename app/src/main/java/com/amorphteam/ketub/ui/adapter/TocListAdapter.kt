package com.amorphteam.ketub.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemTreeTocBinding
import com.amorphteam.ketub.model.IndexesInfo

class TocListAdapter (val listener:EmptyTocListener , private val clickListener: TocListItemClickListener) :
ListAdapter<IndexesInfo, TocListAdapter.ViewHolder>(TocListDiffCallback()), Filterable {


    class ViewHolder private constructor(val binding: ItemTreeTocBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: IndexesInfo, clickListener: TocListItemClickListener) {
            binding.rowText.text = item.navPoint.navLabel
            binding.bookNameText.text = item.bookTitle
//            binding.itemClickListener = clickListener
            binding.executePendingBindings()
        }



        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemTreeTocBinding.inflate(layoutInflater, parent, false)
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

                    listener.onEmptyListReceived()

                    else
                        onFilter(currentList, constraint.toString())
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as? List<IndexesInfo>)

            }
        }
    }

    fun onFilter(list: List<IndexesInfo>, constraint: String): List<IndexesInfo> {
        val filteredList = list.filter {
            it.navPoint.navLabel.lowercase().contains(constraint.lowercase())
        }

        return filteredList
    }
}

class TocListDiffCallback() : DiffUtil.ItemCallback<IndexesInfo>() {

    override fun areItemsTheSame(p0: IndexesInfo, p1: IndexesInfo): Boolean {
        return p0 == p1
    }

    override fun areContentsTheSame(p0: IndexesInfo, p1: IndexesInfo): Boolean {
        return p0 == p1
    }

}

class TocListItemClickListener(val clickListener: (indexesInfo: IndexesInfo) -> Unit) {
    fun onClick(indexesInfo: IndexesInfo) = clickListener(indexesInfo)

}


interface EmptyTocListener{
    fun onEmptyListReceived()
}




