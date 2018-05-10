package ru.spbau.mit.roguelike.world

import ru.spbau.mit.roguelike.vision.Point
import java.util.*


class TileMap(private val tiles: Array<Array<Tile>>) {

    val width: Int = tiles.size
    val height: Int = tiles[0].size

    fun getTile(x: Int, y: Int): Tile = if (x in 0 until width && y in 0 until height) tiles[x][y] else Tile.BOUNDS
    fun setTile(x: Int, y: Int, t: Tile) {
        tiles[x][y] = t
    }

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