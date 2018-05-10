package ru.spbau.mit.roguelike.scene

import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

/**
 * Main interface for action windows displaying.
 * Window is considered to be an AsciiPanel instance,
 * so every scene as a table of ASCII symbols.
 */
interface Scene {

    /**
     * Displays table of ASCII symbols to console.
     *
     * @param terminal table of ASCII symbols that represents the world
     */
    fun displayOutput(terminal: AsciiPanel)

    /**
     * Delegates some action according to what user presses when current scene is active.
     *
     * @param pressedKey key code that user has pressed
     * @return active scene after keypressing action
     */
    fun respondToUserInput(pressedKey: KeyEvent): Scene?
}