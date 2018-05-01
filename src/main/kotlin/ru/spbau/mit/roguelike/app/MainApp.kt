package ru.spbau.mit.roguelike.app

import javax.swing.JFrame
import asciiPanel.AsciiPanel
import ru.spbau.mit.roguelike.scene.Scene
import ru.spbau.mit.roguelike.scene.StartScene
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class MainApp: JFrame(), KeyListener {

    private val MAIN_WINDOW_WIDTH = 1080
    private val MAIN_WINDOW_HEIGHT = 800
    private val PANEL_WIDTH_PROPORTION = 9
    private val PANEL_HEIGHT_PROPORTION = 16

    private val terminal: AsciiPanel = AsciiPanel(
            MAIN_WINDOW_WIDTH / PANEL_WIDTH_PROPORTION,
            MAIN_WINDOW_HEIGHT / PANEL_HEIGHT_PROPORTION
    )

    private var currentScene: Scene

    init {
        add(terminal)
        pack()

        currentScene = StartScene(MAIN_WINDOW_WIDTH / PANEL_WIDTH_PROPORTION, MAIN_WINDOW_HEIGHT / PANEL_HEIGHT_PROPORTION)
        addKeyListener(this)
        repaint()
    }

    override fun keyTyped(e: KeyEvent?) { }

    override fun keyPressed(e: KeyEvent?) {
        if (e != null) {
            currentScene = currentScene.respondToUserInput(e)
        }
        repaint()
    }

    override fun keyReleased(e: KeyEvent?) {
    }

    override fun repaint() {
        terminal.clear()
        currentScene.displayOutput(terminal)
        super.repaint()
    }
}

fun main(args: Array<String>) {
    val app = MainApp()
    app.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    app.isVisible = true
}