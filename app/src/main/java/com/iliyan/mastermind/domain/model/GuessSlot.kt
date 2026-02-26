package com.iliyan.mastermind.domain.model

sealed interface GuessSlot {
    data object Empty : GuessSlot
    data class Filled(val letter: Char) : GuessSlot
}