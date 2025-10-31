package com.example.fp.applicative

import com.example.fp.functor.Functor

sealed class ZipList<out A>: Functor<A> {
    abstract override fun <B> fmap(f: (A) -> B): ZipList<B>

    fun first(): A = when (this) {
        is ZNil -> throw NoSuchElementException("No elements in ZipList")
        is ZCons -> head
    }

    fun size(): Int = when (this) {
        ZNil -> 0
        is ZCons -> 1 + tail.size()
    }

    companion object {
        fun <T> zipListOf(vararg elements: T): ZipList<T> = elements.toZipList()

        private fun <T> Array<out T>.toZipList(): ZipList<T> = when {
            this.isEmpty() -> ZNil
            else -> ZCons(this[0], this.copyOfRange(1, this.size).toZipList())
        }
    }
}

object ZNil : ZipList<Nothing>() {
    override fun <B> fmap(f: (Nothing) -> B): ZipList<B> = ZNil
}

data class ZCons<T>(val head: T, val tail: ZipList<T>) : ZipList<T>() {
    override fun <B> fmap(f: (T) -> B): ZipList<B> = ZCons(f(head), tail.fmap(f))
}

fun <A> ZipList.Companion.pure(a: A): ZipList<A> = ZCons(a, ZNil)

infix fun <A> ZipList<A>.append(other: ZipList<A>): ZipList<A> = when (this) {
    is ZNil -> other
    is ZCons -> ZCons(this.head, this.tail append other)
}

infix fun <A, B> ZipList<(A) -> B>.apply(f: ZipList<A>): ZipList<B> = when (this) {
    is ZNil -> ZNil
    is ZCons -> when (f) {
        is ZNil -> ZNil
        is ZCons -> ZipList.pure(this.head(f.head)) append (this.tail apply f.tail)
    }
}