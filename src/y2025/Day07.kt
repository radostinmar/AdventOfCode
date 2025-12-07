package y2025

import Day

object Day07 : Day(year = 2025, isTest = false) {
    override fun part1(): Any {
        val beamPositions = mutableSetOf<Int>()
        var splits = 0
        lines.forEach { line ->
            line.forEachIndexed { index, char ->
                if (char == 'S') {
                    beamPositions.add(index)
                } else if (char == '^' && index in beamPositions) {
                    beamPositions.remove(index)
                    beamPositions.add(index - 1)
                    beamPositions.add(index + 1)
                    splits++
                }
            }
        }
        return splits
    }

    override fun part2(): Long =
        getTimelines(State(position = lines.first().indexOf('S'), depth = 1))

    private val cache = mutableMapOf<State, Long>()

    private fun getTimelines(state: State): Long =
        cache.getOrPut(state) {
            if (state.depth > lines.lastIndex) {
                1
            } else {
                val char = lines[state.depth][state.position]
                if (char == '^') {
                    getTimelines(State(position = state.position - 1, depth = state.depth + 1)) +
                            getTimelines(State(position = state.position + 1, depth = state.depth + 1))
                } else {
                    getTimelines(state.copy(depth = state.depth + 1))
                }
            }
        }

    private data class State(
        val position: Int,
        val depth: Int,
    )
}
