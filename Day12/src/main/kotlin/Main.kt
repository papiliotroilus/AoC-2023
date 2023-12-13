import java.io.File

fun main() {
    // Initialise variables
    var part1 = 0L
    var part2 = 0L

    // Solve problem
    File("input.txt").forEachLine { inputString ->
        // Part 1
        var springString = inputString.substring(0,inputString.indexOf(' '))
        var groups = inputString.substring(inputString.indexOf(' ') + 1).split(',').map { it.toInt() }.toMutableList()
        val cache = mutableMapOf<List<Int>, Long>()

        fun evaluateString(stringIndex:Int, currentGroup:Int, groupProgress: Int): Long {
            if (cache.containsKey(listOf(stringIndex, currentGroup, groupProgress))) {
                return cache[listOf(stringIndex, currentGroup, groupProgress)]!!
            }
            val springSubstring = springString.substring(stringIndex + 1)
            var remaining = 0
            if (currentGroup == 0) {
                remaining = groups.sum()
            } else if (currentGroup != groups.size + 1) {
                for (groupIndex in currentGroup - 1..<groups.size) {
                    remaining += groups[groupIndex]
                }
                remaining -= groupProgress
            }

            // Successful path
            if (remaining == 0 && (stringIndex == springString.length - 1 || springSubstring.all { it == '.' || it == '?' })) {
                cache[listOf(stringIndex, currentGroup, groupProgress)] = 1
                return 1
            }

            // Cull doomed paths early
            // Too many damaged springs left
            if (springSubstring.count{ it == '#' } > remaining) {
                cache[listOf(stringIndex, currentGroup, groupProgress)] = 0
                return 0
            }
            // Not enough possible damaged springs left
            if (springSubstring.count{ it == '#' || it == '?' } < remaining) {
                cache[listOf(stringIndex, currentGroup, groupProgress)] = 0
                return 0
            }

            // Evaluate next char
            // Damaged spring case
            if (springString[stringIndex + 1] == '#') {
                if (currentGroup == 0) {
                    // Starting first group
                    return evaluateString(stringIndex + 1, 1, 1)
                } else if (groupProgress == groups[currentGroup - 1]) {
                    // Current group too large
                    cache[listOf(stringIndex, currentGroup, groupProgress)] = 0
                    return 0
                } else {
                    // Continuing existing group
                    return evaluateString(stringIndex + 1, currentGroup, groupProgress + 1)
                }
            // Operational spring case
            } else if (springString[stringIndex + 1] == '.') {
                if (groupProgress == 0) {
                    // Skipping space between groups
                    return evaluateString(stringIndex + 1, currentGroup, groupProgress)
                } else if (currentGroup != 0 && groupProgress < groups[currentGroup - 1]) {
                    // Current group too small
                    cache[listOf(stringIndex, currentGroup, groupProgress)] = 0
                    return 0
                } else if (groupProgress == groups[currentGroup - 1]) {
                    // Wrapping up current group
                    return evaluateString(stringIndex + 1, currentGroup + 1, 0)
                }
            // Unknown spring case
            } else {
                var validBranches = 0L
                if (currentGroup == 0) {
                    // Evaluate ? as # starting a new group
                    validBranches += evaluateString(stringIndex + 1, 1, 1)
                } else if (groupProgress != groups[currentGroup - 1]) {
                    // Evaluate ? as # continuing current group
                    validBranches += evaluateString(stringIndex + 1, currentGroup, groupProgress + 1)
                } else if (groupProgress == groups[currentGroup - 1]) {
                    // Evaluate ? as . wrapping up current group
                    validBranches += evaluateString(stringIndex + 1, currentGroup + 1, 0)
                }
                if (groupProgress == 0) {
                    // Evaluate ? as . skipping over space between groups
                    validBranches += evaluateString(stringIndex + 1, currentGroup, groupProgress)
                }
                cache[listOf(stringIndex, currentGroup, groupProgress)] = validBranches
                return validBranches
            }
            return 0
        }

        var firstChar = springString[0]
        if (firstChar == '#' || firstChar == '?') {
            part1 += evaluateString(0, 1, 1)
        }
        if (firstChar == '.' || firstChar == '?') {
            part1 += evaluateString(0, 0, 0)
        }

        // Part 2
        cache.clear()
        val trueSpringString = "$springString?$springString?$springString?$springString?$springString"
        springString = trueSpringString
        val trueGroups = mutableListOf<Int>()
        for (i in 1..5) {
            trueGroups.addAll(groups)
        }
        groups = trueGroups
        firstChar = springString[0]
        if (firstChar == '#' || firstChar == '?') {
            part2 += evaluateString(0, 1, 1)
        }
        if (firstChar == '.' || firstChar == '?') {
            part2 += evaluateString(0, 0, 0)
        }
    }

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}