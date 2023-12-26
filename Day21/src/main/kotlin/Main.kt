import java.io.File

fun main() {
    // Initialise variables
    val gardenMap = Map2D()
    File("input.txt").forEachLine { gardenMap.chart(it) }

    // Solve problem
    val part1 = gardenMap.naive(64)
    val part2 = gardenMap.calculus(26501365)

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}