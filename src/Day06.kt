import utils.*

// https://adventofcode.com/2025/day/6
fun main() {
    val today = "Day06"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>) = Day06(input).grandTotal1()
    fun part2(input: List<String>) = Day06(input).grandTotal2()

    chkTestInput(Part1, testInput, 4277556) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 3263827) { part2(it) }
    solve(Part2, input) { part2(it) }
}
private typealias ColNums = List<Long>

private val sep = """\s+""".toRegex()

private data class Day06(val lines: List<String>) {

    val ops = lines.last().split(sep)
    val numLines = lines.dropLast(1)

    private fun List<ColNums>.sumThemAll() = withIndex().sumOf { (idx, colNums) ->
        when (ops[idx]) {
            "+" -> colNums.sum()
            "*" -> colNums.reduce { a, b -> a * b }
            else -> error("Unknown operation: ${ops[idx]}")
        }
    }

    private fun parseInput1(): List<ColNums> = numLines.map { line -> line.trim().split(sep).map { it.toLong() } }.transpose()

    fun grandTotal1() = parseInput1().sumThemAll()

    private fun parseInput2(): List<ColNums> {
        val lens = parseInput1().map { colNums -> colNums.maxOf { num -> "$num".length } }
        val pat = lens.joinToString(" ") { "(.{${it}})" }.toRegex() //build a re: (.{x}) (.{y}) ...
        //!!! 💢 intellij editor auto strip trailing spaces in input txt files! SHIT!
        return numLines.map { line -> pat.matchEntire(line)!!.groupValues.drop(1) }.transpose()
            .map { colStr -> colStr.map { it.toList() }.transpose().map { it.joinChars().trim().toLong() } }
    }

    fun grandTotal2() = parseInput2().sumThemAll()
}