import java.io.File

data class Brick(var x1: Int, var y1: Int, var z1: Int, var x2: Int, var y2: Int, var z2: Int)

fun overlap(firstBrick:Brick, secondBrick:Brick): Boolean {
    return firstBrick.x1 <= secondBrick.x2 && firstBrick.x2 >= secondBrick.x1 && firstBrick.y1 <= secondBrick.y2 && firstBrick.y2 >= secondBrick.y1
}

fun main() {
    // Initialise variables
    val tower = mutableListOf<Brick>()
    File("input.txt").forEachLine { inputString ->
        val brickEnds = inputString.split('~')
        val firstEnd = brickEnds[0].split(',').map { it.toInt() }
        val secondEnd = brickEnds[1].split(',').map { it.toInt() }
        // Sort range ends before instantiating brick
        val sortedEnds = if (firstEnd[0] != secondEnd[0]) { // Horizontal along X axis
            listOf(firstEnd, secondEnd).sortedBy {it[0]}
        } else if (firstEnd[1] != secondEnd[1]) {
            listOf(firstEnd, secondEnd).sortedBy {it[1]} // Horizontal along Y axis
        } else {
            listOf(firstEnd, secondEnd).sortedBy {it[2]} // Vertical or single cube
        }
        val newBrick = Brick(sortedEnds[0][0], sortedEnds[0][1], sortedEnds[0][2], sortedEnds[1][0], sortedEnds[1][1], sortedEnds[1][2])
        tower.add(newBrick)
    }
    tower.sortBy {it.z1} // Sort tower so all bricks are in order of base altitude

    // Settle bricks
    val settledBricks = mutableListOf<Brick>()
    dropBrick@ for (brick in tower) {
        if (settledBricks.isNotEmpty()) {
            settledBricks.sortBy { it.z2 }
            settledBricks.reverse()
            for (settledBrick in settledBricks) {
                // Case where brick falls on top of another brick
                if (overlap(brick, settledBrick)) {
                    val distance = brick.z1 - settledBrick.z2 - 1
                    brick.z1 -= distance
                    brick.z2 -= distance
                    settledBricks.add(brick)
                    continue@dropBrick
                }
            }
        }
        // Case where brick is free to drop to the ground
        val distanceToGround  = brick.z1 - 1
        brick.z1 -= distanceToGround
        brick.z2 -= distanceToGround
        settledBricks.add(brick)
    }

    // Build brick dependency maps
    val supports = mutableMapOf<Brick, List<Brick>>() // Map of brick to other bricks it supports
    for (brick in tower) {
        val upperLevel = tower.filter { it.z1 == brick.z2 + 1 }
        supports[brick] = upperLevel.filter { overlap(brick, it) }
    }

    val supported = mutableMapOf<Brick, List<Brick>>() // Map of brick to other bricks that support it
    for (brick in tower) {
        // Case of brick resting on the ground
        if (brick.z1 == 1) {
            supported[brick] = mutableListOf()
        // Case of brick resting on one or more other bricks
        } else {
            val lowerLevel = tower.filter { it.z2 == brick.z1 - 1 }
            supported[brick] = lowerLevel.filter { overlap(brick, it) }
        }
    }

    // Solve problem
    val part1 = supports.count { candidate -> candidate.value.all { supported[it]!!.count() > 1 } }

    var part2 = 0
    for (brick in tower) {
        var disintegrated = mutableSetOf<Brick>()
        disintegrated.add(brick)
        val queue = mutableListOf<Brick>()
        queue.addAll(supports[brick]!!)
        while (queue.isNotEmpty()) {
            val queueBrick = queue.first()
            if (supported[queueBrick]!!.all { it in disintegrated }) {
                disintegrated.add(queue.first())
                queue.addAll(supports[queue.first()]!!)
            }
            queue.removeFirst()
        }
        part2 += disintegrated.count() - 1
    }

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}