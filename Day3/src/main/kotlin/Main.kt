import java.io.File

fun main() {
    // Initialise variables
    var rowCount = 0
    File("input.txt").forEachLine { rowCount += 1 }
    var colCount = 0
    File("input.txt").useLines { colCount = it.first().length }
    val engineMap = Map2D(colCount, rowCount)
    var chartTally = 0
    File("input.txt").forEachLine { engineMap.chart(chartTally, it); chartTally++ }

    // Solve problem
    val solutions = engineMap.findParts()

    // Print output
    println("The solution to part 1 is ${solutions.first}")
    println("The solution to part 2 is ${solutions.second}")
}