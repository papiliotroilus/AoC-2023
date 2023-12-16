class Map2D() {
    private var mapContent = mutableListOf<MutableList<Char>>()

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

    // Function for getting a list of column entries
    private fun column(columnNumber:Int): MutableList<Char> {
        val columnList = mutableListOf<Char>()
        for (rowNumber in 0..rows()) {
            columnList.add(mapContent[rowNumber][columnNumber])
        }
        return columnList
    }

    // Function for converting strings to map rows
    fun chart(inputLine: String) {
        mapContent.add(inputLine.toCharArray().toMutableList())
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

    enum class Direction {Up, Down, Left, Right}

    // Function for simulating path of beam starting from arguments, returning number of energised tiles
    fun countEnergised(startTile:Point2D, startIncomingDirection:Direction): Int {
        val currentBeam = mutableSetOf<Pair<Point2D, Direction>>()

        fun beam(currentTile: Point2D, incomingDirection: Direction) {
            if (!currentTile.check(rows(), columns())) {
                return
            }
            if (currentBeam.any{it == Pair(currentTile, incomingDirection)}) {
                return
            }
            currentBeam.add(Pair(currentTile, incomingDirection))
            when(mapContent[currentTile.lat][currentTile.lon]) {
                '/' -> when(incomingDirection) {
                    Direction.Left -> beam(currentTile.up(), Direction.Down)
                    Direction.Up -> beam(currentTile.left(), Direction.Right)
                    Direction.Right -> beam(currentTile.down(), Direction.Up)
                    Direction.Down -> beam(currentTile.right(), Direction.Left)
                }
                '\\' -> when(incomingDirection) {
                    Direction.Left -> beam(currentTile.down(), Direction.Up)
                    Direction.Down -> beam(currentTile.left(), Direction.Right)
                    Direction.Up -> beam(currentTile.right(), Direction.Left)
                    Direction.Right -> beam(currentTile.up(), Direction.Down)
                }
                '-' -> when(incomingDirection) {
                    Direction.Left -> beam(currentTile.right(), Direction.Left)
                    Direction.Right -> beam(currentTile.left(), Direction.Right)
                    Direction.Up, Direction.Down -> { beam(currentTile.left(), Direction.Right); beam(currentTile.right(), Direction.Left) }
                }
                '|' -> when(incomingDirection) {
                    Direction.Up -> beam(currentTile.down(), Direction.Up)
                    Direction.Down -> beam(currentTile.up(), Direction.Down)
                    Direction.Left, Direction.Right -> { beam(currentTile.up(), Direction.Down); beam(currentTile.down(), Direction.Up) }
                }
                '.' -> when(incomingDirection) {
                    Direction.Right -> beam(currentTile.left(), Direction.Right)
                    Direction.Left -> beam(currentTile.right(), Direction.Left)
                    Direction.Up -> beam(currentTile.down(), Direction.Up)
                    Direction.Down -> beam(currentTile.up(), Direction.Down)
                }
            }
        }
        beam(startTile, startIncomingDirection)
        val uniqueTiles = mutableSetOf<Point2D>()
        for (tile in currentBeam) {
            uniqueTiles.add(tile.first)
        }
        return uniqueTiles.count()
    }
}