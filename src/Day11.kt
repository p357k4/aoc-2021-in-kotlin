fun main() {
    fun discharge(board: Array<IntArray>) {
        for (j in 1..board.size - 2) {
            for (i in 1..board.first().size - 2) {
                if (board[j][i] != -1) {
                    board[j][i]++
                }
            }
        }
    }

    fun reset(board: Array<IntArray>) {
        for (j in 1..board.size - 2) {
            for (i in 1..board.first().size - 2) {
                if (board[j][i] == -1) {
                    board[j][i] = 0
                }
            }
        }
    }

    fun propagate(board: Array<IntArray>, j: Int, i: Int) {
        if (board[j - 1][i - 1] != -1) {
            board[j - 1][i - 1]++
        }
        if (board[j - 1][i] != -1) {
            board[j - 1][i]++
        }
        if (board[j - 1][i + 1] != -1) {
            board[j - 1][i + 1]++
        }
        if (board[j][i - 1] != -1) {
            board[j][i - 1]++
        }

        if (board[j][i + 1] != -1) {
            board[j][i + 1]++
        }
        if (board[j + 1][i - 1] != -1) {
            board[j + 1][i - 1]++
        }
        if (board[j + 1][i] != -1) {
            board[j + 1][i]++
        }
        if (board[j + 1][i + 1] != -1) {
            board[j + 1][i + 1]++
        }
    }

    fun charge(board: Array<IntArray>): Int {
        var total = 0

        discharge(board)

        do {
            var pulses = 0

            for (j in 1..board.size - 2) {
                for (i in 1..board.first().size - 2) {
                    if (board[j][i] > 9) {
                        board[j][i] = -1
                        pulses++
                        propagate(board, j, i)
                    }
                }
            }
            total += pulses
        } while (pulses > 0)

        reset(board)

        return total
    }

    fun digitToInt(c: Char) = c.code - '0'.code

    fun board(input: List<String>): Array<IntArray> {
        val marker = "0".repeat(input.first().length)
        return (listOf(marker) + (input) + marker)
            .map { "0" + it + "0" }
            .map { it.map(::digitToInt).toIntArray() }
            .toTypedArray()
    }

    fun part1(input: List<String>): Int {
        val board = board(input)

        return (1..100).fold(0) { acc, _ -> acc + charge(board) }
    }

    fun part2(input: List<String>): Int {
        val board: Array<IntArray> = board(input)

        return (1..1000).first { charge(board) == 100 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    check(part1(input) == 1735)
    check(part2(input) == 400)
}

