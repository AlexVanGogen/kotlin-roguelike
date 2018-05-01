package ru.spbau.mit.roguelike.scene

import asciiPanel.AsciiPanel
import ru.spbau.mit.roguelike.ai.PlayerAI
import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.creatures.CreatureFactory
import ru.spbau.mit.roguelike.engine.CreaturesEngine
import ru.spbau.mit.roguelike.world.MapBuilder
import ru.spbau.mit.roguelike.world.World
import java.awt.event.KeyEvent
import sun.audio.AudioPlayer.player



class PlayScene(private val sceneWidth: Int, private val sceneHeight: Int) : Scene {

    private lateinit var world: World

    private var gameMapWidth: Int = 3 * sceneWidth / 4
    private var gameMapHeight: Int = 3 * sceneHeight / 4
    private val player: Creature
    private val creatureFactory: CreatureFactory
    private val creaturesEngine: CreaturesEngine
    private val messages = ArrayList<String>()
    init {
        createMap()
        creatureFactory = CreatureFactory(world)
        creaturesEngine = CreaturesEngine()
        creaturesEngine.createCreatures(creatureFactory, messages)
        player = world.getPlayer()!!
    }

    override fun displayOutput(terminal: AsciiPanel) {
        val text = "Walking..."
        terminal.write(text, sceneWidth - text.length - 1, 1)

        displayMap(terminal)
        for (creature in world.allCreatures) {
            terminal.write(creature.glyph, creature.x, creature.y, creature.color)
        }

        val stats = String.format(" %3d/%3d hp", player.actualHP, player.maxHP)
        terminal.write(stats, 1, sceneHeight - 1)

        displayMessages(terminal, messages)
    }

    override fun respondToUserInput(pressedKey: KeyEvent): Scene {
        when (pressedKey.keyCode) {
            KeyEvent.VK_ESCAPE -> return LoseScene(sceneWidth, sceneHeight)
            KeyEvent.VK_ENTER -> return WinScene(sceneWidth, sceneHeight)
            KeyEvent.VK_UP -> player.moveBy(0, -1)
            KeyEvent.VK_DOWN -> player.moveBy(0, 1)
            KeyEvent.VK_LEFT -> player.moveBy(-1, 0)
            KeyEvent.VK_RIGHT -> player.moveBy(1, 0)
        }
        creaturesEngine.updateCreatures(world)
        return this
    }

    private fun createMap() {
        world = World(MapBuilder(gameMapWidth, gameMapHeight).makeCaves().buildMap())
    }

    private fun displayMap(terminal: AsciiPanel) {
        for (x in 0 until gameMapWidth) {
            for (y in 0 until gameMapHeight) {
                terminal.write(world.tileMap.getTile(x, y).getGlyph(), x, y, world.tileMap.getTile(x, y).getColor())
            }
        }
    }

    private fun displayMessages(terminal: AsciiPanel, messages: MutableList<String>) {
        val top = sceneHeight - messages.size
        for (i in messages.indices) {
            terminal.writeCenter(messages[i], top + i)
        }
    }
}