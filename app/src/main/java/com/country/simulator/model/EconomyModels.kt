package com.country.simulator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class NationalBudget(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val fiscalYear: Int,
    val totalRevenue: Double,
    val totalExpenses: Double,
    val deficit: Double,
    val surplus: Double,
    
    // Revenue sources
    val incomeTaxRevenue: Double = 0.0,
    val corporateTaxRevenue: Double = 0.0,
    val salesTaxRevenue: Double = 0.0,
    val tariffRevenue: Double = 0.0,
    val resourceRevenue: Double = 0.0,
    val otherRevenue: Double = 0.0,
    
    // Expense categories
    val defenseSpending: Double = 0.0,
    val healthcareSpending: Double = 0.0,
    val educationSpending: Double = 0.0,
    val infrastructureSpending: Double = 0.0,
    val socialWelfareSpending: Double = 0.0,
    val debtServicing: Double = 0.0,
    val otherSpending: Double = 0.0,
    
    val isApproved: Boolean = false,
    val approvedDate: Long? = null
}

@Entity(tableName = "tax_brackets")
data class TaxBracket(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val taxType: TaxType,
    val bracketName: String,
    val minIncome: Double,
    val maxIncome: Double?,
    val taxRate: Double, // percentage
    val isActive: Boolean = true
}

enum class TaxType {
    INCOME,
    CORPORATE,
    SALES,
    LUXURY,
    PROPERTY,
    CAPITAL_GAINS,
    ESTATE
}

@Entity(tableName = "trade_deals")
data class TradeDeal(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val partnerCountryId: Long,
    val partnerCountryName: String,
    val dealType: TradeDealType,
    val status: TradeDealStatus,
    val signedDate: Long? = null,
    val expiryDate: Long? = null,
    val tariffReductions: String, // JSON
    val quotas: String, // JSON
    val specialProvisions: String, // JSON
    val economicImpact: Double = 0.0
}

enum class TradeDealType {
    FREE_TRADE,
    CUSTOMS_UNION,
    BILATERAL,
    MULTILATERAL,
    PREFERENTIAL
}

enum class TradeDealStatus {
    NEGOTIATING,
    SIGNED,
    RATIFIED,
    ACTIVE,
    EXPIRED,
    TERMINATED
}

@Entity(tableName = "businesses")
data class Business(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val ownerId: Long,
    val ownerName: String,
    val businessName: String,
    val businessType: BusinessType,
    val industry: String,
    val location: String,
    val employees: Int,
    val revenue: Double,
    val taxPaid: Double,
    val licenseStatus: LicenseStatus,
    val licenseExpiry: Long?,
    val isUnderAudit: Boolean = false,
    val complianceRating: Double = 100.0
}

enum class BusinessType {
    SOLE_PROPRIETORSHIP,
    PARTNERSHIP,
    CORPORATION,
    LLC,
    COOPERATIVE,
    STATE_OWNED
}

enum class LicenseStatus {
    PENDING,
    ACTIVE,
    EXPIRED,
    SUSPENDED,
    REVOKED
}

@Entity(tableName = "oil_operations")
data class OilOperation(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val operationType: OilOperationType,
    val location: String,
    val companyName: String,
    val permitsApproved: Int,
    val permitsPending: Int,
    val dailyProduction: Double,
    val environmentalImpact: Double, // 1-10
    val workerCount: Int,
    val safetyRating: Double, // 0-100
    val isActive: Boolean = true
}

enum class OilOperationType {
    DRILLING,
    REFINING,
    TRANSPORT,
    STORAGE,
    EXPLORATION
}

@Entity(tableName = "economic_reports")
data class EconomicReport(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val reportDate: Long,
    val reportType: EconomicReportType,
    val gdpGrowth: Double,
    val inflationRate: Double,
    val unemploymentRate: Double,
    val interestRate: Double,
    val consumerConfidence: Double,
    val businessConfidence: Double,
    val currencyStrength: Double,
    val summary: String
}

enum class EconomicReportType {
    MONTHLY,
    QUARTERLY,
    ANNUAL,
    SPECIAL
}
