package com.example.fp.applicative

sealed class AFunList<out T>: Applicative<T> {

    companion object {
        fun <V> pure(value: V): Applicative<V> = ACons(value, ANil)
        fun <T> funListOf(vararg elements: T): AFunList<T> = elements.toFunList()

        private fun <T> Array<out T>.toFunList(): AFunList<T> = when {
            this.isEmpty() -> ANil
            else -> ACons(this[0], this.copyOfRange(1, this.size).toFunList())
        }
    }

    override fun <V> pure(value: V): Applicative<V> = ACons(value, ANil)
    abstract override fun <B> fmap(f: (T) -> B): AFunList<B>
    abstract override fun <B> apply(a: Applicative<(T) -> B>): AFunList<B>

}

object ANil : AFunList<Nothing>() {
    override fun toString(): String = "ANil"
    override fun <B> fmap(f: (Nothing) -> B): AFunList<B> = ANil
    override fun <B> apply(a: Applicative<(Nothing) -> B>): AFunList<B> = when (a) {
        is ACons -> ANil
        else -> ANil
    }

}

data class ACons<T>(val head: T, val tail: AFunList<T>) : AFunList<T>() {

    override fun toString(): String = "ACons($head, $tail)"
    override fun <B> fmap(f: (T) -> B): AFunList<B> = ACons(f(head), tail.fmap(f))

    override fun <B> apply(a: Applicative<(T) -> B>): AFunList<B> = when (a) {
        is ACons -> appendAll(fmap(a.head), tail.apply(a))
        else -> ANil
    }

    private fun <B> appendAll(list1: AFunList<B>, list2: AFunList<B>): AFunList<B> = when (list1) {
        is ANil -> list2
        is ACons -> ACons(list1.head, appendAll(list1.tail, list2))
    }
}

fun <A, B, R> liftA2(binaryFunction: (A, B) -> R) = { f1: AFunList<A>, f2: AFunList<B> ->
    f2 apply f1.fmap { a: A -> { b: B -> binaryFunction(a, b)} }
}