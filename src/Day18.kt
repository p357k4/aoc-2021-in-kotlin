sealed interface Snail
object SnailOpen : Snail {
    override fun toString(): String {
        return "["
    }
}

object SnailClose : Snail {
    override fun toString(): String {
        return "]"
    }
}

data class SnailValue(val value: Int) : Snail {
    override fun toString(): String {
        return value.toString()
    }
}

fun main() {
    fun add(left: List<Snail>, right: List<Snail>): List<Snail> {
        return listOf(SnailOpen).plus(left).plus(right).plus(SnailClose)
    }

    fun read(input: String, snail: List<Snail>): Pair<List<Snail>, String> {
        return if (input.first() == '[') {
            val (left, leftTail) = read(input.drop(1), snail)
            val (right, rightTail) = read(leftTail, snail)
            Pair(add(left, right), rightTail.drop(1))
        } else {
            val split = input.split(',', ']', limit = 2)
            val number = split[0].toInt()
            Pair(listOf<Snail>(SnailValue(number)), split[1])
        }
    }

    fun split(list: List<Snail>): Pair<Boolean, List<Snail>> {
        val snail = list.toMutableList()
        for (i in 0 until snail.size - 1) {
            val si0 = snail[i]
            if (si0 is SnailValue && si0.value >= 10) {
                val left = si0.value / 2
                val right = si0.value - left
                val before = snail.slice(0 until i)
                val after = snail.slice(i + 1 until snail.size)
                return Pair(
                    true,
                    before.plus(SnailOpen).plus(SnailValue(left)).plus(SnailValue(right)).plus(SnailClose).plus(after)
                )
            }
        }

        return Pair(false, snail)
    }

    fun explode(list: List<Snail>): Pair<Boolean, List<Snail>> {
        val snail = list.toMutableList()
        var opened = 0
        var closed = 0
        for (i in 0 until snail.size - 1) {
            val si0 = snail[i]
            val si1 = snail[i + 1]
            if (si0 is SnailOpen) {
                opened++
            }
            if (si0 is SnailClose) {
                closed++
            }
            if (si0 is SnailValue && si1 is SnailValue && opened - closed == 5) {
                for (j in i - 1 downTo 0) {
                    val sj = snail[j]
                    if (sj is SnailValue) {
                        snail[j] = SnailValue(sj.value + si0.value)
                        break;
                    }
                }

                for (j in i + 3 until snail.size) {
                    val sj = snail[j]
                    if (sj is SnailValue) {
                        snail[j] = SnailValue(sj.value + si1.value)
                        break;
                    }
                }

                val before = snail.slice(0 until i - 1)
                val after = snail.slice(i + 3 until snail.size)
                return Pair(true, before.plus(SnailValue(0)).plus(after))
            }
        }

        return Pair(false, snail)
    }

    fun magnitude(snail: List<Snail>, index: Int): Pair<Int, Int> {
        return when (val first = snail[index]) {
            is SnailValue -> Pair(first.value, index + 1)
            is SnailClose -> Pair(0, index + 1)
            is SnailOpen -> {
                val (left, leftIndex) = magnitude(snail, index + 1)
                val (right, rightIndex) = magnitude(snail, leftIndex)
                Pair(3 * left + 2 * right, rightIndex + 1)
            }
        }
    }

    fun calculate(snail: List<Snail>, lastExploded: Boolean, lastSplit: Boolean): List<Snail> {
        return if (lastExploded) {
            val (exploded, fallout) = explode(snail)
            calculate(fallout, exploded, lastSplit)
        } else if (lastSplit) {
            val (split, fallout) = split(snail)
            calculate(fallout, true, split)
        } else {
            val (exploded, fallout) = explode(snail)
            if (exploded) {
                calculate(fallout, true, true)
            } else {
                snail
            }
        }
    }

    fun part1(input: List<String>): Int {
        val pairs = input.map { read(it, listOf()).first }

        val reduced = pairs.reduce { p, q ->
            val r = add(p, q)
            val s = calculate(r, true, true)
            s
        }

        val mag = magnitude(reduced, 0)
        return mag.first
    }

    fun part2(input: List<String>): Int {
        val pairs = input.map { read(it, listOf()).first }

        val f = pairs
            .flatMap { outer -> pairs.filterNot { outer == it }.map { magnitude(calculate(add(outer, it), true, true), 0).first } }
            .maxOf { it }
        return f
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    check(part1(input) == 3524)
    check(part2(input) == 4656)
}

