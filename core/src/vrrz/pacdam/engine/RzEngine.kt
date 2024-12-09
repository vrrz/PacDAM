package vrrz.pacdam.engine

import com.badlogic.gdx.utils.Disposable
import vrrz.pacdam.engine.abstractions.DatabaseInterface
import vrrz.pacdam.engine.controllers.RzGraphicController
import vrrz.pacdam.engine.controllers.RzTactilController
import vrrz.pacdam.engine.controllers.UserController
import vrrz.pacdam.engine.stages.LoginScene
import vrrz.pacdam.engine.utils.loaders.SkinLoader
import vrrz.pacdam.engine.utils.variables.RzEtapaJuego
import java.util.concurrent.locks.ReentrantLock

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
    val user: UserController = UserController

    private var evento: Boolean = false
    private val eventoLock = ReentrantLock()

    fun create() {
        graphics.create()
        tactilController.init(this)
        loginScene.init()
        SkinLoader.skin
    }

    fun render() {
        when (etapa) {
            RzEtapaJuego.LOGIN -> loginScene()
            RzEtapaJuego.JUEGO -> juego()
        }
    }

    fun loginScene() {
        loginScene.show()
        loginScene.render()
        /*
        if (evento) {
            println(tactilController.direccion)
            evento = false
        } else {
            graphics.pantallaInicio()
        }
         */
    }

    fun juego() {
        graphics.juego()
    }

    override fun dispose() {
        graphics.dispose()
        loginScene.dispose()
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