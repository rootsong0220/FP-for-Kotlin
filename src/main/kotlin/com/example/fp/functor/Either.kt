package com.example.fp.functor

import kotlin.Nothing

sealed class Either<out L, out R>: Functor<R> {
    abstract override fun <R2> fmap(f: (R) -> R2): Either<L, R2>

    companion object

    fun <L, A, B, R> liftA2(f: (A, B) -> R) = { ea: Either<L, A>, eb: Either<L, B> ->
        ea.fmap { a: A -> { b: B -> f(a, b)} } apply eb
    }
}

data class Left<out L>(val value: L): Either<L, Nothing>() {
    override fun toString(): String = "Left($value)"
    override fun <R2> fmap(f: (Nothing) -> R2): Either<L, R2> = this
}

data class Right<out R>(val value: R): Either<Nothing, R>() {
    override fun toString(): String = "Right($value)"
    override fun <R2> fmap(f: (R) -> R2): Either<Nothing, R2> = Right(f(value))
}

fun <A> Either.Companion.pure(value: A): Either<Nothing, A> = Right(value)

infix fun <L, A, B> Either<L, (A) -> B>.apply(f: Either<L, A>): Either<L, B> = when (this) {
    is Left -> this
    is Right -> f.fmap(this.value)
}

