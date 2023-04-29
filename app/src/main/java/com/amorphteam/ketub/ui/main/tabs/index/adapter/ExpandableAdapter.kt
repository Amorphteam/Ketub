package com.amorphteam.ketub.ui.main.tabs.index.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import com.amorphteam.ketub.R
import com.amorphteam.ketub.databinding.ItemChildIndexBinding
import com.amorphteam.ketub.databinding.ItemGroupIndexBinding
import com.amorphteam.ketub.ui.main.tabs.index.model.ChildItem
import com.amorphteam.ketub.ui.main.tabs.index.model.GroupItem

class ExpandableAdapter : BaseExpandableListAdapter() {

    private var items = listOf<GroupItem>()

    fun setItems(items: List<GroupItem>) {
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

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        // Inflate the group view layout
        val binding = DataBindingUtil.inflate<ItemGroupIndexBinding>(
            LayoutInflater.from(parent?.context),
            R.layout.item_group_index,
            parent,
            false
        )

        // Get a reference to the group item for this position
        val groupItem = getGroup(groupPosition) as GroupItem

        // Bind the group item to the view
        binding.bookTitle.text = groupItem.bookTitle
        binding.bookName.text = groupItem.bookName
        return binding.root
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {
        // Inflate the child view layout
        val binding = DataBindingUtil.inflate<ItemChildIndexBinding>(
            LayoutInflater.from(parent?.context),
            R.layout.item_child_index,
            parent,
            false
        )

        val childItem = getChild(groupPosition,childPosition) as ChildItem

        binding.bookTitle.text = childItem.bookTitle
        binding.bookName.text = childItem.bookName

        return binding.root
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}