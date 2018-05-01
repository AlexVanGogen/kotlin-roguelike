package ru.spbau.mit.roguelike.inventory

import ru.spbau.mit.roguelike.stuff.Item

class Inventory(private val maxItems: Int) {

    private val items = ArrayList<Item>()

    fun getItems() = items
    fun get(i: Int) = items[i]

    fun add(item: Item) {
        if (!isFull()) {
            items.add(item)
        }
    }

    fun remove(item: Item) {
        items.remove(item)
    }

    fun isFull() = items.size == maxItems
}