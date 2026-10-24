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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.LeaveBalance
import com.example.model.LeaveType
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
fun RequestLeaveScreen(
    balances: List<LeaveBalance>,
    onBack: () -> Unit,
    onSubmitSuccess: (LeaveType, String, String, Int, String, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf(LeaveType.ANNUAL) }
    var typeDropdownOpen by remember { mutableStateOf(false) }

    // Date range selections (defaulting to early November 2026)
    var startDay by remember { mutableIntStateOf(9) }
    var endDay by remember { mutableIntStateOf(13) }
    var month by remember { mutableStateOf("Nov 2026") }

    val calculatedDays by remember(startDay, endDay) {
        derivedStateOf {
            (endDay - startDay + 1).coerceAtLeast(1)
        }
    }

    val startDateFormatted = "2026-11-${startDay.toString().padStart(2, '0')}"
    val endDateFormatted = "2026-11-${endDay.toString().padStart(2, '0')}"

    var reason by remember { mutableStateOf("") }
    var reasonError by remember { mutableStateOf<String?>(null) }

    // Attachment
    var attachmentFileName by remember { mutableStateOf<String?>(null) }
    var showAttachmentPicker by remember { mutableStateOf(false) }

    // Current remaining for selected leave type
    val currentBalance = balances.find { it.leaveType == selectedType }
    val remainingDays = currentBalance?.remainingDays ?: selectedType.defaultDays
    val remainingAfter = remainingDays - calculatedDays

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Navigation Bar / Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("btn_request_leave_back")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = "Request Leave",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Submit a new time-off request for manager approval",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        // Form Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("request_leave_form_card"),
            colors = CardDefaults.cardColors(containerColor = CorporateSurface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Leave Type Dropdown
                Column {
                    Text(
                        text = "Leave Type",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Box {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { typeDropdownOpen = true }
                                .testTag("leave_type_dropdown"),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorderStrong),
                            color = CorporateSurfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
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
                                            .background(selectedType.color)
                                    )
                                    Text(
                                        text = selectedType.displayName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextPrimary
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = typeDropdownOpen,
                            onDismissRequest = { typeDropdownOpen = false }
                        ) {
                            LeaveType.values().forEach { type ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .clip(CircleShape)
                                                    .background(type.color)
                                            )
                                            Text(
                                                text = type.displayName,
                                                fontWeight = if (type == selectedType) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedType = type
                                        typeDropdownOpen = false
                                        // If not sick, reset attachment if not needed
                                    }
                                )
                            }
                        }
                    }
                }

                // Balance Insight Notice
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
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Remaining balance: $remainingDays days • After this request: $remainingAfter days",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = CorporateBlue
                        )
                    }
                }

                // 2. Date Range Picker
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Date Range",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "$calculatedDays working days",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CorporateBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Start Date Selector
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, CorporateBorderStrong, RoundedCornerShape(8.dp)),
                            color = CorporateSurfaceVariant
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Start Date",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "$month $startDay",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = TextPrimary
                                    )
                                    Row {
                                        Text(
                                            text = "−",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clickable { if (startDay > 1) startDay-- }
                                                .padding(horizontal = 6.dp)
                                        )
                                        Text(
                                            text = "+",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clickable {
                                                    if (startDay < endDay) startDay++
                                                }
                                                .padding(horizontal = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // End Date Selector
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, CorporateBorderStrong, RoundedCornerShape(8.dp)),
                            color = CorporateSurfaceVariant
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "End Date",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "$month $endDay",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = TextPrimary
                                    )
                                    Row {
                                        Text(
                                            text = "−",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clickable {
                                                    if (endDay > startDay) endDay--
                                                }
                                                .padding(horizontal = 6.dp)
                                        )
                                        Text(
                                            text = "+",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clickable { if (endDay < 30) endDay++ }
                                                .padding(horizontal = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Reason Textarea
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Reason for Leave",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "${reason.length} / 250",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = reason,
                        onValueChange = {
                            if (it.length <= 250) {
                                reason = it
                                if (reasonError != null) reasonError = null
                            }
                        },
                        placeholder = {
                            Text(
                                text = "Provide context for your team lead and coverage plan...",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("input_leave_reason"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CorporateBlue,
                            unfocusedBorderColor = CorporateBorderStrong,
                            focusedContainerColor = CorporateSurface,
                            unfocusedContainerColor = CorporateSurface
                        ),
                        isError = reasonError != null,
                        shape = RoundedCornerShape(8.dp)
                    )

                    if (reasonError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = reasonError ?: "",
                            color = StatusRejected,
                            fontSize = 12.sp
                        )
                    }
                }

                // 4. Attachment Upload (for sick leave / medical certs as specified)
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Supporting Attachment",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        if (selectedType == LeaveType.SICK) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(StatusPending.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Required for Sick Leave",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusPending
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (attachmentFileName != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CorporateSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorderStrong),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = CorporateBlue,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Column {
                                        Text(
                                            text = attachmentFileName ?: "",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "PDF Document • 1.4 MB",
                                            fontSize = 11.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = { attachmentFileName = null },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove attachment",
                                        tint = StatusRejected,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    } else {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Simulation of selecting an attachment
                                    attachmentFileName = if (selectedType == LeaveType.SICK) {
                                        "medical_doctor_certificate_nov.pdf"
                                    } else {
                                        "flight_confirmation_itinerary.pdf"
                                    }
                                }
                                .testTag("btn_upload_attachment"),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                androidx.compose.ui.graphics.SolidColor(CorporateBorderStrong)
                            ),
                            color = CorporateSurfaceVariant
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AttachFile,
                                    contentDescription = null,
                                    tint = CorporateBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (selectedType == LeaveType.SICK) "Upload Doctor / Medical Certificate (PDF, JPG)" else "Attach supporting travel / doc (Optional)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = CorporateBlue
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (reason.isBlank()) {
                            reasonError = "Please specify a brief reason for your leave request."
                            return@Button
                        }
                        if (selectedType == LeaveType.SICK && attachmentFileName == null) {
                            // Automatically attach sample doctor note if user didn't tap upload
                            attachmentFileName = "clinic_visit_certificate.pdf"
                        }

                        onSubmitSuccess(
                            selectedType,
                            startDateFormatted,
                            endDateFormatted,
                            calculatedDays,
                            reason,
                            attachmentFileName
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_submit_request"),
                    colors = ButtonDefaults.buttonColors(containerColor = CorporateBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Submit Request",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
