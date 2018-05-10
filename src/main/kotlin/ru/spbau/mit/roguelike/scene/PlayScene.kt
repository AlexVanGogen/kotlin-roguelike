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

/**
 * Main scene that represents [world], [player], creatures ([CreatureFactory], [CreaturesEngine])
 * and items ([StuffFactory], [ItemsEngine]).
 *
 * @property sceneWidth scene width
 * @property sceneHeight scene height
 */
class PlayScene(private val sceneWidth: Int, private val sceneHeight: Int) : Scene {

    /**
     * Reference to the current world state.
     */
    private lateinit var world: World

    /**
     * Width and heights of our world.
     */
    private var gameMapWidth: Int = 3 * sceneWidth / 4
    private var gameMapHeight: Int = 3 * sceneHeight / 4

    /**
     * Player instance.
     */
    private val player: Creature

    /**
     * Factory for creating any possible creature in the world.
     * For the details, see [CreatureFactory] class.
     */
    private val creatureFactory: CreatureFactory

    /**
     * Engine for creating all needed creatures in the world.
     * For the details, see [CreaturesEngine] class.
     */
    private val creaturesEngine: CreaturesEngine

    /**
     * Factory for creating any possible item in the world.
     * For the details, see [StuffFactory] class.
     */
    private val stuffFactory: StuffFactory

    /**
     * Engine for creating all needed items in the world.
     * For the details, see [ItemsEngine] class.
     */
    private val stuffEngine: ItemsEngine

    /**
     * All messages that must be displayed now.
     */
    private val messages = ArrayList<String>()

    /**
     * Current map state according to what parts of map player saw and where he is right now.
     * For the details, see [FieldOfView] class.
     */
    private val fieldOfView: FieldOfView

    /**
     * Window that can be displayed along with the playground.
     */
    private var subscene: Scene? = null

    /**
     * Creates content for the world scene.
     */
    init {
        createMap()
        fieldOfView = FieldOfView(world)
        creatureFactory = CreatureFactory(world, fieldOfView)
        creaturesEngine = CreaturesEngine()
        creaturesEngine.createCreatures(creatureFactory, messages)

        player = world.getPlayer()!!
        world.positionsReachableForPlayer = world.tileMap.getAllPositionsReachableFrom(player.x, player.y)

        stuffFactory = StuffFactory(world, fieldOfView)
        stuffEngine = ItemsEngine()
        stuffEngine.createItems(stuffFactory)
    }

    /**
     * Displays table of ASCII symbols to console.
     *
     * @param terminal table of ASCII symbols that represents the world.
     */
    override fun displayOutput(terminal: AsciiPanel) {

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

        terminal.write("HP: ${player.actualHP}/${player.maxHP}", 1, sceneHeight - 4)
        terminal.write("Attack: ${player.attackStat}", 1, sceneHeight - 3)
        terminal.write("Defense: ${player.defenseStat}", 1, sceneHeight - 2)
        terminal.write("Elements: ${player.numberOfElementsInInventory}/${player.maxNumberOfElements}", 1, sceneHeight - 1)
        displayMap(terminal, 0, 0)
        displayMessages(terminal, messages)

        if (subscene != null) {
            subscene!!.displayOutput(terminal)
        }
    }

    /**
     * Delegates some action according to what user presses when current scene is active.
     *
     * @param pressedKey key code that user has pressed
     * @return active scene after keypressing action
     */
    override fun respondToUserInput(pressedKey: KeyEvent): Scene {
        if (subscene != null) {
            subscene = subscene!!.respondToUserInput(pressedKey)
        } else {
            when (pressedKey.keyCode) {
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
        if (player.hasWon) {
            return WinScene(sceneWidth, sceneHeight)
        }
        if (player.numberOfElementsInInventory == player.maxNumberOfElements) {
            player.doAction("Final element has spawned!")
            player.maxNumberOfElements = 5
            stuffFactory.createFinalElement()
        }
        if (player.actualHP < 1) {
            return LoseScene(sceneWidth, sceneHeight)
        }
        return this
    }

    /**
     * Creates map with basic objects (walls, bounds).
     */
    private fun createMap() {
        world = World(MapBuilder(gameMapWidth, gameMapHeight).makeWalls().buildMap())
    }

    /**
     * Displays ASCII symbols that represent map to [terminal]
     * with the top-left corner on coordinates ([left], [top]).
     */
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

    /**
     * Displays game notifications ([messages]) to [terminal].
     */
    private fun displayMessages(terminal: AsciiPanel, messages: MutableList<String>) {
        val top = sceneHeight - messages.size
        for (i in messages.indices) {
            terminal.writeCenter(messages[i], top + i)
        }
    }
}