package com.country.simulator.generation

import com.country.simulator.model.*
import com.country.simulator.repositories.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Game World Initializer - Creates a complete randomized game world
 * Call this when starting a new game
 */
class GameWorldInitializer(private val repository: GameRepository) {
    
    private val populator = DatabasePopulator(repository)
    
    /**
     * Initialize a complete new game world
     */
    suspend fun initializeWorld(
        playerCountryName: String? = null,
        seed: Long = System.currentTimeMillis()
    ): WorldInitializationResult {
        return withContext(Dispatchers.IO) {
            try {
                // Set random seed for reproducibility
                ProceduralGenerator.setSeed(seed)
                
                // 1. Create player country
                val country = createPlayerCountry(playerCountryName)
                val countryId = repository.insertCountry(country)
                
                // 2. Create player country details
                val playerCountry = createPlayerCountryDetails(countryId, country)
                repository.insertPlayerCountry(playerCountry)
                
                // 3. Initialize resources
                val resources = createNationalResources(countryId, country)
                repository.insertResources(resources)
                
                // 4. Generate procedural content
                populator.populateAllData(countryId, seed)
                
                // 5. Create foreign countries
                createForeignCountries(countryId)
                
                // 6. Create initial budget
                createInitialBudget(countryId)
                
                // 7. Create tax brackets
                createTaxBrackets(countryId)
                
                // 8. Create initial demographic data
                createDemographics(countryId)
                
                // 9. Create sports teams and events
                createSportsData(countryId)
                
                // 10. Generate initial notifications
                createInitialNotifications(countryId)
                
                WorldInitializationResult.Success(countryId)
            } catch (e: Exception) {
                WorldInitializationResult.Failure(e.message ?: "Unknown error")
            }
        }
    }
    
    /**
     * Create player country with procedural generation
     */
    private fun createPlayerCountry(nameOverride: String?): Country {
        val country = ProceduralGenerator.generateCountry(isPlayer = true)
        return if (nameOverride != null) {
            country.copy(
                name = nameOverride,
                shortName = nameOverride.take(3).uppercase()
            )
        } else {
            country
        }
    }
    
    /**
     * Create player country details
     */
    private fun createPlayerCountryDetails(countryId: Long, country: Country): PlayerCountry {
        val leaderNames = listOf(
            "Alexander Blackwood", "Victoria Ashford", "Marcus Sterling",
            "Elizabeth Montgomery", "William Kensington", "Catherine Worthington",
            "Jonathan Harrington", "Margaret Pembroke", "Christopher Carlisle", "Anne Beaumont"
        )
        
        val leaderTitle = when (country.governmentType) {
            GovernmentType.MONARCHY -> listOf("King", "Queen").random()
            GovernmentType.TECHNOCRACY -> "Chief Administrator"
            GovernmentType.AUTOCRACY -> "Supreme Leader"
            else -> "President"
        }
        
        return PlayerCountry(
            countryId = countryId,
            leaderName = leaderNames.random(),
            leaderTitle = leaderTitle,
            approvalRating = 50.0,
            stability = 75.0,
            currentGameMode = GameMode.EXECUTIVE,
            currentTerm = 1,
            termsServed = 0,
            inflationRate = (1.5..4.0).random(),
            unemploymentRate = (3.0..8.0).random(),
            growthRate = (1.5..4.0).random(),
            happinessIndex = (45.0..65.0).random(),
            healthIndex = (50.0..80.0).random(),
            educationIndex = (50.0..85.0).random(),
            freedomIndex = (40.0..70.0).random(),
            pollutionLevel = (10.0..50.0).random(),
            environmentalRating = (40.0..70.0).random()
        )
    }
    
    /**
     * Create national resources
     */
    private fun createNationalResources(countryId: Long, country: Country): NationalResources {
        val oilProduction = country.oilReserves * 0.0001
        return NationalResources(
            countryId = countryId,
            treasuryBalance = (500_000_000..5_000_000_000).random().toDouble(),
            nationalDebt = (0..2_000_000_000).random().toDouble(),
            oilProduction = oilProduction,
            oilConsumption = oilProduction * (0.5..1.5).random(),
            oilPrice = (60.0..120.0).random(),
            energyProduction = (5000.0..50000.0).random(),
            energyConsumption = (4000.0..45000.0).random(),
            exports = (50_000_000..500_000_000).random().toDouble(),
            imports = (40_000_000..450_000_000).random().toDouble(),
            tradeBalance = 0.0,
            foreignReserves = (100_000_000..1_000_000_000).random().toDouble(),
            goldReserves = (10.0..500.0).random()
        )
    }
    
    /**
     * Create foreign countries for diplomacy
     */
    private suspend fun createForeignCountries(playerCountryId: Long) {
        val countryTemplates = listOf(
            ForeignCountryTemplate("United Federal States", "UFS", "Democracy", "Friendly"),
            ForeignCountryTemplate("People's Republic of Chenova", "PRC", "Autocracy", "Neutral"),
            ForeignCountryTemplate("Kingdom of Nordland", "KON", "Monarchy", "Friendly"),
            ForeignCountryTemplate("Islamic Republic of Pars", "IRP", "Theocracy", "Tense"),
            ForeignCountryTemplate("Union of Soviet Territories", "UST", "Communist", "Hostile"),
            ForeignCountryTemplate("Commonwealth of Westria", "COW", "Democracy", "Friendly"),
            ForeignCountryTemplate("Empire of Solaria", "EOS", "Monarchy", "Neutral"),
            ForeignCountryTemplate("Democratic Republic of Kongo", "DRK", "Democracy", "Neutral"),
            ForeignCountryTemplate("Federal Republic of Germania", "FRG", "Democracy", "Friendly"),
            ForeignCountryTemplate("Republic of Italia", "ROI", "Democracy", "Friendly"),
            ForeignCountryTemplate("United Kingdom of Britannia", "UKB", "Monarchy", "Friendly"),
            ForeignCountryTemplate("Japanese Federation", "JPF", "Democracy", "Neutral"),
            ForeignCountryTemplate("Republic of Brasilia", "ROB", "Democracy", "Friendly"),
            ForeignCountryTemplate("United Mexican States", "UMS", "Democracy", "Neutral"),
            ForeignCountryTemplate("Arab Emirates Union", "AEU", "Monarchy", "Neutral")
        )
        
        countryTemplates.forEach { template ->
            val country = Country(
                name = template.name,
                shortName = template.shortName,
                flag = generateFlagEmoji(template.shortName),
                capital = "${template.name.split(" ").last()} City",
                population = (1_000_000..200_000_000).random().toLong(),
                gdp = (100_000_000_000..20_000_000_000_000).random().toDouble(),
                governmentType = when (template.governmentType) {
                    "Democracy" -> GovernmentType.DEMOCRACY
                    "Monarchy" -> GovernmentType.MONARCHY
                    "Communist" -> GovernmentType.AUTOCRACY
                    "Theocracy" -> GovernmentType.AUTOCRACY
                    else -> GovernmentType.REPUBLIC
                },
                ideology = when (template.relation) {
                    "Friendly" -> NationalIdeology.FRIENDLY
                    "Hostile" -> NationalIdeology.AGGRESSIVE
                    "Tense" -> NationalIdeology.ISOLATIONIST
                    else -> NationalIdeology.NEUTRAL
                },
                oilReserves = (0.0..50_000_000_000).random(),
                diplomaticStanding = when (template.relation) {
                    "Friendly" -> (60..100).random()
                    "Neutral" -> (30..60).random()
                    "Tense" -> (0..30).random()
                    "Hostile" -> (-50..0).random()
                    else -> 50
                },
                isActive = true,
                isPlayerCountry = false
            )
            
            val foreignCountryId = repository.insertCountry(country)
            
            // Create diplomatic relation
            val relation = DiplomaticRelation(
                countryId = playerCountryId,
                foreignCountryId = foreignCountryId,
                foreignCountryName = country.name,
                relationLevel = country.diplomaticStanding,
                trustLevel = ((country.diplomaticStanding + 100) / 2).coerceIn(0, 100),
                tradeVolume = (1_000_000..50_000_000_000).random().toDouble(),
                ambassadorName = if (country.diplomaticStanding > 30) ProceduralGenerator.generateNPCName() else null,
                embassyEstablished = country.diplomaticStanding > 20,
                lastContact = System.currentTimeMillis() - (0..30).random() * 24 * 60 * 60 * 1000,
                issues = if (country.diplomaticStanding < 30) """["Trade dispute", "Border tension"]""" else "",
                agreements = if (country.diplomaticStanding > 60) """["Trade agreement"]""" else ""
            )
            repository.insertDiplomaticRelation(relation)
        }
    }
    
    /**
     * Create initial national budget
     */
    private suspend fun createInitialBudget(countryId: Long) {
        val totalRevenue = (500_000_000..5_000_000_000).random().toDouble()
        
        val budget = NationalBudget(
            countryId = countryId,
            fiscalYear = 2024,
            totalRevenue = totalRevenue,
            totalExpenses = totalRevenue * (0.9..1.1).random(),
            deficit = 0.0,
            surplus = 0.0,
            incomeTaxRevenue = totalRevenue * 0.35,
            corporateTaxRevenue = totalRevenue * 0.20,
            salesTaxRevenue = totalRevenue * 0.15,
            tariffRevenue = totalRevenue * 0.05,
            resourceRevenue = totalRevenue * 0.15,
            otherRevenue = totalRevenue * 0.10,
            defenseSpending = totalRevenue * 0.15,
            healthcareSpending = totalRevenue * 0.18,
            educationSpending = totalRevenue * 0.12,
            infrastructureSpending = totalRevenue * 0.10,
            socialWelfareSpending = totalRevenue * 0.20,
            debtServicing = totalRevenue * 0.08,
            otherSpending = totalRevenue * 0.17,
            isApproved = true,
            approvedDate = System.currentTimeMillis()
        )
        
        repository.insertBudget(budget)
    }
    
    /**
     * Create tax brackets
     */
    private suspend fun createTaxBrackets(countryId: Long) {
        // Income tax brackets
        val incomeBrackets = listOf(
            TaxBracketData("Basic Rate", 0.0, 20000.0, 10.0),
            TaxBracketData("Lower Middle", 20000.0, 50000.0, 15.0),
            TaxBracketData("Middle", 50000.0, 100000.0, 22.0),
            TaxBracketData("Upper Middle", 100000.0, 200000.0, 28.0),
            TaxBracketData("High Income", 200000.0, 500000.0, 35.0),
            TaxBracketData("Wealthy", 500000.0, null, 40.0)
        )
        
        incomeBrackets.forEach { data ->
            repository.insertTaxBracket(
                TaxBracket(
                    countryId = countryId,
                    taxType = TaxType.INCOME,
                    bracketName = data.name,
                    minIncome = data.min,
                    maxIncome = data.max,
                    taxRate = data.rate
                )
            )
        }
        
        // Corporate tax
        repository.insertTaxBracket(
            TaxBracket(
                countryId = countryId,
                taxType = TaxType.CORPORATE,
                bracketName = "Corporate Rate",
                minIncome = 0.0,
                maxIncome = null,
                taxRate = 21.0
            )
        )
        
        // Sales tax
        repository.insertTaxBracket(
            TaxBracket(
                countryId = countryId,
                taxType = TaxType.SALES,
                bracketName = "Standard VAT",
                minIncome = 0.0,
                maxIncome = null,
                taxRate = 10.0
            )
        )
        
        // Luxury tax
        repository.insertTaxBracket(
            TaxBracket(
                countryId = countryId,
                taxType = TaxType.LUXURY,
                bracketName = "Luxury Goods",
                minIncome = 10000.0,
                maxIncome = null,
                taxRate = 25.0
            )
        )
    }
    
    /**
     * Create demographic data
     */
    private suspend fun createDemographics(countryId: Long) {
        val regions = listOf(
            "Capital Region", "Northern Province", "Southern Province",
            "Eastern Province", "Western Province", "Central Province"
        )
        
        regions.forEach { region ->
            val population = (500_000..5_000_000).random().toLong()
            val demographic = Demographic(
                countryId = countryId,
                region = region,
                totalPopulation = population,
                malePopulation = population * (48..52).random() / 100,
                femalePopulation = population * (48..52).random() / 100,
                ageGroup0_14 = population * (15..25).random() / 100,
                ageGroup15_64 = population * (60..70).random() / 100,
                ageGroup65plus = population * (10..20).random() / 100,
                birthRate = (10.0..20.0).random(),
                deathRate = (5.0..12.0).random(),
                migrationRate = (-5.0..5.0).random(),
                lifeExpectancy = (70.0..85.0).random(),
                fertilityRate = (1.5..3.0).random(),
                urbanPopulation = population * (40..80).random() / 100,
                ruralPopulation = population * (20..60).random() / 100,
                populationDensity = (50.0..500.0).random()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Create sports data
     */
    private suspend fun createSportsData(countryId: Long) {
        // Create national teams for major sports
        val sports = listOf(
            SportType.FOOTBALL to "National Football Team",
            SportType.BASKETBALL to "National Basketball Team",
            SportType.TENNIS to "National Tennis Team",
            SportType.SWIMMING to "National Swimming Team",
            SportType.ATHLETICS to "National Athletics Team"
        )
        
        sports.forEach { (sport, name) ->
            val team = SportsTeam(
                countryId = countryId,
                sportType = sport,
                teamName = name,
                coachName = ProceduralGenerator.generateNPCName(),
                ranking = (1..50).random(),
                wins = (0..30).random(),
                losses = (0..20).random(),
                draws = (0..15).random(),
                budget = (1_000_000..50_000_000).random().toDouble(),
                homeStadium = "National Stadium",
                fanBase = (100_000..5_000_000).random(),
                morale = (50.0..90.0).random()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Create initial notifications
     */
    private suspend fun createInitialNotifications(countryId: Long) {
        val notifications = listOf(
            NotificationData("Welcome to Office", "You have been sworn in as leader. Check your tasks to begin.", NotificationType.INFO),
            NotificationData("Cabinet Meeting", "Your first cabinet meeting is scheduled for tomorrow.", NotificationType.TASK),
            NotificationData("Budget Review", "The national budget requires your review.", NotificationType.TASK),
            NotificationData("Diplomatic Cables", "Several foreign leaders have sent congratulations.", NotificationType.INFO)
        )
        
        notifications.forEach { data ->
            repository.insertNotification(
                Notification(
                    countryId = countryId,
                    notificationType = data.type,
                    title = data.title,
                    message = data.message,
                    relatedEntityId = null,
                    relatedEntityType = null,
                    priority = Priority.NORMAL,
                    createdDate = System.currentTimeMillis(),
                    readDate = null,
                    actionRequired = data.type == NotificationType.TASK
                )
            )
        }
    }
    
    /**
     * Generate flag emoji from country code
     */
    private fun generateFlagEmoji(code: String): String {
        return code.uppercase().map { 
            "\uD83C\uDD${it.code - 0x41}" 
        }.joinToString("")
    }
    
    // Helper data classes
    data class ForeignCountryTemplate(
        val name: String,
        val shortName: String,
        val governmentType: String,
        val relation: String
    )
    
    data class TaxBracketData(
        val name: String,
        val min: Double,
        val max: Double?,
        val rate: Double
    )
    
    data class NotificationData(
        val title: String,
        val message: String,
        val type: NotificationType
    )
}

sealed class WorldInitializationResult {
    data class Success(val countryId: Long) : WorldInitializationResult()
    data class Failure(val error: String) : WorldInitializationResult()
}
