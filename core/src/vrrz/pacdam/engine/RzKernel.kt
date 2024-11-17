package vrrz.pacdam.engine

import com.badlogic.gdx.Gdx

class RzKernel private constructor() {
    companion object {
        val kernel: RzKernel = RzKernel()
    }

    val engine: RzEngine = RzEngine.INSTANCE

    fun create() {
        println("RzKernel - create")
        Gdx.input.inputProcessor = engine.tactilController
        engine.create()
    }


    fun render() {
        engine.render()
    }

    fun resize(width: Int, height: Int) {
        println("RESIZE = W:$width; H:$height")
    }

    fun dispose() {
        engine.dispose()
    }
}