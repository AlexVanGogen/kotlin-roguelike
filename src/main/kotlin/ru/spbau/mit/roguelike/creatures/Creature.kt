package ru.spbau.mit.roguelike.creatures

import com.sun.org.apache.xpath.internal.operations.Bool
import ru.spbau.mit.roguelike.ai.CreatureAI
import ru.spbau.mit.roguelike.ai.PlayerAI
import ru.spbau.mit.roguelike.world.Tile
import ru.spbau.mit.roguelike.world.TileMap
import ru.spbau.mit.roguelike.world.World
import java.awt.Color
import kotlin.math.max


class Creature(val world: World, val name: String, val glyph: Char, val color: Color, val visionRadius: Int, val maxHP: Int, val attackStat: Int, val defenseStat: Int) {

    private lateinit var ai: CreatureAI
    var x: Int = 0
    var y: Int = 0
    var actualHP: Int = maxHP

    fun setAI(creatureAI: CreatureAI) {
        ai = creatureAI
    }

    fun moveBy(mx: Int, my: Int) {
        if (mx == 0 && my == 0) {
            return
        }
        val creatureOnNextPosition = world.tryToGetCreatureInPosition(x + mx, y + my)
        if (creatureOnNextPosition == null) {
            ai.onEnter(x + mx, y + my, world.tileMap.getTile(x + mx, y + my))
        } else {
            attack(creatureOnNextPosition)
        }
    }

    fun attack(creatureOnNextPosition: Creature) {
        var amount = max(0, attackStat - creatureOnNextPosition.defenseStat)
        amount = (Math.random() * amount).toInt() + 1
        creatureOnNextPosition.lowerHP(amount, this)
        notify("You attacked the ${creatureOnNextPosition.name} for $amount damage")
        creatureOnNextPosition.notify("The $name attacks you for $amount damage")
    }

    private fun lowerHP(amount: Int, by: Creature) {
        actualHP -= amount
        if (isDead()) {
            world.annihilate(this)
            by.notify("$name has been defeated")
        }
    }

    private fun isDead() = actualHP < 1

    fun isPlayer() = ai is PlayerAI

    fun update() {
        ai.onUpdate()
    }

    fun canBeOnPosition(x: Int, y: Int): Boolean {
        val feasibleTile = world.tileMap.getTile(x, y)
        return (feasibleTile != Tile.WALL && feasibleTile != Tile.BOUNDS && world.tryToGetCreatureInPosition(x, y) == null)
    }

    fun notify(message: String, vararg params: Any) {
        ai.onNotify(String.format(message, *params))
    }

    fun canSee(x: Int, y: Int) = ai.canSee(x, y)

    fun tile(wx: Int, wy: Int): Tile {
        return world.tileMap.getTile(wx, wy)
    }

    fun doAction(s: String) {
        notify(s)
    }

    fun getCreatureOnPosition(wx: Int, wy: Int) = world.tryToGetCreatureInPosition(wx, wy)
}