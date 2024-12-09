package vrrz.pacdam.engine.utils.loaders

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

object SkinLoader {
    val skin: Skin by lazy {
        val atlas = TextureAtlas(Gdx.files.internal("skins/uiskin.atlas"))
        val skin = Skin(atlas)

        // Registrar colores
        skin.add("darkBlue", Color.valueOf("00008BFF"))
        skin.add("lightBlue", Color.valueOf("ADD8E6FF"))
        skin.add("yellow", Color.valueOf("FFFF00FF"))
        skin.add("white", Color.WHITE)
        skin.add("darkGray", Color.valueOf("333333FF"))


        // Registrar fuentes
        skin.add("pacfont", BitmapFont(Gdx.files.internal("skins/pacfont.fnt")))

        val basePixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888).apply {
            setColor(Color.WHITE) // Base blanca para permitir tintado
            fill()
        }
        val baseTexture = Texture(basePixmap)
        skin.add("base", TextureRegion(baseTexture))
        basePixmap.dispose()


        // Cargar el JSON
        skin.load(Gdx.files.internal("skins/uiskin.json"))
        skin
    }
}
