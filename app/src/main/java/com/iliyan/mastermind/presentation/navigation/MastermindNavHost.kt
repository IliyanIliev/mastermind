package com.iliyan.mastermind.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.iliyan.mastermind.presentation.game.GameEvent
import com.iliyan.mastermind.presentation.game.GameScreen
import com.iliyan.mastermind.presentation.game.GameViewModel
import com.iliyan.mastermind.presentation.result.ResultEvent
import com.iliyan.mastermind.presentation.result.ResultScreen
import com.iliyan.mastermind.presentation.result.ResultViewModel
import com.iliyan.mastermind.presentation.utils.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MastermindNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.Game) {

        composable<Routes.Game> {
            val vm: GameViewModel = koinViewModel()
            val gameState by vm.gameState.collectAsStateWithLifecycle()

            ObserveAsEvents(vm.events) { event ->
                when (event) {
                    is GameEvent.NavigateToResult -> {
                        navController.navigate(Routes.Result(isSuccess = event.isSuccess)) {
                            popUpTo(Routes.Game) {
                                inclusive = true
                            }
                        }
                    }
                }
            }

            GameScreen(
                gameState = gameState,
                onAction = vm::onAction
            )
        }

        composable<Routes.Result> { backStackEntry ->
            val route: Routes.Result = backStackEntry.toRoute()
            val vm: ResultViewModel = koinViewModel(parameters = { parametersOf(route.isSuccess) })
            val state by vm.state.collectAsStateWithLifecycle()

            ObserveAsEvents(vm.events) { event ->
                when (event) {
                    ResultEvent.Retry -> {
                        navController.navigate(Routes.Game) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }

            ResultScreen(
                state = state,
                onAction = vm::onAction
            )
        }
    }
}