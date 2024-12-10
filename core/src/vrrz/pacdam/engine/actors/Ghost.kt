package vrrz.pacdam.engine.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import vrrz.pacdam.engine.controllers.RzGraphicController
import vrrz.pacdam.engine.utils.variables.RzDireccion
import kotlin.math.abs

enum class TipoFantasma {
    BLINKY {
        override fun baseName() = "bly"
    },
    PINKY {
        override fun baseName() = "pyn"
    },
    INKY {
        override fun baseName() = "ink"
    },
    CLYDE {
        override fun baseName() = "cly"
    };

    abstract fun baseName(): String
}

enum class Modo {
    PASIVO,
    CAZA
}

class Ghost(
    val atlas: TextureAtlas,
    val type: TipoFantasma,
    val frameDuration: Float = 0.2F,
    val velocidadCasillasPorSegundo: Float = 2F
) {
    var x: Int = 13 // Posición lógica inicial
    var y: Int = 11
    private var realX: Float = Laberinto.x(x)
    private var realY: Float = Laberinto.y(y)

    private var destinoX: Float = realX
    private var destinoY: Float = realY

    var direccion: RzDireccion = RzDireccion.DERECHA

    private lateinit var animation: Animation<TextureRegion>
    private val frames: Map<RzDireccion, Array<TextureRegion>> = mapOf(
        RzDireccion.DERECHA to arrayOf(
            atlas.findRegion("${type.baseName()}_r0"), atlas.findRegion("${type.baseName()}_r1")
        ), RzDireccion.IZQUIERDA to arrayOf(
            atlas.findRegion("${type.baseName()}_l0"), atlas.findRegion("${type.baseName()}_l1")
        ), RzDireccion.ABAJO to arrayOf(
            atlas.findRegion("${type.baseName()}_d0"), atlas.findRegion("${type.baseName()}_d1")
        ), RzDireccion.ARRIBA to arrayOf(
            atlas.findRegion("${type.baseName()}_u0"), atlas.findRegion("${type.baseName()}_u1")
        )
    )

    init {
        cambiarDireccion(direccion)
    }

    private fun cambiarDireccion(nuevaDireccion: RzDireccion) {
        direccion = nuevaDireccion
        val currentFrames = frames[direccion]
        if (currentFrames != null) {
            animation = Animation(frameDuration, *currentFrames)
        }
    }

    private fun puedeMoverEnDireccion(direccion: RzDireccion): Boolean {
        return when (direccion) {
            RzDireccion.DERECHA -> Laberinto.laberinto[y][x + 1] == 1
            RzDireccion.IZQUIERDA -> Laberinto.laberinto[y][x - 1] == 1
            RzDireccion.ARRIBA -> Laberinto.laberinto[y - 1][x] == 1
            RzDireccion.ABAJO -> Laberinto.laberinto[y + 1][x] == 1
        }
    }

    fun mover(pacMan: PacMan) {
        val velocidadPixeles =
            RzGraphicController.scale() * RzGraphicController.tamFrame * velocidadCasillasPorSegundo * Gdx.graphics.deltaTime

        val dx = destinoX - realX
        val dy = destinoY - realY
        val distancia = kotlin.math.sqrt(dx * dx + dy * dy)

        if (distancia <= velocidadPixeles) {
            // Llega al nodo
            realX = destinoX
            realY = destinoY
            avanzarCoordenadaLogica()
            cambiarDireccion(calcularDireccion(pacMan))
            calcularDestino()
        } else {
            // Movimiento suave hacia el destino
            val factor = velocidadPixeles / distancia
            realX += dx * factor
            realY += dy * factor
        }
    }

    private fun avanzarCoordenadaLogica() {
        when (direccion) {
            RzDireccion.DERECHA -> x += 1
            RzDireccion.IZQUIERDA -> x -= 1
            RzDireccion.ABAJO -> y += 1
            RzDireccion.ARRIBA -> y -= 1
        }
    }

    private fun calcularDestino() {
        destinoX = when (direccion) {
            RzDireccion.DERECHA -> Laberinto.x(x + 1)
            RzDireccion.IZQUIERDA -> Laberinto.x(x - 1)
            else -> realX
        }
        destinoY = when (direccion) {
            RzDireccion.ABAJO -> Laberinto.y(y + 1)
            RzDireccion.ARRIBA -> Laberinto.y(y - 1)
            else -> realY
        }
    }

    fun calcularDireccion(pacMan: PacMan): RzDireccion {
        val izquierda = x > pacMan.x
        val arriba = y > pacMan.y
        val absx = abs(x - pacMan.x) > abs(y - pacMan.y)

        return if (absx) {
            if (izquierda && puedeMoverEnDireccion(RzDireccion.IZQUIERDA)) RzDireccion.IZQUIERDA
            else if (!izquierda && puedeMoverEnDireccion(RzDireccion.DERECHA)) RzDireccion.DERECHA
            else if (arriba) RzDireccion.ARRIBA else RzDireccion.ABAJO
        } else {
            if (arriba && puedeMoverEnDireccion(RzDireccion.ARRIBA)) RzDireccion.ARRIBA
            else if (!arriba && puedeMoverEnDireccion(RzDireccion.ABAJO)) RzDireccion.ABAJO
            else if (izquierda) RzDireccion.IZQUIERDA else RzDireccion.DERECHA
        }
    }

    fun start() {
        x = 13
        y = 11
        realX = Laberinto.x(x) // Actualizamos las coordenadas reales
        realY = Laberinto.y(y) // Basándonos en las posiciones lógicas
        destinoX = realX       // Reseteamos el destino a la posición inicial
        destinoY = realY       // Reseteamos el destino a la posición inicial
        direccion = RzDireccion.DERECHA // Reestablecemos la dirección inicial
    }

    fun render(batch: SpriteBatch, pacMan: PacMan) {
        mover(pacMan)
        val tamReal = RzGraphicController.scale() * RzGraphicController.tamFrame
        batch.draw(
            animation.getKeyFrame(Gdx.graphics.deltaTime, true), realX, realY, tamReal, tamReal
        )
    }

    fun interactuarConPacMan(pacMan: PacMan): Boolean {
        return x == pacMan.x && y == pacMan.y
    }
}
