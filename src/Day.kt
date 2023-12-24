import util.readLines
import util.readText
import java.time.Year
import kotlin.system.measureTimeMillis

abstract class Day(
    protected val isTest: Boolean = false,
    year: Int = Year.now().value
) {

    private val file: String = if (isTest) "test" else "y$year/${this::class.simpleName.orEmpty()}"

    protected val lines: List<String> by lazy { readLines(file) }

    protected val text: String by lazy { readText(file) }

    protected open fun part1(): Any = ""

    protected open fun part2(): Any = ""

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