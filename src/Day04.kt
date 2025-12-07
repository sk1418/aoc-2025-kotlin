import utils.*

// https://adventofcode.com/2025/day/4
fun main() {
    val today = "Day04"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun List<String>.toMatrix(): MatrixDay04 {
        val points = buildMap {
            forEachIndexed { y, line -> line.forEachIndexed { x, c -> put(x to y, c) } }
        }
        return MatrixDay04(this[0].lastIndex, this.lastIndex, MutableNotNullMap(points.toMutableMap()))
    }

    fun part1(input: List<String>) = input.toMatrix().findRolls()
    fun part2(input: List<String>) = input.toMatrix().findRemovable()

    chkTestInput(Part1, testInput, 13) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 43) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private class MatrixDay04(maxX: Int, maxY: Int, override val points: MutableNotNullMap<Point, Char>) : Matrix<Char>(maxX, maxY, points) {

    private fun accessibleRolls() = points.filter { (p, c) -> c == '@' && p.allAroundIn().count { points[it] == '@' } < 4 }.keys
    fun findRolls() = accessibleRolls().size

    fun findRemovable(): Int {
        var stepCount = Int.MAX_VALUE
        var result = 0
        while (stepCount > 0) {
            stepCount == 0
            accessibleRolls().let {
                stepCount = it.size
                result += stepCount
                points.putAll(it.map { it to '.' })
            }
        }
        return result
    }
}