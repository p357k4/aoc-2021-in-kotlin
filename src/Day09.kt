fun main() {
    fun part1(input: List<String>): Int {
        val marker = "9".repeat(input.first().length)
        val board = (listOf(marker).plus(input).plus(marker))
            .map { "9" + it + "9" }
            .map {it.toCharArray()}
            .toTypedArray()

        var risk = 0
        for(j in 1 .. board.size - 2) {
            for(i in 1 .. board.first().size - 2) {
                if (board[j][i] < board[j - 1][i] && board[j][i] < board[j + 1][i] && board[j][i] < board[j][i - 1] && board[j][i] < board[j][i + 1]) {
                    risk += board[j][i].code - '0'.code + 1
                }
            }
        }

        return risk
    }

    fun part2(input: List<String>): Int {
        val marker = "9".repeat(input.first().length)
        val board = (listOf(marker).plus(input).plus(marker))
            .map { "9" + it + "9" }
            .map {it.toCharArray()}
            .toTypedArray()

        fun fill(j : Int, i : Int): Int {
            return if (board[j][i] == '9') {
                0
            } else {
                board[j][i] = '9'
                1 + fill(j + 1, i) + fill(j - 1, i) + fill(j, i - 1) + fill(j, i + 1)
            }
        }

        var risks = mutableListOf<Int>()
        for(j in 1 .. board.size - 2) {
            for(i in 1 .. board.first().size - 2) {
                if (board[j][i] < board[j - 1][i] && board[j][i] < board[j + 1][i] && board[j][i] < board[j][i - 1] && board[j][i] < board[j][i + 1]) {
                    risks.add(fill(j, i))
                }
            }
        }

        return risks.sortedDescending().take(3).reduce(Int::times)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    check(part1(input) == 458)
    check(part2(input) == 1391940)
}

