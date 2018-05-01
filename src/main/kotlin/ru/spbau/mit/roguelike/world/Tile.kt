package ru.spbau.mit.roguelike.world

import asciiPanel.AsciiPanel
import java.awt.Color

enum class Tile(private val glyph: Char, private val color: Color) {

    FLOOR('.', AsciiPanel.yellow),
    WALL('#', AsciiPanel.red),
    BOUNDS('x', AsciiPanel.brightBlack),
    UNKNOWN(' ', AsciiPanel.white);

    fun getGlyph() = glyph
    fun getColor() = color

    fun isGround(): Boolean = this != Tile.BOUNDS && this != Tile.WALL
}