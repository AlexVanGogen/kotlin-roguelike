package ru.spbau.mit.roguelike.vision

data class Point(val x: Int, val y: Int) {

    fun neighbors8(): MutableList<Point> {
        return mutableListOf(
                Point(x - 1, y - 1),
                Point(x - 1, y),
                Point(x - 1, y + 1),
                Point(x, y - 1),
                Point(x, y + 1),
                Point(x + 1, y - 1),
                Point(x + 1, y),
                Point(x + 1, y + 1)
        )
    }
}