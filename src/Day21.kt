import java.lang.Long.max

fun main() {
    fun part1(input: List<String>): Int {
        val player1 = input[0].substringAfter("Player 1 starting position: ").toInt()
        val player2 = input[1].substringAfter("Player 2 starting position: ").toInt()

        fun dice(prev: Int) = if (prev == 100) 1 else prev + 1

        var position1 = player1
        var position2 = player2
        var score1 = 0
        var score2 = 0
        var d = 0
        var rolls = 0

        while (true) {
            for (i in 1..3) {
                d = dice(d)
                rolls++

                position1 += d
                while (position1 > 10) {
                    position1 -= 10
                }
            }
            score1 += position1

            if (score1 >= 1000) {
                return score2 * rolls
            }

            for (i in 1..3) {
                d = dice(d)
                rolls++

                position2 += d
                while (position2 > 10) {
                    position2 -= 10
                }
            }
            score2 += position2

            if (score2 >= 1000) {
                return score1 * rolls
            }
        }
    }

    fun part2(input: List<String>): Long {
        val player1 = input[0].substringAfter("Player 1 starting position: ").toInt()
        val player2 = input[1].substringAfter("Player 2 starting position: ").toInt()

        fun round(p: Int) = if (p > 10) p - 10 else p

        data class State(val position1: Int, val position2: Int, val score1: Int, val score2: Int, val player: Int)

        val cache = HashMap<State, Pair<Long, Long>>()

        val dices = listOf(
            1 + 1 + 1,
            1 + 1 + 2,
            1 + 1 + 3,
            1 + 2 + 1,
            1 + 2 + 2,
            1 + 2 + 3,
            1 + 3 + 1,
            1 + 3 + 2,
            1 + 3 + 3,
            2 + 1 + 1,
            2 + 1 + 2,
            2 + 1 + 3,
            2 + 2 + 1,
            2 + 2 + 2,
            2 + 2 + 3,
            2 + 3 + 1,
            2 + 3 + 2,
            2 + 3 + 3,
            3 + 1 + 1,
            3 + 1 + 2,
            3 + 1 + 3,
            3 + 2 + 1,
            3 + 2 + 2,
            3 + 2 + 3,
            3 + 3 + 1,
            3 + 3 + 2,
            3 + 3 + 3,
        )

        fun go1(state : State): Pair<Long, Long> {
            val maybe = cache[state]
            if (maybe != null) {
                return maybe
            }

            val (position1, position2, score1, score2, player) = state

            var sum = Pair(0L, 0L)
            if (player == 1) {
                for (dice in dices) {
                    val newPosition1 = round(position1 + dice)
                    val newScore1 = score1 + newPosition1
                    if (newScore1 >= 21) {
                        sum = Pair(sum.first + 1, sum.second)
                    } else {
                        val jump = go1(State(newPosition1, position2, newScore1, score2, 2))
                        sum = Pair(sum.first + jump.first, sum.second + jump.second)
                    }
                }
            } else {
                for (dice in dices) {
                    val newPosition2 = round(position2 + dice)
                    val newScore2 = score2 + newPosition2
                    if (newScore2 >= 21) {
                        sum = Pair(sum.first, sum.second + 1)
                    } else {
                        val jump = go1(State(position1, newPosition2, score1, newScore2, 1))
                        sum = Pair(sum.first + jump.first, sum.second + jump.second)
                    }
                }
            }

            cache[state] = sum
            return sum
        }

        val result = go1(State(player1, player2, 0, 0, 1))
        return max(result.first, result.second)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    check(part1(testInput) == 739785)
    check(part2(testInput) == 444356092776315L)

    val input = readInput("Day21")
    check(part1(input) == 918081)
    check(part2(input) == 158631174219251L)
}

