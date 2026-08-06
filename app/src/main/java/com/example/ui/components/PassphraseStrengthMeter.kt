package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun PassphraseStrengthMeter(
    strengthText: String,
    modifier: Modifier = Modifier
) {
    val progress = when {
        strengthText.startsWith("Bulletproof") -> 1.0f
        strengthText.startsWith("Strong") -> 0.75f
        strengthText.startsWith("Fair") -> 0.5f
        else -> 0.25f
    }

    val barColor = when {
        progress >= 0.9f -> EmeraldSafe
        progress >= 0.7f -> CyanAccent
        progress >= 0.4f -> AmberWarning
        else -> RubyEmergency
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Passphrase Security Level:",
                style = MaterialTheme.typography.labelMedium.copy(color = TextSecondaryDark)
            )
            Text(
                text = strengthText,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = barColor
                )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(IndigoCard)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(3.dp))
                    .background(barColor)
            )
        }
    }
}
