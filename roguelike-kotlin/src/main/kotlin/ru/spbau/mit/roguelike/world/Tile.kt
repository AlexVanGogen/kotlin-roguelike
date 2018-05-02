package ru.spbau.mit.roguelike.world

import asciiPanel.AsciiPanel
import java.awt.Color

enum class Tile(private val glyph: Char, private val color: Color) {

    FLOOR('.', AsciiPanel.yellow),
    WALL('#', AsciiPanel.red),
    DESTRUCTED_WALL('#', AsciiPanel.brightRed),
    BOUNDS('x', AsciiPanel.brightBlack),
    UNKNOWN(' ', AsciiPanel.white);

    fun getGlyph() = glyph
    fun getColor() = color

    fun isGround(): Boolean = this !in listOf(Tile.WALL, Tile.DESTRUCTED_WALL, Tile.BOUNDS)
}