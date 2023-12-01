fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val digits = line.filter { it.isDigit() }
            "${digits.first()}${digits.last()}".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val searches = listOf(
                "one" to 1,
                "two" to 2,
                "three" to 3,
                "four" to 4,
                "five" to 5,
                "six" to 6,
                "seven" to 7,
                "eight" to 8,
                "nine" to 9,
                "zero" to 0,
                "1" to 1,
                "2" to 2,
                "3" to 3,
                "4" to 4,
                "5" to 5,
                "6" to 6,
                "7" to 7,
                "8" to 8,
                "9" to 9,
                "0" to 0
        )
        return input.sumOf { line ->
            var firstIndex: Pair<Int, Int> = Pair(Int.MAX_VALUE, 0)
            var lastIndex: Pair<Int, Int> = Pair(-1, 0)
            searches.forEach { (search, value) ->
                val idx = line.indexOf(search)
                if (idx >= 0) {
                    if (idx < firstIndex.first) {
                        firstIndex = Pair(idx, value)
                    }

                    val lastIdx = line.lastIndexOf(search)
                    if (lastIdx > lastIndex.first) {
                        lastIndex = Pair(lastIdx, value)
                    }
                }
            }
            firstIndex.second * 10 + lastIndex.second
        }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
