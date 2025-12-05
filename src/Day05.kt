import utils.*

// https://adventofcode.com/2025/day/5
fun main() {
    val today = "Day05"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun List<String>.toDay05(): Day05 {
        val (rangeLines, numLines) = filter { it.isNotBlank() }.partition { it.contains("-") }
        val numbers = numLines.map { it.toLong() }
        val ranges = rangeLines.map { it.substringBefore("-").toLong()..it.substringAfter("-").toLong() }
        return Day05(ranges, numbers)
    }

    fun part1(input: List<String>): Int = input.toDay05().countFreshNums()
    fun part2(input: List<String>): Long = input.toDay05().countTotalFreshIds()

    chkTestInput(Part1, testInput, 3) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 14) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day05(val freshRanges: List<LongRange>, val numbers: List<Long>) {
    fun countFreshNums() = numbers.count { number -> freshRanges.any { range -> number in range } }

    fun countTotalFreshIds(): Long {
        var total = 0L
        val sorted = freshRanges.sortedBy { it.first }
        var (start, end) = sorted.first().let { it.start to it.last }
        sorted.forEach { range ->
            if (range.start <= end + 1) {
                end = maxOf(end, range.last)
            } else {
                total += end - start + 1
                start = range.start
                end = range.last
            }
        }
        total += end - start + 1
        return total

    }

}