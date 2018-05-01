package ru.spbau.mit.roguelike.creatures

import asciiPanel.AsciiPanel
import ru.spbau.mit.roguelike.ai.FungusAI
import ru.spbau.mit.roguelike.ai.PlayerAI
import ru.spbau.mit.roguelike.world.World

class CreatureFactory(val world: World) {

    private val PLAYER_HP = 100
    private val PLAYER_ATK = 5
    private val PLAYER_DEF = 5

    private val FUNGUS_HP = 10
    private val FUNGUS_ATK = 6
    private val FUNGUS_DEF = 1

    fun createPlayer(messages: ArrayList<String>): Creature {
        val player = Creature(world, '@', AsciiPanel.brightWhite, PLAYER_HP, PLAYER_ATK, PLAYER_DEF)
        world.initializeCreaturePosition(player)
        world.allCreatures.add(player)
        PlayerAI(player, messages)
        return player
    }

    fun createFungus(): Creature {
        val fungus = Creature(world, 'F', AsciiPanel.green, FUNGUS_HP, FUNGUS_ATK, FUNGUS_DEF)
        world.initializeCreaturePosition(fungus)
        world.allCreatures.add(fungus)
        FungusAI(fungus, this)
        return fungus
    }
}