import utils.*

// https://adventofcode.com/2025/day/2
fun main() {
    val today = "Day02"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun List<String>.toDay02() = Day02(
        ranges = single().split(",").map { withDash ->
            withDash.split("-").let { (a, b) -> a.toLong()..b.toLong() }
        }
    )

    fun part1(input: List<String>): Long = input.toDay02().sumInvalidIds()
    fun part2(input: List<String>): Long = input.toDay02().sumInvalidIds2()

    chkTestInput(Part1, testInput, 1227775554) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 4174379265) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day02(val ranges: List<LongRange>) {
    private fun Long.invalidId(): Boolean {
        val s = "$this"
        val len = s.length
        return len % 2 == 0 && s.take(len / 2) == s.takeLast(len / 2)
    }

    fun sumInvalidIds(): Long = ranges.fold(0) { acc, range ->
        var id = range.first
        var sumInRange = 0L
        while (id <= range.last) {
            val len = "$id".length
            if (len % 2 == 1) {
                id = 10L.pow(len.toLong()) //jump to the next target, I think it can be faster
                continue
            }
            sumInRange += id.takeIf { it.invalidId() } ?: 0L
            id++
        }
        sumInRange + acc
    }

    fun sumInvalidIds2(): Long {
        val pat = """(\d+)\1+""".toRegex()
        return ranges.sumOf { range -> range.filter { "$it".matches(pat) }.sum() }
    }

    fun `Part1 alternative inspired by part2 solution, But 2X+ slower than my initial solution`(): Long {
        val pat = """(\d+)\1""".toRegex()
        return ranges.sumOf { range -> range.filter { "$it".matches(pat) }.sum() }
    }

}