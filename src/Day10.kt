import com.microsoft.z3.*
import utils.*
import java.util.*

// https://adventofcode.com/2025/day/10
fun main() {
    val today = "Day10"

    val input = readInput(today)
    val testInput = readTestInput(today)

    fun part1(input: List<String>): Int = Day10(input).machines.sumOf { m -> m.minPressesByBFS() }
    fun part2(input: List<String>): Int = Day10(input).machines.sumOf { m -> m.enableVoltages() }

    chkTestInput(Part1, testInput, 7) { part1(it) }
    solve(Part1, input) { part1(it) }

    chkTestInput(Part2, testInput, 33) { part2(it) }
    solve(Part2, input) { part2(it) }
}

private data class Day10(val lines: List<String>) {

    val machines: List<Machine> = buildList {
        lines.map { line ->
            lateinit var lightTarget: BitSet
            var buttons: MutableList<List<Int>> = mutableListOf()
            lateinit var voltages: List<Int>
            line.split(" ").map { part ->
                when {
                    '[' in part -> {
                        val booList = part.dropLast(1).drop(1).map { it == '#' }
                        lightTarget = BitSet(booList.size)
                        booList.forEachIndexed { idx, b -> if (b) lightTarget.set(idx) }
                    }

                    '(' in part -> buttons += part.dropLast(1).drop(1).toInts(",")
                    '{' in part -> voltages = part.dropLast(1).drop(1).toInts(",")
                }
            }
            add(Machine(lightTarget, buttons, voltages))
        }
    }
}

private data class Machine(val lightTarget: BitSet, val buttons: List<List<Int>>, val voltages: List<Int>) {
    val buttonBitSets = buttons.map { button ->
        BitSet(lightTarget.size()).also { bs ->
            button.forEach { bs.set(it) }
        }
    }

    fun minPressesByBFS(): Int {
        val queue: Queue<Pair<BitSet, Int>> = ArrayDeque()
        val visited = mutableSetOf<BitSet>()
        val initial = BitSet(lightTarget.size()) // all off
        queue.add(initial to 0)
        visited.add(initial)

        while (queue.isNotEmpty()) {
            val (currentState, presses) = queue.poll()
            if (currentState == lightTarget) return presses

            for (button in buttonBitSets) {
                val nextState = currentState.clone() as BitSet
                nextState.xor(button)
                if (nextState !in visited) {
                    visited.add(nextState)
                    queue.add(nextState to presses + 1)
                }
            }
        }
        return -1 // Target state is unreachable
    }


    // AI + external Z3 lib 🫣: minimize the number of button presses to achieve target voltages
    fun enableVoltages(): Int {
        return Context().use { ctx ->
            val opt = ctx.mkOptimize()

            fun intVariableOf(name: String) = ctx.mkIntConst(name)

            fun intValueOf(value: Int) = ctx.mkInt(value)

            infix fun IntExpr.gte(t: IntExpr) = ctx.mkGe(this, t)

            operator fun ArithExpr<IntSort>.plus(t: IntExpr) = ctx.mkAdd(this, t)

            infix fun ArithExpr<IntSort>.equalTo(t: IntExpr) = ctx.mkEq(this, t)

            fun List<IntExpr>.sum() = ctx.mkAdd(*this.toTypedArray())

            val ZERO = intValueOf(0)

            val affectedJoltages = mutableMapOf<Int, MutableList<IntExpr>>()
            val buttonVariables = this.buttons.mapIndexed { index, schematic ->
                // create button variable
                intVariableOf(index.toString()).also { buttonVariable ->
                    // track the joltages if affects
                    schematic.forEach { affectedIndex ->
                        affectedJoltages.computeIfAbsent(affectedIndex) { _ -> mutableListOf() }.add(buttonVariable)
                    }
                    // add it to the optimizer
                    opt.Add(buttonVariable gte ZERO)
                }
            }

            affectedJoltages.forEach { (voltageIndex, buttonsAffecting) ->
                val targetValue = intValueOf(voltages[voltageIndex])
                val sumOfButtonPresses = buttonsAffecting.sum()
                opt.Add(sumOfButtonPresses equalTo targetValue)
            }

            opt.MkMinimize(buttonVariables.sum())

            val status = opt.Check()

            if (status == Status.SATISFIABLE) {
                val model = opt.getModel()
                ((model.evaluate(buttonVariables.sum(), true) as IntNum).int)
            } else {
                throw IllegalStateException("No Solution Found")
            }
        }
    }
}