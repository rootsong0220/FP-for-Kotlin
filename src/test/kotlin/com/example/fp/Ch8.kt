package com.example.fp

import com.example.fp.applicative.ACons
import com.example.fp.applicative.AFunList
import com.example.fp.applicative.AJust
import com.example.fp.applicative.AMaybe
import com.example.fp.applicative.ANil
import com.example.fp.applicative.ANothing
import com.example.fp.applicative.Node
import com.example.fp.applicative.Tree
import com.example.fp.applicative.ZipList.Companion.zipListOf
import com.example.fp.applicative.apply
import com.example.fp.applicative.pure
import com.example.fp.functor.Either
import com.example.fp.functor.FunList
import com.example.fp.functor.FunList.Companion.funListOf
import com.example.fp.functor.Just
import com.example.fp.functor.Left
import com.example.fp.functor.Maybe
import com.example.fp.functor.Nothing
import com.example.fp.functor.Nothing.sequenceA
import com.example.fp.functor.Right
import com.example.fp.functor.append
import com.example.fp.functor.apply
import com.example.fp.functor.pure
import com.example.fp.functor.sequenceAByFoldRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Ch8 : StringSpec({
    "Ex 8-1" {
        val ints = funListOf(1, 2, 3, 4)
        fun product(list: FunList<Int>): (Int) -> FunList<Int> = { z -> list.fmap { x -> x * z } }
        product(ints)(5) shouldBe funListOf(5, 10, 15, 20)
    }

    "Maybe Applicative Examples" {
        AJust(10).fmap { it + 10 } shouldBe AJust(20)
        ANothing.fmap { x: Int -> x + 10 } shouldBe ANothing

        AMaybe.pure(10) shouldBe AJust(10)

        AJust(10) apply AJust { x: Int -> x + 10 } shouldBe AJust(20)
        ANothing apply AJust { x: Int -> x + 10 } shouldBe ANothing
    }

    "Ex 8-2 Applicative Functors in AFunList" {
        val pure = AFunList.pure(10)
        pure shouldBe ACons(10, ANil)
        ANil.fmap { x: Int -> x + 10 } shouldBe ANil

        val list = AFunList.funListOf(1, 2, 3, 4)
        list.fmap { x: Int -> x * 50 } shouldBe ACons(50, ACons(100, ACons(150, ACons(200, ANil))))

    }

    "Maybe Applicative Functors in Maybe" {
        Just(10).fmap { it + 10 } shouldBe Just(20)
        Nothing.fmap { x: Int -> x + 10 } shouldBe Nothing

        Maybe.pure(10) shouldBe Just(10)

        Maybe.pure { x: Int -> x * 2 } apply Just(10) shouldBe Just(20)
        Maybe.pure { x: Int -> x * 2 } apply Nothing shouldBe Nothing

        Maybe.pure({ x: Int, y: Int -> x * y }.curried()) apply Just(10) apply Just(20) shouldBe Just(200)
        Maybe.pure({ x: Int, y: Int, z: Int -> x * y + z }.curried()) apply Just(10) apply Just(20) apply Just(30) shouldBe Just(
            230
        )
    }

    "Ex 8-3 FunList Applicative Functors" {
        val list = funListOf(1, 2, 3, 4)
        val list2 = funListOf(5, 6, 7, 8)
        list.append(list2) shouldBe funListOf(1, 2, 3, 4, 5, 6, 7, 8)

        val applicative = funListOf({ x: Int -> x * 20 })
        applicative apply list shouldBe funListOf(20, 40, 60, 80)
    }

    "Tree Test" {
        val tree = Node(1, listOf(Node(2), Node(3)))
        tree.fmap { it * 2 } shouldBe Node(2, listOf(Node(4), Node(6)))
    }

    "Applicative Tree Test" {
        val tree = Node(1, listOf(Node(2), Node(3)))
        Tree.pure { x: Int -> x * 2 } apply tree shouldBe tree.fmap { it * 2 }

        Tree.pure({ x: Int, y: Int -> x * y }.curried()) apply
                Node(1, listOf(Node(2), Node(3))) apply
                Node(4, listOf(Node(5), Node(6))) shouldBe
                Node(
                    4, listOf(
                        Node(5), Node(6),
                        Node(8, listOf(Node(10), Node(12))),
                        Node(12, listOf(Node(15), Node(18)))
                    )
                )
    }

    "Ex 8-4 Tree Applied inversely" {
        Tree.pure({ x: Int, y: Int -> x * y }.curried()) apply
                Node(4, listOf(Node(5), Node(6))) apply
                Node(1, listOf(Node(2), Node(3))) shouldBe
                Node(
                    4, listOf(
                        Node(8), Node(12),
                        Node(5, listOf(Node(10), Node(15))),
                        Node(6, listOf(Node(12), Node(18)))
                    )
                )
    }

    "Ex 8-5 Tree Combined Applicative Functors" {
        Tree.pure({ x: Int, y: Int -> x to y }.curried()) apply
                Node(1, listOf(Node(2, listOf(Node(3))), Node(4))) apply
                Node(5, listOf(Node(6), Node(7, listOf(Node(8), Node(9))))) shouldBe
                Node(
                    1 to 5,
                    listOf(
                        Node(1 to 6),
                        Node(1 to 7, listOf(Node(1 to 8), Node(1 to 9))),
                        Node(
                            2 to 5, listOf(
                                Node(2 to 6), Node(2 to 7, listOf(Node(2 to 8), Node(2 to 9))),
                                Node(3 to 5, listOf(Node(3 to 6), Node(3 to 7, listOf(Node(3 to 8), Node(3 to 9)))))
                            )
                        ),
                        Node(4 to 5, listOf(Node(4 to 6), Node(4 to 7, listOf(Node(4 to 8), Node(4 to 9))))),
                    )
                )
    }

    "Ex 8-6 ZipList Applicative Functors" {
        val zipList1 = zipListOf({ x: Int -> x * 5 }, { x: Int -> x + 10 })
        val zipList2 = zipListOf(5, 6, 7, 8)
        zipList1 apply zipList2 shouldBe zipListOf(25, 16)
    }

    "Applicative Either" {
        Either.pure { x: Int -> x * 2 } apply Left("error") shouldBe Left("error")
        Either.pure { x: Int -> x * 2 } apply Right(10) shouldBe Right(20)
        Either.pure({ x: Int, y: Int -> x * y }.curried()) apply Left("error") apply Right(10) shouldBe Left("error")
        Either.pure({ x: Int, y: Int -> x * y }.curried()) apply Right(10) apply Right(20) shouldBe Right(200)

    }

    "Identity Law for Applicative Functor" {
        fun identity() = { x: Int -> x }
        val maybeAf = Just(10)
        val leftMaybe = Maybe.pure(identity()) apply maybeAf
        leftMaybe shouldBe maybeAf

        val treeAf = Node(1, listOf(Node(2), Node(3)))
        val leftTree = Tree.pure(identity()) apply treeAf
        leftTree shouldBe treeAf

        val eitherAf = Right(10)
        val leftEither = Either.pure(identity()) apply eitherAf
        leftEither shouldBe eitherAf
    }

    "Ex 8-7 AFunList satisfies Identity law" {
        val list = funListOf(1, 2, 3, 4)
        val id = { x: Int -> x }
        val idList = FunList.pure(id) apply list
        idList shouldBe list
    }

    "Composition Law for Applicative Functor" {
        fun <P1, P2, P3> compose() = { f: (P2) -> P3, g: (P1) -> P2, v: P1 -> f(g(v)) }
        fun <P1, P2, P3, R> ((P1, P2, P3) -> R).curried(): (P1) -> (P2) -> (P3) -> R = { p1 ->
            { p2 -> { p3 -> this(p1, p2, p3) } }
        }

        val maybeAf1 = Just { x: Int -> x * 2 }
        val maybeAf2 = Just { x: Int -> x + 1 }
        val maybeAf3 = Just(30)
        val leftMaybe = Maybe.pure(compose<Int, Int, Int>().curried()) apply maybeAf1 apply maybeAf2 apply maybeAf3
        val rightMaybe = maybeAf1 apply (maybeAf2 apply maybeAf3)

        leftMaybe shouldBe rightMaybe

        val treeAf1 = Node({ x: Int -> x * 2 })
        val treeAf2 = Node({ x: Int -> x + 1 })
        val treeAf3 = Node(10)
        val leftTree = Tree.pure(compose<Int, Int, Int>().curried()) apply treeAf1 apply treeAf2 apply treeAf3
        val rightTree = treeAf1 apply (treeAf2 apply treeAf3)
        leftTree shouldBe rightTree

        val eitherAf1 = Right { x: Int -> x * 2 }
        val eitherAf2 = Right { x: Int -> x + 1 }
        val eitherAf3 = Right(30)
        val leftEither = Either.pure(compose<Int, Int, Int>().curried()) apply eitherAf1 apply eitherAf2 apply eitherAf3
        val rightEither = eitherAf1 apply (eitherAf2 apply eitherAf3)
        leftEither shouldBe rightEither
    }

    "Homomorphism For Applicative Functor" {
        val f = { x: Int -> x * 2 }
        val x = 10

        val leftMaybe = Maybe.pure(f) apply Maybe.pure(x)
        val rightMaybe = Maybe.pure(f(x))
        leftMaybe shouldBe rightMaybe

        val leftTree = Tree.pure(f) apply Tree.pure(x)
        val rightTree = Tree.pure(f(x))
        leftTree shouldBe rightTree

        val leftEither = Either.pure(f) apply Either.pure(x)
        val rightEither = Either.pure(f(x))
        leftEither shouldBe rightEither
    }

    "Interchange Law For Applicative Functor" {
        val x = 10
        fun <T, R> of(value: T) = { f: (T) -> R -> f(value) }

        val maybeAf = Just { a: Int -> a * 2 }
        val leftMaybe = maybeAf apply Maybe.pure(x)
        val rightMaybe = Maybe.pure(of<Int, Int>(x)) apply maybeAf
        leftMaybe shouldBe rightMaybe

        val treeAf = Node({ a: Int -> a * 2 })
        val leftTree = treeAf apply Tree.pure(x)
        val rightTree = Tree.pure(of<Int, Int>(x)) apply treeAf
        leftTree shouldBe rightTree

        val eitherAf = Right { a: Int -> a * 2 }
        val leftEither = eitherAf apply Either.pure(x)
        val rightEither = Either.pure(of<Int, Int>(x)) apply eitherAf
        leftEither shouldBe rightEither
    }

    "sequenceA for Maybe" {
        when (val result = sequenceA(funListOf(Just(10), Just(20)))) {
            is Nothing -> Nothing
            is Just -> result.value
        } shouldBe funListOf(10, 20)

        when (val result = sequenceAByFoldRight(funListOf(Just(10), Just(20)))) {
            is Nothing -> Nothing
            is Just -> result.value
        } shouldBe funListOf(10, 20)
    }
})
