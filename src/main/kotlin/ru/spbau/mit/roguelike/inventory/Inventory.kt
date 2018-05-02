package ru.spbau.mit.roguelike.inventory

import com.sun.org.apache.xpath.internal.operations.Bool
import ru.spbau.mit.roguelike.stuff.*

class Inventory(private val maxItems: Int) {

    private val items = ArrayList<Item>()
    private val equipment = ArrayList<EquippableItem>()

    fun getItems() = items
    fun get(i: Int) = items[i]

    fun add(item: Item) {
        if (!isFull()) {
            items.add(item)
        }
    }

    fun remove(item: Item) {
        items.remove(item)
    }

    fun isFull() = items.size == maxItems

    fun tryEquip(item: EquippableItem): Boolean {
        if (item is Superarmor) {
            if (item in equipment) {
                return false
            } else {
                equipment.clear()
                equipment.add(item)
                item.equip()
                return true
            }
        } else {
            if (item !in equipment) {
                equipment.add(item)
                item.equip()
                return true
            }
            for (nextEquipment in equipment) {
                if (nextEquipment.name == item.name) {
                    equipment.remove(nextEquipment)
                    equipment.add(item)
                    item.equip()
                    return true
                }
            }
        }
        return false
    }

    fun tryUnequip(item: EquippableItem): Boolean {
        if (item !in equipment) {
            return false
        }
        equipment.remove(item)
        item.unequip()
        return true
    }
}