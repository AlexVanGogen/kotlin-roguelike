package ru.spbau.mit.roguelike.world


class TileMap(private val tiles: Array<Array<Tile>>) {

    val width: Int = tiles.size
    val height: Int = tiles[0].size

    fun getTile(x: Int, y: Int): Tile = if (x in 0 until width && y in 0 until height) tiles[x][y] else Tile.BOUNDS
    fun setTile(x: Int, y: Int, t: Tile) {
        tiles[x][y] = t
    }

}