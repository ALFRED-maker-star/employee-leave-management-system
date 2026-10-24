package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.data.LeaveRepository
import com.example.model.DepartmentReport
import com.example.model.LeaveType
import com.example.model.MonthlyTrend
import com.example.ui.components.LeaveTypeTag
import com.example.ui.theme.AnnualLeaveColor
import com.example.ui.theme.CorporateBlue
import com.example.ui.theme.CorporateBlueLight
import com.example.ui.theme.CorporateBorder
import com.example.ui.theme.CorporateSurface
import com.example.ui.theme.CorporateSurfaceVariant
import com.example.ui.theme.PersonalLeaveColor
import com.example.ui.theme.SickLeaveColor
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ReportsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val departmentStats = LeaveRepository.getDepartmentReports()
    val monthlyTrends = LeaveRepository.getMonthlyTrends()
    val maxDeptDays = departmentStats.maxOfOrNull { it.totalLeaveDaysTaken } ?: 150
    val maxMonthlyDays = monthlyTrends.maxOfOrNull { it.daysTaken } ?: 100

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
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
                        text = "Leave Usage & Trend Analytics",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Departmental utilization, seasonal patterns, and leave type distributions",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // 1. Leave Usage by Department Chart (Bar / Progress visualizer)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("report_chart_department"),
                colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Assessment, contentDescription = null, tint = CorporateBlue)
                            Text(
                                text = "Leave Usage by Department",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "Days Taken",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMuted
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        departmentStats.forEach { dept ->
                            val fraction = (dept.totalLeaveDaysTaken.toFloat() / maxDeptDays.toFloat()).coerceIn(0f, 1f)

                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = dept.department,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${dept.totalLeaveDaysTaken} days (${dept.avgDaysPerEmployee}d / emp)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CorporateBlue
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                LinearProgressIndicator(
                                    progress = { fraction },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = CorporateBlue,
                                    trackColor = CorporateSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Most-Used Leave Types Breakdown
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("report_chart_types"),
                colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.PieChart, contentDescription = null, tint = CorporateBlue)
                        Text(
                            text = "Most-Used Leave Types",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                    }

                    // Visual Distribution Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .clip(RoundedCornerShape(7.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(62f)
                                .fillMaxSize()
                                .background(AnnualLeaveColor)
                        )
                        Box(
                            modifier = Modifier
                                .weight(24f)
                                .fillMaxSize()
                                .background(SickLeaveColor)
                        )
                        Box(
                            modifier = Modifier
                                .weight(14f)
                                .fillMaxSize()
                                .background(PersonalLeaveColor)
                        )
                    }

                    // Legend and Details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // Annual
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(AnnualLeaveColor))
                                Text("Annual", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            }
                            Text("62% (265 d)", fontSize = 12.sp, color = TextSecondary)
                        }

                        // Sick
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(SickLeaveColor))
                                Text("Sick", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            }
                            Text("24% (102 d)", fontSize = 12.sp, color = TextSecondary)
                        }

                        // Personal
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(PersonalLeaveColor))
                                Text("Personal", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            }
                            Text("14% (60 d)", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }

        // 3. Seasonal Trends (Month by Month)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("report_chart_seasonal"),
                colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = CorporateBlue)
                            Text(
                                text = "Seasonal Trends (2026)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "Peak: Dec & Jul",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CorporateBlue
                        )
                    }

                    Text(
                        text = "Historical monthly days taken across all active departments",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    // Month Column Bars
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        monthlyTrends.forEach { trend ->
                            val heightFraction = (trend.daysTaken.toFloat() / maxMonthlyDays.toFloat()).coerceIn(0.1f, 1f)
                            val isPeak = trend.daysTaken >= 80

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "${trend.daysTaken}",
                                    fontSize = 9.sp,
                                    color = if (isPeak) CorporateBlue else TextMuted,
                                    fontWeight = if (isPeak) FontWeight.Bold else FontWeight.Normal
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height((100 * heightFraction).dp)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(if (isPeak) CorporateBlue else CorporateBlue.copy(alpha = 0.35f))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = trend.monthName,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Strategic HR Insights
        item {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = CorporateBlueLight,
                border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBlue.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Insights,
                        contentDescription = null,
                        tint = CorporateBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Column {
                        Text(
                            text = "HR Workforce Insights",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CorporateBlue
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "• Q4 holiday surge projected for December (est. 95 days). Early planning reminders sent to team leads.\n• Sick leave rate in Engineering normalized down 12% following ergonomic workstation subsidies.",
                            fontSize = 12.sp,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
