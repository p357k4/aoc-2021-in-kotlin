fun main() {
    fun lowest(risks: Array<IntArray>): Int {
        val size = risks.size

        val accumulated = Array(size) { IntArray(size) }

        for(j in 0 until size) {
            for(i in 0 until size) {
                accumulated[j][i] = (j + 1 + i + 1) * 9
            }
        }

        fun jump(j: Int, i: Int, distance: Int) {
            if (distance < accumulated[j][i]) {
                accumulated[j][i] = distance
                if (0 <= i - 1) {
                    jump(j, i - 1, distance + risks[j][i - 1])
                }
                if (i + 1 < size) {
                    jump(j, i + 1, distance + risks[j][i + 1])
                }

                if (0 <= j - 1) {
                    jump(j - 1, i, distance + risks[j - 1][i])
                }

                if (j + 1 < size) {
                    jump(j + 1, i, distance + risks[j + 1][i])
                }
            }
        }

        jump(0, 0, 0)

        return accumulated[size - 1][size - 1]
    }

    fun part1(input: List<String>): Int {
        val size = input.size

        val risks = Array(size) { IntArray(size) }

        for(j in 0 until size) {
            for(i in 0 until size) {
                risks[j][i] = 1 + (input[j][i].code - '1'.code)
            }
        }

        return lowest(risks)
    }

    fun part2(input: List<String>): Int {
        val size = input.size

        val risks = Array(5 * size) { IntArray(5 * size) }

        for (jj in 0 until 5) {
            for (ii in 0 until 5) {
                for (j in 0 until size) {
                    for (i in 0 until size) {
                        risks[j + jj * size][i + ii * size] = 1 + (input[j][i].code - '1'.code + ii + jj) % 9
                    }
                }
            }
        }

        return lowest(risks)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    check(part1(input) == 415)
    check(part2(input) == 2864)
}

