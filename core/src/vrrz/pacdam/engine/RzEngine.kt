package vrrz.pacdam.engine

import com.badlogic.gdx.utils.Disposable
import vrrz.pacdam.engine.controllers.RzGraphicController
import vrrz.pacdam.engine.controllers.RzTactilController
import vrrz.pacdam.engine.utils.variables.RzEtapaJuego
import java.util.concurrent.locks.ReentrantLock

class RzEngine private constructor() : Disposable {
    companion object {
        val INSTANCE = RzEngine()
        const val PHI = 1.618034F
    }

    val graphics: RzGraphicController = RzGraphicController.INSTANCE
    var etapa: RzEtapaJuego = RzEtapaJuego.PANTALLA_INICIO
    val tactilController: RzTactilController = RzTactilController.INSTANCE

    private var evento: Boolean = false
    private val eventoLock = ReentrantLock()

    fun create() {
        graphics.create()
        tactilController.init(this)
    }

    fun render() {
        println()
        when (etapa) {
            RzEtapaJuego.PANTALLA_INICIO -> pantallaInicio()

            RzEtapaJuego.PANTALLA_CARGA -> TODO()
        }

    }

    fun pantallaInicio() {
        if (evento) {
            println(tactilController.direccion)
            evento = false
        } else {
            graphics.pantallaInicio()
        }
    }

    override fun dispose() {
        graphics.dispose()
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