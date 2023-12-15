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

    // Function for getting a list of column entries
    private fun column(columnNumber:Int): MutableList<Char> {
        val columnList = mutableListOf<Char>()
        for (element in mapContent) {
            columnList.add(element[columnNumber])
        }
        return columnList
    }

    // Function for finding mirror coordinates
    fun mirrorCoords(smudged:Boolean): Int {

        fun checkSmudge(list1:List<Char>, list2:List<Char>): Boolean {
            var smudgeCount = 0
            for (entry in list1.indices) {
                if (list1[entry] != list2[entry]) {
                    if (smudgeCount == 0) {
                        smudgeCount++
                    } else if (smudgeCount == 1) {
                        return false
                    }
                }
            }
            return smudgeCount != 0
        }

        var smudgeFound = false

        seekHorizontal@for (mapRow in 0..<mapContent.size - 1) {
            var rowList1 = mapContent[mapRow].toList()
            var rowList2 = mapContent[mapRow + 1].toList()
            if (smudged) {
                smudgeFound = checkSmudge(rowList1, rowList2)
            }
            if (rowList1 == rowList2 || smudgeFound) {
                val rowsUp = mapRow
                val rowsDown = mapContent.size - mapRow - 2
                if (rowsUp != 0 && rowsDown != 0) {
                    if (rowsUp < rowsDown) {
                        for (reflectionRow in 0..<rowsUp) {
                            rowList1 = mapContent[reflectionRow].toList()
                            rowList2 = mapContent[2 * mapRow + 1 - reflectionRow].toList()
                            if (rowList1 != rowList2) {
                                if (!smudged || smudgeFound) {
                                    continue@seekHorizontal
                                } else {
                                    smudgeFound = true
                                }
                            }
                        }
                    } else if (rowsUp > rowsDown) {
                        for (reflectionRow in mapRow + 2..<mapContent.size) {
                            rowList1 = mapContent[reflectionRow].toList()
                            rowList2 = mapContent[2 * mapRow + 1 - reflectionRow].toList()
                            if (rowList1 != rowList2) {
                                if (!smudged || smudgeFound) {
                                    continue@seekHorizontal
                                } else {
                                    smudgeFound = true
                                }
                            }
                        }
                    }
                }
                if (!smudged || smudgeFound) {
                    return (mapRow + 1) * 100
                }
            }
        }

        smudgeFound = false
        seekVertical@for (mapCol in 0..<mapContent[0].size - 1) {
            var columnList1 = column(mapCol)
            var columnList2 = column(mapCol + 1)
            if (smudged) {
                smudgeFound = checkSmudge(columnList1, columnList2)
            }
            if (columnList1 == columnList2 || smudgeFound) {
                val colsLeft = mapCol
                val colsRight = mapContent[0].size - mapCol - 2
                if (colsLeft > 0 && colsRight > 0) {
                    if (colsLeft < colsRight) {
                        for (reflectionCol in 0..<colsLeft) {
                            columnList1 = column(reflectionCol)
                            columnList2 = column(2 * mapCol + 1 - reflectionCol)
                            if (columnList1 != columnList2) {
                                if (!smudged || smudgeFound) {
                                    continue@seekVertical
                                } else {
                                    smudgeFound = true
                                }
                            }
                        }
                    } else if (colsLeft > colsRight) {
                        for (reflectionCol in mapCol + 2..<mapContent[0].size) {
                            columnList1 = column(reflectionCol)
                            columnList2 = column(2 * mapCol + 1 - reflectionCol)
                            if (columnList1 != columnList2) {
                                if (!smudged || smudgeFound) {
                                    continue@seekVertical
                                } else {
                                    smudgeFound = true
                                }
                            }
                        }
                    }
                }
                if (!smudged || smudgeFound) {
                    return (mapCol + 1)
                }
            }
        }
        return 0
    }
}