import java.lang.Math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val initial = input.first().split(',').map { it.toInt() }
        val min = initial.minOf { it }
        val max = initial.maxOf { it }

        var minFuel = Int.MAX_VALUE
        for (p in min..max) {
            var fuel = initial.sumOf { abs(it - p) }
            minFuel = minOf(minFuel, fuel)
        }
        return minFuel
    }

    fun part2(input: List<String>): Int {
        val initial = input.first().split(',').map { it.toInt() }
        val min = initial.minOf { it }
        val max = initial.maxOf { it }

        var minFuel = Int.MAX_VALUE
        for (p in min..max) {
            var fuel = initial.map { abs(it - p) }.sumOf { it * (it + 1) / 2 }
            minFuel = minOf(minFuel, fuel)
        }
        return minFuel
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    check(part1(input) == 343605)
    check(part2(input) == 96744904)
}

