package com.amorphteam.ketub.ui.epub.fragments.bookmark.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemBookmarkSingleBinding
import com.amorphteam.ketub.model.ReferenceModel
import com.amorphteam.ketub.utility.TempData

class BookmarkSingleAdapter(val clickListener: BookmarkSingleClickListener, val bookmarkDeleteclickListener: BookmarkDeleteSingleClickListener) :
    ListAdapter<ReferenceModel, BookmarkSingleAdapter.ViewHolder>(DiffCallback()), Filterable {

    class ViewHolder private constructor(val binding: ItemBookmarkSingleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReferenceModel, clickListener: BookmarkSingleClickListener, bookmarkDeleteSingleclickListener: BookmarkDeleteSingleClickListener) {
            binding.item = item
            binding.bookmarkSingleClickListener = clickListener
            binding.bookmarkDeleteSingleClickListener = bookmarkDeleteSingleclickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemBookmarkSingleBinding.inflate(layoutInflater, parent, false)
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
                submitList(results?.values as? List<ReferenceModel>)

            }
        }
    }

    fun onFilter(list: List<ReferenceModel>, constraint: String): List<ReferenceModel> {
        val filteredList = list.filter {
            it.title.lowercase().contains(constraint.lowercase())
        }

        return filteredList
    }
}

class DiffCallback() : DiffUtil.ItemCallback<ReferenceModel>() {

    override fun areItemsTheSame(p0: ReferenceModel, p1: ReferenceModel): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: ReferenceModel, p1: ReferenceModel): Boolean {
        return p0 == p1
    }

}

class BookmarkSingleClickListener(val clickListener: (Id: Int) -> Unit) {
    fun onClick(bookmarkData: ReferenceModel) = clickListener(bookmarkData.id)

}

class BookmarkDeleteSingleClickListener(val clickListener: (Id: Int) -> Unit) {
    fun onClick(bookmarkData: ReferenceModel) = clickListener(bookmarkData.id)

}