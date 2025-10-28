package com.example.fp

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CommonTest: StringSpec({
    // 문자열
    "String head returns first char" {
        "abc".head() shouldBe 'a'
    }

    "String tail returns rest" {
        "abc".tail() shouldBe "bc"
    }

    // 리스트
    "List head returns first element" {
        listOf(1, 2, 3).head() shouldBe 1
    }

    "List tail returns remaining elements" {
        listOf(1, 2, 3).tail() shouldBe listOf(2, 3)
    }
})