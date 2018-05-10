package ru.spbau.mit.roguelike.stuff

import java.awt.Color

class Armor(glyph: Char,
            color: Color,
            name: String,
            private val defenseStat: Int): EquippableItem(glyph, color, name) {

    fun getDefenseStat() = defenseStat
}