package com.mylosoftworks.algorithms.pathfinding

/**
 * A single node which can be traversed by a pathfinder.
 */
interface PathNode<Self: PathNode<Self>> {
    /**
     * Returns the raw distance from `this` to [other]. Used for calculating the heuristic cost.
     */
    fun rawDistanceTo(other: Self): Float

    /**
     * Returns a list with all nodes that are accessible from this node.
     */
    fun availableNodes(): List<Self>
}