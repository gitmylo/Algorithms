package com.mylosoftworks.algorithms.pathfinding

/**
 * A pathfinder, capable of calculating algorithms such as A* and Dijkstra's algorithm.
 *
 * @param startPos The position we want to pathfind from.
 * @property target The position we want to find a path to. Can be modifier, although this doesn't have much purpose.
 * @property heuristic A heuristic function to use, defaults to [rawDistance]`(0f)`, which functions like Dijkstra's algorithm.
 */
open class PathFinder<Node: PathNode<Node>>(startPos: Node, var target: Node, private val heuristic: Heuristic<Node> = rawDistance(0f)) {
    /**
     * The root node which stores all paths taken from here.
     */
    private val pathRoot = PartialPath(null, startPos, 0f, 0f)

    /**
     * A flat list containing all unvisited nodes as PartialPath.
     */
    private val unvisited = mutableListOf(pathRoot) // contains nodes available to visit

    /**
     * For quickly looking up if a node was already visited (which implies a faster path was available to that node)
     */
    private val visited = ArrayDeque<Node>()

    /**
     * Visits a node, updates [unvisited].
     *
     * Warning: Does not check if node is accessible from [source] to [target].
     *
     * @return Full path if found else null
     */
    private fun visitNode(target: PartialPath<Node>) {
        val tNode = target.associated

        val available = tNode.availableNodes()
            .filterNot { node -> node in visited || unvisited.find { it.associated == node } != null } // Filter all unknown unvisited paths
            .map {
                val totalCost = tNode.rawDistanceTo(it) + target.cost // Total cost is the cost to get here from the start
                PartialPath(target, it, totalCost, totalCost + heuristic(tNode, it))
            } // And map to PartialPath for finding

        unvisited.addAll(available) // Add the unvisited available paths.

        unvisited.remove(target) // Unmark this node as unvisited
        visited.add(tNode) // Mark this node as visited
    }

    /**
     * Take a step and return a result with the path if the goal has been reached, return failure if a dead end was hit,
     * and return `null` if the path is still being found.
     */
    fun step(): Result<List<Node>>? {
        // Find cheapest unvisited node
        val cheapest = unvisited.minByOrNull { it.predictedCost }
        if (cheapest == null) return Result.failure(RuntimeException("No available path found"))

        if (cheapest.associated == target) return Result.success(cheapest.getFullPath())

        visitNode(cheapest)

        return null
    }

    /**
     * Take up to [steps] steps, no limit if null.
     *
     * It is suggested to set a limit for large node maps.
     */
    fun fullStep(steps: Int? = null): Result<List<Node>> = fullStep { it >= (steps ?: (it + 1)) }

    /**
     * Take steps until [until] returns true.
     *
     * It is suggested to set a limit for large node maps.
     */
    inline fun fullStep(until: (Int) -> Boolean): Result<List<Node>> {
        var current = 0
        while (!until(current++)) {
            step()?.let { return it }
        }
        return Result.failure(RuntimeException("Ran out of available steps to find the path."))
    }
}