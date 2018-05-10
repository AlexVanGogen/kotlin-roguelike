package ru.spbau.mit.roguelike.inventory

import ru.spbau.mit.roguelike.stuff.*

/**
 * Player's inventory.
 *
 * @property maxItems maximal number of items player can carry at once
 */
class Inventory(private val maxItems: Int) {

    /**
     * All items in inventory
     */
    private val items = ArrayList<Item>()

    /**
     * Items that player can wield
     */
    private val equipment = ArrayList<EquippableItem>()

    /**
     * Get all items or just ith item
     */
    fun getItems() = items
    fun get(i: Int) = items[i]

    /**
     * Try to add [item] to inventory
     */
    fun add(item: Item) {
        if (!isFull()) {
            items.add(item)
        }
    }

    /**
     * Remove [item] from inventory
     */
    fun remove(item: Item) {
        items.remove(item)
    }

    /**
     * Checks if there are no space for items in inventory
     */
    fun isFull() = items.size == maxItems

    /**
     * Tries to wield [item]
     */
    fun tryEquip(item: EquippableItem): List<EquippableItem>? {
        if (item is Superarmor) {
            if (item in equipment) {
                return null
            } else {
                val unequippedElements = equipment.toList()
                equipment.forEach { it.unequip() }
                equipment.clear()
                equipment.add(item)
                item.equip()
                return unequippedElements
            }
        } else {
            for (nextEquipment in equipment) {
                if (nextEquipment.javaClass == item.javaClass || nextEquipment is Superarmor) {
                    equipment.remove(nextEquipment)
                    nextEquipment.unequip()
                    equipment.add(item)
                    item.equip()
                    return listOf(nextEquipment)
                }
            }
            equipment.add(item)
            item.equip()
            return listOf()
        }
    }

    /**
     * Tries to unwield [item]
     */
    fun tryUnequip(item: EquippableItem): Boolean {
        if (item !in equipment) {
            return false
        }
        equipment.remove(item)
        item.unequip()
        return true
    }
}