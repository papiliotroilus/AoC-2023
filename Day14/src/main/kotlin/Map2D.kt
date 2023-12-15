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

    // Function for getting a list of column entries
    private fun column(columnNumber:Int): MutableList<Char> {
        var columnList = mutableListOf<Char>()
        for (rowNumber in 0..rows()) {
            columnList.add(mapContent[rowNumber][columnNumber])
        }
        return columnList
    }

    // Function for tilting map
    fun tilt(direction:String) {
        when (direction) {
            "North" -> for (colNumber in 0..columns()) {
                for (rowNumber in 1..rows()) {
                    if (mapContent[rowNumber][colNumber] == 'O') {
                        val lastStatic = column(colNumber).take(rowNumber).indexOfLast { it == 'O' || it == '#' }
                        val emptySpaces = IntRange(lastStatic + 1, rowNumber - 1).toList()
                        if (emptySpaces.isNotEmpty()) {
                            mapContent[emptySpaces.first()][colNumber] = 'O'
                            mapContent[rowNumber][colNumber] = '.'
                        }
                    }
                }
            }
            "West" -> for (rowNumber in 0..rows()) {
                for (colNumber in 1..columns()) {
                    if (mapContent[rowNumber][colNumber] == 'O') {
                        val lastStatic = mapContent[rowNumber].take(colNumber).indexOfLast { it == 'O' || it == '#' }
                        val emptySpaces = IntRange(lastStatic + 1, colNumber - 1).toList()
                        if (emptySpaces.isNotEmpty()) {
                            mapContent[rowNumber][emptySpaces.first()] = 'O'
                            mapContent[rowNumber][colNumber] = '.'
                        }
                    }
                }
            }
            "South" -> for (colNumber in 0..columns()) {
                for (rowNumber in rows() - 1 downTo 0) {
                    if (mapContent[rowNumber][colNumber] == 'O') {
                        val firstStatic = (column(colNumber).reversed().dropLast(rowNumber + 1)
                            .indexOfLast { it == 'O' || it == '#' } - rows()) * -1
                        val emptySpaces = IntRange(rowNumber + 1, firstStatic - 1).toList()
                        if (emptySpaces.isNotEmpty()) {
                            mapContent[emptySpaces.last()][colNumber] = 'O'
                            mapContent[rowNumber][colNumber] = '.'
                        }
                    }
                }
            }
            "East" -> for (rowNumber in 0..rows()) {
                for (colNumber in columns() - 1 downTo 0) {
                    if (mapContent[rowNumber][colNumber] == 'O') {
                        val firstStatic = (mapContent[rowNumber].reversed().dropLast(colNumber + 1)
                            .indexOfLast { it == 'O' || it == '#' } - columns()) * -1
                        val emptySpaces = IntRange(colNumber + 1, firstStatic - 1).toList()
                        if (emptySpaces.isNotEmpty()) {
                            mapContent[rowNumber][emptySpaces.last()] = 'O'
                            mapContent[rowNumber][colNumber] = '.'
                        }
                    }
                }
            }
        }
    }

    // Function for executing spin cycle
    fun spinCycle() {
        tilt("North")
        tilt("West")
        tilt("South")
        tilt("East")
    }

    // Function for determining total load of map
    fun totalLoad(): Int {
        var load = 0
        for (colNumber in 0..columns()) {
            for (rowNumber in 0..rows()) {
                if (mapContent[rowNumber][colNumber] == 'O') {
                    load += rows() - rowNumber + 1
                }
            }
        }
        return load
    }
}