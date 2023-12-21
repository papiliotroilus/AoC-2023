import java.util.PriorityQueue

class Map2D() {
    var mapContent = mutableListOf<MutableList<Int>>()

    // Function for returning the maximum row index of map
    fun rows():Int {
        return mapContent.size - 1
    }

    // Function for returning the maximum column index of map
    fun columns():Int {
        return when(rows()) {
            -1 -> 0
            else -> mapContent.first().size - 1
        }
    }

    // Function for converting strings to map rows
    fun chart(inputLine: String) {
        mapContent.add(inputLine.map { it.toString().toInt() }.toMutableList())
    }

    // Function for printing map
    fun print() {
        for (mapRow in mapContent) {
            val printRow = mapRow.toString()
                .replace("[","")
                .replace("]","")
                .replace(",","")
                .replace(" ","")
            println(printRow)
        }
    }

    // Enum for cardinal directions
    enum class Direction { North, East, South, West }

    // Method for pathfinding from origin to destination using Dijkstra's algorithm
    fun pathFind(origin: Point2D, destination: Point2D, maxDistance: Int, minDistance: Int): Int {

        // Initialise cost to origin map, queue set, visited set, and path to origin map if relevant
        val costToOrigin = HashMap<Triple<Point2D, Direction, Int>, Int>()
        val queue = PriorityQueue<Triple<Point2D, Direction, Int>>(compareBy{costToOrigin[it]})
        val visited = mutableSetOf<Triple<Point2D, Direction, Int>>()
//        val pathToOrigin = mutableMapOf<Triple<Point2D, Direction, Int>, Triple<Point2D, Direction, Int>>() // If path to origin is relevant

        // Create map entries for origin, add to queue
        costToOrigin[Triple(origin, Direction.East, 0)] = 0
        queue.add(Triple(origin, Direction.East, 0))

        // Iterate over queue
        while (queue.isNotEmpty()) {
            // Determine point in queue with the lowest known cost to origin
            val currentPoint = queue.poll()
            visited.add(currentPoint)
            val currentLoss = costToOrigin[currentPoint]
            println("current heat loss is $currentLoss")

            // Iterate over each neighbour

            val neighbours = when (currentPoint.second) {
                Direction.North -> listOf(
                    Triple(currentPoint.first.up(), Direction.North, currentPoint.third + 1),
                    Triple(currentPoint.first.left(), Direction.West, 1),
                    Triple(currentPoint.first.right(), Direction.East, 1))
                Direction.South -> listOf(
                    Triple(currentPoint.first.down(), Direction.South, currentPoint.third + 1),
                    Triple(currentPoint.first.left(), Direction.West, 1),
                    Triple(currentPoint.first.right(), Direction.East, 1))
                Direction.West -> listOf(
                    Triple(currentPoint.first.left(), Direction.West, currentPoint.third + 1),
                    Triple(currentPoint.first.up(), Direction.North, 1),
                    Triple(currentPoint.first.down(), Direction.South, 1))
                else -> listOf(
                    Triple(currentPoint.first.right(), Direction.East, currentPoint.third + 1),
                    Triple(currentPoint.first.up(), Direction.North, 1),
                    Triple(currentPoint.first.down(), Direction.South, 1))
            }
            for (neighbour in neighbours) {
                if (neighbour.first.lat !in 0..rows() || neighbour.first.lon !in 0..columns()) {
                    continue
                }
                if (neighbour.third > maxDistance) {
                    continue
                }
                if (neighbour.second != currentPoint.second && currentPoint.third < minDistance) {
                    continue
                }
                if (!visited.contains(neighbour)) {
                    val alternateRoute = costToOrigin[currentPoint]!! + mapContent[neighbour.first.lat][neighbour.first.lon]
                    if (!costToOrigin.containsKey(neighbour) || alternateRoute < costToOrigin[neighbour]!!) {
                        costToOrigin[neighbour] = alternateRoute
//                        pathToOrigin[neighbour] = currentPoint
                    }
                    if (neighbour.first == destination) {
                        // Find path from origin to destination and print it for debugging
//                        val trace = mutableListOf<Triple<Point2D, Direction, Int>>()
//                        var tracePoint = neighbour
//                        while (tracePoint.first != origin) {
//                            trace.add(tracePoint)
//                            val previousPoint = pathToOrigin[tracePoint]
//                            tracePoint = previousPoint!!
//                        }
//                        val pathToDestination = trace.reversed()
//                        for (pathPoint in pathToDestination) {
//                            mapContent[pathPoint.first.lat][pathPoint.first.lon] = 0
//                        }
//                        print()
                        return costToOrigin[neighbour]!!
                    }
                    queue.add(neighbour)
                }
            }
        }
        return -1
    }
}