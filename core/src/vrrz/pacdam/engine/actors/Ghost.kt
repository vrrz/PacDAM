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

enum class Posicion {
    NODO,
    ARISTA
}

class Ghost(val atlas: TextureAtlas, val type: TipoFantasma, val frameDuration: Float) {
    val frames: Map<RzDireccion, Array<TextureRegion>> = mapOf(
        /*RzDireccion.DERECHA to listOf(
            Sprite(atlas.findRegion("${type.name}_r0")),
            Sprite(atlas.findRegion("${type.name}_r1"))
        ),
        RzDireccion.IZQUIERDA to listOf(
            Sprite(atlas.findRegion("${type.name}_l0")),
            Sprite(atlas.findRegion("${type.name}_l1"))
        ),
        RzDireccion.ABAJO to listOf(
            Sprite(atlas.findRegion("${type.name}_d0")),
            Sprite(atlas.findRegion("${type.name}_d1"))
        ),
        RzDireccion.ARRIBA to listOf(
            Sprite(atlas.findRegion("${type.name}_u0")),
            Sprite(atlas.findRegion("${type.name}_u1"))
        ),*/
        RzDireccion.DERECHA to arrayOf(
            atlas.findRegion("${type.baseName()}_r0"),
            atlas.findRegion("${type.baseName()}_r1")
        ),
        RzDireccion.IZQUIERDA to arrayOf(
            atlas.findRegion("${type.baseName()}_l0"),
            atlas.findRegion("${type.baseName()}_l1")
        ),
        RzDireccion.ABAJO to arrayOf(
            atlas.findRegion("${type.baseName()}_d0"),
            atlas.findRegion("${type.baseName()}_d1")
        ),
        RzDireccion.ARRIBA to arrayOf(
            atlas.findRegion("${type.baseName()}_u0"),
            atlas.findRegion("${type.baseName()}_u1")
        ),
    )

    var x: Int = 13
    var y: Int = 11
    val nCasillasSeg: Int = 2

    lateinit var animation: Animation<TextureRegion>
    var direccion: RzDireccion = RzDireccion.DERECHA
    fun changeDirection(direccion: RzDireccion) {
        this.direccion = direccion
        val currentFrames = frames[direccion]
        if (currentFrames != null) animation = Animation(frameDuration, *currentFrames)

        calcularDestino()
    }


    private var stateTime: Float = 0F
    private var realX: Float = Laberinto.x(x)
    private var realY: Float = Laberinto.y(y)
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

    private var destinoX: Float = Laberinto.x(x + 1) // Coordenada destino inicial
    private var destinoY: Float = realY // Coordenada destino inicial
    private fun calcularDestino() {
        when (direccion) {
            RzDireccion.DERECHA -> {
                destinoX = Laberinto.x(x + 1)
                destinoY = realY
            }
            RzDireccion.IZQUIERDA -> {
                destinoX = Laberinto.x(x - 1)
                destinoY = realY
            }
            RzDireccion.ARRIBA -> {
                destinoY = Laberinto.y(y - 1)
                destinoX = realX
            }
            RzDireccion.ABAJO -> {
                destinoY = Laberinto.y(y + 1)
                destinoX = realX
            }
        }
    }
    fun render(batch: SpriteBatch, pacMan: PacMan) {
        stateTime += Gdx.graphics.deltaTime
        mover(Gdx.graphics.deltaTime)
        print(batch)
        if (posicion == Posicion.NODO) changeDirection(calcularDireccion(pacMan))
    }

    fun mover(deltaTime: Float) {
        if (posicion == Posicion.ARISTA) {
            // Calcular el movimiento en píxeles por frame
            val velocidadPixeles = nCasillasSeg * RzGraphicController.tamFrame * deltaTime

            // Desplazamiento incremental hacia el destino
            val dx = destinoX - realX
            val dy = destinoY - realY
            val distancia = kotlin.math.sqrt(dx * dx + dy * dy)

            if (distancia <= velocidadPixeles) {
                // Hemos alcanzado o superado el destino
                realX = destinoX
                realY = destinoY

                // Actualizar coordenadas lógicas al llegar al nodo
                when (direccion) {
                    RzDireccion.DERECHA -> x += 1
                    RzDireccion.IZQUIERDA -> x -= 1
                    RzDireccion.ARRIBA -> y -= 1
                    RzDireccion.ABAJO -> y += 1
                }

                // Recalcular el siguiente destino
                calcularDestino()
            } else {
                // Movimiento proporcional hacia el destino
                val factor = velocidadPixeles / distancia
                realX += dx * factor
                realY += dy * factor
            }
        }
    }

    val posicion: Posicion = Posicion.ARISTA
    fun calcularDireccion(pacMan: PacMan): RzDireccion {
        val izquierda: Boolean = x > pacMan.x
        val arriba: Boolean = y > pacMan.y
        val absx: Boolean = abs(x - pacMan.x) > abs(y - pacMan.y)
        if (absx) { // Hay mayor distancia en la X
            if (izquierda) {
                if (puedeDireccion(RzDireccion.IZQUIERDA)) return RzDireccion.IZQUIERDA
                else {
                    if (arriba) {
                        if (puedeDireccion(RzDireccion.ARRIBA)) return RzDireccion.ARRIBA
                        else return RzDireccion.ABAJO
                    } else return RzDireccion.DERECHA
                }
            } else {
                if (puedeDireccion(RzDireccion.DERECHA)) return RzDireccion.DERECHA
                else {
                    if (arriba) {
                        if (puedeDireccion(RzDireccion.ARRIBA)) return RzDireccion.ARRIBA
                        else return RzDireccion.ABAJO
                    } else return RzDireccion.IZQUIERDA
                }
            }
        } else {
            if (arriba) {
                if (puedeDireccion(RzDireccion.ARRIBA)) return RzDireccion.ARRIBA
                else {
                    if (izquierda) {
                        if (puedeDireccion(RzDireccion.IZQUIERDA)) return RzDireccion.IZQUIERDA
                        else return RzDireccion.DERECHA
                    } else return RzDireccion.ABAJO
                }
            } else {
                if (puedeDireccion(RzDireccion.ABAJO)) return RzDireccion.ABAJO
                else {
                    if (izquierda) {
                        if (puedeDireccion(RzDireccion.IZQUIERDA)) return RzDireccion.IZQUIERDA
                        else return RzDireccion.DERECHA
                    } else return RzDireccion.ARRIBA
                }
            }
        }
    }

    fun puedeDireccion(dir: RzDireccion): Boolean = when (dir) {
        RzDireccion.ARRIBA -> Laberinto.laberinto[y - 1][x]
        RzDireccion.ABAJO -> Laberinto.laberinto[y + 1][x]
        RzDireccion.DERECHA -> Laberinto.laberinto[y][x + 1]
        RzDireccion.IZQUIERDA -> Laberinto.laberinto[y][x - 1]
    } == 1
}