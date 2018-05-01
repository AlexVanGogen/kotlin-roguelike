package ru.spbau.mit.roguelike.scene

import asciiPanel.AsciiPanel
import java.awt.event.KeyEvent

interface Scene {

    fun displayOutput(terminal: AsciiPanel)
    fun respondToUserInput(pressedKey: KeyEvent): Scene
}