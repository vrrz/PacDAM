package vrrz.pacdam.engine.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import vrrz.pacdam.engine.Ending
import vrrz.pacdam.engine.RzEngine
import vrrz.pacdam.engine.utils.loaders.SkinLoader
import vrrz.pacdam.engine.utils.variables.RzEtapaJuego

class FinalStage(private val engine: RzEngine) {
    private lateinit var stage: Stage

    fun init() {
        stage = Stage(ScreenViewport())
        val skin = SkinLoader.skin // Usar el skin cargado
        val table = Table()
        table.setFillParent(true)

        val buttonPadding = 15F

        val submitButton = TextButton(("VOLVER AL INICIO").uppercase(), skin).apply {
            pad(buttonPadding)
        }

        submitButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                engine.resetAll()
            }
        })

        table.add(submitButton)

        stage.addActor(table)
    }

    var shown = false
    fun show() {
        if (!shown) {
            Gdx.input.inputProcessor = stage // Cambiar InputProcessor al Stage
            shown = true
        }
    }

    fun render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f) // Gris oscuro
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    fun dispose() {
        if (::stage.isInitialized) {
            stage.dispose()
        }
    }
}