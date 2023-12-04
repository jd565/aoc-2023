import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {line ->
            val (winning, actual) = line.split(":")[1].trim().split("|").map { part ->
                part.trim().split("\\s+".toRegex()).map {
                    it.trim().toInt()
                }
            }
            val size = winning.intersect(actual.toSet()).size
            if (size == 0) {
                0
            } else {
                2f.pow(size - 1).toInt()
            }
        }
    }

    fun part2(input: List<String>): Int {
        val copies = List(input.size) { 1 }.toMutableList()
        input.forEachIndexed { index, line ->
            val (winning, actual) = line.split(":")[1].trim().split("|").map { part ->
                part.trim().split("\\s+".toRegex()).map {
                    it.trim().toInt()
                }
            }
            val size = winning.intersect(actual.toSet()).size
            val thisCopies = copies[index]
            repeat(size) { idxAdj ->
                val newIdx = index + idxAdj + 1
                copies[newIdx] = copies[newIdx] + thisCopies
            }
        }
        return copies.sum()
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part2(testInput) == 281)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
