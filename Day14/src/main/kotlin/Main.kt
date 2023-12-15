import java.io.File

fun main() {
    // Initialise variables
    val rockMap = Map2D()
    File("input.txt").forEachLine { rockMap.chart(it) }

    // Solve part 1
    rockMap.tilt("North")
    val part1 = rockMap.totalLoad()

    // Solve part 2
    val cycleMap = mutableMapOf<Int, MutableList<MutableList<Char>>>()
    rockMap.tilt("West")
    rockMap.tilt("South")
    rockMap.tilt("East")
    cycleMap[1] = rockMap.mapContent.map{ it.toMutableList()}.toMutableList()
    var cycleStart = 0
    var cycleEnd = 0
    for (i in 2..<1000000000) {
        rockMap.spinCycle()
        val currentMap = rockMap.mapContent.map{ it.toMutableList()}.toMutableList()
        if (cycleMap.containsValue(currentMap)) {
            cycleStart = cycleMap.filterValues { it == currentMap }.keys.first()
            cycleEnd = i
            break
        } else {
            cycleMap[i] = currentMap
        }
    }
    val cycleLength = cycleEnd - cycleStart
    rockMap.mapContent = cycleMap[cycleStart + ((1000000000 - cycleStart).mod(cycleLength))]!!
    val part2 = rockMap.totalLoad()

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}