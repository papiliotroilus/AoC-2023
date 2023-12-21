import java.io.File

fun main() {
    // Initialise variables
    val heatMap = Map2D()
    File("input.txt").forEachLine { heatMap.chart(it) }

    // Solve problem
    val part1 = heatMap.pathFind(Point2D(0, 0), Point2D(heatMap.rows(), heatMap.columns()), 3, 0)
    val part2 = heatMap.pathFind(Point2D(0, 0), Point2D(heatMap.rows(), heatMap.columns()), 10, 4)

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}