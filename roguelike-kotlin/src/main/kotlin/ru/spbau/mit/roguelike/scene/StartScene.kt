package ru.spbau.mit.roguelike.scene

import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

/**
 * Scene that shows the start menu.
 *
 * @property sceneWidth scene width
 * @property sceneHeight scene height
 */
open class StartScene(private val sceneWidth: Int, private val sceneHeight: Int) : Scene {

    /**
     * Displays table of ASCII symbols to console.
     *
     * @param terminal table of ASCII symbols that represents the start menu.
     */
    override fun displayOutput(terminal: AsciiPanel) {
        terminal.write("Greetings stranger", 1, 1)
        terminal.writeCenter("-- press [Enter] to start... --", sceneHeight - 1)
    }

    /**
     * Delegates some action according to what user presses when current scene is active.
     *
     * @param pressedKey key code that user has pressed
     * @return active scene after keypressing action
     */
    override fun respondToUserInput(pressedKey: KeyEvent): Scene {
        return if (pressedKey.keyCode == KeyEvent.VK_ENTER) PlayScene(sceneWidth, sceneHeight) else this
    }

}