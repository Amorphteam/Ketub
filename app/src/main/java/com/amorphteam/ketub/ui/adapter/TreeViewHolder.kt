package com.amorphteam.ketub.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.amorphteam.ketub.R
import com.amorphteam.ketub.model.SingleTreeBookHolder
import com.amorphteam.ketub.utility.EpubHelper
import com.amorphteam.ketub.utility.EpubHelper.Companion.getContentWithoutTag
import com.amorphteam.ketub.utility.FileManager
import com.unnamed.b.atv.model.TreeNode

class TreeViewHolder(val context: Context): TreeNode.BaseNodeViewHolder<SingleTreeBookHolder>(context), View.OnClickListener {
    private lateinit var tvImage:ImageView
    override fun createNodeView(node: TreeNode?, value: SingleTreeBookHolder?): View {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.item_tree_toc, null, false)
        val tvValue = view.findViewById<TextView>(R.id.row_text)
        val tvBookName = view.findViewById<TextView>(R.id.book_name_text)
        val cardView = view.findViewById<LinearLayout>(R.id.card_view_tab_nav_tree)
        tvImage = view.findViewById<ImageView>(R.id.tree_icon)


        if (node!!.isLeaf) {
            cardView.setPadding(0, 0, 0, 0)
            tvBookName.visibility = View.INVISIBLE
        } else {
            tvImage.setImageResource(R.drawable.row_right)

            if (node.level == 1) {
                cardView.setPadding(0, 0, 0, 0)
            } else if (node.level > 1) {
                tvBookName.visibility = View.INVISIBLE
                cardView.setPadding(0, 0, 40, 0)
            }

    }
        tvValue.text = value?.navPoint?.navLabel
        tvBookName.text = value?.bookTitle

        tvValue.setOnClickListener(this)
        tvImage.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        val nav = mNode.value as SingleTreeBookHolder
        if (v!!.id == R.id.row_text || v.id == R.id.tree_icon) {
            if (mNode.isLeaf) {
                val fileManager = FileManager(view.context)
                val booksAddress: String? = fileManager.getBookAddress(nav.bookPath)
                EpubHelper.openEpub(booksAddress.toString(), getContentWithoutTag(nav.navPoint.content), context)
            } else {
                treeView.toggleNode(mNode)
                if (mNode.isExpanded) {
                    tvImage.setImageResource(R.drawable.row_down)
                } else {
                    tvImage.setImageResource(R.drawable.row_right)
                }
            }
        }
    }

}