package vrrz.pacdam.engine.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import vrrz.pacdam.engine.controllers.RzGraphicController
import vrrz.pacdam.engine.utils.variables.RzDireccion

enum class Estado {
    MOVIENDO,
    CASILLA
}

class PacMan(
    val atlas: TextureAtlas,
    val frameDuration: Float = 0.20F,
    val velocidadCasillasPorSegundo: Float = 5F
) {
    var estado: Estado = Estado.MOVIENDO
    var direccion: RzDireccion = RzDireccion.DERECHA
    var direccionDeseada: RzDireccion = RzDireccion.DERECHA
    var x: Int = 13 // Posición lógica en la matriz
    var y: Int = 23
    private var realX: Float = Laberinto.x(x)
    private var realY: Float = Laberinto.y(y)
    fun start() {
        x = 13
        y = 23
        realX = Laberinto.x(x)
        realY = Laberinto.y(y)
        estado = Estado.CASILLA
        direccion = RzDireccion.DERECHA
    }

    private lateinit var animation: Animation<TextureRegion>

    private val frames: Map<RzDireccion, Array<TextureRegion>> = mapOf(
        RzDireccion.DERECHA to arrayOf(
            atlas.findRegion("pac_o"),
            atlas.findRegion("pac_r1"),
            atlas.findRegion("pac_r0"),
            atlas.findRegion("pac_r1")
        ),
        RzDireccion.IZQUIERDA to arrayOf(
            atlas.findRegion("pac_o"),
            atlas.findRegion("pac_l1"),
            atlas.findRegion("pac_l0"),
            atlas.findRegion("pac_l1")
        ),
        RzDireccion.ABAJO to arrayOf(
            atlas.findRegion("pac_o"),
            atlas.findRegion("pac_d1"),
            atlas.findRegion("pac_d0"),
            atlas.findRegion("pac_d1")
        ),
        RzDireccion.ARRIBA to arrayOf(
            atlas.findRegion("pac_o"),
            atlas.findRegion("pac_u1"),
            atlas.findRegion("pac_u0"),
            atlas.findRegion("pac_u1")
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

    /*
        fun mover() {
            if (estado == Estado.CASILLA) {
                cambiarDireccion(direccionDeseada)
                moverCoordenadas()
                realX = Laberinto.x(x)
                realY = Laberinto.y(y)
            } else {
                if (puedeMoverEnDireccion(direccion)) {
                    val pix =
                        RzGraphicController.scale() * RzGraphicController.tamFrame * velocidadCasillasPorSegundo
                    val parte = pix * (stateTime % 1F)
                    when (direccion) {
                        RzDireccion.ARRIBA -> realY = Laberinto.y(y) + parte
                        RzDireccion.ABAJO -> realY = Laberinto.y(y) - parte
                        RzDireccion.DERECHA -> realX = Laberinto.x(x) + parte
                        RzDireccion.IZQUIERDA -> realX = Laberinto.x(x) - parte
                    }
                }
            }
            estado = Estado.MOVIENDO
        }

        var coordenadaMovida: Int = 0
        fun moverCoordenadas() {
            if (stateTime.toInt() != coordenadaMovida) {
                moverCoordenadas()
                coordenadaMovida = stateTime.toInt()
                when (direccion) {
                    RzDireccion.ARRIBA -> {
                        if (puedeMoverEnDireccion(RzDireccion.ARRIBA)) y -= velocidadCasillasPorSegundo.toInt()
                    }

                    RzDireccion.ABAJO -> {
                        if (puedeMoverEnDireccion(RzDireccion.ABAJO)) y += velocidadCasillasPorSegundo.toInt()
                    }

                    RzDireccion.DERECHA -> {
                        if (puedeMoverEnDireccion(RzDireccion.DERECHA)) x += velocidadCasillasPorSegundo.toInt()
                    }

                    RzDireccion.IZQUIERDA -> {
                        if (puedeMoverEnDireccion(RzDireccion.IZQUIERDA)) x -= velocidadCasillasPorSegundo.toInt()
                    }
                }
            }
        }
    */
    fun mover() {
        val velocidadPixeles =
            RzGraphicController.scale() * RzGraphicController.tamFrame * velocidadCasillasPorSegundo * Gdx.graphics.deltaTime

        if (estado == Estado.CASILLA) {
            // Cambiar dirección si es posible
            if (direccionDeseada != direccion && puedeMoverEnDireccion(direccionDeseada)) {
                cambiarDireccion(direccionDeseada)
            }

            if (puedeMoverEnDireccion(direccion)) {
                estado = Estado.MOVIENDO
                calcularDestino()
            }
        }

        if (estado == Estado.MOVIENDO) {
            val dx = destinoX - realX
            val dy = destinoY - realY
            val distancia = kotlin.math.sqrt(dx * dx + dy * dy)

            if (distancia <= velocidadPixeles) {
                // Llega a la casilla destino
                realX = destinoX
                realY = destinoY
                avanzarCoordenadaLogica()
                estado = Estado.CASILLA
            } else {
                // Movimiento suave hacia el destino
                val factor = velocidadPixeles / distancia
                realX += dx * factor
                realY += dy * factor
            }
        }
    }

    private var destinoX: Float = realX
    private var destinoY: Float = realY

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

    private fun avanzarCoordenadaLogica() {
        when (direccion) {
            RzDireccion.DERECHA -> x += 1
            RzDireccion.IZQUIERDA -> x -= 1
            RzDireccion.ABAJO -> y += 1
            RzDireccion.ARRIBA -> y -= 1
        }
    }

    var stateTime = 0F
    fun render(batch: SpriteBatch, direccion: RzDireccion) {
        direccionDeseada = direccion
        stateTime += Gdx.graphics.deltaTime
        if ((stateTime * 10 % 10).toInt() == 0) {
            estado = Estado.CASILLA
        }
        mover()
        print(batch)
    }

    fun print(batch: SpriteBatch) {
        val tamReal = RzGraphicController.scale() * RzGraphicController.tamFrame
        batch.draw(
            animation.getKeyFrame(stateTime, true),
            realX,
            realY,
            tamReal,
            tamReal
        )
    }
}
