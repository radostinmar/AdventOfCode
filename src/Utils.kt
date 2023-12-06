import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readLines(name: String) = Path("src/$name.txt").readLines()

fun readText(name: String) = Path("src/$name.txt").readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun <T> T.println(): T {
    println(this)
    return this
}

val Iterable<Int>.product: Int
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

fun List<String>.toLongs(): List<Long> = this.map { it.toLong() }

fun List<String>.toInts(): List<Int> = this.map { it.toInt() }