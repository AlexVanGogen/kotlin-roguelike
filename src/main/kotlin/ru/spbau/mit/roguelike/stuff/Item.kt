package ru.spbau.mit.roguelike.stuff

import java.awt.Color

data class Item(private val glyph: Char, private val color: Color, private val name: String) {

    fun getGlyph() = glyph
    fun getColor() = color
    fun getName() = name
}