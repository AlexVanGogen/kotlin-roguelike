package ru.spbau.mit.roguelike.creature

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.spbau.mit.roguelike.ai.PlayerAI
import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.creatures.CreatureFactory
import ru.spbau.mit.roguelike.stuff.Hack
import ru.spbau.mit.roguelike.vision.FieldOfView
import ru.spbau.mit.roguelike.world.Tile
import ru.spbau.mit.roguelike.world.TileMap
import ru.spbau.mit.roguelike.world.World
import java.awt.Color

class PlayerTest {

    private val world = World(TileMap(Array(100, { Array(100, { Tile.FLOOR }) })))
    private val player = Creature(world, "", 'a', Color.BLACK, 10, 10, 5, 5, 10)

    @BeforeEach
    internal fun setUp() {
        PlayerAI(player, ArrayList(), FieldOfView(world))
        world.allCreatures.add(player)
    }

    @Test
    internal fun testFreeMove() {
        player.x = 50
        player.y = 50
        player.moveBy(1, 1)
        assertEquals(Pair(51, 51), Pair(player.x, player.y))
    }

    @Test
    internal fun testTryMoveThroughWall() {
        world.tileMap.setTile(50, 50, Tile.WALL)
        player.x = 49
        player.y = 50
        player.moveBy(1, 0)
        assertEquals(Pair(49, 50), Pair(player.x, player.y))
        assertEquals(Tile.WALL, world.tileMap.getTile(50, 50))
    }

    @Test
    internal fun testTryMoveThroughWallWithHack() {
        world.tileMap.setTile(50, 50, Tile.WALL)
        player.x = 49
        player.y = 50
        world.itemsMap[49][50] = Hack('T', Color.BLACK, "hack")
        player.pickupItem()
        player.moveBy(1, 0)
        assertEquals(Pair(49, 50), Pair(player.x, player.y))
        assertEquals(Tile.DESTRUCTED_WALL, world.tileMap.getTile(50, 50))
    }

    @Test
    internal fun testTryMoveThroughDestructedWallWithHack() {
        world.tileMap.setTile(50, 50, Tile.DESTRUCTED_WALL)
        player.x = 49
        player.y = 50
        world.itemsMap[49][50] = Hack('T', Color.BLACK, "hack")
        player.pickupItem()
        player.moveBy(1, 0)
        assertEquals(Pair(49, 50), Pair(player.x, player.y))
        assertEquals(Tile.FLOOR, world.tileMap.getTile(50, 50))
    }

    @Test
    internal fun playerCannotMoveThroughBounds() {
        world.tileMap.setTile(50, 50, Tile.BOUNDS)
        player.x = 49
        player.y = 50
        world.itemsMap[49][50] = Hack('T', Color.BLACK, "hack")
        player.pickupItem()
        player.moveBy(1, 0)
        assertEquals(Pair(49, 50), Pair(player.x, player.y))
        assertEquals(Tile.BOUNDS, world.tileMap.getTile(50, 50))
    }

    @Test
    internal fun testPickupItemRightUnderThePlayer() {
        player.x = 50
        player.y = 50
        world.itemsMap[50][50] = Hack('T', Color.BLACK, "hack")
        assertFalse(player.hasHackInInventory())
        player.pickupItem()
        assertEquals(1, player.inventory.getItems().size)
        assertEquals(Hack::class.java, player.inventory.get(0).javaClass)
        assertTrue(player.hasHackInInventory())
    }

    @Test
    internal fun testAttackItself() {
        assertTrue(player.actualHP == player.maxHP)
        player.attack(player)
        assertTrue(player.actualHP < player.maxHP)
    }

    @Test
    internal fun testPoisonByFungus() {
        assertTrue(player.actualHP == player.maxHP)
        val creatureFactory = CreatureFactory(world, FieldOfView(world))
        val fungus = creatureFactory.createFungus()
        fungus.x = 50
        fungus.y = 50
        player.x = 49
        player.y = 50
        fungus.update()
        assertEquals(player.maxHP - 1, player.actualHP)
        player.moveBy(1, 1)
        fungus.update()
        assertEquals(player.maxHP - 2, player.actualHP)
        player.moveBy(1, -1)
        fungus.update()
        assertEquals(player.maxHP - 3, player.actualHP)
        player.moveBy(-1, -1)
        fungus.update()
        assertEquals(player.maxHP - 4, player.actualHP)
        player.moveBy(-1, 1)
        fungus.update()
        assertEquals(player.maxHP - 5, player.actualHP)
    }

    @Test
    internal fun testAttackFungusByMoving() {
        val creatureFactory = CreatureFactory(world, FieldOfView(world))
        val fungus = creatureFactory.createFungus()
        assertTrue(fungus.maxHP == fungus.actualHP)
        fungus.x = 50
        fungus.y = 50
        player.x = 49
        player.y = 50
        player.moveBy(1, 0)
        assertTrue(fungus.actualHP < fungus.maxHP)
    }
}