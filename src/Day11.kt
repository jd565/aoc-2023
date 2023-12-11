import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
//        val main = input.mapTo(mutableListOf()) { line ->
//            line.toMutableList()
//        }
//        val idxs = main.mapIndexedNotNull { index, chars -> if (chars.all { it == '.' }) index else null }
//        idxs.reversed().forEach { idx -> main.add(idx, List(main[0].size) { '.' }.toMutableList()) }
//        val xIdxs = (0 until main[0].size).mapNotNull { x ->
//            if (main.map { it[x] }.all { it == '.' }) x else null
//        }
//        xIdxs.reversed().forEach { idx -> main.forEach { it.add(idx, '.') } }
//
//        val galaxies = mutableListOf<Pair<Int, Int>>()
//        main.forEachIndexed { y, chars ->
//            chars.forEachIndexed { x, c ->
//                if (c == '#') {
//                    galaxies.add(Pair(x, y))
//                }
//            }
//        }
//        var sum = 0
//        galaxies.forEachIndexed { index, (x, y) ->
//            galaxies.drop(index + 1).forEach { (gx, gy) ->
//                sum += abs(gx - x) + abs(gy - y)
//            }
//        }
//        return sum
        val main = input.mapTo(mutableListOf()) { line ->
            line.toMutableList()
        }
        val idxs = main.mapIndexedNotNull { index, chars -> if (chars.all { it == '.' }) index else null }
        val xIdxs = (0 until main[0].size).mapNotNull { x ->
            if (main.map { it[x] }.all { it == '.' }) x else null
        }

        val galaxies = mutableListOf<Pair<Int, Int>>()
        main.forEachIndexed { y, chars ->
            chars.forEachIndexed { x, c ->
                if (c == '#') {
                    galaxies.add(Pair(x, y))
                }
            }
        }
        var sum = 0
        galaxies.forEachIndexed { index, (x, y) ->
            galaxies.drop(index + 1).forEach { (gx, gy) ->
                val yAdj = idxs.indexOfFirst { it > y }.takeIf { it >= 0 } ?: idxs.size
                val gyAdj = idxs.indexOfFirst { it > gy }.takeIf { it >= 0 } ?: idxs.size
                val xAdj = xIdxs.indexOfFirst { it > x }.takeIf { it >= 0 } ?: xIdxs.size
                val gxAdj = xIdxs.indexOfFirst { it > gx }.takeIf { it >= 0 } ?: xIdxs.size
                val extra = abs(xAdj - gxAdj) + abs(yAdj - gyAdj)
                sum += abs(gx - x) + abs(gy - y) + extra
            }
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val main = input.mapTo(mutableListOf()) { line ->
            line.toMutableList()
        }
        val idxs = main.mapIndexedNotNull { index, chars -> if (chars.all { it == '.' }) index else null }
        val xIdxs = (0 until main[0].size).mapNotNull { x ->
            if (main.map { it[x] }.all { it == '.' }) x else null
        }

        val galaxies = mutableListOf<Pair<Int, Int>>()
        main.forEachIndexed { y, chars ->
            chars.forEachIndexed { x, c ->
                if (c == '#') {
                    galaxies.add(Pair(x, y))
                }
            }
        }
        var sum = 0L
        galaxies.forEachIndexed { index, (x, y) ->
            galaxies.drop(index + 1).forEach { (gx, gy) ->
                val yAdj = idxs.indexOfFirst { it > y }.takeIf { it >= 0 } ?: idxs.size
                val gyAdj = idxs.indexOfFirst { it > gy }.takeIf { it >= 0 } ?: idxs.size
                val xAdj = xIdxs.indexOfFirst { it > x }.takeIf { it >= 0 } ?: xIdxs.size
                val gxAdj = xIdxs.indexOfFirst { it > gx }.takeIf { it >= 0 } ?: xIdxs.size
                val extra = abs(xAdj - gxAdj) + abs(yAdj - gyAdj)
                sum += abs(gx - x) + abs(gy - y) + extra * 999_999L
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part2(testInput) == 281)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
