package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PhoneReport
import com.example.model.PhoneStatus
import com.example.model.TimelineEvent
import com.example.model.TrackingStage
import com.example.ui.components.GlassCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenGlow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.viewmodel.UiState

@Composable
fun TrackingTimelineScreen(
    uiState: UiState,
    onSelectReport: (String) -> Unit,
    onAdvanceStage: (String) -> Unit
) {
    val currentReport: PhoneReport? = uiState.reports.find { it.id == uiState.selectedReportId }
        ?: uiState.reports.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timeline, contentDescription = null, tint = NeonCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "سجل البلاغات ومتابعة الحالة",
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text(
                        text = "خط زمني مباشر لتتبع تقدم البحث والرصد والاسترجاع",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Horizontal list of user reports to toggle
        item {
            Column {
                Text(
                    text = "اختر البلاغ لعرض خط التتبع:",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.reports) { rep ->
                        val isSelected = rep.id == currentReport?.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NeonCyanGlow else DarkSurfaceVariant)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyan else DarkBorder,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onSelectReport(rep.id) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Column {
                                Text(
                                    text = "${rep.brand} ${rep.model}",
                                    color = if (isSelected) NeonCyan else TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = rep.id,
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = rep.status.labelAr,
                                        color = rep.status.color,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (currentReport != null) {
            // Selected Report Overview Card
            item {
                GlassCard(
                    borderColor = NeonCyan.copy(alpha = 0.5f),
                    cornerRadius = 20
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${currentReport.brand} ${currentReport.model}",
                                    color = TextPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "IMEI: ${currentReport.imei1}",
                                    color = NeonCyan,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            StatusBadge(status = currentReport.status)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkSurfaceVariant)
                                .padding(10.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("كود البلاغ المرجعي:", color = TextSecondary, fontSize = 12.sp)
                                    Text(currentReport.id, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("المدينة وتاريخ البلاغ:", color = TextSecondary, fontSize = 12.sp)
                                    Text("${currentReport.incidentCity} (${currentReport.incidentDate})", color = TextPrimary, fontSize = 12.sp)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("المرحلة الحالية:", color = TextSecondary, fontSize = 12.sp)
                                    Text(currentReport.currentStage.titleAr, color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                if (currentReport.rewardAmount.isNotBlank()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("المكافأة المرصودة:", color = TextSecondary, fontSize = 12.sp)
                                        Text(currentReport.rewardAmount, color = EmeraldGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Simulation button to advance stage
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { onAdvanceStage(currentReport.id) },
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkSurfaceVariant,
                                contentColor = NeonCyan
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                width = 1.dp,
                                brush = androidx.compose.ui.graphics.SolidColor(NeonCyan.copy(alpha = 0.5f))
                            )
                        ) {
                            Icon(Icons.Default.FastForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("محاكاة تقدم مرحلة البلاغ (Demo Simulation)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Interactive Timeline
            item {
                Text(
                    text = "مراحل البلاغ والخط الزمني:",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(currentReport.timeline) { event ->
                TimelineNodeItem(event = event)
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun TimelineNodeItem(event: TimelineEvent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        // Vertical indicator column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            event.isAlert -> AlertRed
                            event.isCompleted -> EmeraldGreen
                            else -> DarkSurfaceVariant
                        }
                    )
                    .border(
                        2.dp,
                        if (event.isAlert) AlertRed else if (event.isCompleted) EmeraldGreen else DarkBorder,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (event.isAlert) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = DarkBg, modifier = Modifier.size(14.dp))
                } else if (event.isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = DarkBg, modifier = Modifier.size(14.dp))
                }
            }

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(64.dp)
                    .background(if (event.isCompleted) EmeraldGreen.copy(alpha = 0.5f) else DarkBorder)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Event Card
        GlassCard(
            borderColor = if (event.isAlert) AlertRed.copy(alpha = 0.8f) else if (event.isCompleted) EmeraldGreen.copy(alpha = 0.3f) else DarkBorder,
            cornerRadius = 14,
            modifier = Modifier.weight(1f)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.title,
                        color = if (event.isAlert) AlertRed else if (event.isCompleted) TextPrimary else TextMuted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = event.timestamp,
                        color = if (event.isAlert) AlertRed else NeonCyan,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = event.description,
                    color = if (event.isCompleted) TextSecondary else TextMuted,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                if (event.location.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextMuted, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(event.location, color = TextMuted, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
