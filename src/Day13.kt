fun main() {
    fun part1(input: List<String>): Int {
        val patterns = mutableListOf<MutableList<List<Char>>>(mutableListOf())
        input.forEach {
            if (it.isEmpty()) {
                patterns.add(mutableListOf())
            } else {
                patterns.last().add(it.toList())
            }
        }
        return patterns.sumOf { pattern ->
            val vSize = pattern[0].size
            var vPoss = List(vSize - 1) { it }
            pattern.forEach { line ->
                vPoss = vPoss.filter { v ->
                    line.mapIndexed { index, c ->
                        val ref = 2 * v + 1 - index
                        line.getOrNull(ref)?.let {
                            c == it
                        } ?: true
                    }.all { it }
                }
            }
            if (vPoss.isNotEmpty()) {
                check(vPoss.size == 1)
                return@sumOf vPoss[0] + 1
            }

            val hSize = pattern.size
            var hPoss = List(hSize - 1) { it }
            repeat(vSize) { col ->
                hPoss = hPoss.filter { h ->
                    (0 until hSize).map { index ->
                        val c = pattern[index][col]
                        val ref = 2 * h + 1 - index
                        pattern.getOrNull(ref)?.get(col)?.let {
                            c == it
                        } ?: true
                    }.all { it }
                }
            }
            check(hPoss.size == 1)
            (hPoss[0] + 1) * 100
        }
    }

    fun part2(input: List<String>): Int {
        val patterns = mutableListOf<MutableList<List<Char>>>(mutableListOf())
        input.forEach {
            if (it.isEmpty()) {
                patterns.add(mutableListOf())
            } else {
                patterns.last().add(it.toList())
            }
        }
        return patterns.sumOf { pattern ->
            val vSize = pattern[0].size
            var vPoss = List(vSize - 1) { Pair(it, 0) }
            pattern.forEach { line ->
                vPoss = vPoss.mapNotNull { (v, err) ->
                    val errors = line.mapIndexed { index, c ->
                        val ref = 2 * v + 1 - index
                        line.getOrNull(ref)?.let {
                            c == it
                        } ?: true
                    }.count { !it }
                    if (err + errors > 2) {
                        null
                    } else {
                        Pair(v, err + errors)
                    }
                }
            }
            vPoss = vPoss.filter { it.second == 2 }

            if (vPoss.isNotEmpty()) {
                check(vPoss.size == 1)
                return@sumOf vPoss[0].first + 1
            }

            val hSize = pattern.size
            var hPoss = List(hSize - 1) { Pair(it, 0) }
            repeat(vSize) { col ->
                hPoss = hPoss.mapNotNull { (h, err) ->
                    val errors = (0 until hSize).map { index ->
                        val c = pattern[index][col]
                        val ref = 2 * h + 1 - index
                        pattern.getOrNull(ref)?.get(col)?.let {
                            c == it
                        } ?: true
                    }.count { !it }
                    if (err + errors > 2) {
                        null
                    } else {
                        Pair(h, err + errors)
                    }
                }
            }
            hPoss = hPoss.filter { it.second == 2 }
            check(hPoss.size == 1)
            (hPoss[0].first + 1) * 100
        }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part2(testInput) == 281)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
