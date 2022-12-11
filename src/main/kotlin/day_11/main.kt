package day_11

import java.io.File

data class Monkey(
    val items: MutableList<Long>,
    val operation: (old: Long) -> Long,
    val test: (old: Long) -> Boolean,
    val testSuccessMonkey: Int,
    val testFailureMonkey: Int
) {
    var inspectionCount = 0

    fun inspectItem(item: Long, successfulMonkey: Monkey, failedMonkey: Monkey, doesWorryLevelDecrease: Boolean) {
        val inspectedItem = operation(item).let { if (doesWorryLevelDecrease) it / 3 else it }
        if (test(inspectedItem)) {
            successfulMonkey.items.add(inspectedItem)
        } else {
            failedMonkey.items.add(inspectedItem)
        }
        items.removeFirst()
        inspectionCount++
    }
}

fun String.toOperationValue(old: Long) = if (this == "old") old else this.toLong()

fun List<String>.toOperation(): (old: Long) -> Long = when (get(1)) {
    "+" -> { old: Long -> get(0).toOperationValue(old) + get(2).toOperationValue(old) }
    "*" -> { old: Long -> get(0).toOperationValue(old) * get(2).toOperationValue(old) }
    else -> error("Fool")
}

fun String.toMonkey() = trimMargin().split("\n").let {
    Monkey(
        it[1].split("Starting items:")[1].split(",").map { item -> item.trim().toLong() }.toMutableList(),
        it[2].split("Operation: new = ")[1].split(" ").toOperation(),
        it[3].split("Test: divisible by ")[1].let { value -> { old: Long -> old % value.toLong() == 0L } },
        it[4].split(" ").last().toInt(),
        it[5].split(" ").last().toInt()
    )
}

fun readInput() = File("src/main/kotlin/day_11/input.txt").readText().trimMargin().split("\n\n").map { it.toMonkey() }

fun playARound(monkeys: List<Monkey>, doesWorryLevelDecrease: Boolean) {
    monkeys.forEach {
        while (it.items.isNotEmpty()) {
            it.inspectItem(
                it.items.first(),
                monkeys[it.testSuccessMonkey],
                monkeys[it.testFailureMonkey],
                doesWorryLevelDecrease
            )
        }
    }
}

fun part1() = readInput()
    .apply { repeat(20) { playARound(this, doesWorryLevelDecrease = true) } }
    .sortedByDescending { it.inspectionCount }
    .take(2)
    .map { it.inspectionCount }
    .reduce { a, b -> a * b }

fun part2() = readInput()
    .apply { repeat(10000) { playARound(this, doesWorryLevelDecrease = false) } }
    .sortedByDescending { it.inspectionCount }
    .take(2)
    .map { it.inspectionCount }
    .let { it[0].toLong() * it[1].toLong() }

fun main() {
    println(part1())
    println(part2())
}