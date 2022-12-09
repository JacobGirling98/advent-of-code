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
    fun move(instruction: Instruction): Knot = when (instruction.direction) {
        RIGHT -> copy(coordinate = coordinate.let { it.copy(x = it.x + 1) })
        LEFT -> copy(coordinate = coordinate.let { it.copy(x = it.x - 1) })
        UP -> copy(coordinate = coordinate.let { it.copy(y = it.y + 1) })
        DOWN -> copy(coordinate = coordinate.let { it.copy(y = it.y - 1) })
    }

    fun moveRelativeTo(knot: Knot): Knot = when {
        isNextTo(knot.coordinate) -> copy()

        isSeparatedHorizontally(knot.coordinate) -> moveHorizontallyTowards(knot.coordinate.x).withUpdatedHistory()

        isSeparatedVertically(knot.coordinate) -> moveVerticallyTowards(knot.coordinate.y).withUpdatedHistory()

        isSeparatedDiagonally(knot.coordinate) -> moveDiagonallyTowards(knot.coordinate).withUpdatedHistory()

        isSeparatedDiagonallySkewedX(knot.coordinate) -> moveDiagonallySkewedXTowards(knot.coordinate).withUpdatedHistory()

        isSeparatedDiagonallySkewedY(knot.coordinate) -> moveDiagonallySkewedYTowards(knot.coordinate).withUpdatedHistory()

        else -> error("oh dear")
    }

    private fun withUpdatedHistory() = copy(history = history + coordinate)

    private fun moveHorizontallyTowards(xCoordinate: Int) =
        copy(coordinate = coordinate.copy(x = coordinate.x + (xCoordinate - coordinate.x) / 2))

    private fun moveVerticallyTowards(yCoordinate: Int) =
        copy(coordinate = coordinate.copy(y = coordinate.y + (yCoordinate - coordinate.y) / 2))

    private fun moveDiagonallySkewedYTowards(coordinate: Coordinate): Knot =
        moveVerticallyTowards(coordinate.y).let { it.copy(coordinate = it.coordinate.copy(x = coordinate.x)) }

    private fun moveDiagonallySkewedXTowards(coordinate: Coordinate) =
        moveHorizontallyTowards(coordinate.x).let { it.copy(coordinate = it.coordinate.copy(y = coordinate.y)) }

    private fun moveDiagonallyTowards(coordinate: Coordinate) =
        moveHorizontallyTowards(coordinate.x).moveVerticallyTowards(coordinate.y)

    private fun isNextTo(coordinate: Coordinate) =
        abs(this.coordinate.x - coordinate.x) <= 1 && abs(this.coordinate.y - coordinate.y) <= 1

    private fun isSeparatedHorizontally(coordinate: Coordinate) =
        this.coordinate.y == coordinate.y && abs(this.coordinate.x - coordinate.x) > 1

    private fun isSeparatedVertically(coordinate: Coordinate) =
        this.coordinate.x == coordinate.x && abs(this.coordinate.y - coordinate.y) > 1

    private fun isSeparatedDiagonallySkewedY(coordinate: Coordinate) =
        abs(this.coordinate.x - coordinate.x) == 1 && abs(this.coordinate.y - coordinate.y) == 2

    private fun isSeparatedDiagonallySkewedX(coordinate: Coordinate) =
        abs(this.coordinate.x - coordinate.x) == 2 && abs(this.coordinate.y - coordinate.y) == 1

    private fun isSeparatedDiagonally(coordinate: Coordinate) =
        abs(this.coordinate.x - coordinate.x) == 2 && abs(this.coordinate.y - coordinate.y) == 2
}

fun List<Knot>.completeInstruction(instruction: Instruction): List<Knot> =
    (1..instruction.distance).map { instruction }.fold(this) { knots, instr ->
        knots.moveKnotsByOne(instr)
    }

fun List<Knot>.moveKnotsByOne(instruction: Instruction): List<Knot> = indices.fold(this) { tails, tailNumber ->
    tails.mapIndexed { index, tail ->
        if (index != tailNumber) {
            tail
        } else {
            if (index == 0) {
                tail.move(instruction)
            } else {
                tail.moveRelativeTo(tails[tailNumber - 1])
            }
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
