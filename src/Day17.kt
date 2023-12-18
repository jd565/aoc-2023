import java.util.PriorityQueue

fun main() {
    data class Entry(
            val x: Int,
            val y: Int,
            val dx: Int,
            val dy: Int,
            val blocks: Int,
    ) {
        fun next(minBlocks: Int, maxBlocks: Int): List<Entry> {
            return when {
                blocks < minBlocks -> listOf(Entry(x + dx, y + dy, dx, dy, blocks + 1))
                else -> {
                    listOfNotNull(
                            Entry(x + dy, y + dx, dy, dx, 1), // Turn left
                            Entry(x - dy, y - dx, -dy, -dx, 1), // Turn right
                            if (blocks < maxBlocks) Entry(x + dx, y + dy, dx, dy, blocks + 1) else null
                    )
                }
            }
        }
    }

    data class EntryWithCost(val e: Entry, val c: Int): Comparable<EntryWithCost> {
        override fun compareTo(other: EntryWithCost): Int {
            return c.compareTo(other.c)
        }
    }

    fun List<List<Int>>.minimise(entries: List<Entry>, minBlocks: Int, maxBlocks: Int): Int {
        val endX = this.lastIndex

        val costs = mutableMapOf<Entry, Int>().withDefault { Int.MAX_VALUE }
        val toVisit = PriorityQueue<EntryWithCost>()

        entries.forEach {
            costs[it] = 0
            toVisit.add(EntryWithCost(it, 0))
        }

        while (toVisit.isNotEmpty()) {
            val current = toVisit.poll()
            if (current.e.x == endX && current.e.y == endX) {
                return current.c
            }

            current.e.next(minBlocks, maxBlocks)
                    .filter { it.y in this.indices && it.x in this.first().indices }
                    .forEach { next ->
                        val newCost = current.c + this[next.y][next.x]
                        if (newCost < costs.getValue(next)) {
                            costs[next] = newCost
                            toVisit.add(EntryWithCost(next, newCost))
                        }
                    }
        }

        error("No path found")
    }

    fun part1(input: List<String>): Int {
        val m = input.map { it.map { c -> c.digitToInt() } }
        return m.minimise(listOf(
                Entry(0, 0, 0, 1, 0),
                Entry(0, 0, 1, 0, 0),
        ), 0, 3)
    }

    fun part2(input: List<String>): Int {
        val m = input.map { it.map { c -> c.digitToInt() } }
        return m.minimise(listOf(
                Entry(0, 0, 0, 1, 0),
                Entry(0, 0, 1, 0, 0),
        ), 4, 10)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part1(testInput) == 102)

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}
