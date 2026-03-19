package com.country.simulator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infrastructure_projects")
data class InfrastructureProject(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val projectType: InfrastructureType,
    val name: String,
    val description: String,
    val location: String,
    val coordinates: String?, // JSON {lat, lng}
    val contractor: String,
    val contractorRating: Double, // 0-100
    val budget: Double,
    val spent: Double,
    val startDate: Long,
    val estimatedEndDate: Long,
    val actualEndDate: Long?,
    val progress: Double, // 0-100
    val status: ProjectStatus,
    val environmentalImpact: Double, // 1-10
    val publicSupport: Double, // 0-100
    val workerCount: Int,
    val safetyIncidents: Int = 0
)

enum class InfrastructureType {
    POWER_STATION,
    POWER_PYTHON,
    HIGHWAY,
    BRIDGE,
    TUNNEL,
    RAILWAY,
    AIRPORT,
    SEAPORT,
    DAM,
    HOSPITAL,
    SCHOOL,
    HOUSING,
    WATER_TREATMENT,
    TELECOM_TOWER
}

enum class ProjectStatus {
    PLANNING,
    APPROVAL_PENDING,
    APPROVED,
    BIDDING,
    CONSTRUCTION,
    ON_HOLD,
    COMPLETED,
    CANCELLED
}

@Entity(tableName = "power_grid")
data class PowerGrid(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val region: String,
    val totalCapacity: Double, // MW
    val currentProduction: Double, // MW
    val currentDemand: Double, // MW
    val surplus: Double, // MW
    val gridStability: Double, // 0-100
    val renewablePercentage: Double, // 0-100
    val transmissionLoss: Double, // percentage
    val blackoutRisk: Double // 0-100
)

@Entity(tableName = "energy_sources")
data class EnergySource(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val sourceType: EnergySourceType,
    val facilityName: String,
    val location: String,
    val capacity: Double, // MW
    val currentOutput: Double, // MW
    val efficiency: Double, // 0-100
    val operatingCost: Double,
    val environmentalImpact: Double, // 1-10
    val workerCount: Int,
    val maintenanceSchedule: String?, // JSON
    val isActive: Boolean = true,
    val yearBuilt: Int
)

enum class EnergySourceType {
    COAL,
    NATURAL_GAS,
    OIL,
    NUCLEAR,
    HYDRO,
    SOLAR,
    WIND,
    GEOTHERMAL,
    BIOMASS
}

@Entity(tableName = "transport_routes")
data class TransportRoute(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val routeType: TransportType,
    val routeName: String,
    val startPoint: String,
    val endPoint: String,
    val distance: Double, // km
    val capacity: Int, // vehicles/passengers per day
    val currentUsage: Int,
    val condition: Double, // 0-100
    val maintenanceNeeded: Boolean,
    val tollFee: Double,
    val averageTravelTime: Int, // minutes
    val isActive: Boolean = true
)

enum class TransportType {
    HIGHWAY,
    RAILWAY,
    BUS_ROUTE,
    SUBWAY,
    FERRY,
    FLIGHT_ROUTE,
    SHIPPING_LANE
}

@Entity(tableName = "bus_schedules")
data class BusSchedule(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val routeId: Long,
    val routeName: String,
    val neighborhood: String,
    val scheduleType: ScheduleType,
    val frequency: Int, // minutes between buses
    val firstBus: String, // HH:MM
    val lastBus: String, // HH:MM
    val averageRidership: Int,
    val fare: Double,
    val isActive: Boolean = true
)

enum class ScheduleType {
    WEEKDAY,
    WEEKEND,
    HOLIDAY,
    PEAK,
    OFF_PEAK
}

@Entity(tableName = "traffic_projects")
data class TrafficProject(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val projectType: TrafficProjectType,
    val location: String,
    val neighborhood: String,
    val description: String,
    val budget: Double,
    val status: ProjectStatus,
    val publicSupport: Double, // 0-100
    val businessImpact: Double, // -100 to 100
    val safetyImprovement: Double, // 0-100
    val completionDate: Long?
)

enum class TrafficProjectType {
    SPEED_BUMP,
    ROUNDABOUT,
    BIKE_LANE,
    PEDESTRIAN_CROSSING,
    PARKING_RESTRICTION,
    ONE_WAY,
    BUS_LANE,
    GREEN_LIGHT_PRIORITY
}

@Entity(tableName = "construction_permits")
data class ConstructionPermit(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val permitNumber: String,
    val applicantName: String,
    val projectDescription: String,
    val location: String,
    val coordinates: String?, // JSON
    val buildingType: String,
    val estimatedCost: Double,
    val environmentalReview: Boolean,
    val zoningApproval: Boolean,
    val safetyReview: Boolean,
    val status: PermitStatus,
    val applicationDate: Long,
    val approvalDate: Long?,
    val expiryDate: Long?,
    val conditions: String = "" // JSON array
)

enum class PermitStatus {
    PENDING,
    UNDER_REVIEW,
    APPROVED,
    REJECTED,
    EXPIRED,
    REVOKED
}

@Entity(tableName = "pollution_controls")
data class PollutionControl(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val facilityId: Long,
    val facilityName: String,
    val controlType: PollutionControlType,
    val pollutant: String,
    val currentEmission: Double,
    val targetEmission: Double,
    val technology: String,
    val cost: Double,
    val effectiveness: Double, // 0-100
    val installationDate: Long?,
    val isActive: Boolean = false
)

enum class PollutionControlType {
    FILTER,
    SCRUBBER,
    CATALYTIC_CONVERTER,
    CARBON_CAPTURE,
    WASTE_TREATMENT,
    EMISSION_MONITOR
}
