package ru.spbau.mit.roguelike.world

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.stuff.Item
import ru.spbau.mit.roguelike.vision.Point
import java.awt.Color
import java.util.*
import kotlin.collections.ArrayList

/**
 * Current world state.
 *
 * @property tileMap map of tiles representing the world
 */
class World(var tileMap: TileMap) {

    /**
     * List of all creatures living in the world by now
     */
    val allCreatures: MutableList<Creature> = mutableListOf()

    /**
     * Map of items location
     */
    val itemsMap: Array<Array<Item?>> = Array(tileMap.width, {Array(tileMap.height, { null as Item? })})

    /**
     * Get positions that can be reached from player's position without having [Hack] in inventory
     */
    lateinit var positionsReachableForPlayer: MutableList<Point>

    /**
     * Get creature (if possible) on position ([x], [y])
     */
    fun tryToGetCreatureInPosition(x: Int, y: Int): Creature? {
        for (creature in allCreatures) {
            if (creature.x == x && creature.y == y) {
                return creature
            }
        }
        return null
    }

    /**
     * Find free position to place next creature
     */
    fun initializeCreaturePosition(creature: Creature) {
        val random = Random(42)
        var x: Int
        var y: Int
        while (true) {
            x = random.nextInt(tileMap.width)
            y = random.nextInt(tileMap.height)
            val randomTile: Tile = tileMap.getTile(x, y)
            if (randomTile.isGround() && tryToGetCreatureInPosition(x, y) == null) {
                creature.x = x
                creature.y = y
                return
            }
        }
    }

    /**
     * Find free position to place next item
     */
    fun initializeItemPosition(item: Item) {
        val random = Random(42)
        var x: Int
        var y: Int
        while (true) {
            x = random.nextInt(tileMap.width)
            y = random.nextInt(tileMap.height)
            val randomTile: Tile = tileMap.getTile(x, y)
            if (randomTile.isGround() && tryToGetCreatureInPosition(x, y) == null && itemsMap[x][y] == null) {
                itemsMap[x][y] = item
                return
            }
        }
    }

    /**
     * Find free position to place next item that must be reachable for player without having [Hack] in inventory
     */
    fun initializeItemPositionReachableForPlayer(item: Item) {
        val random = Random(42)
        val appropriatePoint = positionsReachableForPlayer[random.nextInt(positionsReachableForPlayer.size)]
        itemsMap[appropriatePoint.x][appropriatePoint.y] = item
    }

    /**
     * Try to get player instance
     */
    fun getPlayer(): Creature? = allCreatures[0]

    /**
     * Remove [creature] from the world
     */
    fun annihilate(creature: Creature) {
        allCreatures.remove(creature)
    }

    /**
     * Get current glyph on position ([x], [y])
     */
    fun getGlyph(x: Int, y: Int): Char {
        val creature = tryToGetCreatureInPosition(x, y)
        if (creature != null) {
            return creature.glyph
        }
        if (getItem(x, y) != null) {
            return itemsMap[x][y]!!.glyph
        }
        return tileMap.getTile(x, y).getGlyph()
    }

    /**
     * Get current color of glyph on position ([x], [y])
     */
    fun getColor(x: Int, y: Int): Color {
        val creature = tryToGetCreatureInPosition(x, y)
        if (creature != null) {
            return creature.color
        }
        if (getItem(x, y) != null) {
            return itemsMap[x][y]!!.color
        }
        return tileMap.getTile(x, y).getColor()
    }

    /**
     * Try to get item on position ([x], [y])
     */
    fun getItem(x: Int, y: Int) = itemsMap[x][y]

    /**
     * Remove item from position ([x], [y])
     */
    fun removeItem(x: Int, y: Int) {
        itemsMap[x][y] = null
    }

    /**
     * Find nearest place to drop [item] when player is on position ([x], [y])
     */
    fun addItemAtEmptyPlace(item: Item, x: Int, y: Int) {
        val points = ArrayList<Point>()
        val checked = ArrayList<Point>()

        points.add(Point(x, y))

        while (!points.isEmpty()) {
            val p = points.removeAt(0)
            checked.add(p)

            if (!tileMap.getTile(p.x, p.y).isGround()) {
                continue
            }

            if (itemsMap[p.x][p.y] == null) {
                itemsMap[p.x][p.y] = item
                val c = this.tryToGetCreatureInPosition(p.x, p.y)
                if (c != null) {
                    c.notify("${item.name} lands right here")
                }
                return
            } else {
                val neighbors = p.neighbors8()
                neighbors.removeAll(neighbors)
                points.addAll(neighbors)
            }
        }
    }
}