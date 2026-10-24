package com.example.ui.screens.employee

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LeaveBalance
import com.example.model.LeaveRequest
import com.example.model.LeaveStatus
import com.example.model.UserProfile
import com.example.ui.components.LeaveBalanceCard
import com.example.ui.components.LeaveTypeTag
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CorporateBlue
import com.example.ui.theme.CorporateBorder
import com.example.ui.theme.CorporateSurface
import com.example.ui.theme.CorporateSurfaceVariant
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusApprovedBg
import com.example.ui.theme.StatusApprovedBorder
import com.example.ui.theme.StatusApprovedText
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun EmployeeHomeScreen(
    user: UserProfile,
    balances: List<LeaveBalance>,
    requests: List<LeaveRequest>,
    onRequestLeaveClick: () -> Unit,
    onViewMyRequestsClick: () -> Unit,
    onViewBalancesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Find upcoming approved leave
    val upcomingApprovedLeave = requests.firstOrNull {
        it.employeeId == user.id && it.status == LeaveStatus.APPROVED && it.startDate >= "2026-10-24"
    }

    val recentRequests = requests.filter { it.employeeId == user.id }.take(3)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Hero Greeting & Primary Action
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("employee_home_welcome_card"),
                colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Welcome back, ${user.name.substringBefore(" ")} 👋",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${user.jobTitle} • ${user.department} Team",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }

                        Button(
                            onClick = onRequestLeaveClick,
                            colors = ButtonDefaults.buttonColors(containerColor = CorporateBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("btn_request_leave_hero")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Request Leave",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Upcoming Approved Leave Banner (as required)
        item {
            if (upcomingApprovedLeave != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("upcoming_approved_leave_banner"),
                    colors = CardDefaults.cardColors(containerColor = StatusApprovedBg),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StatusApprovedBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(StatusApproved.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.BeachAccess,
                                contentDescription = "Upcoming Leave",
                                tint = StatusApprovedText,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Upcoming Approved Leave",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = StatusApprovedText
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(StatusApproved)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${upcomingApprovedLeave.totalDays} Days",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${upcomingApprovedLeave.leaveType.displayName}: ${upcomingApprovedLeave.startDate} to ${upcomingApprovedLeave.endDate}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                            Text(
                                text = "“${upcomingApprovedLeave.reason}”",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = CorporateSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.EventNote,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "No upcoming approved leave scheduled. Planning time off soon?",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Leave Balance Summary Cards Section (Annual, Sick, Personal — days remaining)
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Leave Balances",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    OutlinedButton(
                        onClick = onViewBalancesClick,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("btn_view_balance_details")
                    ) {
                        Text("Detailed Breakdown", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Responsive row or column of balance cards
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    balances.forEach { balance ->
                        LeaveBalanceCard(
                            balance = balance,
                            onClick = onViewBalancesClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Recent Requests Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Leave Requests",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "View All (${requests.count { it.employeeId == user.id }})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CorporateBlue,
                    modifier = Modifier
                        .clickable { onViewMyRequestsClick() }
                        .padding(4.dp)
                )
            }
        }

        if (recentRequests.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No leave requests submitted yet.",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
            items(recentRequests, key = { it.id }) { req ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onViewMyRequestsClick() }
                        .testTag("recent_request_${req.id}"),
                    colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                LeaveTypeTag(leaveType = req.leaveType)
                                Text(
                                    text = "${req.totalDays} days",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${req.startDate} to ${req.endDate}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }

                        StatusBadge(status = req.status)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
