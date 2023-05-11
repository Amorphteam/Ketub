package com.mehdok.fineepublib.epubviewer.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehdok on 3/7/18.
 */

//public class Node<T> {
//    private List<Node<T>> children = new ArrayList<Node<T>>();
//    private Node<T> parent = null;
//    private T data = null;
//
//    public Node(T data) {
//        this.data = data;
//    }
//
//    public Node(T data, Node<T> parent) {
//        this.data = data;
//        this.parent = parent;
//    }
//
//    public List<Node<T>> getChildren() {
//        return children;
//    }
//
//    public void setParent(Node<T> parent) {
//        parent.addChild(this);
//        this.parent = parent;
//    }
//
//    public void addChild(T data) {
//        Node<T> child = new Node<T>(data);
//        child.setParent(this);
//        this.children.add(child);
//    }
//
//    public void addChild(Node<T> child) {
//        child.setParent(this);
//        this.children.add(child);
//    }
//
//    public T getData() {
//        return this.data;
//    }
//
//    public void setData(T data) {
//        this.data = data;
//    }
//
//    public boolean isRoot() {
//        return (this.parent == null);
//    }
//
//    public boolean isLeaf() {
//        return this.children.size() == 0;
//    }
//
//    public void removeParent() {
//        this.parent = null;
//    }
//}

public class Node<T>{
    private T data = null;
    private List<Node> children = new ArrayList<>();
    private Node parent = null;

    public Node(T data) {
        this.data = data;
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(T data) {
        Node<T> newChild = new Node<>(data);
        newChild.setParent(this);
        children.add(newChild);
    }

    public void addChildren(List<Node> children) {
        for(Node t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<Node> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(Node parent) {
        this.parent = parent;
    }

    public Node<T> getParent() {
        return parent;
    }
}
