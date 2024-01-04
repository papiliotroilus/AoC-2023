import java.io.File

fun main() {
    // Initialise variables
    val islandMap = Map2D()
    File("input.txt").forEachLine { islandMap.chart(it) }
    val startPoint = Point2D(0, 1)
    val endPoint = Point2D(islandMap.rows(), islandMap.columns()  - 1)

    // Helper functions
    fun graph(slippery:Boolean): MutableMap<Pair<Point2D, Point2D>, Int> {
        val digraph = mutableMapOf<Pair<Point2D, Point2D>, Int>()
        val queue = mutableSetOf<Pair<Point2D, Point2D>>()
        val visited = mutableSetOf<Pair<Point2D, Point2D>>()
        queue.add(Pair(startPoint, Point2D(1, 1)))
        while (queue.isNotEmpty()) {
            val currentDirection = queue.first()
            queue.remove(currentDirection)
            val originNode = currentDirection.first
            var previousTile = originNode
            var currentTile = currentDirection.second
            var stepCount = 0
            var endOfPath = false
            while (!endOfPath) {
                stepCount++
                val candidates = when (slippery) {
                    true -> when (islandMap.mapContent[currentTile.lat][currentTile.lon]) {
                        '>' -> mutableListOf(currentTile.right())
                        'v' -> mutableListOf(currentTile.down())
                        '<' -> mutableListOf(currentTile.left())
                        '^' -> mutableListOf(currentTile.up())
                        else -> {
                            currentTile.neighbours()
                                .culled(islandMap.rows(), islandMap.columns())
                                .filter { islandMap.mapContent[it.lat][it.lon] != '#' }
                                .toMutableList()
                        }
                    }
                    false -> currentTile.neighbours()
                        .culled(islandMap.rows(), islandMap.columns())
                        .filter { islandMap.mapContent[it.lat][it.lon] != '#' }
                        .toMutableList()
                }
                candidates.remove(previousTile)
                if (candidates.size == 0) {
                    endOfPath = true
                } else if (candidates.first() == endPoint) {
                    digraph[Pair(originNode, candidates.first())] = stepCount + 1
                    endOfPath = true
                } else if (candidates.size == 1) {
                    previousTile = currentTile
                    currentTile = candidates.first()
                } else {
                    endOfPath = true
                    for (candidate in candidates) {
                        if (!visited.contains(Pair(originNode, candidate))) {
                            digraph[Pair(originNode, currentTile)] = stepCount
                        }
                        if (!visited.contains(Pair(currentTile, candidate))) {
                            queue.add(Pair(currentTile, candidate))
                        }
                    }
                }
            }
            visited.add(currentDirection)
        }
        return digraph
    }

    fun findLongest(graph:MutableMap<Pair<Point2D, Point2D>, Int>): Int {
        var currentLongest = 0
        val currentPath = mutableListOf(startPoint)
        fun delve(currentNode:Point2D, length:Int) {
            val availableDirections = graph.keys.filter { it.first == currentNode }.filter { it.second !in currentPath }
            if (currentNode == endPoint && length > currentLongest) {
                currentLongest = length
            } else if (availableDirections.isNotEmpty()) {
                for (direction in availableDirections) {
                    val nextNode = direction.second
                    currentPath.add(nextNode)
                    val updatedLength = length + graph[direction]!!
                    delve(nextNode, updatedLength)
                }
            }
            currentPath.remove(currentNode)
        }
        delve(startPoint, 0)
        return currentLongest
    }

    // Solve problem
    val part1Map = graph(true)
    val part1 = findLongest(part1Map)

    val part2Map = graph(false)
    val part2 = findLongest(part2Map)

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}