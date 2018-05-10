package ru.spbau.mit.roguelike.ai

import ru.spbau.mit.roguelike.creatures.Creature

class BatAI(private val creature: Creature): CreatureAI(creature) {

    override fun onUpdate() {
        wander()
        wander()
    }
}