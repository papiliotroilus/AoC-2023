import java.io.File

class Module(val type:Char, val name:String, val connections:List<String>) {
    var flipFlopState = false
    var conjunctionMemory = mutableMapOf<String, Boolean>()
    private val pingLow = connections.map { Triple(name, false, it) }
    private val pingHigh = connections.map { Triple(name, true, it) }
    fun relay(source: String, input:Boolean): List<Triple<String, Boolean, String>>? {
        when(type) {
            '%' -> {
                if (!input) {
                    flipFlopState = !flipFlopState
                    return when (flipFlopState) {
                        true -> pingHigh
                        false -> pingLow
                    }
                }
            }
            '&' -> {
                conjunctionMemory[source] = input
                return when (conjunctionMemory.values.all { it }) {
                    true -> pingLow
                    false -> pingHigh
                }
            }
        }
        return null
    }
}

fun main() {
    // Initialise variables
    val moduleMap = mutableMapOf<String, Module>()
    val moduleSet = mutableSetOf<String>()
    File("input.txt").forEachLine {
        val firstHalf = it.substring(0, it.indexOf(' '))
        val connections = it.substring(it.indexOf('>') + 2).split(", ")
        val type = firstHalf[0]
        val name = firstHalf.drop(1)
        moduleMap[name] = Module(type, name, connections)
        moduleSet.addAll(connections)
    }

    // Establish last module in graph
    val lastModule = moduleSet.first { it !in moduleMap.keys }

    // Establish all incoming connections to conjunction modules
    for (potentialConjunction in moduleMap) {
        if (potentialConjunction.value.type == '&') {
            val currentConjunction = potentialConjunction.key
            val conjunctionConnections = mutableListOf<String>()
            for (potentialConnection in moduleMap) {
                if (potentialConnection.value.connections.contains(currentConjunction)) {
                    conjunctionConnections.add(potentialConnection.key)
                }
            }
            for (connection in conjunctionConnections) {
                potentialConjunction.value.conjunctionMemory[connection] = false
            }
        }
    }

    // Simulate pulses for number of given cycles and return product of low and high pulses
    fun pulseProduct(cycles:Int): Long {
        var totalLowPulses = cycles.toLong()
        var totalHighPulses = 0L
        val pingQueue = mutableListOf<Triple<String, Boolean, String>>()
        for (cycle in 1..cycles) {
            for (branch in moduleMap["roadcaster"]!!.connections) {
                pingQueue.add(Triple("broadcaster", false, branch))
            }
            totalLowPulses += moduleMap["roadcaster"]!!.connections.count()
            while (pingQueue.isNotEmpty()) {
                val currentPing = pingQueue.first()
                pingQueue.removeFirst()
                if (currentPing.third != lastModule) {
                    val nextPings = moduleMap[currentPing.third]!!.relay(currentPing.first, currentPing.second)
                    if (nextPings != null) {
                        totalHighPulses += nextPings.count { it.second }
                        totalLowPulses += nextPings.count { !it.second }
                        pingQueue.addAll(nextPings)
                    }
                }
            }
        }
        return totalHighPulses * totalLowPulses
    }

    // Find number of cycles required for aligning graph branches via LCM
    fun findAlignment(): Long {
        val branchCycles = mutableListOf<Long>()
        var branchNumber = 1
        for (branch in moduleMap["roadcaster"]!!.connections) {
            // Determine length of cycle and number of low and high pulses at each press
            val initialConfig = moduleMap.values.map { it.flipFlopState.toString() + it.conjunctionMemory.toString() }
            var currentConfig = initialConfig
            var pressNo = 1
            while (pressNo == 1 || currentConfig != initialConfig) {
                val pingQueue = mutableListOf<Triple<String, Boolean, String>>()
                pingQueue.add(Triple("broadcaster", false, branch))
                while (pingQueue.isNotEmpty()) {
                    val currentPing = pingQueue.first()
                    pingQueue.removeFirst()
                    if (currentPing.third != lastModule) {
                        val nextPings = moduleMap[currentPing.third]!!.relay(currentPing.first, currentPing.second)
                        if (nextPings != null) {
                            pingQueue.addAll(nextPings)
                        }
                    }
                }
                currentConfig = moduleMap.values.map { it.flipFlopState.toString() + it.conjunctionMemory.toString() }
                pressNo++
            }
            val cycleLength = pressNo - 1
            branchCycles.add(cycleLength.toLong())
        }
        return listLCM(branchCycles)
    }

    // Solve problem
    val part1 = pulseProduct(1000)
    val part2 = findAlignment()

    // Print output
    println("The solution to part 1 is $part1")
    println("The solution to part 2 is $part2")
}