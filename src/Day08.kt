fun main() {
    fun part1(input: List<String>): Int {
        val initial = input
            .map { it.split('|')[1] }
            .flatMap { it.split(' ') }
            .filterNot(String::isEmpty)

        val filtered = initial.filter { listOf(2, 3, 4, 7).contains(it.length) }
        return filtered.size
    }

    class Row(val left: List<Set<Char>>, val right: List<Set<Char>>)

    fun analyze(row: Row, permutations: List<Map<Char, Char>>): Int {
        val wiring = setOf(
            setOf('a', 'b', 'c', 'e', 'f', 'g'),
            setOf('c', 'f'),
            setOf('a', 'c', 'd', 'e', 'g'),
            setOf('a', 'c', 'd', 'f', 'g'),
            setOf('b', 'c', 'd', 'f'),
            setOf('a', 'b', 'd', 'f', 'g'),
            setOf('a', 'b', 'd', 'e', 'f', 'g'),
            setOf('a', 'c', 'f'),
            setOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
            setOf('a', 'b', 'c', 'd', 'f', 'g')
        )

        fun check(input: List<Set<Char>>, wire: Map<Char, Char>): List<Set<Char>> {
            return input.map { it.map(wire::getValue).toSet() }
        }

        for (p in permutations) {
            val f = check(row.left, p)
            if (wiring.containsAll(f)) {
                val digits = row.right.map { it.map(p::getValue).toSet() }
                val decrypted = digits.map(wiring::indexOf).joinToString("")

                return decrypted.toInt()
            }
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        val all = setOf('a', 'b', 'c', 'd', 'e', 'f', 'g')
        val permutations = mutableListOf<Map<Char, Char>>()

        fun generate(set: Set<Char>, perm: Set<Char>): Unit {
            if (set.isEmpty()) {
                val map = perm.zip(all).associate { it.first to it.second }
                permutations.add(map)
            }

            for (x in set) {
                generate(set.minus(x), perm.plus(x))
            }
        }

        generate(all, setOf())

        val rows =
            input
                .map { it.split('|') }
                .map {
                    Row(
                        it[0].trim().split(' ').map { it.toSet() },
                        it[1].trim().split(' ').map { it.toSet() })
                }

        return rows.sumOf { analyze(it, permutations) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    check(part1(input) == 310)
    check(part2(input) == 915941)
}

