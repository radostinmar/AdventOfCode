object Day08 : Day(isTest = false) {
    private fun applyInstructions(
        instructions: String,
        current: String,
        nodes: Map<String, Pair<String, String>>
    ): String = instructions.fold(current) { acc, instruction ->
        if (instruction == 'L') {
            nodes[acc]!!.first
        } else {
            nodes[acc]!!.second
        }
    }

    private fun getNodes(): Map<String, Pair<String, String>> = lines.drop(2).associate { line ->
        val location = line.substringBefore(" =")
        val left = line.substringAfter("= (").substringBefore(", ")
        val right = line.substringAfter(", ").substringBefore(")")
        location to (left to right)
    }

    private fun findDistanceForNode(
        nodes: Map<String, Pair<String, String>>,
        startNode: String,
        endCondition: (String) -> Boolean
    ): Long {
        val instructions = lines.first()
        var current = startNode
        var steps = 0L
        while (!endCondition(current)) {
            current = applyInstructions(instructions, current, nodes)
            steps += instructions.length
        }
        return steps
    }

    override fun part1(): Number {
        val nodes = getNodes()
        return findDistanceForNode(nodes, "AAA") { it == "ZZZ" }
    }

    override fun part2(): Number {
        val nodes = getNodes()
        return nodes
            .keys
            .map { line -> line.substringBefore(" =") }
            .filter { it.endsWith("A") }
            .map { node -> findDistanceForNode(nodes, node) { it.endsWith("Z") } }
            .lcm()
    }
}
 