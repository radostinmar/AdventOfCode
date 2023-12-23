package y2022

import Day
import java.util.*

object Day16 : Day(year = 2022, isTest = true) {

    private fun findBest(start: Valve, valves: Map<String, Valve>): Int {
        val queue = PriorityQueue<State>()
        queue.add(State(start, emptyList(), 1, 0))
        var max = 0
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.minute == 30) {
                if (current.pressure > max) {
                    println(current)
                    max = current.pressure
                }
                continue
            }
            val released = current.open.sumOf(Valve::flowRate)
            if (current.valve.flowRate != 0 && current.valve !in current.open) {
                queue.add(
                    State(
                        valve = current.valve,
                        open = current.open + current.valve,
                        minute = current.minute + 1,
                        pressure = current.pressure + released
                    )
                )
            }
            current.valve.leadsTo.forEach {
                val newValve = valves.getValue(it)
                queue.add(
                    State(
                        valve = newValve,
                        open = current.open,
                        minute = current.minute + 1,
                        pressure = current.pressure + released
                    )
                )
            }
        }
        return max
    }

    data class State(
        val valve: Valve,
        val open: List<Valve>,
        val minute: Int,
        val pressure: Int
    ) : Comparable<State> {
        override fun compareTo(other: State): Int {
            val openResult = other.open.sumOf(Valve::flowRate).compareTo(this.open.sumOf(Valve::flowRate))

            return if (openResult != 0) {
                openResult
            } else {
                val pressureResult = other.pressure.compareTo(this.pressure)
                if (pressureResult != 0) {
                    pressureResult
                } else {
                    other.valve.flowRate.compareTo(this.valve.flowRate)
                }
            }
        }
    }

    data class Valve(
        val flowRate: Int,
        val leadsTo: List<String>
    )

    override fun part1(): Any {
        val valves = lines.associate {
            val (valveName, flowRateString, valvesString) =
                "Valve (.+) has flow rate=(.+); tunnels? leads? to valves? (.+)".toRegex()
                    .find(it)!!.groupValues.drop(1)
            valveName to Valve(
                flowRateString.toInt(),
                valvesString.split(", ")
            )
        }
        return findBest(valves.getValue("AA"), valves)
    }

}