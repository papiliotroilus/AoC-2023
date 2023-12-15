import java.io.File

fun main() {
    // Initialise variables
    val mapList = mutableListOf<Map2D>()
    val stringList = mutableListOf<String>()
    var writing = true
    var rowCount = 0
    File("input.txt").forEachLine {
        if (it.isNotBlank()) {
            stringList.add(it)
            rowCount++
        } else {
            writing = false
        }
        if (!writing) {
            val colCount = stringList[0].length
            mapList.add(Map2D(rowCount, colCount))
            for ((chartTally, line) in stringList.withIndex()) {
                mapList.last().chart(chartTally, line)
            }
            rowCount = 0
            stringList.clear()
            writing = true
        }
    }

    // Solve problem
    var part1 = 0
    for (map in mapList) {
        val mapNo = mapList.indexOf(map)
        part1 += map.mirrorCoords(false)
    }

    var part2 = 0
    for (map in mapList) {
        val mapNo = mapList.indexOf(map)
        part2 += map.mirrorCoords(true)
    }

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}