import java.awt.Color.red

fun main() {
    fun part1(input: List<String>): Int {
        val maxRed = 12
        val maxGreen = 13
        val maxBlue = 14
        return input.sumOf { line ->
            val (gameId, gamesString) = line.substring(5).split(":")
            val games = gamesString.trim().split("; ")
            val validGame = games.all { gameString ->
                gameString.split(", ").all { cubeCount ->
                    val (count, color) = cubeCount.split(" ")
                    val max = when (color) {
                        "red" -> maxRed
                        "green" -> maxGreen
                        "blue" -> maxBlue
                        else -> throw IllegalArgumentException("Bad color $color")
                    }
                    count.toInt() <= max
                }
            }
            if (!validGame) 0 else gameId.toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            var red = 0
            var green = 0
            var blue = 0
            val (gameId, gamesString) = line.substring(5).split(":")
            gamesString.trim().split("; ").forEach { gameString ->
                gameString.split(", ").forEach { cubeCount ->
                    val (count, color) = cubeCount.split(" ")
                    val c = count.toInt()
                    when (color) {
                        "red" -> if (c > red) red = c
                        "green" -> if (c > green) green = c
                        "blue" -> if (c > blue) blue = c
                        else -> throw IllegalArgumentException("Bad color $color")
                    }
                }
            }
            red * green * blue
        }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part2(testInput) == 281)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
