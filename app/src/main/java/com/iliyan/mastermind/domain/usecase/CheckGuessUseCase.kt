package com.iliyan.mastermind.domain.usecase

import com.iliyan.mastermind.domain.model.GuessResult
import com.iliyan.mastermind.domain.model.GuessSlot
import com.iliyan.mastermind.domain.repository.GameRepository

class CheckGuessUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(secret: String, guess: List<GuessSlot>): GuessResult {
        return repository.checkGuess(secret, guess)
    }
}