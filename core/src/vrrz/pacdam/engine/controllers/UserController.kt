package vrrz.pacdam.engine.controllers

data class UserScore(val timestamp: Long, val score: Int)

object UserController {
    var email: String? = null

    private val scores = mutableListOf<UserScore>()

    fun addScore(score: Int) {
        scores.add(UserScore(System.currentTimeMillis(), score))
    }

    fun getScores(): List<UserScore> {
        return scores
    }

    fun clear() {
        email = null
        scores.clear()
    }
}
