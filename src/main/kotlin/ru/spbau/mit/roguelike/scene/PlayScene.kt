package ru.spbau.mit.roguelike.scene

import asciiPanel.AsciiPanel
import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.creatures.CreatureFactory
import ru.spbau.mit.roguelike.engine.CreaturesEngine
import ru.spbau.mit.roguelike.engine.ItemsEngine
import ru.spbau.mit.roguelike.stuff.StuffFactory
import ru.spbau.mit.roguelike.vision.FieldOfView
import ru.spbau.mit.roguelike.world.MapBuilder
import ru.spbau.mit.roguelike.world.World
import java.awt.event.KeyEvent
import java.awt.Color


class PlayScene(private val sceneWidth: Int, private val sceneHeight: Int) : Scene {

    private lateinit var world: World

    private var gameMapWidth: Int = 3 * sceneWidth / 4
    private var gameMapHeight: Int = 3 * sceneHeight / 4
    private val player: Creature
    private val creatureFactory: CreatureFactory
    private val creaturesEngine: CreaturesEngine
    private val stuffFactory: StuffFactory
    private val stuffEngine: ItemsEngine
    private val messages = ArrayList<String>()
    private val fieldOfView: FieldOfView

    private var subscene: Scene? = null

    init {
        createMap()
        fieldOfView = FieldOfView(world)
        creatureFactory = CreatureFactory(world, fieldOfView)
        creaturesEngine = CreaturesEngine()
        creaturesEngine.createCreatures(creatureFactory, messages)

        stuffFactory = StuffFactory(world, fieldOfView)
        stuffEngine = ItemsEngine()
        stuffEngine.createItems(stuffFactory)
        player = world.getPlayer()!!
    }

    override fun displayOutput(terminal: AsciiPanel) {
        val text = "Walking..."
        terminal.write(text, sceneWidth - text.length - 1, 1)

        for (creature in world.allCreatures) {
            terminal.write(creature.glyph, creature.x, creature.y, creature.color)
        }

        for (x in world.itemsMap.indices) {
            for (y in world.itemsMap[0].indices) {
                val item = world.getItem(x, y)
                if (item != null) {
                    terminal.write(item.glyph, x, y, item.color)
                }
            }
        }

        terminal.write("HP: ${player.actualHP}/${player.maxHP}", 1, sceneHeight - 3)
        terminal.write("Attack: ${player.attackStat}", 1, sceneHeight - 2)
        terminal.write("Defense: ${player.defenseStat}", 1, sceneHeight - 1)
        displayMap(terminal, 0, 0)
        displayMessages(terminal, messages)

        if (subscene != null) {
            subscene!!.displayOutput(terminal)
        }
    }

    override fun respondToUserInput(pressedKey: KeyEvent): Scene {
        if (subscene != null) {
            subscene = subscene!!.respondToUserInput(pressedKey)
        } else {
            when (pressedKey.keyCode) {
                KeyEvent.VK_ESCAPE -> return LoseScene(sceneWidth, sceneHeight)
                KeyEvent.VK_ENTER -> return WinScene(sceneWidth, sceneHeight)
                KeyEvent.VK_UP -> player.moveBy(0, -1)
                KeyEvent.VK_DOWN -> player.moveBy(0, 1)
                KeyEvent.VK_LEFT -> player.moveBy(-1, 0)
                KeyEvent.VK_RIGHT -> player.moveBy(1, 0)
                KeyEvent.VK_W -> subscene = EquipmentScene(player)
                KeyEvent.VK_G -> player.pickupItem()
                KeyEvent.VK_I -> subscene = DropScene(player)
            }
        }
        if (subscene == null) {
            creaturesEngine.updateCreatures(world)
        }
        return this
    }

    private fun createMap() {
        world = World(MapBuilder(gameMapWidth, gameMapHeight).makeCaves().buildMap())
    }

    private fun displayMap(terminal: AsciiPanel, left: Int, top: Int) {
        fieldOfView.update(player.x, player.y, player.visionRadius)

        for (x in 0 until gameMapWidth) {
            for (y in 0 until gameMapHeight) {
                val wx = x + left
                val wy = y + top
                val tile = world.tileMap.getTile(wx, wy)

                if (player.canSee(wx, wy)) {
                    terminal.write(world.getGlyph(wx, wy), x, y - top, world.getColor(wx, wy))
                } else {
                    if (fieldOfView.hadSeenBefore(x, y)) {
                        terminal.write(tile.getGlyph(), x, y, Color.darkGray)
                    } else {
                        terminal.write(tile.getGlyph(), x, y, Color.black)
                    }
                }
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