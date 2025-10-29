package com.example.fp

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Ch4 : StringSpec({

    "Ex 4-3 max function with Currying" {
        fun max(a: Int) = { b: Int -> if (a > b) a else b }
        max(1)(2) shouldBe 2
    }

    "Ex 4-4 min function with Curried" {
        val min = { a: Int, b: Int -> if (a < b) a else b }.curried()
        min(1)(2) shouldBe 1
    }

    "Ex 4-4 evaluate square of maximum of integer list" {
        val max = { list: List<Int> -> list.maxOrNull() ?: 0 }
        val square = { i: Int -> i * i }
        fun maxOfSquare(list: List<Int>) = square(max(list))
        maxOfSquare(listOf(1, 2, 3, 4)) shouldBe 16
    }

    "Ex 4-5 reexpress Ex 4-4 with compose" {
        val max = { list: List<Int> -> list.maxOrNull() ?: 0 }
        val square = { i: Int -> i * i }
        val maxOfSquare = square compose max
        maxOfSquare(listOf(1, 2, 3, 4)) shouldBe 16
    }

    "Examples for ZipWith Function" {
        val list1 = listOf(6, 3, 2, 1, 4)
        val list2 = listOf(7, 4, 2, 6, 3)

        val add = { p1: Int, p2: Int -> p1 + p2 }
        val result1 = zipWith(add, list1, list2)

        result1 shouldBe listOf(13, 7, 4, 7, 7)

        val max = { p1: Int, p2: Int -> if (p1 > p2) p1 else p2 }
        val result2 = zipWith(max, list1, list2)

        result2 shouldBe listOf(7, 4, 2, 6, 4)

        val strcat = { p1: String, p2: String -> p1 + p2}
        val result3 = zipWith(strcat, listOf("a", "b", "c"), listOf("d", "e", "f"))

        result3 shouldBe listOf("ad", "be", "cf")

        val product = { p1: Int, p2: Int -> p1 * p2}
        val result4 = zipWith(product, listOf(1, 2, 3), listOf(4, 5, 6))

        result4 shouldBe listOf(4, 10, 18)
    }

    "Ex 4-7 takeWhile with tail recursion" {
        tailrec fun <T> takeWhile(condition: (T) -> Boolean, list: List<T>, acc: List<T> = emptyList()): List<T> = when {
            list.isEmpty() -> acc
            else -> {
                val head = list.head()
                if (condition(head)) {
                    takeWhile(condition, list.tail(), acc + head)
                } else {
                    acc
                }
            }
        }

        takeWhile({ it <= 5 }, listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)) shouldBe listOf(1, 2, 3, 4, 5)
    }

    "Ex 4-8 takeWhile for sequence" {
        fun <T> takeWhile(condition: (T) -> Boolean, sequence: Sequence<T>): List<T> {
            val iterator = sequence.iterator()
            tailrec fun takeWhile(acc: List<T>): List<T> = when {
                !iterator.hasNext() -> acc
                else -> {
                    val next = iterator.next()
                    when {
                        condition(next) -> takeWhile(acc + next)
                        else -> acc
                    }
                }
            }

            return takeWhile(emptyList())
        }

        takeWhile({ it <= 5 }, generateSequence(1) { it + 1 }) shouldBe listOf(1, 2, 3, 4, 5)
    }
})
