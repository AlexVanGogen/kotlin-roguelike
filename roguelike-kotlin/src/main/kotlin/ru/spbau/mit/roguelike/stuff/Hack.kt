package ru.spbau.mit.roguelike.stuff

import java.awt.Color

/**
 * Item that allow player to move through walls.
 *
 * @property glyph symbol of item in the table of ASCII symbols
 * @property color color of [glyph]
 * @property name common name of that item
 */
class Hack(glyph: Char,
           color: Color,
           name: String): Item(glyph, color, name)