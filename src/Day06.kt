import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    fun part1(input: List<String>): Int {
        val times = input[0].split("\\s+".toRegex()).drop(1).map { it.toInt() }
        val distances = input[1].split("\\s+".toRegex()).drop(1).map { it.toInt() }
        return times.zip(distances).map { (time, distance) ->
            // totalDistance = chargeTime * (time - chargeTime)
            // (-1)c2 * ct - d = 0
            // (-b +- sqrt(b2 - 4ac)) / 2a
            val det = (time * time - 4 * distance).toFloat()
            assert(det > 0)
            val largeAnswer = (time + sqrt(det)) / 2
            val smallAnswer = (time - sqrt(det)) / 2
            val l = if (floor(largeAnswer) == largeAnswer) {
                floor(largeAnswer - 1)
            } else {
                floor(largeAnswer)
            }
            val s = if (ceil(smallAnswer) == smallAnswer) {
                ceil(smallAnswer + 1)
            } else {
                ceil(smallAnswer)
            }
            l - s + 1
        }
                .onEach { println(it) }
                .reduce { acc, fl -> acc * fl }
                .toInt()
    }

    fun part2(input: List<String>): Int {
        val time = input[0].split("\\s+".toRegex()).drop(1).joinToString(separator = "").toLong()
        val distance = input[1].split("\\s+".toRegex()).drop(1).joinToString(separator = "").toLong()
        // totalDistance = chargeTime * (time - chargeTime)
        // (-1)c2 * ct - d = 0
        // (-b +- sqrt(b2 - 4ac)) / 2a
        val det = (time * time - 4 * distance).toFloat()
        assert(det > 0)
        val largeAnswer = (time + sqrt(det)) / 2
        val smallAnswer = (time - sqrt(det)) / 2
        val l = if (floor(largeAnswer) == largeAnswer) {
            floor(largeAnswer - 1)
        } else {
            floor(largeAnswer)
        }
        val s = if (ceil(smallAnswer) == smallAnswer) {
            ceil(smallAnswer + 1)
        } else {
            ceil(smallAnswer)
        }
        return (l - s + 1).toInt()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
