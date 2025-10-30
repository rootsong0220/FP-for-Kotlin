package com.example.fp.functor

import kotlin.Nothing

sealed class Tree<out A> : Functor<A> {
    abstract override fun <B> fmap(f: (A) -> B): Tree<B>
    abstract override fun toString(): String

    object EmptyTree : Tree<Nothing>() {
        override fun <B> fmap(f: (Nothing) -> B): Tree<B> = EmptyTree
        override fun toString(): String = "E"
    }

    data class Node<out A>(val value: A, val leftTree: Tree<A>, val rightTree: Tree<A>) : Tree<A>() {
        override fun <B> fmap(f: (A) -> B): Tree<B> = Node(f(value), leftTree.fmap(f), rightTree.fmap(f))
        override fun toString(): String = "(N $value, $leftTree, $rightTree)"
    }

    fun <T> treeOf(value: T, leftTree: Tree<T> = EmptyTree, rightTree: Tree<T> = EmptyTree): Tree<T> = Node(value, leftTree, rightTree)
}
