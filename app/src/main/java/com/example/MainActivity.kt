package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.ResultsScreen
import com.example.ui.screens.SelectOperationScreen
import com.example.ui.theme.KidsMathFunTheme
import com.example.viewmodel.MathQuizViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MathQuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KidsMathFunTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    KidsMathApp(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun KidsMathApp(
    viewModel: MathQuizViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle system back button properly based on current screen
    BackHandler(enabled = uiState.currentScreen != AppScreen.HOME) {
        when (uiState.currentScreen) {
            AppScreen.SELECT_OPERATION -> viewModel.navigateTo(AppScreen.HOME)
            AppScreen.PRACTICE -> viewModel.navigateTo(AppScreen.SELECT_OPERATION)
            AppScreen.RESULTS -> viewModel.navigateTo(AppScreen.SELECT_OPERATION)
            AppScreen.HOME -> { /* Do nothing / default exit */ }
        }
    }

    when (uiState.currentScreen) {
        AppScreen.HOME -> {
            HomeScreen(
                onStartGameClick = { viewModel.navigateTo(AppScreen.SELECT_OPERATION) },
                modifier = modifier
            )
        }
        AppScreen.SELECT_OPERATION -> {
            SelectOperationScreen(
                onSelectOperation = { operation -> viewModel.startNewGame(operation) },
                onBackToHome = { viewModel.navigateTo(AppScreen.HOME) },
                modifier = modifier
            )
        }
        AppScreen.PRACTICE -> {
            PracticeScreen(
                uiState = uiState,
                onDigitClick = { digit -> viewModel.appendDigit(digit) },
                onDeleteClick = { viewModel.deleteDigit() },
                onClearClick = { viewModel.clearInput() },
                onSubmitClick = { viewModel.submitAnswer() },
                onNextClick = { viewModel.nextQuestion() },
                onExitClick = { viewModel.navigateTo(AppScreen.SELECT_OPERATION) },
                modifier = modifier
            )
        }
        AppScreen.RESULTS -> {
            ResultsScreen(
                uiState = uiState,
                onPlayAgainClick = { viewModel.restartCurrentOperation() },
                onChooseOperationClick = { viewModel.navigateTo(AppScreen.SELECT_OPERATION) },
                onHomeClick = { viewModel.resetToHome() },
                modifier = modifier
            )
        }
    }
}
