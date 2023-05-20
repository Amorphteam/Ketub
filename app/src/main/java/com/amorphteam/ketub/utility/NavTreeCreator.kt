package com.amorphteam.ketub.utility

import com.amorphteam.ketub.model.NavPointPath
import com.amorphteam.ketub.model.SingleTreeBookHolder
import com.amorphteam.ketub.model.TreeBookHolder
import com.mehdok.fineepublib.epubviewer.epub.NavPoint
import com.mehdok.fineepublib.epubviewer.tree.Node
import com.unnamed.b.atv.model.TreeNode

object NavTreeCreator {

    fun getNavTree(
        navPoints: java.util.ArrayList<NavPoint>,
        bookPath: String?
    ): java.util.ArrayList<Node<NavPointPath>>? {
        val navTree = java.util.ArrayList<Node<NavPointPath>>()
        for (nav in navPoints) {
            if (nav.depth == 0) {
                // it is top level
                navTree.add(
                    Node(
                        NavPointPath(
                            nav,
                            bookPath!!
                        )
                    )
                )
            } else {
                val parentPlayOrder = nav.depth
                val parent = getLastElementWithPlayOrder(
                    navTree[navTree.size - 1],
                    parentPlayOrder
                )
                parent.addChild(NavPointPath(nav, bookPath!!))
            }
        }
        return navTree
    }

    private fun getLastElementWithPlayOrder(
        tree: Node<NavPointPath>,
        playOrder: Int
    ): Node<NavPointPath> {
        return if (playOrder == tree.data.navPoint.depth.plus(1)) {
            tree
        } else {
            getLastElementWithPlayOrder(
                tree.children[tree.children.size - 1] as Node<NavPointPath>,
                playOrder
            )
        }
    }

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
