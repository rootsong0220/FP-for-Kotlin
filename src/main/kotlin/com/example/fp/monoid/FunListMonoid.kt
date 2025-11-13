package com.example.fp.monoid

import com.example.fp.FunList
import com.example.fp.FunList.Nil
import com.example.fp.addHead
import com.example.fp.foldRight


// Ex 9-7
class FunListMonoid<T>: Monoid<FunList<T>> {
    override fun mempty(): FunList<T> = Nil

    override fun mappend(m1: FunList<T>, m2: FunList<T>): FunList<T> = m1.foldRight(m2) { x, xs -> xs.addHead(x) }
}