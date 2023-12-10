import java.io.File

fun extrapolate(sequence:List<Int>): Pair<Int,Int> {
    val derivatives = mutableListOf<Int>()
    for (i in 0..sequence.size - 2) {
        derivatives.add(sequence[i + 1] - sequence [i])
    }
    return if (derivatives.all { it == 0 }) {
        Pair(sequence.first(), sequence.first())
    } else {
        val ends = extrapolate(derivatives)
        Pair(sequence.first() - ends.first, sequence.last() + ends.second)
    }
}

fun main() {
    // Initialise variables
    var part1 = 0
    var part2 = 0

    // Solve Problem
    File("input.txt").forEachLine {inputString ->
        val sequence = inputString.split(" ").map {it.toInt()}
        val ends = extrapolate(sequence)
        part1 += ends.second
        part2 += ends.first
    }

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}