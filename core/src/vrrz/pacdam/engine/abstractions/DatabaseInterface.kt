package vrrz.pacdam.engine.abstractions

import vrrz.pacdam.engine.utils.variables.Score

interface DatabaseInterface {
    fun createUser(email: String, passwordHash: String, username: String)
    fun addScore(userId: String, levelId: Int, score: Int)
    fun fetchUserScores(userId: String, callback: (List<Score>) -> Unit)
}