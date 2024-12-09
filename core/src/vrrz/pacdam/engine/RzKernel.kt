package vrrz.pacdam.engine

import com.badlogic.gdx.Gdx
import vrrz.pacdam.engine.abstractions.DatabaseInterface

class RzKernel private constructor() {
    companion object {
        val kernel: RzKernel = RzKernel()
    }

    val engine: RzEngine = RzEngine.INSTANCE
    private var database: DatabaseInterface? = null

    fun init(databaseController: DatabaseInterface) {
        this.database = databaseController
        println("RzKernel DatabaseInterface Initialized")
    }

    fun create() {
        println("RzKernel - create")
        Gdx.input.inputProcessor = engine.tactilController
        engine.create()


        engine.database = this.database
        /*
        database?.createUser("prueba@gmail.com")
        database?.fetchUserScores("user") { scores ->
            println("Fetched scores: $scores")
        }
        */
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