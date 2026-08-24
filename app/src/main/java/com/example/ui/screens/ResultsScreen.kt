package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MathQuestion
import com.example.ui.components.KidPrimaryButton
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.FunGreen
import com.example.ui.theme.FunGreenLight
import com.example.ui.theme.FunRed
import com.example.ui.theme.FunRedLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SoftYellow
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.QuizUiState

@Composable
fun ResultsScreen(
    uiState: QuizUiState,
    onPlayAgainClick: () -> Unit,
    onChooseOperationClick: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val total = uiState.questions.size
    val correct = uiState.correctCount
    val percentage = uiState.scorePercentage

    val (headline, emoji, badgeColor) = when {
        percentage >= 90 -> Triple("Super Star!", "🌟", AccentOrange)
        percentage >= 70 -> Triple("Great Job!", "🎉", FunGreen)
        percentage >= 50 -> Triple("Good Effort!", "👍", PrimaryBlue)
        else -> Triple("Keep Practicing!", "🌱", AccentOrange)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEFF6FF),
                        Color(0xFFF8FAFC)
                    )
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 500.dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(14.dp))

                // Score Celebration Header
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .shadow(4.dp, CircleShape)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 36.sp)
                    }

                    Text(
                        text = headline,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = badgeColor,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "${uiState.selectedOperation.title} Practice Complete!",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Summary Stats Card
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("results_summary_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Big Percentage Circle
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .shadow(2.dp, CircleShape)
                                .background(uiState.selectedOperation.lightColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "$percentage%",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    color = uiState.selectedOperation.primaryColor
                                )
                                Text(
                                    text = "Score",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary
                                )
                            }
                        }

                        // 3 Stat Metrics
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatBox(
                                title = "Total",
                                value = "$total",
                                color = TextPrimary,
                                bgColor = Color(0xFFF1F5F9)
                            )
                            StatBox(
                                title = "Correct",
                                value = "$correct",
                                color = FunGreen,
                                bgColor = FunGreenLight
                            )
                            StatBox(
                                title = "Incorrect",
                                value = "${total - correct}",
                                color = FunRed,
                                bgColor = FunRedLight
                            )
                        }
                    }
                }
            }

            // Question Review Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question Review",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            // Review list items
            itemsIndexed(uiState.questions) { index, question ->
                QuestionReviewItem(index = index + 1, question = question)
            }

            // Action Buttons
            item {
                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KidPrimaryButton(
                        text = "Play Again 🔄",
                        onClick = onPlayAgainClick,
                        backgroundColor = uiState.selectedOperation.primaryColor,
                        testTag = "play_again_button"
                    )

                    KidPrimaryButton(
                        text = "Choose Another Operation 🎯",
                        onClick = onChooseOperationClick,
                        backgroundColor = PrimaryBlue,
                        testTag = "choose_operation_button"
                    )

                    OutlinedButton(
                        onClick = onHomeClick,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("results_home_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Home",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun StatBox(
    title: String,
    value: String,
    color: Color,
    bgColor: Color
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        modifier = Modifier.widthIn(min = 85.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun QuestionReviewItem(
    index: Int,
    question: MathQuestion
) {
    val isCorrect = question.isCorrect == true
    val iconColor = if (isCorrect) FunGreen else FunRed
    val itemBg = if (isCorrect) Color.White else Color(0xFFFFF7ED)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = itemBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isCorrect) FunGreenLight else FunRedLight,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = if (isCorrect) "Correct" else "Incorrect",
                            tint = iconColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Text(
                    text = "$index. ${question.operand1} ${question.operation.symbol} ${question.operand2} =",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (!isCorrect && question.userAnswer != null) {
                    Text(
                        text = "${question.userAnswer}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = FunRed
                    )
                    Text(
                        text = "→",
                        fontSize = 13.sp,
                        color = TextLight
                    )
                }
                Text(
                    text = "${question.correctAnswer}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCorrect) FunGreen else TextPrimary
                )
            }
        }
    }
}
