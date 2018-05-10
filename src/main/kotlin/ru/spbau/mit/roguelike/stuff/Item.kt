package ru.spbau.mit.roguelike.stuff

import java.awt.Color

/**
 * Basic class representing an item.
 *
 * @property glyph symbol of item in the table of ASCII symbols
 * @property color color of [glyph]
 * @property name common name of that item
 */
open class Item(open val glyph: Char, open val color: Color, open val name: String)