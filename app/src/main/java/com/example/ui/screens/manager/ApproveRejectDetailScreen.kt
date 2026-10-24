package com.example.ui.screens.manager

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LeaveRequest
import com.example.model.LeaveStatus
import com.example.ui.components.LeaveTypeTag
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CorporateBlue
import com.example.ui.theme.CorporateBlueLight
import com.example.ui.theme.CorporateBorder
import com.example.ui.theme.CorporateBorderStrong
import com.example.ui.theme.CorporateSurface
import com.example.ui.theme.CorporateSurfaceVariant
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusRejected
import com.example.ui.theme.StatusRejectedBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ApproveRejectDetailScreen(
    request: LeaveRequest,
    onApprove: (String, String?) -> Unit,
    onReject: (String, String?) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var managerComment by remember { mutableStateOf("") }
    var commentError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Navigation Header
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
                    text = "Request Review • ${request.id}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Inspect request parameters, policy compliance, and render decision",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        // Full Request Detail Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("request_review_detail_card"),
            colors = CardDefaults.cardColors(containerColor = CorporateSurface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Employee Identity Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(CorporateBlueLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = request.employeeName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = CorporateBlue
                            )
                        }

                        Column {
                            Text(
                                text = request.employeeName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = request.employeeEmail,
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "Department: ${request.department}",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    StatusBadge(status = request.status)
                }

                // Summary Specs
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CorporateSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Leave Type", fontSize = 11.sp, color = TextMuted)
                                Spacer(modifier = Modifier.height(2.dp))
                                LeaveTypeTag(leaveType = request.leaveType)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Total Duration", fontSize = 11.sp, color = TextMuted)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${request.totalDays} Business Days",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CorporateBlue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Start Date", fontSize = 11.sp, color = TextMuted)
                                Text(
                                    text = request.startDate,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("End Date", fontSize = 11.sp, color = TextMuted)
                                Text(
                                    text = request.endDate,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }

                // Reason Block
                Column {
                    Text(
                        text = "Employee Reason & Justification",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CorporateSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = request.reason,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // Supporting Document Attachment
                if (request.attachmentName != null) {
                    Column {
                        Text(
                            text = "Verified Medical / Supporting Document",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CorporateBlueLight,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBlue.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = CorporateBlue,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column {
                                    Text(
                                        text = request.attachmentName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CorporateBlue
                                    )
                                    Text(
                                        text = "Verified PDF Certificate • ${request.attachmentSize ?: "1.4 MB"} • Tamper Checked",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                // Team Overlap / Conflict check indicator
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CorporateSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = CorporateBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Coverage Analysis",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "Engineering team has sufficient peer coverage during this period.",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                // Manager Decision Comment (Optional for Approve, recommended for Reject)
                Column {
                    Text(
                        text = "Manager Review Comment (Optional)",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = managerComment,
                        onValueChange = {
                            managerComment = it
                            if (commentError != null) commentError = null
                        },
                        placeholder = {
                            Text(
                                text = "Add feedback, coverage notes, or reasons for decision...",
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .testTag("input_manager_review_comment"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CorporateBlue,
                            unfocusedBorderColor = CorporateBorderStrong
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    if (commentError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = commentError ?: "",
                            color = StatusRejected,
                            fontSize = 12.sp
                        )
                    }
                }

                // Decision Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            onReject(request.id, managerComment)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StatusRejected),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("btn_decision_reject")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reject Request", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            onApprove(request.id, managerComment)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StatusApproved),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("btn_decision_approve")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Approve Request", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
