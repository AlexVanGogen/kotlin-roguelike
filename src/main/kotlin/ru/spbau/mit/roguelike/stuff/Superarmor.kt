package ru.spbau.mit.roguelike.stuff

import java.awt.Color

/**
 * Class representing an armor with bots high attack and defense stat.
 *
 * @property glyph symbol of item in the table of ASCII symbols
 * @property color color of [glyph]
 * @property name common name of that item
 * @property attackStat attack value that armor gives to player
 * @property defenseStat defense value that armor gives to player
 */
class Superarmor(glyph: Char,
                color: Color,
                name: String,
                private val attackStat: Int,
                private val defenseStat: Int): EquippableItem(glyph, color, name) {

    fun getAttackStat() = attackStat
    fun getDefenseStat() = defenseStat
}