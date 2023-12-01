import java.io.File

fun main() {
    // Initialise variables
    var part1sum = 0
    var part2sum = 0

    // Solve part 1
    File("input.txt").forEachLine { inputString ->
        val inputDigits = inputString.filter { it.isDigit() }
        part1sum += (inputDigits.substring(0, 1) + inputDigits.substring(inputDigits.length - 1)).toInt()
    }

    // Solve part 2
    File("input.txt").forEachLine { inputString ->
        val inputParsed = inputString.replace("one","o1e")
            .replace("two","t2o")
            .replace("three","t3e")
            .replace("four","4")
            .replace("five","5e")
            .replace("six","6")
            .replace("seven","7n")
            .replace("eight","e8t")
            .replace("nine","9e")
            .replace("zero","0o")
        val inputDigits = inputParsed.filter { it.isDigit() }
        part2sum += (inputDigits.substring(0, 1) + inputDigits.substring(inputDigits.length - 1)).toInt()
    }

    // Print output
    println("The solution to part 1 is $part1sum")
    println("The solution to part 2 is $part2sum")
}