package ru.spbau.mit.roguelike.engine

import ru.spbau.mit.roguelike.stuff.StuffFactory

class ItemsEngine {

    private val NUMBER_OF_STICKS = 2
    private val NUMBER_OF_DAGGERS = 1
    private val NUMBER_OF_TUNICS = 2
    private val NUMBER_OF_CHAINMAILS = 1
    private val SWORD_LOOT_PROBABILITY = 0.5
    private val FULLPLATE_LOOT_PROBABILITY = 0.5
    private val GODARMOR_LOOT_PROBABILITY = 1.0

    fun createItems(itemsFactory: StuffFactory) {

        for (i in 1..NUMBER_OF_STICKS) {
            itemsFactory.createStick()
        }
        for (i in 1..NUMBER_OF_DAGGERS) {
            itemsFactory.createDagger()
        }
        if (Math.random() < SWORD_LOOT_PROBABILITY) {
            itemsFactory.createSword()
        }
        for (i in 1..NUMBER_OF_TUNICS) {
            itemsFactory.createTunic()
        }
        for (i in 1..NUMBER_OF_CHAINMAILS) {
            itemsFactory.createChainmail()
        }
        if (Math.random() < FULLPLATE_LOOT_PROBABILITY) {
            itemsFactory.createFullplate()
        }

        if (Math.random() < GODARMOR_LOOT_PROBABILITY) {
            itemsFactory.createSuperarmor()
        }

        itemsFactory.createFireElement()
        itemsFactory.createWaterElement()
        itemsFactory.createGrassElement()
        itemsFactory.createAirElement()

        itemsFactory.createHack()
    }
}