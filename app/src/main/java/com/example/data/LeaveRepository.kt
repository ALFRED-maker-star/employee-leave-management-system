package com.example.data

import com.example.model.AccrualItem
import com.example.model.DepartmentReport
import com.example.model.LeaveBalance
import com.example.model.LeavePolicy
import com.example.model.LeaveRequest
import com.example.model.LeaveStatus
import com.example.model.LeaveType
import com.example.model.MonthlyTrend
import com.example.model.UserProfile
import com.example.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

object LeaveRepository {

    // Pre-configured Corporate Profiles
    val employeeAlex = UserProfile(
        id = "emp-101",
        name = "Alex Rivera",
        email = "alex.rivera@leaveease.corp",
        role = UserRole.EMPLOYEE,
        department = "Engineering",
        jobTitle = "Senior Frontend Developer",
        avatarInitials = "AR",
        managerName = "Sarah Jenkins"
    )

    val managerSarah = UserProfile(
        id = "mgr-201",
        name = "Sarah Jenkins",
        email = "sarah.jenkins@leaveease.corp",
        role = UserRole.MANAGER,
        department = "Engineering",
        jobTitle = "Engineering Lead / Manager",
        avatarInitials = "SJ",
        managerName = "David Chen"
    )

    val adminDavid = UserProfile(
        id = "adm-301",
        name = "David Chen",
        email = "david.chen@leaveease.corp",
        role = UserRole.ADMIN,
        department = "HR & People Ops",
        jobTitle = "VP of People & Culture",
        avatarInitials = "DC",
        managerName = null
    )

    // Current logged-in user state
    private val _currentUser = MutableStateFlow<UserProfile>(employeeAlex)
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // Track login states
    private val _managerLoggedIn = MutableStateFlow(true)
    val managerLoggedIn: StateFlow<Boolean> = _managerLoggedIn.asStateFlow()

    private val _adminLoggedIn = MutableStateFlow(true)
    val adminLoggedIn: StateFlow<Boolean> = _adminLoggedIn.asStateFlow()

    // Company Leave Policies
    private val _policies = MutableStateFlow(
        listOf(
            LeavePolicy(
                id = "pol-1",
                leaveType = LeaveType.ANNUAL,
                annualAllocationDays = 20,
                carryoverLimitDays = 5,
                noticeDaysRequired = 14,
                requiresMedicalCertificate = false,
                isPaid = true,
                description = "Standard paid vacation days accrued monthly for rest and recreation."
            ),
            LeavePolicy(
                id = "pol-2",
                leaveType = LeaveType.SICK,
                annualAllocationDays = 10,
                carryoverLimitDays = 0,
                noticeDaysRequired = 0,
                requiresMedicalCertificate = true,
                isPaid = true,
                description = "Paid sick leave for medical illnesses, surgeries, or doctor appointments. Requires medical note for >2 consecutive days."
            ),
            LeavePolicy(
                id = "pol-3",
                leaveType = LeaveType.PERSONAL,
                annualAllocationDays = 5,
                carryoverLimitDays = 2,
                noticeDaysRequired = 3,
                requiresMedicalCertificate = false,
                isPaid = true,
                description = "Discretionary days for emergencies, moving, family obligations, or personal milestones."
            ),
            LeavePolicy(
                id = "pol-4",
                leaveType = LeaveType.MATERNITY_PATERNITY,
                annualAllocationDays = 60,
                carryoverLimitDays = 0,
                noticeDaysRequired = 30,
                requiresMedicalCertificate = true,
                isPaid = true,
                description = "Paid parental leave following the birth or adoption of a child."
            ),
            LeavePolicy(
                id = "pol-5",
                leaveType = LeaveType.UNPAID,
                annualAllocationDays = 30,
                carryoverLimitDays = 0,
                noticeDaysRequired = 14,
                requiresMedicalCertificate = false,
                isPaid = false,
                description = "Unpaid leave of absence subject to department head and HR approval."
            )
        )
    )
    val policies: StateFlow<List<LeavePolicy>> = _policies.asStateFlow()

    // Leave Requests Database
    private val _requests = MutableStateFlow(
        listOf(
            // Alex's requests
            LeaveRequest(
                id = "REQ-1092",
                employeeId = "emp-101",
                employeeName = "Alex Rivera",
                employeeEmail = "alex.rivera@leaveease.corp",
                department = "Engineering",
                leaveType = LeaveType.ANNUAL,
                startDate = "2026-11-16",
                endDate = "2026-11-20",
                totalDays = 5,
                reason = "Annual family retreat and recharge in Lake Tahoe.",
                status = LeaveStatus.APPROVED,
                attachmentName = null,
                submittedAt = "2026-10-18",
                reviewedBy = "Sarah Jenkins",
                reviewerComment = "Approved! Coverage organized with Marcus Vance.",
                reviewedAt = "2026-10-19"
            ),
            LeaveRequest(
                id = "REQ-1095",
                employeeId = "emp-101",
                employeeName = "Alex Rivera",
                employeeEmail = "alex.rivera@leaveease.corp",
                department = "Engineering",
                leaveType = LeaveType.SICK,
                startDate = "2026-11-02",
                endDate = "2026-11-03",
                totalDays = 2,
                reason = "Wisdom tooth extraction procedure and recovery.",
                status = LeaveStatus.PENDING,
                attachmentName = "dental_surgery_certificate.pdf",
                attachmentSize = "1.4 MB",
                submittedAt = "2026-10-23"
            ),
            LeaveRequest(
                id = "REQ-1044",
                employeeId = "emp-101",
                employeeName = "Alex Rivera",
                employeeEmail = "alex.rivera@leaveease.corp",
                department = "Engineering",
                leaveType = LeaveType.PERSONAL,
                startDate = "2026-10-05",
                endDate = "2026-10-05",
                totalDays = 1,
                reason = "Closing and inspection on apartment lease renewal.",
                status = LeaveStatus.APPROVED,
                attachmentName = null,
                submittedAt = "2026-09-28",
                reviewedBy = "Sarah Jenkins",
                reviewerComment = "Enjoy the day off!",
                reviewedAt = "2026-09-29"
            ),
            LeaveRequest(
                id = "REQ-0988",
                employeeId = "emp-101",
                employeeName = "Alex Rivera",
                employeeEmail = "alex.rivera@leaveease.corp",
                department = "Engineering",
                leaveType = LeaveType.PERSONAL,
                startDate = "2026-08-14",
                endDate = "2026-08-17",
                totalDays = 2,
                reason = "Music festival weekend in Austin.",
                status = LeaveStatus.REJECTED,
                attachmentName = null,
                submittedAt = "2026-08-01",
                reviewedBy = "Sarah Jenkins",
                reviewerComment = "Q3 critical release sprint launch date overlaps. Let's schedule post-launch.",
                reviewedAt = "2026-08-02"
            ),

            // Team requests awaiting Sarah's approval
            LeaveRequest(
                id = "REQ-1098",
                employeeId = "emp-102",
                employeeName = "Marcus Vance",
                employeeEmail = "marcus.v@leaveease.corp",
                department = "Engineering",
                leaveType = LeaveType.ANNUAL,
                startDate = "2026-11-09",
                endDate = "2026-11-13",
                totalDays = 5,
                reason = "Attending international developer summit & taking additional PTO days.",
                status = LeaveStatus.PENDING,
                attachmentName = "conf_schedule_pass.pdf",
                attachmentSize = "820 KB",
                submittedAt = "2026-10-22"
            ),
            LeaveRequest(
                id = "REQ-1101",
                employeeId = "emp-103",
                employeeName = "Elena Rostova",
                employeeEmail = "elena.r@leaveease.corp",
                department = "Engineering",
                leaveType = LeaveType.SICK,
                startDate = "2026-11-04",
                endDate = "2026-11-05",
                totalDays = 2,
                reason = "High viral fever & physician advised strict bedrest.",
                status = LeaveStatus.PENDING,
                attachmentName = "urgent_care_receipt.pdf",
                attachmentSize = "2.1 MB",
                submittedAt = "2026-10-23"
            ),
            LeaveRequest(
                id = "REQ-1104",
                employeeId = "emp-104",
                employeeName = "Chloe Kim",
                employeeEmail = "chloe.kim@leaveease.corp",
                department = "Product",
                leaveType = LeaveType.PERSONAL,
                startDate = "2026-11-25",
                endDate = "2026-11-27",
                totalDays = 3,
                reason = "Early Thanksgiving family travel and volunteer commitments.",
                status = LeaveStatus.PENDING,
                attachmentName = null,
                submittedAt = "2026-10-21"
            ),

            // Company-wide requests for Admin overview & calendar
            LeaveRequest(
                id = "REQ-1070",
                employeeId = "emp-105",
                employeeName = "Michael Scott",
                employeeEmail = "michael.s@leaveease.corp",
                department = "Sales",
                leaveType = LeaveType.ANNUAL,
                startDate = "2026-10-23",
                endDate = "2026-10-27",
                totalDays = 5,
                reason = "Client summit decompression & trip to Florida.",
                status = LeaveStatus.APPROVED,
                attachmentName = null,
                submittedAt = "2026-10-10",
                reviewedBy = "David Chen",
                reviewerComment = "Approved per sales targets met.",
                reviewedAt = "2026-10-11"
            ),
            LeaveRequest(
                id = "REQ-1065",
                employeeId = "emp-106",
                employeeName = "Jessica Pearson",
                employeeEmail = "jessica.p@leaveease.corp",
                department = "HR & Legal",
                leaveType = LeaveType.ANNUAL,
                startDate = "2026-11-02",
                endDate = "2026-11-06",
                totalDays = 5,
                reason = "Annual autumn holiday.",
                status = LeaveStatus.APPROVED,
                attachmentName = null,
                submittedAt = "2026-10-15",
                reviewedBy = "David Chen",
                reviewerComment = "Approved, legal matters handed off.",
                reviewedAt = "2026-10-16"
            ),
            LeaveRequest(
                id = "REQ-1080",
                employeeId = "emp-107",
                employeeName = "James Wilson",
                employeeEmail = "james.w@leaveease.corp",
                department = "Marketing",
                leaveType = LeaveType.SICK,
                startDate = "2026-10-24",
                endDate = "2026-10-25",
                totalDays = 2,
                reason = "Migraine and medical therapy.",
                status = LeaveStatus.APPROVED,
                attachmentName = "doctor_rx.pdf",
                attachmentSize = "450 KB",
                submittedAt = "2026-10-23",
                reviewedBy = "David Chen",
                reviewerComment = "Rest well!",
                reviewedAt = "2026-10-23"
            )
        )
    )
    val requests: StateFlow<List<LeaveRequest>> = _requests.asStateFlow()

    // Accrual History for Alex
    private val _accrualHistory = MutableStateFlow(
        listOf(
            AccrualItem(
                id = "acc-1",
                date = "2026-10-01",
                leaveType = LeaveType.ANNUAL,
                type = "Monthly Accrual",
                amount = 1.67,
                runningBalance = 15.0,
                note = "Standard monthly earned allocation (1.67 days/mo)"
            ),
            AccrualItem(
                id = "acc-2",
                date = "2026-10-05",
                leaveType = LeaveType.PERSONAL,
                type = "Leave Deduction",
                amount = -1.0,
                runningBalance = 4.0,
                note = "Personal day taken (Apartment lease renewal)"
            ),
            AccrualItem(
                id = "acc-3",
                date = "2026-09-01",
                leaveType = LeaveType.ANNUAL,
                type = "Monthly Accrual",
                amount = 1.67,
                runningBalance = 13.33,
                note = "Standard monthly earned allocation"
            ),
            AccrualItem(
                id = "acc-4",
                date = "2026-08-01",
                leaveType = LeaveType.ANNUAL,
                type = "Monthly Accrual",
                amount = 1.67,
                runningBalance = 11.66,
                note = "Standard monthly earned allocation"
            ),
            AccrualItem(
                id = "acc-5",
                date = "2026-01-01",
                leaveType = LeaveType.ANNUAL,
                type = "Yearly Grant & Rollover",
                amount = 10.0,
                runningBalance = 10.0,
                note = "5 carryover days from 2025 + initial grant"
            ),
            AccrualItem(
                id = "acc-6",
                date = "2026-01-01",
                leaveType = LeaveType.SICK,
                type = "Annual Grant",
                amount = 10.0,
                runningBalance = 10.0,
                note = "Full 10 days sick leave granted for calendar year"
            ),
            AccrualItem(
                id = "acc-7",
                date = "2026-01-01",
                leaveType = LeaveType.PERSONAL,
                type = "Annual Grant",
                amount = 5.0,
                runningBalance = 5.0,
                note = "Discretionary personal allocation"
            )
        )
    )
    val accrualHistory: StateFlow<List<AccrualItem>> = _accrualHistory.asStateFlow()

    // Switch Role
    fun switchRole(role: UserRole) {
        val user = when (role) {
            UserRole.EMPLOYEE -> employeeAlex
            UserRole.MANAGER -> managerSarah
            UserRole.ADMIN -> adminDavid
        }
        _currentUser.value = user
    }

    fun setManagerLoggedIn(loggedIn: Boolean) {
        _managerLoggedIn.value = loggedIn
        if (loggedIn) {
            _currentUser.value = managerSarah
        }
    }

    fun setAdminLoggedIn(loggedIn: Boolean) {
        _adminLoggedIn.value = loggedIn
        if (loggedIn) {
            _currentUser.value = adminDavid
        }
    }

    // Submit Request
    fun submitRequest(
        leaveType: LeaveType,
        startDate: String,
        endDate: String,
        totalDays: Int,
        reason: String,
        attachmentName: String? = null
    ): LeaveRequest {
        val user = _currentUser.value
        val newReq = LeaveRequest(
            id = "REQ-${(1100..9999).random()}",
            employeeId = user.id,
            employeeName = user.name,
            employeeEmail = user.email,
            department = user.department,
            leaveType = leaveType,
            startDate = startDate,
            endDate = endDate,
            totalDays = totalDays,
            reason = reason,
            status = LeaveStatus.PENDING,
            attachmentName = attachmentName,
            attachmentSize = if (attachmentName != null) "1.2 MB" else null,
            submittedAt = "2026-10-24"
        )

        _requests.update { listOf(newReq) + it }
        return newReq
    }

    // Cancel Request
    fun cancelRequest(requestId: String): Boolean {
        var found = false
        _requests.update { list ->
            list.map { req ->
                if (req.id == requestId && req.status == LeaveStatus.PENDING) {
                    found = true
                    req.copy(status = LeaveStatus.CANCELLED)
                } else {
                    req
                }
            }
        }
        return found
    }

    // Approve Request
    fun approveRequest(requestId: String, reviewerName: String, comment: String? = null) {
        _requests.update { list ->
            list.map { req ->
                if (req.id == requestId) {
                    req.copy(
                        status = LeaveStatus.APPROVED,
                        reviewedBy = reviewerName,
                        reviewerComment = comment?.ifBlank { "Approved by $reviewerName" },
                        reviewedAt = "2026-10-24"
                    )
                } else {
                    req
                }
            }
        }
    }

    // Reject Request
    fun rejectRequest(requestId: String, reviewerName: String, comment: String? = null) {
        _requests.update { list ->
            list.map { req ->
                if (req.id == requestId) {
                    req.copy(
                        status = LeaveStatus.REJECTED,
                        reviewedBy = reviewerName,
                        reviewerComment = comment?.ifBlank { "Request declined by manager" },
                        reviewedAt = "2026-10-24"
                    )
                } else {
                    req
                }
            }
        }
    }

    // Update Company Policy
    fun updatePolicy(
        type: LeaveType,
        annualAllocation: Int,
        carryoverLimit: Int,
        noticeDays: Int,
        requiresMedicalCert: Boolean
    ) {
        _policies.update { list ->
            list.map { pol ->
                if (pol.leaveType == type) {
                    pol.copy(
                        annualAllocationDays = annualAllocation,
                        carryoverLimitDays = carryoverLimit,
                        noticeDaysRequired = noticeDays,
                        requiresMedicalCertificate = requiresMedicalCert
                    )
                } else {
                    pol
                }
            }
        }
    }

    // Helper: Compute leave balances for a specific employee
    fun getEmployeeBalances(employeeId: String): List<LeaveBalance> {
        val userRequests = _requests.value.filter { it.employeeId == employeeId }
        val currentPolicies = _policies.value

        return listOf(
            LeaveType.ANNUAL,
            LeaveType.SICK,
            LeaveType.PERSONAL
        ).map { type ->
            val policy = currentPolicies.find { it.leaveType == type }
            val allocated = policy?.annualAllocationDays ?: type.defaultDays
            val used = userRequests
                .filter { it.leaveType == type && it.status == LeaveStatus.APPROVED }
                .sumOf { it.totalDays }
            val pending = userRequests
                .filter { it.leaveType == type && it.status == LeaveStatus.PENDING }
                .sumOf { it.totalDays }

            LeaveBalance(
                leaveType = type,
                allocatedDays = allocated,
                usedDays = used,
                pendingDays = pending
            )
        }
    }

    // Department Stats for Admin Dashboard
    fun getDepartmentReports(): List<DepartmentReport> {
        return listOf(
            DepartmentReport("Engineering", 28, 142, 3, 5.1),
            DepartmentReport("Marketing", 14, 88, 1, 6.2),
            DepartmentReport("Sales", 22, 115, 2, 5.2),
            DepartmentReport("Product", 10, 47, 1, 4.7),
            DepartmentReport("HR & Legal", 8, 36, 0, 4.5)
        )
    }

    // Monthly Trend Data
    fun getMonthlyTrends(): List<MonthlyTrend> {
        return listOf(
            MonthlyTrend("Jan", 32, LeaveType.ANNUAL),
            MonthlyTrend("Feb", 24, LeaveType.SICK),
            MonthlyTrend("Mar", 29, LeaveType.PERSONAL),
            MonthlyTrend("Apr", 45, LeaveType.ANNUAL),
            MonthlyTrend("May", 48, LeaveType.ANNUAL),
            MonthlyTrend("Jun", 68, LeaveType.ANNUAL),
            MonthlyTrend("Jul", 85, LeaveType.ANNUAL),
            MonthlyTrend("Aug", 74, LeaveType.ANNUAL),
            MonthlyTrend("Sep", 41, LeaveType.PERSONAL),
            MonthlyTrend("Oct", 52, LeaveType.ANNUAL),
            MonthlyTrend("Nov", 62, LeaveType.ANNUAL),
            MonthlyTrend("Dec", 94, LeaveType.ANNUAL)
        )
    }
}
