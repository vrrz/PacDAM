package vrrz.pacdam.engine.utils.components

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Queue
import vrrz.pacdam.engine.RzEngine
import vrrz.pacdam.engine.utils.variables.RzButtonType
import vrrz.pacdam.engine.utils.variables.RzSize
import java.util.LinkedList

data class RzButton(val type: RzButtonType, val width: Float, val v: Vector2) : Disposable {
    val renderer: ShapeRenderer = ShapeRenderer()
    val src = LinkedList<Disposable>()
    var height = 0F

    init {
        height = width / RzEngine.PHI
        src.add(renderer)
    }

    fun render() {
        when (type) {
            RzButtonType.NORMAL -> {
                renderer.begin(ShapeRenderer.ShapeType.Filled)
                renderer.color = Color.YELLOW
                renderer.rect(v.x, v.y, width , height)
                renderer.end()
            }
        }
    }

    override fun dispose() {
        src.forEach { it.dispose() }
    }
}