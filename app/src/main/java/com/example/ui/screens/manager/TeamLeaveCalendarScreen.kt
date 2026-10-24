package com.example.ui.screens.manager

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LeaveRequest
import com.example.model.LeaveStatus
import com.example.model.LeaveType
import com.example.ui.components.LeaveTypeTag
import com.example.ui.theme.CorporateBlue
import com.example.ui.theme.CorporateBlueLight
import com.example.ui.theme.CorporateBorder
import com.example.ui.theme.CorporateSurface
import com.example.ui.theme.CorporateSurfaceVariant
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TeamLeaveCalendarScreen(
    requests: List<LeaveRequest>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Current focused month: November 2026
    var selectedDayNumber by remember { mutableIntStateOf(16) }
    val monthName = "November 2026"
    val daysInMonth = 30
    val firstDayOffset = 0 // Sunday = 0, for Nov 2026, Nov 1 is Sunday!

    // Filter approved leave requests that fall in November 2026
    val approvedRequests = requests.filter { it.status == LeaveStatus.APPROVED }

    // Helper: Find requests active on day d
    fun getRequestsOnDay(day: Int): List<LeaveRequest> {
        val dateStr = "2026-11-${day.toString().padStart(2, '0')}"
        return approvedRequests.filter { req ->
            req.startDate <= dateStr && req.endDate >= dateStr
        }
    }

    val selectedDayLeaves = getRequestsOnDay(selectedDayNumber)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Team Leave Calendar",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Monthly overview of scheduled team member absences",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Calendar Month Navigation Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("team_calendar_card"),
                colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = monthName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { /* Previous month */ }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month")
                            }
                            IconButton(onClick = { /* Next month */ }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.ChevronRight, contentDescription = "Next Month")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Day-of-week headers
                    val dayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        dayNames.forEach { dayName ->
                            Text(
                                text = dayName,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Calendar Grid (6 rows of 7 days)
                    val totalSlots = 35
                    val weeks = (totalSlots + 6) / 7

                    for (week in 0 until weeks) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (dow in 0..6) {
                                val slotIndex = week * 7 + dow
                                val dayNum = slotIndex - firstDayOffset + 1

                                if (dayNum in 1..daysInMonth) {
                                    val leavesOnDay = getRequestsOnDay(dayNum)
                                    val isSelected = (dayNum == selectedDayNumber)

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                            .padding(2.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                when {
                                                    isSelected -> CorporateBlue
                                                    leavesOnDay.isNotEmpty() -> leavesOnDay.first().leaveType.color.copy(alpha = 0.15f)
                                                    else -> Color.Transparent
                                                }
                                            )
                                            .border(
                                                1.dp,
                                                if (isSelected) CorporateBlue else CorporateBorder.copy(alpha = 0.4f),
                                                RoundedCornerShape(6.dp)
                                            )
                                            .clickable { selectedDayNumber = dayNum }
                                            .testTag("cal_day_$dayNum"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = "$dayNum",
                                                fontSize = 12.sp,
                                                fontWeight = if (leavesOnDay.isNotEmpty() || isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) Color.White else TextPrimary
                                            )
                                            if (leavesOnDay.isNotEmpty()) {
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                                    modifier = Modifier.padding(top = 2.dp)
                                                ) {
                                                    leavesOnDay.take(3).forEach { req ->
                                                        Box(
                                                            modifier = Modifier
                                                                .size(4.dp)
                                                                .clip(CircleShape)
                                                                .background(if (isSelected) Color.White else req.leaveType.color)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Color-Coded Legend
        item {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CorporateSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LeaveType.values().take(3).forEach { type ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(type.color)
                            )
                            Text(
                                text = type.displayName,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Selected Date Detail Roster
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Out of Office on Nov $selectedDayNumber, 2026",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${selectedDayLeaves.size} Member(s)",
                    fontSize = 12.sp,
                    color = CorporateBlue,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if (selectedDayLeaves.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.EventAvailable,
                            contentDescription = null,
                            tint = StatusApproved,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Full team attendance. No approved leaves on this date.",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        } else {
            items(selectedDayLeaves, key = { it.id }) { req ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(req.leaveType.color.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = req.employeeName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = req.leaveType.color
                                )
                            }

                            Column {
                                Text(
                                    text = req.employeeName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${req.startDate} to ${req.endDate} (${req.totalDays}d)",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        LeaveTypeTag(leaveType = req.leaveType)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
