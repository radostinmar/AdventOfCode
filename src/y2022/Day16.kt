package y2022

import Day

object Day16 : Day(year = 2022, isTest = false) {

    private data class State(
        val current: Valve,
        val open: Set<Valve>,
        val minutes: Int
    )

    private val cache = mutableMapOf<State, Int>()

    private fun findBest(
        state: State,
        shortestPaths: Map<Valve, Map<Valve, Int>>,
        limit: Int
    ): Int {
        if (state.minutes == limit) {
            return 0
        }
        if (state.minutes > limit) {
            return Int.MIN_VALUE
        }
        cache[state]?.let { return it }
        return shortestPaths.getValue(state.current).maxOf {
            if(it.key in state.open) {
                Int.MIN_VALUE
            }
            val timeToReachAndOpen = it.value + 1
            state.open.sumOf(Valve::flowRate) * timeToReachAndOpen + findBest(
                state = state.copy(
                    current = it.key,
                    open = state.open + it.key,
                    minutes = state.minutes + timeToReachAndOpen
                ),
                shortestPaths = shortestPaths,
                limit = limit
            )
        }.also { cache[state] = it }
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
        val shortestPaths = floydWarshall(valves).mapValues {
            it.value
                .mapKeys { entry -> valvesMap.getValue(entry.key) }
                .filterKeys { key -> key in valvesWithPositiveFlowRate }
                .filterValues { value -> value != 0 }
        }
        return findBest(State(valvesMap.getValue("AA"), emptySet(), 0), shortestPaths, 30)
    }
}
