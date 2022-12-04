package day_4

import java.io.File

private data class Pairing(
    val firstElf: List<Int>,
    val secondElf: List<Int>
) {
    fun isFullyContained() = firstElf.containsAll(secondElf) || secondElf.containsAll(firstElf)

    fun hasOverlap() = firstElf.any { secondElf.contains(it) } || secondElf.any { firstElf.contains(it) }
}

private fun readInput() = File("src/main/kotlin/day_4/input.txt").readLines()

private fun String.toPairing() = split(",").let {
    Pairing(
        it.first().toRange(),
        it.last().toRange()
    )
}

private fun String.toRange(): List<Int> = split("-").let { (it.first().toInt()..it.last().toInt()).toList() }

private fun part1() = readInput().map { it.toPairing() }.filter { it.isFullyContained() }.size

private fun part2() = readInput().map { it.toPairing() }.filter { it.hasOverlap() }.size

fun main() {
    println(part1())
    println(part2())
}