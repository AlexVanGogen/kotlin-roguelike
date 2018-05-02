package ru.spbau.mit.roguelike.ai

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.creatures.CreatureFactory
import java.util.*

class FungusAI(val creature: Creature, val creatureFactory: CreatureFactory): CreatureAI(creature) {

    private val MIN_SPREAD = 1
    private val MAX_SPREAD = 2
    private val MAX_SPREAD_DISTANCE = 3
    private val SPREAD_PROBABILITY = 0.001

    private val random = Random(42)

    override fun onUpdate() {
        if (Math.random() <= SPREAD_PROBABILITY) {
            spread()
        }
        toxic()
    }

    private fun spread() {
        val spreadFactor = random.nextInt(MAX_SPREAD - MIN_SPREAD) + MIN_SPREAD
        for (Unit in 1..spreadFactor) {
            var x = creature.x + random.nextInt(2 * MAX_SPREAD_DISTANCE) - MAX_SPREAD_DISTANCE
            if (x == creature.x) {
                x++
            }
            var y = creature.y + random.nextInt(2 * MAX_SPREAD_DISTANCE) - MAX_SPREAD_DISTANCE
            if (y == creature.y) {
                y++
            }
            if (creature.canBeOnPosition(x, y)) {
                val childFungus = creatureFactory.createFungus()
                childFungus.x = x
                childFungus.y = y
            }
        }
    }

    private fun toxic() {
        val x = creature.x
        val y = creature.y
        listOf(Pair(x + 1, y), Pair(x - 1, y), Pair(x, y + 1), Pair(x, y - 1)).forEach {
            val feasibleCreature = creatureFactory.world.tryToGetCreatureInPosition(it.first, it.second)
            if (feasibleCreature != null && feasibleCreature.isPlayer()) {
                creature.attack(feasibleCreature)
            }
        }
    }
}