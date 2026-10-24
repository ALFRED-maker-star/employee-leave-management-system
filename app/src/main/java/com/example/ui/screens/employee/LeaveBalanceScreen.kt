package com.example.ui.screens.employee

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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Policy
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
import com.example.model.AccrualItem
import com.example.model.LeaveBalance
import com.example.model.UserProfile
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

@Composable
fun LeaveBalanceScreen(
    user: UserProfile,
    balances: List<LeaveBalance>,
    accrualHistory: List<AccrualItem>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                        text = "Leave Balance & Accruals",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Detailed entitlement breakdown and accrual history for 2026",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Detailed Breakdown per leave type (Used vs Remaining)
        item {
            Text(
                text = "Annual Entitlement Breakdown",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        items(balances, key = { it.leaveType.name }) { balance ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("balance_detail_${balance.leaveType.name.lowercase()}"),
                colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    // Type header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(balance.leaveType.color)
                            )
                            Text(
                                text = balance.leaveType.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Text(
                            text = "${balance.allocatedDays} days allocated",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 Metric Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Remaining
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = CorporateBlueLight,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBlue.copy(alpha = 0.2f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Remaining", fontSize = 11.sp, color = CorporateBlue, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "${balance.remainingDays} d", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = CorporateBlue)
                            }
                        }

                        // Used
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = CorporateSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Used / Taken", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "${balance.usedDays} d", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                        }

                        // Pending
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            color = if (balance.pendingDays > 0) Color(0xFFFEF3C7) else CorporateSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Pending", fontSize = 11.sp, color = if (balance.pendingDays > 0) StatusPending else TextMuted, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = "${balance.pendingDays} d", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = if (balance.pendingDays > 0) StatusPending else TextMuted)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { balance.progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = balance.leaveType.color,
                        trackColor = CorporateSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${(balance.progressFraction * 100).toInt()}% utilized",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Text(
                            text = "${balance.remainingDays} days available for request",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Accrual History Section (as requested)
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = CorporateBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Accrual History Timeline",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Text(
                    text = "FY 2026",
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        items(accrualHistory, key = { it.id }) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("accrual_item_${item.id}"),
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
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (item.amount >= 0) StatusApproved.copy(alpha = 0.15f)
                                    else StatusRejected.copy(alpha = 0.15f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (item.amount >= 0) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                contentDescription = null,
                                tint = if (item.amount >= 0) StatusApproved else StatusRejected,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = item.type,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "• ${item.leaveType.displayName}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.note,
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = item.date,
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (item.amount >= 0) "+${item.amount} d" else "${item.amount} d",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (item.amount >= 0) StatusApproved else StatusRejected
                        )
                        Text(
                            text = "Bal: ${item.runningBalance}d",
                            fontSize = 11.sp,
                            color = TextMuted
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
