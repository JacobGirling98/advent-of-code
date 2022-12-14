package day_12

import day_12.NodeType.*
import java.io.File
import kotlin.math.abs


private enum class NodeType {
    START, END, REGULAR
}

private data class Node(
    val height: Char,
    val x: Int,
    val y: Int,
    var distance: Int,
    val type: NodeType
)

private typealias Grid = List<Node>

private fun readInput() = File("src/main/kotlin/day_12/input.txt").readLines().flatMapIndexed { y: Int, row: String ->
    row.mapIndexed { x, node ->
        when (node) {
            'S' -> Node('a', x, y, Int.MAX_VALUE, END)
            'E' -> Node('z', x, y, 0, START)
            else -> Node(node, x, y, Int.MAX_VALUE, REGULAR)
        }
    }
}.toMutableList()

private fun Node.traversableNeighbours(grid: Grid) = grid
    .filter { other -> abs(other.x - this.x) == 1 && other.y == this.y || abs(other.y - this.y) == 1 && other.x == this.x }
    .filter { other -> this.height - other.height <= 1 }

private fun traverseGrid(isEndNode: (node: Node) -> Boolean): Node {
    val grid = readInput()
    val visited = mutableListOf<Node>()

    while (grid.isNotEmpty()) {
        val node = grid.minBy { it.distance }
        grid.remove(node)

        node.traversableNeighbours(grid).forEach { neighbour ->
            (node.distance + 1).let { newDistance ->
                if (newDistance < neighbour.distance) neighbour.distance = newDistance
            }
        }

        visited.add(node)

        if (isEndNode(node)) break
    }

    return visited.first(isEndNode)
}

private fun part1() = traverseGrid { it.type == END }.distance

private fun part2() = traverseGrid { it.height == 'a' }.distance

fun main() {
    println(part1())
    println(part2())
}