package ru.spbau.mit.roguelike.stuff

import java.awt.Color

class Weapon(glyph: Char,
             color: Color,
             name: String,
             private val attackStat: Int): EquippableItem(glyph, color, name) {

    fun getAttackStat() = attackStat
}