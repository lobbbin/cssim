package com.country.simulator.engine

import com.country.simulator.model.*
import com.country.simulator.repositories.GameRepository
import kotlinx.coroutines.flow.first
import kotlin.random.Random

/**
 * Dynamic Economy Simulation
 * Living economy with supply/demand, market forces, inflation, trade flows
 * Economy reacts to player policies and NPC actions
 */
class DynamicEconomySystem(private val repository: GameRepository) {
    
    private val random = Random
    
    // Market sectors with dynamic behavior
    data class MarketSector(
        val sectorType: SectorType,
        val health: Double, // 0-100
        val growthRate: Double, // -10 to 10
        val employment: Int,
        val avgWage: Double,
        val companies: Int,
        val demand: Double, // 0-100
        val supply: Double, // 0-100
        val inventory: Double // days of inventory
    )
    
    enum class SectorType {
        AGRICULTURE, MANUFACTURING, TECHNOLOGY, FINANCE, HEALTHCARE,
        RETAIL, ENERGY, CONSTRUCTION, TRANSPORTATION, HOSPITALITY
    }
    
    // Commodity markets
    data class CommodityMarket(
        val commodityType: CommodityType,
        val pricePerUnit: Double,
        val dailyVolume: Double,
        val inventory: Double,
        val priceHistory: List<Double>, // last 30 days
        val volatility: Double // 0-1
    )
    
    enum class CommodityType {
        OIL, NATURAL_GAS, GOLD, SILVER, COPPER, WHEAT, CORN, SOYBEANS,
        COFFEE, SUGAR, COTTON, LUMBER, IRON_ORE, COAL, URANIUM
    }
    
    // Stock market simulation
    data class StockIndex(
        val indexName: String,
        val value: Double,
        val dailyChange: Double,
        val volume: Long,
        val peRatio: Double,
        val dividendYield: Double
    )
    
    /**
     * Simulate one day of economic activity
     */
    suspend fun simulateDay(countryId: Long) {
        // Update all market sectors
        updateMarketSectors(countryId)
        
        // Update commodity prices
        updateCommodityPrices(countryId)
        
        // Update stock market
        updateStockMarket(countryId)
        
        // Calculate inflation
        calculateInflation(countryId)
        
        // Update employment
        updateEmployment(countryId)
        
        // Process business lifecycle
        processBusinessLifecycle(countryId)
        
        // Generate economic events
        generateEconomicEvents(countryId)
    }
    
    /**
     * Update all market sectors based on supply/demand
     */
    private suspend fun updateMarketSectors(countryId: Long) {
        val playerCountry = repository.getPlayerCountrySync() ?: return
        val resources = repository.getResources(countryId).first() ?: return
        
        val sectors = getAllMarketSectors(countryId)
        
        for (sector in sectors) {
            // Calculate supply/demand imbalance
            val imbalance = sector.demand - sector.supply
            
            // Price pressure based on imbalance
            val pricePressure = imbalance / 100.0
            
            // Update sector health
            val healthChange = when {
                imbalance > 20 -> 2.0 // High demand = healthy
                imbalance < -20 -> -2.0 // Oversupply = unhealthy
                else -> 0.0
            }
            
            // Employment changes
            val employmentChange = when {
                sector.growthRate > 3 -> (sector.employment * 0.01).toInt() // 1% growth
                sector.growthRate < -3 -> -(sector.employment * 0.005).toInt() // 0.5% decline
                else -> 0
            }
            
            // Wage pressure
            val wageChange = when {
                employmentChange > 0 -> sector.avgWage * 0.001 // Wage growth
                employmentChange < 0 -> sector.avgWage * -0.0005 // Wage decline
                else -> 0.0
            }
            
            // Update sector (would save to database)
            val updatedSector = sector.copy(
                health = (sector.health + healthChange).coerceIn(0.0, 100.0),
                employment = sector.employment + employmentChange,
                avgWage = sector.avgWage + wageChange
            )
            
            // Save updated sector
            // Would use SectorDao
        }
        
        // Update national economic indicators
        val gdpGrowth = sectors.map { it.growthRate }.average()
        val avgWageChange = sectors.map { it.avgWage }.average()
        
        val updatedCountry = playerCountry.copy(
            growthRate = gdpGrowth.coerceIn(-5.0, 10.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
    }
    
    /**
     * Update commodity prices with realistic market dynamics
     */
    private suspend fun updateCommodityPrices(countryId: Long) {
        val commodities = getAllCommodityMarkets(countryId)
        
        for (commodity in commodities) {
            // Random daily fluctuation
            val randomFluctuation = (random.nextDouble() - 0.5) * commodity.volatility * 2
            
            // Trend based on inventory levels
            val inventoryPressure = when {
                commodity.inventory < 30 -> 0.02 // Low inventory = price up
                commodity.inventory > 90 -> -0.02 // High inventory = price down
                else -> 0.0
            }
            
            // Mean reversion (prices tend to return to average)
            val avgPrice = commodity.priceHistory.average()
            val meanReversion = (avgPrice - commodity.pricePerUnit) / commodity.pricePerUnit * 0.01
            
            // Calculate new price
            val priceChange = randomFluctuation + inventoryPressure + meanReversion
            val newPrice = commodity.pricePerUnit * (1 + priceChange)
            
            // Update commodity (would save to database)
            val updatedCommodity = commodity.copy(
                pricePerUnit = newPrice.coerceAtLeast(0.01),
                priceHistory = (commodity.priceHistory.drop(1) + newPrice).take(30)
            )
            
            // Save updated commodity
            // Would use CommodityDao
            
            // If oil, update national resources
            if (commodity.commodityType == CommodityType.OIL) {
                val resources = repository.getResources(countryId).first()
                if (resources != null) {
                    repository.updateResources(resources.copy(
                        oilPrice = newPrice,
                        lastUpdated = System.currentTimeMillis()
                    ))
                }
            }
        }
    }
    
    /**
     * Simulate stock market movements
     */
    private suspend fun updateStockMarket(countryId: Long) {
        val playerCountry = repository.getPlayerCountrySync() ?: return
        
        // Market sentiment based on economic conditions
        val economicSentiment = when {
            playerCountry.growthRate > 3 -> 0.6
            playerCountry.growthRate > 2 -> 0.5
            playerCountry.growthRate > 0 -> 0.4
            playerCountry.growthRate > -2 -> 0.3
            else -> 0.2
        }
        
        // Random daily movement
        val randomMove = (random.nextDouble() - 0.5) * 2
        
        // Sentiment-weighted movement
        val marketMove = (economicSentiment * 0.5 + randomMove * 0.5) * 2
        
        // Update market indices (would save to database)
        val sp500 = StockIndex(
            indexName = "S&P 500",
            value = 4000 * (1 + marketMove / 100),
            dailyChange = marketMove,
            volume = (3_000_000_000L * (0.8 + random.nextDouble() * 0.4)).toLong(),
            peRatio = 20.0 + (marketMove * 0.5),
            dividendYield = 1.5 - (marketMove * 0.1)
        )
        
        // Generate market events for extreme moves
        if (Math.abs(marketMove) > 3) {
            val marketEvent = GameEvent(
                countryId = countryId,
                eventType = GameEventType.ECONOMIC_SHIFT,
                title = if (marketMove > 0) "Market Rally" else "Market Selloff",
                description = "Stock market ${if (marketMove > 0) "gains" else "loses"} ${String.format("%.2f", Math.abs(marketMove))}%",
                category = EventCategory.ECONOMIC,
                priority = if (Math.abs(marketMove) > 5) 8 else 5,
                options = "Ignore|Comment|Intervene|Regulate",
                eventDate = System.currentTimeMillis()
            )
            repository.insertGameEvent(marketEvent)
        }
    }
    
    /**
     * Calculate inflation based on economic activity
     */
    private suspend fun calculateInflation(countryId: Long) {
        val playerCountry = repository.getPlayerCountrySync() ?: return
        val resources = repository.getResources(countryId).first() ?: return
        
        // Demand-pull inflation (too much demand)
        val demandPressure = when {
            playerCountry.growthRate > 4 -> 0.5
            playerCountry.growthRate > 3 -> 0.3
            playerCountry.growthRate > 2 -> 0.1
            else -> 0.0
        }
        
        // Cost-push inflation (rising commodity prices)
        val oilPriceChange = (resources.oilPrice - 80) / 80 // vs baseline
        val costPressure = oilPriceChange * 0.2
        
        // Monetary inflation (money supply growth)
        val moneySupplyGrowth = playerCountry.growthRate * 0.1
        val monetaryPressure = moneySupplyGrowth * 0.3
        
        // Calculate new inflation
        val targetInflation = 2.0 // Central bank target
        val currentInflation = playerCountry.inflationRate
        
        val inflationChange = demandPressure + costPressure + monetaryPressure
        val newInflation = currentInflation + (inflationChange - (currentInflation - targetInflation) * 0.1)
        
        val updatedCountry = playerCountry.copy(
            inflationRate = newInflation.coerceIn(0.0, 50.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
        
        // Generate inflation event if high
        if (newInflation > 5) {
            val inflationEvent = GameEvent(
                countryId = countryId,
                eventType = GameEventType.ECONOMIC_SHIFT,
                title = "High Inflation Alert",
                description = "Inflation reaches ${String.format("%.1f", newInflation)}%, eroding purchasing power",
                category = EventCategory.ECONOMIC,
                priority = 7,
                options = "Raise Rates|Price Controls|Do Nothing|Subsidies",
                eventDate = System.currentTimeMillis()
            )
            repository.insertGameEvent(inflationEvent)
        }
    }
    
    /**
     * Update employment based on economic conditions
     */
    private suspend fun updateEmployment(countryId: Long) {
        val playerCountry = repository.getPlayerCountrySync() ?: return
        
        // Job creation based on growth
        val jobGrowth = when {
            playerCountry.growthRate > 4 -> 0.5
            playerCountry.growthRate > 3 -> 0.3
            playerCountry.growthRate > 2 -> 0.1
            playerCountry.growthRate > 0 -> 0.0
            playerCountry.growthRate > -2 -> -0.1
            else -> -0.3
        }
        
        // Update unemployment (inverse relationship with growth)
        val unemploymentChange = -jobGrowth * 0.5
        val newUnemployment = playerCountry.unemploymentRate + unemploymentChange
        
        val updatedCountry = playerCountry.copy(
            unemploymentRate = newUnemployment.coerceIn(0.0, 50.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
        
        // Generate employment event if significant change
        if (Math.abs(unemploymentChange) > 0.3) {
            val employmentEvent = GameEvent(
                countryId = countryId,
                eventType = GameEventType.SOCIAL_CHANGE,
                title = if (unemploymentChange < 0) "Job Growth" else "Rising Unemployment",
                description = "Unemployment ${if (unemploymentChange < 0) "falls to" else "rises to"} ${String.format("%.1f", newUnemployment)}%",
                category = EventCategory.SOCIAL,
                priority = 6,
                options = "Job Programs|Tax Incentives|Training|Do Nothing",
                eventDate = System.currentTimeMillis()
            )
            repository.insertGameEvent(employmentEvent)
        }
    }
    
    /**
     * Process business births and deaths
     */
    private suspend fun processBusinessLifecycle(countryId: Long) {
        val businesses = repository.getBusinessesPaged(countryId).first()
            ?: return
        
        var newBusinesses = 0
        var failedBusinesses = 0
        
        for (business in businesses) {
            // Business failure chance based on economic conditions
            val failureChance = when {
                business.complianceRating < 50 -> 0.05 // Non-compliant businesses fail more
                business.revenue < 100000 -> 0.02 // Small businesses risky
                else -> 0.005 // Base failure rate
            }
            
            if (random.nextDouble() < failureChance) {
                // Business fails
                repository.updateBusiness(business.copy(
                    licenseStatus = LicenseStatus.REVOKED
                ))
                failedBusinesses++
            }
            
            // Business growth chance
            val growthChance = when {
                business.revenue > 1_000_000_000 -> 0.01 // Large businesses grow slowly
                business.revenue > 100_000_000 -> 0.03
                business.revenue > 10_000_000 -> 0.05
                else -> 0.08 // Small businesses can grow fast
            }
            
            if (random.nextDouble() < growthChance) {
                // Business grows
                repository.updateBusiness(business.copy(
                    revenue = business.revenue * 1.1,
                    employees = (business.employees * 1.05).toInt()
                ))
            }
        }
        
        // New business formation (based on economic conditions)
        val playerCountry = repository.getPlayerCountrySync()
        if (playerCountry != null && playerCountry.growthRate > 2) {
            newBusinesses = (random.nextDouble() * 10 * playerCountry.growthRate).toInt()
            
            for (i in 1..newBusinesses) {
                // Would create new business entities
                // generateNewBusiness(countryId)
            }
        }
    }
    
    /**
     * Generate economic events based on conditions
     */
    private suspend fun generateEconomicEvents(countryId: Long) {
        val playerCountry = repository.getPlayerCountrySync() ?: return
        
        // Random economic events
        val eventRoll = random.nextDouble()
        
        when {
            // Banking crisis (rare but severe)
            eventRoll < 0.001 -> {
                val bankingCrisis = GameEvent(
                    countryId = countryId,
                    eventType = GameEventType.CRISIS,
                    title = "Banking Crisis",
                    description = "Major bank faces insolvency amid loan defaults",
                    category = EventCategory.ECONOMIC,
                    priority = 10,
                    options = "Bailout|Let Fail|Nationalize|Capital Controls",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(bankingCrisis)
            }
            
            // Currency crash
            eventRoll < 0.003 -> {
                val currencyCrisis = GameEvent(
                    countryId = countryId,
                    eventType = GameEventType.CRISIS,
                    title = "Currency Crash",
                    description = "National currency loses significant value",
                    category = EventCategory.ECONOMIC,
                    priority = 9,
                    options = "Raise Rates|Buy Back|IMF Help|Forex Controls",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(currencyCrisis)
            }
            
            // Trade boom
            eventRoll < 0.01 && playerCountry.growthRate > 3 -> {
                val tradeBoom = GameEvent(
                    countryId = countryId,
                    eventType = GameEventType.OPPORTUNITY,
                    title = "Export Boom",
                    description = "Unexpected surge in export demand",
                    category = EventCategory.ECONOMIC,
                    priority = 6,
                    options = "Invest in Capacity|Tax Windfall|Save Reserves|Cut Taxes",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(tradeBoom)
            }
            
            // Tech startup success
            eventRoll < 0.02 -> {
                val startupSuccess = GameEvent(
                    countryId = countryId,
                    eventType = GameEventType.BREAKTHROUGH,
                    title = "Tech Unicorn Created",
                    description = "Local startup reaches billion-dollar valuation",
                    category = EventCategory.ECONOMIC,
                    priority = 5,
                    options = "Tax Incentives|State Stake|Support Expansion|No Intervention",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(startupSuccess)
            }
        }
    }
    
    /**
     * Get all market sectors for a country
     */
    private suspend fun getAllMarketSectors(countryId: Long): List<MarketSector> {
        // Would fetch from database
        // For now, return simulated sectors
        return SectorType.entries.map { sectorType ->
            MarketSector(
                sectorType = sectorType,
                health = 50.0 + random.nextDouble() * 20,
                growthRate = random.nextDouble(-3.0, 5.0),
                employment = random.nextInt(10000, 500000),
                avgWage = random.nextDouble(30000.0, 100000.0),
                companies = random.nextInt(100, 10000),
                demand = 50.0 + random.nextDouble() * 20,
                supply = 50.0 + random.nextDouble() * 20,
                inventory = random.nextDouble(30.0, 90.0)
            )
        }
    }
    
    /**
     * Get all commodity markets
     */
    private suspend fun getAllCommodityMarkets(countryId: Long): List<CommodityMarket> {
        // Would fetch from database
        return CommodityType.entries.map { type ->
            CommodityMarket(
                commodityType = type,
                pricePerUnit = when (type) {
                    CommodityType.OIL -> 80.0
                    CommodityType.GOLD -> 2000.0
                    CommodityType.NATURAL_GAS -> 3.0
                    CommodityType.COPPER -> 4.0
                    CommodityType.WHEAT -> 6.0
                    else -> random.nextDouble(1.0, 100.0)
                },
                dailyVolume = random.nextDouble(1000000.0, 100000000.0),
                inventory = random.nextDouble(30.0, 90.0),
                priceHistory = List(30) { random.nextDouble(50.0, 100.0) },
                volatility = random.nextDouble(0.01, 0.05)
            )
        }
    }
    
    /**
     * Apply player economic policy
     */
    suspend fun applyEconomicPolicy(
        countryId: Long,
        policyType: EconomicPolicyType,
        intensity: Double // 0-1
    ): PolicyResult {
        val playerCountry = repository.getPlayerCountrySync() ?: return PolicyResult.FAILURE
        
        return when (policyType) {
            EconomicPolicyType.STIMULUS -> {
                // Stimulus boosts growth but increases inflation
                val growthBoost = intensity * 2.0
                val inflationIncrease = intensity * 1.5
                
                val updatedCountry = playerCountry.copy(
                    growthRate = (playerCountry.growthRate + growthBoost).coerceIn(-5.0, 10.0),
                    inflationRate = (playerCountry.inflationRate + inflationIncrease).coerceIn(0.0, 50.0),
                    lastUpdated = System.currentTimeMillis()
                )
                repository.updatePlayerCountry(updatedCountry)
                
                PolicyResult.SUCCESS(
                    effects = mapOf(
                        "growth" to growthBoost,
                        "inflation" to inflationIncrease
                    ),
                    message = "Stimulus package implemented"
                )
            }
            
            EconomicPolicyType.AUSTERITY -> {
                // Austerity reduces inflation but hurts growth
                val growthHit = -intensity * 1.5
                val inflationReduction = -intensity * 1.0
                
                val updatedCountry = playerCountry.copy(
                    growthRate = (playerCountry.growthRate + growthHit).coerceIn(-5.0, 10.0),
                    inflationRate = (playerCountry.inflationRate + inflationReduction).coerceIn(0.0, 50.0),
                    lastUpdated = System.currentTimeMillis()
                )
                repository.updatePlayerCountry(updatedCountry)
                
                PolicyResult.SUCCESS(
                    effects = mapOf(
                        "growth" to growthHit,
                        "inflation" to inflationReduction
                    ),
                    message = "Austerity measures implemented"
                )
            }
            
            EconomicPolicyType.DEREGULATION -> {
                // Deregulation boosts growth long-term, risk short-term
                val growthBoost = intensity * 1.0
                val stabilityHit = -intensity * 5.0
                
                val updatedCountry = playerCountry.copy(
                    growthRate = (playerCountry.growthRate + growthBoost).coerceIn(-5.0, 10.0),
                    stability = (playerCountry.stability + stabilityHit).coerceIn(0.0, 100.0),
                    lastUpdated = System.currentTimeMillis()
                )
                repository.updatePlayerCountry(updatedCountry)
                
                PolicyResult.SUCCESS(
                    effects = mapOf(
                        "growth" to growthBoost,
                        "stability" to stabilityHit
                    ),
                    message = "Deregulation implemented"
                )
            }
            
            EconomicPolicyType.TAX_CUTS -> {
                // Tax cuts boost growth but increase deficit
                val growthBoost = intensity * 1.5
                val deficitIncrease = intensity * 10_000_000_000.0
                
                val updatedCountry = playerCountry.copy(
                    growthRate = (playerCountry.growthRate + growthBoost).coerceIn(-5.0, 10.0),
                    lastUpdated = System.currentTimeMillis()
                )
                repository.updatePlayerCountry(updatedCountry)
                
                PolicyResult.SUCCESS(
                    effects = mapOf(
                        "growth" to growthBoost,
                        "deficit" to deficitIncrease
                    ),
                    message = "Tax cuts implemented"
                )
            }
        }
    }
}

enum class EconomicPolicyType {
    STIMULUS, AUSTERITY, DEREGULATION, TAX_CUTS
}

sealed class PolicyResult {
    object FAILURE : PolicyResult()
    data class SUCCESS(val effects: Map<String, Any>, val message: String) : PolicyResult()
}
