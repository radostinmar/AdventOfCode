package y2022

import Day

object Day02 : Day(year = 2022) {

    private val opponentMap = mapOf(
        "A" to 'R',
        "B" to 'P',
        "C" to 'S'
    )

    private val playerMap = mapOf(
        "X" to 'R',
        "Y" to 'P',
        "Z" to 'S'
    )

    private val beats = mapOf(
        'R' to 'S',
        'P' to 'R',
        'S' to 'P'
    )

    private val loses = beats.toList().associate { it.second to it.first }

    private val score = mapOf(
        'R' to 1,
        'P' to 2,
        'S' to 3
    )

    override fun part1(): Any = lines.sumOf { game ->
        val (opponent, player) = game.split(" ")
        val opponentChoice = opponentMap.getValue(opponent)
        val playerChoice = playerMap.getValue(player)
        val shapeScore = score.getValue(playerChoice)
        val gameScore = when {
            playerChoice == opponentChoice -> 3
            beats.getValue(playerChoice) == opponentChoice -> 6
            else -> 0
        }
        shapeScore + gameScore
    }

    override fun part2(): Any = lines.sumOf { game ->
        val (opponent, outcome) = game.split(" ")
        val opponentChoice = opponentMap.getValue(opponent)
        val (gameScore, playerChoice) = when (outcome) {
            "Y" -> 3 to opponentChoice
            "Z" -> 6 to loses.getValue(opponentChoice)
            else -> 0 to beats.getValue(opponentChoice)
        }
        val shapeScore = score.getValue(playerChoice)
        shapeScore + gameScore
    }
}