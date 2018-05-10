package ru.spbau.mit.roguelike.world

import asciiPanel.AsciiPanel
import java.awt.Color

/**
 * Single tile on the map.
 *
 * @property glyph how tile is represented on the map
 * @property color color of glyph
 */
enum class Tile(private val glyph: Char, private val color: Color) {

    /**
     * Tile player can freely move through
     */
    FLOOR('.', AsciiPanel.yellow),
    /**
     * Tile player cannot freely move through, except he has [Hack] in inventory.
     * If he has, pushed wall becomes [DESTRUCTED_WALL]
     */
    WALL('#', AsciiPanel.red),
    /**
     * Tile player cannot freely move through, except he has [Hack] in inventory.
     * If he has, pushed wall becomes [FLOOR]
     */
    DESTRUCTED_WALL('#', AsciiPanel.brightRed),
    /**
     * Tile player cannot freely move through
     */
    BOUNDS('x', AsciiPanel.brightBlack),
    /**
     * Tile player had not seen whenever
     */
    UNKNOWN(' ', AsciiPanel.white);

    fun getGlyph() = glyph
    fun getColor() = color

    /**
     * Check if player can move through that tile
     */
    fun isGround(): Boolean = this !in listOf(Tile.WALL, Tile.DESTRUCTED_WALL, Tile.BOUNDS)
}