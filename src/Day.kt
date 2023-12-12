import util.readLines
import util.readText
import kotlin.system.measureTimeMillis

abstract class Day(isTest: Boolean = false) {

    private val file: String = if (isTest) "y2023/test" else "y2023/${this::class.simpleName.orEmpty()}"

    protected val lines: List<String> by lazy { readLines(file) }

    protected val text: String by lazy { readText(file) }

    protected open fun part1(): Number = 0

    protected open fun part2(): Number = 0

    operator fun invoke() {
        println("● ${this::class.simpleName.orEmpty()}")
        println("   ○ Part 1")
        measureTimeMillis {
            println("       Solution ${part1()}")
        }.let { println("       Time $it") }

        println("   ○ Part 2")
        measureTimeMillis {
            println("       Solution ${part2()}")
        }.let { println("       Time $it") }
    }
}