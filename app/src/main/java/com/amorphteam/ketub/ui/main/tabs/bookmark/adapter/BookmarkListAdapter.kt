package com.amorphteam.ketub.ui.main.tabs.bookmark.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemBookmarkBinding
import com.amorphteam.ketub.ui.main.tabs.bookmark.model.BookmarkModel
import com.amorphteam.ketub.utility.TempData

class BookmarkListAdapter(val clickListener: BookmarkClickListener, val bookmarkDeleteclickListener: BookmarkDeleteClickListener) :
    ListAdapter<BookmarkModel, BookmarkListAdapter.ViewHolder>(DiffCallback()), Filterable {

    class ViewHolder private constructor(val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookmarkModel, clickListener: BookmarkClickListener, bookmarkDeleteclickListener: BookmarkDeleteClickListener ) {
            binding.item = item
            binding.bookmarkClickListener = clickListener
            binding.bookmarkDeleteClickListener = bookmarkDeleteclickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemBookmarkBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, bookmarkDeleteclickListener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults().apply {
                    values = if (constraint.isNullOrEmpty())
                    //TODO: IT MUST LOAD FROM VIEWMODEL

                        TempData.bookMarkArray
                    else
                        onFilter(TempData.bookMarkArray, constraint.toString())
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as? List<BookmarkModel>)

            }
        }
    }

    fun onFilter(list: List<BookmarkModel>, constraint: String): List<BookmarkModel> {
        val filteredList = list.filter {
            it.bookTitle.lowercase().contains(constraint.lowercase())
        }

        return filteredList
    }
}

class DiffCallback() : DiffUtil.ItemCallback<BookmarkModel>() {

    override fun areItemsTheSame(p0: BookmarkModel, p1: BookmarkModel): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: BookmarkModel, p1: BookmarkModel): Boolean {
        return p0 == p1
    }

}

class BookmarkClickListener(val clickListener: (Id: Int) -> Unit) {
    fun onClick(bookmarkData: BookmarkModel) = clickListener(bookmarkData.id)

}

class BookmarkDeleteClickListener(val clickListener: (Id: Int) -> Unit) {
    fun onClick(bookmarkData: BookmarkModel) = clickListener(bookmarkData.id)

}