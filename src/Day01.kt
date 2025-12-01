import utils.*

// https://adventofcode.com/2025/day/1
fun main() {
    val today = "Day01"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun List<String>.toDay01() = Day01(map { it[0] to it.drop(1).toInt() })

    fun part1(input: List<String>) = input.toDay01().start().zeros

    fun part2(input: List<String>) = input.toDay01().start2().zeros

    chkTestInput(Part1, testInput, 3) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 6) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day01(val rotations: List<Pair<Char, Int>>) {
    private var cur = 50
    var zeros = 0 // the result

    fun start() = apply {
        rotations.forEach {
            cur = when (it.first) {
                'L' -> cur.rotateLeft(it.second)
                'R' -> cur.rotateRight(it.second)
                else -> error("Unexpected character: ${it.first}")
            }
            if (cur == 0) zeros++
        }
    }

    private fun Int.rotateRight(x: Int): Int = (this + x) % 100
    private fun Int.rotateLeft(x: Int): Int = this.rotateRight(100 - x % 100)

    fun start2() = apply {
        rotations.forEach { (dir, step) ->
            zeros += step / 100
            cur = when (dir) {
                'L' -> cur.rotateLeft(step).also { if (it == 0 || (cur != 0 && it >= cur)) zeros++ }
                'R' -> cur.rotateRight(step).also { if (it == 0 || (cur != 0 && it <= cur)) zeros++ }
                else -> error("Unexpected character")
            }
        }
    }

}