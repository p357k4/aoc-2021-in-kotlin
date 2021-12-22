fun main() {
    data class Cube(val action: Int, val xl: Int, val xh: Int, val yl: Int, val yh: Int, val zl: Int, val zh: Int)

    fun read(line: String): Cube {
        val split0 = line.split(' ')
        val action = if (split0[0] == "on") 1 else 0
        val parts = split0[1].split(',')

        val xr = parts[0].substringAfter("x=").split("..").map(String::toInt)
        val yr = parts[1].substringAfter("y=").split("..").map(String::toInt)
        val zr = parts[2].substringAfter("z=").split("..").map(String::toInt)

        return Cube(action, xr[0], xr[1], yr[0], yr[1], zr[0], zr[1])
    }

    fun part1(input: List<String>): Int {
        val cubes = Array(101) { Array(101) { IntArray(101) } }
        val commands = input.map(::read)

        for (command in commands) {
            val xl = maxOf(0, command.xl + 50)
            val xh = minOf(100, command.xh + 50)
            val yl = maxOf(0, command.yl + 50)
            val yh = minOf(100, command.yh + 50)
            val zl = maxOf(0, command.zl + 50)
            val zh = minOf(100, command.zh + 50)

            for (i in xl..xh) {
                for (j in yl..yh) {
                    for (k in zl..zh) {
                        cubes[i][j][k] = command.action
                    }
                }
            }
        }

        var lights = 0
        for (i in 0..100) {
            for (j in 0..100) {
                for (k in 0..100) {
                    lights += cubes[i][j][k]
                }
            }
        }
        return lights
    }

    fun isInside(xl: Int, xh: Int, yl: Int, yh: Int) = xl in yl..yh && xh in yl..yh

    fun cubeInside(on: Cube, off: Cube) =
        isInside(on.xl, on.xh, off.xl, off.xh) &&
                isInside(on.yl, on.yh, off.yl, off.yh) &&
                isInside(on.zl, on.zh, off.zl, off.zh)

    fun split(on: Cube, off: Cube): Collection<Cube> {
        // is on cube total inside off cube?
        if (cubeInside(on, off)) {
            return listOf()
        } else if (on.xl < off.xl && off.xl <= on.xh) {
            val left = Cube(on.action, on.xl, off.xl - 1, on.yl, on.yh, on.zl, on.zh)
            val right = Cube(on.action, off.xl, on.xh, on.yl, on.yh, on.zl, on.zh)
            return split(left, off).plus(split(right, off))
        } else if (on.yl < off.yl && off.yl <= on.yh) {
            val left = Cube(on.action, on.xl, on.xh, on.yl, off.yl - 1, on.zl, on.zh)
            val right = Cube(on.action, on.xl, on.xh, off.yl, on.yh, on.zl, on.zh)
            return split(left, off).plus(split(right, off))
        } else if (on.zl < off.zl && off.zl <= on.zh) {
            val left = Cube(on.action, on.xl, on.xh, on.yl, on.yh, on.zl, off.zl - 1)
            val right = Cube(on.action, on.xl, on.xh, on.yl, on.yh, off.zl, on.zh)
            return split(left, off).plus(split(right, off))
        } else if (on.xl <= off.xh && off.xh < on.xh) {
            val left = Cube(on.action, on.xl, off.xh, on.yl, on.yh, on.zl, on.zh)
            val right = Cube(on.action, off.xh + 1, on.xh, on.yl, on.yh, on.zl, on.zh)
            return split(left, off).plus(split(right, off))
        } else if (on.yl <= off.yh && off.yh < on.yh) {
            val left = Cube(on.action, on.xl, on.xh, on.yl, off.yh, on.zl, on.zh)
            val right = Cube(on.action, on.xl, on.xh, off.yh + 1, on.yh, on.zl, on.zh)
            return split(left, off).plus(split(right, off))
        } else if (on.zl <= off.zh && off.zh < on.zh) {
            val left = Cube(on.action, on.xl, on.xh, on.yl, on.yh, on.zl, off.zh)
            val right = Cube(on.action, on.xl, on.xh, on.yl, on.yh, off.zh + 1, on.zh)
            return split(left, off).plus(split(right, off))
        } else {
            return listOf(on)
        }
    }

    fun volume(cube: Cube) =
        (cube.xh - cube.xl + 1).toLong() * (cube.yh - cube.yl + 1).toLong() * (cube.zh - cube.zl + 1).toLong()

    fun part2(input: List<String>): Long {
        val cubes = input.map(::read)

        var onCubes = listOf<Cube>()

        for (cube in cubes) {
            onCubes = onCubes.flatMap { split(it, cube) }
            if (cube.action == 1) {
                onCubes = onCubes.plus(cube)
            }
        }
        val lights = onCubes.sumOf(::volume)

        return lights
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")
    check(part1(testInput) == 590784)

    val testInput2 = readInput("Day22_test2")
    check(part2(testInput2) == 2758514936282235L)

    val input = readInput("Day22")
    check(part1(input) == 647062)
    check(part2(input) == 1319618626668022L)
}

