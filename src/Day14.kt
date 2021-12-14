import java.util.Map.copyOf

fun main() {
    fun part1(input: List<String>): Int {
        val template = input.first()
        val rules = input.drop(2).associate {
            val arr = it.split(" -> ")
            arr[0] to arr[1]
        }

        val result = (1..10).fold(template) { acc, _ ->
            val pairs = acc.windowed(2, 1)
            val next = pairs.map {
                val insertion = rules.getOrDefault(it, "")
                "${it[0]}$insertion"
            }.joinToString("")
            next + acc.last()
        }

        val counts = result.groupBy { it }.mapValues { it.value.size }
        val max = counts.maxOf { it.value }
        val min = counts.minOf { it.value }
        return max - min
    }

    fun part2(input: List<String>): Long {
        val template = input.first()
        val rules = input.drop(2).associate {
            val arr = it.split(" -> ")
            arr[0] to arr[1][0]
        }

        var characterCounts = template.groupBy { it }.mapValues { it.value.size.toLong() }.toMutableMap()
        var pairCounts = template.windowed(2, 1).groupBy { it }.mapValues { it.value.size.toLong() }.toMutableMap()

        for(i in 1 .. 40) {
            val copyPairCounts = mutableMapOf<String, Long>()
            val copyCharacterCounts = copyOf(characterCounts).toMutableMap()

            for (e in pairCounts.entries) {
                val insertion = rules.getValue(e.key)
                val count = e.value
                val left = "${e.key[0]}$insertion"
                val right = "$insertion${e.key[1]}"
                copyCharacterCounts[insertion] = copyCharacterCounts.getOrDefault(insertion, 0) + count
                copyPairCounts[left] = copyPairCounts.getOrDefault(left, 0) + count
                copyPairCounts[right] = copyPairCounts.getOrDefault(right, 0) + count
            }

            pairCounts = copyPairCounts
            characterCounts = copyCharacterCounts
        }

        val result = characterCounts.values.maxOf { it } - characterCounts.values.minOf { it }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529L)

    val input = readInput("Day14")
    check(part1(input) == 3697)
    check(part2(input) == 4371307836157L)
}

