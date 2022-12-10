package day_10

import java.io.File

private data class Instruction(
    val command: String,
    val value: Int?
) {
    fun isNoop() = command == "noop"
}

private class Cpu(private val instructions: MutableList<Instruction>) {

    private var cycle = 1
    private var register = 1
    var signalStrengths = 0

    private val screen = mutableListOf(mutableListOf<String>())

    private fun calculateSignalStrength() {
        if (cycle % 40 == 20)
            signalStrengths += cycle * register
    }

    private fun performInstruction() {
        if (instructions.first().isNoop()) {
            executeCycle()
            instructions.removeFirst()
        } else {
            executeCycle()
            executeCycle()
            register += instructions.first().value!!
            instructions.removeFirst()
        }
    }

    private fun executeCycle() {
        calculateSignalStrength()
        drawPixel()
        cycle++
    }

    private fun drawPixel() {
        if (screen.last().size == 40)
            screen.add(mutableListOf())
        screen.last().let { it.add(if (it.size >= register - 1 && it.size <= register + 1) "#" else ".") }
    }

    fun run() {
        while (instructions.isNotEmpty()) {
            performInstruction()
        }
    }

    fun generateScreen() = screen.joinToString("\n") { it.joinToString("") }
}

private fun String.toInstruction() = split(" ").let {
    when (it.size) {
        1 -> Instruction(it[0], null)
        else -> Instruction(it[0], it[1].toInt())
    }
}

private fun readInput() =
    File("src/main/kotlin/day_10/input.txt").readLines().map { it.toInstruction() }.toMutableList()

private fun part1(): Int {
    val cpu = Cpu(readInput())
    cpu.run()
    return cpu.signalStrengths
}

private fun part2(): String {
    val cpu = Cpu(readInput())
    cpu.run()
    return cpu.generateScreen()
}

fun main() {
    println(part1())
    println(part2())
}