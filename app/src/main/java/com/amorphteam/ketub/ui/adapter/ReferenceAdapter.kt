package com.amorphteam.ketub.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amorphteam.ketub.databinding.ItemBookmarkBinding
import com.amorphteam.ketub.model.ReferenceModel
import com.amorphteam.ketub.utility.TempData

class ReferenceAdapter(
    private val clickListener: ItemClickListener,
    private val deleteClickListener: DeleteClickListener
) : ListAdapter<ReferenceModel, ReferenceAdapter.ViewHolder>(DiffCallback()), Filterable {

    private var originalList: List<ReferenceModel> = emptyList()

    class ViewHolder private constructor(val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ReferenceModel,
            clickListener: ItemClickListener,
            deleteClickListener: DeleteClickListener
        ) {
            binding.item = item
            binding.itemClickListener = clickListener
            binding.deleteClickListener = deleteClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBookmarkBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, deleteClickListener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                if (constraint.isNullOrEmpty()) {
                    results.values = originalList.toList()
                } else {
                    results.values = onFilter(originalList, constraint.toString())
                }
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val filteredList = results?.values as? List<ReferenceModel> ?: emptyList()
                submitList(filteredList)
            }
        }
    }

    fun onFilter(list: List<ReferenceModel>, constraint: String): List<ReferenceModel> {
        val filteredList = list.filter {
            it.title.lowercase().contains(constraint.lowercase())

        }
        Log.d("Filter", "Before: ${list.size}, After: ${filteredList.size}")
        return filteredList
    }

    fun setData(data: List<ReferenceModel>) {
        originalList = data
        submitList(data)
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

class ItemClickListener(val clickListener: (referenceModel: ReferenceModel) -> Unit) {
    fun onClick(bookmarkData: ReferenceModel) = clickListener(bookmarkData)

}

class DeleteClickListener(val clickListener: (Id: Int) -> Unit) {
    fun onClick(bookmarkData: ReferenceModel) = clickListener(bookmarkData.id)

}