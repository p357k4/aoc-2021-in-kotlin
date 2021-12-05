import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

    fun transform(input: List<String>): List<Line> =
        input
            .map { it.split("->", ",").map { it.trim().toInt() } }
            .map { Line(it[0], it[1], it[2], it[3]) }

    fun mark(board: Array<IntArray>, line: Line): Array<IntArray> {
        val deltaX = line.x2 - line.x1
        val deltaY = line.y2 - line.y1
        val deltaM = maxOf(deltaX.absoluteValue, deltaY.absoluteValue)

        val signX = deltaX.sign
        val signY = deltaY.sign

        val deltas = (0..deltaM).map { Pair(line.x1 + it * signX, line.y1 + it * signY) }

        return deltas.fold(board) { b, p ->
            b[p.first][p.second]++
            b
        }
    }

    fun calculate(lines: List<Line>): Int {
        val maxX = lines.maxOf { maxOf(it.x1, it.x2) }
        val maxY = lines.maxOf { maxOf(it.y1, it.y2) }

        val board = Array(maxY + 1) { IntArray(maxX + 1) }
        val fboard = lines.fold(board) { acc, line -> mark(acc, line) }

        return fboard.sumOf { it.count() { it > 1 } }
    }

    fun part1(input: List<String>): Int {
        val lines = transform(input).filter { line -> line.x1 == line.x2 || line.y1 == line.y2 }
        return calculate(lines)
    }

    fun part2(input: List<String>): Int {
        val lines = transform(input)
        return calculate(lines)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    check(part1(input) == 7414)
    check(part2(input) == 19676)
}

