package com.example.fp.functor

import com.example.fp.compose

data class UnaryFunction<in T, out R>(val g: (T) -> R): Functor<R> {
    override fun <R2> fmap(f: (R) -> R2): UnaryFunction<T, R2> = UnaryFunction { x: T -> (f compose g)(x)  }

    fun invoke(input: T): R = g(input)
}
