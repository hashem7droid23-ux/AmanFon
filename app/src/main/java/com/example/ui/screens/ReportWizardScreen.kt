package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.NeonButton
import com.example.ui.components.StepIndicator
import com.example.ui.theme.AlertRed
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenGlow
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextOnAccent
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.viewmodel.NewReportFormData
import com.example.viewmodel.UiState

@Composable
fun ReportWizardScreen(
    uiState: UiState,
    onStepChange: (Int) -> Unit,
    onFormDataChange: (NewReportFormData.() -> NewReportFormData) -> Unit,
    onSubmitReport: () -> Unit
) {
    val formData = uiState.formData
    val currentStep = uiState.formStep

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "معالج تقديم بلاغ جديد",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "الخطوة $currentStep من 4: " + when (currentStep) {
                            1 -> "بيانات الهاتف والـ IMEI"
                            2 -> "تفاصيل وموقع الحادثة"
                            3 -> "إثبات الملكية والمستندات"
                            else -> "بيانات الاتصال والمكافأة"
                        },
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Quick autofill for demo test
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurfaceVariant)
                        .clickable {
                            onFormDataChange {
                                copy(
                                    brand = "Apple",
                                    model = "iPhone 14 Pro",
                                    storage = "128 GB",
                                    color = "بنفسجي غامق",
                                    imei1 = "354129087654321",
                                    imei2 = "354129087654322",
                                    incidentType = "سرقة",
                                    incidentDate = "2024-05-07",
                                    incidentCity = "الرياض",
                                    incidentDescription = "تمت سرقة الجهاز أثناء التسوق بأحد المجمعات التجارية.",
                                    contactName = "سلطان العتيبي",
                                    contactPhone = "0551122334",
                                    rewardAmount = "500 ر.س"
                                )
                            }
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("ملء تلقائي للتجربة ⚡", color = NeonCyan, fontSize = 11.sp)
                }
            }

            StepIndicator(
                currentStep = currentStep,
                totalSteps = 4,
                onStepClick = { onStepChange(it) }
            )
        }

        // Form Step Body
        item {
            AnimatedContent(targetState = currentStep, label = "step_transition") { step ->
                when (step) {
                    1 -> Step1PhoneDetails(formData, onFormDataChange)
                    2 -> Step2IncidentDetails(formData, onFormDataChange)
                    3 -> Step3ProofOwnership(formData, onFormDataChange)
                    4 -> Step4ContactAndReward(formData, onFormDataChange)
                }
            }
        }

        // Navigation Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (currentStep > 1) {
                    Button(
                        onClick = { onStepChange(currentStep - 1) },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DarkSurfaceVariant,
                            contentColor = TextPrimary
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "السابق")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("السابق")
                    }
                }

                if (currentStep < 4) {
                    NeonButton(
                        text = "التالي",
                        onClick = { onStepChange(currentStep + 1) },
                        modifier = Modifier.weight(if (currentStep == 1) 2f else 1f),
                        icon = Icons.AutoMirrored.Filled.ArrowBack
                    )
                } else {
                    NeonButton(
                        text = if (uiState.isSubmittingReport) "جاري إرسال البلاغ..." else "إرسال البلاغ واعتماده فوراً 🚀",
                        onClick = onSubmitReport,
                        modifier = Modifier.weight(2f),
                        colorAccent = AlertRed,
                        enabled = !uiState.isSubmittingReport
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
fun Step1PhoneDetails(
    formData: NewReportFormData,
    onFormDataChange: (NewReportFormData.() -> NewReportFormData) -> Unit
) {
    GlassCard(cornerRadius = 18) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = NeonCyan)
                Spacer(modifier = Modifier.width(8.dp))
                Text("مواصفات الهاتف المسروق / المفقود", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            FormField(
                label = "الماركة (Brand):",
                value = formData.brand,
                placeholder = "مثل: Apple, Samsung, Xiaomi",
                onValueChange = { onFormDataChange { copy(brand = it) } }
            )

            FormField(
                label = "الموديل (Model):",
                value = formData.model,
                placeholder = "مثل: iPhone 15 Pro Max, S24 Ultra",
                onValueChange = { onFormDataChange { copy(model = it) } }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    FormField(
                        label = "السعة التخزينية:",
                        value = formData.storage,
                        placeholder = "256 GB",
                        onValueChange = { onFormDataChange { copy(storage = it) } }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    FormField(
                        label = "اللون:",
                        value = formData.color,
                        placeholder = "تيتانيوم أسود",
                        onValueChange = { onFormDataChange { copy(color = it) } }
                    )
                }
            }

            FormField(
                label = "رقم الـ IMEI 1 (الأساسي - 15 رقم):",
                value = formData.imei1,
                placeholder = "356938035643803",
                keyboardType = KeyboardType.Number,
                onValueChange = { onFormDataChange { copy(imei1 = it.filter { ch -> ch.isDigit() }.take(15)) } }
            )

            FormField(
                label = "رقم الـ IMEI 2 (اختياري):",
                value = formData.imei2,
                placeholder = "إذا كان الهاتف يدعم شريحتين",
                keyboardType = KeyboardType.Number,
                onValueChange = { onFormDataChange { copy(imei2 = it.filter { ch -> ch.isDigit() }.take(15)) } }
            )
        }
    }
}

@Composable
fun Step2IncidentDetails(
    formData: NewReportFormData,
    onFormDataChange: (NewReportFormData.() -> NewReportFormData) -> Unit
) {
    GlassCard(cornerRadius = 18) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationCity, contentDescription = null, tint = WarningAmber)
                Spacer(modifier = Modifier.width(8.dp))
                Text("تفاصيل الحادثة والموقع", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Text("نوع الواقعة:", color = TextSecondary, fontSize = 13.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IncidentTypeOption(
                    title = "سُرق مني (سرقة 🔴)",
                    selected = formData.incidentType == "سرقة",
                    accentColor = AlertRed,
                    onClick = { onFormDataChange { copy(incidentType = "سرقة") } },
                    modifier = Modifier.weight(1f)
                )
                IncidentTypeOption(
                    title = "فُقد مني (فقدان 🟡)",
                    selected = formData.incidentType == "فقدان",
                    accentColor = WarningAmber,
                    onClick = { onFormDataChange { copy(incidentType = "فقدان") } },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    FormField(
                        label = "المدينة / الموقع:",
                        value = formData.incidentCity,
                        placeholder = "الرياض",
                        onValueChange = { onFormDataChange { copy(incidentCity = it) } }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    FormField(
                        label = "تاريخ الواقعة:",
                        value = formData.incidentDate,
                        placeholder = "2024-05-08",
                        onValueChange = { onFormDataChange { copy(incidentDate = it) } }
                    )
                }
            }

            FormField(
                label = "وصف الحادثة وأي تفاصيل مميزة للجهاز:",
                value = formData.incidentDescription,
                placeholder = "أين فقدته؟ علامات مميزة كفر الحماية، خدوش معينة...",
                maxLines = 3,
                onValueChange = { onFormDataChange { copy(incidentDescription = it) } }
            )
        }
    }
}

@Composable
fun Step3ProofOwnership(
    formData: NewReportFormData,
    onFormDataChange: (NewReportFormData.() -> NewReportFormData) -> Unit
) {
    GlassCard(cornerRadius = 18) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text("إثبات الملكية لضمان حقوقك", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Text("نوع المستند الذي تثبت به ملكية الجهاز:", color = TextSecondary, fontSize = 13.sp)

            val proofOptions = listOf(
                "فاتورة شراء رسمية باسمك",
                "صورة كرتون الجهاز يظهر بها الـ IMEI",
                "محضر شرطة رسمي / بلاغ أمني",
                "عقد شريحة هاتف أو ضمان الوكيل"
            )

            proofOptions.forEach { opt ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (formData.proofType == opt) DarkSurfaceVariant else Color.Transparent)
                        .border(
                            1.dp,
                            if (formData.proofType == opt) NeonCyan.copy(alpha = 0.6f) else DarkBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { onFormDataChange { copy(proofType = opt) } }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = formData.proofType == opt,
                        onClick = { onFormDataChange { copy(proofType = opt) } },
                        colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(opt, color = TextPrimary, fontSize = 13.sp)
                }
            }

            // Upload Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceVariant)
                    .border(1.5.dp, EmeraldGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(formData.proofFileName, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("تم إرفاق المستند وجاهز للتدقيق الأمني", color = EmeraldGreen, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun Step4ContactAndReward(
    formData: NewReportFormData,
    onFormDataChange: (NewReportFormData.() -> NewReportFormData) -> Unit
) {
    GlassCard(cornerRadius = 18) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, contentDescription = null, tint = NeonCyan)
                Spacer(modifier = Modifier.width(8.dp))
                Text("بيانات التواصل والمكافأة المالية", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            FormField(
                label = "اسم المالك / مقدم البلاغ:",
                value = formData.contactName,
                placeholder = "الاسم الكامل",
                onValueChange = { onFormDataChange { copy(contactName = it) } }
            )

            FormField(
                label = "رقم الجوال للتواصل:",
                value = formData.contactPhone,
                placeholder = "05XXXXXXXX",
                keyboardType = KeyboardType.Phone,
                onValueChange = { onFormDataChange { copy(contactPhone = it) } }
            )

            // Hide Phone Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceVariant)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("إخفاء رقم هاتفي والتواصل عبر المنصة فقط", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("حماية خصوصيتك؛ سيتواصل الفني معك برسائل مشفرة داخل التطبيق", color = TextSecondary, fontSize = 11.sp)
                }
                Switch(
                    checked = formData.hidePhone,
                    onCheckedChange = { onFormDataChange { copy(hidePhone = it) } },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = NeonCyan,
                        checkedTrackColor = NeonCyan.copy(alpha = 0.4f)
                    )
                )
            }

            FormField(
                label = "المكافأة المالية لمن يعثر عليه (اختياري لتحفيز الأسواق):",
                value = formData.rewardAmount,
                placeholder = "مثال: 1,000 ر.س",
                onValueChange = { onFormDataChange { copy(rewardAmount = it) } }
            )
        }
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1
) {
    Column {
        Text(label, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextMuted, fontSize = 13.sp) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = maxLines == 1,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = DarkBorder,
                focusedContainerColor = DarkSurfaceVariant,
                unfocusedContainerColor = DarkSurfaceVariant
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
fun IncidentTypeOption(
    title: String,
    selected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) accentColor.copy(alpha = 0.15f) else DarkSurfaceVariant)
            .border(
                1.5.dp,
                if (selected) accentColor else DarkBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (selected) accentColor else TextSecondary,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
