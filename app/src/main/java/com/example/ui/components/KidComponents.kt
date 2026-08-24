package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FeedbackState
import com.example.ui.theme.FunGreen
import com.example.ui.theme.FunGreenLight
import com.example.ui.theme.FunRed
import com.example.ui.theme.FunRedLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun KidPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = PrimaryBlue,
    contentColor: Color = Color.White,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    testTag: String = "kid_primary_button"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = Color(0xFFCBD5E1),
            disabledContentColor = Color(0xFF64748B)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 1.dp
        ),
        modifier = modifier
            .height(58.dp)
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Box(modifier = Modifier.size(8.dp))
            }
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun KidNumericKeypad(
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val keypadRows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("C", "0", "DEL")
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        keypadRows.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { key ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                    ) {
                        when (key) {
                            "C" -> {
                                KeypadSpecialButton(
                                    label = "C",
                                    bgColor = Color(0xFFF1F5F9),
                                    contentColor = Color(0xFF64748B),
                                    onClick = onClearClick,
                                    enabled = enabled,
                                    testTag = "keypad_clear"
                                )
                            }
                            "DEL" -> {
                                KeypadSpecialButton(
                                    icon = Icons.Default.Backspace,
                                    bgColor = Color(0xFFF1F5F9),
                                    contentColor = Color(0xFF64748B),
                                    onClick = onDeleteClick,
                                    enabled = enabled,
                                    testTag = "keypad_delete"
                                )
                            }
                            else -> {
                                KeypadDigitButton(
                                    digit = key,
                                    onClick = { onDigitClick(key) },
                                    enabled = enabled,
                                    testTag = "keypad_digit_$key"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeypadDigitButton(
    digit: String,
    onClick: () -> Unit,
    enabled: Boolean,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (enabled) Color(0xFFFFFFFF) else Color(0xFFF8FAFC),
        shadowElevation = if (enabled) 2.dp else 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .testTag(testTag)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = digit,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (enabled) TextPrimary else Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
private fun KeypadSpecialButton(
    label: String? = null,
    icon: ImageVector? = null,
    bgColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    enabled: Boolean,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                enabled = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onClick
            )
            .testTag(testTag)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (label != null) {
                Text(
                    text = label,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            } else if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Delete",
                    tint = contentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
fun FeedbackCard(
    state: FeedbackState,
    correctAnswerText: String,
    onNextClick: () -> Unit,
    isLastQuestion: Boolean,
    modifier: Modifier = Modifier
) {
    if (state == FeedbackState.NONE) return

    val isCorrect = (state == FeedbackState.CORRECT)
    val bgColor = if (isCorrect) FunGreenLight else FunRedLight
    val borderColor = if (isCorrect) FunGreen else FunRed
    val titleText = if (isCorrect) "🎉 Awesome! That's Correct!" else "💪 Nice Try!"
    val subtitleText = if (isCorrect) "You got the right answer!" else "The correct answer is $correctAnswerText"

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("feedback_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = null,
                    tint = borderColor,
                    modifier = Modifier.size(28.dp)
                )
                Box(modifier = Modifier.size(8.dp))
                Text(
                    text = titleText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCorrect) Color(0xFF065F46) else Color(0xFF991B1B)
                )
            }

            Text(
                text = subtitleText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = if (isCorrect) Color(0xFF047857) else Color(0xFFB91C1C),
                textAlign = TextAlign.Center
            )

            KidPrimaryButton(
                text = if (isLastQuestion) "See Results 🏆" else "Next Question ➡️",
                onClick = onNextClick,
                backgroundColor = if (isCorrect) FunGreen else PrimaryBlue,
                testTag = "next_question_button"
            )
        }
    }
}
