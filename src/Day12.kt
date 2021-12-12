sealed interface Cavern {
    val name : String

    fun canBeVisited(path: Collection<Cavern>): Boolean {
        val visits = path.count(this::equals)
        return when (this) {
            is Big -> visits < 10
            is Small -> visits < 1
        }
    }

    fun extendedCanBeVisited(path: List<Cavern>): Boolean {
        return when (this) {
            is Big -> path.count(this::equals) < 10
            is Small -> {
                val visits = path.count(this::equals)
                if (visits == 0) {
                    true
                } else {
                    val grouped = path.filterIsInstance<Small>().groupBy { it }.mapValues { it.value.size }
                    val values = grouped.values
                    !values.contains(2)
                }
            }
        }
    }

    companion object {
        fun create(name: String): Cavern {
            return if (name.first().isUpperCase()) {
                Big(name)
            } else {
                Small(name)
            }
        }
    }
}

data class Big(override val name: String) : Cavern
data class Small(override val name: String) : Cavern

fun main() {
    val start = Cavern.create("start")
    val end = Cavern.create("end")

    fun transform(input: List<String>) = input
        .flatMap {
            val arrow = it.split('-')
            listOf(
                Cavern.create(arrow[0]) to Cavern.create(arrow[1]),
                Cavern.create(arrow[1]) to Cavern.create(arrow[0])
            )
        }
        .toSet()
        .filterNot { it.first == end }
        .filterNot { it.second == start }
        .groupBy({ p -> p.first }, { p -> p.second })

    fun part1(input: List<String>): Int {
        val map = transform(input)

        fun search(cavern: Cavern, path: List<Cavern>, paths: List<List<Cavern>>): List<List<Cavern>> {
            return if (cavern.canBeVisited(path)) {
                val newPath = path.plusElement(cavern)

                if (cavern == end) {
                    paths.plusElement(newPath)
                } else {
                    val others = map.getOrDefault(cavern, emptyList())
                    paths.plus(others.flatMap { search(it, newPath, paths) })
                }
            } else {
                paths
            }
        }

        val all = search(start, emptyList(), emptyList())
        return all.toSet().size
    }

    fun part2(input: List<String>): Int {
        val map = transform(input)

        fun search(cavern: Cavern, path: List<Cavern>, paths: List<List<Cavern>>): List<List<Cavern>> {
            return if (cavern.extendedCanBeVisited(path)) {
                val newPath = path.plusElement(cavern)

                if (cavern == end) {
                    paths.plusElement(newPath)
                } else {
                    val others = map.getOrDefault(cavern, emptyList())
                    paths.plus(others.flatMap { search(it, newPath, paths) })
                }
            } else {
                paths
            }
        }

        val all = search(start, emptyList(), emptyList())

        return all.toSet().size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    check(part1(input) == 4304)
    check(part2(input) == 118242)
}

