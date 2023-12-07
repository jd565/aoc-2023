fun main() {
    class Hand(input: String, val bid: Int, joker: Boolean = false) : Comparable<Hand> {
        val hand: String = input
                .replace("A", "E")
                .replace("T", "A")
                .replace("J", if (joker) "1" else "B")
                .replace("Q", "C")
                .replace("K", "D")
        val handScore = hand.toLong(15)

        val rank: Int

        init {
            val m = mutableMapOf<Char, Int>()
            hand.forEach { c -> m[c] = m[c]?.plus(1) ?: 1 }
            var sorted = m.entries.sortedByDescending { it.value }
            if (joker) {
                val jokerCount = m['1'] ?: 0
                if (jokerCount != 0 && jokerCount < 5) {
                    val addIdx = if (sorted[0].key == '1') 1 else 0
                    m[sorted[addIdx].key] = m[sorted[addIdx].key]!! + jokerCount
                    m['1'] = 0
                    sorted = m.entries.sortedByDescending { it.value }
                }
            }
            rank = when {
                sorted[0].value == 5 -> 10
                sorted[0].value == 4 -> 9
                sorted[0].value == 3 && sorted[1].value == 2 -> 8
                sorted[0].value == 3 -> 7
                sorted[0].value == 2 && sorted[1].value == 2 -> 6
                sorted[0].value == 2 -> 5
                sorted[0].value == 1 -> 4
                else -> throw IllegalStateException()
            }
        }

        override fun compareTo(other: Hand): Int {
            val rankCompare = rank.compareTo(other.rank)
            if (rankCompare != 0) {
                return rankCompare
            }
            return handScore.compareTo(other.handScore)
        }
    }

    fun part1(input: List<String>): Int {
        return input.map { line ->
            val split = line.split(" ")
            Hand(split[0], split[1].toInt())
        }.sorted()
                .foldIndexed(0) { idx, acc, hand ->
                    acc + hand.bid * (idx + 1)
                }
    }

    fun part2(input: List<String>): Int {
        return input.map { line ->
            val split = line.split(" ")
            Hand(split[0], split[1].toInt(), true)
        }.sorted()
                .foldIndexed(0) { idx, acc, hand ->
                    acc + hand.bid * (idx + 1)
                }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day07_test")
//    check(part1(testInput) == 6440)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
