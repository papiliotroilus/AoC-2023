import java.io.File

fun main() {
    // Initialise variables
    val redQuota = 12; val greenQuota = 13; val blueQuota = 14
    var part1 = 0; var part2 = 0

    // Solve both parts
    fun playGame(input: String): Array<Int> {
        val inputString = input.substring(input.indexOf(":") + 2)
        val gameNumber = input.substring(5, input.indexOf(":")).toInt()

        var quotaExceeded = false
        var redMax = 0 ; var greenMax = 0; var blueMax = 0
        val solutions = arrayOf(0, 0)

        val draws = inputString.split("; ")
        for (draw in draws) {
            val colourGroups = draw.split(", ")
            for (colourGroup in colourGroups) {
                val quantity = colourGroup.substring(0, colourGroup.indexOf(" ")).toInt()
                val colour = colourGroup.substring(colourGroup.indexOf(" ") + 1)
                var comparator = 0
                when (colour) {
                    "red" -> {
                        if (quantity > redMax) {redMax = quantity}
                        comparator = redQuota
                    }
                    "green" -> {
                        if (quantity > greenMax) {greenMax = quantity}
                        comparator = greenQuota
                    }
                    "blue" -> {
                        if (quantity > blueMax) {blueMax = quantity}
                        comparator = blueQuota
                    }
                }
                if (quantity > comparator) {
                    quotaExceeded = true
                }
            }
        }

        if (!quotaExceeded) {
            solutions[0] = gameNumber
        }
        val gamePower = redMax * greenMax * blueMax
        solutions[1] = gamePower
        return solutions
    }

    File("input.txt").forEachLine {
        val lineSolutions = playGame(it)
        part1 += lineSolutions[0]
        part2 += lineSolutions[1]
    }
    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}