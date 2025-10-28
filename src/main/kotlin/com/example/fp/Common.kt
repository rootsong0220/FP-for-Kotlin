package com.example.fp

fun String.head() = first()
fun String.tail() = drop(1)

fun <T> List<T>.head(): T = first()
fun <T> List<T>.tail(): List<T> = drop(1)

fun <T> Set<T>.head(): T = first()
fun <T> Set<T>.tail(): Set<T> = drop(1).toSet()

operator fun <T> Sequence<T>.plus(other: () -> Sequence<T>) = object : Sequence<T> {
    private val thisIterator: Iterator<T> by lazy { this@plus.iterator() }
    private val otherIterator: Iterator<T> by lazy { other().iterator() }
    override fun iterator() = object : Iterator<T> {
        override fun next(): T =
            if (thisIterator.hasNext())
                thisIterator.next()
            else
                otherIterator.next()

        override fun hasNext(): Boolean = thisIterator.hasNext() || otherIterator.hasNext()
    }
}

sealed class Bounce<A>
data class Done<A>(val result: A) : Bounce<A>()
data class More<A>(val thunk: () -> Bounce<A>) : Bounce<A>()

tailrec fun <A> trampoline(bounce: Bounce<A>): A = when (bounce) {
    is Done -> bounce.result
    is More -> trampoline(bounce.thunk())
}
