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
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
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
import com.example.model.LeaveRequest
import com.example.model.LeaveStatus
import com.example.model.UserProfile
import com.example.ui.components.LeaveTypeTag
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CorporateBlue
import com.example.ui.theme.CorporateBlueLight
import com.example.ui.theme.CorporateBorder
import com.example.ui.theme.CorporateSurface
import com.example.ui.theme.CorporateSurfaceVariant
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusRejected
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PendingApprovalsScreen(
    manager: UserProfile,
    requests: List<LeaveRequest>,
    onRequestSelected: (LeaveRequest) -> Unit,
    onQuickApprove: (String) -> Unit,
    onQuickReject: (String) -> Unit,
    onOpenCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pendingTeamRequests = requests.filter { it.status == LeaveStatus.PENDING }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Pending Approvals",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(StatusPending)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${pendingTeamRequests.size} Pending",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "Review and action leave requests submitted by your team members",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            OutlinedButton(
                onClick = onOpenCalendar,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_view_team_calendar_top")
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = CorporateBlue
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Team Calendar", fontSize = 12.sp, color = CorporateBlue)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (pendingTeamRequests.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(StatusApproved.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = StatusApproved,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "All Caught Up!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "No pending leave requests currently require your review.",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(pendingTeamRequests, key = { it.id }) { req ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onRequestSelected(req) }
                            .testTag("pending_approval_card_${req.id}"),
                        colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Employee info row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(CorporateBlueLight),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = req.employeeName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = CorporateBlue
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = req.employeeName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "${req.department} • Submitted ${req.submittedAt}",
                                            fontSize = 11.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                LeaveTypeTag(leaveType = req.leaveType)
                            }

                            // Dates and Days duration
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CorporateSurfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = null,
                                            tint = CorporateBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "${req.startDate}  →  ${req.endDate}",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp,
                                            color = TextPrimary
                                        )
                                    }
                                    Text(
                                        text = "${req.totalDays} Days",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = CorporateBlue
                                    )
                                }
                            }

                            // Reason preview
                            Text(
                                text = "Reason: “${req.reason}”",
                                fontSize = 13.sp,
                                color = TextSecondary,
                                maxLines = 2
                            )

                            if (req.attachmentName != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AttachFile,
                                        contentDescription = null,
                                        tint = CorporateBlue,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Attached: ${req.attachmentName}",
                                        fontSize = 11.sp,
                                        color = CorporateBlue,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { onRequestSelected(req) },
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.testTag("btn_review_full_detail_${req.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RateReview,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Full Details", fontSize = 12.sp)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = { onQuickApprove(req.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = StatusApproved),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.testTag("btn_quick_approve_${req.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Approve", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
