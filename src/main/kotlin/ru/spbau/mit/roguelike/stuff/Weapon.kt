package ru.spbau.mit.roguelike.stuff

import java.awt.Color

/**
 * Class representing weapon.
 *
 * @property glyph symbol of item in the table of ASCII symbols
 * @property color color of [glyph]
 * @property name common name of that item
 * @property attackStat attack value that armor gives to player
 */
class Weapon(glyph: Char,
             color: Color,
             name: String,
             private val attackStat: Int): EquippableItem(glyph, color, name) {

    fun getAttackStat() = attackStat
}