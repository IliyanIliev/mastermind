package com.iliyan.mastermind.presentation.game

sealed interface GameEvent {
    data class NavigateToResult(val isSuccess: Boolean) : GameEvent
}