package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AnnualLeaveColor
import com.example.ui.theme.MaternityLeaveColor
import com.example.ui.theme.PersonalLeaveColor
import com.example.ui.theme.SickLeaveColor
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusApprovedBg
import com.example.ui.theme.StatusApprovedBorder
import com.example.ui.theme.StatusApprovedText
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusPendingBorder
import com.example.ui.theme.StatusPendingText
import com.example.ui.theme.StatusRejected
import com.example.ui.theme.StatusRejectedBg
import com.example.ui.theme.StatusRejectedBorder
import com.example.ui.theme.StatusRejectedText
import com.example.ui.theme.TextMuted
import com.example.ui.theme.UnpaidLeaveColor

enum class UserRole(val displayName: String, val badgeTitle: String) {
    EMPLOYEE("Employee", "Employee Portal"),
    MANAGER("Manager", "Manager Approvals Hub"),
    ADMIN("Admin / HR", "HR Admin Center")
}

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val department: String,
    val jobTitle: String,
    val avatarInitials: String,
    val managerName: String? = null
)

enum class LeaveType(
    val displayName: String,
    val color: Color,
    val defaultDays: Int,
    val requiresAttachment: Boolean
) {
    ANNUAL("Annual Leave", AnnualLeaveColor, 20, false),
    SICK("Sick Leave", SickLeaveColor, 10, true),
    PERSONAL("Personal Leave", PersonalLeaveColor, 5, false),
    MATERNITY_PATERNITY("Maternity / Paternity", MaternityLeaveColor, 60, true),
    UNPAID("Unpaid Leave", UnpaidLeaveColor, 30, false)
}

enum class LeaveStatus(
    val label: String,
    val badgeColor: Color,
    val badgeBg: Color,
    val badgeBorder: Color,
    val textColor: Color
) {
    PENDING("Pending", StatusPending, StatusPendingBg, StatusPendingBorder, StatusPendingText),
    APPROVED("Approved", StatusApproved, StatusApprovedBg, StatusApprovedBorder, StatusApprovedText),
    REJECTED("Rejected", StatusRejected, StatusRejectedBg, StatusRejectedBorder, StatusRejectedText),
    CANCELLED("Cancelled", TextMuted, Color(0xFFF1F5F9), Color(0xFFE2E8F0), Color(0xFF64748B))
}

data class LeaveRequest(
    val id: String,
    val employeeId: String,
    val employeeName: String,
    val employeeEmail: String,
    val department: String,
    val leaveType: LeaveType,
    val startDate: String, // e.g., "2026-11-03"
    val endDate: String,   // e.g., "2026-11-06"
    val totalDays: Int,
    val reason: String,
    val status: LeaveStatus,
    val attachmentName: String? = null,
    val attachmentSize: String? = null,
    val submittedAt: String,
    val reviewedBy: String? = null,
    val reviewerComment: String? = null,
    val reviewedAt: String? = null
)

data class LeaveBalance(
    val leaveType: LeaveType,
    val allocatedDays: Int,
    val usedDays: Int,
    val pendingDays: Int
) {
    val remainingDays: Int
        get() = (allocatedDays - usedDays - pendingDays).coerceAtLeast(0)

    val progressFraction: Float
        get() = if (allocatedDays > 0) (usedDays.toFloat() / allocatedDays.toFloat()).coerceIn(0f, 1f) else 0f
}

data class AccrualItem(
    val id: String,
    val date: String,
    val leaveType: LeaveType,
    val type: String, // e.g. "Monthly Accrual", "Yearly Grant", "Carryover", "Leave Deduction"
    val amount: Double,
    val runningBalance: Double,
    val note: String
)

data class LeavePolicy(
    val id: String,
    val leaveType: LeaveType,
    val annualAllocationDays: Int,
    val carryoverLimitDays: Int,
    val noticeDaysRequired: Int,
    val requiresMedicalCertificate: Boolean,
    val isPaid: Boolean,
    val description: String
)

data class DepartmentReport(
    val department: String,
    val employeeCount: Int,
    val totalLeaveDaysTaken: Int,
    val pendingRequestsCount: Int,
    val avgDaysPerEmployee: Double
)

data class MonthlyTrend(
    val monthName: String,
    val daysTaken: Int,
    val peakType: LeaveType
)
