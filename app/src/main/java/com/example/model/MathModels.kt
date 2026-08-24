package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AdditionColor
import com.example.ui.theme.AdditionColorLight
import com.example.ui.theme.DivisionColor
import com.example.ui.theme.DivisionColorLight
import com.example.ui.theme.MultiplicationColor
import com.example.ui.theme.MultiplicationColorLight
import com.example.ui.theme.SubtractionColor
import com.example.ui.theme.SubtractionColorLight
import kotlin.random.Random

enum class MathOperation(
    val symbol: String,
    val title: String,
    val subtitle: String,
    val primaryColor: Color,
    val lightColor: Color
) {
    ADDITION(
        symbol = "+",
        title = "Addition",
        subtitle = "Add numbers together",
        primaryColor = AdditionColor,
        lightColor = AdditionColorLight
    ),
    SUBTRACTION(
        symbol = "−",
        title = "Subtraction",
        subtitle = "Take away numbers",
        primaryColor = SubtractionColor,
        lightColor = SubtractionColorLight
    ),
    MULTIPLICATION(
        symbol = "×",
        title = "Multiplication",
        subtitle = "Multiply numbers",
        primaryColor = MultiplicationColor,
        lightColor = MultiplicationColorLight
    ),
    DIVISION(
        symbol = "÷",
        title = "Division",
        subtitle = "Divide into equal parts",
        primaryColor = DivisionColor,
        lightColor = DivisionColorLight
    );

    companion object {
        fun generateQuestions(operation: MathOperation, count: Int = 10): List<MathQuestion> {
            val questions = mutableListOf<MathQuestion>()
            val random = Random.Default

            for (i in 1..count) {
                val question = when (operation) {
                    ADDITION -> {
                        // Single or double digits appropriate for age 5-10
                        val op1 = random.nextInt(1, 21)
                        val op2 = random.nextInt(1, 21)
                        MathQuestion(
                            operand1 = op1,
                            operand2 = op2,
                            operation = ADDITION,
                            correctAnswer = op1 + op2
                        )
                    }
                    SUBTRACTION -> {
                        // Ensure non-negative answer: op1 >= op2
                        val a = random.nextInt(3, 30)
                        val b = random.nextInt(1, a)
                        MathQuestion(
                            operand1 = a,
                            operand2 = b,
                            operation = SUBTRACTION,
                            correctAnswer = a - b
                        )
                    }
                    MULTIPLICATION -> {
                        // Standard multiplication tables 1 through 10
                        val op1 = random.nextInt(1, 11)
                        val op2 = random.nextInt(1, 11)
                        MathQuestion(
                            operand1 = op1,
                            operand2 = op2,
                            operation = MULTIPLICATION,
                            correctAnswer = op1 * op2
                        )
                    }
                    DIVISION -> {
                        // Clean whole-number division with no remainder
                        val divisor = random.nextInt(1, 11)
                        val quotient = random.nextInt(1, 11)
                        val dividend = divisor * quotient
                        MathQuestion(
                            operand1 = dividend,
                            operand2 = divisor,
                            operation = DIVISION,
                            correctAnswer = quotient
                        )
                    }
                }
                questions.add(question)
            }
            return questions
        }
    }
}

data class MathQuestion(
    val operand1: Int,
    val operand2: Int,
    val operation: MathOperation,
    val correctAnswer: Int,
    val userAnswer: Int? = null,
    val isCorrect: Boolean? = null
)

enum class AppScreen {
    HOME,
    SELECT_OPERATION,
    PRACTICE,
    RESULTS
}

enum class FeedbackState {
    NONE,
    CORRECT,
    INCORRECT
}
