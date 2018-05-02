package ru.spbau.mit.roguelike.scene

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.stuff.Item

class DropScene(override val player: Creature): InventoryScene(player) {

    override fun getVerb() = "drop"

    override fun isAcceptable(item: Item) = true

    override fun use(item: Item): Scene? {
        player.drop(item)
        return null
    }
}