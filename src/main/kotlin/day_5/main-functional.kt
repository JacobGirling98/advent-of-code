package day_5

import java.io.File


private data class MoveInstruction(
    val count: Int,
    private val start: Int,
    private val end: Int
) {
    val from = start - 1
    val to = end - 1
}

private data class Instructions(
    val stacks: List<List<Char>>,
    val moves: List<MoveInstruction>
)

private fun readInput(): Instructions {
    val split = File("src/main/kotlin/day_5/input.txt").readLines()
    val halfway = split.indexOf("")
    val crates = split.subList(0, halfway).reversed()
    val stackIndices = crates.first().trim().split(" ").filter { it != "" }.map { crates.first().indexOf(it) }
    val stacks = stackIndices.map { index ->
        crates.subList(1, crates.size).map { it[index] }.takeWhile { it.isLetter() }.toMutableList()
    }
    val moves = split.subList(halfway + 1, split.size).map {
        it.split(" ").filter { str -> str.matches("-?\\d+(\\.\\d+)?".toRegex()) }.let { digits ->
            MoveInstruction(
                digits[0].toInt(),
                digits[1].toInt(),
                digits[2].toInt()
            )
        }
    }
    return Instructions(stacks, moves)
}

private fun moveSingleCrate(stacks: List<List<Char>>, instruction: MoveInstruction): List<List<Char>> =
    stacks.mapIndexed { index, stack -> processStack(index, instruction, stack, stacks, 1) }

private fun moveCrates9001(stacks: List<List<Char>>, instruction: MoveInstruction): List<List<Char>> =
    stacks.mapIndexed { index, stack -> processStack(index, instruction, stack, stacks, instruction.count) }

private fun moveCrates9000(stacks: List<List<Char>>, instruction: MoveInstruction): List<List<Char>> =
    (1..instruction.count).map { instruction }.fold(stacks, ::moveSingleCrate)

private fun processStack(
    index: Int,
    instruction: MoveInstruction,
    currentStack: List<Char>,
    stacks: List<List<Char>>,
    cratesToMove: Int
) = when (index) {
    instruction.from -> currentStack.dropLast(cratesToMove)
    instruction.to -> currentStack + stacks[instruction.from].takeLast(cratesToMove)
    else -> currentStack
}

private fun List<List<Char>>.topCrates() = map { it.last() }.joinToString("")

private fun part1(): String = readInput().let { it.moves.fold(it.stacks, ::moveCrates9000) }.topCrates()

private fun part2(): String = readInput().let { it.moves.fold(it.stacks, ::moveCrates9001) }.topCrates()

fun main() {
    println(part1())
    println(part2())
}
