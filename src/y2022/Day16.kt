package y2022

import Day

object Day16 : Day(year = 2022, isTest = false) {

    private data class State(
        val current: Valve,
        val open: Set<Valve>,
        val minutes: Int
    )

    private val cache = mutableMapOf<State, Int>()
    private val cacheElephant = mutableMapOf<Pair<State, Set<String>>, Int>()

    private fun findBest(
        state: State,
        shortestPaths: Map<Valve, Map<Valve, Int>>,
        limit: Int,
        currentSum: Int,
        isElephant: Boolean
    ): Int {
        val elephantKey = if (!isElephant) {
            cache[state]?.let { return it }
        } else {
            val key = shortestPaths.keys.map(Valve::name).toSet()
            cacheElephant[state to key]?.let {
                return it
            }
            key
        }
        if (state.minutes == limit) {
            val subset = shortestPaths
                .filterKeys { key -> key !in state.open }
                .mapValues { it.value.filterKeys { key -> key !in state.open } }
            if (subset.size == 1) {
                return 0
            }
            val start = subset.keys.first { it.name == "AA" }
            return findBest(State(start, emptySet(), 0), subset, limit, currentSum, isElephant = true)
        }
        if (state.minutes > limit) {
            return Int.MIN_VALUE
        }
        val switch = shortestPaths.getValue(state.current).filter {
            it.key !in state.open
        }.map {
            val timeToReachAndOpen = it.value + 1
            val result = state.open.sumOf(Valve::flowRate) * timeToReachAndOpen + findBest(
                state = state.copy(
                    current = it.key,
                    open = state.open + it.key,
                    minutes = state.minutes + timeToReachAndOpen
                ),
                shortestPaths = shortestPaths,
                limit = limit,
                currentSum = currentSum + state.open.sumOf(Valve::flowRate) * timeToReachAndOpen,
                isElephant
            )
            result
        }
        return if (isElephant) {
            switch.ifEmpty {
                val minutesLeft = limit - state.minutes
                return (state.open.sumOf(Valve::flowRate) * minutesLeft).also { cacheElephant[state to elephantKey!!] = it }
            }.max().also { cacheElephant[state to elephantKey!!] = it }
        } else {
            val minutesLeft = limit - state.minutes
            val stay = state.open.sumOf(Valve::flowRate) * minutesLeft + findBest(
                state.copy(minutes = limit),
                shortestPaths,
                limit,
                currentSum + state.open.sumOf(Valve::flowRate) * minutesLeft,
                false
            )
            (switch + stay).max().also { cache[state] = it }
        }
    }

    private fun floydWarshall(valves: List<Valve>): Map<Valve, Map<String, Int>> {
        val distances = valves.associateWith { mutableMapOf(it.name to 0).withDefault { 100 } }.toMutableMap()
        valves.forEach { valve ->
            valve.leadsTo.forEach { adjacent ->
                distances.getValue(valve)[adjacent] = 1
            }
        }
        for (k in valves) {
            for (i in valves) {
                for (j in valves) {
                    if (
                        distances.getValue(i).getValue(j.name) >
                        distances.getValue(i).getValue(k.name) + distances.getValue(k).getValue(j.name)
                    ) {
                        distances.getValue(i)[j.name] =
                            distances.getValue(i).getValue(k.name) + distances.getValue(k).getValue(j.name)
                    }
                }
            }
        }
        return distances
    }

    data class Valve(
        val name: String,
        val flowRate: Int,
        val leadsTo: List<String>
    )

    override fun part1(): Any {
        val valves = lines.map {
            val (valveName, flowRateString, valvesString) =
                "Valve (.+) has flow rate=(.+); tunnels? leads? to valves? (.+)".toRegex()
                    .find(it)!!.groupValues.drop(1)
            Valve(
                valveName,
                flowRateString.toInt(),
                valvesString.split(", ")
            )
        }
        val valvesMap = valves.associateBy { it.name }
        val valvesWithPositiveFlowRate = valves.filter { it.flowRate > 0 }.toSet()
        val shortestPaths = floydWarshall(valves)
            .filterKeys { key -> key in valvesWithPositiveFlowRate || key.name == "AA" }
            .mapValues {
                it.value
                    .mapKeys { entry -> valvesMap.getValue(entry.key) }
                    .filterKeys { key -> key in valvesWithPositiveFlowRate }
                    .filterValues { value -> value != 0 }
            }
        return findBest(State(valvesMap.getValue("AA"), emptySet(), 0), shortestPaths, 26, 0, isElephant = false)
    }
}
