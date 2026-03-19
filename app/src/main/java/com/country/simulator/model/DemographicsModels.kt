package com.country.simulator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "demographics")
data class Demographic(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val region: String,
    val totalPopulation: Long,
    val malePopulation: Long,
    val femalePopulation: Long,
    val ageGroup0_14: Long,
    val ageGroup15_64: Long,
    val ageGroup65plus: Long,
    val birthRate: Double, // per 1000
    val deathRate: Double, // per 1000
    val migrationRate: Double, // per 1000
    val lifeExpectancy: Double,
    val fertilityRate: Double,
    val urbanPopulation: Long,
    val ruralPopulation: Long,
    val populationDensity: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "immigration_cases")
data class ImmigrationCase(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val caseNumber: String,
    val applicantName: String,
    val applicantNationality: String,
    val applicationType: ImmigrationType,
    val reason: String,
    val familyMembers: Int,
    val status: ImmigrationStatus,
    val applicationDate: Long,
    val decisionDate: Long?,
    val decision: String?,
    val assignedOfficer: String,
    val priority: Priority,
    val isRefugee: Boolean = false,
    val isHighTalent: Boolean = false,
    val integrationProgram: Boolean = false
)

enum class ImmigrationType {
    WORK,
    FAMILY,
    STUDENT,
    REFUGEE,
    ASYLUM,
    INVESTOR,
    RETIREMENT,
    PERMANENT_RESIDENT,
    CITIZENSHIP
}

enum class ImmigrationStatus {
    PENDING,
    UNDER_REVIEW,
    INTERVIEW_SCHEDULED,
    APPROVED,
    REJECTED,
    APPEALED,
    DEPORTATION
}

enum class Priority {
    LOW,
    NORMAL,
    HIGH,
    URGENT
}

@Entity(tableName = "visa_quotas")
data class VisaQuota(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val visaType: ImmigrationType,
    val nationality: String,
    val annualQuota: Int,
    val usedQuota: Int,
    val remainingQuota: Int,
    val year: Int,
    val isActive: Boolean = true
)

@Entity(tableName = "health_programs")
data class HealthProgram(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val programName: String,
    val programType: HealthProgramType,
    val description: String,
    val targetPopulation: String,
    val budget: Double,
    val spent: Double,
    val startDate: Long,
    val endDate: Long?,
    val coverage: Int, // number of people
    val effectiveness: Double, // 0-100
    val isActive: Boolean = true
)

enum class HealthProgramType {
    VACCINATION,
    DISEASE_PREVENTION,
    MATERNAL_HEALTH,
    MENTAL_HEALTH,
    ADDICTION_TREATMENT,
    EMERGENCY_RESPONSE,
    RESEARCH,
    PUBLIC_AWARENESS
}

@Entity(tableName = "medicine_stockpiles")
data class MedicineStockpile(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val medicineName: String,
    val medicineType: String,
    val quantity: Int, // units
    val unit: String, // tablets, vials, etc.
    val expiryDate: Long,
    val storageLocation: String,
    val criticalLevel: Int,
    val isCritical: Boolean = false,
    val lastRestocked: Long
)

@Entity(tableName = "disease_outbreaks")
data class DiseaseOutbreak(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val diseaseName: String,
    val diseaseType: DiseaseType,
    val location: String,
    val confirmedCases: Int,
    val suspectedCases: Int,
    val deaths: Int,
    val recovered: Int,
    val outbreakDate: Long,
    val containmentLevel: Double, // 0-100
    val r0Value: Double, // reproduction number
    val mortalityRate: Double,
    val quarantineActive: Boolean = false,
    val quarantineAreas: String = "", // JSON array
    val vaccineAvailable: Boolean = false,
    val status: OutbreakStatus
)

enum class DiseaseType {
    VIRAL,
    BACTERIAL,
    PARASITIC,
    FUNGAL,
    UNKNOWN
}

enum class OutbreakStatus {
    CONTAINED,
    SPREADING,
    EPIDEMIC,
    PANDEMIC,
    ERADICATED
}

@Entity(tableName = "social_groups")
data class SocialGroup(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val groupName: String,
    val groupType: GroupType,
    val population: Long,
    val percentage: Double,
    val averageIncome: Double,
    val unemploymentRate: Double,
    val educationLevel: Double, // 0-100
    val satisfactionLevel: Double, // 0-100
    val tensionLevel: Double, // 0-100
    val leaders: String = "", // JSON array
    val demands: String = "", // JSON array
    val isActive: Boolean = true
)

enum class GroupType {
    ETHNIC,
    RELIGIOUS,
    ECONOMIC_CLASS,
    POLITICAL,
    REGIONAL,
    LINGUISTIC,
    CULTURAL
}

@Entity(tableName = "welfare_payments")
data class WelfarePayment(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val recipientId: Long,
    val recipientName: String,
    val paymentType: WelfareType,
    val amount: Double,
    val frequency: PaymentFrequency,
    val startDate: Long,
    val endDate: Long?,
    val eligibilityCriteria: String, // JSON
    val status: PaymentStatus,
    val lastPaymentDate: Long?,
    val totalPaid: Double = 0.0
)

enum class WelfareType {
    UNEMPLOYMENT,
    DISABILITY,
    OLD_AGE_PENSION,
    CHILD_BENEFIT,
    HOUSING_ASSISTANCE,
    FOOD_STAMPS,
    HEALTHCARE_SUBSIDY,
    EDUCATION_GRANT
}

enum class PaymentFrequency {
    WEEKLY,
    BIWEEKLY,
    MONTHLY,
    QUARTERLY,
    ANNUAL
}

enum class PaymentStatus {
    ACTIVE,
    PENDING,
    SUSPENDED,
    TERMINATED
}

@Entity(tableName = "community_projects")
data class CommunityProject(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val projectName: String,
    val neighborhood: String,
    val projectType: CommunityProjectType,
    val budget: Double,
    val communitySupport: Double, // 0-100
    val status: ProjectStatus,
    val startDate: Long,
    val completionDate: Long?,
    val beneficiaries: Int,
    val communityLeaders: String = "" // JSON array
)

enum class CommunityProjectType {
    RECREATION_CENTER,
    LIBRARY,
    COMMUNITY_GARDEN,
    YOUTH_PROGRAM,
    SENIOR_CENTER,
    JOB_TRAINING,
    AFFORDABLE_HOUSING,
    INFRASTRUCTURE
}

@Entity(tableName = "job_training_programs")
data class JobTrainingProgram(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val programName: String,
    val targetGroup: String,
    val skills: String, // JSON array
    val duration: Int, // weeks
    val capacity: Int,
    val enrolled: Int,
    val graduationRate: Double, // 0-100
    val employmentRate: Double, // 0-100
    val budget: Double,
    val isActive: Boolean = true
)
