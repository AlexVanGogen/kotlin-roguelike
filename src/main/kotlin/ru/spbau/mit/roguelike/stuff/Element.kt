package ru.spbau.mit.roguelike.stuff

import java.awt.Color

/**
 * Element that player must find and keep in inventory
 *
 * @property glyph symbol of item in the table of ASCII symbols
 * @property color color of [glyph]
 * @property name common name of that item
 */
open class Element(glyph: Char,
                   color: Color,
                   name: String): Item(glyph, color, name)

/**
 * Element that player must find and win the game
 *
 * @property glyph symbol of item in the table of ASCII symbols
 * @property color color of [glyph]
 * @property name common name of that item
 */
class FinalElement(glyph: Char,
                   color: Color,
                   name: String): Element(glyph, color, name)