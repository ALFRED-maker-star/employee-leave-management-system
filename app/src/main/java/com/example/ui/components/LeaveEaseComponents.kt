package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LeaveBalance
import com.example.model.LeaveStatus
import com.example.model.LeaveType
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.ui.theme.CorporateBlue
import com.example.ui.theme.CorporateBlueLight
import com.example.ui.theme.CorporateBorder
import com.example.ui.theme.CorporateBorderStrong
import com.example.ui.theme.CorporateSurface
import com.example.ui.theme.CorporateSurfaceVariant
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusRejected
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun StatusBadge(
    status: LeaveStatus,
    modifier: Modifier = Modifier
) {
    val icon = when (status) {
        LeaveStatus.APPROVED -> Icons.Default.CheckCircle
        LeaveStatus.PENDING -> Icons.Default.HourglassTop
        LeaveStatus.REJECTED -> Icons.Default.Close
        LeaveStatus.CANCELLED -> Icons.Default.Close
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(status.badgeBg)
            .border(1.dp, status.badgeBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(status.badgeColor)
            )
            Text(
                text = status.label,
                color = status.textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun LeaveTypeTag(
    leaveType: LeaveType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(leaveType.color.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = leaveType.displayName,
            color = leaveType.color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color = CorporateBlue,
    iconBgColor: Color = CorporateBlueLight,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.testTag("stat_card_${title.lowercase().replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = CorporateSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
fun LeaveBalanceCard(
    balance: LeaveBalance,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag("balance_card_${balance.leaveType.name.lowercase()}"),
        colors = CardDefaults.cardColors(containerColor = CorporateSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
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
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(balance.leaveType.color)
                    )
                    Text(
                        text = balance.leaveType.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CorporateSurfaceVariant)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${balance.allocatedDays}d total",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${balance.remainingDays}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = CorporateBlue
                )
                Text(
                    text = "days remaining",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { balance.progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = balance.leaveType.color,
                trackColor = CorporateSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${balance.usedDays} days used",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                if (balance.pendingDays > 0) {
                    Text(
                        text = "${balance.pendingDays}d pending",
                        fontSize = 12.sp,
                        color = StatusPending,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun CorporateHeader(
    currentUser: UserProfile,
    onRoleSelected: (UserRole) -> Unit,
    onMenuToggle: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var roleDropdownExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = CorporateSurface,
        border = androidx.compose.foundation.BorderStroke(0.dp, Color.Transparent),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Portal title & user welcome
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CorporateBlue.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = currentUser.role.badgeTitle,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CorporateBlue
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LeaveEase",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${currentUser.name} (${currentUser.jobTitle})",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            // Quick Role Switcher (Crucial for exploring Employee, Manager, Admin easily)
            Box {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CorporateSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder),
                    modifier = Modifier
                        .clickable { roleDropdownExpanded = true }
                        .testTag("role_switcher_dropdown")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Switch Role",
                            tint = CorporateBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = currentUser.role.displayName,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = roleDropdownExpanded,
                    onDismissRequest = { roleDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text("Employee Portal", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Alex Rivera (Frontend Dev)", fontSize = 11.sp, color = TextMuted)
                            }
                        },
                        onClick = {
                            roleDropdownExpanded = false
                            onRoleSelected(UserRole.EMPLOYEE)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text("Manager Hub", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Sarah Jenkins (Lead)", fontSize = 11.sp, color = TextMuted)
                            }
                        },
                        onClick = {
                            roleDropdownExpanded = false
                            onRoleSelected(UserRole.MANAGER)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text("HR Admin Center", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("David Chen (VP People)", fontSize = 11.sp, color = TextMuted)
                            }
                        },
                        onClick = {
                            roleDropdownExpanded = false
                            onRoleSelected(UserRole.ADMIN)
                        }
                    )
                }
            }
        }
    }
}
