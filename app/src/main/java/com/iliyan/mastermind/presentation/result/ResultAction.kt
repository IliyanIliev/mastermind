package com.iliyan.mastermind.presentation.result

sealed interface ResultAction {
    data object RetryClicked : ResultAction
}