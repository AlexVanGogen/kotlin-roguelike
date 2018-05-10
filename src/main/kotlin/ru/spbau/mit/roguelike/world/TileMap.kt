package ru.spbau.mit.roguelike.world

import ru.spbau.mit.roguelike.vision.Point
import java.util.*

/**
 * Table of tiles the world consists of.
 *
 * @property tiles table of tiles
 */
class TileMap(private val tiles: Array<Array<Tile>>) {

    /**
     * Width and height of the map
     */
    val width: Int = tiles.size
    val height: Int = tiles[0].size

    /**
     * Get tile of position ([x], [y])
     */
    fun getTile(x: Int, y: Int): Tile = if (x in 0 until width && y in 0 until height) tiles[x][y] else Tile.BOUNDS

    /**
     * Set tile [t] of position ([x], [y])
     */
    fun setTile(x: Int, y: Int, t: Tile) {
        tiles[x][y] = t
    }

    /**
     * Get positions that can be reached from ([x], [y]) without having [Hack] in inventory
     */
    fun getAllPositionsReachableFrom(x: Int, y: Int): MutableList<Point> {
        val visited = Array(width, {Array(height, {false})})
        val reachablePositions = mutableListOf<Point>()
        val queue = LinkedList<Point>()
        val start = Point(x, y)
        queue.addLast(start)
        while (!queue.isEmpty()) {
            val p: Point = queue.removeFirst()
            visited[p.x][p.y] = true
            for (neighbor in p.neighbors8()) {
                if (getTile(neighbor.x, neighbor.y).isGround() && !visited[neighbor.x][neighbor.y]) {
                    visited[neighbor.x][neighbor.y] = true
                    queue.addLast(neighbor)
                    reachablePositions.add(neighbor)
                }
            }
        }
        return reachablePositions
    }
}