package com.example.fp.functor

data class Just<out A>(val value: A): Maybe<A>() {
    override fun <B> fmap(f: (A) -> B): Maybe<B> = Just(f(value))
    override fun toString(): String = "Just($value)"
}
