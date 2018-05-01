package ru.spbau.mit.roguelike.engine

import ru.spbau.mit.roguelike.stuff.StuffFactory

class ItemsEngine {

    private val NUMBER_OF_ROCKS = 10

    fun createItems(itemsFactory: StuffFactory) {
        for (i in 1..NUMBER_OF_ROCKS) {
            itemsFactory.createRock()
        }
        itemsFactory.createVictoryItem();
    }
}