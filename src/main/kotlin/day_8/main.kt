package day_8

import java.io.File

private data class Tree(
    val height: Int,
    val x: Int,
    val y: Int,
    val isVisible: Boolean,
    val visibilityAbove: Int = 0,
    val visibilityBelow: Int = 0,
    val visibilityLeft: Int = 0,
    val visibilityRight: Int = 0
) {
    val scenicScore = visibilityAbove * visibilityLeft * visibilityBelow * visibilityRight
}

private fun readInput(): List<Tree> =
    File("src/main/kotlin/day_8/input.txt").readLines().flatMapIndexed { yIndex, line ->
        line.mapIndexed { xIndex, col ->
            Tree(col.toString().toInt(), xIndex, yIndex, false)
        }
    }

private fun Tree.isVisible(otherTrees: List<Tree>) = otherTrees.all { it.height < height }

private fun List<Tree>.aboveOf(x: Int, y: Int) = filter { it.x == x && it.y < y }.sortedByDescending { it.y }

private fun List<Tree>.leftOf(x: Int, y: Int) = filter { it.x < x && it.y == y }.sortedByDescending { it.x }

private fun List<Tree>.belowOf(x: Int, y: Int) = filter { it.x == x && it.y > y }.sortedBy { it.y }

private fun List<Tree>.rightOf(x: Int, y: Int) = filter { it.x > x && it.y == y }.sortedBy { it.x }

private fun Tree.visibilityDistance(trees: List<Tree>): Int {
    val smallerTrees = trees.takeWhile { it.height < height }.count()
    return smallerTrees + if (isNotEdgeOfTheGrid(smallerTrees, trees)) 1 else 0
}

private fun isNotEdgeOfTheGrid(smallerTrees: Int, trees: List<Tree>) = smallerTrees != trees.size

private fun List<Tree>.determineVisibility() = map { tree ->
    when {
        tree.isVisible(aboveOf(tree.x, tree.y)) -> tree.copy(isVisible = true)
        tree.isVisible(leftOf(tree.x, tree.y)) -> tree.copy(isVisible = true)
        tree.isVisible(rightOf(tree.x, tree.y)) -> tree.copy(isVisible = true)
        tree.isVisible(belowOf(tree.x, tree.y)) -> tree.copy(isVisible = true)
        else -> tree.copy(isVisible = false)
    }
}

private fun List<Tree>.determineVisibilityDistance() = map { tree ->
    tree
        .copy(visibilityAbove = tree.visibilityDistance(aboveOf(tree.x, tree.y)))
        .copy(visibilityLeft = tree.visibilityDistance(leftOf(tree.x, tree.y)))
        .copy(visibilityBelow = tree.visibilityDistance(belowOf(tree.x, tree.y)))
        .copy(visibilityRight = tree.visibilityDistance(rightOf(tree.x, tree.y)))
}

private fun List<Tree>.numberOfTreesVisible() = filter { it.isVisible }.size

private fun List<Tree>.highestScenicScore() = maxOf { it.scenicScore }

private fun part1() = readInput().determineVisibility().numberOfTreesVisible()

private fun part2() = readInput().determineVisibilityDistance().highestScenicScore()

fun main() {
    println(part1())
    println(part2())
}