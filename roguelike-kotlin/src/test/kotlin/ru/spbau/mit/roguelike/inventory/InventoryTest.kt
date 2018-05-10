package ru.spbau.mit.roguelike.inventory

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.spbau.mit.roguelike.ai.PlayerAI
import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.stuff.*
import ru.spbau.mit.roguelike.vision.FieldOfView
import ru.spbau.mit.roguelike.world.Tile
import ru.spbau.mit.roguelike.world.TileMap
import ru.spbau.mit.roguelike.world.World
import java.awt.Color

class InventoryTest {

    private val inventory = Inventory(10)
    private val world = World(TileMap(Array(100, { Array(100, { Tile.FLOOR }) })))

    @BeforeEach
    internal fun setUp() {
        for (i in 1..4) {
            inventory.add(Armor('a' + i, Color.BLACK, "armor", i))
        }
        for (i in 1..4) {
            inventory.add(Weapon('A' + i, Color.BLACK, "weapon", i))
        }
        for (i in 1..2) {
            inventory.add(Superarmor('$' + i, Color.BLACK, "superarmor", i, i))
        }
    }

    @Test
    internal fun testCannotAddItemsWhenInventoryIsFull() {
        inventory.add(Item('x', Color.BLACK, ""))
        assertTrue((1..4).all { inventory.get(it - 1) is Armor })
        assertTrue((5..8).all { inventory.get(it - 1) is Weapon })
        assertTrue((9..10).all { inventory.get(it - 1) is Superarmor })
    }

    @Test
    internal fun testItemEquippingAffectsStats() {
        val player = Creature(world, "", 'a', Color.BLACK, 10, 10, 5, 5, 10)
        inventory.getItems().forEach { player.inventory.add(it) }
        PlayerAI(player, ArrayList(), FieldOfView(world))
        val equipment = inventory.getItems().map { it as EquippableItem }

        (1..4).forEach {
            player.equip(equipment[it - 1])
            assertEquals(5, player.attackStat)
            assertEquals(5 + (equipment[it - 1] as Armor).defenseStat, player.defenseStat)
        }

        player.unequip(equipment[3])

        (5..8).forEach {
            player.equip(equipment[it - 1])
            assertEquals(5, player.defenseStat)
            assertEquals(5 + (equipment[it - 1] as Weapon).attackStat, player.attackStat)
        }

        player.unequip(equipment[7])

        (9..10).forEach {
            player.equip(equipment[it - 1])
            assertEquals(5 + (equipment[it - 1] as Superarmor).defenseStat, player.defenseStat)
            assertEquals(5 + (equipment[it - 1] as Superarmor).attackStat, player.attackStat)
        }

        player.unequip(equipment[9])
    }

    @Test
    internal fun testWieldArmorAndWeaponAtOnce() {
        val player = Creature(world, "", 'a', Color.BLACK, 10, 10, 5, 5, 10)
        inventory.getItems().forEach { player.inventory.add(it) }
        PlayerAI(player, ArrayList(), FieldOfView(world))
        val equipment = inventory.getItems().map { it as EquippableItem }

        player.equip(equipment[0])
        player.equip(equipment[4])
        assertEquals(5 + (equipment[0] as Armor).defenseStat, player.defenseStat)
        assertEquals(5 + (equipment[4] as Weapon).attackStat, player.attackStat)
    }

    @Test
    internal fun testSuperarmorReplacesArmorAndWeapon() {
        val player = Creature(world, "", 'a', Color.BLACK, 10, 10, 5, 5, 10)
        inventory.getItems().forEach { player.inventory.add(it) }
        PlayerAI(player, ArrayList(), FieldOfView(world))
        val equipment = inventory.getItems().map { it as EquippableItem }

        player.equip(equipment[0])
        player.equip(equipment[4])
        player.equip(equipment[8])
        assertEquals(5 + (equipment[8] as Superarmor).defenseStat, player.defenseStat)
        assertEquals(5 + (equipment[8] as Superarmor).attackStat, player.attackStat)
    }

    @Test
    internal fun testArmorOrWeaponReplacesSuperarmor() {
        val player = Creature(world, "", 'a', Color.BLACK, 10, 10, 5, 5, 10)
        inventory.getItems().forEach { player.inventory.add(it) }
        PlayerAI(player, ArrayList(), FieldOfView(world))
        val equipment = inventory.getItems().map { it as EquippableItem }

        player.equip(equipment[8])
        player.equip(equipment[0])
        assertEquals(5 + (equipment[0] as Armor).defenseStat, player.defenseStat)
        assertEquals(5, player.attackStat)

        player.equip(equipment[8])
        player.equip(equipment[4])
        assertEquals(5 + (equipment[4] as Weapon).attackStat, player.attackStat)
        assertEquals(5, player.defenseStat)
    }
}