package com.amorphteam.ketub.utility

import com.amorphteam.ketub.model.NavPointPath
import com.amorphteam.ketub.model.SingleTreeBookHolder
import com.amorphteam.ketub.model.TreeBookHolder
import com.mehdok.fineepublib.epubviewer.epub.NavPoint
import com.mehdok.fineepublib.epubviewer.tree.Node
import com.unnamed.b.atv.model.TreeNode

object NavTreeCreator {
    fun getNavTree(navTree: ArrayList<Node<NavPoint>>): TreeNode {
        val root = TreeNode.root()
        for (tree in navTree) {
            val child = getTreeChild(tree)
            root.addChild(child)
        }
        return root
    }

    fun getTabNavTree(navTrees: ArrayList<TreeBookHolder>): TreeNode {
        val root = TreeNode.root()
        for (tree in navTrees) {
            for (navNode in tree.getNavTree()) {
                val child = getTabTreeChild(navNode.getNode(), tree.bookTitle)
                root.addChild(child)
            }
        }
        return root
    }

    private fun getTabTreeChild(
        root: Node<NavPointPath>,
        bookTitle: String
    ): TreeNode {
        val ch = TreeNode(
            SingleTreeBookHolder(
                bookTitle, root.data.navPoint,
                root.data.bookPath
            )
        )
        if (root.children.size > 0) {
            for (child in root.children) {
                ch.addChildren(
                    getTabTreeChild(
                        child as Node<NavPointPath>,
                        bookTitle
                    )
                )
            }
        }
        return ch
    }

    private fun getTreeChild(root: Node<NavPoint>): TreeNode {
        val ch = TreeNode(root.data)
        if (root.children.size > 0) {
            for (child in root.children) {
                ch.addChildren(getTreeChild(child as Node<NavPoint>))
            }
        }
        return ch
    }
}
