package com.example.fp

import com.example.fp.FunList.Nil.funListOf
import com.example.fp.foldable.Node
import com.example.fp.foldable.contains
import com.example.fp.foldable.toFunList
import com.example.fp.functor.Just
import com.example.fp.monoid.AllMonoid
import com.example.fp.monoid.AnyMonoid
import com.example.fp.monoid.FunListMonoid
import com.example.fp.monoid.MaybeMonoid
import com.example.fp.monoid.ProductMonoid
import com.example.fp.monoid.SumMonoid
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Ch9 : StringSpec({

    "Ex 9-3 AnyMonoid Satisfies Monoid Law" {
        val x = false
        val y = true
        val z = false

        AnyMonoid().run {
            (mappend(x, mempty())) shouldBe x
            (mappend(mempty(), x)) shouldBe x
            (mappend(x, mappend(y, z))) shouldBe mappend(mappend(x, y), z)
        }

    }

    "Ex 9-4 AllMonoid Satisfies Monoid Law" {
        val x = true
        val y = false
        val z = false

        AllMonoid().run {
            (mappend(x, mempty())) shouldBe x
            (mappend(mempty(), x)) shouldBe x
            (mappend(x, mappend(y, z))) shouldBe mappend(mappend(x, y), z)
        }
    }

    "mconcat test" {
        ProductMonoid().mconcat(funListOf(1, 2, 3, 4, 5)) shouldBe 120
        SumMonoid().mconcat(funListOf(1, 2, 3, 4, 5)) shouldBe 15
    }

    "Ex 9-5 AnyMonoid run mconcat" {
        AnyMonoid().run {
            mconcat(funListOf(true, true, true)) shouldBe true
            mconcat(funListOf(false, false, false)) shouldBe false
            mconcat(funListOf(true, false, true)) shouldBe true
        }
    }

    "Ex 9-6 AllMonoid run mconcat" {
        AllMonoid().run {
            mconcat(funListOf(true, true, true)) shouldBe true
            mconcat(funListOf(false, false, false)) shouldBe false
            mconcat(funListOf(true, false, true)) shouldBe false
        }
    }

    "MaybeMonoid satisfied Monoid Law" {
        val x = Just(1)
        val y = Just(2)
        val z = Just(3)

        MaybeMonoid.monoid(ProductMonoid()).run {
            (mappend(x, mempty())) shouldBe x
            (mappend(mempty(), x)) shouldBe x
            (mappend(x, mappend(y, z))) shouldBe mappend(mappend(x, y), z)
        }

        MaybeMonoid.monoid(SumMonoid()).run {
            (mappend(x, mempty())) shouldBe x
            (mappend(mempty(), x)) shouldBe x
            (mappend(x, mappend(y, z))) shouldBe mappend(mappend(x, y), z)
        }
    }

    "Ex 9-8 FunListMonoid satisfied Monoid Law" {
        val x = funListOf(1, 2, 3)
        val y = funListOf(4, 5, 6)
        val z = funListOf(7, 8, 9)

        FunListMonoid<Int>().run {
            mappend(x, mempty()) shouldBe x
            mappend(mempty(), x) shouldBe x
            mappend(x, mappend(y, z)) shouldBe mappend(mappend(x, y), z)
        }
    }

    "Ex 9-9 FunListMonoid satisfied Monoid Law" {
        val x = funListOf(funListOf(1, 2), funListOf(3, 4), funListOf(5))

        FunListMonoid<Int>().run {
            mconcat(x) shouldBe funListOf(1, 2, 3, 4, 5)
        }
    }

    "BinaryTree with Foldable" {
        val tree = Node(
            1,
            Node(
                2,
                Node(3), Node(4)
            ),
            Node(
                5,
                Node(6), Node(7)
            )
        )

        tree.foldLeft(0) { a, b -> a + b } shouldBe 28
        tree.foldLeft(1) { a, b -> a * b } shouldBe 5040

        tree.foldMap({ it * 2 }, SumMonoid()) shouldBe 56
        tree.foldMap({ it + 1 }, ProductMonoid()) shouldBe 40320

        tree.contains(3) shouldBe true
        tree.contains(8) shouldBe false

        tree.toFunList() shouldBe funListOf(3, 2, 4, 1, 6, 5, 7)
    }
})