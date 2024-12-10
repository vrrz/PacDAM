package vrrz.pacdam.engine.actors

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas

/**
 * @param atlas
 * @param x - La posición x (columna) en el laberinto
 * @param y - La posición y (fila) en el laberinto
 */
class PacDot(val atlas: TextureAtlas, val x: Int, val y: Int ) {
    val sprite: Sprite = Sprite(atlas.findRegion("pacdot"))
    var comido: Boolean = false

    fun render(batch: SpriteBatch, pacMan: PacMan) {
        if (pacMan.x == x && pacMan.y == y) {
            comido = true
        }
        if (!comido) {
            batch.draw(sprite, Laberinto.x(x), Laberinto.y(y))
        }
    }
}