package ru.spbau.mit.roguelike.creatures

import com.sun.org.apache.xpath.internal.operations.Bool
import ru.spbau.mit.roguelike.ai.CreatureAI
import ru.spbau.mit.roguelike.ai.PlayerAI
import ru.spbau.mit.roguelike.inventory.Inventory
import ru.spbau.mit.roguelike.stuff.*
import ru.spbau.mit.roguelike.world.Tile
import ru.spbau.mit.roguelike.world.TileMap
import ru.spbau.mit.roguelike.world.World
import java.awt.Color
import kotlin.math.max


class Creature(val world: World,
               val name: String,
               val glyph: Char,
               val color: Color,
               val visionRadius: Int,
               val maxHP: Int,
               var attackStat: Int,
               var defenseStat: Int,
               val maxItemsInInventory: Int = 0) {

    private lateinit var ai: CreatureAI
    var x: Int = 0
    var y: Int = 0
    var actualHP: Int = maxHP
    val inventory = Inventory(maxItemsInInventory)
    var numberOfElementsInInventory = 0
    var maxNumberOfElements = 4
    var hasWon = false

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
        notify("You attacked the ${creatureOnNextPosition.name} for $amount damage")
        creatureOnNextPosition.lowerHP(amount, this)
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

    fun pickupItem() {
        val itemOnPosition = world.getItem(x, y)

        if (itemOnPosition == null) {
            doAction("Sorry, nothing to grab!")
            return
        }

        if (inventory.isFull()) {
            doAction("Cannot grab ${itemOnPosition.name}: inventory is full!")
        } else {
            doAction("Picked up ${itemOnPosition.name}")
            world.removeItem(x, y)
            inventory.add(itemOnPosition)
            if (itemOnPosition is Element) {
                if (itemOnPosition is FinalElement) {
                    hasWon = true
                }
                addElementToInventory(itemOnPosition)
            }
        }
    }

    fun drop(item: Item) {
        doAction("Dropped ${item.name}")
        inventory.remove(item)
        if (item is EquippableItem) {
            unequip(item)
        }
        world.addItemAtEmptyPlace(item, x, y)
        if (item is Element) {
            removeElementFromInventory(item)
        }
    }

    private fun addElementToInventory(element: Element) {
        numberOfElementsInInventory++
        doAction("Element ${element.name} has been found")
    }

    private fun removeElementFromInventory(element: Element) {
        numberOfElementsInInventory--
        doAction("Element ${element.name} has been dropped :(")
    }

    fun unequip(item: EquippableItem) {
        if (!item.isEquipped()) {
            doAction("${item.name} isn't equipped!")
        }
        val hasUnequipped = inventory.tryUnequip(item)
        if (hasUnequipped) {
            doAction("Unequip ${item.name}")
            decreaseStats(item)
        }
    }

    fun equip(item: EquippableItem) {
        if (item.isEquipped()) {
            doAction("${item.name} already equipped!")
        } else {
            val probablyUnequippedItems = inventory.tryEquip(item)
            if (probablyUnequippedItems != null) {
                doAction("Equipped ${item.name}")
                increaseStats(item)
                for (nextItem in probablyUnequippedItems) {
                    decreaseStats(nextItem)
                }
            }
        }
    }

    private fun decreaseStats(item: EquippableItem) {
        when (item) {
            is Weapon -> attackStat -= item.getAttackStat()
            is Armor -> defenseStat -= item.getDefenseStat()
            is Superarmor -> {
                attackStat -= item.getAttackStat()
                defenseStat -= item.getDefenseStat()
            }
        }
    }

    private fun increaseStats(item: EquippableItem) {
        when (item) {
            is Weapon -> attackStat += item.getAttackStat()
            is Armor -> defenseStat += item.getDefenseStat()
            is Superarmor -> {
                attackStat += item.getAttackStat()
                defenseStat += item.getDefenseStat()
            }
        }
    }

    fun hasHackInInventory(): Boolean {
        for (nextItem in inventory.getItems()) {
            if (nextItem.name == "hack") {
                return true
            }
        }
        return false
    }
}