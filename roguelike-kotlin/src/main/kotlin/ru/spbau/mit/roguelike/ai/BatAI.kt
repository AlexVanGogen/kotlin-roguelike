package ru.spbau.mit.roguelike.ai

import ru.spbau.mit.roguelike.creatures.Creature

/**
 * Bat's logic.
 *
 * @property creature creature that assigned to be a bat
 */
class BatAI(creature: Creature): CreatureAI(creature) {

    /**
     * Behavior of creature when the state of the world is updating
     */
    override fun onUpdate() {
        wander()
        wander()
    }
}