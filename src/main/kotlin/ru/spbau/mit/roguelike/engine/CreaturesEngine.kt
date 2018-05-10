package ru.spbau.mit.roguelike.engine

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.creatures.CreatureFactory
import ru.spbau.mit.roguelike.world.World

/**
 * Engine for creating all needed creatures in the world.
 */
class CreaturesEngine {

    private val NUMBER_OF_FUNGUSES = 10
    private val NUMBER_OF_BATS = 10

    /**
     * Create all creatures.
     *
     * @param creatureFactory factory that contains logic of creating
     * @param messages reference to messages that user will see in the [ru.spbau.mit.roguelike.scene.PlayScene]
     */
    fun createCreatures(creatureFactory: CreatureFactory, messages: ArrayList<String>) {
        creatureFactory.createPlayer(messages)

        for (Unit in 1..NUMBER_OF_FUNGUSES) {
            creatureFactory.createFungus()
        }

        for (Unit in 1..NUMBER_OF_BATS) {
            creatureFactory.createBat()
        }
    }

    /**
     * Update behavior of all creatures in the [world].
     */
    fun updateCreatures(world: World) {
        val currentCreatures = world.allCreatures.toMutableList()
        currentCreatures.forEach(Creature::update)
    }
}