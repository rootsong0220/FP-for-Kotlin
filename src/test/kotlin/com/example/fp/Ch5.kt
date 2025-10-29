package com.example.fp

import com.example.fp.FunList.Cons
import com.example.fp.FunList.Nil
import com.example.fp.FunList.Nil.funListOf
import com.example.fp.FunStream.Nil.funStreamOf
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.math.sqrt

class Ch5 : StringSpec({

    fun <T> FunList<T>.toList(): List<T> {
        tailrec fun FunList<T>.toList(acc: MutableList<T>): MutableList<T> = when (this) {
            is Cons -> this.tail.toList(acc.add(this.head).let { acc })
            is Nil -> acc
        }

        return this.toList(mutableListOf())
    }

    "Ex 5-1 create intList with FunList" {
        val intList = Cons(1, Cons(2, Cons(3, Cons(4, Cons(5, Nil)))))
        intList.toList() shouldBe listOf(1, 2, 3, 4, 5)
    }

    "Ex 5-2 create doubleList with FunList" {
        val doubleList = Cons(1.0, Cons(2.0, Cons(3.0, Cons(4.0, Cons(5.0, Nil)))))
        doubleList.toList() shouldBe listOf(1.0, 2.0, 3.0, 4.0, 5.0)
    }

    "Ex 5-3 getHead" {
        fun <T> FunList<T>.getHead(): T = when (this) {
            is Nil -> throw NoSuchElementException()
            is Cons -> this.head
        }

        Cons(1, Cons(2, Nil)).getHead() shouldBe 1
    }

    "Ex 5-4 drop for FunList" {
        tailrec fun <T> FunList<T>.drop(n: Int): FunList<T> = when (n) {
            0 -> this
            else -> when (this) {
                is Nil -> Nil
                is Cons -> this.tail.drop(n - 1)
            }
        }

        Cons(1, Cons(2, Cons(3, Nil))).drop(2) shouldBe Cons(3, Nil)
    }

    "Ex 5-5 dropWhile for FunList" {
        tailrec fun <T> FunList<T>.dropWhile(p: (T) -> Boolean): FunList<T> = when (this) {
            is Nil -> Nil
            is Cons -> if (p(this.head)) this.tail.dropWhile(p) else this
        }

        Cons(1, Cons(2, Cons(3, Nil))).dropWhile { it < 2 } shouldBe Cons(2, Cons(3, Nil))
    }

    "Ex 5-6 take for FunList" {
        tailrec fun <T> FunList<T>.take(n: Int, acc: FunList<T> = Nil): FunList<T> = when (n) {
            0 -> acc.reverse()
            else -> when (this) {
                is Nil -> acc
                is Cons -> this.tail.take(n - 1, acc.addHead(this.head))
            }
        }

        Cons(1, Cons(2, Cons(3, Nil))).take(2) shouldBe Cons(1, Cons(2, Nil))
    }

    "Ex 5-7 takeWhile for FunList" {
        tailrec fun <T> FunList<T>.takeWhile(p: (T) -> Boolean, acc: FunList<T> = Nil): FunList<T> = when (this) {
            is Nil -> acc.reverse()
            is Cons -> if (p(this.head)) this.tail.takeWhile(p, acc.addHead(this.head)) else this.tail.takeWhile(p, acc)
        }

        Cons(1, Cons(2, Cons(3, Nil))).takeWhile({ it <= 2 }) shouldBe Cons(1, Cons(2, Nil))
    }

    "Ex 5-8 indexedMap for FunList" {
        tailrec fun <T, R> FunList<T>.indexedMap(index: Int = 0, acc: FunList<R> = Nil, f: (Int, T) -> R): FunList<R> =
            when (this) {
                is Nil -> acc.reverse()
                is Cons -> this.tail.indexedMap(index + 1, acc.addHead(f(index, this.head)), f)
            }

        funListOf(1, 2, 3, 4, 5).indexedMap { index, value -> index * value } shouldBe funListOf(0, 2, 6, 12, 20)
    }

    "sum function with foldLeft" {
        fun sum(list: FunList<Int>): Int = list.foldLeft(0) { acc, value -> acc + value }
        sum(funListOf(1, 2, 3, 4, 5)) shouldBe 15
    }

    "toUpper function with foldLeft" {
        fun toUpper(list: FunList<Char>): FunList<Char> =
            list.foldLeft(Nil) { acc: FunList<Char>, char: Char -> acc.appendTail(char.uppercaseChar()) }
        toUpper(funListOf('a', 'b', 'c', 'd', 'e')) shouldBe funListOf('A', 'B', 'C', 'D', 'E')
    }

    "Ex 5-9 maximum function with foldLeft" {
        fun FunList<Int>.maximum(): Int = foldLeft(Int.MIN_VALUE) { acc, value -> if (acc > value) acc else value }
        funListOf(-10, 20, 34, 100, 50).maximum() shouldBe 100
    }

    "Ex 5-10 filter function with foldLeft" {
        fun <T> FunList<T>.filterByFoldLeft(p: (T) -> Boolean): FunList<T> =
            foldLeft(Nil) { acc: FunList<T>, value: T -> if (p(value)) acc.appendTail(value) else acc }
        funListOf(1, 2, 3, 4, 5).filterByFoldLeft { it % 2 == 0 } shouldBe funListOf(2, 4)
    }

    "Ex 5-11 reverse function with foldRight" {
        fun <T> FunList<T>.reverseByFoldRight(): FunList<T> =
            foldRight(Nil as FunList<T>) { x, acc -> acc.appendTail(x) }
        funListOf(1, 2, 3, 4, 5).reverseByFoldRight() shouldBe funListOf(5, 4, 3, 2, 1)
    }

    "Ex 5-12 filter function with foldRight" {
        fun <T> FunList<T>.filterByFoldRight(p: (T) -> Boolean): FunList<T> =
            foldRight(Nil as FunList<T>) { x, acc -> if (p(x)) acc.addHead(x) else acc }
        funListOf(1, 2, 3, 4, 5).filterByFoldRight { it % 2 == 0 } shouldBe funListOf(2, 4)
    }

    "Ex 5-14 associate function" {
        fun <T, R> FunList<T>.associate(f: (T) -> Pair<T, R>): Map<T, R> = foldLeft(emptyMap()) { acc, x -> acc + f(x) }
        funListOf(1, 2, 3, 4, 5).associate { it to it * 10 } shouldBe mapOf(1 to 10, 2 to 20, 3 to 30, 4 to 40, 5 to 50)
    }

    "Ex 5-15 groupBy function" {
        fun <T, K> FunList<T>.groupBy(f: (T) -> K): Map<K, FunList<T>> = foldRight(emptyMap()) { x, acc ->
            val key = f(x)
            acc + (key to (acc[key] ?: Nil).addHead(x))
        }

        funListOf(1, 2, 3, 4, 5).groupBy { it % 2 == 0 } shouldBe mapOf(
            true to funListOf(2, 4),
            false to funListOf(1, 3, 5)
        )
    }

    "Ex 5-17 sum with FunStream" {
        funStreamOf(1, 2, 3, 4, 5).sum() shouldBe 15
    }

    "Ex 5-18 product with FunStream" {
        funStreamOf(1, 2, 3, 4, 5).product() shouldBe 120
    }

    "Ex 5-19 appendTail with FunStream" {
        val funStream = funStreamOf(1, 2, 3, 4, 5)
        funStream.appendTail(6) shouldBe funStreamOf(1, 2, 3, 4, 5, 6)
    }

    "Ex 5-20 filter with FunStream" {
        funStreamOf(1, 2, 3, 4, 5).filter { it % 2 == 0 } shouldBe funStreamOf(2, 4)
    }

    "Ex 5-21 map with FunStream" {
        funStreamOf(1, 2, 3, 4, 5).map { it * 2 } shouldBe funStreamOf(2, 4, 6, 8, 10)
    }

    "Ex 5-22 take with FunStream" {
        generateFunStream(1) { it + 1 }.map { it * 2 }.filter { it % 3 == 0 }.take(3) shouldBe funStreamOf(6, 12, 18)
    }

    "Ex 5-23 toString with FunList" {
        tailrec fun <T> FunList<T>.toString(acc: String): String = when (this) {
            is Nil -> "[${acc.drop(2)}]"
            is Cons -> tail.toString("$acc, $head")
        }

        funListOf(1, 2, 3, 4, 5).toString("") shouldBe "[1, 2, 3, 4, 5]"
    }

    "toStringByFoldLeft For FunList" {
        fun <T> FunList<T>.toStringByFoldLeft(): String =
            foldLeft("") { acc, x -> "$acc, $x" }.drop(2).let { "[$it]" }

        funListOf(1, 2, 3, 4, 5).toStringByFoldLeft() shouldBe "[1, 2, 3, 4, 5]"
    }

    "Ex 5-24 find natural number x when sum of x^2 is larger than 1000" {
        tailrec fun FunStream<Int>.findNumberWhenSumOfSquareLargerThan1000(acc: Double): Int =
            if (acc < 1000) {
                this.getTail().findNumberWhenSumOfSquareLargerThan1000(acc + sqrt(this.getHead().toDouble()))
            } else this.getHead() - 1

        // Generalized Version
        tailrec fun <T> FunStream<T>.findIndexByAccumulation(
            acc: Double = 0.0,
            threshold: Double,
            index: Int = 0,
            f: (T) -> Double
        ): Int =
            when (this) {
                FunStream.Nil -> throw NoSuchElementException("Threshold not reached before stream ended.")
                is FunStream.Cons -> {
                    val h = this.head()
                    val newAcc = acc + f(h)
                    if (newAcc > threshold) index + 1
                    else this.tail().findIndexByAccumulation(newAcc, threshold, index + 1, f)
                }
            }

        generateFunStream(1) { it + 1 }.findNumberWhenSumOfSquareLargerThan1000(0.0) shouldBe 131
        generateFunStream(1) { it + 1 }.findIndexByAccumulation(0.0, 1000.0, 0){ sqrt(it.toDouble())} shouldBe 131


    }
})
