package vrrz.pacdam.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Disposable
import vrrz.pacdam.engine.abstractions.DatabaseInterface
import vrrz.pacdam.engine.actors.Laberinto
import vrrz.pacdam.engine.controllers.RzGraphicController
import vrrz.pacdam.engine.controllers.RzTactilController
import vrrz.pacdam.engine.controllers.UserController
import vrrz.pacdam.engine.stages.FinalStage
import vrrz.pacdam.engine.stages.LoginScene
import vrrz.pacdam.engine.utils.loaders.SkinLoader
import vrrz.pacdam.engine.utils.variables.RzDireccion
import vrrz.pacdam.engine.utils.variables.RzEtapaJuego
import java.util.concurrent.locks.ReentrantLock

enum class Ending {
    GOOD_ENDING, BAD_ENDING
}

class RzEngine private constructor() : Disposable {
    companion object {
        val INSTANCE = RzEngine()
        const val PHI = 1.618034F
    }

    val graphics: RzGraphicController = RzGraphicController.INSTANCE
    var etapa: RzEtapaJuego = RzEtapaJuego.LOGIN
    val tactilController: RzTactilController = RzTactilController.INSTANCE
    var database: DatabaseInterface? = null
    val loginScene: LoginScene = LoginScene(this)
    val finalStage: FinalStage = FinalStage(this)
    val user: UserController = UserController

    private var evento: Boolean = false
    private val eventoLock = ReentrantLock()

    fun create() {
        graphics.create()
        tactilController.init(this)
        loginScene.init()
        finalStage.init()
        SkinLoader.skin
    }

    fun render() {
        when (etapa) {
            RzEtapaJuego.LOGIN -> loginScene()
            RzEtapaJuego.JUEGO -> juego()
            RzEtapaJuego.FIN -> fin()
        }
    }

    fun loginScene() {
        loginScene.show()
        loginScene.render()
    }

    var vidas = 3
    var tactilInput = false
    var direccion: RzDireccion = RzDireccion.DERECHA
    lateinit var ending: Ending
    fun juego() {
        if (!tactilInput) setTactilInput()
        if (vidas > 0) {
            graphics.renderJuego(direccion) { e ->
                when (e) {
                    0 -> { // PacMan se ha comido todos los puntos
                        etapa = RzEtapaJuego.FIN
                        database?.addScore(user.email ?: "", 1000) { success ->
                            if (success) {
                                println("Todo ha salido correctamente")
                            }
                        }
                    }

                    1 -> {
                        vidas--
                    }
                }
            }
        } else {
            etapa = RzEtapaJuego.FIN
            val puntuacion = Laberinto.calcularPuntuacion()
            database?.addScore(user.email!!, puntuacion) {
                println("Puntuación alñadida")
            }
        }
    }

    fun setTactilInput() {
        Gdx.input.inputProcessor = tactilController
        tactilInput = true
    }

    fun fin() {
        finalStage.show()
        finalStage.render()
    }

    override fun dispose() {
        graphics.dispose()
        loginScene.dispose()
    }

    fun resetAll() {
        vidas = 3
        Gdx.input.inputProcessor = tactilController
        graphics.resetAll()
        etapa = RzEtapaJuego.JUEGO
    }

    fun evento(valor: Boolean?): Boolean {
        eventoLock.lock()
        try {
            if (null != valor) evento = valor
            return evento
        } finally {
            eventoLock.unlock()
        }
    }
}