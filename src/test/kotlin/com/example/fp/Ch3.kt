package com.example.fp

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import kotlin.math.sqrt

class Ch3 : StringSpec({
    "Ex 3.2 Power function in Simple Recursion" {
        fun power(x: Double, n: Int): Double {
            return if (n <= 1) x
            else x * power(x, n - 1)
        }

        power(2.0, 10) shouldBe 1024.0
    }



    "Ex 3.3 Factorial function in Simple Recursion" {
        fun factorial(n: Int): Double {
            return if (n <= 1) 1.0
            else n * factorial(n - 1)
        }

        factorial(10) shouldBe 3628800.0
    }

    "maximum function in simple recursion" {
        fun maximum(items: List<Int>): Int = when {
            items.isEmpty() -> error("empty List")
            1 == items.size -> items.first()
            else -> {
                val head = items.head()
                val tail = items.tail()
                val maxVal = maximum(tail)
                if (head > maxVal) head else maxVal
            }
        }

        maximum(listOf(1, 2, 3, 4, 5)) shouldBe 5
    }

    "reverse function in simple recursion" {
        fun reverse(str: String): String = when {
            str.isEmpty() -> ""
            else -> reverse(str.tail()) + str.head()
        }
        reverse("abcd") shouldBe "dcba"
    }

    "Ex 3.4 Binary Function in simple recursion" {
        fun toBinary(n: Int): String = when {
            n <= 1 -> "$n"
            else -> toBinary(n / 2) + "${n % 2}"
        }

        toBinary(10) shouldBe "1010"
    }

    "Ex 3.5 replicate function in simple recursion" {
        fun replicate(n: Int, element: Int): List<Int> = when {
            n <= 0 -> emptyList()
            else -> listOf(element) + replicate(n - 1, element)
        }

        replicate(3, 10) shouldBe listOf(10, 10, 10)
    }

    "take in list with simple recursion" {
        fun take(n: Int, list: List<Int>): List<Int> = when {
            n <= 0 -> emptyList()
            list.isEmpty() -> emptyList()
            else -> listOf(list.head()) + take(n - 1, list.tail())
        }

        take(2, listOf(1, 2, 3)) shouldBe listOf(1, 2)
    }

    "Ex 3.6 elem function in simple recursion" {
        fun elem(num: Int, list: List<Int>): Boolean = when {
            list.isEmpty() -> false
            else -> list.head() == num || elem(num, list.tail())
        }

        elem(1, listOf(1, 2, 3)) shouldBe true
    }

    "Ex 3.7 take Sequence function in simple recursion" {
        fun repeat(n: Int): Sequence<Int> = sequenceOf(n) + { repeat(n) }

        fun takeSequence(n: Int, sequence: Sequence<Int>): List<Int> = when {
            n <= 0 -> emptyList()
            else -> listOf(sequence.first()) + takeSequence(n - 1, sequence)
        }

        takeSequence(3, repeat(10)) shouldBe listOf(10, 10, 10)
    }

    "zip function in simple recursion" {
        fun zip(list1: List<Int>, list2: List<Int>): List<Pair<Int, Int>> = when {
            list1.isEmpty() || list2.isEmpty() -> emptyList()
            else -> listOf(Pair(list1.head(), list2.head())) + zip(list1.tail(), list2.tail())
        }

        zip(listOf(1, 2, 3), listOf(4, 5, 6)) shouldBe listOf(Pair(1, 4), Pair(2, 5), Pair(3, 6))
    }

    "Ex 3.8 Quick Sort function in simple recursion" {
        fun quickSort(arr: IntArray, low: Int = 0, high: Int = arr.size - 1) {

            fun partition(arr: IntArray, low: Int, high: Int): Int {
                val pivot = arr[high]
                var i = low - 1

                fun swap(arr: IntArray, i: Int, j: Int) {
                    arr[i] = arr[j].also { arr[j] = arr[i] }
                }

                for (j in low until high) {
                    if (arr[j] <= pivot) {
                        i++
                        swap(arr, i, j)
                    }
                }

                swap(arr, i + 1, high)
                return i + 1
            }

            if (low < high) {
                val pivotIndex = partition(arr, low, high)
                quickSort(arr, low, pivotIndex - 1)
                quickSort(arr, pivotIndex + 1, high)
            }


        }

        val intArray = intArrayOf(1, 5, 6, 8, 2, 4, 3, 9)
        quickSort(intArray)
        intArray shouldBe intArrayOf(1, 2, 3, 4, 5, 6, 8, 9)
    }

    "Ex 3.9 GCD function in simple recursion" {
        fun gcd(a: Int, b: Int): Int = when {
            a == 0 -> b
            b == 0 -> a
            a > b -> gcd(a % b, b)
            else -> gcd(a, b % a)
        }

        gcd(12, 8) shouldBe 4
    }

    "Fibonacci Function in simple recursion" {
        fun fibonacci(n: Int): Int = when(n) {
            0 -> 0
            1 -> 1
            else -> fibonacci(n - 1) + fibonacci(n - 2)
        }

        fibonacci(10) shouldBe 55
    }

    "Fibonacci with memoization recursion" {
        var memo = Array(100) { -1 }

        fun fiboMemoization(n: Int): Int = when {
            n == 0 -> 0
            n == 1 -> 1
            memo[n] != -1  -> memo[n]
            else -> {
                memo[n] = fiboMemoization(n - 1) + fiboMemoization(n - 2)
                memo[n]
            }
        }

        fiboMemoization(10) shouldBe 55
    }

    "Ex 3.10 Factorial function with memoization" {
        var memo2 = DoubleArray(100) { -1.0 }

        fun factiorial(n: Int): Double = when {
            n <= 1 -> 1.0
            memo2[n] != -1.0 -> memo2[n]
            else -> {
                memo2[n] = factiorial(n - 1) * n
                memo2[n]
            }
        }

        factiorial(10) shouldBe 3628800.0
    }

    "Fibonacci with Tail Recursion" {
        tailrec fun fiboFP(n: Int, first: Int = 0, second: Int = 1): Int = when (n) {
            0 -> first
            1 -> second
            else -> fiboFP(n - 1, second, first + second)
        }

        fiboFP(10) shouldBe 55
    }

    "Ex 3.11 Factorial Function with Tail Recursion" {
        tailrec fun factorialFP(n: Int, acc: Double = 1.0): Double = when (n) {
            1 -> acc
            else -> factorialFP(n - 1, n * acc)
        }

        factorialFP(10) shouldBe 3628800.0
    }

    "Ex 3.13 Power Function with Tail Recursion" {
        tailrec fun powerFP(x: Double, n: Int, acc: Double = 1.0): Double = when (n) {
            0 -> acc
            else -> powerFP(x, n - 1, x * acc)
        }

        powerFP(2.0, 10) shouldBe 1024.0
    }

    "Maximum Function with Tail Recursion" {
        tailrec fun maximumFP(list: List<Int>, acc: Int = Int.MIN_VALUE): Int = when {
            list.isEmpty() -> error("empty list")
            list.size == 1 -> acc
            else -> {
                val head = list.head()
                val maxValue = if(head > acc) head else acc
                val tail = list.tail()
                maximumFP(tail, maxValue)
            }
        }

        maximumFP(listOf(1, 2, 3, 10, 5)) shouldBe 10
    }

    "reverse function in tail recursion" {
        tailrec fun reverseFP(str: String, acc: String = ""): String = when {
            str.isEmpty() -> acc
            else -> reverseFP(str.tail(), str.head() + acc)
        }
        reverseFP("abcd") shouldBe "dcba"
    }

    "Ex 3.14 Binary Function with Tail Recursion" {
        fun toBinary(n: Int): String = when {
            n <= 1 -> "$n"
            else -> toBinary(n / 2) + "${n % 2}"
        }

        tailrec fun toBinaryFP(n: Int, acc: String = ""): String = when {
            n <= 1 -> n.toString() + acc
            else -> toBinaryFP(n / 2, (n % 2).toString() + acc)
        }

        toBinaryFP(10) shouldBe "1010"
    }

    "Ex 3.15 Replicate Function with Tail Recursion" {
        fun replicateFP(n: Int, element: Int, acc: List<Int> = emptyList()): List<Int> = when {
            n <= 0 -> acc
            else -> replicateFP(n - 1, element, acc + element)
        }

        replicateFP(3, 10) shouldBe listOf(10, 10, 10)
    }

    "take function with tail Recursion" {
        tailrec fun take(n: Int, list: List<Int>, acc: List<Int> = emptyList()): List<Int> = when {
            n <= 0 || list.isEmpty() -> acc
            else -> {
                val takeList = acc + listOf(list.head())
                take(n -1, list.tail(), takeList)
            }
        }

        take(2, listOf(1, 2, 3)) shouldBe listOf(1, 2)
    }

    "Ex 3.16 elem function with tail Recursion" {
        fun elem(num: Int, list: List<Int>): Boolean = when {
            list.isEmpty() -> false
            else -> list.head() == num || elem(num, list.tail())
        }

        tailrec fun elemFP(num: Int, list: List<Int>): Boolean = when {
            list.isEmpty() -> false
            list.head() == num -> true
            else -> elemFP(num, list.tail())
        }

        elemFP(2, listOf(1, 2, 3)) shouldBe true
    }

    "zip function with tail Recursion" {
        tailrec fun zip(list1: List<Int>, list2: List<Int>, acc: List<Pair<Int, Int>> = emptyList()): List<Pair<Int, Int>> = when {
            list1.isEmpty() || list2.isEmpty() -> acc
            else -> zip(list1.tail(), list2.tail(), acc + listOf(Pair(list1.head(), list2.head())))
        }

        zip(listOf(1, 2, 3), listOf(4, 5, 6)) shouldBe listOf(Pair(1, 4), Pair(2, 5), Pair(3, 6))
    }

    "Ex 3.17 returns sqrt(n)/2^k at the first k such that sqrt(n)/2^k < 1" {
        fun sqrtAndDivideBy2(n: Double): Double {
            fun divideBy2(n: Double): Double {
                val divided = n / 2
                val result = if (divided < 1) divided else divideBy2(divided)
                return result
            }

            return divideBy2(sqrt(n))
        }

        sqrtAndDivideBy2(8.0) shouldBe sqrt(2.0)/2
    }

    "Even Or Odd fun with trampoline" {
        trampoline(EvenOrOdd.isEven(9999)) shouldBe false
    }

    "Ex 3.18 trampoline with 3.17" {
        fun sqrtAndDivideBy2(n: Double): Bounce<Double> {
            fun dividedBy2(n: Double): Bounce<Double> {
                val dividedBy2 = n / 2
                return if(dividedBy2 < 1)
                    Done(dividedBy2)
                else
                    More { dividedBy2(dividedBy2) }
            }

            return dividedBy2(sqrt(n))
        }

        trampoline(sqrtAndDivideBy2(8.0)) shouldBe (sqrt(2.0)/2)
    }

    "Ex 3.19 trampoline with factorial" {
        fun factorial(n: BigDecimal): BigDecimal {
            fun factorial(n: BigDecimal, first: BigDecimal, second: BigDecimal): Bounce<BigDecimal> = when (n) {
                BigDecimal.ZERO -> Done(first)
                BigDecimal.ONE -> Done(second)
                else -> More { factorial(n.minus(BigDecimal.ONE), second, second * n) }
            }

            return trampoline(factorial(n, BigDecimal.ONE, BigDecimal.ONE))
        }

        factorial(BigDecimal(10)) shouldBe BigDecimal(3628800)
    }

    "Basic PowerSet Function" {
        fun <T> powerSet(s: Set<T>): Set<Set<T>> = when {
            s.isEmpty() -> setOf(setOf())
            else -> {
                val head = s.head()
                val restSet = powerSet(s.tail())
                restSet + restSet.map { setOf(head) + it}.toSet()
            }
        }
    }

    "PowerSet Function with tail recursion" {
        tailrec fun <T> powerSet(s: Set<T>, acc: Set<Set<T>> = setOf(setOf())): Set<Set<T>> = when {
            s.isEmpty() -> acc
            else -> powerSet(s.tail(), acc + acc.map { it + s.head()})
        }

        powerSet(setOf(1, 2, 3)) shouldBe setOf(emptySet(), setOf(1), setOf(2), setOf(3), setOf(1, 2), setOf(1, 3), setOf(2, 3), setOf(1, 2, 3))
    }


})