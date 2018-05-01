package ru.spbau.mit.roguelike.world

import ru.spbau.mit.roguelike.creatures.Creature
import java.awt.Color
import java.util.*

class World(var tileMap: TileMap) {

    val allCreatures: MutableList<Creature> = mutableListOf()

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

    fun getPlayer(): Creature? = allCreatures[0]

    fun annihilate(creature: Creature) {
        allCreatures.remove(creature)
    }

    fun getGlyph(x: Int, y: Int): Char {
        val creature = tryToGetCreatureInPosition(x, y)
        return creature?.glyph ?: tileMap.getTile(x, y).getGlyph()
    }

    fun getColor(x: Int, y: Int): Color {
        val creature = tryToGetCreatureInPosition(x, y)
        return creature?.color ?: tileMap.getTile(x, y).getColor()
    }
}