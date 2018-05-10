package ru.spbau.mit.roguelike.scene

import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

/**
 * Scene that shows messages when playes has lost.
 *
 * @property sceneWidth scene width
 * @property sceneHeight scene height
 */
class LoseScene(private val sceneWidth: Int, private val sceneHeight: Int) : Scene {

    /**
     * Displays table of ASCII symbols to console.
     *
     * @param terminal table of ASCII symbols that represents the world after loosing.
     */
    override fun displayOutput(terminal: AsciiPanel) {
        terminal.write("You lost.", 1, 1);
        terminal.writeCenter("-- press [Enter] to restart... --", sceneHeight - 1);
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
