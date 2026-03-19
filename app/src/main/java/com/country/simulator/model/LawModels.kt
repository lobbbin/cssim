package com.country.simulator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laws")
data class Law(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val title: String,
    val description: String,
    val lawType: LawType,
    val status: LawStatus,
    val introducedDate: Long,
    val passedDate: Long? = null,
    val vetoedDate: Long? = null,
    val effectiveDate: Long? = null,
    val sponsorName: String,
    val coSponsors: String = "", // JSON array
    val fullText: String,
    val clauses: String = "", // JSON array
    val amendments: String = "", // JSON array
    val voteCount: Int = 0,
    val voteFor: Int = 0,
    val voteAgainst: Int = 0,
    val abstentions: Int = 0,
    val publicSupport: Double = 50.0,
    val economicImpact: Double = 0.0,
    val socialImpact: Double = 0.0
}

enum class LawType {
    TAX,
    CRIMINAL,
    CIVIL,
    REGULATORY,
    CONSTITUTIONAL,
    EMERGENCY,
    BUDGET,
    SOCIAL,
    ECONOMIC,
    ENVIRONMENTAL
}

enum class LawStatus {
    DRAFT,
    COMMITTEE_REVIEW,
    FLOOR_DEBATE,
    PASSED,
    VETOED,
    ENACTED,
    REPEALED,
    UNCONSTITUTIONAL
}

@Entity(tableName = "bills")
data class Bill(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val billNumber: String,
    val title: String,
    val summary: String,
    val fullText: String,
    val currentStage: BillStage,
    val currentCommittee: String?,
    val introducedBy: String,
    val introducedDate: Long,
    val committees: String = "", // JSON array of committee names
    val hearings: String = "", // JSON array
    val votes: String = "", // JSON array of vote records
    val lobbySupport: Double = 0.0,
    val lobbyOpposition: Double = 0.0
}

enum class BillStage {
    DRAFTING,
    INTRODUCED,
    COMMITTEE,
    SUBCOMMITTEE,
    MARKUP,
    FLOOR,
    CONFERENCE,
    FINAL_PASSAGE,
    EXECUTIVE_REVIEW,
    ENACTED
}

@Entity(tableName = "committees")
data class Committee(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val committeeType: CommitteeType,
    val chairName: String,
    val rankingMemberName: String,
    val memberCount: Int,
    val members: String = "", // JSON array
    val currentBills: Int = 0,
    val hearingsScheduled: String = "", // JSON array
    val budget: Double = 0.0
}

enum class CommitteeType {
    STANDING,
    SELECT,
    JOINT,
    CONFERENCE,
    SUBCOMMITTEE
}

@Entity(tableName = "court_cases")
data class CourtCase(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val caseNumber: String,
    val caseName: String,
    val caseType: CaseType,
    val court: String,
    val plaintiff: String,
    val defendant: String,
    val description: String,
    val filingDate: Long,
    val status: CaseStatus,
    val judge: String?,
    val jury: String = "", // JSON array
    val evidence: String = "", // JSON array
    val witnesses: String = "", // JSON array
    val verdict: String? = null,
    val sentence: String? = null,
    val damages: Double = 0.0,
    val isAppealed: Boolean = false,
    val appealDate: Long? = null
}

enum class CaseType {
    CIVIL,
    CRIMINAL,
    CONSTITUTIONAL,
    ADMINISTRATIVE,
    FAMILY,
    CORPORATE,
    TAX
}

enum class CaseStatus {
    FILED,
    PRE_TRIAL,
    TRIAL,
    DELIBERATION,
    VERDICT,
    SENTENCED,
    APPEALED,
    CLOSED
}

@Entity(tableName = "judges")
data class Judge(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val court: String,
    val position: String,
    val appointedDate: Long,
    val appointedBy: String,
    val termLength: Int, // years, 0 for lifetime
    val termEnd: Long?,
    val ideology: String,
    val rulingsCount: Int = 0,
    val approvalRating: Double = 50.0,
    val isActive: Boolean = true
}

@Entity(tableName = "sentencing_guidelines")
data class SentencingGuideline(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val crimeType: String,
    val offenseLevel: Int, // 1-43
    val criminalHistoryCategory: Int, // I-VI
    val minSentence: Int, // months
    val maxSentence: Int, // months
    val guidelinesType: GuidelinesType,
    val isActive: Boolean = true
}

enum class GuidelinesType {
    MANDATORY,
    ADVISORY,
    RECOMMENDED
}

@Entity(tableName = "legal_aid")
data class LegalAid(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val programName: String,
    val eligibilityCriteria: String, // JSON
    val fundingAmount: Double,
    val casesHandled: Int,
    val successRate: Double,
    val averageWaitTime: Int, // days
    val isActive: Boolean = true
}
