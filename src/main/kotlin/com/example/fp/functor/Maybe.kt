package com.example.fp.functor

sealed class Maybe<out A>: Functor<A> {
    abstract override fun <B> fmap(f: (A) -> B): Maybe<B>
    abstract override fun toString(): String
}
