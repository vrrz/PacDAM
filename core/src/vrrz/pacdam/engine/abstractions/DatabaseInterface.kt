package vrrz.pacdam.engine.abstractions

import vrrz.pacdam.engine.utils.variables.Pair

interface DatabaseInterface {
    fun saveScore(playerName: String, score: Int)
    fun getTopScores(callback: ScoreCallback)

    interface ScoreCallback {
        fun onCallback(scores: List<Pair<String, Int>>)
    }
}