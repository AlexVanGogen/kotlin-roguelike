package ru.spbau.mit.roguelike.scene

import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

class WinScene(private val sceneWidth: Int, private val sceneHeight: Int) : Scene {

    override fun displayOutput(terminal: AsciiPanel) {
        terminal.write("You won.", 1, 1);
        terminal.writeCenter("-- press [Enter] to restart... --", sceneHeight - 1);
    }

    override fun respondToUserInput(pressedKey: KeyEvent): Scene {
        return if (pressedKey.getKeyCode() == KeyEvent.VK_ENTER) PlayScene(sceneWidth, sceneHeight) else this
    }

}
