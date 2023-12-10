class Map2D(rows: Int, columns: Int) {
    private var mapContent = arrayOf<CharArray>()
    init {
        mapContent = Array(rows) { CharArray(columns) }
    }

    // Function for converting strings to map lines
    fun chart(rowNumber: Int, inputLine: String) {
        // Split line string to char array
        val inputChars = inputLine.toCharArray()
        // Add point coordinates to map
        System.arraycopy(inputChars, 0, mapContent[rowNumber], 0, inputChars.size)
    }

    // Function for printing map
    fun print() {
        for (mapRow in mapContent) {
            println(mapRow)
        }
    }

    // Function for determining if two points can connect
    private fun canConnect(currentPoint: Point2D, nextPoint: Point2D): Boolean {
        // Up case
        if (nextPoint.lat == currentPoint.lat - 1) {
            return when(mapContent[currentPoint.lat][currentPoint.lon]) {
                '|', 'L', 'J', 'S' -> when(mapContent[nextPoint.lat][nextPoint.lon]) {
                    '|', '7', 'F', 'S' -> true
                    else -> false
                }
                else -> false
            }
            // Down case
        } else if (nextPoint.lat == currentPoint.lat + 1) {
            return when(mapContent[currentPoint.lat][currentPoint.lon]) {
                '|', '7', 'F', 'S' -> when(mapContent[nextPoint.lat][nextPoint.lon]) {
                    '|', 'L', 'J', 'S' -> true
                    else -> false
                }
                else -> false
            }
            // Left case
        } else if (nextPoint.lon == currentPoint.lon - 1) {
            return when(mapContent[currentPoint.lat][currentPoint.lon]) {
                '-', 'J', '7', 'S' -> when(mapContent[nextPoint.lat][nextPoint.lon]) {
                    '-', 'L', 'F', 'S' -> true
                    else -> false
                }
                else -> false
            }
            // Right case
        } else if (nextPoint.lon == currentPoint.lon + 1) {
            return when(mapContent[currentPoint.lat][currentPoint.lon]) {
                '-', 'L', 'F', 'S' -> when(mapContent[nextPoint.lat][nextPoint.lon]) {
                    '-', 'J', '7', 'S' -> true
                    else -> false
                }
                else -> false
            }
        } else { return false }
    }

    // Function for getting list of viable connections for a given point
    private fun connections(currentPoint: Point2D): MutableList<Point2D> {
        return currentPoint.neighbours()
            .culled(mapContent.size, mapContent[0].size)
            .filter {canConnect(currentPoint, it) }
            .toMutableList()
    }

    // Function for parsing map to determine the length and area of the loop
    fun getLoop(): List<Point2D> {
        // Find loop start
        var x = 0
        var y = 0
        while (x < mapContent.size) {
            if (mapContent[x].contains('S')) {
                y = mapContent[x].indexOf('S')
                break
            } else {
                x++
            }
        }

        // Initialise loop with the starting point and one of its connections
        val startingPoint = Point2D(x, y)
        val loop = mutableListOf(startingPoint, connections(startingPoint).last())

        // Iterate through loop until closed
        while (loop.first() != loop.last()) {
            val currentPoint = loop.last()
            val previousPoint = loop[loop.size - 2]
            val loopCandidates = connections(currentPoint)
            loopCandidates.remove(previousPoint)
            loop.add(loopCandidates.last())
        }

        // Trim loop then replace starting point with its true character
        loop.removeAt(loop.size - 1)
        val firstPoint = loop[1]
        val lastPoint = loop [loop.size - 1]
        for (replacement in listOf('|', '-', 'L', 'J', 'F', '7')) {
            mapContent[startingPoint.lat][startingPoint.lon] = replacement
            if (canConnect(startingPoint, firstPoint) && canConnect(lastPoint, startingPoint)) {
                break
            }
        }

        return loop.toList()
    }

    // Function for determining number of points within polygon determined by loop
    fun pointsInsideLoop(loop:List<Point2D>): MutableList<Point2D> {
        // Determine limits of loop
        val minLat = loop.minOf {it.lat}
        val maxLat = loop.maxOf {it.lat}
        val minLon = loop.minOf {it.lon}
        val maxLon = loop.maxOf {it.lon}

        // Ray cast through square created by limits to find points within the loop polygon
        var currentLat = minLat
        var currentLon = minLon
        var insideLoop = false
        var loopContents = mutableListOf<Point2D>()
        var currentPoint = Point2D(minLat, minLon)
        while (currentLat <= maxLat) {
            var lineDirection = mutableListOf<String>()
            while (currentLon <= maxLon) {
                currentPoint = Point2D(currentLat, currentLon)
                if (currentPoint in loop) {
                    if (mapContent[currentLat][currentLon] == '|') {
                        insideLoop = !insideLoop
                    } else if (mapContent[currentLat][currentLon] in listOf('F', '7')) {
                        lineDirection.add("Down")
                    } else if (mapContent[currentLat][currentLon] in listOf('J', 'L')) {
                        lineDirection.add("Up")
                    }
                    if (lineDirection.size == 2) {
                        if (lineDirection.first() != lineDirection.last()) {
                            insideLoop = !insideLoop
                        }
                        lineDirection.clear()
                    }
                }
                if (currentPoint !in loop && insideLoop) {
                    loopContents.add(currentPoint)
                }
                currentLon++
            }
            currentLon = minLon
            currentLat++
            insideLoop = false
        }

        return loopContents
    }
}