package com.example.fp.functor

import kotlin.Nothing

sealed class MaybeCounter<out A>: Functor<A> {
    abstract override fun <B> fmap(f: (A) -> B): MaybeCounter<B>
    abstract override fun toString(): String
}

data class JustCounter<out A>(val value: A, val count: Int): MaybeCounter<A>() {
    override fun <B> fmap(f: (A) -> B): MaybeCounter<B> = JustCounter(f(value), count + 1)
    override fun toString(): String = "JustCounter($value, $count)"
}

object NothingCounter: MaybeCounter<Nothing>() {
    override fun <B> fmap(f: (Nothing) -> B): MaybeCounter<B> =
        NothingCounter

    override fun toString(): String = "NothingCounter"

}