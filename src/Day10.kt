fun main() {
    fun analyze(input: String): Int {
        val stack = mutableListOf<Char>()
        for (h in input) {
            when (h) {
                '(' -> stack.add(')')
                '[' -> stack.add(']')
                '{' -> stack.add('}')
                '<' -> stack.add('>')
                else -> if (stack.last() == h) {
                    stack.removeLast()
                } else {
                    return when (h) {
                        ')' -> 3
                        ']' -> 57
                        '}' -> 1197
                        '>' -> 25137
                        else -> 0
                    }
                }
            }
        }

        return 0
    }

    fun analyze2(input: String): Long {
        val stack = mutableListOf<Char>()
        for (h in input) {
            when (h) {
                '(' -> stack.add(')')
                '[' -> stack.add(']')
                '{' -> stack.add('}')
                '<' -> stack.add('>')
                else -> if (stack.last() == h) {
                    stack.removeLast()
                } else {
                    return 0
                }
            }
        }

        return stack
            .reversed()
            .map {
                when (it) {
                    ')' -> 1L
                    ']' -> 2L
                    '}' -> 3L
                    '>' -> 4L
                    else -> 0L
                }
            }.fold(0L) { acc, i -> 5 * acc + i }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf(::analyze)
    }

    fun part2(input: List<String>): Long {
        val result = input
            .map(::analyze2)
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

