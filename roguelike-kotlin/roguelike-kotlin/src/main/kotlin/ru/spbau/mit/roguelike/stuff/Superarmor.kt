package ru.spbau.mit.roguelike.stuff

import java.awt.Color

class Superarmor(glyph: Char,
                color: Color,
                name: String,
                private val attackStat: Int,
                private val defenseStat: Int): EquippableItem(glyph, color, name) {

    fun getAttackStat() = attackStat
    fun getDefenseStat() = defenseStat
}