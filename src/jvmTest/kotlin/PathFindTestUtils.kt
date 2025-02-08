import com.mylosoftworks.algorithms.pathfinding.PathNode
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @property grid A 2d array of bools where true means a spot is available and false means it's not.
 */
class GridNodeMap(val grid: List<List<Boolean>>) {
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

    fun getAvailable(x: Int, y: Int): Boolean {
        if (!grid.indices.contains(x)) return false
        if (!grid[x].indices.contains(y)) return false

        return grid[x][y]
    }

    fun visualizePath(path: List<GridNodeNode>, pathChar: String = "*") =
        grid.mapIndexed {x, it ->
            it.mapIndexed {y, empty ->
                if (path.find { it.x == x && it.y == y } != null) pathChar else if (empty) " " else "#"
            }.joinToString("")
        }.joinToString("\n")

    companion object {
        fun fromString(string: String): Triple<GridNodeMap, GridNodeNode, GridNodeNode> =
            string.split("\n").map { line ->
                line.toCharArray().map { char ->
                    when (char) {
                        ' ' -> 0
                        '@' -> 2
                        '$' -> 3
                        else -> 1
                    }
                }.toTypedArray()
            }.toTypedArray()
                .let { g ->
                    GridNodeMap(g.map { it.map { it != 1 } }).let { grid ->
                        Triple(grid,
                            g.indexOfFirst { it.contains(2) }.let { GridNodeNode(grid, it, g[it].indexOf(2)) },
                            g.indexOfFirst { it.contains(3) }.let { GridNodeNode(grid, it, g[it].indexOf(3)) }
                        )
                    }
                }


// The above function is a one-liner, but expanded over multiple lines for readability
//        fun fromString(string: String): Triple<GridNodeMap, GridNodeNode, GridNodeNode> = string.split("\n").map { line -> line.toCharArray().map { char -> when (char) {' ' -> 0 '@' -> 2 '$' -> 3 else -> 1 } }.toTypedArray() }.toTypedArray().let { g -> GridNodeMap(g.map { it.map { it != 1 } }).let { grid -> Triple(grid, g.indexOfFirst { it.contains(2) }.let { GridNodeNode(grid, it, g[it].indexOf(2)) }, g.indexOfFirst { it.contains(3) }.let { GridNodeNode(grid, it, g[it].indexOf(3)) }) } }
    }
}