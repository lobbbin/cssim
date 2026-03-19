package com.country.simulator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ministries")
data class Ministry(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val ministerName: String,
    val ministerId: Long?,
    val budget: Double,
    val allocatedBudget: Double,
    val spentBudget: Double,
    val employeeCount: Int,
    val efficiency: Double, // 0-100
    val corruptionLevel: Double, // 0-100
    val publicApproval: Double, // 0-100
    val priorities: String = "", // JSON array
    val activeProjects: Int = 0,
    val isActive: Boolean = true
}

enum class MinistryType {
    DEFENSE,
    FOREIGN_AFFAIRS,
    TREASURY,
    JUSTICE,
    INTERIOR,
    HEALTH,
    EDUCATION,
    TRANSPORT,
    ENERGY,
    COMMERCE,
    LABOR,
    ENVIRONMENT,
    AGRICULTURE,
    HOUSING,
    SOCIAL_WELFARE
}

@Entity(tableName = "appointees")
data class Appointee(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val position: String,
    val ministry: MinistryType,
    val appointmentDate: Long,
    val termLength: Int, // years
    val termEnd: Long?,
    val qualifications: String, // JSON array
    val previousExperience: String,
    val approvalRating: Double, // 0-100
    val loyalty: Double, // 0-100
    val competence: Double, // 0-100
    val scandals: Int = 0,
    val isActive: Boolean = true,
    val isSenateConfirmed: Boolean = true
}

@Entity(tableName = "government_departments")
data class GovernmentDepartment(
    @PrimaryKey val id: Long = 0,
    val ministryId: Long,
    val countryId: Long,
    val name: String,
    val departmentHead: String,
    val employeeCount: Int,
    val budget: Double,
    val responsibilities: String, // JSON array
    val performance: Double, // 0-100
    val backlog: Int,
    val isActive: Boolean = true
}

@Entity(tableName = "agency_heads")
data class AgencyHead(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val agencyName: String,
    val agencyType: AgencyType,
    val appointmentDate: Long,
    val qualifications: String,
    val budget: Double,
    val staffCount: Int,
    val effectiveness: Double, // 0-100
    val controversies: Int = 0,
    val isActive: Boolean = true
}

enum class AgencyType {
    INTELLIGENCE,
    LAW_ENFORCEMENT,
    REGULATORY,
    PUBLIC_HEALTH,
    ENVIRONMENTAL,
    FINANCIAL,
    ELECTORAL,
    STATISTICS,
    RESEARCH
}

@Entity(tableName = "purchase_orders")
data class PurchaseOrder(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val ministryId: Long?,
    val departmentId: Long?,
    val orderNumber: String,
    val description: String,
    val vendor: String,
    val amount: Double,
    val purpose: String,
    val status: PurchaseOrderStatus,
    val requestedDate: Long,
    val approvedDate: Long?,
    val approvedBy: String?,
    val deliveryDate: Long?,
    val isUrgent: Boolean = false,
    val isSuspicious: Boolean = false
}

enum class PurchaseOrderStatus {
    PENDING,
    APPROVED,
    REJECTED,
    FULFILLED,
    CANCELLED
}

@Entity(tableName = "expense_reports")
data class ExpenseReport(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val ministryId: Long,
    val departmentId: Long,
    val reportDate: Long,
    val category: ExpenseCategory,
    val amount: Double,
    val description: String,
    val submittedBy: String,
    val approvedBy: String?,
    val status: ExpenseStatus,
    val isFlagged: Boolean = false,
    val flagReason: String? = null
}

enum class ExpenseCategory {
    TRAVEL,
    EQUIPMENT,
    PERSONNEL,
    CONTRACTS,
    OPERATIONS,
    MARKETING,
    RESEARCH,
    OTHER
}

enum class ExpenseStatus {
    PENDING,
    APPROVED,
    REJECTED,
    UNDER_REVIEW
}

@Entity(tableName = "inter_department_disputes")
data class InterDepartmentDispute(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val department1Id: Long,
    val department1Name: String,
    val department2Id: Long,
    val department2Name: String,
    val disputeType: DisputeType,
    val description: String,
    val severity: Int, // 1-10
    val dateReported: Long,
    val resolved: Boolean = false,
    val resolution: String? = null,
    val resolvedDate: Long? = null,
    val mediator: String? = null
}

enum class DisputeType {
    BUDGET,
    JURISDICTION,
    RESOURCE,
    POLICY,
    PERSONNEL
}

@Entity(tableName = "outreach_plans")
data class OutreachPlan(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val ministryId: Long,
    val campaignName: String,
    val targetAudience: String,
    val channels: String, // JSON array
    val budget: Double,
    val startDate: Long,
    val endDate: Long,
    val metrics: String, // JSON
    val effectiveness: Double, // 0-100
    val isActive: Boolean = true
}
