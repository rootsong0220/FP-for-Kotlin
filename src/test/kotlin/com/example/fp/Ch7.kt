package com.example.fp

import com.example.fp.functor.*
import com.example.fp.functor.FunList.Companion.funListOf
import com.example.fp.functor.Nothing
import com.example.fp.functor.Tree.EmptyTree
import com.example.fp.functor.Tree.EmptyTree.treeOf
import com.example.fp.functor.Tree.Node
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class Ch7 : StringSpec({
    "Ex 7-1 Functor FunList Test" {
        val list = funListOf(1, 2, 3)

        list.first() shouldBe 1
        list.size() shouldBe 3
        list.fmap { it * 2 } shouldBe funListOf(2, 4, 6)
    }

    "Functor Tree Test" {
        val tree = treeOf(
            1,
            treeOf(
                2,
                treeOf(3),
                treeOf(4)
            ),
            treeOf(
                5,
                treeOf(6),
                treeOf(7)
            )
        )

        println(tree)

        tree.fmap { it + 1 } shouldBe treeOf(
            2,
            treeOf(
                3,
                treeOf(4),
                treeOf(5)
            ),
            treeOf(
                6,
                treeOf(7),
                treeOf(8)
            )
        )
    }

    "Either Functor Test" {
        fun divideTenByN(n: Int): Either<String, Int> = try {
            Right(10 / n)
        } catch (e: ArithmeticException) {
            Left("Division by zero")
        }

        divideTenByN(5) shouldBeEqual Right(2)
        divideTenByN(0) shouldBeEqual Left("Division by zero")
        divideTenByN(5).fmap { it * 2 } shouldBeEqual Right(4)
        divideTenByN(0).fmap { it * 2 } shouldBeEqual Left("Division by zero")
    }

    "UnaryFunction Functor Test" {
        val f = { x: Int -> x + 1 }
        val g = { x: Int -> x * 2 }
        val fg = UnaryFunction(g).fmap(f)
        fg.invoke(5) shouldBe 11
    }

    "UnaryFunction Changing Functor" {
        val g = { a: Int -> a * 2 }
        val k = { b: Int -> Just(b) }
        val kg = UnaryFunction(g).fmap(k)

        kg.invoke(5) shouldBe Just(10)
    }

    "1st law of Functor (Identity)" {
        Nothing.fmap(::identity) shouldBe identity(Nothing)
        Just(1).fmap(::identity) shouldBe identity(Just(1))

        val tree =
            Node(
                1,
                Node(2, EmptyTree, EmptyTree),
                Node(3, EmptyTree, EmptyTree)
            )

        EmptyTree.fmap(::identity) shouldBe identity(EmptyTree)
        tree.fmap(::identity) shouldBe identity(tree)

        Left("error").fmap(::identity) shouldBe identity(Left("error"))
        Right(5).fmap(::identity) shouldBe identity(Right(5))
    }

    "2nd law of Functor (Composition)" {
        val f = { a: Int -> a + 1 }
        val g = { b: Int -> b * 2 }

        val nothingLeft = Nothing.fmap(f).fmap(g)
        // Compile error
        // val nothingRight = Nothing.fmap(f) compose Nothing.fmap(g)

        val nothingRight = Nothing.fmap(g).fmap(f)
        nothingLeft shouldBe nothingRight

        val justLeft = Just(5).fmap(f compose g)
        // Compile error
        // val justRight = Just(5).fmap(f) compose Just(0).fmap(g)
        val justRight = Just(5).fmap(g).fmap(f)
        justLeft shouldBe justRight

        val tree =
            Node(
                1,
                Node(2, EmptyTree, EmptyTree),
                Node(3, EmptyTree, EmptyTree)
            )

        EmptyTree.fmap(f compose g) shouldBe EmptyTree.fmap(g).fmap(f)
        tree.fmap(f compose g) shouldBe tree.fmap(g).fmap(f)

        Left("error").fmap(f compose g) shouldBe Left("error").fmap(g).fmap(f)
        Right(5).fmap(f compose g) shouldBe Right(5).fmap(g).fmap(f)
    }

    "Check if FunList satisfies Functor Laws" {
        val list = funListOf(1, 2, 3)
        val f = { a: Int -> a + 1 }
        val g = { b: Int -> b * 2 }

        // 1st law
        list.fmap(::identity) shouldBe identity(list)

        // 2nd law
        list.fmap(f compose g) shouldBe list.fmap(g).fmap(f)
        list.fmap(g compose f) shouldBe list.fmap(f).fmap(g)
    }

    "Check if MaybeCounter satisfies Functor Laws" {
        NothingCounter.fmap(::identity) shouldBe identity(NothingCounter)

        //JustCounter does not satisfy 1st law
        JustCounter(5, 0).fmap(::identity) shouldBe identity(JustCounter(5, 1))

        val f = { a: Int -> a + 1 }
        val g = { b: Int -> b * 2 }

        val nothingLeft = NothingCounter.fmap { f compose g }
        val nothingRight = NothingCounter.fmap(g).fmap(f)
        nothingLeft shouldBe nothingRight

        val justLeft = JustCounter(5, 0).fmap { f compose g }
        val justRight = JustCounter(5, 0).fmap(g).fmap(f)
        // JustCounter does not satisfy 2nd law
        justLeft shouldNotBe justRight

        // Functor의 법칙을 만족하는 Functor는 mapping 이외의 다른 동작이 없어야함
        // 다시말해 상태 변경과 같은 부수효과가 없어야함
    }

    "Functor with currying" {
        val product: (Int, Int) -> Int = { x: Int, y: Int -> x * y}
        val curriedProduct = product.curried()
        val maybeProductTen: Maybe<(Int) -> Int> = Just(10).fmap(curriedProduct)

        maybeProductTen.fmap { it(5) } shouldBe Just(50)
    }
})
