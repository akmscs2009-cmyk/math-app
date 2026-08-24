package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FeedbackState
import com.example.ui.components.FeedbackCard
import com.example.ui.components.KidNumericKeypad
import com.example.ui.components.KidPrimaryButton
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.FunGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.QuizUiState

@Composable
fun PracticeScreen(
    uiState: QuizUiState,
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onNextClick: () -> Unit,
    onExitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val currentQuestion = uiState.currentQuestion
    val operation = uiState.selectedOperation

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 500.dp)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top Bar: Back button, Operation badge, Question number
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onExitClick,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("practice_exit_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Exit to Operations",
                        tint = TextPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Operation Badge
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = operation.lightColor,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = operation.symbol,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = operation.primaryColor
                        )
                        Text(
                            text = operation.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = operation.primaryColor
                        )
                    }
                }

                // Score and mistakes pill
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiState.wrongCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFFEE2E2),
                            shadowElevation = 1.dp
                        ) {
                            Text(
                                text = "Mistakes: ${uiState.wrongCount}/3",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFDC2626),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Text(
                            text = "Score: ${uiState.correctCount}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = FunGreen,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Question Progress Header & Linear Progress Bar
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${uiState.currentIndex + 1} of ${uiState.totalQuestions}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${((uiState.currentIndex + 1) * 10)}%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                }

                LinearProgressIndicator(
                    progress = { uiState.progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = operation.primaryColor,
                    trackColor = Color(0xFFE2E8F0),
                    strokeCap = StrokeCap.Round
                )
            }

            // Math Question Card
            if (currentQuestion != null) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("math_question_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Big Math Equation
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${currentQuestion.operand1}  ${operation.symbol}  ${currentQuestion.operand2}  =",
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        // Answer Box Display
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .height(60.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFF8FAFC))
                                .border(
                                    width = 2.dp,
                                    color = if (uiState.inputAnswer.isNotEmpty()) operation.primaryColor else BorderLight,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .testTag("answer_display_box"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.inputAnswer.isEmpty()) {
                                Text(
                                    text = "?",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextLight
                                )
                            } else {
                                Text(
                                    text = uiState.inputAnswer,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = operation.primaryColor
                                )
                            }
                        }
                    }
                }
            }

            // Instant Feedback Overlay (when submitted) or Keypad + Submit button
            if (uiState.submitted) {
                FeedbackCard(
                    state = uiState.feedbackState,
                    correctAnswerText = currentQuestion?.correctAnswer?.toString() ?: "",
                    onNextClick = onNextClick,
                    isLastQuestion = uiState.currentIndex >= uiState.totalQuestions - 1
                )
            } else {
                // Keypad
                KidNumericKeypad(
                    onDigitClick = onDigitClick,
                    onDeleteClick = onDeleteClick,
                    onClearClick = onClearClick,
                    enabled = !uiState.submitted
                )

                // Submit Button
                KidPrimaryButton(
                    text = "Submit Answer ✓",
                    onClick = onSubmitClick,
                    enabled = uiState.inputAnswer.isNotEmpty() && !uiState.submitted,
                    backgroundColor = operation.primaryColor,
                    testTag = "submit_answer_button"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
