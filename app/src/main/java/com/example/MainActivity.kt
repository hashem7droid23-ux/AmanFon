package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ImeiCheckerScreen
import com.example.ui.screens.ReportWizardScreen
import com.example.ui.screens.TechnicianModeScreen
import com.example.ui.screens.TrackingTimelineScreen
import com.example.ui.theme.AlertRed
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonCyanGlow
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.PhoneSecurityViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    PhoneSecurityApp()
                }
            }
        }
    }
}

@Composable
fun PhoneSecurityApp(
    viewModel: PhoneSecurityViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBg,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            AppBottomNavigationBar(
                currentScreen = uiState.currentScreen,
                onSelectScreen = { viewModel.navigateTo(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentScreen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        uiState = uiState,
                        onNavigate = { viewModel.navigateTo(it) },
                        onSelectReport = { viewModel.selectReport(it) }
                    )
                }
                AppScreen.IMEI_CHECKER -> {
                    ImeiCheckerScreen(
                        uiState = uiState,
                        onImeiChange = { viewModel.setImeiQuery(it) },
                        onScanSimulate = { viewModel.simulateCameraScan(it) },
                        onPerformCheck = { viewModel.performImeiCheck() },
                        onReportSighting = { reportId, shopLoc, notes ->
                            viewModel.reportSighting(reportId, shopLoc, notes)
                        },
                        onViewTimeline = { viewModel.selectReport(it) }
                    )
                }
                AppScreen.NEW_REPORT -> {
                    ReportWizardScreen(
                        uiState = uiState,
                        onStepChange = { viewModel.setFormStep(it) },
                        onFormDataChange = { viewModel.updateFormData(it) },
                        onSubmitReport = { viewModel.submitReport() }
                    )
                }
                AppScreen.TIMELINE -> {
                    TrackingTimelineScreen(
                        uiState = uiState,
                        onSelectReport = { viewModel.selectReport(it) },
                        onAdvanceStage = { viewModel.advanceReportStage(it) }
                    )
                }
                AppScreen.TECHNICIAN_MODE -> {
                    TechnicianModeScreen(
                        uiState = uiState,
                        onNavigate = { viewModel.navigateTo(it) },
                        onQuickCheck = { imei ->
                            viewModel.setImeiQuery(imei)
                            viewModel.navigateTo(AppScreen.IMEI_CHECKER)
                            viewModel.performImeiCheck()
                        },
                        onAlertPoliceAndOwner = { imei, notes ->
                            val match = uiState.reports.find { it.imei1 == imei || it.imei2 == imei }
                            if (match != null) {
                                viewModel.reportSighting(match.id, uiState.shopName + " - " + uiState.techCity, notes)
                            } else {
                                viewModel.showNotification("تم إرسال إشعار الاشتباه بالرقم $imei للتحقق المركزي 🚨")
                            }
                        }
                    )
                }
            }

            // In-app Notification Banner
            AnimatedVisibility(
                visible = uiState.notificationMessage != null,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it }),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                uiState.notificationMessage?.let { msg ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkSurfaceVariant)
                            .border(1.5.dp, NeonCyan, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = msg,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            IconButton(
                                onClick = { viewModel.dismissNotification() },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "إغلاق",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class NavItem(val screen: AppScreen, val label: String, val icon: ImageVector)

@Composable
fun AppBottomNavigationBar(
    currentScreen: AppScreen,
    onSelectScreen: (AppScreen) -> Unit
) {
    val items = listOf(
        NavItem(AppScreen.HOME, "الرئيسية", Icons.Default.Home),
        NavItem(AppScreen.IMEI_CHECKER, "فحص IMEI", Icons.Default.QrCodeScanner),
        NavItem(AppScreen.NEW_REPORT, "تقديم بلاغ", Icons.Default.AddCircle),
        NavItem(AppScreen.TIMELINE, "سجل التتبع", Icons.Default.Timeline),
        NavItem(AppScreen.TECHNICIAN_MODE, "وضع الفني", Icons.Default.Build)
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 0.8.dp, color = DarkBorder)
            .navigationBarsPadding(),
        containerColor = DarkSurface,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val isSelected = currentScreen == item.screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectScreen(item.screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonCyan,
                    selectedTextColor = NeonCyan,
                    indicatorColor = NeonCyanGlow,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                )
            )
        }
    }
}
