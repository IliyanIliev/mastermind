package com.iliyan.mastermind.presentation.game

import androidx.compose.runtime.Immutable
import com.iliyan.mastermind.domain.model.GameRules.GAME_LENGTH
import com.iliyan.mastermind.domain.model.GuessSlot
import com.iliyan.mastermind.domain.model.LetterResult

@Immutable
data class GameState(
    val slots: List<GuessSlot> = List(GAME_LENGTH) { GuessSlot.Empty },
    val evaluations: List<LetterResult?> = List(GAME_LENGTH) { null },
    val secondsRemaining: Int = 60
) {
    val progress: Float
        get() = secondsRemaining / 60f
}