import java.io.File

fun main() {
    println("Processing...")
    // Initialise variables
    val nodeMap = mutableMapOf<String, MutableSet<String>>()
    File("input.txt").forEachLine { inputString ->
        val nodeName = inputString.substring(0, inputString.indexOf(':'))
        val nodeConnections = inputString.substring(inputString.indexOf(' ') + 1).split(' ')
        if (!nodeMap.keys.contains(nodeName)) {
            nodeMap[nodeName] = mutableSetOf()
        }
        nodeMap[nodeName]!!.addAll(nodeConnections)
        for (connection in nodeConnections) {
            if (!nodeMap.keys.contains(connection)) {
                nodeMap[connection] = mutableSetOf()
            }
            nodeMap[connection]!!.add(nodeName)
        }
    }

    // Find min cut
    val nodeList = nodeMap.keys.toList()
    val heatMap = mutableMapOf<Set<String>, Int>()
    var sampleTally = 0
    broot@for (i in nodeList.indices) {
        for (j in i..<nodeList.size) {
            val startNode = nodeList[i]
            val destinationNode = nodeList[j]
            val shortestPath = mutableMapOf<String, String>()
            val queue = mutableListOf<String>()
            queue.add(startNode)
            pathfind@while (queue.isNotEmpty()) {
                val currentNode = queue.first()
                queue.remove(currentNode)
                val nextNodes = nodeMap[currentNode]
                for (nextNode in nextNodes!!) {
                    if (shortestPath.containsKey(nextNode)) {
                        continue
                    }
                    shortestPath[nextNode] = currentNode
                    if (nextNode == destinationNode)  {
                        var traceNode = destinationNode
                        while (traceNode != startNode) {
                            val nodePair = setOf(traceNode, shortestPath[traceNode]!!)
                            if (!heatMap.containsKey(nodePair)) {
                                heatMap[nodePair] = 0
                            }
                            heatMap[nodePair] = heatMap[nodePair]!! + 1
                            traceNode = shortestPath[traceNode]!!
                        }
                        break@pathfind
                    } else {
                        queue.add(nextNode)
                    }
                }
            }
            sampleTally++
            if (sampleTally == 50000) {
                break@broot
            }
        }
    }
    val minCut = heatMap.entries.sortedBy { it.value }.map { it.key }.takeLast(3)

    // Remove min cut
    for (edge in minCut) {
        val firstVertex = edge.toList().first()
        val secondVertex = edge.toList().last()
        val firstConnections = nodeMap[firstVertex]
        firstConnections!!.remove(secondVertex)
        nodeMap[firstVertex] = firstConnections
        val secondConnections = nodeMap[secondVertex]
        secondConnections!!.remove(firstVertex)
        nodeMap[secondVertex] = secondConnections
    }

    // Find max flow
    val groupNodes = mutableSetOf<String>()
    val firstNode = nodeList.first()
    val queue = mutableSetOf<String>()
    queue.addAll(nodeMap[firstNode]!!)
    while (queue.isNotEmpty()) {
        val currentNode = queue.first()
        queue.remove(currentNode)
        val newNodes = nodeMap[currentNode]!!.filter { it !in groupNodes }
        groupNodes.addAll(newNodes)
        queue.addAll(newNodes)
    }

    // Solve problem
    val solution = groupNodes.size * (nodeList.size - groupNodes.size)

    // Print output
    println("The solution is $solution")
}