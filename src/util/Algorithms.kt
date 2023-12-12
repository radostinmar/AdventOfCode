package util

import java.util.*


data class Node(val x: Long, val y: Long)

data class Edge(val start: Node, val end: Node, val distance: Int = 1)

fun getPath(node: Node, previous: Map<Node, Node>): List<Node> =
    generateSequence(node) { previous[it] }.toList()

fun bfs(start: Node, goal: Node, edges: Set<Edge>): Int {
    val visited = mutableSetOf<Node>()
    val previous = mutableMapOf<Node, Node>()
    val queue: Queue<Node> = LinkedList()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val current = queue.remove()
        visited.add(current)
        if (current == goal) {
            return getPath(current, previous).size - 1
        }
        edges.filter {
            it.start == current && it.end !in visited
        }.forEach {
            previous[it.end] = current
            queue.add(it.end)
        }
    }
    return -1
}

fun dijkstra(start: Node, goal: Node, edges: Set<Edge>): Long {
    val previous = mutableMapOf<Node, Node>()
    val distance = mutableMapOf<Node, Long>().withDefault { Long.MAX_VALUE }
    val nodes = edges.flatMap { listOf(it.start, it.end) }.distinct().toMutableList()
    distance[start] = 0
    while (nodes.isNotEmpty()) {
        val current = nodes.minBy { distance.getValue(it) }
        nodes.remove(current)
        if (current == goal) {
            return distance.getValue(goal)
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
    return -1
}