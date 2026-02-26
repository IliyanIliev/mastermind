package com.iliyan.mastermind.data.repository

import com.iliyan.mastermind.domain.model.GameRules.GAME_LENGTH
import com.iliyan.mastermind.domain.model.GuessResult
import com.iliyan.mastermind.domain.model.GuessSlot
import com.iliyan.mastermind.domain.model.LetterResult
import com.iliyan.mastermind.domain.model.PositionEvaluation
import com.iliyan.mastermind.domain.repository.GameRepository

class GameRepositoryImpl : GameRepository {

    override fun generateSecret(): String {
        return (1..GAME_LENGTH)
            .map { ('A'..'Z').random() }
            .joinToString("")
    }

    override fun checkGuess(secret: String, guess: List<GuessSlot>): GuessResult {
        require(secret.length == GAME_LENGTH) { "Secret must be exactly 4 characters long" }
        require(guess.size == GAME_LENGTH) { "Guess size must be exactly 4" }
        val evaluations = guess.mapIndexed { index, slot ->

            when (slot) {
                GuessSlot.Empty -> {
                    PositionEvaluation(
                        slot = slot,
                        result = null
                    )
                }

                is GuessSlot.Filled -> {
                    val letter = slot.letter

                    val result = when {
                        secret[index] == letter -> LetterResult.GREEN
                        secret.contains(letter) -> LetterResult.ORANGE
                        else -> LetterResult.RED
                    }

                    PositionEvaluation(
                        slot = slot,
                        result = result
                    )
                }
            }
        }
        return GuessResult(evaluations)
    }
}