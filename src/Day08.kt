import utils.*
import kotlin.math.pow
import kotlin.math.sqrt

// https://adventofcode.com/2025/day/8
fun main() {
    val today = "Day08"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun List<String>.toDay08() = Day08(map { line -> line.split(",").let { (x, y, z) -> Point3D(x.toInt(), y.toInt(), z.toInt()) } })

    fun part1(input: List<String>, topXConn: Int) = input.toDay08().calcTop3ConnectedProduct(topXConn)
    fun part2(input: List<String>): Long = input.toDay08().twoXsAfterAllConnected()

    chkTestInput(Part1, testInput, 40) { part1(it, 10) }
    solve(Part1, input) { part1(it, 1000) }

    chkTestInput(Part2, testInput, 25272) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Point3D(val x: Int, val y: Int, val z: Int) {
    infix fun distanceTo(other: Point3D): Double = sqrt((x - other.x).toDouble().pow(2) + (y - other.y).toDouble().pow(2) + (z - other.z).toDouble().pow(2))
}
private typealias Point3DPair = Pair<Point3D, Point3D>

private class Day08(val points: List<Point3D>) {
    val distances: List<Point3DPair> = buildList {
        for (i in points.indices) {
            for (j in i + 1 until points.size) {
                add(points[i] to points[j])
            }
        }
    }.sortedBy { (p1, p2) -> p1 distanceTo p2 }

    val allConnected: MutableSet<MutableSet<Point3D>> = buildSet { points.forEach { add(mutableSetOf(it)) } }.toMutableSet()

    fun calcTop3ConnectedProduct(topXConn: Int): Int {
        top3Circuits2(topXConn)
        return allConnected.map { it.size }.sortedDescending().take(3).reduce(Int::times)
    }

    fun twoXsAfterAllConnected(topXConn: Int = distances.size): Long {
        return top3Circuits2(topXConn).let { (p1, p2) -> p1.x * p2.x.toLong() }
    }

    private fun top3Circuits2(topXConn: Int): Pair<Point3D, Point3D> {
        var lastPair = distances.first()
        distances.take(topXConn).forEach { (p1, p2) ->
            lastPair = p1 to p2
            val contained = allConnected.filter { p1 in it || p2 in it }
            allConnected -= contained
            allConnected += if (contained.isNotEmpty())
                (contained.flatten() + setOf(p1, p2)).toMutableSet()
            else
                mutableSetOf(p1, p2)
            //for part2
            if (allConnected.size == 1) return lastPair
        }
        return lastPair
    }
}