package day_6

import java.io.File

private fun readInput() = File("src/main/kotlin/day_6/input.txt").readText()

private fun String.findFirstMarker(packetSize: Int): Int {
    val rollingSequence = mutableListOf<Char>()
    forEachIndexed { index, char ->
        rollingSequence.add(char)
        if (rollingSequence.size > packetSize) rollingSequence.removeFirst()
        if (rollingSequence.distinct().size == packetSize) return index + 1
    }
    error("Couldn't be found :(")
}

private fun part1() = readInput().findFirstMarker(4)

private fun part2() = readInput().findFirstMarker(14)

fun main() {
    println(part1())
    println(part2())
}