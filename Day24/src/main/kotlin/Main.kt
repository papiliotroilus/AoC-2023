import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.sign

data class Hailstone(val px: Double, val py: Double, val pz: Double, var vx: Double, var vy: Double, var vz: Double)

fun main() {
    // Initialise variables
    val hailstones = mutableListOf<Hailstone>()
    var maxVelocity = 0
    File("input.txt").forEachLine { inputString ->
        val inputList = inputString.split(",", "@").map { it.trim() }.map { it.toDouble() }
        val lineMax = inputList.drop(3).maxOfOrNull { it.absoluteValue }
        if (lineMax!! > maxVelocity) {
            maxVelocity = lineMax.toInt()
        }
        hailstones.add(Hailstone(inputList[0], inputList[1], inputList[2], inputList[3], inputList[4], inputList[5]))
    }

    // Part 1 function
    fun findInt(min:Double, max: Double): Int {
        var collisionTally = 0
        for (i in 0..<hailstones.size - 1) {
            for (j in i + 1..<hailstones.size) {
                val stone1 = hailstones[i]
                val stone2 = hailstones[j]
                val divergence = stone1.vx * stone2.vy - stone2.vx * stone1.vy
                if (divergence != 0.0) { // Check that the trajectories are not parallel
                    val stone1path = stone1.vy * stone1.px - stone1.vx * stone1.py
                    val stone2path = stone2.vy * stone2.px - stone2.vx * stone2.py
                    val intX = (stone1.vx * stone2path - stone2.vx * stone1path) / divergence
                    val intY = (stone1.vy * stone2path - stone2.vy * stone1path) / divergence
                    if (listOf(intX, intY).all{it in min..max}) // Check that the collision is within bounds
                        if (listOf(stone1, stone2).all{(intX - it.px).sign == it.vx.sign && (intY - it.py).sign == it.vy.sign}) { // Check that the collision happens in the future
                            collisionTally++
                        }
                    }
            }
        }
        return collisionTally
    }

    // Part 2 brute force function
    fun broot(): Double {
        // Pre-process possible range
        val notX = mutableSetOf<Int>()
        val notY = mutableSetOf<Int>()
        val notZ = mutableSetOf<Int>()
        for (stone1 in hailstones) {
            for (stone2 in hailstones) {
                if (stone1.px > stone2.px && stone1.vx > stone2.vx) {
                    val notEnds = listOf(stone1.vx.toInt(), stone2.vx.toInt()).sorted()
                    notX.addAll(IntRange(notEnds.first(), notEnds.last()).toSet())
                }
                if (stone1.py > stone2.py && stone1.vy > stone2.vy) {
                    val notEnds = listOf(stone1.vy.toInt(), stone2.vy.toInt()).sorted()
                    notY.addAll(IntRange(notEnds.first(), notEnds.last()).toSet())
                }
                if (stone1.pz > stone2.pz && stone1.vz > stone2.vz) {
                    val notEnds = listOf(stone1.vz.toInt(), stone2.vz.toInt()).sorted()
                    notZ.addAll(IntRange(notEnds.first(), notEnds.last()).toSet())
                }
            }
        }
        val possibleX = IntRange(-maxVelocity, maxVelocity).toMutableList()
        possibleX.sortBy { it.absoluteValue }
        possibleX.removeAll(notX)
        val possibleY = IntRange(-maxVelocity, maxVelocity).toMutableList()
        possibleX.sortBy { it.absoluteValue }
        possibleY.removeAll(notY)
        val possibleZ = IntRange(-maxVelocity, maxVelocity).toMutableList()
        possibleX.sortBy { it.absoluteValue }
        possibleZ.removeAll(notZ)

        for (xModifier in possibleX) {
            for (yModifier in possibleY) {
                gauntlet@for (zModifier in possibleZ) {
                    // Add frame of reference modifiers
                    var currentX: Double? = null
                    var currentY: Double? = null
                    var currentZ: Double? = null
                    val stone1 = hailstones[1].copy()
                    val stone2 = hailstones[2].copy()
                    for (stone in listOf(stone1, stone2)) {
                        stone.vx -= xModifier
                        stone.vy -= yModifier
                        stone.vz -= zModifier
                    }

                    // Check X and Y coordinates
                    var divergence = stone1.vx * stone2.vy - stone2.vx * stone1.vy
                    if (divergence == 0.0) { // Check that the trajectories are not parallel
                        continue@gauntlet
                    }
                    var stone1path = stone1.vy * stone1.px - stone1.vx * stone1.py
                    var stone2path = stone2.vy * stone2.px - stone2.vx * stone2.py
                    val intX = (stone1.vx * stone2path - stone2.vx * stone1path) / divergence
                    val intY = (stone1.vy * stone2path - stone2.vy * stone1path) / divergence
                    if (listOf(stone1, stone2).any{(intX - it.px).sign != it.vx.sign || (intY - it.py).sign != it.vy.sign}) { // Check that the collision happens in the future
                        continue@gauntlet
                    }
                    currentX = intX
                    currentY = intY

                    // Check Z coordinates
                    divergence = stone1.vx * stone2.vz - stone2.vx * stone1.vz
                    if (divergence == 0.0) { // Check that the trajectories are not parallel
                        continue@gauntlet
                    }
                    stone1path = stone1.vz * stone1.px - stone1.vx * stone1.pz
                    stone2path = stone2.vz * stone2.px - stone2.vx * stone2.pz
                    if ((stone1.vx * stone2path - stone2.vx * stone1path) / divergence != currentX) { // Check the X value is still the same
                        continue@gauntlet
                    }
                    val intZ = (stone1.vz * stone2path - stone2.vz * stone1path) / divergence
                    if (listOf(stone1, stone2).any{(intZ - it.pz).sign != it.vz.sign}) { // Check that the collision happens in the future
                        continue@gauntlet
                    }
                    currentZ = intZ

                    return currentX + currentY + currentZ
                }
            }
        }
        return -1.0
    }

    // Solve problem
//    val part1 = findInt(7.0, 27.0)
    val part1 = findInt(200000000000000.0, 400000000000000.0)
    val part2 = broot().toLong()

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}