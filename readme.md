# Algorithms
Some algorithms implemented in Kotlin multiplatform.

This project is not intended for serious applications, there might be performance issues. While it should have decent performance, I cannot guarantee this.

# Pathfinding: A*/Dijkstra and derivatives thereof
Path-finding is implemented through `PathFinder` and `PathNode`.

* `PathFinder` is used to calculate the optimal path from the source `PathNode` to the target `PathNode`
* `PathNode` is used to represent a position on the map

> There are no size or position restrictions, overriding equals allows for merging nodes for implementations which require estimation.

> No reference is held to the map itself, only the nodes. Nodes indicate available paths, which are then filtered for duplicates.

PathNodes are effectively virtual, and only require 2 implementations, `rawDistanceTo(other: Node): Float` and `availableNodes(): List<Node>`.

Example path node (from [PathFindTestUtils](src/jvmTest/kotlin/PathFindTestUtils.kt))
```kotlin
// GridNodeMap is defined earlier, see PathFindTestUtils.kt for the definition

class GridNodeNode(val grid: GridNodeMap, val x: Int, val y: Int): PathNode<GridNodeNode> {
    override fun rawDistanceTo(other: GridNodeNode): Float {
        return sqrt((x.toFloat() - other.x.toFloat()).pow(2f) + (y.toFloat() - other.y.toFloat()).pow(2f))
    }

    override fun availableNodes(): List<GridNodeNode> = listOf(x - 1 to y, x + 1 to y, x to y - 1, x to y + 1)
        .filter { grid.getAvailable(it.first, it.second) } // Filter to only include valid nodes
        .map { GridNodeNode(grid, it.first, it.second) } // Wrap in GridNodeNode

    // Since we create new GridNodeNodes, and not taking them from a shared storage, we need a better equals check.
    override fun equals(other: Any?): Boolean {
        if (other is GridNodeNode) {
            return x == other.x && y == other.y && grid == other.grid
        }
        return super.equals(other)
    }
}
```
When the path node is defined, we can path-find through these nodes.
```kotlin
fun pathFind(start: GridNodeNode, target: GridNodeNode) {
    // Defining the solver
    
    // Default heuristic function is f(x) = 0. Like Dijkstra's algorithm
    val solver = PathFinder(start, target)
    // Optionally define a heuristic function
    val solver2 = PathFinder(start, target, rawDistance()) // Default multiplier for rawDistance is 1
    // Or provide a custom function
    val solver3 = PathFinder(start, target, {from, to -> from.rawDistanceTo(to) }) // Same as solver2, but manually written
    
    
    // Using the solver
    
    // Step by step
    val step = solver.step()
    if (step != null) { // When the solver is not done yet, it returns null
        val result = step.getOrThrow() // Result<List<Node>>. Errors if the path couldn't be found.
    } // Repeat until step != null or you need to abort
    
    // Complete a solve or fail by given condition
    val result = solver.fullStep().getOrThrow() // Result<List<Node>>. Errors if the path couldn't be found or if a max step count was specified and passed before solving.
    val resultMaxSteps = solver.fullStep(500).getOrThrow() // Max 500 steps
    val resultUntil = solver.fullStep { false }.getOrThrow() // Until condition returns true (in this case never)
}
```