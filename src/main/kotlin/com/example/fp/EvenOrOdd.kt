package com.example.fp

object EvenOrOdd {
    fun isOdd(n: Int): Bounce<Boolean> = when (n) {
        0 -> Done(false)
        else -> More { isEven(n - 1) }
    }

    fun isEven(n: Int): Bounce<Boolean> = when (n) {
        0 -> Done(true)
        else -> More { isOdd(n - 1) }
    }
}