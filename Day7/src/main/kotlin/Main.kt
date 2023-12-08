import java.io.File

fun main() {
    // Function for translating card type to ordered alphanumeric, with joker rule parameter
    fun cardToStrength(cardType:Char, jokers:Boolean): Char {
        return when (cardType){
            'T' -> 'A'
            'J' -> {if (jokers) {'1'} else {'B'} }
            'Q' -> 'C'
            'K' -> 'D'
            'A' -> 'E'
            else -> cardType
        }
    }

    // Function for determining the total strength of a hand from map of card counts
    fun calculateTotalStrength(cardCounts: Map<Char, Int>): Int {
        return when (cardCounts.count()){
            1 -> 6
            2 -> { if (cardCounts.maxBy{it.value}.value == 4) { 5 } else { 4 } }
            3 -> { if (cardCounts.maxBy{it.value}.value == 3) { 3 } else { 2 } }
            4 -> 1
            else -> 0
        }
    }

    // Class for containing individual and total card strengths for both parts, and the bid
    data class Hand(
        val cards:String,
        val strengths:String,
        val trueStrengths:String,
        val totalStrength:Int,
        val trueTotalStrength:Int,
        val bid:Int)

    // Function for ranking hands and determining total score, with joker rule parameter
    fun rankHands(handsList:List<Hand>, jokers:Boolean): Int {
        val sortedHandsList:List<Hand> = if (jokers) {
            handsList.sortedWith(compareBy({it.trueTotalStrength}, {it.trueStrengths}))
        } else {
            handsList.sortedWith(compareBy({it.totalStrength}, {it.strengths}))
        }
        var winnings = 0
        var rank = 0
        for (hand in sortedHandsList) {
            rank++
            winnings += hand.bid * rank
        }
        return winnings
    }

    // Build hands list
    val handsList = mutableListOf<Hand>()
    File("input.txt").forEachLine { inputString ->
        val cards = inputString.substring(0, inputString.indexOf(" ")).trim()
        val bid = inputString.substring(inputString.indexOf(" ") + 1).toInt()

        // Determine the individual card strengths
        val cardStrengths = cards.toCharArray().map{ cardToStrength(it, false) }
        val trueCardStrengths = cards.toCharArray().map{ cardToStrength(it, true) }

        // Group cards by type and map type to its count
        val cardCounts = cardStrengths.toList().groupingBy { it }.eachCount().toMap()
        val bestCardCounts = trueCardStrengths.toList().groupingBy { it }.eachCount().toMutableMap()

        // Reassign wildcards for part 2
        if (bestCardCounts.containsKey('1')) {
            val wildCardCount = bestCardCounts['1']
            bestCardCounts.remove('1')
            if (wildCardCount == 5) {
                bestCardCounts['E'] = 5
            } else {
                bestCardCounts[bestCardCounts.maxBy{it.value}.key] = bestCardCounts[bestCardCounts.maxBy{it.value}.key]!! + wildCardCount!!
            }
        }

        val totalStrength = calculateTotalStrength(cardCounts)
        val trueTotalStrength = calculateTotalStrength(bestCardCounts)

        handsList.add(Hand(cards, cardStrengths.toString(), trueCardStrengths.toString(), totalStrength, trueTotalStrength, bid))
    }

    // Solve the problem
    val part1 = rankHands(handsList, false)
    val part2 = rankHands(handsList, true)

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 1 is $part2")
}