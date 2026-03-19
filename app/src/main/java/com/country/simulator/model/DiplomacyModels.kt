package com.country.simulator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diplomatic_relations")
data class DiplomaticRelation(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val foreignCountryId: Long,
    val foreignCountryName: String,
    val relationLevel: Int, // -100 to 100
    val trustLevel: Int, // 0-100
    val tradeVolume: Double,
    val ambassadorName: String?,
    val embassyEstablished: Boolean,
    val lastContact: Long,
    val issues: String = "", // JSON array
    val agreements: String = "" // JSON array
)

@Entity(tableName = "alliances")
data class Alliance(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val allianceName: String,
    val allianceType: AllianceType,
    val members: String, // JSON array of country IDs
    val foundedDate: Long,
    val headquarters: String,
    val secretaryGeneral: String?,
    val mutualDefense: Boolean,
    val sharedTechnology: Boolean,
    val jointExercises: Boolean,
    val isActive: Boolean = true
)

enum class AllianceType {
    MUTUAL_DEFENSE,
    NON_AGGRESSION,
    NEUTRALITY,
    ENTENTE,
    MILITARY_COALITION
}

@Entity(tableName = "trade_blocs")
data class TradeBloc(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val blocName: String,
    val blocType: TradeBlocType,
    val members: String, // JSON array
    val foundedDate: Long,
    val headquarters: String,
    val commonTariff: Double,
    val freeMovement: Boolean,
    val commonCurrency: Boolean,
    val isActive: Boolean = true
)

enum class TradeBlocType {
    FREE_TRADE_AREA,
    CUSTOMS_UNION,
    COMMON_MARKET,
    ECONOMIC_UNION,
    MONETARY_UNION
}

@Entity(tableName = "diplomats")
data class Diplomat(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val rank: DiplomaticRank,
    val assignedCountry: Long?,
    val assignedCountryName: String?,
    val currentPost: String,
    val appointmentDate: Long,
    val languages: String, // JSON array
    val effectiveness: Double, // 0-100
    val scandals: Int = 0,
    val isActive: Boolean = true
)

enum class DiplomaticRank {
    AMBASSADOR,
    MINISTER,
    COUNSELOR,
    FIRST_SECRETARY,
    SECOND_SECRETARY,
    THIRD_SECRETARY,
    ATTACHE,
    CULTURAL_ATTACHE // Spy cover
}

@Entity(tableName = "embassy_projects")
data class EmbassyProject(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val embassyLocation: String,
    val projectName: String,
    val projectType: EmbassyProjectType,
    val budget: Double,
    val purpose: String,
    val status: ProjectStatus,
    val startDate: Long,
    val completionDate: Long?,
    val contractor: String?
)

enum class EmbassyProjectType {
    CONSTRUCTION,
    RENOVATION,
    SECURITY_UPGRADE,
    CULTURAL_CENTER,
    VISA_CENTER,
    STAFF_HOUSING
}

@Entity(tableName = "spies")
data class Spy(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val codename: String,
    val realName: String,
    val coverIdentity: String,
    val assignedCountry: Long,
    val assignedCountryName: String,
    val rank: SpyRank,
    val skills: String, // JSON array
    val missionsCompleted: Int,
    val missionsFailed: Int,
    val coverStatus: CoverStatus,
    val isCompromised: Boolean = false,
    val lastContact: Long,
    val isActive: Boolean = true
)

enum class SpyRank {
    ROOKIE,
    FIELD_AGENT,
    SENIOR_AGENT,
    CASE_OFFICER,
    STATION_CHIEF
}

enum class CoverStatus {
    SECURE,
    SUSPECTED,
    COMPROMISED,
    EXTRACTED
}

@Entity(tableName = "sanctions")
data class Sanction(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val targetCountryId: Long,
    val targetCountryName: String,
    val sanctionType: SanctionType,
    val severity: Int, // 1-10
    val startDate: Long,
    val endDate: Long?,
    val description: String,
    val economicImpact: Double,
    val domesticBacklash: Double, // 0-100
    val internationalSupport: Double, // 0-100
    val isActive: Boolean = true
)

enum class SanctionType {
    TRADE_EMBARGO,
    ASSET_FREEZE,
    TRAVEL_BAN,
    ARMS_EMBARGO,
    FINANCIAL,
    DIPLOMATIC,
    SPORTING,
    CULTURAL
}

@Entity(tableName = "wars")
data class War(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val warName: String,
    val enemyCountryId: Long,
    val enemyCountryName: String,
    val casusBelli: String,
    val declarationDate: Long,
    val endDate: Long?,
    val status: WarStatus,
    val battlesWon: Int,
    val battlesLost: Int,
    val casualties: Int,
    val enemyCasualties: Int,
    val territoryGained: Double, // sq km
    val territoryLost: Double,
    val economicCost: Double,
    val publicSupport: Double, // 0-100
    val internationalSupport: Double // 0-100
)

enum class WarStatus {
    DECLARED,
    ACTIVE,
    CEASEFIRE,
    PEACE_TALKS,
    ENDED,
    VICTORY,
    DEFEAT,
    STALEMATE
}

@Entity(tableName = "work_permits")
data class WorkPermit(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val permitNumber: String,
    val applicantName: String,
    val applicantNationality: String,
    val employerName: String,
    val jobTitle: String,
    val industry: String,
    val salary: Double,
    val startDate: Long,
    val endDate: Long,
    val status: PermitStatus,
    val isHighTalent: Boolean = false,
    val dependents: Int = 0,
    val conditions: String = "" // JSON array
)
