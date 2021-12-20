fun main() {
    fun enlarge(
        tail: List<String>, i : Int, m : Char
    ): List<String> {
        if (i == 0) {
            return tail
        } else {
            val enlarged = enlarge(tail, i - 1, m)
            val marker = "$m".repeat(enlarged.first().length)
            return listOf(marker).plus(enlarged).plus(marker).map { "$m$it$m" }
        }
    }

    fun transform(image: List<String>, enhance: String, i : Int): List<String> {
        if (i== 0) {
            return image
        } else {
            val output =
                (0 until image.size - 2).map { ".".repeat(image.first().length - 2).toCharArray() }.toTypedArray()

            for (j in 1 until image.size - 1) {
                for (i in 1 until image.first().length - 1) {

                    val code =
                        image[j - 1].slice(i - 1..i + 1) + image[j].slice(i - 1..i + 1) + image[j + 1].slice(i - 1..i + 1)
                    val index = code.replace('.', '0').replace('#', '1').toInt(2)

                    val decoded = enhance[index]
                    output[j - 1][i - 1] = decoded
                }
            }
            return transform(output.map { it.joinToString("") }, enhance, i - 1)
        }
    }

    fun part1(input: List<String>): Int {
        val enhance = input.first()
        val tail = input.drop(2)
        val once = transform(enlarge(tail, 2 * 2, '.'), enhance, 2)
        return once.sumOf { it.count('#'::equals) }
    }

    fun part2(input: List<String>): Int {
        val enhance = input.first()
        val tail = input.drop(2)
        val once = transform(enlarge(tail, 50*2, '.'), enhance, 50)
        return once.sumOf { it.count('#'::equals) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3351)

    val input = readInput("Day20")
    check(part1(input) == 5231)
    check(part2(input) == 14279)
}

