import utils.*

// https://adventofcode.com/2025/day/11
fun main() {
    val today = "Day11"

    val input = readInput(today)
    val testInput1 = readTestInput("${today}-part1")
    val testInput2 = readTestInput("${today}-part2")

    fun part1(input: List<String>): Long = Day11(input).countPaths("you")
    fun part2(input: List<String>): Long = Day11(input).countPaths("svr", setOf("fft", "dac"))


    chkTestInput(Part1, testInput1, 5) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput2, 2) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day11(val lines: List<String>) {
    private val splitRe = Regex(""":? """)
    val routes = lines.associate { line ->
        line.split(splitRe).let {devList-> devList[0] to devList.drop(1) }
    }.toMutableNotNullMap()

    val cache = mutableMapOf<Pair<String, Set<String>>, Long>()

    fun countPaths(current: String, required: Set<String> = setOf(), reachedKeyNode: Set<String> = setOf()): Long =
        cache.getOrPut(current to reachedKeyNode) {
            if (current == "out") {
                if (reachedKeyNode == required) 1L else 0L
            } else {
                routes[current].sumOf { next ->
                    countPaths(next, required, if (next in required) reachedKeyNode + next else reachedKeyNode)
                }
            }
        }
}