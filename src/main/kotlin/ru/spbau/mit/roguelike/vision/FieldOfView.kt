package ru.spbau.mit.roguelike.vision

import ru.spbau.mit.roguelike.world.Tile
import ru.spbau.mit.roguelike.world.World

/**
 * Map areas that player sees now and that player had seen whatever.
 *
 * @property world current world state
 */
class FieldOfView(private val world: World) {

    /**
     * Mask of tiles that player sees right now
     */
    private var visibilityOfTiles: Array<Array<Boolean>> = Array(world.tileMap.width, {Array(world.tileMap.height, {false})})

    /**
     * Mask of tiles that player had ever seen
     */
    private var knowingOfTiles: Array<Array<Boolean>> = Array(world.tileMap.width, {Array(world.tileMap.height, {false})})

    /**
     * All tiles on the map
     */
    private val tiles: Array<Array<Tile>> = Array(world.tileMap.width, {Array(world.tileMap.height, {Tile.UNKNOWN})})

    /**
     * Check if tile on position ([x], [y]) is visible
     */
    fun isVisible(x: Int, y: Int): Boolean {
        return x >= 0 && y >= 0 && x < visibilityOfTiles.size && y < visibilityOfTiles[0].size && visibilityOfTiles[x][y]
    }

    /**
     * Check if tile on position ([x], [y]) had ever seen
     */
    fun hadSeenBefore(x: Int, y: Int): Boolean {
        return x >= 0 && y >= 0 && x < knowingOfTiles.size && y < knowingOfTiles[0].size && knowingOfTiles[x][y]
    }

    /**
     * Update information about tile on position ([wx], [wy]),
     * if it is known that observer has radius of vision equal to [radius]
     */
    fun update(wx: Int, wy: Int, radius: Int) {
        visibilityOfTiles = Array(world.tileMap.width, {Array(world.tileMap.height, {false})})

        for (x in -radius until radius) {
            for (y in -radius until radius) {
                if (x * x + y * y > radius * radius) {
                    continue
                }

                if (wx + x < 0 || wx + x >= world.tileMap.width || wy + y < 0 || wy + y >= world.tileMap.height) {
                    continue;
                }

                for (p in Line(wx, wy, wx + x, wy + y)) {
                    val tile = world.tileMap.getTile(p.x, p.y)
                    visibilityOfTiles[p.x][p.y] = true
                    knowingOfTiles[p.x][p.y] = true
                    tiles[p.x][p.y] = tile

                    if (!tile.isGround()) {
                        break
                    }
                }
            }
        }
    }
}