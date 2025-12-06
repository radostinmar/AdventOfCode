package y2023

import Day
import util.product

object Day19 : Day(year = 2023) {

    private data class Part(val ratings: Map<String, Int>)

    private data class RangePart(val ratings: MutableMap<String, IntRange>)

    private data class Check(val field: String, val comparison: Char?, val value: Int, val target: String)

    private data class WorkFlow(val name: String, val checks: List<Check>) {
        fun evaluate(rangePart: RangePart): List<Pair<RangePart, String>> {
            val newParts = mutableListOf<Pair<RangePart, String>>()
            checks.forEach {
                when (it.comparison) {
                    '>' -> {
                        val range = rangePart.ratings.getValue(it.field)
                        when {
                            it.value in range -> {
                                rangePart.ratings[it.field] = range.first..it.value

                                val newRatings = rangePart.ratings.toMutableMap()
                                newRatings[it.field] = (it.value + 1)..range.last
                                newParts.add(RangePart(newRatings) to it.target)
                            }

                            it.value < range.first -> {
                                rangePart.ratings[it.field] = IntRange.EMPTY

                                val newRatings = rangePart.ratings.toMutableMap()
                                newParts.add(RangePart(newRatings) to it.target)
                            }
                        }
                    }

                    '<' -> {
                        val range = rangePart.ratings.getValue(it.field)
                        when {
                            it.value in range -> {
                                rangePart.ratings[it.field] = it.value..range.last

                                val newRatings = rangePart.ratings.toMutableMap()
                                newRatings[it.field] = range.first..<it.value
                                newParts.add(RangePart(newRatings) to it.target)
                            }

                            it.value > range.last -> {
                                rangePart.ratings[it.field] = IntRange.EMPTY

                                val newRatings = rangePart.ratings.toMutableMap()
                                newParts.add(RangePart(newRatings) to it.target)
                            }
                        }
                    }

                    else -> {
                        val newRatings = rangePart.ratings.toMutableMap()
                        newParts.add(RangePart(newRatings) to it.target)
                    }
                }
            }
            return newParts
        }

        fun evaluate(part: Part): String =
            checks.firstNotNullOf {
                when (it.comparison) {
                    '>' -> if (part.ratings.getValue(it.field) > it.value) it.target else null
                    '<' -> if (part.ratings.getValue(it.field) < it.value) it.target else null
                    else -> it.target
                }
            }
    }

    private fun evaluatePart(part: Part, workFlows: List<WorkFlow>): Boolean {
        var state: String? = null
        while (state != "A" && state != "R") {
            state = workFlows.first { it.name == (state ?: "in") }.evaluate(part)
        }
        return state == "A"
    }

    private fun evaluateParts(rangePart: RangePart, target: String, workFlows: List<WorkFlow>): Long =
        when (target) {
            "A" -> rangePart.ratings.values.map { it.last - it.first + 1L }.product()
            "R" -> 0L
            else -> workFlows.first { it.name == target }.evaluate(rangePart).sumOf {
                evaluateParts(it.first, it.second, workFlows)
            }
        }

    private fun getWorkFlows(workFlowsString: String): List<WorkFlow> =
        workFlowsString.split("\\R".toRegex()).map { workFlowString ->
            WorkFlow(
                name = workFlowString.substringBefore('{'),
                checks = workFlowString
                    .substringAfter('{')
                    .substringBefore('}')
                    .split(',').map { checkString ->
                        if (checkString.contains('<')) {
                            Check(
                                checkString.substringBefore('<'),
                                '<',
                                checkString.substringAfter('<').substringBefore(':').toInt(),
                                checkString.substringAfter(':')
                            )
                        } else if (checkString.contains('>')) {
                            Check(
                                checkString.substringBefore('>'),
                                '>',
                                checkString.substringAfter('>').substringBefore(':').toInt(),
                                checkString.substringAfter(':')
                            )
                        } else {
                            Check("null", null, 0, checkString)
                        }
                    }
            )
        }

    override fun part1(): Any {
        val (workFlowsString, partsString) = text.split("\\R{2}".toRegex())
        return partsString.split("\\R".toRegex()).map { partString ->
            Part(
                partString.substring(1, partString.lastIndex)
                    .split(',')
                    .associate { it.substringBefore('=') to it.substringAfter('=').toInt() }
            )
        }.filter { evaluatePart(it, getWorkFlows(workFlowsString)) }.sumOf { it.ratings.values.sum() }
    }


    override fun part2(): Any =
        evaluateParts(
            rangePart = RangePart(
                mutableMapOf(
                    "x" to 1..4000,
                    "m" to 1..4000,
                    "a" to 1..4000,
                    "s" to 1..4000
                )
            ),
            target = "in",
            workFlows = getWorkFlows(text.split("\\R{2}".toRegex()).first())
        )
}
