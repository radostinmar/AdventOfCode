package util

import java.util.Collections.swap
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.math.pow
import kotlin.math.sqrt

fun readLines(name: String) = Path("src/$name.txt").readLines()

fun readText(name: String) = Path("src/$name.txt").readText()

fun <T> T.println(): T {
    println(this)
    return this
}

val Iterable<Int>.product: Int
    get() = this.reduce { acc, cur ->
        acc * cur
    }

val Iterable<Long>.product: Long
    get() = this.reduce { acc, cur ->
        acc * cur
    }

fun <T> List<T>.productOf(selector: (T) -> Int): Int =
    this.map { selector(it) }.product

fun <T> List<T>.split(delimiter: T): List<List<T>> {
    var currentOffset = 0
    var nextIndex = this.withIndex().indexOfFirst { it.value == delimiter && it.index >= 0 }
    if (nextIndex == -1) {
        return listOf(this)
    }

    val result = ArrayList<List<T>>(10)
    do {
        result.add(subList(currentOffset, nextIndex))
        currentOffset = nextIndex + 1
        nextIndex = this.withIndex().indexOfFirst { it.value == delimiter && it.index >= currentOffset }
    } while (nextIndex != -1)

    result.add(subList(currentOffset, this.size))
    return result
}

fun quadratic(a: Double, b: Double, c: Double): Pair<Double, Double> {
    val d = b.pow(2) - 4 * a * c
    val x1 = (-b + sqrt(d)) / (2 * a)
    val x2 = (-b - sqrt(d)) / (2 * a)
    return x1 to x2
}

private fun gcd(first: Long, second: Long): Long {
    var a = first
    var b = second
    while (b > 0) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}

fun List<Long>.gcd(): Long = reduce(::gcd)

private fun lcm(first: Long, second: Long): Long =
    first * (second / gcd(first, second))

fun List<Long>.lcm(): Long = reduce(::lcm)

fun <T> List<List<T>>.transposed(): List<List<T>> =
    this.first().indices.map { index ->
        this.map { it[index] }
    }

fun <T> accumulate(times: Int, initial: T, operation: (acc: T, index: Int) -> T): T =
    List(times) { it }.fold(initial) { acc, index ->
        operation(acc, index)
    }

fun MutableList<*>.swap(firstIndex: Int, secondIndex: Int) {
    swap(this, firstIndex, secondIndex)
}
