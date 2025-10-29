package com.example.fp

import com.example.fp.FunList.Cons
import com.example.fp.FunList.Nil

sealed class FunList<out T> {
    object Nil : FunList<Nothing>()
    data class Cons<T>(val head: T, val tail: FunList<T>) : FunList<T>()

    fun <T> funListOf(vararg elements: T): FunList<T> = elements.toFunList()

    private fun <T> Array<out T>.toFunList(): FunList<T> = when {
        this.isEmpty() -> Nil
        else -> Cons(this[0], this.copyOfRange(1, this.size).toFunList())
    }
}

// FunList Extension
fun <T> FunList<T>.addHead(head: T): FunList<T> = Cons(head, this)
tailrec fun <T> FunList<T>.appendTail(value: T, acc: FunList<T> = Nil): FunList<T> = when (this) {
    Nil -> Cons(value, acc).reverse()
    is Cons -> tail.appendTail(value, acc.addHead(head))
}

tailrec fun <T> FunList<T>.reverse(acc: FunList<T> = Nil): FunList<T> = when (this) {
    Nil -> acc
    is Cons -> tail.reverse(acc.addHead(head))
}

fun <T> FunList<T>.getTail(): FunList<T> = when (this) {
    Nil -> throw NoSuchElementException("Nil doesn't have a tail.")
    is Cons -> tail
}

tailrec fun <T> FunList<T>.filter(acc: FunList<T> = Nil, p: (T) -> Boolean): FunList<T> = when (this) {
    Nil -> acc.reverse()
    is Cons -> if (p(head)) tail.filter(acc.addHead(head), p) else tail.filter(acc, p)
}

// Ex 5.3
fun <T> FunList<T>.getHead(): T = when (this) {
    is Nil -> throw NoSuchElementException()
    is Cons -> this.head
}

tailrec fun <T, R> FunList<T>.map(acc: FunList<R> = Nil, f: (T) -> R): FunList<R> = when (this) {
    is Nil -> acc.reverse()
    is Cons -> tail.map(acc.addHead(f(head)), f)
}

tailrec fun <T, R> FunList<T>.foldLeft(acc: R, f: (R, T) -> R): R = when(this) {
    Nil -> acc
    is Cons -> tail.foldLeft(f(acc, head), f)
}

fun FunList<Int>.sum(): Int = foldLeft(0) { acc, value -> acc + value }
fun <T, R> FunList<T>.mapByFoldLeft(f: (T) -> R): FunList<R> = foldLeft(Nil) { acc: FunList<R>, value: T -> acc.appendTail(f(value)) }

fun <T, R> FunList<T>.foldRight(acc: R, f: (T, R) -> R): R = when(this) {
    Nil -> acc
    is Cons -> f(head, tail.foldRight(acc, f))
}

// Ex 5.13
tailrec fun <T, R> FunList<T>.zip(other: FunList<R>, acc: FunList<Pair<T, R>> = Nil): FunList<Pair<T, R>> = when {
    this is Nil || other is Nil -> acc.reverse()
    else -> getTail().zip(other.getTail(), acc.addHead(Pair(getHead(), other.getHead())))
}

tailrec fun <T1, T2, R> FunList<T1>.zipWith(f: (T1, T2) -> R, other: FunList<T2>, acc: FunList<R> = Nil): FunList<R> = when {
    this is Nil || other is Nil -> acc.reverse()
    else -> getTail().zipWith(f, other.getTail(), acc.addHead(f(getHead(), other.getHead())))
}
