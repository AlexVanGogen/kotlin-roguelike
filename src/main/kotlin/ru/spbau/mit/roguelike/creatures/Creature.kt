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

/**
 * Class that represents creature behavior.
 *
 * @property world current world state
 * @property name common name of that creature
 * @property glyph symbol of creature in the table of ASCII symbols
 * @property color color of [glyph]
 * @property visionRadius how far creature can see other creatures in the map
 * @property maxHP maximum HP value that creature can have
 * @property attackStat current attack stat of creature
 * @property defenseStat current defense stat of creature
 * @property maxItemsInInventory how many items creature can carry at once
 */
class Creature(val world: World,
               val name: String,
               val glyph: Char,
               val color: Color,
               val visionRadius: Int,
               val maxHP: Int,
               var attackStat: Int,
               var defenseStat: Int,
               val maxItemsInInventory: Int = 0) {

    /**
     * Behavior logic of creature
     */
    private lateinit var ai: CreatureAI

    /**
     * Coordinates of creature on the map
     */
    var x: Int = 0
    var y: Int = 0

    /**
     * Current HP value
     */
    var actualHP: Int = maxHP

    /**
     * Current inventory that creature has
     */
    val inventory = Inventory(maxItemsInInventory)

    /**
     * Actual number of elements in creature's [inventory]
     */
    var numberOfElementsInInventory = 0

    /**
     * Maximum number of elements that creature must gather to proceed in the game
     */
    var maxNumberOfElements = 4

    /**
     * Indicator if creature has completed all in-game goals
     */
    var hasWon = false

    /**
     * Sets [creatureAI] logic to creature's logic
     */
    fun setAI(creatureAI: CreatureAI) {
        ai = creatureAI
    }

    /**
     * Moves creature position to [mx] units to the right and [my] units to the bottom of map
     */
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

    /**
     * Launches action for attacking [creatureOnNextPosition]
     */
    fun attack(creatureOnNextPosition: Creature) {
        var amount = max(0, attackStat - creatureOnNextPosition.defenseStat)
        amount = (Math.random() * amount).toInt() + 1
        notify("You attacked the ${creatureOnNextPosition.name} for $amount damage")
        creatureOnNextPosition.lowerHP(amount, this)
        creatureOnNextPosition.notify("The $name attacks you for $amount damage")
    }

    /**
     * Reduces actual HP by [amount] after attack that creature [by] has done.
     * If creature loses all HP, he dies.
     */
    private fun lowerHP(amount: Int, by: Creature) {
        actualHP -= amount
        if (isDead()) {
            world.annihilate(this)
            by.notify("$name has been defeated")
        }
    }

    /**
     * Indicates if creature had just died
     */
    private fun isDead() = actualHP < 1

    /**
     * Indicates if creature is our player
     */
    fun isPlayer() = ai is PlayerAI

    /**
     * Updates creature's behavior according to changes in the world
     */
    fun update() {
        ai.onUpdate()
    }

    /**
     * Checks if some creature can be on position ([x], [y])
     */
    fun canBeOnPosition(x: Int, y: Int): Boolean {
        val feasibleTile = world.tileMap.getTile(x, y)
        return (feasibleTile != Tile.WALL && feasibleTile != Tile.BOUNDS && world.tryToGetCreatureInPosition(x, y) == null)
    }

    /**
     * Notify creature with [message] and [params]
     */
    fun notify(message: String, vararg params: Any) {
        ai.onNotify(String.format(message, *params))
    }

    /**
     * Checks if this creature can see position ([x], [y])
     */
    fun canSee(x: Int, y: Int) = ai.canSee(x, y)

    /**
     * Returns what is on position ([wx], [wy])
     */
    fun tile(wx: Int, wy: Int): Tile {
        return world.tileMap.getTile(wx, wy)
    }

    /**
     * Notifies creature with message [s]
     */
    fun doAction(s: String) {
        notify(s)
    }

    /**
     * Tries to get creature on position ([wx], [wy])
     */
    fun getCreatureOnPosition(wx: Int, wy: Int) = world.tryToGetCreatureInPosition(wx, wy)

    /**
     * Attempts to pickup item that is right under the player now
     */
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

    /**
     * Drops [item] from inventory
     */
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

    /**
     * Adds special item named [element] to inventory
     */
    private fun addElementToInventory(element: Element) {
        numberOfElementsInInventory++
        doAction("Element ${element.name} has been found")
    }

    /**
     * Removes special item named [element] from inventory
     */
    private fun removeElementFromInventory(element: Element) {
        numberOfElementsInInventory--
        doAction("Element ${element.name} has been dropped :(")
    }

    /**
     * Unwields [item] from player
     */
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

    /**
     * Wields [item] to player
     */
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

    /**
     * Decreases stats when [item] has unequipped
     */
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

    /**
     * Increases stats when [item] has equipped
     */
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

    /**
     * Checks if hack is in inventory
     */
    fun hasHackInInventory(): Boolean {
        for (nextItem in inventory.getItems()) {
            if (nextItem.name == "hack") {
                return true
            }
        }
        return false
    }
}