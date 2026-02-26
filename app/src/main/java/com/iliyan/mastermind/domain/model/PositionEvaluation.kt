package com.iliyan.mastermind.domain.model

data class PositionEvaluation(
    val slot: GuessSlot,
    val result: LetterResult?
)