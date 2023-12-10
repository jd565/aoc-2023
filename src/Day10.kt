import com.sun.nio.sctp.IllegalUnbindException

fun main() {
    fun List<List<Char>>.next(l: Pair<Int, Int>, prev: Pair<Int, Int>?): Pair<Int, Int> {
        val piece = this[l.first][l.second]
        val options = when (piece) {
            '|' -> listOf(Pair(l.first - 1, l.second), Pair(l.first + 1, l.second))
            '-' -> listOf(Pair(l.first, l.second - 1), Pair(l.first, l.second + 1))
            'L' -> listOf(Pair(l.first - 1, l.second), Pair(l.first, l.second + 1))
            'J' -> listOf(Pair(l.first - 1, l.second), Pair(l.first, l.second - 1))
            '7' -> listOf(Pair(l.first, l.second - 1), Pair(l.first + 1, l.second))
            'F' -> listOf(Pair(l.first, l.second + 1), Pair(l.first + 1, l.second))
            else -> throw IllegalStateException("$l not on loop")
        }
        if (prev == null) {
            return options[0]
        }
        val filtered = options.filter { it != prev }
        check(filtered.size == 1)
        return filtered[0]
    }

    fun part1(input: List<String>): Int {
        val map = input.map { it.toMutableList() }
        var startX = -1
        val startY = map.indexOfFirst {
            startX = it.indexOf('S')
            startX != -1
        }
        val start = Pair(startY, startX)

        val guessPieces = listOf('|', '-', 'L', 'J', '7', 'F')
        guessPieces.forEach {
            map[startY].apply {
                removeAt(startX)
                add(startX, it)
            }
            var location = Pair(startY, startX)
            location = map.next(location, null)
            val loop = mutableListOf(start)
            try {
                do {
                    loop.add(location)
                    location = map.next(location, loop[loop.lastIndex - 1])
                } while (location != start && location !in loop)
                if (location == start) {
                    // Check the start piece actually fits
                    if (map.next(start, loop[1]) == loop.last()) {
                        return loop.size / 2
                    }
                }
            } catch (e: IllegalStateException) {
                // Ignore
            }
        }
        return -1
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.toMutableList() }
        var startX = -1
        val startY = map.indexOfFirst {
            startX = it.indexOf('S')
            startX != -1
        }
        val start = Pair(startY, startX)

        val guessPieces = listOf('|', '-', 'L', 'J', '7', 'F')
        guessPieces.forEach {
            map[startY].apply {
                removeAt(startX)
                add(startX, it)
            }
            var location = Pair(startY, startX)
            location = map.next(location, null)
            val loop = mutableListOf(start)
            try {
                do {
                    loop.add(location)
                    location = map.next(location, loop[loop.lastIndex - 1])
                } while (location != start && location !in loop)
            } catch (e: IllegalStateException) {
                // Ignore
            }
            if (location == start) {
                // Check the start piece actually fits
                if (map.next(start, loop[1]) == loop.last()) {
                    var insideCount = 0
                    map.forEachIndexed { y, row ->
                        var inside = false
                        var dir = 0
                        row.forEachIndexed { x, c ->
                            if (Pair(y, x) in loop) {
                                when (c) {
                                    'L' -> when (dir) {
                                        0 -> dir = 1
                                        1 -> dir = 0
                                        -1 -> {
                                            dir = 0
                                            inside = !inside
                                        }
                                    }
                                    'J' -> when (dir) {
                                        0 -> dir = 1
                                        1 -> dir = 0
                                        -1 -> {
                                            dir = 0
                                            inside = !inside
                                        }
                                    }
                                    '|' -> {
                                        check(dir == 0)
                                        inside = !inside
                                    }

                                    '7' -> when (dir) {
                                        0 -> dir = -1
                                        -1 -> dir = 0
                                        1 -> {
                                            dir = 0
                                            inside = !inside
                                        }
                                    }
                                    'F' -> when (dir) {
                                        0 -> dir = -1
                                        -1 -> dir = 0
                                        1 -> {
                                            dir = 0
                                            inside = !inside
                                        }
                                    }
                                }
                            } else if (inside) {
                                insideCount++
                            }
                        }
                    }
                    return insideCount
                }
            }
        }
        return -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part2(testInput) == 8)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
