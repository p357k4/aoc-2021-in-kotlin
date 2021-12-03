fun main() {
    fun part1(input: List<String>): Int {
        val ints = input.map { it.toInt() }
        val diffs = ints.zipWithNext { a, b  -> if (b > a) 1 else 0}
        return diffs.sum()
    }

    fun part2(input: List<String>): Int {
        val ints = input.map { it.toInt() }
        val windowed = ints.windowed(3, 1).map { it.sum() }
        val diffs = windowed.zipWithNext { a, b  -> if (b > a) 1 else 0}
        return diffs.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
