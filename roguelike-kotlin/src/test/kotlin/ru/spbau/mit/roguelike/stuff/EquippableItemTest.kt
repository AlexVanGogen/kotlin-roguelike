package ru.spbau.mit.roguelike.stuff

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.spbau.mit.roguelike.inventory.Inventory
import ru.spbau.mit.roguelike.world.Tile
import ru.spbau.mit.roguelike.world.TileMap
import ru.spbau.mit.roguelike.world.World
import java.awt.Color

class EquippableItemTest {

    private val inventory = Inventory(10)

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
    internal fun testItemEquipping() {
        val equipment = inventory.getItems().map { it as EquippableItem }
        equipment.forEach {
            assertFalse(it.isEquipped())
            it.equip()
            assertTrue(it.isEquipped())
            it.unequip()
            assertFalse(it.isEquipped())
        }
    }
}