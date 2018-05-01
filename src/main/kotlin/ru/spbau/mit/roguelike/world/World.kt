package ru.spbau.mit.roguelike.world

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.stuff.Item
import ru.spbau.mit.roguelike.vision.Point
import java.awt.Color
import java.util.*
import kotlin.collections.ArrayList

class World(var tileMap: TileMap) {

    val allCreatures: MutableList<Creature> = mutableListOf()
    val itemsMap: Array<Array<Item?>> = Array(tileMap.width, {Array(tileMap.height, { null as Item? })})

    fun tryToGetCreatureInPosition(x: Int, y: Int): Creature? {
        for (creature in allCreatures) {
            if (creature.x == x && creature.y == y) {
                return creature
            }
        }
        return null
    }

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

    fun getPlayer(): Creature? = allCreatures[0]

    fun annihilate(creature: Creature) {
        allCreatures.remove(creature)
    }

    fun getGlyph(x: Int, y: Int): Char {
        val creature = tryToGetCreatureInPosition(x, y)
        if (creature != null) {
            return creature.glyph
        }
        if (getItem(x, y) != null) {
            return itemsMap[x][y]!!.getGlyph()
        }
        return tileMap.getTile(x, y).getGlyph()
    }

    fun getColor(x: Int, y: Int): Color {
        val creature = tryToGetCreatureInPosition(x, y)
        if (creature != null) {
            return creature.color
        }
        if (getItem(x, y) != null) {
            return itemsMap[x][y]!!.getColor()
        }
        return tileMap.getTile(x, y).getColor()
    }

    fun getItem(x: Int, y: Int) = itemsMap[x][y]

    fun removeItem(x: Int, y: Int) {
        itemsMap[x][y] = null
    }

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
                    c.notify("${item.getName()} lands right here")
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