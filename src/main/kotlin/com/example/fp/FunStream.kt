package com.example.fp

import com.example.fp.FunStream.*

sealed class FunStream<out T> {
    object Nil : FunStream<Nothing>()
    data class Cons<T>(val head: () -> T, val tail: () -> FunStream<T>) : FunStream<T>() {
        override fun equals(other: Any?): Boolean = when (other) {
            is Cons<*> -> head() == other.head() && tail() == other.tail()
            else -> false
        }

        override fun hashCode(): Int {
            var result = head.hashCode()
            result = 31 * result + tail.hashCode()
            return result
        }
    }

    fun <T> funStreamOf(vararg elements: T): FunStream<T> = elements.toFunStream()

    private fun <T> Array<out T>.toFunStream(): FunStream<T> = when {
        this.isEmpty() -> Nil
        else -> Cons({ this[0] }, { this.copyOfRange(1, this.size).toFunStream() })
    }
}

fun <T> FunStream<T>.getHead(): T = when (this) {
    Nil -> throw NoSuchElementException()
    is Cons -> this.head()
}

fun <T> FunStream<T>.getTail(): FunStream<T> = when (this) {
    Nil -> throw NoSuchElementException("Nil doesn't have a tail.")
    is Cons -> this.tail()
}

// Ex 5.17
fun FunStream<Int>.sum(): Int = when (this) {
    Nil -> 0
    is Cons -> head() + tail().sum()
}

// Ex 5.18
fun FunStream<Int>.product(): Int = when (this) {
    Nil -> 1
    is Cons -> head() * tail().product()
}

// Ex 5.19
fun <T> FunStream<T>.appendTail(value: T): FunStream<T> = when (this) {
    Nil -> Cons({ value }, { Nil })
    is Cons -> Cons(head) { tail().appendTail(value) }
}

// Ex 5.20
fun <T> FunStream<T>.filter(p: (T) -> Boolean): FunStream<T> = when (this) {
    Nil -> Nil
    is Cons -> if (p(head())) Cons(head) { tail().filter(p) } else tail().filter(p)
}

// Ex 5.21
fun <T, R> FunStream<T>.map(f: (T) -> R): FunStream<R> = when (this) {
    Nil -> Nil
    is Cons -> Cons({ f(head()) }, { tail().map(f) })
}

fun <T> generateFunStream(seed: T, generate: (T) -> T): FunStream<T> =
    Cons({ seed }, { generateFunStream(generate(seed), generate) })

tailrec fun <T> FunStream<T>.forEach(f: (T) -> Unit): Unit = when (this) {
    Nil -> Unit
    is Cons -> {
        f(head())
        tail().forEach(f)
    }
}

// Ex 5.22
fun <T> FunStream<T>.take(n: Int): FunStream<T> = when (n) {
    0 -> Nil
    else -> when (this) {
        Nil -> Nil
        is Cons -> Cons(head) { tail().take(n - 1) }
    }
}
