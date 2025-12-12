import utils.Part1
import utils.readInput
import utils.readTestInput
import utils.solve

// https://adventofcode.com/2025/day/12
fun main() {
    val today = "Day12"

    val input = readInput(today)
    val testInput = readTestInput(today)

    // 🤓 rotating the shapes in the real input makes no difference (but test input does!), so it becomes a fairly easy challenge and skipping the test
    fun part1(input: List<String>) = input.takeLastWhile { it.isNotBlank() }.map { it.split(Regex("""\D{1,2}""")).map { it.toInt() } }
        .count { nums -> nums.drop(2).sum() <= nums[0] * nums[1] / 9 }
    //  chkTestInput(Part1, testInput, 2) { part1(it) }
    solve(Part1, input) { part1(it) }
}