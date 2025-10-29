package com.example.fp

class PartialFunction<in P, out R>(
    private val condition: (P) -> Boolean,
    private val f: (P) -> R
): (P) -> R {
    override fun invoke(p: P): R = when {
        condition(p) -> f(p)
        else -> throw IllegalArgumentException("$p isn't supported by this function")
    }

    fun isDefinedAt(p: P): Boolean = condition(p)


    // Ex 4.1
    fun invokeOrElse(p: P, default: @UnsafeVariance R): R = when {
        condition(p) -> f(p)
        else -> default
    }

    fun orElse(p: P, that: PartialFunction<@UnsafeVariance P, @UnsafeVariance R>): PartialFunction<P, R> = when {
        condition(p) -> this
        else -> that
    }
}
