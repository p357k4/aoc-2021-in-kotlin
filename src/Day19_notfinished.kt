fun main() {
    data class Vector(val first: Int, val second: Int, val third: Int)

    val transforms = arrayOf(
        { v: Vector -> Vector(-v.first, -v.second, -v.third) },
        { v: Vector -> Vector(-v.first, -v.third, -v.second) },
        { v: Vector -> Vector(-v.second, -v.first, -v.third) },
        { v: Vector -> Vector(-v.second, -v.third, -v.first) },
        { v: Vector -> Vector(-v.third, -v.first, -v.second) },
        { v: Vector -> Vector(-v.third, -v.second, -v.first) },

        { v: Vector -> Vector(v.first, -v.second, -v.third) },
        { v: Vector -> Vector(v.first, -v.third, -v.second) },
        { v: Vector -> Vector(v.second, -v.first, -v.third) },
        { v: Vector -> Vector(v.second, -v.third, -v.first) },
        { v: Vector -> Vector(v.third, -v.first, -v.second) },
        { v: Vector -> Vector(v.third, -v.second, -v.first) },

        { v: Vector -> Vector(-v.first, v.second, -v.third) },
        { v: Vector -> Vector(-v.first, v.third, -v.second) },
        { v: Vector -> Vector(-v.second, v.first, -v.third) },
        { v: Vector -> Vector(-v.second, v.third, -v.first) },
        { v: Vector -> Vector(-v.third, v.first, -v.second) },
        { v: Vector -> Vector(-v.third, v.second, -v.first) },

        { v: Vector -> Vector(v.first, v.second, -v.third) },
        { v: Vector -> Vector(v.first, v.third, -v.second) },
        { v: Vector -> Vector(v.second, v.first, -v.third) },
        { v: Vector -> Vector(v.second, v.third, -v.first) },
        { v: Vector -> Vector(v.third, v.first, -v.second) },
        { v: Vector -> Vector(v.third, v.second, -v.first) },

        { v: Vector -> Vector(-v.first, -v.second, v.third) },
        { v: Vector -> Vector(-v.first, -v.third, v.second) },
        { v: Vector -> Vector(-v.second, -v.first, v.third) },
        { v: Vector -> Vector(-v.second, -v.third, v.first) },
        { v: Vector -> Vector(-v.third, -v.first, v.second) },
        { v: Vector -> Vector(-v.third, -v.second, v.first) },

        { v: Vector -> Vector(v.first, -v.second, v.third) },
        { v: Vector -> Vector(v.first, -v.third, v.second) },
        { v: Vector -> Vector(v.second, -v.first, v.third) },
        { v: Vector -> Vector(v.second, -v.third, v.first) },
        { v: Vector -> Vector(v.third, -v.first, v.second) },
        { v: Vector -> Vector(v.third, -v.second, v.first) },

        { v: Vector -> Vector(-v.first, v.second, v.third) },
        { v: Vector -> Vector(-v.first, v.third, v.second) },
        { v: Vector -> Vector(-v.second, v.first, v.third) },
        { v: Vector -> Vector(-v.second, v.third, v.first) },
        { v: Vector -> Vector(-v.third, v.first, v.second) },
        { v: Vector -> Vector(-v.third, v.second, v.first) },

        { v: Vector -> Vector(v.first, v.second, v.third) },
        { v: Vector -> Vector(v.first, v.third, v.second) },
        { v: Vector -> Vector(v.second, v.first, v.third) },
        { v: Vector -> Vector(v.second, v.third, v.first) },
        { v: Vector -> Vector(v.third, v.first, v.second) },
        { v: Vector -> Vector(v.third, v.second, v.first) },
    )

//    tailrec fun rotateX(v: Vector, i: Int): Vector = { if (i == 0) v else rotateX(Vector(v.first, -v.third, v.second), i - 1) }
//    tailrec fun rotateY(v: Vector, i: Int): Vector = { if (i == 0) v else rotateY(Vector(v.third, v.second, -v.first), i -1) }
//    tailrec fun rotateZ(v: Vector, i: Int): Vector = { if (i == 0) v else rotateZ(Vector(-v.second, v.first, v.third), i -1) }

    fun read(input: List<String>): List<List<Vector>> {
        val fold = input.fold(Pair(listOf<List<Vector>>(), listOf<Vector>())) { acc, line ->
            if (line == "") {
                Pair(acc.first.plusElement(acc.second), listOf())
            } else if (!line.startsWith("---")) {
                val split = line.split(',').map(String::toInt)
                val triple = Vector(split[0], split[1], split[2])
                Pair(acc.first, acc.second.plus(triple))
            } else {
                acc
            }
        }

        return fold.first.plusElement(fold.second)
    }

    fun sub(end: Vector, start: Vector) = Vector(
        end.first - start.first,
        end.second - start.second,
        end.third - start.third
    )

    fun add(start: Vector, end: Vector) = Vector(
        end.first + start.first,
        end.second + start.second,
        end.third + start.third
    )

    fun diffs(left: List<Vector>, right: List<Vector>): List<Vector> {
        return left.flatMap { start ->
            right.map { end ->
                sub(end, start)
            }
        }
    }


    fun part1(input: List<String>): Int {
        val cubes = read(input)

        val vectors = mutableListOf<Vector>()

        val origins = cubes.map { Vector(0, 0, 0) }.toMutableList()
        val transformed = cubes.toMutableList()

        vectors.addAll(transformed[0])

        for (i in 0 until transformed.size - 1) {
            for (j in i + 1 until transformed.size) {
                if (origins[j] != Vector(0,0,0)) {
                    continue
                }
                val left = transformed[i]
                val right = transformed[j]
                val g = transforms
                    .flatMap { transform ->
                        val rightTransformed = right.map(transform)
                        val d = diffs(rightTransformed, left)
                                .groupBy { it }
                                .mapValues { Pair(transform, it.value.size) }
                        d.toList()
                    }

                val t = g
                    .filter { it.second.second >= 12 }
                    .maxByOrNull { it.second.second }

                if (t != null) {
                    origins[j] = add(origins[i], t.first)
                    val f = transformed[j].map(t.second.first).map { add(it, origins[j]) }
                    vectors.addAll(f)
                    //transformed[j] = f
                }
            }
        }

        val g = vectors.toSet().sortedBy { it.first }
        return vectors.toSet().size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 79)
//    check(part2(testInput) == 3993)

    val input = readInput("Day19")
    check(part1(input) == 3524)
//    check(part2(input) == 3282)
}

