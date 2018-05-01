package ru.spbau.mit.roguelike.ai

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.vision.FieldOfView
import ru.spbau.mit.roguelike.world.Tile

class PlayerAI(val creature: Creature, val messages: ArrayList<String>, private val fieldOfView: FieldOfView): CreatureAI(creature) {

    override fun onEnter(x: Int, y: Int, tile: Tile) {
        if (tile.isGround()) {
            creature.x = x
            creature.y = y
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