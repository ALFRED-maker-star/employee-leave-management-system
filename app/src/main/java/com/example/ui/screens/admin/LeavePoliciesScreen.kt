package com.example.ui.screens.admin

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LeavePolicy
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
import kotlinx.coroutines.launch

@Composable
fun LeavePoliciesScreen(
    policies: List<LeavePolicy>,
    onUpdatePolicy: (LeaveType, Int, Int, Int, Boolean) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Map policies to local editable state
    var annualAllocation by remember {
        mutableIntStateOf(policies.find { it.leaveType == LeaveType.ANNUAL }?.annualAllocationDays ?: 20)
    }
    var annualCarryover by remember {
        mutableIntStateOf(policies.find { it.leaveType == LeaveType.ANNUAL }?.carryoverLimitDays ?: 5)
    }
    var annualNotice by remember {
        mutableIntStateOf(policies.find { it.leaveType == LeaveType.ANNUAL }?.noticeDaysRequired ?: 14)
    }

    var sickAllocation by remember {
        mutableIntStateOf(policies.find { it.leaveType == LeaveType.SICK }?.annualAllocationDays ?: 10)
    }
    var sickRequireCert by remember {
        mutableStateOf(policies.find { it.leaveType == LeaveType.SICK }?.requiresMedicalCertificate ?: true)
    }

    var personalAllocation by remember {
        mutableIntStateOf(policies.find { it.leaveType == LeaveType.PERSONAL }?.annualAllocationDays ?: 5)
    }
    var personalCarryover by remember {
        mutableIntStateOf(policies.find { it.leaveType == LeaveType.PERSONAL }?.carryoverLimitDays ?: 2)
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
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
                            text = "Leave Policies Setup",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Configure annual allocations, carryover rules, and approval prerequisites",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Info Notice
            item {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CorporateBlueLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBlue.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = CorporateBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Modifications apply globally to the next fiscal year accrual cycle for all active employees.",
                            fontSize = 12.sp,
                            color = CorporateBlue
                        )
                    }
                }
            }

            // 1. Annual Leave Policy Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("policy_card_annual"),
                    colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LeaveTypeTag(leaveType = LeaveType.ANNUAL)
                            Text(
                                text = "Annual Vacation Policy",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextPrimary
                            )
                        }

                        // Allocation Stepper
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Annual Allocation", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextPrimary)
                                Text("Days granted per full-time employee", fontSize = 11.sp, color = TextMuted)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = { if (annualAllocation > 10) annualAllocation-- },
                                    colors = ButtonDefaults.buttonColors(containerColor = CorporateSurfaceVariant),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(36.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("−", fontSize = 18.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = "$annualAllocation days",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = CorporateBlue,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                                Button(
                                    onClick = { if (annualAllocation < 35) annualAllocation++ },
                                    colors = ButtonDefaults.buttonColors(containerColor = CorporateSurfaceVariant),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(36.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("+", fontSize = 18.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Carryover Limit
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Carryover Rollover Limit", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextPrimary)
                                Text("Max unused days transferred to next year", fontSize = 11.sp, color = TextMuted)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = { if (annualCarryover > 0) annualCarryover-- },
                                    colors = ButtonDefaults.buttonColors(containerColor = CorporateSurfaceVariant),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(36.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("−", fontSize = 18.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = "$annualCarryover days",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                                Button(
                                    onClick = { if (annualCarryover < 10) annualCarryover++ },
                                    colors = ButtonDefaults.buttonColors(containerColor = CorporateSurfaceVariant),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(36.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("+", fontSize = 18.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Required Notice Days
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Advance Notice Required", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextPrimary)
                                Text("Minimum days notice prior to leave start", fontSize = 11.sp, color = TextMuted)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = { if (annualNotice > 0) annualNotice-- },
                                    colors = ButtonDefaults.buttonColors(containerColor = CorporateSurfaceVariant),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(36.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("−", fontSize = 18.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = "$annualNotice days",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                                Button(
                                    onClick = { if (annualNotice < 30) annualNotice++ },
                                    colors = ButtonDefaults.buttonColors(containerColor = CorporateSurfaceVariant),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(36.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("+", fontSize = 18.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // 2. Sick Leave Policy Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("policy_card_sick"),
                    colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LeaveTypeTag(leaveType = LeaveType.SICK)
                            Text(
                                text = "Sick Leave Policy",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextPrimary
                            )
                        }

                        // Allocation
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Annual Sick Days", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextPrimary)
                                Text("Paid days for illness or medical recovery", fontSize = 11.sp, color = TextMuted)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = { if (sickAllocation > 5) sickAllocation-- },
                                    colors = ButtonDefaults.buttonColors(containerColor = CorporateSurfaceVariant),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(36.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("−", fontSize = 18.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = "$sickAllocation days",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = CorporateBlue,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                                Button(
                                    onClick = { if (sickAllocation < 20) sickAllocation++ },
                                    colors = ButtonDefaults.buttonColors(containerColor = CorporateSurfaceVariant),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(36.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("+", fontSize = 18.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Require Doctor Certificate Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Mandatory Medical Certificate",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Requires document attachment on requests > 2 days",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                            Switch(
                                checked = sickRequireCert,
                                onCheckedChange = { sickRequireCert = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = CorporateBlue
                                )
                            )
                        }
                    }
                }
            }

            // 3. Personal Leave Policy Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("policy_card_personal"),
                    colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LeaveTypeTag(leaveType = LeaveType.PERSONAL)
                            Text(
                                text = "Personal Leave Policy",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextPrimary
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Annual Personal Days", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextPrimary)
                                Text("Discretionary time off for personal errands", fontSize = 11.sp, color = TextMuted)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = { if (personalAllocation > 2) personalAllocation-- },
                                    colors = ButtonDefaults.buttonColors(containerColor = CorporateSurfaceVariant),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(36.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("−", fontSize = 18.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = "$personalAllocation days",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = CorporateBlue,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                                Button(
                                    onClick = { if (personalAllocation < 10) personalAllocation++ },
                                    colors = ButtonDefaults.buttonColors(containerColor = CorporateSurfaceVariant),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(36.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("+", fontSize = 18.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Save Policy Button
            item {
                Button(
                    onClick = {
                        onUpdatePolicy(LeaveType.ANNUAL, annualAllocation, annualCarryover, annualNotice, false)
                        onUpdatePolicy(LeaveType.SICK, sickAllocation, 0, 0, sickRequireCert)
                        onUpdatePolicy(LeaveType.PERSONAL, personalAllocation, personalCarryover, 3, false)

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Leave policies successfully updated & saved!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CorporateBlue),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_save_leave_policies")
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Policy Allocations", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
