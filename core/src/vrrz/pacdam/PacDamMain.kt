package vrrz.pacdam

import com.badlogic.gdx.ApplicationAdapter
import vrrz.pacdam.engine.RzKernel
import vrrz.pacdam.engine.abstractions.DatabaseInterface

class PacDamMain(private val databaseController: DatabaseInterface) : ApplicationAdapter() {
    private val kernel = RzKernel.kernel

    override fun create() {
        kernel.init(databaseController)
        kernel.create()
    }

    override fun render() {
        kernel.render()
    }

    override fun resize(width: Int, height: Int) {
        kernel.resize(width, height)
    }

    override fun dispose() {
        kernel.dispose()
    }
}