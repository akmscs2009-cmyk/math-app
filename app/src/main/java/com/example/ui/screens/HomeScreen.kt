package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.MathOperation
import com.example.ui.components.KidPrimaryButton
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AdditionColor
import com.example.ui.theme.DivisionColor
import com.example.ui.theme.MultiplicationColor
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SoftYellow
import com.example.ui.theme.SubtractionColor
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    onStartGameClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEFF6FF),
                        Color(0xFFF8FAFC),
                        Color(0xFFFFFBEB)
                    )
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 500.dp)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // App Title Header with colorful badge
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = AccentOrange.copy(alpha = 0.15f),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = "⭐ FOR AGES 5–10 ⭐",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentOrange,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Text(
                    text = "Kids Math Fun",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    color = PrimaryBlue,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Practice basic math the fun and easy way!",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            // Hero Banner Illustration
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_math_hero),
                    contentDescription = "Cute mascot learning math",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Quick Math Operations Preview Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OperationBadge(symbol = "+", label = "Add", color = AdditionColor)
                OperationBadge(symbol = "−", label = "Subtract", color = SubtractionColor)
                OperationBadge(symbol = "×", label = "Multiply", color = MultiplicationColor)
                OperationBadge(symbol = "÷", label = "Divide", color = DivisionColor)
            }

            // Features Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureRow(emoji = "🎯", text = "10 random math questions per game")
                    FeatureRow(emoji = "⚡", text = "Instant feedback with hints")
                    FeatureRow(emoji = "🏆", text = "Fun score report at the end")
                    FeatureRow(emoji = "🔒", text = "100% offline & kid-safe")
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Big Start Game Button
            KidPrimaryButton(
                text = "Start Game 🚀",
                onClick = onStartGameClick,
                backgroundColor = PrimaryBlue,
                testTag = "start_game_button"
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun OperationBadge(
    symbol: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .shadow(2.dp, CircleShape)
                .background(color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = symbol,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
    }
}

@Composable
private fun FeatureRow(emoji: String, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = emoji, fontSize = 18.sp)
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
        )
    }
}
