package util

import java.util.*
import kotlin.math.abs

data class Node(val x: Long, val y: Long) {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())
}

infix fun Long.nodeTo(that: Long): Node = Node(this, that)
infix fun Int.nodeTo(that: Int): Node = Node(this, that)

data class Edge(val start: Node, val end: Node, val distance: Long = 1)

data class SearchResult(val distance: Long, val path: List<Node>)

fun manhattan(first: Node, second: Node): Long =
    abs(first.x - second.x) + abs(first.y - second.y)

fun bfs(start: Node, goal: Node, edges: Set<Edge>): SearchResult {
    val visited = mutableSetOf<Node>()
    val previous = mutableMapOf<Node, Node>()
    val queue: Queue<Node> = LinkedList()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val current = queue.remove()
        visited.add(current)
        if (current == goal) {
            val path = getPath(current, previous)
            return SearchResult(path.size - 1L, path)
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

fun dijkstra(start: Node, goal: Node, edges: Set<Edge>): SearchResult {
    val previous = mutableMapOf<Node, Node>()
    val distance = mutableMapOf<Node, Long>().withDefault { Long.MAX_VALUE }
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

fun aStar(start: Node, goal: Node, edges: Set<Edge>): SearchResult {
    val previous = mutableMapOf<Node, Node>()
    val distance = mutableMapOf<Node, Long>().withDefault { Long.MAX_VALUE }
    val guess = mutableMapOf<Node, Long>().withDefault { Long.MAX_VALUE }
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

private fun getPath(node: Node, previous: Map<Node, Node>): List<Node> =
    generateSequence(node) { previous[it] }.toList()

