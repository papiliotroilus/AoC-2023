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
}