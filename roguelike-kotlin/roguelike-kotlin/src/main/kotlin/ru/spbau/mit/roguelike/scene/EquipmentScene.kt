package ru.spbau.mit.roguelike.scene

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.stuff.EquippableItem
import ru.spbau.mit.roguelike.stuff.Item

class EquipmentScene(override val player: Creature): InventoryScene(player) {

    override fun getVerb(): String = "equip"

    override fun isAcceptable(item: Item): Boolean {
        return item is EquippableItem
    }

    override fun use(item: Item): Scene? {
        if (item is EquippableItem) {
            player.equip(item)
        }
        return null
    }

}