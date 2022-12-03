package day_2

import day_2.Option.*
import java.io.File

private enum class Option(val score: Int) {
    ROCK(1), PAPER(2), SCISSORS(3)
}

private fun String.toOption() = when (this) {
    "A" -> ROCK
    "B" -> PAPER
    "C" -> SCISSORS
    "X" -> ROCK
    "Y" -> PAPER
    "Z" -> SCISSORS
    else -> error("rip")
}

private fun String.toOutcome(): (p: Option) -> Option = when (this) {
    "X" -> ::toLose
    "Y" -> ::toTie
    "Z" -> ::toWin
    else -> error("rip")
}

private fun toWin(option: Option): Option = when (option) {
    ROCK -> PAPER
    SCISSORS -> ROCK
    PAPER -> SCISSORS
}

private fun toTie(option: Option): Option = option

private fun toLose(option: Option): Option = when(option) {
    ROCK -> SCISSORS
    SCISSORS -> PAPER
    PAPER -> ROCK
}

private data class Turn(
    val opponent: Option,
    val you: Option
) {
    private val isATie get() = opponent == you

    private val youWin
        get() = when {
            you == ROCK && opponent == SCISSORS -> true
            you == PAPER && opponent == ROCK -> true
            you == SCISSORS && opponent == PAPER -> true
            else -> false
        }

    private val youLose get() = !youWin

    fun scoreTurn(): Int {
        var score = 0
        score += you.score
        score += when {
            isATie -> 3
            youWin -> 6
            youLose -> 0
            else -> error("failed")
        }
        return score
    }
}

private fun readInputToWin(): List<Turn> = readLines().map { line ->
    line.split(" ").let {
        Turn(
            it[0].toOption(),
            it[1].toOption()
        )
    }
}

private fun readInputToFollowInstructions(): List<Turn> = readLines().map { line ->
    line.split(" ").let {
        val opponentOption = it[0].toOption()
        Turn(
            opponentOption,
            it[1].toOutcome()(opponentOption)
        )
    }
}

private fun readLines() = File("src/main/kotlin/day_2/input.txt").readLines()

private fun part1() = readInputToWin().sumOf { it.scoreTurn() }

private fun part2() = readInputToFollowInstructions().sumOf { it.scoreTurn() }


fun main() {
    println(part1())
    println(part2())
}