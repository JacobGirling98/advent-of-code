package day_1

import java.io.File

fun readInput() = File("src/main/kotlin/day_1/input.txt").readLines()

fun addCalories(): List<Int> {
    var currentTotal = 0
    val totals = mutableListOf<Int>()

    for (line in readInput()) {
        if (line.isEmpty()) {
            totals.add(currentTotal)
            currentTotal = 0
        } else {
            currentTotal += line.toInt()
        }
    }
    if (currentTotal > 0) {
        totals.add(currentTotal)
    }
    return totals.toList()
}

fun part1() = addCalories().max()

fun part2() = addCalories().sortedDescending().take(3).sum()

fun main() {
    println(part1())
    println(part2())
}