package day_5

import java.io.File


private data class MoveInstructionOO(
    val count: Int,
    private val start: Int,
    private val end: Int
) {
    val from = start - 1
    val to = end - 1
}

private data class Controller(
    val stacks: List<MutableList<Char>>,
    val moves: List<MoveInstructionOO>
) {

    private fun move(instruction: MoveInstructionOO) {
        repeat((1..instruction.count).count()) {
            stacks[instruction.to].add(stacks[instruction.from].removeLast())
        }
    }

    private fun moveAll(instruction: MoveInstructionOO) {
        stacks[instruction.to].addAll(stacks[instruction.from].takeLast(instruction.count))
        repeat((1..instruction.count).count()) { stacks[instruction.from].removeLast() }

    }

    fun execute9000() {
        moves.forEach { move(it) }
    }

    fun execute9001() {
        moves.forEach { moveAll(it) }
    }

    fun topCrates(): String = stacks.map { it.last() }.joinToString("")
}

private fun readInput(): Controller {
    val split = File("src/main/kotlin/day_5/input.txt").readLines()
    val halfway = split.indexOf("")
    val crates = split.subList(0, halfway).reversed()
    val stackIndices = crates.first().trim().split(" ").filter { it != "" }.map { crates.first().indexOf(it) }
    val stacks = stackIndices.map { index ->
        crates.subList(1, crates.size).map { it[index] }.takeWhile { it.isLetter() }.toMutableList()
    }
    val moves = split.subList(halfway + 1, split.size).map {
        it.split(" ").filter { str -> str.matches("-?\\d+(\\.\\d+)?".toRegex()) }.let { digits ->
            MoveInstructionOO(
                digits[0].toInt(),
                digits[1].toInt(),
                digits[2].toInt()
            )
        }
    }
    return Controller(stacks, moves)
}

private fun part1(): String {
    val controller = readInput()
    controller.execute9000()
    return controller.topCrates()
}

private fun part2(): String {
    val controller = readInput()
    controller.execute9001()
    return controller.topCrates()
}

fun main() {
    println(part1())
    println(part2())
}