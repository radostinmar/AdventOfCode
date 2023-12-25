import util.readLines
import util.readText
import java.time.Year
import kotlin.system.measureNanoTime

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
        measureNanoTime {
            println("       Solution ${part1()}")
        }.let { println("       Time ${it}ns | ${it / 1000f}μs | ${it / 1000000f}ms | ${it / 1000000000f}s") }

        println("   ○ Part 2")
        measureNanoTime {
            println("       Solution ${part2()}")
        }.let { println("       Time ${it}ns | ${it / 1000f}μs | ${it / 1000000f}ms | ${it / 1000000000f}s") }
    }
}