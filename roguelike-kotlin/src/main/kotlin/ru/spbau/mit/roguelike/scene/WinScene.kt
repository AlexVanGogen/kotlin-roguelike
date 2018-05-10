package ru.spbau.mit.roguelike.scene

import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

class WinScene(private val sceneWidth: Int, private val sceneHeight: Int) : Scene {

    override fun displayOutput(terminal: AsciiPanel) {
        terminal.write("*", sceneWidth / 2 - 2, sceneHeight / 2, AsciiPanel.brightRed)
        terminal.write("*", sceneWidth / 2 - 1, sceneHeight / 2, AsciiPanel.brightBlue)
        terminal.write("*", sceneWidth / 2, sceneHeight / 2, AsciiPanel.brightWhite)
        terminal.write("*", sceneWidth / 2 + 1, sceneHeight / 2, AsciiPanel.brightGreen)
        terminal.write("*", sceneWidth / 2 + 2, sceneHeight / 2, AsciiPanel.brightCyan)
        terminal.writeCenter("You won.", 1 + sceneHeight / 2);
        terminal.writeCenter("-- press [Enter] to restart... --", sceneHeight - 1);
    }

    override fun respondToUserInput(pressedKey: KeyEvent): Scene {
        return if (pressedKey.getKeyCode() == KeyEvent.VK_ENTER) PlayScene(sceneWidth, sceneHeight) else this
    }
}
