package vrrz.pacdam.engine.controllers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Disposable
import vrrz.pacdam.engine.utils.components.RzButton
import java.util.LinkedList


class RzGraphicController private constructor() {
    companion object {
        val INSTANCE = RzGraphicController()
        val fillAll: (Color, Float) -> Unit = { c, a ->
            Gdx.gl.glClearColor(c.r, c.g, c.b, a)
        }
    }

    fun clear() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT) // Limpia el Buffer de la gráfica
    }

    fun pantallaCarga() {
        /* clear()
         if (src.isEmpty) {
             val loadingTexture = Texture("rick.gif")
             src.add(loadingTexture)
         }
         Gdx.gl.glClearColor(161F / 256, 69F / 256, 95F / 256, 1F)

         batch.begin()
         src[0].let { loadingTexture -> // Texture rick.gif
             (loadingTexture as Texture).let {
                 batch.draw(
                     loadingTexture,
                     (Gdx.graphics.width - loadingTexture.width) / 2F,
                     (Gdx.graphics.height - loadingTexture.height) / 2F
                 )
             }
         }
         batch.end()*/
    }


    var button1: RzButton? = null
    var batch: SpriteBatch? = null
    var texture1: Texture? = null
    var animation: Animation<TextureRegion>? = null
    var stateTime = 0f
    val scale = 10f
    val tamFrame = 16
    val frameDuration = 0.4f

    fun create() {
        batch = SpriteBatch()
        texture1 = Texture(Gdx.files.internal("spritessheets/pacman-sprites.png"))
        val textureRegionPacman = TextureRegion.split(texture1, tamFrame, tamFrame)

        val firstRowFrames = mutableListOf<TextureRegion>()
        for (i in 0 until 3) firstRowFrames.add(textureRegionPacman[0][i])
        //val frames = textureRegionPacman.flatten().toTypedArray()
        val frames = firstRowFrames.toTypedArray()

        animation = Animation(frameDuration, *frames)
        stateTime = 0f

    }

    fun pantallaInicio() {
        clear()
        //fillAll(Color.SKY, 1F)
        //button1!!.render()
        stateTime += Gdx.graphics.deltaTime
        println()

        val currentFrame = animation?.getKeyFrame(stateTime, true)
        batch?.begin()
        batch?.draw(
            currentFrame,
            0f,
            0f,
            currentFrame!!.regionWidth.times(scale),
            currentFrame.regionHeight.times(scale)
        )
        batch?.end()
    }

    fun dispose() {
        batch?.dispose()
        texture1?.dispose()

    }
}