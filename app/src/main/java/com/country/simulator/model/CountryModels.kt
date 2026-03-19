package com.country.simulator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class Country(
    @PrimaryKey val id: Long = 0,
    val name: String,
    val shortName: String,
    val flag: String,
    val capital: String,
    val population: Long,
    val gdp: Double,
    val governmentType: GovernmentType = GovernmentType.DEMOCRACY,
    val ideology: NationalIdeology = NationalIdeology.NEUTRAL,
    val electionCycle: ElectionCycle = ElectionCycle.EVERY_4_YEARS,
    
    // Resources
    val oilReserves: Double = 0.0,
    val naturalGas: Double = 0.0,
    val minerals: Double = 0.0,
    val agriculturalLand: Double = 0.0,
    
    // Relations
    val diplomaticStanding: Int = 50, // 0-100
    val tradeRelations: Int = 50, // 0-100
    
    // Status
    val isActive: Boolean = true,
    val isPlayerCountry: Boolean = false,
    
    // Metadata
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "player_countries")
data class PlayerCountry(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val leaderName: String,
    val leaderTitle: String = "President",
    val approvalRating: Double = 50.0,
    val corruptionLevel: Double = 0.0,
    val stability: Double = 75.0,
    val currentGameMode: GameMode = GameMode.EXECUTIVE,
    val currentTerm: Int = 1,
    val termsServed: Int = 0,
    val promisesMade: Int = 0,
    val scandalsActive: Int = 0,
    val lastElectionResult: Double = 0.0,
    
    // Economic indicators
    val inflationRate: Double = 2.0,
    val unemploymentRate: Double = 5.0,
    val treasuryBalance: Double = 1000000000.0,
    val growthRate: Double = 2.5,
    
    // Social indicators
    val happinessIndex: Double = 50.0,
    val healthIndex: Double = 50.0,
    val educationIndex: Double = 50.0,
    val freedomIndex: Double = 50.0,
    
    // Environment
    val pollutionLevel: Double = 0.0,
    val environmentalRating: Double = 50.0,
    
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "resources")
data class NationalResources(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    
    // Treasury
    val treasuryBalance: Double = 1000000000.0,
    val nationalDebt: Double = 0.0,
    val budgetDeficit: Double = 0.0,
    
    // Oil & Energy
    val oilProduction: Double = 0.0,
    val oilConsumption: Double = 0.0,
    val oilPrice: Double = 80.0,
    val energyProduction: Double = 0.0,
    val energyConsumption: Double = 0.0,
    
    // Trade
    val exports: Double = 0.0,
    val imports: Double = 0.0,
    val tradeBalance: Double = 0.0,
    
    // Reserves
    val foreignReserves: Double = 0.0,
    val goldReserves: Double = 0.0,
    
    val lastUpdated: Long = System.currentTimeMillis()
)
