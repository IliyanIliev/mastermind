package com.iliyan.mastermind.presentation.game

sealed interface GameAction {
    data class InputChanged(val index: Int, val value: String) : GameAction
    data object CheckClicked : GameAction
}