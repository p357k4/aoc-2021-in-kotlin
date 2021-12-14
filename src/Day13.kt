fun main() {
    data class Point(val x: Int, val y: Int)

    fun part1(input: List<String>): Int {
        val (f, p) = input.filterNot(String::isEmpty).partition { it.startsWith("fold") }

        val points = p.map {
            val arr = it.split(',')
            Point(arr[0].toInt(), arr[1].toInt())
        }

        val folded = f.take(1).fold(points) { acc, fold ->
            if (fold.startsWith("fold along y=")) {
                val y = fold.removePrefix("fold along y=").toInt()
                acc.map { p ->
                    if (p.y < y) {
                        p
                    } else {
                        Point(p.x, 2 * y - p.y)
                    }
                }
            } else if (fold.startsWith("fold along x=")) {
                val x = fold.removePrefix("fold along x=").toInt()
                acc.map { p ->
                    if (p.x < x) {
                        p
                    } else {
                        Point(2 * x - p.x, p.y)
                    }
                }
            } else {
                acc
            }
        }.toSet()

        return folded.size
    }

    fun part2(input: List<String>): Int {
        val (f, p) = input.filterNot(String::isEmpty).partition { it.startsWith("fold") }

        val points = p.map {
            val arr = it.split(',')
            Point(arr[0].toInt(), arr[1].toInt())
        }

        val folded = f.fold(points) { acc, fold ->
            if (fold.startsWith("fold along y=")) {
                val y = fold.removePrefix("fold along y=").toInt()
                acc.map { p ->
                    if (p.y < y) {
                        p
                    } else {
                        Point(p.x, 2 * y - p.y)
                    }
                }
            } else if (fold.startsWith("fold along x=")) {
                val x = fold.removePrefix("fold along x=").toInt()
                acc.map { p ->
                    if (p.x < x) {
                        p
                    } else {
                        Point(2 * x - p.x, p.y)
                    }
                }
            } else {
                acc
            }
        }.toSet()

        val maxX = folded.maxOf { it.x }
        val maxY = folded.maxOf { it.y }

        for (y in 0..maxY) {
            for (x in 0..maxX) {
                if (folded.contains(Point(x, y))) {
                    print('#')
                } else {
                    print('.')
                }
            }
            println()
        }

        return folded.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    check(part2(testInput) == 16)

    val input = readInput("Day13")
    check(part1(input) == 695)
    check(part2(input) == 118242)
}

