import java.util.stream.IntStream.IntMapMultiConsumer

enum class D { Up, Down, Left, Right }

fun main() {
    data class Entry(
            val x: Int,
            val y: Int,
            val d: D,
    ) {
        fun next(d: D): Entry = when (d) {
            D.Up -> Entry(x, y - 1, d)
            D.Down -> Entry(x, y + 1, d)
            D.Left -> Entry(x - 1, y, d)
            D.Right -> Entry(x + 1, y, d)
        }
    }

    fun List<List<Char>>.trace(
            c: Entry,
            previous: MutableList<Entry>
    ) {
        if (c.y < 0 || c.y > lastIndex || c.x < 0 || c.x > this[0].lastIndex) return
        if (c in previous) return
        previous.add(c)
        when (this[c.y][c.x]) {
            '\\' -> {
                val d = when (c.d) {
                    D.Up -> D.Left
                    D.Down -> D.Right
                    D.Left -> D.Up
                    D.Right -> D.Down
                }
                trace(c.next(d), previous)
            }

            '/' -> {
                val d = when (c.d) {
                    D.Up -> D.Right
                    D.Down -> D.Left
                    D.Left -> D.Down
                    D.Right -> D.Up
                }
                trace(c.next(d), previous)
            }

            '|' -> {
                when (c.d) {
                    D.Left, D.Right -> {
                        trace(c.next(D.Up), previous)
                        trace(c.next(D.Down), previous)
                    }

                    D.Up, D.Down -> trace(c.next(c.d), previous)
                }
            }

            '-' -> {
                when (c.d) {
                    D.Up, D.Down -> {
                        trace(c.next(D.Left), previous)
                        trace(c.next(D.Right), previous)
                    }

                    D.Left, D.Right -> trace(c.next(c.d), previous)
                }
            }

            '.' -> trace(c.next(c.d), previous)
        }
    }

    fun List<List<Char>>.energise(
            start: Entry
    ): Int {
        val p = mutableListOf<Entry>()
        trace(start, p)
        return p.mapTo(mutableSetOf()) { it.x to it.y }.size
    }

    fun part1(input: List<String>): Int {
        val m = input.map { it.toList() }
        return m.energise(Entry(0, 0, D.Right))
    }

    fun part2(input: List<String>): Int {
        val m = input.map { it.toList() }
        return buildList {
            repeat(m.size) {
                add(Entry(0, it, D.Right))
                add(Entry(m[0].lastIndex, it, D.Left))
            }
            repeat(m[0].size) {
                add(Entry(it, 0, D.Down))
                add(Entry(it, m.lastIndex, D.Up))
            }
        }.maxOf { m.energise(it) }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part2(testInput) == 281)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
