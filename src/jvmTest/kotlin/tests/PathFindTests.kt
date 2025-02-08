package tests

import GridNodeMap
import com.mylosoftworks.algorithms.pathfinding.PathFinder
import kotlin.test.Test

class PathFindTests {
    fun solveMaze(maze: String) {
        val (map, start, end) = GridNodeMap.fromString(maze)
        val solver = PathFinder(start, end)

        val path = solver.fullStep().getOrThrow()

        println("Maze:\n$maze")
        println()
        println("Solution:\n${map.visualizePath(path)}")
    }

    @Test
    fun tryFindPath() {
        solveMaze("""
            |@          #     ######## $
            |####### #######    ##      
            |         ##      ##    #   
            | #   ##         #     #    
            |  #  #   ##  #  #    ##    
            |# #  #     #     ###       
            |  #  ##    ##              
            |         #     ###   #     
        """.trimMargin())
    }
}