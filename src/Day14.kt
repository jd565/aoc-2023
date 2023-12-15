import java.nio.file.Files.move

fun main() {
    fun Array<CharArray>.move(direction: String, size: Pair<Int, Int>) {
        val adj = when (direction) {
            "u" -> Pair(-1, 0)
            "d" -> Pair(1, 0)
            "l" -> Pair(0, -1)
            "r" -> Pair(0, 1)
            else -> throw Exception()
        }
        repeat(size.first) { yIdx ->
            val y = if (direction == "d") {
                size.first - yIdx - 1
            } else { yIdx }
            repeat(size.second) { xIdx ->
                val x = if (direction == "r") {
                    size.second - xIdx - 1
                } else { xIdx }

                if (this[y][x] == 'O') {
                    this[y][x] = '.'
                    var newY = y + adj.first
                    var newX = x + adj.second
                    while (this.getOrNull(newY)?.getOrNull(newX) == '.') {
                        newY += adj.first
                        newX += adj.second
                    }
                    this[newY - adj.first][newX - adj.second] = 'O'
                }
            }
        }
    }

    fun Array<CharArray>.cycle(size: Pair<Int, Int>) {
        listOf("u", "l", "d", "r").forEach {
            move(it, size)
        }
    }

    fun Array<CharArray>.string(): String {
        return joinToString(separator = "") {
            it.joinToString(separator = "")
        }
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }.toTypedArray()
        grid.move("u", Pair(input.size, input[0].length))
        return grid.mapIndexed { y, l ->
            l.sumOf {
                if (it == 'O') input.size - y else 0
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }.toTypedArray()
        val size = Pair(input.size, input[0].length)
        val previous = mutableListOf<String>()
        var new: String = grid.string()
        var count = 0
        do {
            previous.add(new)
            grid.cycle(size)
            count++
            new = grid.string()
        } while (new !in previous)
        val idx = previous.indexOf(new)
        check(idx >= 0)
        val loopSize = previous.size - idx
        println(loopSize)
        val target = 1000000000
        while (count + loopSize < target) {
            count += loopSize
        }
        while (count < target) {
            grid.cycle(size)
            count++
        }

        return grid.mapIndexed { y, l ->
            l.sumOf {
                if (it == 'O') input.size - y else 0
            }
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part2(testInput) == 281)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
