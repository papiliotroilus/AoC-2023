import kotlin.collections.LinkedHashMap

class Map2D(rows: Int, columns: Int) {
    private var mapContent = arrayOf<CharArray>()
    init {
        mapContent = Array(rows) { CharArray(columns) }
    }

    // Method for converting strings to map lines
    fun chart(rowNumber: Int, inputLine: String) {
        // Split line string to char array
        val inputChars = inputLine.toCharArray()
        // Add point coordinates to map
        System.arraycopy(inputChars, 0, mapContent[rowNumber], 0, inputChars.size)
    }

    // Method for printing map
    fun print() {
        for (mapRow in mapContent) {
            println(mapRow)
        }
    }

    // Method for parsing map to find engine parts
    fun findParts(): Pair<Int, Int> {
        val currentNumberCoords = mutableListOf<Point2D>()
        val currentNumberValues = mutableListOf<Char>()
        var partSum = 0
        var ratioSum = 0
        val gearMap = LinkedHashMap<Point2D, Int>()
        var x = 0
        var y = 0

        while (x < mapContent.size) {
            while (y < mapContent[x].size) {
                if (mapContent[x][y].isDigit()) {
                    currentNumberCoords.add(Point2D(x, y))
                    currentNumberValues.add(mapContent[x][y])
                } else {
                    if (currentNumberCoords.isNotEmpty()) {
                        val neighbours = mutableSetOf<Point2D>()
                        for (digit in currentNumberCoords) {
                            neighbours.addAll(digit.neighbours().culled(mapContent.size - 1, mapContent[0].size - 1))
                        }
                        for (neighbour in neighbours) {
                            if (mapContent[neighbour.lat][neighbour.lon] != '.' && !mapContent[neighbour.lat][neighbour.lon].isDigit()) {
                                partSum += String(currentNumberValues.toCharArray()).toInt()
                                if (mapContent[neighbour.lat][neighbour.lon] == '*') {
                                    if (gearMap.containsKey(neighbour)) {
                                        ratioSum += (gearMap[neighbour]?.times(String(currentNumberValues.toCharArray()).toInt())!!)
                                        gearMap.remove(neighbour)
                                    } else {
                                        gearMap[neighbour] = String(currentNumberValues.toCharArray()).toInt()
                                    }
                                }
                                break
                            }
                        }
                        currentNumberCoords.clear()
                        currentNumberValues.clear()
                    }
                }
                y++
            }
            y = 0
            x++
        }
        return Pair(partSum, ratioSum)
    }
}