class Map2D() {
    var mapContent = mutableListOf<MutableList<Char>>()

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
        mapContent.add(inputLine.toCharArray().toMutableList())
    }

    // Naive function for exploring map with BFS for given number of steps
    fun naive(steps:Int): Int {
        val startLat = mapContent.indexOfFirst { it.contains('S') }
        val startLon = mapContent[startLat].indexOf('S')
        val startPoint = Point4D(startLat, startLon, 0, 0)
        var currentLayer = mutableSetOf<Point4D>()
        var previousLayer = mutableSetOf<Point4D>()
        val alreadyExplored = mutableSetOf<Point4D>()
        val finalLayer = mutableSetOf<Point4D>()
        currentLayer.add(startPoint)
        if (steps.mod(2) == 0) {
            finalLayer.add(startPoint)
        }
        for (currentDistance in 1..steps) {
            val nextLayer = mutableSetOf<Point4D>()
            for (point in currentLayer) {
                val neighbours = point.neighbours().wrapped(rows(), columns())
                for (neighbour in neighbours) {
                    if (neighbour !in currentLayer && neighbour!in previousLayer && mapContent[neighbour.lat][neighbour.lon] != '#') {
                        nextLayer.add(neighbour)
                        alreadyExplored.add(neighbour)
                    }
                }
            }
            previousLayer = currentLayer
            currentLayer = nextLayer
            if ((steps.mod(2) == 0) == (currentDistance.mod(2) == 0)) {
                finalLayer.addAll(currentLayer)
            }
        }
        return finalLayer.count()
    }

    // Calculus function for extrapolating explored tiles
    fun calculus(steps:Long): Long {
        fun extrapolate(sequence:List<Long>): Long {
            val derivatives = mutableListOf<Long>()
            for (i in 0..sequence.size - 2) {
                derivatives.add(sequence[i + 1] - sequence [i])
            }
            return if (derivatives.all { it == 0L }) {
                sequence.first()
            } else {
                val end = extrapolate(derivatives)
                sequence.last() + end
            }
        }

        val mapSize = mapContent.size
        val loops = steps / mapSize
        val extra = steps.mod(mapSize)
        val extrapolationList = mutableListOf<Long>()
        for (loop in 0..2) {
            val currentStep = extra + mapSize * loop
            val plots = naive(currentStep).toLong()
            extrapolationList.add(plots)
        }
        var plots = 0L
        for (loop in 3..loops) {
            plots = extrapolate(extrapolationList)
            extrapolationList.add(plots)
            extrapolationList.removeFirst()
        }
        return plots
    }
}