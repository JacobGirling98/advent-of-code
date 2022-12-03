package day_3

import java.io.File

private const val LOWERCASE_OFFSET_VALUE = 96
private const val UPPERCASE_OFFSET_VALUE = 38

private data class Rucksack(
    val firstCompartment: List<Char>,
    val secondCompartment: List<Char>
) {
    val allItems get() = firstCompartment + secondCompartment

    val sharedItems
        get() = firstCompartment.filter { secondCompartment.contains(it) }.toSet()
}

private fun score(items: Set<Char>): Int =
    items.sumOf { if (it.isLowerCase()) it.code - LOWERCASE_OFFSET_VALUE else it.code - UPPERCASE_OFFSET_VALUE }

private fun sharedItemsFrom(rucksacks: List<Rucksack>): Set<Char> =
    rucksacks.first().allItems.filter { item -> rucksacks.takeLast(rucksacks.size - 1).all { it.allItems.contains(item) } }
        .toSet()

private fun readInput(): List<String> = File("src/main/kotlin/day_3/input.txt").readLines()

private fun String.sortToRucksack(): Rucksack = Rucksack(substring(0, length / 2).toList(), substring(length / 2).toList())

private fun part1(): Int = readInput().map { it.sortToRucksack() }.sumOf { score(it.sharedItems) }

private fun part2(): Int = readInput().map { it.sortToRucksack() }.chunked(3).sumOf { score(sharedItemsFrom(it)) }

fun main() {
    println(part1())
    println(part2())
}