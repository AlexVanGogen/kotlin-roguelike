package ru.spbau.mit.roguelike.ai

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.stuff.Hack
import ru.spbau.mit.roguelike.vision.FieldOfView
import ru.spbau.mit.roguelike.world.Tile

class PlayerAI(val creature: Creature, val messages: ArrayList<String>, private val fieldOfView: FieldOfView): CreatureAI(creature) {

    override fun onEnter(x: Int, y: Int, tile: Tile) {
        when (tile) {
            Tile.FLOOR, Tile.UNKNOWN -> {
                creature.x = x
                creature.y = y
            }
            Tile.WALL -> {
                if (creature.hasHackInInventory()) {
                    creature.doAction("Wall is destructing")
                    creature.world.tileMap.setTile(x, y, Tile.DESTRUCTED_WALL)
                } else {
                    creature.doAction("Bump into a wall")
                }
            }
            Tile.DESTRUCTED_WALL -> {
                if (creature.hasHackInInventory()) {
                    creature.doAction("Wall has been destructed")
                    creature.world.tileMap.setTile(x, y, Tile.FLOOR)
                } else {
                    creature.doAction("Bump into a wall")
                }
            }
            else -> {
                creature.doAction("Cannot pass")
            }
        }
    }

    override fun onNotify(message: String) {
        messages.add(message)
        if (messages.size > 5) {
            messages.removeAt(0)
        }
    }

    override fun canSee(x: Int, y: Int) = fieldOfView.isVisible(x, y)
}