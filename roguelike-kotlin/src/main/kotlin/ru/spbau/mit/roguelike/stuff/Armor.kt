package ru.spbau.mit.roguelike.stuff

import java.awt.Color

/**
 * Class representing armor.
 *
 * @property glyph symbol of item in the table of ASCII symbols
 * @property color color of [glyph]
 * @property name common name of that item
 * @property defenseStat defense value that armor gives to player
 */
class Armor(glyph: Char,
            color: Color,
            name: String,
            val defenseStat: Int): EquippableItem(glyph, color, name)