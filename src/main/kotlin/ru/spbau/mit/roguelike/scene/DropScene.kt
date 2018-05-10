package ru.spbau.mit.roguelike.scene

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.stuff.Item

/**
 * Scene that shows all inventory that player looted during the game.
 * Player can drop whatever he wants.
 *
 * For the details, you can see [InventoryScene] superclass.
 *
 * @property player player instance
 */
class DropScene(override val player: Creature): InventoryScene(player) {

    /**
     * Returns the verb that describes main action when current scene is active.
     */
    override fun getVerb() = "drop"

    /**
     * Checks whether given [item] can be handled somehow when current scene is active.
     */
    override fun isAcceptable(item: Item) = true

    /**
     * Does some action with given [item].
     *
     * @return active scen after completing of action
     */
    override fun use(item: Item): Scene? {
        player.drop(item)
        return this
    }
}