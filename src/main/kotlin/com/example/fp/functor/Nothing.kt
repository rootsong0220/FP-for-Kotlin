package com.example.fp.functor

import kotlin.Nothing

object Nothing: Maybe<Nothing>() {
    override fun <B> fmap(f: (Nothing) -> B): Maybe<B> = Nothing
    override fun toString(): String = "Nothing"
}
