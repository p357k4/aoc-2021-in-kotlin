fun main() {
    fun fast(input: List<String>, days: Long): Long {
        val fish = Array(8 + 1) { 0L }
        val initial = input.first().split(',').map { it.toInt() }

        for (i in initial) {
            fish[i]++
        }

        for(i in 0 until days) {
            val zero = fish[0]
            for (i in 1 until fish.size) {
                fish[i - 1] = fish[i]
            }
            fish[6] += zero
            fish[8] = zero
        }

        return fish.sum()
    }

    fun part1(input: List<String>) = fast(input, 80)

    fun part2(input: List<String>) = fast(input, 256)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06")
    check(part1(input) == 380758L)
    check(part2(input) == 1710623015163L)
}

