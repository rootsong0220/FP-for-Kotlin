package com.example.fp.functor

import com.example.fp.curried
import com.example.fp.functor.FunList.Companion.funListOf
import com.example.fp.functor.Nil.cons


sealed class Maybe<out A>: Functor<A> {
    abstract override fun <B> fmap(f: (A) -> B): Maybe<B>
    abstract override fun toString(): String
    companion object

    fun <T> sequenceA(maybeList: FunList<Maybe<T>>): Maybe<FunList<T>> = when (maybeList) {
        is Nil -> Just(funListOf())
        is Cons -> Maybe.pure(cons<T>().curried()) apply maybeList.head apply sequenceA(maybeList.tail)
    }

}

fun <A> Maybe.Companion.pure(value: A): Maybe<A> = Just(value)

infix fun <A, B> Maybe<(A) -> B>.apply(a: Maybe<A>): Maybe<B> = when (this) {
    is Just -> a.fmap(this.value)
    else -> Nothing
}

fun <A, B, R> liftA2(binaryFunction: (A, B) -> R) = { f1: Maybe<A>, f2: Maybe<B> ->
    Maybe.pure(binaryFunction.curried()) apply f1 apply f2
}

fun <T> sequenceAByFoldRight(maybeList: FunList<Maybe<T>>): Maybe<FunList<T>> =
    maybeList.foldRight(Maybe.pure(funListOf()), liftA2(cons()))
