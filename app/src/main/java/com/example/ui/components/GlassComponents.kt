package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PhoneStatus
import com.example.ui.theme.AlertRed
import com.example.ui.theme.AlertRedGlow
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenGlow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOnAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningAmberGlow

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = DarkBorder,
    glowColor: Color = Color.Transparent,
    cornerRadius: Int = 20,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius.dp)
    val cardModifier = if (onClick != null) {
        modifier
            .clip(shape)
            .clickable { onClick() }
    } else {
        modifier.clip(shape)
    }

    Box(
        modifier = cardModifier
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        borderColor.copy(alpha = 0.8f),
                        borderColor.copy(alpha = 0.2f)
                    )
                ),
                shape = shape
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkCardBg,
                        DarkSurfaceVariant.copy(alpha = 0.9f)
                    )
                )
            )
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun StatusBadge(
    status: PhoneStatus,
    modifier: Modifier = Modifier
) {
    val (bgGlow, borderCol, icon) = when (status) {
        PhoneStatus.STOLEN -> Triple(AlertRedGlow, AlertRed, Icons.Default.Error)
        PhoneStatus.LOST -> Triple(WarningAmberGlow, WarningAmber, Icons.Default.Warning)
        PhoneStatus.RECOVERED -> Triple(EmeraldGreenGlow, EmeraldGreen, Icons.Default.CheckCircle)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgGlow)
            .border(1.dp, borderCol.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = status.labelAr,
            tint = borderCol,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = status.labelAr,
            color = borderCol,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isSecondary: Boolean = false,
    colorAccent: Color = NeonCyan,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .height(52.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSecondary) DarkSurfaceVariant else colorAccent,
            contentColor = if (isSecondary) TextPrimary else TextOnAccent,
            disabledContainerColor = DarkSurfaceVariant.copy(alpha = 0.5f),
            disabledContentColor = TextMuted
        ),
        border = if (isSecondary) {
            ButtonDefaults.outlinedButtonBorder(enabled).copy(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(listOf(colorAccent, colorAccent.copy(alpha = 0.4f)))
            )
        } else null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (isSecondary) colorAccent else TextOnAccent
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSecondary) TextPrimary else TextOnAccent
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    accentColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        borderColor = accentColor.copy(alpha = 0.4f),
        cornerRadius = 18
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.15f))
                    .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun StepIndicator(
    currentStep: Int,
    totalSteps: Int = 4,
    onStepClick: ((Int) -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (step in 1..totalSteps) {
            val isActive = step == currentStep
            val isPassed = step < currentStep

            val dotColor = when {
                isActive -> NeonCyan
                isPassed -> EmeraldGreen
                else -> DarkBorder
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) NeonCyan.copy(alpha = 0.2f)
                        else if (isPassed) EmeraldGreen.copy(alpha = 0.2f)
                        else DarkSurfaceVariant
                    )
                    .border(
                        width = if (isActive || isPassed) 2.dp else 1.dp,
                        color = dotColor,
                        shape = CircleShape
                    )
                    .then(
                        if (onStepClick != null && isPassed) Modifier.clickable { onStepClick(step) }
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isPassed) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "تم",
                        tint = EmeraldGreen,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = "$step",
                        color = if (isActive) NeonCyan else TextMuted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (step < totalSteps) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (step < currentStep) EmeraldGreen else DarkBorder)
                )
            }
        }
    }
}

@Composable
fun ScannerViewfinder(
    isScanning: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scan_laser")
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_pos"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurfaceVariant.copy(alpha = 0.8f))
            .border(1.5.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Grid pattern
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isScanning) "جاري مسح الباركود والـ IMEI بالليزر..." else "وجّه الكاميرا نحو باركود الـ IMEI أو الكرتون",
                color = if (isScanning) NeonCyan else TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Barcode representation
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                val barHeights = listOf(40, 60, 50, 65, 45, 60, 35, 55, 65, 40, 55, 60, 45, 65, 50, 40)
                barHeights.forEach { h ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .width(3.dp)
                            .height(h.dp)
                            .background(if (isScanning) NeonCyan.copy(alpha = 0.8f) else TextMuted.copy(alpha = 0.5f))
                    )
                }
            }
            Text(
                text = "رمز التعرف السريع (IMEI-1 / IMEI-2)",
                color = TextMuted,
                fontSize = 11.sp
            )
        }

        // Scanning Laser beam
        if (isScanning) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(3.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = (laserY * 180).dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                NeonCyan,
                                Color.White,
                                NeonCyan,
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}
