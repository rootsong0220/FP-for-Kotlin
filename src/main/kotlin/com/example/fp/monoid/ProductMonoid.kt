package com.example.fp.monoid

class ProductMonoid: Monoid<Int> {
    override fun mempty(): Int = 1

    override fun mappend(m1: Int, m2: Int): Int = m1 * m2
}