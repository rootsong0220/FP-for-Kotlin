package com.example.fp.functor

interface Functor<out A> {
    fun <B> fmap(f: (A) -> B): Functor<B>
}
