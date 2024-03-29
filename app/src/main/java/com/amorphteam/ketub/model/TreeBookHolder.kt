package com.amorphteam.ketub.model

import com.mehdok.fineepublib.epubviewer.epub.NavPoint
import com.mehdok.fineepublib.epubviewer.tree.Node

class TreeBookHolder(
    val bookTitle: String,
    navTree: ArrayList<TreeAndPath>
) {
    private val navTree: ArrayList<TreeAndPath>

    init {
        this.navTree = navTree
    }

    fun getNavTree(): ArrayList<TreeAndPath> {
        return navTree
    }
}

class TreeAndPath(
    node: Node<NavPointPath>
) {
    private val node: Node<NavPointPath>

    init {
        this.node = node
    }

    fun getNode(): Node<NavPointPath> {
        return node
    }
}

class NavPointPath(val navPoint: NavPoint, val bookPath: String)


class NavResult(
    navPoints: java.util.ArrayList<IndexesInfo>,
    navTrees: java.util.ArrayList<TreeBookHolder>
) {
    var navPoints: java.util.ArrayList<IndexesInfo>
    var navTrees: java.util.ArrayList<TreeBookHolder>

    init {
        this.navPoints = navPoints
        this.navTrees = navTrees
    }
}


data class IndexesInfo(val bookTitle: String, val navPoint: NavPoint, val bookPath: String)

