package ru.spbau.mit.roguelike.stuff

import java.awt.Color

/**
 * Basic class representing an item that player can equip.
 *
 * @property glyph symbol of item in the table of ASCII symbols
 * @property color color of [glyph]
 * @property name common name of that item
 */
open class EquippableItem(glyph: Char,
                          color: Color,
                          name: String): Item(glyph, color, name) {

    private var isEquipped = false

    /**
     * Mark item as equipped
     */
    fun equip() {
        isEquipped = true
    }

    /**
     * Mark item as unequipped
     */
    fun unequip() {
        isEquipped = false
    }

    /**
     * Check if item is equipped
     */
    fun isEquipped() = isEquipped
}