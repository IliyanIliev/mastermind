package com.iliyan.mastermind.domain.repository

import com.iliyan.mastermind.domain.model.GuessResult
import com.iliyan.mastermind.domain.model.GuessSlot

interface GameRepository {
    fun generateSecret(): String
    fun checkGuess(secret: String, guess: List<GuessSlot>): GuessResult
}