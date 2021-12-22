fun main() {
    data class Vector(val x: Int, val y: Int, val z: Int)

    val transforms = arrayOf(
        { v: Vector -> Vector(v.x, v.y, v.z) },
        { v: Vector -> Vector(-v.x, -v.y, v.z) },
        { v: Vector -> Vector(-v.x, v.y, -v.z) },
        { v: Vector -> Vector(v.x, -v.y, -v.z) },

        { v: Vector -> Vector(-v.x, v.z, v.y) },
        { v: Vector -> Vector(v.x, -v.z, v.y) },
        { v: Vector -> Vector(v.x, v.z, -v.y) },
        { v: Vector -> Vector(-v.x, -v.z, -v.y) },

        { v: Vector -> Vector(-v.y, v.x, v.z) },
        { v: Vector -> Vector(v.y, -v.x, v.z) },
        { v: Vector -> Vector(v.y, v.x, -v.z) },
        { v: Vector -> Vector(-v.y, -v.x, -v.z) },

        { v: Vector -> Vector(v.y, v.z, v.x) },
        { v: Vector -> Vector(-v.y, -v.z, v.x) },
        { v: Vector -> Vector(-v.y, v.z, -v.x) },
        { v: Vector -> Vector(v.y, -v.z, -v.x) },

        { v: Vector -> Vector(v.z, v.x, v.y) },
        { v: Vector -> Vector(-v.z, -v.x, v.y) },
        { v: Vector -> Vector(-v.z, v.x, -v.y) },
        { v: Vector -> Vector(v.z, -v.x, -v.y) },

        { v: Vector -> Vector(-v.z, v.y, v.x) },
        { v: Vector -> Vector(v.z, -v.y, v.x) },
        { v: Vector -> Vector(v.z, v.y, -v.x) },
        { v: Vector -> Vector(-v.z, -v.y, -v.x) },
    )

    fun read(input: Collection<String>): Collection<Set<Vector>> {
        val fold = input.fold(Pair(listOf<Set<Vector>>(), setOf<Vector>())) { acc, line ->
            if (line == "") {
                Pair(acc.first.plusElement(acc.second), setOf())
            } else if (!line.startsWith("--- ")) {
                val split = line.split(',').map(String::toInt)
                val triple = Vector(split[0], split[1], split[2])
                Pair(acc.first, acc.second.plus(triple))
            } else {
                acc
            }
        }

        return fold.first.plusElement(fold.second)
    }

    fun sub(start: Vector, end: Vector) = Vector(
        end.x - start.x,
        end.y - start.y,
        end.z - start.z
    )

    fun add(start: Vector, end: Vector) = Vector(
        end.x + start.x,
        end.y + start.y,
        end.z + start.z
    )

    fun diffs(left: Collection<Vector>, right: Collection<Vector>) =
        left.flatMap { start -> right.map { end -> sub(end, start) } }

    fun part1(input: Collection<String>): Int {
        val cubes = read(input)

        val vectors = mutableListOf<Vector>()

        val origins = cubes.map { Vector(0, 0, 0) }.toMutableList()
        val transformed = cubes.toMutableList()

        vectors.addAll(transformed[0])

        for (i in 0 until transformed.size - 1) {
            for (j in i + 1 until transformed.size) {
                if (origins[j] != Vector(0, 0, 0)) {
                    continue
                }
                val left = transformed[i]
                val right = transformed[j]
                val g = transforms
                    .map { transform ->
                        val rightTransformed = right.map(transform)
                        val diffs = diffs(left, rightTransformed)

                        val grouped = diffs.groupBy { it }
                        val sized = grouped.mapValues { it.value.size }
                        Pair(transform, sized)
                    }
                    .filter { it.second.filter { it.value >= 12 }.isNotEmpty() }

                if (g.isEmpty()) {
                    continue
                }

                val t = g.first()

                val delta = t.second.filter { it.value >= 12 }.map { it.key }.first()
                origins[j] = add(origins[i], delta)
                val f = transformed[j].map(t.first).toSet()
                vectors.addAll(f.map { add(it, origins[j]) })
                transformed[j] = f
            }
        }

        val g = vectors.toSet().sortedBy { it.x }
        return vectors.toSet().size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
//    check(part1(testInput) == 79)
//    check(part2(testInput) == 3993)

    val input = readInput("Day19")
    check(part1(input) == 3524)
//    check(part2(input) == 3282)
}

