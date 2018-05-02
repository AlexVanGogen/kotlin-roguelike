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

    fun createStick(): Item {
        val item = Weapon(')', AsciiPanel.brightGreen, "stick", 2)
        world.initializeItemPosition(item)
        return item
    }

    fun createDagger(): Item {
        val item = Weapon(')', AsciiPanel.brightBlue, "dagger", 5)
        world.initializeItemPosition(item)
        return item
    }

    fun createSword(): Item {
        val item = Weapon(')', AsciiPanel.brightWhite, "sword", 10)
        world.initializeItemPosition(item)
        return item
    }

    fun createTunic(): Item {
        val item = Armor('%', AsciiPanel.brightGreen, "tunic", 2)
        world.initializeItemPosition(item)
        return item
    }

    fun createChainmail(): Item {
        val item = Armor('%', AsciiPanel.brightBlue, "chainmail", 5)
        world.initializeItemPosition(item)
        return item
    }

    fun createFullplate(): Item {
        val item = Armor('%', AsciiPanel.brightWhite, "fullplate armor", 10)
        world.initializeItemPosition(item)
        return item
    }

    fun createSuperarmor(): Item {
        val item = Superarmor('A', AsciiPanel.brightWhite, "godarmor", 20, 20)
        world.initializeItemPosition(item)
        return item
    }

    fun createHack(): Item {
        val item = Hack('T', AsciiPanel.red, "hack")
        world.initializeItemPositionReachableForPlayer(item)
        return item
    }
}