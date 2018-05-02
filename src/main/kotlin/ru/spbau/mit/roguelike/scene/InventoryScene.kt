package ru.spbau.mit.roguelike.scene

import ru.spbau.mit.roguelike.creatures.Creature
import ru.spbau.mit.roguelike.stuff.Item
import java.util.ArrayList
import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent


abstract class InventoryScene(protected open val player: Creature): Scene {

    private val letters = "abcdefghijklmnopqrstuvwxyz"

    protected abstract fun getVerb(): String
    protected abstract fun isAcceptable(item: Item): Boolean
    protected abstract fun use(item: Item): Scene?

    override fun displayOutput(terminal: AsciiPanel) {
        val lines = getList()

        var y = terminal.heightInCharacters - lines.size - 3
        val x = terminal.widthInCharacters - 25

        if (lines.size > 0) {
            terminal.clear(' ', x, y, 120 - x, lines.size)
        }

        terminal.write("Inventory", x, y++)
        for (line in lines) {
            terminal.write(line, x, y++)
        }

        terminal.clear(' ', 0, 23, 80, 1)
        terminal.write("What would you like to " + getVerb() + "?", x - 9, y)

        terminal.repaint()
    }

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

    private fun getList(): ArrayList<String> {
        val lines = ArrayList<String>()
        val inventory = player.inventory.getItems()

        for (i in inventory.indices) {
            val item = inventory[i]

            if (!isAcceptable(item)) {
                continue
            }

            val line = letters[i] + " - " + item.glyph + " " + item.name

            lines.add(line)
        }
        return lines
    }
}