package com.example.fp.applicative

import com.example.fp.curried
import com.example.fp.functor.Functor

sealed class Tree<out A> : Functor<A> {
    abstract override fun <B> fmap(f: (A) -> B): Functor<B>

    companion object

    fun <A, B, R> liftA2(f: (A, B) -> R) = { f1: Node<A>, f2: Node<B> ->
        Tree.pure(f.curried()) apply f1 apply f2
    }
}

data class Node<out A>(val value: A, val forest: List<Node<A>> = emptyList()) : Tree<A>() {

    override fun <B> fmap(f: (A) -> B): Node<B> = Node(f(value), forest.map { it.fmap(f) })

    override fun toString(): String {
        return "$value $forest"
    }
}

fun <A> Tree.Companion.pure(value: A) = Node(value)
infix fun <A, B> Node<(A) -> B>.apply(node: Node<A>): Node<B> =
    Node(value(node.value), node.forest.map { it.fmap(value) } + forest.map { it apply node })
