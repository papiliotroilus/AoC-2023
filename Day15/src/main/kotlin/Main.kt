import java.io.File

fun hash(label:String):Int {
    var currentHash = 0
    val characters = label.toCharArray()
    for (character in characters) {
        currentHash += character.code
        currentHash *= 17
        currentHash = currentHash.mod(256)
    }
    return currentHash
}

fun main() {
    // Initialise variables
    var part1 = 0
    var part2 = 0
    val boxesMap = mutableMapOf<Int, MutableList<String>>()
    val lensMap = mutableMapOf<String, Int>()

    // Solve problem
    File("input.txt").forEachLine { inputString ->
        val instructionList = inputString.split(',')
        for (instruction in instructionList) {
            val instructionHash = hash(instruction)
            part1 += instructionHash
            val instructionSign = instruction.filter{ it == '=' || it == '-' }
            val label = instruction.substring(0, instruction.indexOf(instructionSign))
            val boxNo = hash(label)
            if (!boxesMap.containsKey(boxNo)) {
                boxesMap[boxNo] = mutableListOf()
            }
            if (instruction.last() == '-') {
                if (boxesMap[boxNo]!!.contains(label)) {
                    boxesMap[boxNo]!!.remove(label)
                }
            } else {
                val focalLength = instruction.substring(instruction.indexOf(instructionSign) + 1).toInt()
                lensMap[label] = focalLength
                if(!boxesMap[boxNo]!!.contains(label)) {
                    boxesMap[boxNo]!!.add(label)
                }
            }
        }
    }
    for (box in boxesMap) {
        for (lens in box.value) {
            part2 += (1 + box.key) * (box.value.indexOf(lens) + 1) * lensMap[lens]!!
        }
    }

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}