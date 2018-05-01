package ru.spbau.mit.roguelike.creatures

import asciiPanel.AsciiPanel
import ru.spbau.mit.roguelike.ai.BatAI
import ru.spbau.mit.roguelike.ai.FungusAI
import ru.spbau.mit.roguelike.ai.PlayerAI
import ru.spbau.mit.roguelike.vision.FieldOfView
import ru.spbau.mit.roguelike.world.World
import java.awt.Color

class CreatureFactory(val world: World, private val fieldOfView: FieldOfView) {

    private val PLAYER_HP = 100
    private val PLAYER_ATK = 5
    private val PLAYER_DEF = 5

    private val FUNGUS_HP = 10
    private val FUNGUS_ATK = 6
    private val FUNGUS_DEF = 1

    fun createPlayer(messages: ArrayList<String>): Creature {
        val player = Creature(world = world, name = "player", glyph = '@', color = AsciiPanel.brightWhite, visionRadius = 9, maxHP = PLAYER_HP, attackStat = PLAYER_ATK, defenseStat = PLAYER_DEF, maxItemsInInventory = 20)
        world.initializeCreaturePosition(player)
        world.allCreatures.add(player)
        PlayerAI(player, messages, fieldOfView)
        return player
    }

    fun createFungus(): Creature {
        val fungus = Creature(world = world, name = "fungus", glyph = 'F', color = AsciiPanel.green, visionRadius = 9, maxHP = FUNGUS_HP, attackStat = FUNGUS_ATK, defenseStat = FUNGUS_DEF)
        world.initializeCreaturePosition(fungus)
        world.allCreatures.add(fungus)
        FungusAI(fungus, this)
        return fungus
    }

    fun createBat(): Creature {
        val bat = Creature(world = world, name = "bat", glyph = 'W', color = AsciiPanel.yellow, visionRadius = 15, maxHP = 20, attackStat = 5, defenseStat = 2)
        world.initializeCreaturePosition(bat)
        world.allCreatures.add(bat)
        BatAI(bat)
        return bat
    }
}