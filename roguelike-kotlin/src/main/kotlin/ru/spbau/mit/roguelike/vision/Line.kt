package ru.spbau.mit.roguelike.vision

import kotlin.math.abs

/**
 * Raycasting line defined by points ([x0], [y0]) and ([x1], [y1]), to form [FieldOfView]
 */
class Line(var x0: Int, var y0: Int, x1: Int, y1: Int): Iterable<Point> {

    private val points = ArrayList<Point>()

    init {
        val dx = abs(x1 - x0)
        val dy = abs(y1 - y0)
        var err = dx - dy
        val sx = if (x0 < x1) 1 else -1
        val sy = if (y0 < y1) 1 else -1

        while (true) {
            points.add(Point(x0, y0))
            if (x0 == x1 && y0 == y1) {
                break
            }

            val e2 = err shl 1
            if (e2 > -dx) {
                err -= dy
                x0 += sx
            }
            if (e2 < dx) {
                err += dx
                y0 += sy
            }
        }
    }

    override fun iterator() = points.iterator()
}