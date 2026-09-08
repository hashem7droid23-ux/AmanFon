package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockDataProvider
import com.example.model.ImeiValidator
import com.example.model.PhoneReport
import com.example.model.PhoneStatus
import com.example.model.TechnicianLog
import com.example.model.TimelineEvent
import com.example.model.TrackingStage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class AppScreen {
    HOME,
    IMEI_CHECKER,
    NEW_REPORT,
    TIMELINE,
    TECHNICIAN_MODE
}

sealed class ImeiCheckOutcome {
    object Idle : ImeiCheckOutcome()
    data class Clean(val imei: String, val checkedAt: String) : ImeiCheckOutcome()
    data class Reported(val report: PhoneReport, val checkedAt: String) : ImeiCheckOutcome()
    data class Invalid(val message: String) : ImeiCheckOutcome()
}

data class NewReportFormData(
    val brand: String = "",
    val model: String = "",
    val storage: String = "256 GB",
    val color: String = "",
    val imei1: String = "",
    val imei2: String = "",
    val incidentType: String = "سرقة",
    val incidentDate: String = "2024-05-08",
    val incidentCity: String = "الرياض",
    val incidentDescription: String = "",
    val proofType: String = "فاتورة شراء رسمية",
    val proofFileName: String = "فاتورة_الشراء_المعتمدة.pdf",
    val proofFileUploaded: Boolean = true,
    val contactName: String = "",
    val contactPhone: String = "",
    val hidePhone: Boolean = false,
    val rewardAmount: String = "1,000 ر.س"
)

data class UiState(
    val currentScreen: AppScreen = AppScreen.HOME,
    val reports: List<PhoneReport> = MockDataProvider.initialReports,
    val technicianLogs: List<TechnicianLog> = MockDataProvider.initialTechnicianLogs,
    val selectedReportId: String? = "REP-2024-8891",
    // Stats
    val totalRecovered: Int = 1842,
    val totalChecked: Int = 64319,
    // IMEI Checker
    val imeiQuery: String = "356938035643803",
    val isScanning: Boolean = false,
    val isChecking: Boolean = false,
    val checkOutcome: ImeiCheckOutcome = ImeiCheckOutcome.Idle,
    val notificationMessage: String? = null,
    // New Report Wizard
    val formStep: Int = 1,
    val formData: NewReportFormData = NewReportFormData(),
    val isSubmittingReport: Boolean = false,
    val lastCreatedReportId: String? = null,
    // Technician
    val shopName: String = "مركز إبداع للاتصالات والصيانة",
    val techName: String = "م. خالد الأنصاري",
    val techCity: String = "الرياض"
)

class PhoneSecurityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun selectReport(reportId: String) {
        _uiState.update {
            it.copy(
                selectedReportId = reportId,
                currentScreen = AppScreen.TIMELINE
            )
        }
    }

    fun showNotification(msg: String) {
        _uiState.update { it.copy(notificationMessage = msg) }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(notificationMessage = null) }
    }

    // --- IMEI Checker Operations ---
    fun setImeiQuery(imei: String) {
        val clean = imei.filter { it.isDigit() }.take(15)
        _uiState.update {
            it.copy(
                imeiQuery = clean,
                checkOutcome = ImeiCheckOutcome.Idle
            )
        }
    }

    fun simulateCameraScan(targetImei: String = "356938035643803") {
        viewModelScope.launch {
            _uiState.update { it.copy(isScanning = true) }
            delay(1600) // Realistic camera laser scan
            _uiState.update {
                it.copy(
                    isScanning = false,
                    imeiQuery = targetImei
                )
            }
            performImeiCheck()
        }
    }

    fun performImeiCheck() {
        val imei = _uiState.value.imeiQuery.trim()
        if (imei.length < 14) {
            _uiState.update {
                it.copy(
                    checkOutcome = ImeiCheckOutcome.Invalid("يرجى إدخال رقم الـ IMEI المكون من 15 رقماً كاملاً")
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isChecking = true) }
            delay(600) // quick verification feeling

            val matchingReport = _uiState.value.reports.find {
                it.imei1 == imei || (it.imei2.isNotBlank() && it.imei2 == imei)
            }

            val outcome = if (matchingReport != null) {
                ImeiCheckOutcome.Reported(matchingReport, "اليوم، منذ دقيقة")
            } else {
                ImeiCheckOutcome.Clean(imei, "اليوم، منذ دقيقة")
            }

            // Log technician check
            val newLog = TechnicianLog(
                id = "LOG-${Random.nextInt(1000, 9999)}",
                imei = imei,
                deviceName = matchingReport?.let { "${it.brand} ${it.model}" } ?: "جهاز مفحوص",
                timestamp = "الآن",
                status = matchingReport?.status,
                isClean = matchingReport == null,
                shopName = _uiState.value.shopName,
                technicianName = _uiState.value.techName,
                city = _uiState.value.techCity,
                notes = if (matchingReport != null) "تحذير: رقم مطابق لبلاغ نشط (${matchingReport.status.labelAr})" else "سليم ونظيف"
            )

            _uiState.update { current ->
                current.copy(
                    isChecking = false,
                    checkOutcome = outcome,
                    totalChecked = current.totalChecked + 1,
                    technicianLogs = listOf(newLog) + current.technicianLogs
                )
            }
        }
    }

    fun reportSighting(reportId: String, shopLocation: String, notes: String) {
        val nowFormatted = "اليوم - " + java.text.SimpleDateFormat("hh:mm a", java.util.Locale.forLanguageTag("ar")).format(java.util.Date())
        _uiState.update { current ->
            val updatedReports = current.reports.map { report ->
                if (report.id == reportId) {
                    val updatedTimeline = report.timeline + TimelineEvent(
                        id = "sight-${System.currentTimeMillis()}",
                        title = "تنبيه رصد فوري من نقطة فحص",
                        description = "تم رصد ومحاولة فحص الجهاز في: $shopLocation. ملاحظات الفني: $notes",
                        timestamp = nowFormatted,
                        location = shopLocation,
                        isCompleted = true,
                        isAlert = true
                    )
                    report.copy(
                        currentStage = TrackingStage.LOCATED,
                        lastLocationSighting = shopLocation,
                        timeline = updatedTimeline
                    )
                } else report
            }
            current.copy(
                reports = updatedReports,
                notificationMessage = "تم إرسال بلاغ الرصد وتحديد الموقع الفوري للمالك والجهات المختصة بنجاح! 🚨"
            )
        }
    }

    // --- Timeline Simulation actions ---
    fun advanceReportStage(reportId: String) {
        _uiState.update { current ->
            val updated = current.reports.map { report ->
                if (report.id == reportId) {
                    val nextStage = when (report.currentStage) {
                        TrackingStage.RECEIVED -> TrackingStage.VERIFYING
                        TrackingStage.VERIFYING -> TrackingStage.BROADCASTED
                        TrackingStage.BROADCASTED -> TrackingStage.LOCATED
                        TrackingStage.LOCATED -> TrackingStage.RECOVERED
                        TrackingStage.RECOVERED -> TrackingStage.RECOVERED
                    }
                    val nextStatus = if (nextStage == TrackingStage.RECOVERED) PhoneStatus.RECOVERED else report.status
                    val newEvent = TimelineEvent(
                        id = "evt-${System.currentTimeMillis()}",
                        title = nextStage.titleAr,
                        description = nextStage.descriptionAr,
                        timestamp = "الآن",
                        location = report.incidentCity,
                        isCompleted = true,
                        isAlert = nextStage == TrackingStage.LOCATED
                    )
                    report.copy(
                        currentStage = nextStage,
                        status = nextStatus,
                        timeline = report.timeline + newEvent
                    )
                } else report
            }
            val recoveredDelta = if (updated.any { it.id == reportId && it.currentStage == TrackingStage.RECOVERED }) 1 else 0
            current.copy(
                reports = updated,
                totalRecovered = current.totalRecovered + recoveredDelta,
                notificationMessage = "تم تحديث مرحلة البلاغ بنجاح! ⏱️"
            )
        }
    }

    // --- Multi-Step Report Form ---
    fun updateFormData(update: NewReportFormData.() -> NewReportFormData) {
        _uiState.update { it.copy(formData = it.formData.update()) }
    }

    fun setFormStep(step: Int) {
        if (step in 1..4) {
            _uiState.update { it.copy(formStep = step) }
        }
    }

    fun submitReport() {
        val form = _uiState.value.formData
        if (form.brand.isBlank() || form.model.isBlank() || form.imei1.isBlank()) {
            showNotification("يرجى إكمال بيانات الهاتف ورقم الـ IMEI الأساسي")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmittingReport = true) }
            delay(1000)

            val newId = "REP-2024-${Random.nextInt(1000, 9999)}"
            val status = if (form.incidentType == "سرقة") PhoneStatus.STOLEN else PhoneStatus.LOST

            val newReport = PhoneReport(
                id = newId,
                imei1 = form.imei1.filter { it.isDigit() },
                imei2 = form.imei2.filter { it.isDigit() },
                brand = form.brand,
                model = form.model,
                storage = form.storage,
                color = form.color.ifBlank { "غير محدد" },
                status = status,
                incidentType = form.incidentType,
                incidentDate = form.incidentDate,
                incidentCity = form.incidentCity,
                incidentDescription = form.incidentDescription.ifBlank { "بلاغ مسجل عبر تطبيق أمان فون" },
                proofDocumentType = form.proofType,
                proofDocUriOrName = form.proofFileName,
                contactName = form.contactName.ifBlank { "صاحب الجهاز" },
                contactPhone = form.contactPhone.ifBlank { "0500000000" },
                hidePhoneContact = form.hidePhone,
                rewardAmount = form.rewardAmount,
                currentStage = TrackingStage.RECEIVED,
                reportedAt = "اليوم - الآن",
                timeline = listOf(
                    TimelineEvent(
                        id = "init-1",
                        title = "تم استلام البلاغ",
                        description = "تم قيد البلاغ في قاعدة البيانات الوطنية للمفقودات",
                        timestamp = "الآن",
                        location = form.incidentCity,
                        isCompleted = true
                    ),
                    TimelineEvent(
                        id = "init-2",
                        title = "قيد التحقق من المستندات",
                        description = "المستند المرفق: ${form.proofType}",
                        timestamp = "قيد المعالجة",
                        location = "المنصة المركزية",
                        isCompleted = false
                    ),
                    TimelineEvent(
                        id = "init-3",
                        title = "التعميم في السوق",
                        description = "سيتم إشعار كافة نقاط الصيانة بعد التحقق",
                        timestamp = "--",
                        location = "محلات الاتصالات والصيانة",
                        isCompleted = false
                    ),
                    TimelineEvent(
                        id = "init-4",
                        title = "رصد الجهاز",
                        description = "تتبع أي محاولة فحص IMEI",
                        timestamp = "--",
                        location = "--",
                        isCompleted = false
                    ),
                    TimelineEvent(
                        id = "init-5",
                        title = "تم الاسترجاع",
                        description = "إعادة الجهاز لمالكه",
                        timestamp = "--",
                        location = "--",
                        isCompleted = false
                    )
                )
            )

            _uiState.update { current ->
                current.copy(
                    isSubmittingReport = false,
                    reports = listOf(newReport) + current.reports,
                    lastCreatedReportId = newId,
                    selectedReportId = newId,
                    formStep = 1,
                    formData = NewReportFormData(),
                    currentScreen = AppScreen.TIMELINE,
                    notificationMessage = "تم تسجيل البلاغ بنجاح! كود التتبع: $newId 🎉"
                )
            }
        }
    }
}
