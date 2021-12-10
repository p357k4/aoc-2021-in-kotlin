fun main() {
    fun analyze(input: String, stack: List<Char>): Int {
        val result = when (val h = input.firstOrNull()) {
            '(' -> analyze(input.drop(1), stack.plus(')'))
            '[' -> analyze(input.drop(1), stack.plus(']'))
            '{' -> analyze(input.drop(1), stack.plus('}'))
            '<' -> analyze(input.drop(1), stack.plus('>'))
            else -> if (stack.last() == h) {
                analyze(input.drop(1), stack.dropLast(1))
            } else {
                when (h) {
                    ')' -> 3
                    ']' -> 57
                    '}' -> 1197
                    '>' -> 25137
                    else -> 0
                }
            }
        }

        return result
    }

    fun analyze2(input: String, stack: List<Char>): Long {

        val result = when (val h = input.firstOrNull()) {
            '(' -> analyze2(input.drop(1), stack.plus(')'))
            '[' -> analyze2(input.drop(1), stack.plus(']'))
            '{' -> analyze2(input.drop(1), stack.plus('}'))
            '<' -> analyze2(input.drop(1), stack.plus('>'))
            else -> if (stack.last() == h) {
                analyze2(input.drop(1), stack.dropLast(1))
            } else {
                when (h) {
                    ')' -> 0
                    ']' -> 0
                    '}' -> 0
                    '>' -> 0
                    else ->
                        stack.reversed().map {
                        when (it) {
                            ')' -> 1L
                            ']' -> 2L
                            '}' -> 3L
                            '>' -> 4L
                            else -> 0L
                        }
                    }.fold(0L) { acc, i -> 5 * acc + i }
                }
            }
        }

        return result
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { analyze(it, listOf()) }
    }

    fun part2(input: List<String>): Long {
        val result = input
            .map { analyze2(it, listOf()) }
            .filter { it != 0L }
            .sorted()
        return result.get(result.size / 2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    check(part1(input) == 343863)
    check(part2(input) == 2924734236L)
}

