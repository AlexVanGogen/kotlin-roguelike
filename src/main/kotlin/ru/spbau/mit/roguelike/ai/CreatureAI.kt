package ru.spbau.mit.roguelike.ai

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.vision.Line
import ru.spbau.mit.roguelike.world.Tile

/**
 * Basic creature logic.
 *
 * @property creature creature this logic is attached to
 */
open class CreatureAI(creature: Creature) {

    /**
     * Creature this logic is attached to
     */
    private val creature: Creature

    /**
     * Initialize creature and its behavior
     */
    init {
        creature.setAI(this)
        this.creature = creature
    }

    /**
     * Behavior of creature when it tries to move to position ([x], [y])
     * and [tile] is a tile on that position
     */
    open fun onEnter(x: Int, y: Int, tile: Tile) {
        if (tile.isGround()) {
            creature.x = x
            creature.y = y
        } else {
            creature.doAction("Bump into a wall")
        }
    }

    /**
     * Behavior of creature when the state of the world is updating
     */
    open fun onUpdate() {}

    /**
     * Behavior of creature when it is notified with [message]
     */
    open fun onNotify(message: String) {}

    /**
     * Check if position ([x], [y]) is visible for creature
     */
    open fun canSee(x: Int, y: Int): Boolean {
        if ((creature.x - x) * (creature.x - x) + (creature.y - y) * (creature.y - y) > creature.visionRadius * creature.visionRadius) {
            return false
        }

        for (p in Line(creature.x, creature.y, x, y)) {
            if (creature.tile(p.x, p.y).isGround() || p.x == x && p.y == y) {
                continue
            }
            return false
        }

        return true
    }

    /**
     * Move to random nearby position
     */
    open fun wander() {
        val mx: Int = (Math.random() * 3).toInt() - 1
        val my: Int = (Math.random() * 3).toInt() - 1

        val other = creature.getCreatureOnPosition(mx, my)

        if (other == null || other.glyph != creature.glyph) {
            creature.moveBy(mx, my)
        }
    }
}