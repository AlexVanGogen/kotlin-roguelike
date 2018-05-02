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

    fun createFireElement(): Item {
        val item = Element('*', AsciiPanel.brightRed, "fire")
        world.initializeItemPosition(item)
        return item
    }

    fun createWaterElement(): Item {
        val item = Element('*', AsciiPanel.brightBlue, "water")
        world.initializeItemPosition(item)
        return item
    }

    fun createGrassElement(): Item {
        val item = Element('*', AsciiPanel.brightGreen, "grass")
        world.initializeItemPosition(item)
        return item
    }

    fun createAirElement(): Item {
        val item = Element('*', AsciiPanel.brightCyan, "air")
        world.initializeItemPosition(item)
        return item
    }

    fun createFinalElement(): Item {
        val item = FinalElement('*', AsciiPanel.brightWhite, "UML diagram")
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
        val item = Armor('%', AsciiPanel.brightWhite, "fullplate", 10)
        world.initializeItemPosition(item)
        return item
    }

    fun createSuperarmor(): Item {
        val item = Superarmor('A', AsciiPanel.brightWhite, "GODARMOR", 20, 20)
        world.initializeItemPosition(item)
        return item
    }

    fun createHack(): Item {
        val item = Hack('T', AsciiPanel.red, "hack")
        world.initializeItemPositionReachableForPlayer(item)
        return item
    }
}