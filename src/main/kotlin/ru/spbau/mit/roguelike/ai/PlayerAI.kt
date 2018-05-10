package ru.spbau.mit.roguelike.ai

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.stuff.Hack
import ru.spbau.mit.roguelike.vision.FieldOfView
import ru.spbau.mit.roguelike.world.Tile

/**
 * Player's logic.
 *
 * @property creature creature that assigned to be a player
 * @property messages reference to messages that user will see in the [ru.spbau.mit.roguelike.scene.PlayScene]
 * @property fieldOfView part of world what player had seen whenever
 */
class PlayerAI(val creature: Creature, val messages: ArrayList<String>, private val fieldOfView: FieldOfView): CreatureAI(creature) {

    /**
     * Behavior of creature when it tries to move to position ([x], [y])
     * and [tile] is a tile on that position
     */
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

    /**
     * Behavior of creature when it is notified with [message]
     */
    override fun onNotify(message: String) {
        messages.add(message)
        if (messages.size > 5) {
            messages.removeAt(0)
        }
    }

    /**
     * Check if position ([x], [y]) is visible for creature
     */
    override fun canSee(x: Int, y: Int) = fieldOfView.isVisible(x, y)
}