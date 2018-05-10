package ru.spbau.mit.roguelike.stuff

import java.awt.Color

open class Element(glyph: Char,
                   color: Color,
                   name: String): Item(glyph, color, name)

class FinalElement(glyph: Char,
                   color: Color,
                   name: String): Element(glyph, color, name)