import java.io.File

fun main() {
    // Read input
    val inputList = File("input.txt") .readLines()
        .map { row -> row.substring(row.indexOf(':') + 1).trim().split("\\s+".toRegex()).map { it.toInt() }}
    val timeList = inputList[0]
    val trueTime = timeList.joinToString("") { it.toString() }.toLong()
    val distanceList = inputList[1]
    val trueDistance = distanceList.joinToString("") { it.toString() }.toLong()

    // Solve part 1
    var part1 = 1
    for (gameNumber in timeList.indices) {
        var winTally = 0
        val timeLimit = timeList[gameNumber]
        val recordDistance = distanceList[gameNumber]
        for (chargeTime in 1..<timeLimit) {
            val distance = (timeLimit - chargeTime) * chargeTime
            if (distance > recordDistance) { winTally += 1 }
        }
        part1 *= winTally
    }

    // Solve part 2
    var part2 = 0
    for (chargeTime in 1..<trueTime) {
        val distance = (trueTime - chargeTime) * chargeTime
        if (distance > trueDistance) { part2 += 1 }
    }

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 1 is $part2")
}