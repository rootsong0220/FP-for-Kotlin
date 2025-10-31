package com.example.fp

import com.example.fp.applicative.AJust
import com.example.fp.applicative.AMaybe
import com.example.fp.applicative.ANothing
import com.example.fp.functor.FunList
import com.example.fp.functor.FunList.Nil.funListOf
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Ch8 : StringSpec({
    "Ex 8-1 " {
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
})
