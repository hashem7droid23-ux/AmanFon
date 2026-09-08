package com.example.data

import com.example.model.PhoneReport
import com.example.model.PhoneStatus
import com.example.model.TechnicianLog
import com.example.model.TimelineEvent
import com.example.model.TrackingStage

object MockDataProvider {

    val initialReports: List<PhoneReport> = listOf(
        PhoneReport(
            id = "REP-2024-8891",
            imei1 = "356938035643803",
            imei2 = "356938035643804",
            brand = "Apple",
            model = "iPhone 15 Pro Max",
            storage = "256 GB",
            color = "تيتانيوم طبيعي (Natural Titanium)",
            status = PhoneStatus.STOLEN,
            incidentType = "سرقة",
            incidentDate = "2024-05-02",
            incidentCity = "الرياض",
            incidentDescription = "تمت سرقة الهاتف من داخل حقيبة اليد في مركز تجاري بحي العليا، تم تقديم محضر شرطة فوري.",
            proofDocumentType = "محضر شرطة رسمي وفاتورة الشراء",
            contactName = "سعود بن فهد السبيعي",
            contactPhone = "0501234567",
            hidePhoneContact = true,
            rewardAmount = "1,500 ر.س",
            currentStage = TrackingStage.LOCATED,
            reportedAt = "2024-05-02 18:20",
            lastLocationSighting = "محل إلكترونيات وصيانة بحي البطحاء، الرياض",
            timeline = listOf(
                TimelineEvent(
                    id = "e1",
                    title = "تم استلام البلاغ والتحقق الأولي",
                    description = "تم تسجيل رقم الـ IMEI ومطابقة بيانات الشراء برقم الهوية",
                    timestamp = "02 مايو 2024 - 18:30",
                    location = "المنصة المركزية - الرياض",
                    isCompleted = true
                ),
                TimelineEvent(
                    id = "e2",
                    title = "مراجعة واعتماد إثبات الملكية",
                    description = "تم قبول صورة الكرتون والرقم التسلسلي ومحضر الشرطة رقم 44921",
                    timestamp = "03 مايو 2024 - 10:15",
                    location = "قسم التدقيق الأمني",
                    isCompleted = true
                ),
                TimelineEvent(
                    id = "e3",
                    title = "تعميم بيانات الجهاز على شبكة المحلات",
                    description = "تم إدراج الهاتف بالقائمة السوداء وإشعار أكثر من 4,200 نقطة صيانة معتمدة",
                    timestamp = "03 مايو 2024 - 14:00",
                    location = "شبكة أسواق الاتصالات",
                    isCompleted = true
                ),
                TimelineEvent(
                    id = "e4",
                    title = "رصد الجهاز في متجر صيانة",
                    description = "قام فني معتمد بفحص الـ IMEI أثناء طلب فتح القفل وطلب تسليمه للجهات المختصة",
                    timestamp = "07 مايو 2024 - 21:40",
                    location = "سوق البطحاء للاتصالات، الرياض",
                    isCompleted = true,
                    isAlert = true
                ),
                TimelineEvent(
                    id = "e5",
                    title = "استرداد الهاتف وتسليمه للمالك",
                    description = "جاري التنسيق مع الجهات الأمنية لتسليم الهاتف للمالك واستلام المكافأة",
                    timestamp = "قيد الإجراء النهائي",
                    location = "مركز شرطة العليا",
                    isCompleted = false
                )
            )
        ),
        PhoneReport(
            id = "REP-2024-7124",
            imei1 = "864321045678901",
            imei2 = "",
            brand = "Samsung",
            model = "Galaxy S24 Ultra",
            storage = "512 GB",
            color = "تيتانيوم أسود (Titanium Black)",
            status = PhoneStatus.LOST,
            incidentType = "فقدان",
            incidentDate = "2024-05-04",
            incidentCity = "جدة",
            incidentDescription = "نسيت الهاتف في سيارة أجرة بعد الوصول إلى الواجهة البحرية، الجهاز مغلق بالبصمة.",
            proofDocumentType = "كرتون الجهاز الأصلي والفاتورة الإلكترونية",
            contactName = "عبدالرحمن الشريف",
            contactPhone = "0559876543",
            hidePhoneContact = false,
            rewardAmount = "800 ر.س",
            currentStage = TrackingStage.BROADCASTED,
            reportedAt = "2024-05-04 22:10",
            lastLocationSighting = null,
            timeline = listOf(
                TimelineEvent(
                    id = "e21",
                    title = "تم استلام البلاغ",
                    description = "تم تسجيل تفاصيل الفقدان وتفعيل الرصد الفوري",
                    timestamp = "04 مايو 2024 - 22:15",
                    location = "المنصة - جدة",
                    isCompleted = true
                ),
                TimelineEvent(
                    id = "e22",
                    title = "توثيق ملكية الجهاز",
                    description = "تمت مطابقة الفاتورة الصادرة من الوكيل المعتمد",
                    timestamp = "05 مايو 2024 - 09:00",
                    location = "نظام التوثيق الآلي",
                    isCompleted = true
                ),
                TimelineEvent(
                    id = "e23",
                    title = "تعميم الرقم على الأسواق",
                    description = "جاري انتظار أي عملية فحص أو محاولة تشغيل برقم الـ IMEI",
                    timestamp = "05 مايو 2024 - 11:30",
                    location = "المنطقة الغربية - جدة ومكة",
                    isCompleted = true
                ),
                TimelineEvent(
                    id = "e24",
                    title = "رصد الجهاز في نقطة بيع",
                    description = "في انتظار فحص الجهاز في نقاط الفحص المعتمدة",
                    timestamp = "--",
                    location = "غير محدد بعد",
                    isCompleted = false
                ),
                TimelineEvent(
                    id = "e25",
                    title = "استرداد الهاتف",
                    description = "إعادة الجهاز للمالك الأصلي وإغلاق البلاغ",
                    timestamp = "--",
                    location = "جدة",
                    isCompleted = false
                )
            )
        ),
        PhoneReport(
            id = "REP-2024-5510",
            imei1 = "352109876543210",
            imei2 = "",
            brand = "Xiaomi",
            model = "14 Ultra Leica",
            storage = "512 GB",
            color = "أبيض سيراميك",
            status = PhoneStatus.RECOVERED,
            incidentType = "سرقة",
            incidentDate = "2024-04-18",
            incidentCity = "الدمام",
            incidentDescription = "سُرق من سيارة متوقفة وتم الإبلاغ الفوري، وتم العثور عليه بفضل تنبيه أحد محلات الصيانة.",
            proofDocumentType = "محضر شرطة وفاتورة الشراء",
            contactName = "محمد الدوسري",
            contactPhone = "0543219876",
            hidePhoneContact = false,
            rewardAmount = "500 ر.س",
            currentStage = TrackingStage.RECOVERED,
            reportedAt = "2024-04-18 16:00",
            lastLocationSighting = "مجمع أبراج الاتصالات، الخبر",
            timeline = listOf(
                TimelineEvent("e31", "تم تسجيل البلاغ", "تم إدراج البلاغ بالرقم المرجعي", "18 أبريل 2024", "الدمام", true),
                TimelineEvent("e32", "التحقق من المستندات", "تم اعتماد محضر الشرطة", "19 أبريل 2024", "المنصة", true),
                TimelineEvent("e33", "التعميم على الأسواق", "إرسال بيانات الـ IMEI للمتاجر", "19 أبريل 2024", "المنطقة الشرقية", true),
                TimelineEvent("e34", "رصد الجهاز وضبطه", "قام محل صيانة بالإبلاغ الفوري عند فحص الـ IMEI", "23 أبريل 2024", "الخبر", true, isAlert = true),
                TimelineEvent("e35", "تم الاسترجاع بنجاح", "تم تسليم الجهاز للمالك وإغلاق ملف القضية", "25 أبريل 2024", "الدمام", true)
            )
        ),
        PhoneReport(
            id = "REP-2024-3209",
            imei1 = "359876123456789",
            imei2 = "",
            brand = "Google",
            model = "Pixel 8 Pro",
            storage = "128 GB",
            color = "أزرق باي (Bay Blue)",
            status = PhoneStatus.STOLEN,
            incidentType = "سرقة",
            incidentDate = "2024-05-06",
            incidentCity = "المدينة المنورة",
            incidentDescription = "فقدان في حديقة عامة وبعد الاتصال بالرقم تم إغلاقه مباشرة.",
            proofDocumentType = "كرتون الجهاز",
            contactName = "عمر باقيس",
            contactPhone = "0567788990",
            hidePhoneContact = true,
            rewardAmount = "600 ر.س",
            currentStage = TrackingStage.VERIFYING,
            reportedAt = "2024-05-06 20:45",
            lastLocationSighting = null,
            timeline = listOf(
                TimelineEvent("e41", "استلام البلاغ", "تم تدوين بيانات الجهاز ورقم الهاتف", "06 مايو 2024", "المدينة", true),
                TimelineEvent("e42", "قيد التدقيق والتحقق", "جاري التحقق من الرقم التسلسلي", "07 مايو 2024", "المنصة", true),
                TimelineEvent("e43", "تعميم الـ IMEI", "سيتم التعميم فور إتمام التحقق", "--", "المتاجر", false),
                TimelineEvent("e44", "الرصد والمتابعة", "في انتظار رصد الجهاز", "--", "--", false),
                TimelineEvent("e45", "الاسترداد النهائي", "تسليم الجهاز للمالك", "--", "--", false)
            )
        )
    )

    val initialTechnicianLogs: List<TechnicianLog> = listOf(
        TechnicianLog(
            id = "LOG-901",
            imei = "356938035643803",
            deviceName = "iPhone 15 Pro Max",
            timestamp = "اليوم - 11:45 ص",
            status = PhoneStatus.STOLEN,
            isClean = false,
            shopName = "مركز إبداع للاتصالات والصيانة",
            technicianName = "م. خالد الأنصاري",
            city = "الرياض",
            notes = "جاء عميل يطلب عمل سوفتوير وتخطي حساب iCloud، تم التحفظ على الجهاز وإرسال إشعار فوري للنظام"
        ),
        TechnicianLog(
            id = "LOG-902",
            imei = "869234051234567",
            deviceName = "Samsung S23 FE",
            timestamp = "اليوم - 10:15 ص",
            status = null,
            isClean = true,
            shopName = "مركز إبداع للاتصالات والصيانة",
            technicianName = "م. خالد الأنصاري",
            city = "الرياض",
            notes = "فحص روتيني قبل شراء جهاز مستعمل، الجهاز نظيف بنسبة 100%"
        ),
        TechnicianLog(
            id = "LOG-903",
            imei = "864321045678901",
            deviceName = "Galaxy S24 Ultra",
            timestamp = "أمس - 19:20 م",
            status = PhoneStatus.LOST,
            isClean = false,
            shopName = "متجر تقنية المستقبل",
            technicianName = "م. فيصل الغامدي",
            city = "جدة",
            notes = "تم مطابقة الرقم، الجهاز مسجل كمفقود مع مكافأة 800 ريال"
        )
    )
}
