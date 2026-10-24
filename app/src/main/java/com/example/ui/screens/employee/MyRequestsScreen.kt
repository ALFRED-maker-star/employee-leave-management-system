package com.example.ui.screens.employee

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.ui.theme.StatusApprovedBg
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusRejected
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MyRequestsScreen(
    user: UserProfile,
    requests: List<LeaveRequest>,
    onRequestLeaveClick: () -> Unit,
    onCancelRequest: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf<LeaveStatus?>(null) }
    var requestToCancel by remember { mutableStateOf<LeaveRequest?>(null) }

    val userRequests = requests.filter { it.employeeId == user.id }
    val filteredList = if (selectedFilter == null) {
        userRequests
    } else {
        userRequests.filter { it.status == selectedFilter }
    }

    // Cancel confirmation dialog
    if (requestToCancel != null) {
        val req = requestToCancel!!
        AlertDialog(
            onDismissRequest = { requestToCancel = null },
            title = {
                Text(
                    text = "Cancel Leave Request?",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to cancel your ${req.leaveType.displayName} request (${req.startDate} to ${req.endDate})? This will immediately restore ${req.totalDays} days to your balance.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCancelRequest(req.id)
                        requestToCancel = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusRejected)
                ) {
                    Text("Confirm Cancellation")
                }
            },
            dismissButton = {
                TextButton(onClick = { requestToCancel = null }) {
                    Text("Keep Request", color = TextSecondary)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                        text = "My Requests",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Track status and manage your submitted leave history",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            Button(
                onClick = onRequestLeaveClick,
                colors = ButtonDefaults.buttonColors(containerColor = CorporateBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_new_request_top")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("New Request", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Status Filter Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == null,
                onClick = { selectedFilter = null },
                label = { Text("All (${userRequests.size})", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = CorporateBlue,
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = selectedFilter == LeaveStatus.PENDING,
                onClick = { selectedFilter = LeaveStatus.PENDING },
                label = {
                    Text(
                        "Pending (${userRequests.count { it.status == LeaveStatus.PENDING }})",
                        fontSize = 12.sp
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StatusPending,
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = selectedFilter == LeaveStatus.APPROVED,
                onClick = { selectedFilter = LeaveStatus.APPROVED },
                label = {
                    Text(
                        "Approved (${userRequests.count { it.status == LeaveStatus.APPROVED }})",
                        fontSize = 12.sp
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StatusApproved,
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = selectedFilter == LeaveStatus.REJECTED,
                onClick = { selectedFilter = LeaveStatus.REJECTED },
                label = {
                    Text(
                        "Rejected (${userRequests.count { it.status == LeaveStatus.REJECTED }})",
                        fontSize = 12.sp
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StatusRejected,
                    selectedLabelColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "No requests found for this status filter.",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList, key = { it.id }) { req ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("request_item_${req.id}"),
                        colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Top Row: Type tag, Ref ID, and Status Badge
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    LeaveTypeTag(leaveType = req.leaveType)
                                    Text(
                                        text = req.id,
                                        fontSize = 12.sp,
                                        color = TextMuted,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                StatusBadge(status = req.status)
                            }

                            // Dates and Days
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${req.startDate}  →  ${req.endDate}",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Submitted on ${req.submittedAt}",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CorporateBlueLight)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${req.totalDays} Working Days",
                                        color = CorporateBlue,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Reason
                            Text(
                                text = "Reason: ${req.reason}",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )

                            // Attachment if present
                            if (req.attachmentName != null) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = CorporateSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AttachFile,
                                            contentDescription = null,
                                            tint = CorporateBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = req.attachmentName,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = CorporateBlue
                                        )
                                        if (req.attachmentSize != null) {
                                            Text(
                                                text = "(${req.attachmentSize})",
                                                fontSize = 11.sp,
                                                color = TextMuted
                                            )
                                        }
                                    }
                                }
                            }

                            // Manager Reviewer note (if Reviewed)
                            if (req.reviewerComment != null) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (req.status == LeaveStatus.APPROVED) StatusApprovedBg else StatusRejected.copy(alpha = 0.08f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ChatBubbleOutline,
                                            contentDescription = null,
                                            tint = if (req.status == LeaveStatus.APPROVED) StatusApproved else StatusRejected,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Column {
                                            Text(
                                                text = "${req.reviewedBy ?: "Manager"}:",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = TextPrimary
                                            )
                                            Text(
                                                text = req.reviewerComment,
                                                fontSize = 12.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    }
                                }
                            }

                            // Cancel Option for PENDING requests (as specified in prompt)
                            if (req.status == LeaveStatus.PENDING) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    OutlinedButton(
                                        onClick = { requestToCancel = req },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = StatusRejected
                                        ),
                                        modifier = Modifier.testTag("btn_cancel_request_${req.id}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Cancel,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Cancel Request",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
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
}
