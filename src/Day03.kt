fun main() {
    class PartNumber(var number: Int, var included: Boolean)

    fun part1(input: List<String>): Int {
        val parts = mutableMapOf<Pair<Int, Int>, PartNumber>()

        input.forEachIndexed { y, line ->
            var part: PartNumber? = null
            line.forEachIndexed { x, char ->
                if (char.isDigit()) {
                    if (part != null) {
                        part!!.number = part!!.number * 10 + char.digitToInt()
                    } else {
                        part = PartNumber(char.digitToInt(), false)
                    }
                    parts[Pair(x, y)] = part!!
                } else {
                    part = null
                }
            }
        }

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (!char.isDigit() && char != '.') {
                    parts[Pair(x-1, y-1)]?.included = true
                    parts[Pair(x-1, y)]?.included = true
                    parts[Pair(x-1, y+1)]?.included = true
                    parts[Pair(x, y-1)]?.included = true
                    parts[Pair(x, y+1)]?.included = true
                    parts[Pair(x+1, y-1)]?.included = true
                    parts[Pair(x+1, y)]?.included = true
                    parts[Pair(x+1, y+1)]?.included = true
                }
            }
        }

        return parts.values.toSet().sumOf {
            if (it.included) it.number else 0
        }
    }

    fun part2(input: List<String>): Int {
        val parts = mutableMapOf<Pair<Int, Int>, PartNumber>()

        input.forEachIndexed { y, line ->
            var part: PartNumber? = null
            line.forEachIndexed { x, char ->
                if (char.isDigit()) {
                    if (part != null) {
                        part!!.number = part!!.number * 10 + char.digitToInt()
                    } else {
                        part = PartNumber(char.digitToInt(), false)
                    }
                    parts[Pair(x, y)] = part!!
                } else {
                    part = null
                }
            }
        }

        var gearSum = 0

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == '*') {
                    val linkedParts = mutableSetOf<PartNumber>()
                    listOf(Pair(x-1, y-1), Pair(x-1, y), Pair(x-1, y+1), Pair(x, y-1), Pair(x, y+1), Pair(x+1, y-1), Pair(x+1, y), Pair(x+1, y+1)).forEach { location ->
                        val part = parts[location]
                        if (part != null) {
                            linkedParts.add(part)
                        }
                    }
                    if (linkedParts.size == 2) {
                        gearSum += linkedParts.fold(1) { acc, partNumber -> acc * partNumber.number }
                    }
                }
            }
        }

        return gearSum
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day03_test")
//    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
