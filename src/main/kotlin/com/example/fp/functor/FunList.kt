package com.example.fp.functor

import kotlin.Nothing

//Ex 7-1
sealed class FunList<out A>: Functor<A> {
    abstract override fun <B> fmap(f: (A) -> B): FunList<B>
    abstract override fun toString(): String

    fun <T> funListOf(vararg elements: T): FunList<T> = elements.toFunList()

    private fun <T> Array<out T>.toFunList(): FunList<T> = when {
        this.isEmpty() -> Nil
        else -> Cons(this[0], this.copyOfRange(1, this.size).toFunList())
    }

    object Nil : FunList<Nothing>() {
        override fun <B> fmap(f: (Nothing) -> B): FunList<B> = Nil
        override fun toString(): String = "Nil"
    }

    data class Cons<T>(val head: T, val tail: FunList<T>) : FunList<T>() {
        override fun <B> fmap(f: (T) -> B): FunList<B> = when (this) {
            Nil -> Nil
            is Cons -> Cons(f(head), tail.fmap(f))
        }
    }

    fun first(): A = when (this) {
        is Nil -> throw NoSuchElementException("No elements in FunList")
        is Cons -> head
    }

    fun size(): Int = when (this) {
        Nil -> 0
        is Cons -> 1 + tail.size()
    }
}
