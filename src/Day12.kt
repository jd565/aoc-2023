fun main() {
    val mem = mutableMapOf<Pair<String, List<Int>>, Long>()
    fun solve(gears: String, counts: List<Int>): Long {
        mem[Pair(gears, counts)]?.let {
            return it
        }
        if (counts.isEmpty()) {
            if ('#' in gears) {
                return 0
            }
            return 1
        }
        val endCharsSize = counts.drop(1).sum() + counts.size - 1
        val endIndex = gears.length - endCharsSize
        val area = gears.substring(0, endIndex)
        val count = counts.first()
        if (area.length < count) {
            return 0
        }
        val s = "[?#]{${count},}".toRegex().findAll(area)
                .filter { it.value.length >= count }
                .map { match ->
                    List(match.value.length - count + 1) { listIdx ->
                        try {
                            val out = gears.toMutableList()
                            repeat(match.range.first + listIdx) {
                                if (out[it] == '?') {
                                    out.removeAt(it)
                                    out.add(it, '.')
                                } else if (out[it] == '#') {
                                    out.removeAt(it)
                                    out.add(it, 'x')
                                }
                            }
                            repeat(count) {
                                val idx = match.range.first + it + listIdx
                                out.removeAt(idx)
                                out.add(idx, 'x')
                            }
                            val endIdx = match.range.first + listIdx + count
                            if (out.lastIndex >= endIdx) {
                                check(out[endIdx] != '#')
                                out.removeAt(endIdx)
                                out.add(endIdx, '.')
                            }
                            val sIdx = match.range.first + listIdx - 1
                            if (sIdx >= 0) {
                                check(out[sIdx] != 'x')
                            }
                            check(out.count { it == 'x' } == count)
                            out
                        } catch (e: Exception) {
                            null
                        }
                    }.mapNotNull {
                        val c = it?.joinToString(separator = "")
                        c
                    }
                }.flatten()
                .sumOf {
                    solve(it.substringAfterLast("x."), counts.drop(1))
                }
        mem[Pair(gears, counts)] = s
        return s
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            val (gears, countsString) = line.split(" ")
            val counts = countsString.split(",").map { it.toInt() }
            solve(gears, counts)
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val (gearsInit, countsStringInit) = line.split(" ")
            val gears = List(5) { gearsInit }.joinToString(separator = "?")
            val countsString = List(5) { countsStringInit }.joinToString(separator = ",")
            val counts = countsString.split(",").map { it.toInt() }
            solve(gears, counts)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
