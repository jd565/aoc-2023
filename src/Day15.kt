import java.util.Stack

fun main() {
    fun hash(input: String): Int {
        var current = 0
        input.forEach { c ->
            current += c.code
            current *= 17
            current = current.and(255)
        }
        return current
    }

    fun part1(input: List<String>): Int {
        return input[0].split(",").sumOf { hash(it) }
    }

    fun part2(input: List<String>): Int {
        val r = "^([a-z]+)([=-])(\\d*)$".toRegex()

        val map = Array<ArrayDeque<Pair<String, Int>>>(256) { ArrayDeque() }

        input[0].split(",").map {
            val m = r.find(it)
            check(m != null)
            m.groupValues.drop(1)
        }.forEach { matches ->
            val label = matches[0]
            val op = matches[1]
            val i = matches.getOrNull(2)?.ifEmpty { null }?.toInt()

            val idx = hash(label)
            val dq = map[idx]
            when (op) {
                "=" -> {
                    val labelIdx = dq.indexOfFirst { it.first == label }
                    if (labelIdx >= 0) {
                        dq[labelIdx] = Pair(label, i!!)
                    } else {
                        dq.addLast(Pair(label, i!!))
                    }
                }

                "-" -> dq.removeIf { it.first == label }
                else -> throw IllegalStateException("Bad op $op")
            }
        }
        return map.mapIndexed { box, dq ->
            dq.mapIndexed { idx, (_, p) ->
                (1 + box) * (idx + 1) * p
            }.sum()
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day15_test")
//    check(part2(testInput) == 145)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
