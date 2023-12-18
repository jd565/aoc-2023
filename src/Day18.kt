fun main() {
    fun List<Point>.area(): Long {
        return this.zipWithNext { a, b ->
            a.x * b.y - a.y * b.x
        }.sum() / 2L
    }

    fun part1(input: List<String>): Long {
        val points = mutableListOf(Point(0, 0))
        var perimeter = 0

        input.forEach { line ->
            val (dir, countStr) = line.split(" ")
            val count = countStr.toInt()
            val d = when (dir) {
                "L" -> Point.West
                "D" -> Point.South
                "R" -> Point.East
                "U" -> Point.North
                else -> throw IllegalStateException(dir)
            }

            val p = points.last() + d * count
            perimeter += count

            points.add(p)
        }
        check(points.last() == points.first())

        val a = points.area()

        // i + b = A + b/2 + 1
        // b = perimeter
        check(perimeter % 2 == 0)

        return a + (perimeter / 2) + 1
    }

    fun part2(input: List<String>): Long {
        val points = mutableListOf(Point(0, 0))
        var perimeter = 0L

        input.forEach { line ->
            val (_, _, h) = line.split(" ")
            val h2 = h.trim('(', ')', '#')
            val dir = h2.last()
            val countStr = h2.dropLast(1)
            val count = countStr.toInt(16)
            val d = when (dir) {
                '2' -> Point.West
                '1' -> Point.South
                '0' -> Point.East
                '3' -> Point.North
                else -> throw IllegalStateException(dir.toString())
            }

            points.add(points.last() + d * count)
            perimeter += count
        }
        check(points.last() == points.first())

        val a = points.area()

        // i + b = A + b/2 + 1
        // b = perimeter
        check(perimeter % 2 == 0L)

        return a + (perimeter / 2) + 1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part1(testInput) == 62L)

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}

data class Point(val x: Long, val y: Long) {
    operator fun plus(o: Point): Point {
        return Point(x + o.x, y + o.y)
    }

    operator fun minus(o: Point): Point {
        return Point(x - o.x, y - o.y)
    }

    operator fun times(i: Int): Point {
        return Point(x * i, y * i)
    }

    fun turn(left: Boolean): Point {
        if (left) return Point(y, -x)
        return Point(-y, x)
    }

    companion object {
        val East = Point(1, 0)
        val West = Point(-1, 0)
        val North = Point(0, -1)
        val South = Point(0, 1)
    }
}