package com.example.fp.foldable

import com.example.fp.FunList
import com.example.fp.FunList.Nil.funListOf
import com.example.fp.monoid.AnyMonoid
import com.example.fp.monoid.FunListMonoid

sealed class BinaryTree<out A> : Foldable<A> {
    override fun <B> foldLeft(acc: B, f: (B, A) -> B): B = when (this) {
        is EmptyTree -> acc
        is Node -> {
            val leftAcc = leftTree.foldLeft(acc, f)
            val rootAcc = f(leftAcc, value)
            rightTree.foldLeft(rootAcc, f)
        }
    }

}

data class Node<A>(
    val value: A,
    val leftTree: BinaryTree<A> = EmptyTree,
    val rightTree: BinaryTree<A> = EmptyTree
) : BinaryTree<A>()

object EmptyTree : BinaryTree<Nothing>()

fun <A> BinaryTree<A>.contains(value: A) = foldMap({ it == value }, AnyMonoid())

fun <A> BinaryTree<A>.toFunList(): FunList<A> = foldMap({ funListOf(it) }, FunListMonoid())