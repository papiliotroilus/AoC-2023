import java.io.File

fun main() {
    // Initialise variables
    var rowCount = 0
    File("input.txt").forEachLine { rowCount += 1 }
    var colCount = 0
    File("input.txt").useLines { colCount = it.first().length }
    val starMap = Map2D(rowCount, colCount)
    var chartTally = 0
    File("input.txt").forEachLine { starMap.chart(chartTally, it); chartTally++ }

    // Solve problem
    val part1 = starMap.galaxyDistances(2)
    val part2 = starMap.galaxyDistances(1000000)

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}