class Board(val numbers: Array<IntArray>, val mark: Array<IntArray>, val row: IntArray, val column: IntArray)

fun main() {
    fun isWinning(board: Board) : Boolean {
        return board.row.contains(5) || board.column.contains(5)
    }
    fun checkBoard(board: Board, number: Int): Boolean {
        for (i in 0 until 5) {
            for (j in 0 until 5) {
                if (board.numbers[i][j] == number) {
                    board.mark[i][j] = 1;
                    board.row[i]++
                    board.column[j]++

                    if (board.row[i] == 5) {
                        return true
                    }

                    if (board.column[j] == 5) {
                        return true
                    }
                }
            }
        }

        return false
    }

    fun sumBoard(board: Board): Int {
        var s = 0
        for (i in 0 until 5) {
            for (j in 0 until 5) {
                if (board.mark[i][j] == 0) {
                    s += board.numbers[i][j]
                }
            }
        }

        return s
    }

    fun toBoards(input: List<String>): List<Board> {
        return input
            .windowed(6, 6)
            .map {
                it
                    .drop(1)
                    .map {
                        it
                            .split(' ')
                            .filterNot { it.isEmpty() }
                            .map(String::toInt)
                            .toIntArray()
                    }
            }
            .map {
                Board(it.toTypedArray(), Array(5) { _ -> IntArray(5) }, IntArray(5), IntArray(5))
            }
    }

    fun part1(input: List<String>): Int {
        val numbers = input.first().split(',').map { it.toInt() }
        val boards = toBoards(input.drop(1))

        for (number in numbers) {
            for (board in boards) {
                if (checkBoard(board, number)) {
                    return sumBoard(board) * number
                }
            }
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        val numbers = input.first().split(',').map { it.toInt() }
        val boards = toBoards(input.drop(1))

        var result : Int = 0

        for (number in numbers) {
            for (board in boards) {
                if (!isWinning(board) && checkBoard(board, number)) {
                    result = sumBoard(board) * number
                }
            }
        }

        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    check(part1(input) == 39984)
    check(part2(input) == 8468)
    println(part2(input))
}

