package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LeaveRepository
import com.example.model.LeaveRequest
import com.example.model.LeaveStatus
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.ui.components.CorporateHeader
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.AdminLoginScreen
import com.example.ui.screens.admin.AllRequestsScreen
import com.example.ui.screens.admin.LeavePoliciesScreen
import com.example.ui.screens.admin.ReportsScreen
import com.example.ui.screens.employee.EmployeeHomeScreen
import com.example.ui.screens.employee.LeaveBalanceScreen
import com.example.ui.screens.employee.MyRequestsScreen
import com.example.ui.screens.employee.RequestLeaveScreen
import com.example.ui.screens.manager.ApproveRejectDetailScreen
import com.example.ui.screens.manager.ManagerLoginScreen
import com.example.ui.screens.manager.PendingApprovalsScreen
import com.example.ui.screens.manager.TeamLeaveCalendarScreen
import com.example.ui.theme.CorporateBackground
import com.example.ui.theme.CorporateBlue
import com.example.ui.theme.CorporateBlueLight
import com.example.ui.theme.CorporateBorder
import com.example.ui.theme.CorporateSurface
import com.example.ui.theme.CorporateSurfaceVariant
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusRejected
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

// Screen Enumeration covering all 13 required screens
enum class ScreenDestination(val title: String, val icon: ImageVector, val role: UserRole) {
    // Employee Screens (1-4)
    EMPLOYEE_HOME("Home", Icons.Default.Home, UserRole.EMPLOYEE),
    REQUEST_LEAVE("Request Leave", Icons.Default.AddCircle, UserRole.EMPLOYEE),
    MY_REQUESTS("My Requests", Icons.Default.ListAlt, UserRole.EMPLOYEE),
    LEAVE_BALANCE("Leave Balance", Icons.Default.History, UserRole.EMPLOYEE),

    // Manager Screens (5-8)
    MANAGER_LOGIN("Manager Login", Icons.Default.Lock, UserRole.MANAGER),
    PENDING_APPROVALS("Approvals", Icons.Default.HourglassTop, UserRole.MANAGER),
    APPROVE_REJECT_DETAIL("Review Detail", Icons.Default.CheckCircle, UserRole.MANAGER),
    TEAM_CALENDAR("Team Calendar", Icons.Default.CalendarMonth, UserRole.MANAGER),

    // Admin Screens (9-13)
    ADMIN_LOGIN("Admin Login", Icons.Default.Lock, UserRole.ADMIN),
    ADMIN_DASHBOARD("Dashboard", Icons.Default.Dashboard, UserRole.ADMIN),
    LEAVE_POLICIES("Policies Setup", Icons.Default.Policy, UserRole.ADMIN),
    ALL_REQUESTS("All Requests", Icons.Default.ListAlt, UserRole.ADMIN),
    REPORTS("Reports & Trends", Icons.Default.Assessment, UserRole.ADMIN)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveEaseApp() {
    val currentUser by LeaveRepository.currentUser.collectAsState()
    val requests by LeaveRepository.requests.collectAsState()
    val policies by LeaveRepository.policies.collectAsState()
    val accrualHistory by LeaveRepository.accrualHistory.collectAsState()
    val managerLoggedIn by LeaveRepository.managerLoggedIn.collectAsState()
    val adminLoggedIn by LeaveRepository.adminLoggedIn.collectAsState()

    val balances = LeaveRepository.getEmployeeBalances(currentUser.id)
    val pendingApprovalsCount = requests.count { it.status == LeaveStatus.PENDING }

    var currentScreen by remember { mutableStateOf(ScreenDestination.EMPLOYEE_HOME) }
    var selectedRequestForDetail by remember { mutableStateOf<LeaveRequest?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Handle system back navigation safely
    BackHandler(
        enabled = currentScreen != ScreenDestination.EMPLOYEE_HOME &&
                currentScreen != ScreenDestination.PENDING_APPROVALS &&
                currentScreen != ScreenDestination.ADMIN_DASHBOARD
    ) {
        currentScreen = when (currentUser.role) {
            UserRole.EMPLOYEE -> ScreenDestination.EMPLOYEE_HOME
            UserRole.MANAGER -> ScreenDestination.PENDING_APPROVALS
            UserRole.ADMIN -> ScreenDestination.ADMIN_DASHBOARD
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(CorporateBackground)) {
        val isDesktopWide = maxWidth >= 760.dp

        Row(modifier = Modifier.fillMaxSize()) {
            // Desktop-first Sidebar Navigation
            if (isDesktopWide) {
                CorporateDesktopSidebar(
                    currentUser = currentUser,
                    currentScreen = currentScreen,
                    pendingCount = pendingApprovalsCount,
                    onNavigate = { screen ->
                        currentScreen = screen
                    },
                    onSwitchRole = { role ->
                        LeaveRepository.switchRole(role)
                        currentScreen = when (role) {
                            UserRole.EMPLOYEE -> ScreenDestination.EMPLOYEE_HOME
                            UserRole.MANAGER -> if (managerLoggedIn) ScreenDestination.PENDING_APPROVALS else ScreenDestination.MANAGER_LOGIN
                            UserRole.ADMIN -> if (adminLoggedIn) ScreenDestination.ADMIN_DASHBOARD else ScreenDestination.ADMIN_LOGIN
                        }
                    }
                )
            }

            // Main Content Area with Corporate Top Header
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // Top Corporate Header (present in both desktop and mobile, with role switcher)
                CorporateHeader(
                    currentUser = currentUser,
                    onRoleSelected = { role ->
                        LeaveRepository.switchRole(role)
                        currentScreen = when (role) {
                            UserRole.EMPLOYEE -> ScreenDestination.EMPLOYEE_HOME
                            UserRole.MANAGER -> if (managerLoggedIn) ScreenDestination.PENDING_APPROVALS else ScreenDestination.MANAGER_LOGIN
                            UserRole.ADMIN -> if (adminLoggedIn) ScreenDestination.ADMIN_DASHBOARD else ScreenDestination.ADMIN_LOGIN
                        }
                    },
                    modifier = Modifier.statusBarsPadding()
                )

                // Screen Body
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "ScreenTransition"
                    ) { target ->
                        when (target) {
                            // Employee Screens
                            ScreenDestination.EMPLOYEE_HOME -> {
                                EmployeeHomeScreen(
                                    user = currentUser,
                                    balances = balances,
                                    requests = requests,
                                    onRequestLeaveClick = { currentScreen = ScreenDestination.REQUEST_LEAVE },
                                    onViewMyRequestsClick = { currentScreen = ScreenDestination.MY_REQUESTS },
                                    onViewBalancesClick = { currentScreen = ScreenDestination.LEAVE_BALANCE }
                                )
                            }

                            ScreenDestination.REQUEST_LEAVE -> {
                                RequestLeaveScreen(
                                    balances = balances,
                                    onBack = { currentScreen = ScreenDestination.EMPLOYEE_HOME },
                                    onSubmitSuccess = { type, start, end, days, reason, attachment ->
                                        LeaveRepository.submitRequest(type, start, end, days, reason, attachment)
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Leave request ($days days) submitted for manager approval!")
                                        }
                                        currentScreen = ScreenDestination.MY_REQUESTS
                                    }
                                )
                            }

                            ScreenDestination.MY_REQUESTS -> {
                                MyRequestsScreen(
                                    user = currentUser,
                                    requests = requests,
                                    onRequestLeaveClick = { currentScreen = ScreenDestination.REQUEST_LEAVE },
                                    onCancelRequest = { reqId ->
                                        LeaveRepository.cancelRequest(reqId)
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Leave request $reqId was cancelled.")
                                        }
                                    },
                                    onBack = { currentScreen = ScreenDestination.EMPLOYEE_HOME }
                                )
                            }

                            ScreenDestination.LEAVE_BALANCE -> {
                                LeaveBalanceScreen(
                                    user = currentUser,
                                    balances = balances,
                                    accrualHistory = accrualHistory,
                                    onBack = { currentScreen = ScreenDestination.EMPLOYEE_HOME }
                                )
                            }

                            // Manager Screens
                            ScreenDestination.MANAGER_LOGIN -> {
                                ManagerLoginScreen(
                                    onLoginSuccess = {
                                        LeaveRepository.setManagerLoggedIn(true)
                                        currentScreen = ScreenDestination.PENDING_APPROVALS
                                    },
                                    onBackToEmployee = {
                                        LeaveRepository.switchRole(UserRole.EMPLOYEE)
                                        currentScreen = ScreenDestination.EMPLOYEE_HOME
                                    }
                                )
                            }

                            ScreenDestination.PENDING_APPROVALS -> {
                                PendingApprovalsScreen(
                                    manager = currentUser,
                                    requests = requests,
                                    onRequestSelected = { req ->
                                        selectedRequestForDetail = req
                                        currentScreen = ScreenDestination.APPROVE_REJECT_DETAIL
                                    },
                                    onQuickApprove = { reqId ->
                                        LeaveRepository.approveRequest(reqId, currentUser.name, "Approved via quick action")
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Request $reqId approved!")
                                        }
                                    },
                                    onQuickReject = { reqId ->
                                        LeaveRepository.rejectRequest(reqId, currentUser.name, "Declined by manager")
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Request $reqId rejected.")
                                        }
                                    },
                                    onOpenCalendar = { currentScreen = ScreenDestination.TEAM_CALENDAR }
                                )
                            }

                            ScreenDestination.APPROVE_REJECT_DETAIL -> {
                                val req = selectedRequestForDetail ?: requests.firstOrNull { it.status == LeaveStatus.PENDING }
                                if (req != null) {
                                    ApproveRejectDetailScreen(
                                        request = req,
                                        onApprove = { reqId, comment ->
                                            LeaveRepository.approveRequest(reqId, currentUser.name, comment)
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Request $reqId successfully approved!")
                                            }
                                            currentScreen = ScreenDestination.PENDING_APPROVALS
                                        },
                                        onReject = { reqId, comment ->
                                            LeaveRepository.rejectRequest(reqId, currentUser.name, comment)
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Request $reqId rejected.")
                                            }
                                            currentScreen = ScreenDestination.PENDING_APPROVALS
                                        },
                                        onBack = { currentScreen = ScreenDestination.PENDING_APPROVALS }
                                    )
                                } else {
                                    currentScreen = ScreenDestination.PENDING_APPROVALS
                                }
                            }

                            ScreenDestination.TEAM_CALENDAR -> {
                                TeamLeaveCalendarScreen(
                                    requests = requests,
                                    onBack = { currentScreen = ScreenDestination.PENDING_APPROVALS }
                                )
                            }

                            // Admin Screens
                            ScreenDestination.ADMIN_LOGIN -> {
                                AdminLoginScreen(
                                    onLoginSuccess = {
                                        LeaveRepository.setAdminLoggedIn(true)
                                        currentScreen = ScreenDestination.ADMIN_DASHBOARD
                                    },
                                    onBackToEmployee = {
                                        LeaveRepository.switchRole(UserRole.EMPLOYEE)
                                        currentScreen = ScreenDestination.EMPLOYEE_HOME
                                    }
                                )
                            }

                            ScreenDestination.ADMIN_DASHBOARD -> {
                                AdminDashboardScreen(
                                    admin = currentUser,
                                    requests = requests,
                                    onNavigatePolicies = { currentScreen = ScreenDestination.LEAVE_POLICIES },
                                    onNavigateAllRequests = { currentScreen = ScreenDestination.ALL_REQUESTS },
                                    onNavigateReports = { currentScreen = ScreenDestination.REPORTS }
                                )
                            }

                            ScreenDestination.LEAVE_POLICIES -> {
                                LeavePoliciesScreen(
                                    policies = policies,
                                    onUpdatePolicy = { type, alloc, carry, notice, cert ->
                                        LeaveRepository.updatePolicy(type, alloc, carry, notice, cert)
                                    },
                                    onBack = { currentScreen = ScreenDestination.ADMIN_DASHBOARD }
                                )
                            }

                            ScreenDestination.ALL_REQUESTS -> {
                                AllRequestsScreen(
                                    requests = requests,
                                    onBack = { currentScreen = ScreenDestination.ADMIN_DASHBOARD }
                                )
                            }

                            ScreenDestination.REPORTS -> {
                                ReportsScreen(
                                    onBack = { currentScreen = ScreenDestination.ADMIN_DASHBOARD }
                                )
                            }
                        }
                    }

                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }

                // Compact / Mobile Navigation Bar (shown when not on wide desktop screen)
                if (!isDesktopWide) {
                    CorporateMobileBottomBar(
                        currentUser = currentUser,
                        currentScreen = currentScreen,
                        pendingCount = pendingApprovalsCount,
                        onNavigate = { currentScreen = it }
                    )
                }
            }
        }
    }
}

@Composable
fun CorporateDesktopSidebar(
    currentUser: UserProfile,
    currentScreen: ScreenDestination,
    pendingCount: Int,
    onNavigate: (ScreenDestination) -> Unit,
    onSwitchRole: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(260.dp)
            .fillMaxHeight(),
        color = CorporateSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Corporate Brand Banner
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(bottom = 20.dp, top = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CorporateBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.BeachAccess,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "LeaveEase",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Enterprise Leave MS",
                            fontSize = 11.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Navigation Sections based on active user role
                val navItems = when (currentUser.role) {
                    UserRole.EMPLOYEE -> listOf(
                        ScreenDestination.EMPLOYEE_HOME,
                        ScreenDestination.REQUEST_LEAVE,
                        ScreenDestination.MY_REQUESTS,
                        ScreenDestination.LEAVE_BALANCE
                    )
                    UserRole.MANAGER -> listOf(
                        ScreenDestination.PENDING_APPROVALS,
                        ScreenDestination.TEAM_CALENDAR,
                        ScreenDestination.MANAGER_LOGIN
                    )
                    UserRole.ADMIN -> listOf(
                        ScreenDestination.ADMIN_DASHBOARD,
                        ScreenDestination.LEAVE_POLICIES,
                        ScreenDestination.ALL_REQUESTS,
                        ScreenDestination.REPORTS,
                        ScreenDestination.ADMIN_LOGIN
                    )
                }

                Text(
                    text = "${currentUser.role.displayName.uppercase()} NAVIGATION",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                navItems.forEach { item ->
                    val isSelected = (currentScreen == item)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onNavigate(item) }
                            .testTag("sidebar_nav_${item.name.lowercase()}"),
                        color = if (isSelected) CorporateBlueLight else Color.Transparent
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) CorporateBlue else TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = item.title,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) CorporateBlue else TextPrimary
                                )
                            }

                            if (item == ScreenDestination.PENDING_APPROVALS && pendingCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(StatusPending)
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "$pendingCount",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Role Switcher Footer Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CorporateSurfaceVariant),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Switch Corporate Role",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(
                            Triple(UserRole.EMPLOYEE, "Emp", Icons.Default.Person),
                            Triple(UserRole.MANAGER, "Mgr", Icons.Default.SupervisorAccount),
                            Triple(UserRole.ADMIN, "Admin", Icons.Default.AdminPanelSettings)
                        ).forEach { (role, label, icon) ->
                            val active = (currentUser.role == role)
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { onSwitchRole(role) },
                                color = if (active) CorporateBlue else CorporateSurface,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (active) CorporateBlue else CorporateBorder
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        tint = if (active) Color.White else TextSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                                        color = if (active) Color.White else TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CorporateMobileBottomBar(
    currentUser: UserProfile,
    currentScreen: ScreenDestination,
    pendingCount: Int,
    onNavigate: (ScreenDestination) -> Unit
) {
    val items = when (currentUser.role) {
        UserRole.EMPLOYEE -> listOf(
            ScreenDestination.EMPLOYEE_HOME,
            ScreenDestination.REQUEST_LEAVE,
            ScreenDestination.MY_REQUESTS,
            ScreenDestination.LEAVE_BALANCE
        )
        UserRole.MANAGER -> listOf(
            ScreenDestination.PENDING_APPROVALS,
            ScreenDestination.TEAM_CALENDAR,
            ScreenDestination.MANAGER_LOGIN
        )
        UserRole.ADMIN -> listOf(
            ScreenDestination.ADMIN_DASHBOARD,
            ScreenDestination.LEAVE_POLICIES,
            ScreenDestination.ALL_REQUESTS,
            ScreenDestination.REPORTS
        )
    }

    NavigationBar(
        containerColor = CorporateSurface,
        tonalElevation = 2.dp,
        modifier = Modifier.navigationBarsPadding()
    ) {
        items.forEach { dest ->
            val selected = (currentScreen == dest)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(dest) },
                icon = {
                    if (dest == ScreenDestination.PENDING_APPROVALS && pendingCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = StatusPending) {
                                    Text("$pendingCount")
                                }
                            }
                        ) {
                            Icon(dest.icon, contentDescription = dest.title)
                        }
                    } else {
                        Icon(dest.icon, contentDescription = dest.title)
                    }
                },
                label = {
                    Text(
                        text = dest.title,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CorporateBlue,
                    selectedTextColor = CorporateBlue,
                    indicatorColor = CorporateBlueLight,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}
