package y2023

import Day
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.writeLines

object Day25 : Day(year = 2023) {

    private fun findReachable(start: String, vertices: Map<String, List<String>>): Int {
        val visited = mutableSetOf<String>()
        val queue: Queue<String> = LinkedList()
        queue.add(start)
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current in visited) continue
            visited.add(current)
            vertices.getValue(current).filter { it !in visited }.forEach(queue::add)
        }
        return visited.size
    }
    /**
     * Used for generating dot file for GraphViz. Then add the three edges to the excluded set manually.
     * ```
     * dot -Tsvg day25.dot -o day25.svg
     * ```
     */
    override fun part2(): Any {
        val output = lines.flatMap { line ->
            val start = line.substringBefore(':')
            val ends = line.substringAfter(": ").split(' ')
            ends.map {
                "$start -> $it"
            }
        }
        Path("src/y2023/day25.dot").writeLines(
            listOf("digraph G {") + output + listOf("layout=neato", "}")
        )
        return 0
    }

    override fun part1(): Any {
        val excluded = listOf(
            "ltn" to "trh",
            "psj" to "fdb",
            "rmt" to "nqh"
        )
        val vertices = mutableMapOf<String, List<String>>()
        lines.forEach { line ->
            val start = line.substringBefore(':')
            val ends = line.substringAfter(": ").split(' ')
            ends.forEach {
                if (start to it !in excluded) {
                    vertices.merge(start, listOf(it), List<String>::plus)
                    vertices.merge(it, listOf(start), List<String>::plus)
                }
            }
        }
        return findReachable("ltn", vertices) * findReachable("trh", vertices)
    }
}
