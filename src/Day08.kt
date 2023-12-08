fun main() {
    class Node(
            val name: String,
            val l: String,
            val r: String,
    ) {
        operator fun get(c: Char): String {
            return if (c == 'L') l else r
        }

        var fullSequence: String = ""
        var fullSequenceLength: Int = 0
        var zIndexes: List<Int> = emptyList()
    }

    fun Map<String, Node>.runSequence(start: Node, sequence: String): List<String> {
        var current = start.name
        val visited = mutableListOf<String>()
        sequence.forEach {
            current = this[current]!![it]
            visited += current
        }
        return visited
    }

    fun part1(input: List<String>): Int {
        val steps = input[0]
        val map = input.drop(2).associate { line ->
            val parts = line.split(" = ")
            val (l, r) = parts[1].split(", ").map { it.trim('(', ')') }
            parts[0] to Node(parts[0], l, r)
        }

        map.forEach { (k, v) ->
            val s = map.runSequence(v, steps)
            if ("ZZZ" in s) {
                v.zIndexes = listOf(s.indexOf("ZZZ") + 1)
                v.fullSequence = "ZZZ"
                v.fullSequenceLength = v.zIndexes[0]
            } else {
                v.fullSequence = s.last()
                v.fullSequenceLength = s.size
            }
        }

        var current = "AAA"
        var numSteps = 0
        val visited = mutableListOf(current)
        while (current != "ZZZ") {
            numSteps += map[current]!!.fullSequenceLength
            current = map[current]!!.fullSequence
            if (current in visited) {
                println("Already visited $current")
            }
        }
        return numSteps
    }

    fun Map<String, Node>.generateZIndexes(start: String): Sequence<Int> {
        var current = this[start]!!
        var numSteps = 0
        var idx = 0
        return generateSequence {
            while (current.zIndexes.size <= idx) {
                idx = 0
                numSteps += current.fullSequenceLength
                current = this[current.fullSequence]!!
            }
            val ret = current.zIndexes[idx] + numSteps
            idx++
            ret
        }
    }

    fun findLcm(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun List<Long>.lcm(): Long {
        return reduce { a, b -> findLcm(a, b) }
    }

    fun part2(input: List<String>): Long {
        val steps = input[0]
        val map = input.drop(2).associate { line ->
            val parts = line.split(" = ")
            val (l, r) = parts[1].split(", ").map { it.trim('(', ')') }
            parts[0] to Node(parts[0], l, r)
        }

        map.forEach { (k, v) ->
            val seq = map.runSequence(v, steps)
            v.zIndexes = seq.mapIndexedNotNull { index, s -> if (s.endsWith("Z")) index + 1 else null }
            v.fullSequence = seq.last()
            v.fullSequenceLength = seq.size
        }

        return map.keys.filter { it.endsWith("A") }
                .map { map.generateZIndexes(it).first().toLong() }
                .lcm()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part2(testInput) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
