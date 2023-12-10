import java.io.File

fun main() {
    // Initialise variables
    var rowCount = 0
    File("input.txt").forEachLine { rowCount += 1 }
    var colCount = 0
    File("input.txt").useLines { colCount = it.first().length }
    val pipeMap = Map2D(rowCount, colCount)
    var chartTally = 0
    File("input.txt").forEachLine { pipeMap.chart(chartTally, it); chartTally++ }

    // Solve problem
    val loop = pipeMap.getLoop()
    val part1 = loop.size / 2
    val part2 = pipeMap.pointsInsideLoop(loop).size

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}