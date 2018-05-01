package ru.spbau.mit.roguelike.ai

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.world.Tile

open class CreatureAI(creature: Creature) {

    init {
        creature.setAI(this)
    }

    open fun onEnter(x: Int, y: Int, tile: Tile) {}
    open fun onUpdate() {}
    open fun onNotify(message: String) {}
}