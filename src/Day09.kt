fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val d = mutableListOf(line.split(" ").mapTo(mutableListOf()) { it.toInt() })
            while (d.last().any { it != 0 }) {
                val nextLevel = d.last().mapIndexedNotNullTo(mutableListOf()) { idx, it ->
                    if (idx == 0) {
                        null
                    } else {
                        it - d.last()[idx-1]
                    }
                }
                d.add(nextLevel)
            }
            d.last().add(0)
            d.reverse()
            d.forEachIndexed { idx, it ->
                if (idx != 0) {
                    it.add(it.last() + d[idx-1].last())
                }
            }
            d.last().last()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val d = mutableListOf(line.split(" ").mapTo(mutableListOf()) { it.toInt() })
            while (d.last().any { it != 0 }) {
                val nextLevel = d.last().mapIndexedNotNullTo(mutableListOf()) { idx, it ->
                    if (idx == 0) {
                        null
                    } else {
                        it - d.last()[idx-1]
                    }
                }
                d.add(nextLevel)
            }
            d.last().add(0)
            d.reverse()
            d.forEachIndexed { idx, it ->
                if (idx != 0) {
                    it.add(0, it[0] - d[idx-1][0])
                }
            }
            d.last().first()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
