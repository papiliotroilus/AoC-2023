import java.io.File

fun main() {

    // Build locations map
    var directions = charArrayOf()
    val locationsMap = linkedMapOf<String, Pair<String, String>>()

    File("input.txt").forEachLine {
        if (it.isNotEmpty()) {
            if (it.toCharArray().last() != ')') {
                directions = it.toCharArray()
            } else {
                val currentLocation = it.substring(0, it.indexOf(' '))
                val options = it.substring(it.indexOf('(') + 1, it.indexOf(')')).split(", ")
                locationsMap[currentLocation] = Pair(options[0], options[1])
            }
        }
    }

    // Solve part 1
    var part1 = 0
    var directionsIndex = 0
    var currentLocation = "AAA"
    while (currentLocation != "ZZZ") {
        part1++
        currentLocation = when (directions[directionsIndex]) {
            'L' -> locationsMap[currentLocation]!!.first
            else -> locationsMap[currentLocation]!!.second
        }
        if (directionsIndex == directions.size - 1) {
            directionsIndex = 0
        } else {
            directionsIndex++
        }
    }

    // Solve part 2
    directionsIndex = 0
    val startLocationsList = locationsMap.keys.filter {it.toCharArray()[2] == 'A'}.toMutableList()
    val finalLocationsList = locationsMap.keys.filter {it.toCharArray()[2] == 'Z'}.toMutableList()
    val loopLengths = mutableMapOf<String, Long>()
    for (startLocation in startLocationsList) {
        var currentGhostLocation = startLocation
        var loopLength = 0
        while (currentGhostLocation !in finalLocationsList) {
            currentGhostLocation = when (directions[directionsIndex]) {
                'L' -> locationsMap[currentGhostLocation]!!.first
                else -> locationsMap[currentGhostLocation]!!.second
            }
            if (directionsIndex == directions.size - 1) {
                directionsIndex = 0
            } else {
                directionsIndex++
            }
            loopLength++
        }
        loopLengths[startLocation] = loopLength.toLong()
    }

    fun findLCM(a: Long, b: Long): Long {
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

    fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1..<numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }

    val part2 = findLCMOfListOfNumbers(loopLengths.values.toList())

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}