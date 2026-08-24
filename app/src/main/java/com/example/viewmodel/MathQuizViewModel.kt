package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundEffectManager
import com.example.model.AppScreen
import com.example.model.FeedbackState
import com.example.model.MathOperation
import com.example.model.MathQuestion
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuizUiState(
    val currentScreen: AppScreen = AppScreen.HOME,
    val selectedOperation: MathOperation = MathOperation.ADDITION,
    val questions: List<MathQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val inputAnswer: String = "",
    val feedbackState: FeedbackState = FeedbackState.NONE,
    val submitted: Boolean = false,
    val totalQuestions: Int = 10,
    val wrongCount: Int = 0,
    val isBlocked: Boolean = false,
    val blockedSecondsRemaining: Int = 60
) {
    val currentQuestion: MathQuestion?
        get() = questions.getOrNull(currentIndex)

    val correctCount: Int
        get() = questions.count { it.isCorrect == true }

    val incorrectCount: Int
        get() = questions.count { it.isCorrect == false }

    val progressPercent: Float
        get() = if (questions.isEmpty()) 0f else (currentIndex + 1).toFloat() / questions.size

    val scorePercentage: Int
        get() = if (questions.isEmpty()) 0 else ((correctCount.toFloat() / questions.size) * 100).toInt()
}

class MathQuizViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var blockTimerJob: Job? = null

    fun navigateTo(screen: AppScreen) {
        if (_uiState.value.isBlocked) return
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun startNewGame(operation: MathOperation) {
        blockTimerJob?.cancel()
        val newQuestions = MathOperation.generateQuestions(operation, count = 10)
        _uiState.update {
            it.copy(
                currentScreen = AppScreen.PRACTICE,
                selectedOperation = operation,
                questions = newQuestions,
                currentIndex = 0,
                inputAnswer = "",
                feedbackState = FeedbackState.NONE,
                submitted = false,
                wrongCount = 0,
                isBlocked = false,
                blockedSecondsRemaining = 60
            )
        }
    }

    fun appendDigit(digit: String) {
        val current = _uiState.value
        if (current.isBlocked || current.submitted) return
        if (current.inputAnswer.length >= 4) return // Max 4 digits
        _uiState.update { it.copy(inputAnswer = it.inputAnswer + digit) }
    }

    fun deleteDigit() {
        val current = _uiState.value
        if (current.isBlocked || current.submitted) return
        if (current.inputAnswer.isNotEmpty()) {
            _uiState.update { it.copy(inputAnswer = it.inputAnswer.dropLast(1)) }
        }
    }

    fun clearInput() {
        val current = _uiState.value
        if (current.isBlocked || current.submitted) return
        _uiState.update { it.copy(inputAnswer = "") }
    }

    fun submitAnswer() {
        val current = _uiState.value
        if (current.isBlocked || current.submitted) return
        val currentQ = current.currentQuestion ?: return
        val parsedAnswer = current.inputAnswer.toIntOrNull() ?: return

        val isCorrect = (parsedAnswer == currentQ.correctAnswer)
        val updatedQuestions = current.questions.toMutableList()
        updatedQuestions[current.currentIndex] = currentQ.copy(
            userAnswer = parsedAnswer,
            isCorrect = isCorrect
        )

        val newWrongCount = if (isCorrect) current.wrongCount else current.wrongCount + 1

        _uiState.update {
            it.copy(
                questions = updatedQuestions,
                submitted = true,
                feedbackState = if (isCorrect) FeedbackState.CORRECT else FeedbackState.INCORRECT,
                wrongCount = newWrongCount
            )
        }

        if (isCorrect) {
            SoundEffectManager.playCorrectSound()
        } else {
            if (newWrongCount >= 3) {
                triggerAppBlock()
            } else {
                SoundEffectManager.playIncorrectSound()
            }
        }
    }

    private fun triggerAppBlock() {
        SoundEffectManager.playBlockAlertSound()
        blockTimerJob?.cancel()
        _uiState.update {
            it.copy(
                isBlocked = true,
                blockedSecondsRemaining = 60
            )
        }

        blockTimerJob = viewModelScope.launch {
            for (sec in 60 downTo 1) {
                _uiState.update { it.copy(blockedSecondsRemaining = sec) }
                delay(1000L)
            }
            // Unlock app and reset wrong counter for a fresh start
            _uiState.update {
                it.copy(
                    isBlocked = false,
                    wrongCount = 0,
                    blockedSecondsRemaining = 0
                )
            }
        }
    }

    fun nextQuestion() {
        val current = _uiState.value
        if (current.isBlocked) return
        if (current.currentIndex < current.questions.size - 1) {
            _uiState.update {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    inputAnswer = "",
                    feedbackState = FeedbackState.NONE,
                    submitted = false
                )
            }
        } else {
            // Reached end of 10 questions -> Show results screen
            _uiState.update {
                it.copy(
                    currentScreen = AppScreen.RESULTS,
                    feedbackState = FeedbackState.NONE,
                    submitted = false
                )
            }
        }
    }

    fun restartCurrentOperation() {
        startNewGame(_uiState.value.selectedOperation)
    }

    fun resetToHome() {
        if (_uiState.value.isBlocked) return
        blockTimerJob?.cancel()
        _uiState.update {
            it.copy(
                currentScreen = AppScreen.HOME,
                inputAnswer = "",
                feedbackState = FeedbackState.NONE,
                submitted = false,
                wrongCount = 0,
                isBlocked = false,
                blockedSecondsRemaining = 60
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        blockTimerJob?.cancel()
    }
}
