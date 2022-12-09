package day_9

import day_9.Direction.*
import java.io.File
import kotlin.math.abs

enum class Direction {
    RIGHT, LEFT, DOWN, UP
}

fun String.toDirection() = when (this) {
    "R" -> RIGHT
    "L" -> LEFT
    "U" -> UP
    "D" -> DOWN
    else -> error("silly billy")
}

data class Instruction(
    val direction: Direction,
    val distance: Int
)

data class Coordinate(
    val x: Int,
    val y: Int
)

data class Knot(
    val coordinate: Coordinate,
    val history: Set<Coordinate>
) {
    fun move(direction: Direction): Knot = when (direction) {
        RIGHT -> copy(coordinate = coordinate.let { it.copy(x = it.x + 1) })
        LEFT -> copy(coordinate = coordinate.let { it.copy(x = it.x - 1) })
        UP -> copy(coordinate = coordinate.let { it.copy(y = it.y + 1) })
        DOWN -> copy(coordinate = coordinate.let { it.copy(y = it.y - 1) })
    }

    fun moveRelativeTo(coordinate: Coordinate): Knot = when {
        isNextTo(coordinate) -> copy()

        isSeparatedHorizontally(coordinate) -> moveHorizontallyTowards(coordinate.x).withUpdatedHistory()

        isSeparatedVertically(coordinate) -> moveVerticallyTowards(coordinate.y).withUpdatedHistory()

        isSeparatedDiagonally(coordinate) -> moveDiagonallyTowards(coordinate).withUpdatedHistory()

        isSeparatedDiagonallySkewedX(coordinate) -> moveDiagonallySkewedXTowards(coordinate).withUpdatedHistory()

        isSeparatedDiagonallySkewedY(coordinate) -> moveDiagonallySkewedYTowards(coordinate).withUpdatedHistory()

        else -> error("oh dear")
    }

    private fun withUpdatedHistory() = copy(history = history + coordinate)

    private fun moveHorizontallyTowards(target: Int) =
        copy(coordinate = coordinate.copy(x = coordinate.x + (target - coordinate.x) / 2))

    private fun moveVerticallyTowards(target: Int) =
        copy(coordinate = coordinate.copy(y = coordinate.y + (target - coordinate.y) / 2))

    private fun moveDiagonallySkewedYTowards(target: Coordinate): Knot =
        moveVerticallyTowards(target.y).let { it.copy(coordinate = it.coordinate.copy(x = target.x)) }

    private fun moveDiagonallySkewedXTowards(target: Coordinate) =
        moveHorizontallyTowards(target.x).let { it.copy(coordinate = it.coordinate.copy(y = target.y)) }

    private fun moveDiagonallyTowards(target: Coordinate) =
        moveHorizontallyTowards(target.x).moveVerticallyTowards(target.y)

    private fun isNextTo(target: Coordinate) =
        abs(coordinate.x - target.x) <= 1 && abs(coordinate.y - target.y) <= 1

    private fun isSeparatedHorizontally(target: Coordinate) =
        coordinate.y == target.y && abs(coordinate.x - target.x) > 1

    private fun isSeparatedVertically(target: Coordinate) =
        coordinate.x == target.x && abs(coordinate.y - target.y) > 1

    private fun isSeparatedDiagonallySkewedY(target: Coordinate) =
        abs(coordinate.x - target.x) == 1 && abs(coordinate.y - target.y) == 2

    private fun isSeparatedDiagonallySkewedX(target: Coordinate) =
        abs(coordinate.x - target.x) == 2 && abs(coordinate.y - target.y) == 1

    private fun isSeparatedDiagonally(target: Coordinate) =
        abs(coordinate.x - target.x) == 2 && abs(coordinate.y - target.y) == 2
}

fun List<Knot>.completeInstruction(instruction: Instruction): List<Knot> =
    (1..instruction.distance).map { instruction }.fold(this) { knots, instr ->
        knots.moveKnotsByOne(instr)
    }

fun List<Knot>.moveKnotsByOne(instruction: Instruction): List<Knot> = indices.fold(this) { knots, knotNumber ->
    knots.mapIndexed { index, knot ->
        when {
            index != knotNumber -> knot
            index == 0 -> knot.move(instruction.direction)
            else -> knot.moveRelativeTo(knots[knotNumber - 1].coordinate)
        }
    }
}

fun readInput(): List<Instruction> = File("src/main/kotlin/day_9/input.txt").readLines()
    .map { line -> line.split(" ").let { Instruction(it[0].toDirection(), it[1].toInt()) } }

fun part1() =
    readInput().fold(
        (1..2).map { (Knot(Coordinate(0, 0), setOf(Coordinate(0, 0)))) },
        List<Knot>::completeInstruction
    ).last().history.size

fun part2() =
    readInput().fold(
        (1..10).map { (Knot(Coordinate(0, 0), setOf(Coordinate(0, 0)))) },
        List<Knot>::completeInstruction
    ).last().history.size

fun main() {
    println(part1())
    println(part2())
}
