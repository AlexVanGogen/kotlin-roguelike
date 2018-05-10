package ru.spbau.mit.roguelike.stuff

import java.awt.Color

open class EquippableItem(glyph: Char,
                          color: Color,
                          name: String): Item(glyph, color, name) {

    private var isEquipped = false

    fun equip() {
        isEquipped = true
    }

    fun unequip() {
        isEquipped = false
    }

    fun isEquipped() = isEquipped
}