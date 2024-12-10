package vrrz.pacdam.engine.controllers

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Queue
import vrrz.pacdam.engine.RzEngine
import vrrz.pacdam.engine.utils.variables.RzDireccion
import kotlin.math.abs

class RzTactilController() : InputProcessor {
    companion object {
        val INSTANCE: RzTactilController = RzTactilController()
        const val maxDirecciones: Int = 5
    }

    private lateinit var vector: Vector2
    private val direcciones: Queue<RzDireccion?> = Queue(maxDirecciones)
    private lateinit var engine: RzEngine
    private var dragged: Boolean = false
    var direccion: RzDireccion? = null

    fun init(engine: RzEngine) {
        this.engine = engine
    }

    override fun keyDown(keycode: Int): Boolean {
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        return true
    }

    override fun keyTyped(character: Char): Boolean {
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        setVector(x = screenX, y = screenY)
        direcciones.removeAll { true }
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        setVector(x = screenX, y = screenY)
        dragged = direcciones.size == maxDirecciones
        direccion = if (dragged) direcciones.last() else null
        direcciones.removeAll { true }
        if (direccion != null) engine.direccion = direccion as RzDireccion
        return engine.evento(valor = true)
    }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val deltaX: Float = screenX.toFloat() - vector.x
        val deltaY: Float = screenY.toFloat() - vector.y
        val absDeltaX = abs(deltaX)
        val absDeltaY = abs(deltaY)

        newDireccion(
            if (absDeltaX > absDeltaY)
                if (deltaX > 0) RzDireccion.DERECHA
                else RzDireccion.IZQUIERDA
            else
                if (deltaY > 0) RzDireccion.ABAJO
                else RzDireccion.ARRIBA
        )
        // println("touch Dragged")
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return true
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return true
    }

    private fun setVector(x: Int, y: Int) {
        vector = Vector2(x.toFloat(), y.toFloat())
    }

    private fun newDireccion(d: RzDireccion) {
        if (direcciones.size == maxDirecciones) direcciones.removeFirst()
        direcciones.addLast(d)
    }

    fun getVector2() = vector
}