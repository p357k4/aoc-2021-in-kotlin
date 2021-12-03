sealed interface Command

data class Forward(val x : Long) : Command
data class Up(val x : Long) : Command
data class Down(val x : Long) : Command
data class Nope(val x : Long) : Command

fun main() {
    fun part1(input: List<String>): Long {
        val commands = input.map {
            val arr = it.split(' ')
            val x = arr[1].toLong()
            when(arr[0]) {
                "up" -> Up(x)
                "down" -> Down(x)
                "forward" -> Forward(x)
                else -> Nope(x)
            }
        }

        val position = commands.fold(Pair(0L, 0L)) { acc, command ->
            when(command) {
                is Up -> acc.copy(first = acc.first - command.x)
                is Down -> acc.copy(first = acc.first + command.x)
                is Forward -> acc.copy(second = acc.second + command.x)
                is Nope -> acc
            }
        }
        return position.first * position.second
    }

    fun part2(input: List<String>): Long {
        val commands = input.map {
            val arr = it.split(' ')
            val x = arr[1].toLong()
            when(arr[0]) {
                "up" -> Up(x)
                "down" -> Down(x)
                "forward" -> Forward(x)
                else -> Nope(x)
            }
        }

        val position = commands.fold(Triple(0L, 0L, 0L)) { acc, command ->
            when(command) {
                is Up -> acc.copy(first = acc.first - command.x)
                is Down -> acc.copy(first = acc.first + command.x)
                is Forward -> acc.copy(second = acc.second + command.x, third = acc.third + acc.first * command.x)
                is Nope -> acc
            }
        }

        return position.second * position.third
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150L)
    check(part2(testInput) == 900L)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
