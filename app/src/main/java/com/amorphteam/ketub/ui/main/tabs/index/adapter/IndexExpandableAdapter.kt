package com.amorphteam.ketub.ui.main.tabs.index.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.BaseObservable
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ItemChildIndexBinding
import com.amorphteam.ketub.databinding.ItemGroupIndexBinding
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexFirstChildItem
import com.amorphteam.ketub.ui.main.tabs.index.model.IndexGroupItem
import com.amorphteam.ketub.utility.TempData



class IndexExpandableAdapter : BaseExpandableListAdapter(), Filterable {
     val clickListener = ClickListener()

    private var items = listOf<IndexGroupItem>()

    fun submitList(items: List<IndexGroupItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getGroupCount(): Int {
        return items.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return items[groupPosition].childItems.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return items[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return items[groupPosition].childItems[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        // Inflate the group view layout
        val binding = DataBindingUtil.inflate<ItemGroupIndexBinding>(
            LayoutInflater.from(parent?.context),
            R.layout.item_group_index,
            parent,
            false
        )

        // Get a reference to the group item for this position
        val groupItem = getGroup(groupPosition) as IndexGroupItem

        binding.indexClickListener.setOnClickListener { clickListener.onGroupClick(groupPosition) }

        // Bind the group item to the view
        binding.bookTitle.text = groupItem.bookTitle
        binding.bookName.text = groupItem.bookName
        return binding.root
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        // Inflate the child view layout
        val binding = DataBindingUtil.inflate<ItemChildIndexBinding>(
            LayoutInflater.from(parent?.context),
            R.layout.item_child_index,
            parent,
            false
        )

        val childItem = getChild(groupPosition, childPosition) as IndexFirstChildItem

        binding.indexClickListener.setOnClickListener { clickListener.onGroupClick(childPosition) }

        binding.bookTitle.text = childItem.bookTitle
        binding.bookName.text = childItem.bookName

        return binding.root
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults().apply {
                    values = if (constraint.isNullOrEmpty())
                    //TODO: IT MUST LOAD FROM VIEWMODEL

                        TempData.indexItems
                    else
                        onFilter(TempData.indexItems!!, constraint.toString())
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                (results?.values as? List<IndexGroupItem>)?.let { submitList(it) }

            }
        }
    }

    fun onFilter(list: List<IndexGroupItem>, constraint: String): List<IndexGroupItem> {
        val filteredList = list.filter {
            //TODO: IT MUST SEARCH BOOK NAME TOO
            it.bookTitle.lowercase().contains(constraint.lowercase())
        }
        return filteredList
    }
}

class ClickListener : BaseObservable() {
    private var listener: ((Int) -> Unit)? = null

    fun setOnGroupClickListener(listener: (Int) -> Unit) {
        this.listener = listener
        notifyChange()
    }

    fun onGroupClick(position: Int) {
        listener?.invoke(position)
    }
}
