package com.country.simulator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sports_teams")
data class SportsTeam(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val sportType: SportType,
    val teamName: String,
    val coachName: String,
    val captainName: String?,
    val ranking: Int,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val players: String = "", // JSON array of player IDs
    val budget: Double,
    val sponsor: String?,
    val homeStadium: String,
    val fanBase: Int,
    val morale: Double, // 0-100
    val isActive: Boolean = true
)

enum class SportType {
    FOOTBALL,
    BASKETBALL,
    TENNIS,
    SWIMMING,
    ATHLETICS,
    GYMNASTICS,
    CYCLING,
    BOXING,
    WRESTLING,
    VOLLEYBALL,
    RUGBY,
    CRICKET,
    HOCKEY,
    BASEBALL
}

@Entity(tableName = "players")
data class Player(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val teamId: Long?,
    val name: String,
    val sportType: SportType,
    val position: String,
    val age: Int,
    val height: Double, // cm
    val weight: Double, // kg
    val nationality: String,
    val contractValue: Double,
    val contractExpiry: Long?,
    val performance: Double, // 0-100
    val fitness: Double, // 0-100
    val marketValue: Double,
    val goals: Int = 0,
    val assists: Int = 0,
    val caps: Int = 0, // international appearances
    val isSuspended: Boolean = false,
    val suspensionReason: String? = null,
    val isActive: Boolean = true
)

@Entity(tableName = "stadiums")
data class Stadium(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val stadiumName: String,
    val location: String,
    val capacity: Int,
    val sportType: SportType,
    val homeTeam: String?,
    val yearBuilt: Int,
    val condition: Double, // 0-100
    val facilities: String = "", // JSON array
    val averageAttendance: Int,
    val revenue: Double,
    val isUnderRenovation: Boolean = false,
    val renovationBudget: Double = 0.0
)

@Entity(tableName = "stadium_designs")
data class StadiumDesign(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val projectName: String,
    val location: String,
    val capacity: Int,
    val designType: String,
    val architect: String,
    estimatedCost: Double,
    val features: String = "", // JSON array
    val environmentalRating: Double,
    val publicSupport: Double, // 0-100
    val status: ProjectStatus,
    val approvalDate: Long?
)

@Entity(tableName = "sports_events")
data class SportsEvent(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val eventName: String,
    val eventType: SportsEventType,
    val sportType: SportType,
    val startDate: Long,
    val endDate: Long,
    val location: String,
    val participatingCountries: String, // JSON array
    val expectedAttendees: Int,
    val budget: Double,
    val revenue: Double,
    val status: EventStatus,
    val bidWon: Boolean = false,
    val bidDate: Long?,
    val infrastructureNeeded: String = "" // JSON array
)

enum class SportsEventType {
    OLYMPICS,
    WORLD_CUP,
    CONTINENTAL_CHAMPIONSHIP,
    FRIENDLY,
    QUALIFIER,
    TOURNAMENT,
    LEAGUE
}

enum class EventStatus {
    BIDDING,
    AWARDED,
    PLANNING,
    PREPARATION,
    ACTIVE,
    COMPLETED,
    CANCELLED
}

@Entity(tableName = "doping_cases")
data class DopingCase(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val athleteName: String,
    val sportType: SportType,
    val substance: String,
    val testDate: Long,
    val testType: TestType,
    val result: String,
    val status: DopingStatus,
    val suspensionLength: Int, // months
    val startDate: Long,
    val endDate: Long?,
    val appeal: Boolean = false,
    val appealDate: Long?,
    val mediaAttention: Int // 1-10
)

enum class TestType {
    URINE,
    BLOOD,
    RANDOM,
    SCHEDULED,
    COMPETITION,
    OUT_OF_COMPETITION
}

enum class DopingStatus {
    PENDING,
    CONFIRMED,
    APPEALED,
    SUSPENDED,
    CLEARED,
    SERVED
}

@Entity(tableName = "stadium_vendors")
data class StadiumVendor(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val stadiumId: Long,
    val stadiumName: String,
    val vendorName: String,
    val vendorType: VendorType,
    val location: String, // section/stand
    val contractValue: Double,
    val contractExpiry: Long,
    val revenue: Double,
    val customerRating: Double, // 0-100
    val healthRating: Double, // 0-100
    val isActive: Boolean = true
)

enum class VendorType {
    FOOD,
    BEVERAGE,
    MERCHANDISE,
    SOUVENIRS,
    PARKING,
    ENTERTAINMENT
}

@Entity(tableName = "elite_athletes")
data class EliteAthlete(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val sportType: SportType,
    val specialty: String,
    val age: Int,
    val worldRanking: Int,
    val nationalRanking: Int,
    val medals: String = "", // JSON array
    val fundingAmount: Double,
    val sponsor: String?,
    val coach: String,
    val trainingFacility: String,
    val nextCompetition: String?,
    val nextCompetitionDate: Long?,
    val performanceTrend: Trend,
    val isActive: Boolean = true
)

enum class Trend {
    IMPROVING,
    STABLE,
    DECLINING
}

@Entity(tableName = "fitness_policies")
data class FitnessPolicy(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val policyName: String,
    val policyType: FitnessPolicyType,
    val targetDemographic: String,
    val budget: Double,
    val participants: Int,
    val goals: String = "", // JSON array
    val metrics: String = "", // JSON
    val effectiveness: Double, // 0-100
    val startDate: Long,
    val endDate: Long?,
    val isActive: Boolean = true
)

enum class FitnessPolicyType {
    SCHOOL_SPORTS,
    PUBLIC_FACILITIES,
    WORKPLACE_WELLNESS,
    SENIOR_FITNESS,
    YOUTH_DEVELOPMENT,
    DISABLED_SPORTS,
    COMMUNITY_LEAGUES
}

@Entity(tableName = "sports_sponsors")
data class SportsSponsor(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val sponsorName: String,
    val sponsorType: String,
    val sponsoredEntity: String, // team/athlete/event
    val entityType: EntityType,
    val contractValue: Double,
    val contractLength: Int, // years
    val startDate: Long,
    val endDate: Long,
    val activationBudget: Double,
    val roi: Double, // return on investment
    val isActive: Boolean = true
)

enum class EntityType {
    TEAM,
    ATHLETE,
    EVENT,
    FACILITY,
    LEAGUE
}
