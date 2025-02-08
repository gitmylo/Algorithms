package com.mylosoftworks.algorithms.pathfinding

/**
 * A tree node corresponding to a [PathNode], containing other [PathNode]s.
 * @property parent The parent node or null if this is the root. Used for backtracing.
 * @property associated The node associated with this partialpath
 *
 * @property cost The cost to get from the root to this node
 * @property predictedCost The predicted cost from the root to the end through this node, using heuristic function.
 */
class PartialPath<Node: PathNode<Node>>(val parent: PartialPath<Node>?, val associated: Node, val cost: Float, val predictedCost: Float) {
    fun getFullPath(): List<Node> {
        val currentPath = mutableListOf<Node>()
        var currentCheck: PartialPath<Node>? = this
        while (currentCheck != null) {
            currentPath.add(currentCheck.associated)

            currentCheck = currentCheck.parent
        }
        return currentPath.reversed()
    }

    override fun toString(): String {
        return "PartialPath $parent"
    }
}