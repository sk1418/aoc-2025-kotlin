import utils.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// https://adventofcode.com/2025/day/9
fun main() {
    val today = "Day09"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun List<String>.toDay09() = Day09(map { line -> line.split(",").let { (x, y) -> x.toInt() to y.toInt() } })

    fun part1(input: List<String>): Long = input.toDay09().largestArea()
    fun part2(input: List<String>): Long = input.toDay09().largestAreaByRedAndGreen()

    chkTestInput(Part1, testInput, 50) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 24) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private class Day09(val redPoints: List<Point>) {
    val cornerPairs = buildList {
        for (i in redPoints.indices) {
            for (j in i + 1 until redPoints.size) {
                add(redPoints[i] to redPoints[j])
            }
        }
    }

    private val area: Pair<Point, Point>.() -> Long = {
        val (a, b) = this
        (abs(a.first - b.first) + 1L) * (abs(b.second - a.second) + 1L)
    }

    fun largestArea(): Long = cornerPairs.maxOf { it.area() }

    fun largestAreaByRedAndGreen(): Long {
        fun Pair<Point, Point>.toSortedEdgePoints(): Pair<Point, Point> {
            val (p1, p2) = this
            val point1 = min(p1.first, p2.first) to min(p1.second, p2.second)
            val point2 = max(p1.first, p2.first) to max(p1.second, p2.second)
            return point1 to point2
        }

        val polygonEdges = (redPoints + redPoints.first())
            .zipWithNext()
            .map { it.toSortedEdgePoints() }

        return cornerPairs.map { it.toSortedEdgePoints() }.filter { (p1, p2) ->
            polygonEdges.none { (e1, e2) ->
                p1.first < e2.first && p2.first > e1.first && p1.second < e2.second && p2.second > e1.second
            }
        }.maxOf { it.area() }
    }
}