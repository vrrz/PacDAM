package vrrz.pacdam.engine.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.scenes.scene2d.InputEvent
import vrrz.pacdam.engine.RzEngine
import vrrz.pacdam.engine.utils.loaders.SkinLoader
import vrrz.pacdam.engine.utils.variables.RzEtapaJuego

class LoginScene(private val engine: RzEngine) {
    private lateinit var stage: Stage

    fun init() {
        stage = Stage(ScreenViewport())
        val skin = SkinLoader.skin // Usar el skin cargado
        val table = Table()
        table.setFillParent(true)

        val screenWidth = Gdx.graphics.width.toFloat()
        val textFieldWidth = screenWidth / RzEngine.PHI
        val textFieldHeight = 60F
        val buttonPadding = 15F

        val label = Label(("Introduce tu email").uppercase(), skin, "large")
        val emailField = TextField("", skin).apply {
            setSize(textFieldWidth, textFieldHeight)
        }
        val submitButton = TextButton(("Enviar").uppercase(), skin).apply {
            pad(buttonPadding)
        }

        submitButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val email = emailField.text
                if (email.isNotEmpty()) {
                    engine.database?.checkUserExists(email) { exists ->
                        if (exists) {
                            println("Email $email existe")
                        } else {
                            engine.database?.createUser(email) { success ->
                                println("Usuario $email " + if (success) "creado" else "no pudo ser creado")
                            }
                        }
                        engine.user.email = email
                        engine.etapa = RzEtapaJuego.JUEGO
                    }
                } else {
                    println("El campo de email está vacío")
                }
            }
        })

        table.add(label).padBottom(20f).row()
        table.add(emailField).width(textFieldWidth).height(textFieldHeight).padBottom(20f).row()
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
