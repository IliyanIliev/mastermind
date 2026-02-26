package com.iliyan.mastermind.domain.model

import com.iliyan.mastermind.domain.model.GameRules.GAME_LENGTH

data class GuessResult(
    val positions: List<PositionEvaluation>
) {
    val isWin: Boolean
        get() = positions.size == GAME_LENGTH &&
                positions.all { it.result == LetterResult.GREEN }
}