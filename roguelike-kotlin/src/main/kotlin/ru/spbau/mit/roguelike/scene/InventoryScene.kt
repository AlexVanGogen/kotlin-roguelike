package ru.spbau.mit.roguelike.scene

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.stuff.Item
import java.util.ArrayList
import asciiPanel.AsciiPanel
import ru.spbau.mit.roguelike.stuff.EquippableItem
import java.awt.event.KeyEvent

/**
 * Scene that shows some kind of player looted during the game.
 * Player can drop whatever he wants.
 *
 * Current implementations: [DropScene], [EquipmentScene]
 *
 * @property player player instance
 */
abstract class InventoryScene(protected open val player: Creature): Scene {

    private val letters = "abcdefghijklmnopqrstuvwxyz"

    /**
     * Returns the verb that describes main action when current scene is active.
     */
    protected abstract fun getVerb(): String

    /**
     * Checks whether given [item] can be handled somehow when current scene is active.
     */
    protected abstract fun isAcceptable(item: Item): Boolean

    /**
     * Does some action with given [item].
     *
     * @return active scen after completing of action
     */
    protected abstract fun use(item: Item): Scene?

    /**
     * Displays table of ASCII symbols to console.
     *
     * @param terminal table of ASCII symbols that represents the world with inventory window opened.
     */
    override fun displayOutput(terminal: AsciiPanel) {
        val lines = getList()

        var y = terminal.heightInCharacters - lines.size - 3
        val x = terminal.widthInCharacters - 25

        if (lines.size > 0) {
            terminal.clear(' ', x, y, 156 - x, lines.size)
        }

        terminal.write("Inventory", x, y++)
        for (line in lines) {
            terminal.write(line, x, y++)
        }

        terminal.clear(' ', 0, 23, 80, 1)
        terminal.write("What would you like to " + getVerb() + "?", x - 9, y)

        terminal.repaint()
    }

    /**
     * Delegates some action according to what user presses when current scene is active.
     *
     * @param pressedKey key code that user has pressed
     * @return active scene after keypressing action
     */
    override fun respondToUserInput(pressedKey: KeyEvent): Scene? {
        val c = pressedKey.keyChar

        val items = player.inventory.getItems()

        return if (letters.indexOf(c) > -1 && items.size > letters.indexOf(c) && isAcceptable(items[letters.indexOf(c)])) {
            use(items[letters.indexOf(c)])
        } else if (pressedKey.keyCode == KeyEvent.VK_ESCAPE) {
            null
        } else {
            this
        }
    }

    /**
     * Return list of items in the following format: <letter> - <glyph> - <name>.
     */
    private fun getList(): ArrayList<String> {
        val lines = ArrayList<String>()
        val inventory = player.inventory.getItems()

        for (i in inventory.indices) {
            val item = inventory[i]

            if (!isAcceptable(item)) {
                continue
            }

            var line = letters[i] + " - " + item.glyph + " " + item.name

            if(item is EquippableItem && item.isEquipped()) {
                line += " (E)";
            }

            lines.add(line)
        }
        return lines
    }
}