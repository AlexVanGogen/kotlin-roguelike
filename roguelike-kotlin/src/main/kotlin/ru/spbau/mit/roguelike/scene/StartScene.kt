package ru.spbau.mit.roguelike.scene

import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

open class StartScene(private val sceneWidth: Int, private val sceneHeight: Int) : Scene {

    override fun displayOutput(terminal: AsciiPanel) {
        terminal.write("Greetings stranger", 1, 1)
        terminal.writeCenter("-- press [Enter] to start... --", sceneHeight - 1)
    }

    override fun respondToUserInput(pressedKey: KeyEvent): Scene {
        return if (pressedKey.keyCode == KeyEvent.VK_ENTER) PlayScene(sceneWidth, sceneHeight) else this
    }

}