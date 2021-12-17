import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    data class Trench(val x1 : Int, val x2 : Int, val y1 : Int, val y2 : Int)

    fun trentch(input: List<String>): Trench {
        val p0 = input.first().substringAfter("target area: x=")
        val x0 = p0.substringBefore(", y=")
        val y0 = p0.substringAfter(", y=")
        val x1 = x0.split("..").map(String::toInt)
        val y1 = y0.split("..").map(String::toInt)

        return Trench(x1[0], x1[1], y1[0], y1[1])
    }

    fun part1(input: List<String>): Int {
        val trench = trentch(input)

        fun go(x : Int, y : Int, vx : Int, vy : Int, max : Int) : Int? {
            return if (vx == 0 && !(trench.x1 <= x && x <= trench.x2)) {
                null
            } else if (trench.x1 <= x && x <= trench.x2 && trench.y1 <= y && y <= trench.y2) {
                max
            } else if (y < trench.y1) {
                null
            } else {
                go(x + vx, y + vy, vx.sign * maxOf(vx.absoluteValue - 1, 0), vy - 1, maxOf(max, y))
            }
        }

        var max = 0
        for(vx in -100 .. 100) {
            for(vy in -1000 .. 1000) {
                val res = go(0, 0, vx, vy, max)
                if (res != null && res > max) {
                    max = res
                }
            }
        }

        return max
    }

    fun part2(input: List<String>): Int {
        val trench = trentch(input)

        fun go(x : Int, y : Int, vx : Int, vy : Int, max : Int) : Int? {
            return if (vx == 0 && !(trench.x1 <= x && x <= trench.x2)) {
                null
            } else if (trench.x1 <= x && x <= trench.x2 && trench.y1 <= y && y <= trench.y2) {
                max
            } else if (y < trench.y1) {
                null
            } else {
                go(x + vx, y + vy, vx.sign * maxOf(vx.absoluteValue - 1, 0), vy - 1, maxOf(max, y))
            }
        }

        var count = 0
        for(vx in -100 .. 100) {
            for(vy in -1000 .. 1000) {
                val res = go(0, 0, vx, vy, 0)
                if (res != null) {
                    count++
                }
            }
        }

        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    check(part1(input) == 35511)
    check(part2(input) == 3282)
}

