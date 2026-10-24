package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.LeaveRepository
import com.example.model.LeaveStatus
import com.example.model.LeaveType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("LeaveEase", appName)
    }

    @Test
    fun `test leave repository submit request and balances`() {
        val initialBalances = LeaveRepository.getEmployeeBalances("emp-101")
        val annualBal = initialBalances.find { it.leaveType == LeaveType.ANNUAL }
        assertNotNull(annualBal)

        val newReq = LeaveRepository.submitRequest(
            leaveType = LeaveType.ANNUAL,
            startDate = "2026-12-01",
            endDate = "2026-12-05",
            totalDays = 5,
            reason = "Winter holidays test"
        )

        assertEquals(LeaveStatus.PENDING, newReq.status)

        // Test cancel
        val cancelled = LeaveRepository.cancelRequest(newReq.id)
        assertTrue(cancelled)
    }
}
