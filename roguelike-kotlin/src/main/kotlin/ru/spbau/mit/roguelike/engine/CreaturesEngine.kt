package ru.spbau.mit.roguelike.engine

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.creatures.CreatureFactory
import ru.spbau.mit.roguelike.world.World

class CreaturesEngine {

    private val NUMBER_OF_FUNGUSES = 10
    private val NUMBER_OF_BATS = 10

    fun createCreatures(creatureFactory: CreatureFactory, messages: ArrayList<String>) {
        creatureFactory.createPlayer(messages)

        for (Unit in 1..NUMBER_OF_FUNGUSES) {
            creatureFactory.createFungus()
        }

        for (Unit in 1..NUMBER_OF_BATS) {
            creatureFactory.createBat()
        }
    }

    fun updateCreatures(world: World) {
        val currentCreatures = world.allCreatures.toMutableList()
        currentCreatures.forEach(Creature::update)
    }
}