import java.io.File
import kotlin.math.pow

fun main() {
    // Initialise variables
    var part1 = 0
    var part2 = 0
    val cardMap = mutableMapOf<Int, Int>()
    var cardCounter = 0

    // Solve problem
    File("input.txt").forEachLine {
        cardCounter ++
        val cardNumbers = it.substring(it.indexOf(':') + 1).trim().split("  ", " ")
        val pickedNumbers = cardNumbers.take(cardNumbers.indexOf("|")).toSet()
        val winningNumbers = cardNumbers.drop(cardNumbers.indexOf("|") + 1).toSet()

        // Part 1
        val score = pickedNumbers.intersect(winningNumbers).count()
        part1 += 2.toDouble().pow(score - 1.toDouble()).toInt()

        // Part 2
        if (!cardMap.containsKey(cardCounter)) { cardMap[cardCounter] = 1 }
        part2 += cardMap[cardCounter]!!
        for (i in 1..score) {
            if (!cardMap.containsKey(cardCounter + i)) { cardMap[cardCounter + i] = 1 }
            cardMap[cardCounter + i] = cardMap[cardCounter + i]!! + cardMap[cardCounter]!!
        }
    }

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}