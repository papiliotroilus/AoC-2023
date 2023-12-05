import java.io.File

fun main() {
    // Initialise variables
    var seedString = ""
    File("input.txt").useLines { seedString = it.first() }

    val seedList: List<Long> = seedString.substring(seedString.indexOf(":") + 2)
        .split(" ")
        .map {it.toLong()}

    val seedMap = mutableMapOf<Long, Long>()
    for (seed in seedList) { seedMap[seed] = seed }

    var seedRangeList = mutableListOf<LongRange>()
    var i = 0
    while (i < seedList.size - 1) {
        seedRangeList.add(seedList[i]..<seedList[i] + seedList[i + 1])
        i += 2
    }

    // Go through category maps
    val rangeMap = mutableMapOf<LongRange, LongRange>()
    File("input.txt").forEachLine {
        if (it.isNotEmpty() && it.first().isDigit()) {
            val categoryList = it.split(" ")
            val destination = categoryList[0].toLong()
            val source = categoryList[1].toLong()
            val length = categoryList[2].toLong()
            rangeMap[source..<source + length] = destination..<destination + length
        } else if (it.isEmpty() && rangeMap.isNotEmpty()) {

            // Part 1
            for (seed in seedMap.keys) {
                for (sourceRange in rangeMap.keys) {
                    if (sourceRange.contains(seedMap[seed])) {
                        seedMap[seed] = seedMap[seed]!! - sourceRange.first + rangeMap[sourceRange]!!.first
                        break
                    }
                }
            }

            // Part 2
            val seedRangeProcessed = mutableListOf<LongRange>()
            while (seedRangeList.isNotEmpty()) {
                val seedRange = seedRangeList.first()
                var broken = false
                for (sourceRange in rangeMap.keys) {
                    // Test if seed range exceeds source range to the left only
                    if (seedRange.first < sourceRange.first && seedRange.last >= sourceRange.first && seedRange.last < sourceRange.last) {
                        seedRangeList.add(seedRange.first..<sourceRange.first)
                        seedRangeProcessed.add(rangeMap[sourceRange]!!.first..(seedRange.last - sourceRange.first + rangeMap[sourceRange]!!.first))
                        broken = true
                        break
                    // Test if seed range exceeds source range to the right only
                    } else if (seedRange.first >= sourceRange.first && seedRange.first < sourceRange.last && seedRange.last > sourceRange.last) {
                        seedRangeProcessed.add((seedRange.first - sourceRange.first + rangeMap[sourceRange]!!.first)..rangeMap[sourceRange]!!.last)
                        seedRangeList.add(sourceRange.last + 1..seedRange.last)
                        broken = true
                        break
                    // Test if seed range is contained by source range
                    } else if (seedRange.first < sourceRange.first && seedRange.last > sourceRange.last) {
                        seedRangeList.add(seedRange.first..<sourceRange.first)
                        seedRangeProcessed.add(rangeMap[sourceRange]!!.first..rangeMap[sourceRange]!!.last)
                        seedRangeList.add(sourceRange.last + 1..seedRange.last)
                        broken = true
                        break
                    // Test if seed range contains source range
                    } else if (seedRange.first >= sourceRange.first && seedRange.last <= sourceRange.last) {
                        seedRangeProcessed.add((seedRange.first - sourceRange.first + rangeMap[sourceRange]!!.first)..(seedRange.last - sourceRange.first + rangeMap[sourceRange]!!.first))
                        broken = true
                        break
                    }
                }
                // If seed range does not intersect source range, continue with seed range as is
                if (!broken) {
                    seedRangeProcessed.add(seedRange)
                }
                seedRangeList.remove(seedRange)
            }
            seedRangeList = seedRangeProcessed
            rangeMap.clear()
        }
    }

    val part1 = seedMap.minBy { it.value }.value
    val part2 = seedRangeList.sortedBy { it.first }.first().first

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 1 is $part2")
}