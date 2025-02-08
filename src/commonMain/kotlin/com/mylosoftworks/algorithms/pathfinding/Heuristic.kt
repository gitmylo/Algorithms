package com.mylosoftworks.algorithms.pathfinding

fun interface Heuristic<Node: PathNode<Node>> {
    /**
     * A function which calculates the predicted cost from [from] to [to].
     */
    fun cost(from: Node, to: Node): Float
}

fun <Node: PathNode<Node>> rawDistance(multiplier: Float = 1f) =
    Heuristic<Node> { from, to -> if (multiplier == 0f) 0f else from.rawDistanceTo(to) * multiplier }

operator fun <Node: PathNode<Node>> Heuristic<Node>.invoke(from: Node, to: Node) = cost(from, to)