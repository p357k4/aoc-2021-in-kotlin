fun main() {
    fun part1(input: List<String>): Int {
        val bits = (0 until input.first().length)
            .map { i ->
                val (ones, zeros) = input.partition { k -> k.get(i) == '1' }

                ones.size > zeros.size
            }

        val rates =
            bits
                .fold(Pair(0, 0)) { acc, v ->
                    val g = if (v) 1 else 0
                    val e = if (v) 0 else 1
                    Pair(acc.first * 2 + g, acc.second * 2 + e)
                }

        return rates.first * rates.second
    }

    fun part2(input: List<String>): Int {
        tailrec fun calculateOxygen(lines : List<String>, index : Int) : String =
            if (lines.size == 1 || index == lines.first().length) {
                lines.first()
            } else {
                val (ones, zeros) = lines.partition { it[index] == '1' }

                val result = if (ones.size >= zeros.size) ones else zeros

                calculateOxygen(result, index + 1)
            }

        tailrec fun calculateCarbonDioxide(lines: List<String>, index: Int) : String =
            if (lines.size == 1 || index == lines.first().length) {
                lines.first()
            } else {
                val (ones, zeros) = lines.partition { it[index] == '1' }

                val result = if (zeros.isEmpty() || ones.isEmpty()) {
                    lines
                } else if (ones.size >= zeros.size) {
                    zeros
                } else {
                    ones
                }

                calculateCarbonDioxide(result, index + 1)
            }

        val oxygen = calculateOxygen(input, 0)
        val carbonDioxide = calculateCarbonDioxide(input, 0)

        return oxygen.toInt(2) * carbonDioxide.toInt(2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
