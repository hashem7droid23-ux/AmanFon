package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AlertRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.WarningAmber

enum class PhoneStatus(val labelAr: String, val color: Color) {
    STOLEN("مسروق", AlertRed),
    LOST("مفقود", WarningAmber),
    RECOVERED("مسترجع", EmeraldGreen)
}

enum class TrackingStage(val step: Int, val titleAr: String, val descriptionAr: String) {
    RECEIVED(1, "تم استلام البلاغ", "تم قيد البلاغ وتثبيت بيانات الـ IMEI بالنظام الوطني"),
    VERIFYING(2, "قيد التحقق", "جاري التحقق من ملكية الجهاز وفحص المستندات والفواتير"),
    BROADCASTED(3, "تم تعميم الرقم في السوق", "تم إرسال تنبيه حظر لكافة محلات الصيانة وفنيي البيع"),
    LOCATED(4, "تم رصد الجهاز", "تم اكتشاف محاولة فحص أو بيع للجهاز في أحد المراكز المعتمدة"),
    RECOVERED(5, "تم الاسترجاع", "تم استعادة الهاتف بنجاح وتسليمه لصاحبه الشرعي")
}

data class TimelineEvent(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val location: String,
    val isCompleted: Boolean,
    val isAlert: Boolean = false
)

data class PhoneReport(
    val id: String, // e.g. "REP-2024-8891"
    val imei1: String,
    val imei2: String = "",
    val brand: String,
    val model: String,
    val storage: String,
    val color: String,
    val status: PhoneStatus,
    val incidentType: String, // "سرقة" or "فقدان"
    val incidentDate: String,
    val incidentCity: String,
    val incidentDescription: String,
    val proofDocumentType: String, // "فاتورة شراء رسمية", "كرتون الجهاز", "محضر شرطة رسمي"
    val proofDocUriOrName: String = "document_proof.pdf",
    val contactName: String,
    val contactPhone: String,
    val hidePhoneContact: Boolean = false,
    val rewardAmount: String = "",
    val currentStage: TrackingStage,
    val timeline: List<TimelineEvent> = emptyList(),
    val reportedAt: String = "2024-05-10 14:30",
    val lastLocationSighting: String? = null
)

data class TechnicianLog(
    val id: String,
    val imei: String,
    val deviceName: String,
    val timestamp: String,
    val status: PhoneStatus?,
    val isClean: Boolean,
    val shopName: String,
    val technicianName: String,
    val city: String,
    val notes: String = ""
)

object ImeiValidator {
    /**
     * Calculates the Luhn check digit for a 14-digit IMEI prefix.
     */
    fun calculateLuhnDigit(imei14: String): Int {
        var sum = 0
        for (i in imei14.indices) {
            var digit = imei14[i] - '0'
            if (i % 2 == 1) {
                digit *= 2
                if (digit > 9) digit -= 9
            }
            sum += digit
        }
        val remainder = sum % 10
        return if (remainder == 0) 0 else 10 - remainder
    }

    /**
     * Checks if a 15-digit IMEI satisfies the Luhn algorithm.
     */
    fun isValidLuhn(imei: String): Boolean {
        if (imei.length != 15 || !imei.all { it.isDigit() }) return false
        var sum = 0
        for (i in 0 until 14) {
            var digit = imei[i] - '0'
            if (i % 2 == 1) {
                digit *= 2
                if (digit > 9) digit -= 9
            }
            sum += digit
        }
        val checkDigit = imei[14] - '0'
        val expected = if (sum % 10 == 0) 0 else 10 - (sum % 10)
        return checkDigit == expected
    }

    /**
     * Formats IMEI in readable groups: 35-693803-564380-3
     */
    fun formatImei(imei: String): String {
        val clean = imei.filter { it.isDigit() }
        if (clean.length < 15) return clean
        return "${clean.substring(0, 2)} ${clean.substring(2, 8)} ${clean.substring(8, 14)} ${clean.substring(14, 15)}"
    }
}
