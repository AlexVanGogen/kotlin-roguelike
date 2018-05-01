package ru.spbau.mit.roguelike.stuff

import asciiPanel.AsciiPanel
import ru.spbau.mit.roguelike.vision.FieldOfView
import ru.spbau.mit.roguelike.world.World

class StuffFactory(val world: World, val fieldOfView: FieldOfView) {

    fun createRock(): Item {
        val rock = Item('o', AsciiPanel.yellow, "rock")
        world.initializeItemPosition(rock)
        return rock
    }

    fun createVictoryItem(): Item {
        val item = Item('*', AsciiPanel.brightWhite, "teddy bear")
        world.initializeItemPosition(item)
        return item
    }
}