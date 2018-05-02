package ru.spbau.mit.roguelike.world

class MapBuilder(private val width: Int, private val height: Int) {

    private var currentMap: TileMap

    init {
        currentMap = TileMap(Array(width, { Array(height, {Tile.BOUNDS}) }))
    }

    fun buildMap(): TileMap {
        return currentMap
    }

    fun makeCaves(): MapBuilder {
        return buildRandomMap().smoothRandomizedMap(3)
    }

    private fun buildRandomMap(): MapBuilder {
        for (x in 0 until currentMap.width) {
            for (y in 0 until currentMap.height) {
                if (x == 0 || y == 0 || x == currentMap.width - 1 || y == currentMap.height - 1) {
                    currentMap.setTile(x, y, Tile.BOUNDS)
                } else {
                    currentMap.setTile(x, y, if (Math.random() < 0.5) Tile.FLOOR else Tile.WALL)
                }
            }
        }
        return this
    }

    private fun smoothRandomizedMap(times: Int): MapBuilder {
        val smoothedTileMap: Array<Array<Tile>> = Array(currentMap.width, { Array(currentMap.height, {Tile.BOUNDS}) })
        for (Unit in 0..times) {
            for (x in 1 until currentMap.width - 1) {
                for (y in 1 until currentMap.height - 1) {
                    var floors: Int = 0
                    var rocks: Int = 0

                    for (ox in -1 until 2) {
                        for (oy in -1 until 2) {
                            if (x + ox in 0 until currentMap.width && y + oy in 0 until currentMap.height) {
                                if (currentMap.getTile(x + ox, y + oy) == Tile.FLOOR) {
                                    floors++;
                                } else {
                                    rocks++;
                                }
                            }
                        }
                    }
                    smoothedTileMap[x][y] = if (floors >= rocks) Tile.FLOOR else Tile.WALL
                }
            }
            currentMap = TileMap(smoothedTileMap)
        }
        return this
    }
}