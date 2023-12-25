import java.io.File

fun main() {
    // Helper classes and functions
    data class Part(val x:Int, val m:Int, val a:Int, val s:Int)
    class PartRange(val x:IntRange, val m:IntRange, val a:IntRange, val s:IntRange) {
        fun substitute(category:Char, newRange:IntRange): PartRange {
            return when (category) {
                'x' -> PartRange(newRange, m, a, s)
                'm' -> PartRange(x, newRange, a, s)
                'a' -> PartRange(x, m, newRange, s)
                else -> PartRange(x, m, a,newRange)
            }
        }
    }

    class Rule(private val variable: Char, private val comparator: Char, private val value: Int, private val result: String) {
        fun evaluate(part:Part): String {
            val partVariable = when (variable) {
                'x' -> part.x
                'm' -> part.m
                'a' -> part.a
                else -> part.s
            }
            if ((comparator == '<' && partVariable < value) || (comparator == '>' && partVariable > value)) {
                return result
            }
            return "continue"
        }

        fun evaluateRange(partRange:PartRange): Pair<Pair<PartRange?, String>, PartRange?> {
            val partVariable = when (variable) {
                'x' -> partRange.x
                'm' -> partRange.m
                'a' -> partRange.a
                else -> partRange.s
            }
            // Passes entirely
            if ((comparator == '<' && partVariable.last < value) || (comparator == '>' && partVariable.first > value)) {
                return(Pair(Pair(partRange, result), null))
            // Fails entirely
            } else if ((comparator == '<' && partVariable.first > value) || (comparator == '>' && partVariable.last < value )) {
                return(Pair(Pair(null, result), partRange))
            // Passes partially
            } else {
                var passRange = -1..-1
                var failRange = -1..-1
                when(comparator) {
                    '<' -> { passRange = partVariable.first..<value; failRange = value..partVariable.last }
                    '>' -> { failRange = partVariable.first..value; passRange = value + 1..partVariable.last}
                }
                val failParts = partRange.substitute(variable, failRange)
                val passParts = partRange.substitute(variable, passRange)
                return(Pair(Pair(passParts, result), failParts))
            }
        }
    }

    class Workflow(private val rules:MutableList<Rule>, private val finalDestination: String) {
        fun evaluate(part:Part): String {
            for (rule in rules) {
                return when(val verdict = rule.evaluate(part)) {
                    "continue" -> continue
                    "R" -> "R"
                    "A" -> "A"
                    else -> verdict
                }
            }
            return finalDestination
        }

        fun evaluateRange(partRange:PartRange): MutableList<Pair<PartRange, String>> {
            var currentRange = partRange
            val outboundParts = mutableListOf<Pair<PartRange, String>>()
            for (rule in rules) {
                val verdict = rule.evaluateRange(currentRange)
                val passParts = verdict.first.first
                val passDestination = verdict.first.second
                val failParts = verdict.second
                if (passParts != null) {
                    outboundParts.add(Pair(passParts, passDestination))
                }
                if (failParts != null) {
                    currentRange = failParts
                }
            }
            outboundParts.add(Pair(currentRange, finalDestination))
            return outboundParts
        }
    }

    val workflows = mutableMapOf<String, Workflow>()
    val unprocessedParts = mutableListOf<Pair<Part, String>>()


    // Initialise workflows and parts
    var secondHalf = false
    File("input.txt").forEachLine { inputString ->
        if (inputString.isEmpty()) {
            secondHalf = true
        } else if (!secondHalf) {
            val workflowName = inputString.substring(0, inputString.indexOf('{'))
            val ruleStrings = inputString.substring(inputString.indexOf('{') + 1, inputString.indexOf('}')).split(',')
            val ruleList = mutableListOf<Rule>()
            var finalDestination = "R"
            for (rule in ruleStrings) {
                if (rule.contains(':')) {
                    val variable = rule.toCharArray().first()
                    var comparator = '>'
                    if (rule.contains('<')) {
                        comparator = '<'
                    }
                    val value = rule.substring(rule.indexOf(comparator) + 1, rule.indexOf(':')).toInt()
                    val result = rule.substring(rule.indexOf(':') + 1)
                    ruleList.add(Rule(variable, comparator, value, result))
                } else {
                    finalDestination = rule
                }
            }
            workflows[workflowName] = Workflow(ruleList, finalDestination)
        } else {
            val partVariables = inputString.drop(1).dropLast(1)
                .split(',')
                .map {it.substring(it.indexOf('=') + 1).toInt()}
            unprocessedParts.add(Pair(Part(partVariables[0], partVariables[1], partVariables[2], partVariables[3]), "in"))
        }
    }

    // Solve part 1
    val acceptedParts = mutableSetOf<Part>()
    while (unprocessedParts.isNotEmpty()) {
        val currentPart = unprocessedParts.first()
        unprocessedParts.remove(currentPart)
        when (val verdict = workflows[currentPart.second]!!.evaluate(currentPart.first)) {
            "A" -> acceptedParts.add(currentPart.first)
            "R" -> continue
            else -> unprocessedParts.add(Pair(currentPart.first, verdict))
        }
    }
    var part1 = 0
    for (part in acceptedParts) {
        part1 += part.x + part.m + part.a + part.s
    }

    // Solve part 2
    val unprocessedRanges = mutableListOf<Pair<PartRange, String>>()
    val acceptedRanges = mutableListOf<PartRange>()
    unprocessedRanges.add(Pair(PartRange(1..4000, 1..4000, 1..4000, 1..4000), "in"))
    while (unprocessedRanges.isNotEmpty()) {
        val currentRange = unprocessedRanges.first()
        unprocessedRanges.remove(currentRange)
        val verdict = workflows[currentRange.second]!!.evaluateRange(currentRange.first)
        for (range in verdict) {
            when (val destination = range.second) {
                "A" -> acceptedRanges.add(range.first)
                "R" -> continue
                else -> unprocessedRanges.add(range)
            }
        }
    }
    var part2 = 0L
    for (range in acceptedRanges) {
        val unpackedRange:Long = range.x.count().toLong() * range.m.count() * range.a.count() * range.s.count()
        part2 += unpackedRange
    }

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}