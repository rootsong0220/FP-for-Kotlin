package com.example.fp.applicative

import com.example.fp.functor.Functor

interface Applicative<out A>: Functor<A> {
    fun <V> pure(value: V): Applicative<V>
    infix fun <B> apply(a: Applicative<(A) -> B>): Applicative<B>
}
