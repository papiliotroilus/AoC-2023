import java.io.File

fun main() {
    // Initialise variables
    val lightMap = Map2D()
    File("input.txt").forEachLine { lightMap.chart(it) }

    // Solve problem
    val part1 = lightMap.countEnergised(Point2D(0,0), Map2D.Direction.Left)

    var part2 = 0
    val startingPoints = mutableListOf<Pair<Point2D, Map2D.Direction>>()
    for (leftTile in 0..lightMap.rows()) {
        startingPoints.add(Pair(Point2D(leftTile, 0), Map2D.Direction.Left))
    }
    for (rightTile in 0..lightMap.rows()) {
        startingPoints.add(Pair(Point2D(rightTile, lightMap.columns()), Map2D.Direction.Right))
    }
    for (upTile in 0..lightMap.columns()) {
        startingPoints.add(Pair(Point2D(0, upTile), Map2D.Direction.Up))
    }
    for (downTile in 0..lightMap.columns()) {
        startingPoints.add(Pair(Point2D(lightMap.rows(), downTile), Map2D.Direction.Down))
    }
    for (startingPoint in startingPoints) {
        val currentEnergised = lightMap.countEnergised(startingPoint.first, startingPoint.second)
        val currentIndex = startingPoints.indexOf(startingPoint)
        val totalIndex = startingPoints.size - 1
        println("finished $currentIndex of $totalIndex")
        if (currentEnergised > part2) {
            part2 = currentEnergised
        }
    }

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}