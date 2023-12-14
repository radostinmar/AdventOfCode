package util

import java.math.BigInteger
import java.security.MessageDigest

fun List<String>.transposed(): List<String> =
    this.first().indices.map { index ->
        this.map { it[index] }.joinToString("")
    }

fun List<String>.transposedToChars(): List<List<Char>> =
    this.first().indices.map { index ->
        this.map { it[index] }
    }

fun List<String>.toLongs(): List<Long> = this.map { it.toLong() }

fun List<String>.toInts(): List<Int> = this.map { it.toInt() }

fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
