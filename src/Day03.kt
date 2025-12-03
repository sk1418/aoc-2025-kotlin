import utils.*

// https://adventofcode.com/2025/day/3
fun main() {
    val today = "Day03"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun List<String>.toDay03() = Day03(map { it.toList().map { it.digitToInt() } })

    fun part1(input: List<String>): Long = input.toDay03().sumLargest()
    fun part2(input: List<String>): Long = input.toDay03().sumLargest2()

    chkTestInput(Part1, testInput, 357) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 3121910778619) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day03(val allDigits: List<List<Int>>) {
    fun sumLargest() = allDigits.sumOf { digits ->
        val (idx, v1) = digits.dropLast(1).withIndex().maxBy { it.value }
        val v2 = digits.drop(idx + 1).max()
        v1 * 10L + v2
    }

    fun sumLargest2(): Long {
        val len = allDigits.first().size
        return allDigits.sumOf { digits ->
            var remaining = 12
            val sorted = digits.withIndex().sortedWith(compareByDescending<IndexedValue<Int>> { it.value }.thenBy { it.index }).toMutableList()
            var num = 0L
            while (remaining > 0) {
                val (idx, v) = sorted.first { it.index <= len - remaining }
                num = num * 10 + v
                sorted.removeAll { it.index <= idx }
                remaining--
            }
            num
        }
    }

}