package util

import java.util.*
import kotlin.math.abs

data class Edge(val start: Point, val end: Point, val distance: Int = 1)

data class SearchResult(val distance: Int, val path: List<Point>)

fun manhattan(first: Point, second: Point): Int =
    abs(first.x - second.x) + abs(first.y - second.y)

fun bfs(start: Point, goal: Point, edges: Set<Edge>): SearchResult {
    val visited = mutableSetOf<Point>()
    val previous = mutableMapOf<Point, Point>()
    val queue: Queue<Point> = LinkedList()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val current = queue.remove()
        visited.add(current)
        if (current == goal) {
            val path = getPath(current, previous)
            return SearchResult(path.size - 1, path)
        }
        edges.filter {
            it.start == current && it.end !in visited
        }.forEach {
            previous[it.end] = current
            queue.add(it.end)
        }
    }
    return SearchResult(-1, emptyList())
}

fun dijkstra(start: Point, goal: Point, edges: Set<Edge>): SearchResult {
    val previous = mutableMapOf<Point, Point>()
    val distance = mutableMapOf<Point, Int>().withDefault { Int.MAX_VALUE }
    val nodes = edges.flatMap { listOf(it.start, it.end) }.distinct().toMutableList()
    distance[start] = 0
    while (nodes.isNotEmpty()) {
        val current = nodes.minBy { distance.getValue(it) }
        nodes.remove(current)
        if (current == goal) {
            return SearchResult(distance.getValue(goal), getPath(goal, previous))
        }
        edges.filter { it.start == current }.forEach { edge ->
            val currentDistance = distance.getValue(edge.end)
            val alternative = distance.getValue(current) + edge.distance
            if (currentDistance > alternative) {
                previous[edge.end] = current
                distance[edge.end] = alternative
            }
        }
    }
    return SearchResult(-1, emptyList())
}

fun aStar(start: Point, goal: Point, edges: Set<Edge>): SearchResult {
    val previous = mutableMapOf<Point, Point>()
    val distance = mutableMapOf<Point, Int>().withDefault { Int.MAX_VALUE }
    val guess = mutableMapOf<Point, Int>().withDefault { Int.MAX_VALUE }
    val nodes = mutableListOf(start)
    distance[start] = 0
    guess[start] = manhattan(start, goal)
    while (nodes.isNotEmpty()) {
        val current = nodes.minBy { guess.getValue(it) }
        nodes.remove(current)
        if (current == goal) {
            return SearchResult(distance.getValue(goal), getPath(goal, previous))
        }
        edges.filter { it.start == current }.forEach { edge ->
            val alternative = distance.getValue(current) + edge.distance
            if (alternative < distance.getValue(edge.end)) {
                previous[edge.end] = current
                distance[edge.end] = alternative
                guess[edge.end] = alternative + manhattan(edge.end, goal)
                if (edge.end !in nodes) {
                    nodes.add(edge.end)
                }
            }
        }
    }
    return SearchResult(-1, emptyList())
}

private fun getPath(point: Point, previous: Map<Point, Point>): List<Point> =
    generateSequence(point) { previous[it] }.toList()

