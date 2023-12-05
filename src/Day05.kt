fun main() {
    data class Range(val destinationStart: Long, val sourceStart: Long, val size: Long)

    class Ranges(val source: String, val destination: String) {
        val ranges: MutableList<Range> = mutableListOf()

        fun finish() {
            ranges.sortBy { it.sourceStart }
        }

        fun map(source: Long): Long {
            val range = ranges.firstOrNull { it.sourceStart <= source && it.sourceStart + it.size > source}
            return range?.let { source - it.sourceStart + it.destinationStart } ?: source
        }

        // Range pair is start and count
        fun mapRange(range: Pair<Long, Long>): List<Pair<Long, Long>> {
            var (source, count) = range
            var done = false
            val output = mutableListOf<Pair<Long, Long>>()
            while (!done) {
                assert(count > 0)
                val r = ranges.firstOrNull { it.sourceStart <= source && it.sourceStart + it.size > source }
                if (r != null) {
                    val extent = r.sourceStart + r.size - source
                    val ds = source - r.sourceStart + r.destinationStart
                    if (extent >= count) {
                        done = true
                        output.add(Pair(ds, count))
                    } else {
                        output.add(Pair(ds, extent))
                        source += extent
                        count -= extent
                    }
                } else {
                    // outside of a range - find the start of the next range
                    val nr = ranges.firstOrNull { it.sourceStart > source }
                    if (nr == null) {
                        done = true
                        output.add(Pair(source, count))
                    } else {
                        val extent = nr.sourceStart - source
                        if (extent >= count) {
                            done = true
                            output.add(Pair(source, count))
                        } else {
                            output.add(Pair(source, extent))
                            source += extent
                            count -= extent
                        }
                    }
                }
            }
            return output
        }
    }

    fun part1(input: List<String>): Int {
        val seeds = input[0].split(": ")[1].split(" ").map { it.toLong() }
        val maps = mutableMapOf<String, Ranges>()
        var currentRange: Ranges? = null
        input.drop(2).forEach { line ->
            when (line.split(" ").size) {
                2 -> {
                    assert(currentRange == null)
                    val (source, destination) = line.split(" ")[0].split("-to-")
                    currentRange = Ranges(source, destination)
                }
                3 -> {
                    assert(currentRange != null)
                    val (d, s, c) = line.split(" ").map { it.toLong() }
                    currentRange?.ranges?.add(Range(d, s, c))
                }
                else -> {
                    currentRange?.let { range ->
                        range.finish()
                        maps[range.source] = range
                    }
                    currentRange = null
                }
            }
        }
        currentRange?.let { range ->
            range.finish()
            maps[range.source] = range
        }
        return seeds.minOf{
            var s = "seed"
            var idx = it
            while (s != "location") {
                val ranges = maps[s]!!
                idx = ranges.map(idx)
                s = ranges.destination
            }
            idx
        }.toInt()
    }

    fun part2(input: List<String>): Int {
        val seeds = input[0].split(": ")[1].split(" ").map { it.toLong() }
                .chunked(2) { pair ->
                    Pair(pair[0], pair[1])
                }
        val maps = mutableMapOf<String, Ranges>()
        var currentRange: Ranges? = null
        input.drop(2).forEach { line ->
            when (line.split(" ").size) {
                2 -> {
                    assert(currentRange == null)
                    val (source, destination) = line.split(" ")[0].split("-to-")
                    currentRange = Ranges(source, destination)
                }
                3 -> {
                    assert(currentRange != null)
                    val (d, s, c) = line.split(" ").map { it.toLong() }
                    currentRange?.ranges?.add(Range(d, s, c))
                }
                else -> {
                    currentRange?.let { range ->
                        range.finish()
                        maps[range.source] = range
                    }
                    currentRange = null
                }
            }
        }
        currentRange?.let { range ->
            range.finish()
            maps[range.source] = range
        }
        return seeds.flatMap { pair ->
            var s = "seed"
            var ps = listOf(pair)
            while (s != "location") {
                val ranges = maps[s]!!
                ps = ps.flatMap { ranges.mapRange(it) }
                s = ranges.destination
            }
            ps
        }.minOf { it.first }.toInt()
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part2(testInput) == 281)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
