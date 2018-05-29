package ru.spbau.mit.roguelike.scene

import asciiPanel.AsciiPanel
import org.apache.logging.log4j.LogManager
import java.awt.event.KeyEvent

/**
 * Scene that shows messages when playes has won.
 *
 * @property sceneWidth scene width
 * @property sceneHeight scene height
 */
class WinScene(private val sceneWidth: Int, private val sceneHeight: Int) : Scene {

    private val winSceneLogger = LogManager.getLogger(WinScene::class.java)

    /**
     * Displays table of ASCII symbols to console.
     *
     * @param terminal table of ASCII symbols that represents the world after winning
     */
    override fun displayOutput(terminal: AsciiPanel) {
        winSceneLogger.info("Player has won")
        terminal.write("*", sceneWidth / 2 - 2, sceneHeight / 2, AsciiPanel.brightRed)
        terminal.write("*", sceneWidth / 2 - 1, sceneHeight / 2, AsciiPanel.brightBlue)
        terminal.write("*", sceneWidth / 2, sceneHeight / 2, AsciiPanel.brightWhite)
        terminal.write("*", sceneWidth / 2 + 1, sceneHeight / 2, AsciiPanel.brightGreen)
        terminal.write("*", sceneWidth / 2 + 2, sceneHeight / 2, AsciiPanel.brightCyan)
        terminal.writeCenter("You won.", 1 + sceneHeight / 2);
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
