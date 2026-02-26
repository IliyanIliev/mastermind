package com.iliyan.mastermind.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object Game : Routes

    @Serializable
    data class Result(val isSuccess: Boolean) : Routes
}