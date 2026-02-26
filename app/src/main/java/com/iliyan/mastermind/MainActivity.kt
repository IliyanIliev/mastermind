package com.iliyan.mastermind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.iliyan.mastermind.presentation.navigation.MastermindNavHost
import com.iliyan.mastermind.presentation.theme.MastermindTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MastermindTheme {
                MastermindNavHost()
            }
        }
    }
}