package com.example.fp

import com.example.fp.Tree.EmptyTree
import com.example.fp.Tree.Node

sealed class Tree<out T> {
    object EmptyTree : Tree<Nothing>()
    // left, right 가 불변인 경우 Node를 매번 생성해야하므로 꼬리재귀만으로는 Stack에 대해 안전하게 구현이 불가능
    // 불변을 유지할 이유가 있다면, Tree depth가 깊지 않은 경우에 대해서만 사용하거나 Balanced Binary Search Tree를 사용하는 것을 권장
    data class Node<T>(val value: T, var left: Tree<T>, var right: Tree<T>) : Tree<T>()
}

fun Tree<Int>.insert(elem: Int): Tree<Int> =  when (this) {
    is EmptyTree -> Node(elem, EmptyTree, EmptyTree)
    is Node -> when {
        elem <= value -> Node(value, left.insert(elem), right)
        else -> Node(value, left, right.insert(elem))
    }
}

// 결과적으로 반드시 꼬리재귀로 구현해야할 이유는 없어보임
fun Tree<Int>.insertTailRec(elem: Int): Tree<Int> {
    if (this is EmptyTree)
        return Node(elem, EmptyTree, EmptyTree)

    val root = this as Node<Int>
    tailrec fun go(cur: Node<Int>) {
        if (elem <= cur.value) {
            when (val l = cur.left) {
                is EmptyTree -> {
                    cur.left = Node(elem, EmptyTree, EmptyTree)
                    return
                }
                is Node -> go(l)
            }
        } else {
            when (val r = cur.right) {
                is EmptyTree -> {
                    cur.right = Node(elem, EmptyTree, EmptyTree)
                    return
                }
                is Node -> go(r)
            }
        }
    }
    go(root)
    return root
}

// 가독성 측면에서는 반복 루프가 더 나아 보임. 꼬리재귀 구현보다 목적이 명확하게 드러남
fun Tree<Int>.loopInsert(elem: Int): Tree<Int> {
    if (this is EmptyTree) return Node(elem, EmptyTree, EmptyTree)

    var cur = this as Node<Int>
    while (true) {
        cur = if (elem <= cur.value) {
            when (val l = cur.left) {
                is EmptyTree -> {
                    cur.left = Node(elem, EmptyTree, EmptyTree)
                    return this
                }
                is Node -> l
            }
        } else {
            when (val r = cur.right) {
                is EmptyTree -> {
                    cur.right = Node(elem, EmptyTree, EmptyTree)
                    return this
                }
                is Node -> r
            }
        }
    }
}

fun Tree<Int>.contains(elem: Int): Boolean = when (this) {
    EmptyTree -> false
    is Node -> when {
        elem == value -> true
        elem <= value -> left.contains(elem)
        else -> right.contains(elem)
    }
}
