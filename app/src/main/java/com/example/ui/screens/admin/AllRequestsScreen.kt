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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LeaveRequest
import com.example.model.LeaveStatus
import com.example.model.LeaveType
import com.example.ui.components.LeaveTypeTag
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CorporateBlue
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
import kotlinx.coroutines.launch

@Composable
fun AllRequestsScreen(
    requests: List<LeaveRequest>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var searchQuery by remember { mutableStateOf("") }
    var selectedDept by remember { mutableStateOf("All Departments") }
    var selectedStatus by remember { mutableStateOf<LeaveStatus?>(null) }
    var selectedType by remember { mutableStateOf<LeaveType?>(null) }

    var deptDropdownOpen by remember { mutableStateOf(false) }

    val departments = listOf("All Departments", "Engineering", "Marketing", "Sales", "Product", "HR & Legal")

    val filteredRequests = requests.filter { req ->
        val matchesSearch = searchQuery.isBlank() ||
                req.employeeName.contains(searchQuery, ignoreCase = true) ||
                req.id.contains(searchQuery, ignoreCase = true) ||
                req.department.contains(searchQuery, ignoreCase = true)

        val matchesDept = selectedDept == "All Departments" || req.department.contains(selectedDept, ignoreCase = true)
        val matchesStatus = selectedStatus == null || req.status == selectedStatus
        val matchesType = selectedType == null || req.leaveType == selectedType

        matchesSearch && matchesDept && matchesStatus && matchesType
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
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
                            text = "All Requests Master Table",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Company-wide leave request audit, filtering, and export",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Exported master leave audit log (CSV)")
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("btn_export_csv")
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by employee name, request ID, or department...", fontSize = 13.sp, color = TextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_search_requests"),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CorporateBlue,
                    unfocusedBorderColor = CorporateBorderStrong
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filters row (Department dropdown + Status chips)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Department dropdown selector
                Box {
                    Surface(
                        modifier = Modifier
                            .clickable { deptDropdownOpen = true }
                            .testTag("dropdown_dept_filter"),
                        shape = RoundedCornerShape(8.dp),
                        color = CorporateSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorderStrong)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = selectedDept,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
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
                        expanded = deptDropdownOpen,
                        onDismissRequest = { deptDropdownOpen = false }
                    ) {
                        departments.forEach { dept ->
                            DropdownMenuItem(
                                text = { Text(dept, fontSize = 13.sp) },
                                onClick = {
                                    selectedDept = dept
                                    deptDropdownOpen = false
                                }
                            )
                        }
                    }
                }

                // Status Filter Chips
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    label = { Text("All Status", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CorporateBlue,
                        selectedLabelColor = Color.White
                    )
                )

                FilterChip(
                    selected = selectedStatus == LeaveStatus.PENDING,
                    onClick = {
                        selectedStatus = if (selectedStatus == LeaveStatus.PENDING) null else LeaveStatus.PENDING
                    },
                    label = { Text("Pending", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StatusPending,
                        selectedLabelColor = Color.White
                    )
                )

                FilterChip(
                    selected = selectedStatus == LeaveStatus.APPROVED,
                    onClick = {
                        selectedStatus = if (selectedStatus == LeaveStatus.APPROVED) null else LeaveStatus.APPROVED
                    },
                    label = { Text("Approved", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StatusApproved,
                        selectedLabelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Total results count badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Showing ${filteredRequests.size} of ${requests.size} requests",
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )

                if (selectedStatus != null || selectedDept != "All Departments" || searchQuery.isNotEmpty()) {
                    Text(
                        text = "Reset Filters",
                        fontSize = 12.sp,
                        color = CorporateBlue,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable {
                                selectedStatus = null
                                selectedDept = "All Departments"
                                searchQuery = ""
                            }
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Table / List
            if (filteredRequests.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No requests match the selected department, status, or search query.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredRequests, key = { it.id }) { req ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("table_row_${req.id}"),
                            colors = CardDefaults.cardColors(containerColor = CorporateSurface),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CorporateBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                                        Text(
                                            text = req.employeeName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "(${req.department})",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }
                                    StatusBadge(status = req.status)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        LeaveTypeTag(leaveType = req.leaveType)
                                        Text(
                                            text = "${req.startDate} → ${req.endDate}",
                                            fontSize = 12.sp,
                                            color = TextSecondary
                                        )
                                    }

                                    Text(
                                        text = "${req.totalDays} Days",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = CorporateBlue
                                    )
                                }

                                Text(
                                    text = "“${req.reason}”",
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
