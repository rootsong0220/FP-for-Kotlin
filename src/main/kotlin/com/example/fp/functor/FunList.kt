package com.example.fp.functor

import kotlin.Nothing

//Ex 7-1
sealed class FunList<out A>: Functor<A> {
    abstract override fun <B> fmap(f: (A) -> B): FunList<B>

    fun first(): A = when (this) {
        is Nil -> throw NoSuchElementException("No elements in FunList")
        is Cons -> head
    }

    fun size(): Int = when (this) {
        Nil -> 0
        is Cons -> 1 + tail.size()
    }

    companion object {
        fun <T> funListOf(vararg elements: T): FunList<T> = elements.toFunList()

        private fun <T> Array<out T>.toFunList(): FunList<T> = when {
            this.isEmpty() -> Nil
            else -> Cons(this[0], this.copyOfRange(1, this.size).toFunList())
        }
    }

    fun <T> cons() = { x: T, xs: FunList<T> -> Cons(x, xs) }
}

object Nil : FunList<Nothing>() {
    override fun <B> fmap(f: (Nothing) -> B): FunList<B> = Nil
}

data class Cons<T>(val head: T, val tail: FunList<T>) : FunList<T>() {
    override fun <B> fmap(f: (T) -> B): FunList<B> = Cons(f(head), tail.fmap(f))
}

fun <A> FunList.Companion.pure(a: A): FunList<A> = Cons(a, Nil)

infix fun <A> FunList<A>.append(other: FunList<A>): FunList<A> = when (this) {
    is Nil -> other
    is Cons -> Cons(this.head, this.tail append other)
}

infix fun <A, B> FunList<(A) -> B>.apply(f: FunList<A>): FunList<B> = when (this) {
    is Nil -> Nil
    is Cons -> f.fmap(this.head) append (this.tail apply f)
}

fun <T, R> FunList<T>.foldRight(acc: R, f: (T, R) -> R): R = when(this) {
    Nil -> acc
    is Cons -> f(head, tail.foldRight(acc, f))
}
