import kotlin.math.abs

class Map2D(rows: Int, columns: Int) {
    private var mapContent = arrayOf<CharArray>()
    init {
        mapContent = Array(rows) { CharArray(columns) }
    }

    // Function for converting strings to map lines
    fun chart(rowNumber: Int, inputLine: String) {
        val inputChars = inputLine.toCharArray()
        System.arraycopy(inputChars, 0, mapContent[rowNumber], 0, inputChars.size)
    }

    // Function for printing map
    fun print() {
        for (mapRow in mapContent) {
            println(mapRow)
        }
    }

    // Function for building galaxy list
    fun galaxyDistances(multiplier:Int): Long {
        // Horizontal expansion
        val extraRows = mutableListOf<Int>()
        for (row in mapContent.indices) {
            if (mapContent[row].all{ it == '.' }) {
                extraRows.add(row)
            }
        }

        // Vertical expansion
        val extraCols = mutableListOf<Int>()
        vertical@for (col in mapContent[0].indices) {
            var columnList = mutableListOf<Point2D>()
            for (row in mapContent.indices) {
                if (mapContent[row][col] == '#') {
                    continue@vertical
                } else {
                    continue
                }
            }
            extraCols.add(col)
        }

        // Build list of galaxies then set of pairs of galaxies
        var galaxyList = mutableListOf<Point2D>()
        for (row in mapContent.indices) {
            for (col in mapContent[0].indices) {
                if (mapContent[row][col] == '#') {
                    galaxyList.add(Point2D(row, col))
                }
            }
        }

        var galaxyPairs = mutableSetOf<Set<Point2D>>()
        for (galaxy1 in galaxyList) {
            for (galaxy2 in galaxyList) {
                if (galaxy1 != galaxy2) {
                    val pair = setOf(galaxy1, galaxy2)
                    galaxyPairs.add(pair)
                }
            }
        }

        // Add up Manhattan distances between members of all pairs, plus the extra rows and columns
        var distances = 0L
        for (pair in galaxyPairs) {
            var pairList = pair.toList()
            pairList = pairList.sortedBy { it.lat }
            val latModifier = extraRows.count { it in pairList[0].lat..pairList[1].lat } * (multiplier - 1)
            pairList = pairList.sortedBy { it.lon }
            val lonModifier = extraCols.count { it in pairList[0].lon..pairList[1].lon } * (multiplier - 1)
            distances += abs(pairList[0].lat - pairList[1].lat) + latModifier + abs(pairList[0].lon - pairList[1].lon) + lonModifier
        }

        return distances
    }
}