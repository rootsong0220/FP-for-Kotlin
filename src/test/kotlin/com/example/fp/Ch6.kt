package com.example.fp

import com.example.fp.Tree.EmptyTree
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class Ch6: StringSpec({

    "Ex 6-3 insert Tree n times" {
        val times = 10000
        generateSequence(1) { it + 1 }.take(times).fold(EmptyTree as Tree<Int>) { t, i -> t.insert(i)}
    }

    "Ex 6-4 insertTailRec Tree n times" {
        val times = 140000
        generateSequence(1) { it + 1 }.take(times).fold(EmptyTree as Tree<Int>) { t, i -> t.insertTailRec(i)}
    }

    "loopInsert Tree n times" {
        val times = 140000
        generateSequence(1) { it + 1 }.take(times).fold(EmptyTree as Tree<Int>) { t, i -> t.loopInsert(i)}
    }

    "check if tree contains the specified element" {
        val tree = generateSequence(1) { it + 1 }.take(500).fold(EmptyTree as Tree<Int>) { t, i -> t.loopInsert(i)}

        tree.contains(100) shouldBe true
        tree.contains(1000) shouldBe false
    }
})
