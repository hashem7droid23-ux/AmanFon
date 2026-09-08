package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ImeiValidator
import com.example.model.PhoneReport
import com.example.model.PhoneStatus
import com.example.ui.components.GlassCard
import com.example.ui.components.NeonButton
import com.example.ui.components.ScannerViewfinder
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AlertRed
import com.example.ui.theme.AlertRedGlow
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
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
import com.example.viewmodel.ImeiCheckOutcome
import com.example.viewmodel.UiState

@Composable
fun ImeiCheckerScreen(
    uiState: UiState,
    onImeiChange: (String) -> Unit,
    onScanSimulate: (String) -> Unit,
    onPerformCheck: () -> Unit,
    onReportSighting: (reportId: String, shopLocation: String, notes: String) -> Unit,
    onViewTimeline: (String) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var showReportDialog by remember { mutableStateOf<PhoneReport?>(null) }
    var sightingShopName by remember { mutableStateOf("مركز الصيانة المعتمد - الرياض") }
    var sightingNotes by remember { mutableStateOf("حضر شخص يطلب فك قفل الجهاز بدون كرتون أو فاتورة.") }

    val isLuhnValid by remember(uiState.imeiQuery) {
        derivedStateOf {
            if (uiState.imeiQuery.length == 15) {
                ImeiValidator.isValidLuhn(uiState.imeiQuery)
            } else null
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            // Title
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "أداة الفحص الأمني للـ IMEI",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "تحقق فوري من سلامة الأجهزة قبل الشراء أو الاستلام عبر قاعدة البيانات الوطنية",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Camera / Barcode scan simulation
        item {
            GlassCard(
                borderColor = if (uiState.isScanning) NeonCyan else DarkBorder,
                cornerRadius = 18
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ScannerViewfinder(isScanning = uiState.isScanning)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onScanSimulate("356938035643803") },
                            enabled = !uiState.isScanning,
                            modifier = Modifier.weight(1f).height(44.dp),
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
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("مسح الباركود", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                clipboardManager.getText()?.text?.let { pasteText ->
                                    val digits = pasteText.filter { it.isDigit() }.take(15)
                                    if (digits.isNotBlank()) onImeiChange(digits)
                                }
                            },
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkSurfaceVariant,
                                contentColor = TextPrimary
                            )
                        ) {
                            Icon(Icons.Default.ContentPaste, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("لصق من الحافظة", fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Manual IMEI Input with Luhn Indicator
        item {
            GlassCard(cornerRadius = 18) {
                Column {
                    Text(
                        text = "أدخل رقم الـ IMEI يدوياً (15 رقماً):",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.imeiQuery,
                        onValueChange = { onImeiChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("مثال: 356938035643803", color = TextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = DarkBorder,
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant
                        ),
                        shape = RoundedCornerShape(14.dp),
                        trailingIcon = {
                            if (uiState.imeiQuery.isNotEmpty()) {
                                IconButton(onClick = { onImeiChange("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "مسح", tint = TextMuted)
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Luhn and Length Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "عدد الأرقام: ${uiState.imeiQuery.length} / 15",
                            color = if (uiState.imeiQuery.length == 15) EmeraldGreen else TextSecondary,
                            fontSize = 12.sp
                        )

                        if (uiState.imeiQuery.length == 15) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isLuhnValid == true) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (isLuhnValid == true) EmeraldGreen else WarningAmber,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isLuhnValid == true) "خوارزمية Luhn: رقم فحص سليم" else "تنبيه: تحقق من صحة الرقم",
                                    color = if (isLuhnValid == true) EmeraldGreen else WarningAmber,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Presets chips
                    Text(
                        text = "أرقام تجريبية سريعة للفحص:",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SuggestionChip(
                            onClick = { onImeiChange("356938035643803") },
                            label = { Text("مسروق 🔴", fontSize = 11.sp) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = AlertRedGlow,
                                labelColor = AlertRed
                            ),
                            border = SuggestionChipDefaults.suggestionChipBorder(
                                enabled = true,
                                borderColor = AlertRed.copy(alpha = 0.5f)
                            )
                        )
                        SuggestionChip(
                            onClick = { onImeiChange("864321045678901") },
                            label = { Text("مفقود 🟡", fontSize = 11.sp) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = WarningAmberGlow,
                                labelColor = WarningAmber
                            ),
                            border = SuggestionChipDefaults.suggestionChipBorder(
                                enabled = true,
                                borderColor = WarningAmber.copy(alpha = 0.5f)
                            )
                        )
                        SuggestionChip(
                            onClick = { onImeiChange("869234051234567") },
                            label = { Text("نظيف وسليم 🟢", fontSize = 11.sp) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = EmeraldGreenGlow,
                                labelColor = EmeraldGreen
                            ),
                            border = SuggestionChipDefaults.suggestionChipBorder(
                                enabled = true,
                                borderColor = EmeraldGreen.copy(alpha = 0.5f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Perform check button
                    NeonButton(
                        text = if (uiState.isChecking) "جاري الفحص بالقاعدة المركزية..." else "فحص أمني فوري للـ IMEI",
                        onClick = onPerformCheck,
                        icon = if (uiState.isChecking) null else Icons.Default.QrCodeScanner,
                        enabled = !uiState.isChecking && uiState.imeiQuery.isNotBlank()
                    )
                }
            }
        }

        // Checking Loader
        if (uiState.isChecking) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = NeonCyan)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "جاري مطابقة الرقم مع سجلات الشرطة ومحلات الصيانة...",
                            color = NeonCyan,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Result Card
        item {
            AnimatedVisibility(
                visible = uiState.checkOutcome !is ImeiCheckOutcome.Idle && !uiState.isChecking,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                when (val outcome = uiState.checkOutcome) {
                    is ImeiCheckOutcome.Clean -> {
                        CleanResultCard(outcome)
                    }
                    is ImeiCheckOutcome.Reported -> {
                        ReportedResultCard(
                            report = outcome.report,
                            onReportSightingClick = { showReportDialog = outcome.report },
                            onViewTimelineClick = { onViewTimeline(outcome.report.id) }
                        )
                    }
                    is ImeiCheckOutcome.Invalid -> {
                        GlassCard(borderColor = WarningAmber) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = WarningAmber)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = outcome.message, color = WarningAmber, fontSize = 13.sp)
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }

    // Sighting Alert Dialog
    showReportDialog?.let { report ->
        AlertDialog(
            onDismissRequest = { showReportDialog = null },
            containerColor = DarkSurfaceVariant,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = AlertRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "إبلاغ فوري عن رصد جهاز مسروق",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "سيتم إرسال إشعار فوري لمالك الجهاز والجهات المختصة لتحديد موقع وتواجد الهاتف (${report.brand} ${report.model}):",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "موقع المحل / نقطة الرصد:",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = sightingShopName,
                        onValueChange = { sightingShopName = it },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = DarkBorder
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ملاحظات الفني:",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = sightingNotes,
                        onValueChange = { sightingNotes = it },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = DarkBorder
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReportSighting(report.id, sightingShopName, sightingNotes)
                        showReportDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AlertRed)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("إرسال التنبيه الآن", color = TextPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = null }) {
                    Text("إلغاء", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun CleanResultCard(clean: ImeiCheckOutcome.Clean) {
    GlassCard(
        borderColor = EmeraldGreen,
        cornerRadius = 20,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(EmeraldGreenGlow)
                    .border(2.dp, EmeraldGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "سليم",
                    tint = EmeraldGreen,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "جهاز نظيف وسليم 100%",
                color = EmeraldGreen,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "هذا الجهاز غير مسجل في قاعدة بيانات الهواتف المسروقة أو المفقودة الوطنية.",
                color = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))

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
                        Text("رقم الـ IMEI المفحوص:", color = TextSecondary, fontSize = 12.sp)
                        Text(clean.imei, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("تاريخ ووقت الفحص:", color = TextSecondary, fontSize = 12.sp)
                        Text(clean.checkedAt, color = NeonCyan, fontSize = 12.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("النتيجة الرسمية:", color = TextSecondary, fontSize = 12.sp)
                        Text("آمن للشراء والاستخدام", color = EmeraldGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ReportedResultCard(
    report: PhoneReport,
    onReportSightingClick: () -> Unit,
    onViewTimelineClick: () -> Unit
) {
    val isStolen = report.status == PhoneStatus.STOLEN
    val accentCol = if (isStolen) AlertRed else WarningAmber
    val glowCol = if (isStolen) AlertRedGlow else WarningAmberGlow

    GlassCard(
        borderColor = accentCol,
        cornerRadius = 20,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(glowCol)
                            .border(1.5.dp, accentCol, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isStolen) Icons.Default.Error else Icons.Default.Warning,
                            contentDescription = null,
                            tint = accentCol,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isStolen) "تحذير أمني: جهاز مسروق!" else "تنبيه: جهاز مسجل كمفقود!",
                            color = accentCol,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "مسجل في المنظومة الوطنية بالرقم: ${report.id}",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
                StatusBadge(status = report.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Details Table
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceVariant)
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    DetailRow("الجهاز والموديل:", "${report.brand} ${report.model} (${report.storage})")
                    DetailRow("اللون:", report.color)
                    DetailRow("رقم الـ IMEI:", report.imei1)
                    DetailRow("المدينة وتاريخ البلاغ:", "${report.incidentCity} • ${report.incidentDate}")
                    if (report.rewardAmount.isNotBlank()) {
                        DetailRow("المكافأة المرصودة:", report.rewardAmount, EmeraldGreen)
                    }
                    DetailRow("حالة البلاغ الحالية:", report.currentStage.titleAr, NeonCyan)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NeonButton(
                    text = "إبلاغ صاحب الجهاز / تحديد مكان تواجده 🚨",
                    onClick = onReportSightingClick,
                    colorAccent = AlertRed,
                    icon = Icons.Default.NotificationsActive
                )

                NeonButton(
                    text = "متابعة سجل وتتبع البلاغ",
                    onClick = onViewTimelineClick,
                    isSecondary = true,
                    colorAccent = NeonCyan
                )
            }
        }
    }
}

@Composable
private fun DetailRow(title: String, value: String, valueColor: Color = TextPrimary) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = TextSecondary, fontSize = 12.sp)
        Text(value, color = valueColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
