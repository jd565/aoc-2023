fun main() {
    data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
        fun sum() = x + m + a + s
    }

    fun part(line: String): Part {
        val (x, m, a, s) = line.trim('{', '}').split(",").map { it.split("=")[1].toInt() }
        return Part(x, m, a, s)
    }

    data class Workflow(
            val name: String,
            val rule: (Part) -> String
    )

    fun workflow(line: String): Workflow {
        val (name, rulesStr) = line.trim('}').split("{")
        val rules = rulesStr.split(",")
        return Workflow(name) { part ->
            rules.forEach { rule ->
                val arg = rule.split(":")
                if (arg.size == 1) {
                    return@Workflow rule
                }

                val (a, out) = arg
                val count = a.drop(2).toInt()
                val c = rule[0]
                val op = rule[1]
                val pc = when (c) {
                    'x' -> part.x
                    'm' -> part.m
                    'a' -> part.a
                    's' -> part.s
                    else -> throw IllegalStateException("Bad c $c")
                }
                when (op) {
                    '<' -> if (pc < count) return@Workflow out
                    '>' -> if (pc > count) return@Workflow out
                    else -> throw IllegalStateException("Bad op $op")
                }
            }
            throw IllegalStateException("Did not exit part")
        }
    }

    fun part1(input: List<String>): Int {
        val workflows = mutableMapOf<String, Workflow>()
        val parts = mutableListOf<Part>()
        input.forEach { line ->
            if (line.isNotEmpty()) {
                if (line.startsWith('{')) {
                    parts.add(part(line))
                } else {
                    val w = workflow(line)
                    workflows[w.name] = w
                }
            }
        }
        return parts.sumOf { p ->
            var n = "in"
            while (n != "A" && n != "R") {
                n = workflows[n]!!.rule(p)
            }
            if (n == "A") {
                p.sum()
            } else {
                0
            }
        }
    }

    data class PossiblePart(
            val mx: Int,
            val bx: Int,
            val mm: Int,
            val bm: Int,
            val ma: Int,
            val ba: Int,
            val ms: Int,
            val bs: Int,
    )

    data class State(
            val pp: PossiblePart,
            val wn: String,
            val idx: Int,
    )

    fun part2(input: List<String>): Long {
        val workflows = mutableMapOf<String, List<String>>()
        input.forEach { line ->
            if (line.isNotEmpty()) {
                if (!line.startsWith('{')) {
                    val (name, rulesStr) = line.trim('}').split("{")
                    val rules = rulesStr.split(",")
                    workflows[name] = rules
                }
            }
        }

        val startPossible = PossiblePart(1, 4000, 1, 4000, 1, 4000, 1, 4000)
        val acceptedParts = mutableListOf<PossiblePart>()
        val currentStates = ArrayDeque<State>()
        currentStates.add(State(startPossible, "in", 0))

        while (currentStates.isNotEmpty()) {
            val s = currentStates.removeFirst()
            val w = workflows[s.wn]!!
            val rule = w[s.idx]

            val arg = rule.split(":")
            if (arg.size == 1) {
                if (rule == "A") {
                    acceptedParts.add(s.pp)
                } else if (rule != "R") {
                    currentStates.add(State(s.pp, rule, 0))
                }
                continue
            }

            val (a, out) = arg
            val count = a.drop(2).toInt()
            val c = rule[0]
            val op = rule[1]
            val pp = s.pp
            when (c) {
                'x' -> {
                    when (op) {
                        '<' -> if (pp.mx < count && pp.bx < count) {
                            if (out == "A") {
                                acceptedParts.add(pp)
                            } else if (out != "R") {
                                currentStates.add(State(s.pp, out, 0))
                            }
                        } else if (pp.mx >= count && pp.bx >= count) {
                            currentStates.add(State(s.pp, s.wn, s.idx + 1))
                        } else {
                            val ppLower = s.pp.copy(bx = count - 1)
                            if (out == "A") {
                                acceptedParts.add(ppLower)
                            } else if (out != "R") {
                                currentStates.add(State(ppLower, out, 0))
                            }
                            val ppUpper = s.pp.copy(mx = count)
                            currentStates.add(State(ppUpper, s.wn, s.idx + 1))
                        }
                        '>' -> if (pp.mx > count && pp.bx > count) {
                            if (out == "A") {
                                acceptedParts.add(pp)
                            } else if (out != "R") {
                                currentStates.add(State(s.pp, out, 0))
                            }
                        } else if (pp.mx <= count && pp.bx <= count) {
                            currentStates.add(State(s.pp, s.wn, s.idx + 1))
                        } else {
                            val ppLower = s.pp.copy(bx = count)
                            val ppUpper = s.pp.copy(mx = count + 1)
                            if (out == "A") {
                                acceptedParts.add(ppUpper)
                            } else if (out != "R") {
                                currentStates.add(State(ppUpper, out, 0))
                            }
                            currentStates.add(State(ppLower, s.wn, s.idx + 1))
                        }
                        else -> throw IllegalStateException("Bad op $op")
                    }
                }
                'm' -> {
                    when (op) {
                        '<' -> if (pp.mm < count && pp.bm < count) {
                            if (out == "A") {
                                acceptedParts.add(pp)
                            } else if (out != "R") {
                                currentStates.add(State(s.pp, out, 0))
                            }
                        } else if (pp.mm >= count && pp.bm >= count) {
                            currentStates.add(State(s.pp, s.wn, s.idx + 1))
                        } else {
                            val ppLower = s.pp.copy(bm = count - 1)
                            if (out == "A") {
                                acceptedParts.add(ppLower)
                            } else if (out != "R") {
                                currentStates.add(State(ppLower, out, 0))
                            }
                            val ppUpper = s.pp.copy(mm = count)
                            currentStates.add(State(ppUpper, s.wn, s.idx + 1))
                        }
                        '>' -> if (pp.mm > count && pp.bm > count) {
                            if (out == "A") {
                                acceptedParts.add(pp)
                            } else if (out != "R") {
                                currentStates.add(State(s.pp, out, 0))
                            }
                        } else if (pp.mm <= count && pp.bm <= count) {
                            currentStates.add(State(s.pp, s.wn, s.idx + 1))
                        } else {
                            val ppLower = s.pp.copy(bm = count)
                            val ppUpper = s.pp.copy(mm = count + 1)
                            if (out == "A") {
                                acceptedParts.add(ppUpper)
                            } else if (out != "R") {
                                currentStates.add(State(ppUpper, out, 0))
                            }
                            currentStates.add(State(ppLower, s.wn, s.idx + 1))
                        }
                        else -> throw IllegalStateException("Bad op $op")
                    }
                }
                'a' -> {
                    when (op) {
                        '<' -> if (pp.ma < count && pp.ba < count) {
                            if (out == "A") {
                                acceptedParts.add(pp)
                            } else if (out != "R") {
                                currentStates.add(State(s.pp, out, 0))
                            }
                        } else if (pp.ma >= count && pp.ba >= count) {
                            currentStates.add(State(s.pp, s.wn, s.idx + 1))
                        } else {
                            val ppLower = s.pp.copy(ba = count - 1)
                            if (out == "A") {
                                acceptedParts.add(ppLower)
                            } else if (out != "R") {
                                currentStates.add(State(ppLower, out, 0))
                            }
                            val ppUpper = s.pp.copy(ma = count)
                            currentStates.add(State(ppUpper, s.wn, s.idx + 1))
                        }
                        '>' -> if (pp.ma > count && pp.ba > count) {
                            if (out == "A") {
                                acceptedParts.add(pp)
                            } else if (out != "R") {
                                currentStates.add(State(s.pp, out, 0))
                            }
                        } else if (pp.ma <= count && pp.ba <= count) {
                            currentStates.add(State(s.pp, s.wn, s.idx + 1))
                        } else {
                            val ppLower = s.pp.copy(ba = count)
                            val ppUpper = s.pp.copy(ma = count + 1)
                            if (out == "A") {
                                acceptedParts.add(ppUpper)
                            } else if (out != "R") {
                                currentStates.add(State(ppUpper, out, 0))
                            }
                            currentStates.add(State(ppLower, s.wn, s.idx + 1))
                        }
                        else -> throw IllegalStateException("Bad op $op")
                    }
                }
                's' -> {
                    when (op) {
                        '<' -> if (pp.ms < count && pp.bs < count) {
                            if (out == "A") {
                                acceptedParts.add(pp)
                            } else if (out != "R") {
                                currentStates.add(State(s.pp, out, 0))
                            }
                        } else if (pp.ms >= count && pp.bs >= count) {
                            currentStates.add(State(s.pp, s.wn, s.idx + 1))
                        } else {
                            val ppLower = s.pp.copy(bs = count - 1)
                            if (out == "A") {
                                acceptedParts.add(ppLower)
                            } else if (out != "R") {
                                currentStates.add(State(ppLower, out, 0))
                            }
                            val ppUpper = s.pp.copy(ms = count)
                            currentStates.add(State(ppUpper, s.wn, s.idx + 1))
                        }
                        '>' -> if (pp.ms > count && pp.bs > count) {
                            if (out == "A") {
                                acceptedParts.add(pp)
                            } else if (out != "R") {
                                currentStates.add(State(s.pp, out, 0))
                            }
                        } else if (pp.ms <= count && pp.bs <= count) {
                            currentStates.add(State(s.pp, s.wn, s.idx + 1))
                        } else {
                            val ppLower = s.pp.copy(bs = count)
                            val ppUpper = s.pp.copy(ms = count + 1)
                            if (out == "A") {
                                acceptedParts.add(ppUpper)
                            } else if (out != "R") {
                                currentStates.add(State(ppUpper, out, 0))
                            }
                            currentStates.add(State(ppLower, s.wn, s.idx + 1))
                        }
                        else -> throw IllegalStateException("Bad op $op")
                    }
                }
                else -> throw IllegalStateException("Bad c $c")
            }
        }

        return acceptedParts.sumOf {
            (it.bx - it.mx + 1L) * (it.bm - it.mm + 1L) * (it.ba - it.ma + 1L) * (it.bs - it.ms + 1L)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
//    println(part2(testInput))
//    check(part2(testInput) == 167409079868000L)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
