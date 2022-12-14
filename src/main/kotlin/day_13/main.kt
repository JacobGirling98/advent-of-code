package day_13

import day_13.Result.*
import java.io.File

private enum class Result(val sortValue: Int) {
    CORRECT(-1), INCORRECT(1), SAME(0)
}

private fun String.compareWith(right: String): Result {
    val leftElements = this.trimStartAndEnd().parseElements()
    val rightElements = right.trimStartAndEnd().parseElements()
    var index = 0

    while (true) {
        when {
            leftElements.isShorterThan(rightElements, index) -> return CORRECT
            leftElements.isLongerThan(rightElements, index) -> return INCORRECT
            leftElements.isSameSizeAs(rightElements, index) -> return SAME
        }

        when {
            bothAreInts(leftElements[index], rightElements[index]) -> {
                when {
                    leftElements[index].toInt() < rightElements[index].toInt() -> return CORRECT
                    leftElements[index].toInt() > rightElements[index].toInt() -> return INCORRECT
                }
            }

            leftElements[index].startsWithDigit() -> {
                leftElements[index].wrapWithBrackets().compareWith(rightElements[index])
                    .let { if (it != SAME) return it }
            }

            rightElements[index].startsWithDigit() -> {
                leftElements[index].compareWith(rightElements[index].wrapWithBrackets())
                    .let { if (it != SAME) return it }
            }

            else -> leftElements[index].compareWith(rightElements[index]).let { if (it != SAME) return it }
        }

        index++
    }
}

private fun List<String>.isSameSizeAs(
    rightElements: List<String>,
    index: Int
) = index == size && index == rightElements.size

private fun List<String>.isLongerThan(
    rightElements: List<String>,
    index: Int
) = index < size && index == rightElements.size

private fun List<String>.isShorterThan(
    rightElements: List<String>,
    index: Int
) = index == size && index < rightElements.size

private fun String.startsWithDigit() = first().isDigit()

private fun String.wrapWithBrackets() = "[${this}]"

private fun String.parseElements(): List<String> {
    var copy = this
    val elements = mutableListOf<String>()
    while (copy.isNotBlank()) {
        copy = if (copy.startsWithDigit()) {
            elements.add(copy.takeWhile { char -> char != ',' }.trim())
            copy.dropWhile { char -> char != ',' }.drop(1)
        } else {
            elements.add(copy.firstElement())
            copy.drop(elements.last().length).drop(1)
        }
    }
    return elements
}

private fun bothAreInts(left: String, right: String) = left.startsWithDigit() && right.startsWithDigit()

private fun String.trimStartAndEnd() = substring(1, length - 1)

private fun String.firstElement(): String {
    var bracketCount = 0
    return "${
        takeWhile {
            if (it == '[') bracketCount++
            if (it == ']') bracketCount--
            bracketCount > 0
        }
    }]"
}

private fun readInput(): List<List<String>> = File("src/main/kotlin/day_13/input.txt").readText()
    .split("\r\n\r\n")
    .map { it.split("\r\n") }

private fun part1() = readInput()
    .map { it.first().compareWith(it.last()) }
    .mapIndexed { index, result -> if (result == INCORRECT) 0 else index + 1 }
    .sum()

private fun part2(): Int {
    val two = "[[2]]"
    val six = "[[6]]"
    return readInput()
        .flatten()
        .plus(listOf(two, six))
        .sortedWith { firstPacket, secondPacket -> firstPacket.compareWith(secondPacket).sortValue }
        .mapIndexed { index, packet -> if (packet == two || packet == six) index + 1 else 0 }
        .filterNot { it == 0 }
        .reduce { a, b -> a * b }
}

fun main() {
    println(part1())
    println(part2())
}