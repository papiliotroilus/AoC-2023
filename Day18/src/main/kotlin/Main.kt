import java.io.File
import kotlin.math.abs

fun main() {
    // Initialise variables
    var previousPoint = Point2D(0,0)
    var truePrevious = Point2D(0,0)
    val polygon = mutableListOf<Point2D>()
    val truePolygon = mutableListOf<Point2D>()
    var perimeter = 0L
    var truePerimeter = 0L
    File("input.txt").forEachLine {
        fun nextPoint(previousPoint:Point2D, direction:String, length: Long): Point2D {
            return when (direction) {
                "U" -> Point2D(previousPoint.lat - length, previousPoint.lon)
                "D" -> Point2D(previousPoint.lat + length, previousPoint.lon)
                "R" -> Point2D(previousPoint.lat, previousPoint.lon + length)
                else -> Point2D(previousPoint.lat, previousPoint.lon - length)
            }
        }

        val inputList = it.split(" ")
        val direction = inputList[0]
        val length = inputList[1].toLong()
        val currentPoint = nextPoint(previousPoint, direction, length)
        perimeter += length
        polygon.add(currentPoint)
        previousPoint = currentPoint

        val colour = inputList[2].drop(2).dropLast(1)
        val trueLength = colour.take(5).toLong(radix = 16)
        val trueDirection = when (colour.takeLast(1)) {
            "0" -> "R"
            "1" -> "D"
            "2" -> "L"
            else -> "U"
        }
        val truePoint = nextPoint(truePrevious, trueDirection, trueLength)
        truePerimeter += trueLength
        truePolygon.add(truePoint)
        truePrevious = truePoint
    }

    // Solve problem
    fun orthoArea(polygon:List<Point2D>, perimeter: Long):Long {
        // Count half-tiles for straight edges
        val straightAreas = (perimeter - polygon.size) / 2

        // Count 1/4 and 3/4 tiles for convex and concave corners
        val convexEdges = (polygon.size + 4) / 2
        val concaveEdges = (polygon.size - 4) / 2
        val edgeAreas = (convexEdges + concaveEdges * 3) / 4

        // Shoelace theorem for interior of polygon
        var leftLaces = 0L
        var rightLaces = 0L
        for (i in 0..polygon.size - 2) {
            leftLaces += polygon[i].lat * polygon[i + 1].lon
            rightLaces += polygon[i].lon * polygon[i + 1].lat
        }
        val innerArea = abs(rightLaces - leftLaces) / 2
        return perimeter + innerArea - edgeAreas - straightAreas
    }

    val part1 = orthoArea(polygon, perimeter)
    val part2 = orthoArea(truePolygon, truePerimeter)

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}