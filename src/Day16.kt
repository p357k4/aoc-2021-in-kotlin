sealed class Packet {
    abstract val version: Int
    abstract val typeId: Int
}

data class Literal(override val version: Int, override val typeId: Int, val value: Long) : Packet()
data class Operator(override val version: Int, override val typeId: Int, val sub: List<Packet>) : Packet()

fun main() {
    fun sumVersion(packet: Packet): Int =
        when (packet) {
            is Literal -> packet.version
            is Operator -> packet.version + packet.sub.sumOf(::sumVersion)
        }

    fun calculate(packet: Packet): Long =
        when (packet) {
            is Literal -> packet.value
            is Operator -> {
                val list = packet.sub.map(::calculate)
                when (packet.typeId) {
                    0 -> list.reduce(Long::plus)
                    1 -> list.reduce(Long::times)
                    2 -> list.minOf { it }
                    3 -> list.maxOf { it }
                    5 -> if (list[0] > list[1]) 1L else 0L
                    6 -> if (list[0] < list[1]) 1L else 0L
                    7 -> if (list[0] == list[1]) 1L else 0L
                    else -> 0L
                }
            }
        }

    fun toBitString(line: String) = line
        .flatMap { Integer.toBinaryString(it.digitToInt(16)).padStart(4, '0').asIterable() }
        .joinToString("")

    fun decodeLiteral(input: String, index: Int, base: Long): Pair<Int, Long> {
        val nextIndex = index + 5
        val delta = input.substring(index + 1 until nextIndex).toLong(2)
        val value = base * 16 + delta
        return if (input[index] == '1') {
            decodeLiteral(input, nextIndex, value)
        } else {
            Pair(nextIndex, value)
        }
    }

    fun decode(input: String, index: Int): Pair<Int, Packet> {
        val versionStart = index
        val versionEnd = versionStart + 2
        val typeStart = versionEnd + 1
        val typeEnd = typeStart + 2
        val packetStart = typeEnd + 1
        val version = input.substring(versionStart..versionEnd).toInt(2)
        val typeID = input.substring(typeStart..typeEnd).toInt(2)
        return if (typeID == 4) {
            val (lastIndex, value) = decodeLiteral(input, packetStart, 0)
            Pair(lastIndex, Literal(version, typeID, value))
        } else {
            val subpackets = mutableListOf<Packet>()

            if (input[packetStart] == '0') {
                val totalLengthInBits = input.substring(packetStart + 1..packetStart + 15).toInt(2)
                var nextIndex = packetStart + 16
                val lastBitIndex = nextIndex + totalLengthInBits

                do {
                    val (lastIndex, subpacket) = decode(input, nextIndex)
                    subpackets.add(subpacket)
                    nextIndex = lastIndex
                } while (lastIndex < lastBitIndex)

                Pair(nextIndex, Operator(version, typeID, subpackets))
            } else {
                val numberOfSubPackets = input.substring(packetStart + 1..packetStart + 11).toInt(2)
                var nextIndex = packetStart + 12

                for (i in 1..numberOfSubPackets) {
                    val (lastIndex, subpacket) = decode(input, nextIndex)
                    subpackets.add(subpacket)
                    nextIndex = lastIndex
                }

                Pair(nextIndex, Operator(version, typeID, subpackets))
            }
        }
    }

    fun part1(input: String): Int {
        val bits = toBitString(input)
        val p = decode(bits, 0)
        val s = sumVersion(p.second)
        return s
    }

    fun part2(input: String): Long {
        val bits = toBitString(input)
        val p = decode(bits, 0)
        val c = calculate(p.second)
        return c
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput[8]) == 16)
    check(part1(testInput[9]) == 12)
    check(part1(testInput[10]) == 23)
    check(part1(testInput[11]) == 31)
    check(part2(testInput[0]) == 3L)
    check(part2(testInput[1]) == 54L)
    check(part2(testInput[2]) == 7L)
    check(part2(testInput[3]) == 9L)
    check(part2(testInput[4]) == 1L)
    check(part2(testInput[5]) == 0L)
    check(part2(testInput[6]) == 0L)
    check(part2(testInput[7]) == 1L)

    val input = readInput("Day16")
    check(part1(input[0]) == 901)
    check(part2(input[0]) == 110434737925L)
}

