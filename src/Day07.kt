import utils.*

// https://adventofcode.com/2025/day/7
fun main() {
    val today = "Day07"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun List<String>.toMatrix(): MatrixDay07 {
        val points = buildMap {
            forEachIndexed { y, line -> line.forEachIndexed { x, c -> put(x to y, c) } }
        }
        return MatrixDay07(this[0].lastIndex, this.lastIndex, points)
    }

    fun part1(input: List<String>): Int = input.toMatrix().countSplitTimes()
    fun part2(input: List<String>): Long = input.toMatrix().countTimelines()

    chkTestInput(Part1, testInput, 21) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 40L) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private class MatrixDay07(maxX: Int, maxY: Int, override val points: Map<Point, Char>) : Matrix<Char>(maxX, maxY, points) {
    val start = points.filterValues { it == 'S' }.keys.first()
    fun countSplitTimes(): Int {
        var chkingRow = 0
        var count = 0
        var curRow = mutableSetOf(start)
        var nextRow = mutableSetOf<Point>()
        while (chkingRow < maxY) {
            curRow.forEach { curPoint ->
                val nextPoint = curPoint.move(Direction.Down)
                when (points[nextPoint]) {
                    '^' -> {
                        nextRow += nextPoint.move(Direction.Left)
                        nextRow += nextPoint.move(Direction.Right)
                        count++
                    }

                    '.' -> nextRow += nextPoint
                }
            }
            curRow = nextRow
            nextRow = mutableSetOf()
            chkingRow++
        }
        return count
    }

    fun countTimelines(): Long = countForThePoint(start)

    private val cache = mutableMapOf<Point, Long>()
    private fun countForThePoint(point: Point): Long {
        if (point in cache) return cache[point]!!
        val nextPoint = point.move(Direction.Down)
        val count = when (points[nextPoint]) {
            '^' -> countForThePoint(nextPoint.move(Direction.Left)) + countForThePoint(nextPoint.move(Direction.Right))
            '.' -> countForThePoint(nextPoint)
            else -> 1L // out of the grid, we're done here
        }
        return count.also { cache[point] = it }
    }
}