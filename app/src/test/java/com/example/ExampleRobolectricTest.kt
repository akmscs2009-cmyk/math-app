package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.AppScreen
import com.example.model.FeedbackState
import com.example.model.MathOperation
import com.example.viewmodel.MathQuizViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app name from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Kids Math Fun", appName)
  }

  @Test
  fun `generate math questions for all operations`() {
    for (op in MathOperation.entries) {
      val questions = MathOperation.generateQuestions(op, 10)
      assertEquals(10, questions.size)
      for (q in questions) {
        when (op) {
          MathOperation.ADDITION -> assertEquals(q.operand1 + q.operand2, q.correctAnswer)
          MathOperation.SUBTRACTION -> {
            assertTrue(q.operand1 >= q.operand2)
            assertEquals(q.operand1 - q.operand2, q.correctAnswer)
          }
          MathOperation.MULTIPLICATION -> assertEquals(q.operand1 * q.operand2, q.correctAnswer)
          MathOperation.DIVISION -> assertEquals(q.operand1 / q.operand2, q.correctAnswer)
        }
      }
    }
  }

  @Test
  fun `viewmodel quiz workflow works correctly`() {
    val viewModel = MathQuizViewModel()
    assertEquals(AppScreen.HOME, viewModel.uiState.value.currentScreen)

    // Start game
    viewModel.startNewGame(MathOperation.ADDITION)
    assertEquals(AppScreen.PRACTICE, viewModel.uiState.value.currentScreen)
    assertEquals(10, viewModel.uiState.value.questions.size)
    assertEquals(0, viewModel.uiState.value.currentIndex)

    // Keypad input
    val currentQ = viewModel.uiState.value.currentQuestion
    assertNotNull(currentQ)
    val answerStr = currentQ!!.correctAnswer.toString()

    for (ch in answerStr) {
      viewModel.appendDigit(ch.toString())
    }
    assertEquals(answerStr, viewModel.uiState.value.inputAnswer)

    // Submit answer
    viewModel.submitAnswer()
    assertEquals(FeedbackState.CORRECT, viewModel.uiState.value.feedbackState)
    assertEquals(1, viewModel.uiState.value.correctCount)

    // Next question
    viewModel.nextQuestion()
    assertEquals(1, viewModel.uiState.value.currentIndex)
    assertEquals("", viewModel.uiState.value.inputAnswer)
    assertEquals(FeedbackState.NONE, viewModel.uiState.value.feedbackState)
  }
}
