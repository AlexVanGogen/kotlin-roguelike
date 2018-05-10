package ru.spbau.mit.roguelike.ai

import com.sun.org.apache.xpath.internal.operations.Bool
import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.vision.Line
import ru.spbau.mit.roguelike.world.Tile
import ru.spbau.mit.roguelike.world.World

open class CreatureAI(creature: Creature) {

    private val creature: Creature

    init {
        creature.setAI(this)
        this.creature = creature
    }

    open fun onEnter(x: Int, y: Int, tile: Tile) {
        if (tile.isGround()) {
            creature.x = x
            creature.y = y
        } else {
            creature.doAction("Bump into a wall")
        }
    }

    open fun onUpdate() {}
    open fun onNotify(message: String) {}

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

    open fun wander() {
        val mx: Int = (Math.random() * 3).toInt() - 1
        val my: Int = (Math.random() * 3).toInt() - 1

        val other = creature.getCreatureOnPosition(mx, my)

        if (other == null || other.glyph != creature.glyph) {
            creature.moveBy(mx, my)
        }
    }
}