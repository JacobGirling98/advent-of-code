package day_7

import java.io.File


data class MyFile(val path: String, val isDir: Boolean, var size: Int?, val children: MutableList<MyFile>) {

    fun normalisePaths(): MyFile {
        val normalisedPath = mutableListOf<String>()
        path.split("/").forEach { if (it == "..") normalisedPath.removeLast() else normalisedPath.add(it) }
        return MyFile(normalisedPath.joinToString("/"), isDir, size, children)
    }

    fun parent(): String = path.split("/").dropLast(1).joinToString("/")

    fun calculateSize() = copy(size = children.sumOf { child ->
        if (child.isDir) child.size() else child.size!!
    } + if (!isDir) size!! else 0)

    fun addChild(file: MyFile) {
        children += file
    }

    private fun size(): Int = children.sumOf { child ->
        if (child.isDir) child.size() else child.size!!
    } + if (!isDir) size!! else 0
}

private fun readInput() = File("src/main/kotlin/day_7/input.txt").readLines()

private fun List<MyFile>.resolveDirectoryTree(): List<MyFile> {
    val resolvedFiles = listOf(*this.toTypedArray())
    forEach { file -> parentOf(resolvedFiles, file)?.addChild(file) }
    return resolvedFiles
}

private fun parentOf(resolvedFiles: List<MyFile>, file: MyFile) =
    resolvedFiles.find { it.path == file.parent() }

private fun List<String>.parseInput(): List<MyFile> {
    var cursor = ""
    val files = mutableListOf<MyFile>()
    forEach { command ->
        if (command.isAnInput()) {
            if (command.isCD() && command.isNotMovingToRoot()) {
                cursor += "${command.directory()}/"
            }
        } else {
            val line = command.split(" ")
            files.add(
                MyFile(
                    "$cursor${line[1]}",
                    line[0] == "dir",
                    if (line[0] != "dir") line[0].toInt() else null,
                    mutableListOf()
                )
            )
        }
    }
    return files
}

private fun String.directory() = split(" ")[2]

private fun String.isNotMovingToRoot() = split(" ")[2] != "/"

private fun String.isCD() = split(" ")[1] == "cd"

private fun String.isAnInput() = first() == '$'

private fun generateFileTree() = readInput()
    .parseInput()
    .map { it.normalisePaths() }
    .resolveDirectoryTree()
    .map { it.calculateSize() }

private fun part1(): Int = generateFileTree()
    .filter { it.isDir && it.size!! < 100000 }
    .sumOf { it.size!! }

private fun part2(): Int {
    val files = generateFileTree()
    val sizeUsed = files.filter { it.parent() == "" }.sumOf { it.size!! }
    val totalSize = 70000000
    val requiredSpace = 30000000
    val minimumToRemove = sizeUsed - (totalSize - requiredSpace)
    return files.filter { it.isDir && it.size!! > minimumToRemove }.minOf { it.size!! }
}

fun main() {
    println(part1())
    println(part2())
}