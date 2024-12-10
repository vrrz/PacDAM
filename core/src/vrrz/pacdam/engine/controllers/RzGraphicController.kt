package vrrz.pacdam.engine.controllers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import vrrz.pacdam.engine.actors.Ghost
import vrrz.pacdam.engine.actors.Laberinto
import vrrz.pacdam.engine.actors.PacMan
import vrrz.pacdam.engine.actors.TipoFantasma
import vrrz.pacdam.engine.utils.loaders.SkinLoader
import vrrz.pacdam.engine.utils.variables.RzDireccion

class RzGraphicController private constructor() {
    companion object {
        val INSTANCE = RzGraphicController()
        val fillAll: (Color, Float) -> Unit = { c, a ->
            Gdx.gl.glClearColor(c.r, c.g, c.b, a)
        }
        val tamFrame = 15F
        fun scale() = (Gdx.graphics.width / Laberinto.laberinto[0].size.toFloat()) / tamFrame
    }

    fun clear() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f) // Color negro de fondo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT or GL20.GL_STENCIL_BUFFER_BIT)
        Gdx.input.setOnscreenKeyboardVisible(false)
    }

    var pacman: PacMan? = null
    var blinky: Ghost? = null
    private var eventos: List<() -> Boolean> = listOf(
        {
            Laberinto.pacdots.all { i -> i.all { pacdot -> pacdot?.comido ?: true } }
        },
        {
            blinky?.interactuarConPacMan(pacman!!) ?: false
        }
    )
    lateinit var batch: SpriteBatch
    lateinit var backgroundLaberinto: Texture
    var screenWidth: Float = 0F
    var screenHeight: Float = 0F
    var textureWidth = 0F
    var textureHeight = 0F
    var scaleLaberinto = 0F
    var scaledHeight = 0F
    fun create() {
        val skin = SkinLoader.skin
        batch = SpriteBatch()
        backgroundLaberinto = Texture(Gdx.files.internal("skins/laberinto.png"))
        screenWidth = Gdx.graphics.width.toFloat()
        screenHeight = Gdx.graphics.height.toFloat()
        textureWidth = backgroundLaberinto.width.toFloat()
        textureHeight = backgroundLaberinto.height.toFloat()
        scaleLaberinto = screenWidth / textureWidth
        scaledHeight = textureHeight * scaleLaberinto

        pacman = PacMan(skin.atlas)
        blinky = Ghost(skin.atlas, TipoFantasma.BLINKY, velocidadCasillasPorSegundo = 5F)
    }

    fun renderJuego(pacmanDirection: RzDireccion, lambda: (Int) -> Unit) {
        direccion = pacmanDirection
        printJuego()
        for ((i, event) in eventos.withIndex()) {
            if (event()) {
                lambda(i)
                Thread.sleep(2000)
                pacman?.start()
                blinky?.start()
                break
            }
        }
        lambda(-1)
    }

    var direccion: RzDireccion = RzDireccion.DERECHA
    fun printJuego() {
        clear()
        batch.begin()
        printBackground()
        pacman?.render(batch, direccion)
        blinky?.render(batch, pacman!!)
        Laberinto.render(batch, pacman!!)
        batch.end()
    }

    fun printBackground() {
        batch.draw(
            backgroundLaberinto,
            0f, // La imagen empieza en el borde izquierdo
            (screenHeight - scaledHeight) / 2, // Centrar verticalmente si es necesario
            screenWidth, // El ancho escalado para llenar toda la pantalla
            scaledHeight // El alto escalado manteniendo la proporción
        )
    }

    fun resetAll() {
        Laberinto.resetPacdots()
    }

    fun dispose() {
        batch.dispose()
        backgroundLaberinto.dispose()
    }

}