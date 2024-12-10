package vrrz.pacdam.engine.abstractions

import vrrz.pacdam.engine.utils.variables.Score

interface DatabaseInterface {
    // fun createUser(email: String, passwordHash: String, username: String)

    fun createUser(email: String, callback: (Boolean) -> Unit)
    fun addScore(email: String, score: Int, callback: (Boolean) -> Unit)
    fun fetchUserScores(userId: String, callback: (List<Score>) -> Unit)

    /**
     * Comprueba si existe el usuario a través del email y devuelve la puntuación enc aso de que exista
     */
    fun checkUserExists(email: String, callback: (Boolean) -> Unit)
    // fun checkUserExists(email: String, callback: Function1<Boolean, Unit>)
}