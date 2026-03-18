package com.country.simulator.engine

import com.country.simulator.model.*
import com.country.simulator.repositories.GameRepository
import kotlinx.coroutines.flow.first
import kotlin.random.Random

/**
 * Dynamic World System
 * AI-controlled countries with their own goals, relationships, and behaviors
 * World events that cascade across borders
 */
class DynamicWorldSystem(private val repository: GameRepository) {
    
    private val random = Random
    
    // Country AI personality
    data class CountryAI(
        val countryId: Long,
        val aggression: Double, // 0-1 (peaceful vs aggressive)
        val cooperation: Double, // 0-1 (isolationist vs cooperative)
        val reliability: Double, // 0-1 (unreliable vs trustworthy)
        val ambition: Double, // 0-1 (status quo vs expansionist)
        val ideology: String,
        val leaderPersonality: LeaderType
    )
    
    enum class LeaderType {
        DIPLOMAT, WARMONGER, ECONOMIST, IDEOLOGUE, OPPORTUNIST, REFORMER
    }
    
    // Country goals
    data class CountryGoal(
        val goalType: GoalType,
        val priority: Int, // 1-10
        val targetCountryId: Long?,
        val deadline: Long?,
        val progress: Double // 0-1
    )
    
    enum class GoalType {
        TERRITORIAL_EXPANSION, ECONOMIC_DOMINANCE, REGIONAL_HEGEMONY,
        IDEOLOGICAL_SPREAD, RESOURCE_SECURITY, ALLIANCE_BUILDING,
        MILITARY_MODERNIZATION, SOFT_POWER, TECHNOLOGY_LEADERSHIP
    }
    
    /**
     * Simulate one day of world activity
     */
    suspend fun simulateDay(playerCountryId: Long) {
        val allCountries = repository.getAllCountries().first()
        
        for (country in allCountries) {
            if (country.id == playerCountryId || !country.isActive) continue
            
            // Process this AI country's actions
            processCountryAI(country, playerCountryId)
        }
        
        // Process world events
        processWorldEvents(playerCountryId)
        
        // Check for war spread
        checkWarSpread(playerCountryId)
        
        // Update alliance dynamics
        updateAllianceDynamics(playerCountryId)
    }
    
    /**
     * Process AI country decision-making
     */
    private suspend fun processCountryAI(country: Country, playerCountryId: Long) {
        val ai = getCountryAI(country)
        val relations = repository.getRelations(playerCountryId).first()
        val playerRelation = relations.find { it.foreignCountryId == country.id }
        
        // AI country makes decisions based on personality and goals
        
        // 10% chance of major action per day
        if (random.nextDouble() < 0.10) {
            val action = generateCountryAction(ai, country, playerRelation)
            executeCountryAction(action, country, playerCountryId)
        }
        
        // Daily relation adjustments
        adjustDailyRelations(country, playerCountryId, ai)
    }
    
    /**
     * Generate action based on country AI personality
     */
    private suspend fun generateCountryAction(
        ai: CountryAI,
        country: Country,
        playerRelation: DiplomaticRelation?
    ): CountryAction {
        
        return when {
            // High aggression + low relation = hostile action
            ai.aggression > 0.7 && (playerRelation?.relationLevel ?: 50) < 30 -> {
                val actions = listOf(
                    CountryAction.MILITARY_THREAT,
                    CountryAction.ECONOMIC_SANCTIONS,
                    CountryAction.CYBER_ATTACK,
                    CountryAction.SUPPORT_REBELS
                )
                actions.random()
            }
            
            // High cooperation + good relation = friendly action
            ai.cooperation > 0.6 && (playerRelation?.relationLevel ?: 50) > 60 -> {
                val actions = listOf(
                    CountryAction.TRADE_OFFER,
                    CountryAction.MILITARY_COOPERATION,
                    CountryAction.TECHNOLOGY_SHARING,
                    CountryAction.DIPLOMATIC_SUPPORT
                )
                actions.random()
            }
            
            // High ambition = expansionist action
            ai.ambition > 0.7 -> {
                val actions = listOf(
                    CountryAction.TERRITORIAL_CLAIM,
                    CountryAction.RESOURCE_GRAB,
                    CountryAction.SPHERE_OF_INFLUENCE,
                    CountryAction.ARMS_RACE
                )
                actions.random()
            }
            
            // Economist leader = trade focus
            ai.leaderPersonality == LeaderType.ECONOMIST -> {
                val actions = listOf(
                    CountryAction.TRADE_DEAL,
                    CountryAction.INVESTMENT_OFFER,
                    CountryAction.ECONOMIC_AID,
                    CountryAction.MARKET_OPENING
                )
                actions.random()
            }
            
            // Warmonger = military focus
            ai.leaderPersonality == LeaderType.WARMONGER -> {
                val actions = listOf(
                    CountryAction.MILITARY_BUILDUP,
                    CountryAction.WAR_GAMES,
                    CountryAction.ARMS_DEAL,
                    CountryAction.BASE_ACCESS
                )
                actions.random()
            }
            
            // Default: diplomatic action
            else -> {
                val actions = listOf(
                    CountryAction.DIPLOMATIC_VISIT,
                    CountryAction.CULTURAL_EXCHANGE,
                    CountryAction.DEMARCHE,
                    CountryAction.NOTE_VERBALE
                )
                actions.random()
            }
        }
    }
    
    /**
     * Execute AI country action with consequences
     */
    private suspend fun executeCountryAction(
        action: CountryAction,
        country: Country,
        playerCountryId: Long
    ) {
        when (action) {
            CountryAction.MILITARY_THREAT -> {
                // Generate crisis event
                val threatEvent = GameEvent(
                    countryId = playerCountryId,
                    eventType = GameEventType.DIPLOMATIC_INCIDENT,
                    title = "Military Threat from ${country.name}",
                    description = "${country.name} conducts military exercises near border",
                    category = EventCategory.DIPLOMATIC,
                    priority = 8,
                    options = "Mobilize|Protest|Negotiate|Ignore|Seek Allies",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(threatEvent)
                
                // Update relations
                updateRelation(playerCountryId, country.id, -15)
            }
            
            CountryAction.ECONOMIC_SANCTIONS -> {
                // Create sanction
                val sanction = Sanction(
                    countryId = playerCountryId,
                    targetCountryId = country.id,
                    targetCountryName = country.name,
                    sanctionType = SanctionType.TRADE_EMBARGO,
                    severity = random.nextInt(5, 10),
                    startDate = System.currentTimeMillis(),
                    description = "${country.name} imposes trade sanctions",
                    economicImpact = -5.0,
                    domesticBacklash = 30.0,
                    internationalSupport = 40.0,
                    isActive = true
                )
                repository.insertSanction(sanction)
                
                // Generate economic event
                val sanctionEvent = GameEvent(
                    countryId = playerCountryId,
                    eventType = GameEventType.ECONOMIC_SHIFT,
                    title = "Trade Sanctions Imposed",
                    description = "${country.name} sanctions affect ${sanction.economicImpact}% of trade",
                    category = EventCategory.ECONOMIC,
                    priority = 7,
                    options = "Retaliate|Negotiate|Find Alternatives|Concede",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(sanctionEvent)
                
                updateRelation(playerCountryId, country.id, -25)
            }
            
            CountryAction.CYBER_ATTACK -> {
                // Generate security crisis
                val cyberEvent = GameEvent(
                    countryId = playerCountryId,
                    eventType = GameEventType.CRISIS,
                    title = "Cyber Attack Detected",
                    description = "State-sponsored hackers from ${country.name} target infrastructure",
                    category = EventCategory.DIPLOMATIC,
                    priority = 9,
                    options = "Counter-Attack|Expose|Sanctions|Negotiate|Ignore",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(cyberEvent)
                
                updateRelation(playerCountryId, country.id, -30)
            }
            
            CountryAction.TRADE_OFFER -> {
                // Generate trade deal opportunity
                val tradeEvent = GameEvent(
                    countryId = playerCountryId,
                    eventType = GameEventType.OPPORTUNITY,
                    title = "Trade Deal Offered",
                    description = "${country.name} proposes new trade agreement",
                    category = EventCategory.ECONOMIC,
                    priority = 6,
                    options = "Accept|Negotiate|Reject|Counter-Offer",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(tradeEvent)
                
                updateRelation(playerCountryId, country.id, 10)
            }
            
            CountryAction.MILITARY_COOPERATION -> {
                // Generate alliance opportunity
                val allianceEvent = GameEvent(
                    countryId = playerCountryId,
                    eventType = GameEventType.DIPLOMATIC_INCIDENT,
                    title = "Military Cooperation Proposed",
                    description = "${country.name} offers joint exercises and defense cooperation",
                    category = EventCategory.DIPLOMATIC,
                    priority = 6,
                    options = "Accept|Decline|Negotiate Terms|Observe First",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(allianceEvent)
                
                updateRelation(playerCountryId, country.id, 15)
            }
            
            CountryAction.TECHNOLOGY_SHARING -> {
                // Generate technology event
                val techEvent = GameEvent(
                    countryId = playerCountryId,
                    eventType = GameEventType.BREAKTHROUGH,
                    title = "Technology Sharing Offered",
                    description = "${country.name} offers access to advanced technology",
                    category = EventCategory.ECONOMIC,
                    priority = 5,
                    options = "Accept|Reciprocate|Decline|Negotiate",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(techEvent)
                
                updateRelation(playerCountryId, country.id, 10)
            }
            
            CountryAction.TERRITORIAL_CLAIM -> {
                // Generate territorial crisis
                val territorialEvent = GameEvent(
                    countryId = playerCountryId,
                    eventType = GameEventType.CRISIS,
                    title = "Territorial Claim Dispute",
                    description = "${country.name} claims disputed territory",
                    category = EventCategory.DIPLOMATIC,
                    priority = 9,
                    options = "Reject|Negotiate|Mobilize|International Court|Concede",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(territorialEvent)
                
                updateRelation(playerCountryId, country.id, -40)
            }
            
            CountryAction.DIPLOMATIC_VISIT -> {
                // Small relation boost
                updateRelation(playerCountryId, country.id, 5)
            }
            
            else -> {
                // Other actions have smaller effects
                updateRelation(playerCountryId, country.id, random.nextInt(-5, 5))
            }
        }
    }
    
    /**
     * Adjust relations daily based on AI personality
     */
    private suspend fun adjustDailyRelations(
        country: Country,
        playerCountryId: Long,
        ai: CountryAI
    ) {
        val relations = repository.getRelations(playerCountryId).first()
        val playerRelation = relations.find { it.foreignCountryId == country.id }
            ?: return
        
        // Natural relation drift based on personality
        val drift = when {
            ai.cooperation > 0.7 -> 0.5 // Cooperative countries drift positive
            ai.aggression > 0.7 -> -0.3 // Aggressive countries drift negative
            else -> 0.0
        }
        
        // Ideology compatibility
        val ideologyBonus = when {
            ai.ideology == "Democracy" && country.governmentType == GovernmentType.DEMOCRACY -> 0.2
            ai.ideology == "Autocracy" && country.governmentType == GovernmentType.AUTOCRACY -> 0.2
            ai.ideology != getCountryIdeology(playerCountryId) -> -0.1
            else -> 0.0
        }
        
        // Update relation
        val newRelationLevel = (playerRelation.relationLevel + drift + ideologyBonus).coerceIn(-100, 100)
        
        repository.updateDiplomaticRelation(playerRelation.copy(
            relationLevel = newRelationLevel.toInt(),
            lastContact = System.currentTimeMillis()
        ))
    }
    
    /**
     * Check if wars are spreading to neighboring countries
     */
    private suspend fun checkWarSpread(playerCountryId: Long) {
        val activeWars = repository.getWars(playerCountryId).first()
            .filter { it.status == WarStatus.ACTIVE }
        
        for (war in activeWars) {
            // Check if enemy's allies should join
            val enemyAlliances = repository.getAlliances(war.enemyCountryId).first()
            
            for (alliance in enemyAlliances) {
                if (alliance.mutualDefense) {
                    // Ally should consider joining
                    val shouldJoin = random.nextDouble() < 0.5 // 50% chance
                    
                    if (shouldJoin) {
                        // Generate event for player
                        val joinEvent = GameEvent(
                            countryId = playerCountryId,
                            eventType = GameEventType.CRISIS,
                            title = "War Expanding",
                            description = "${alliance.members} may join the war against you",
                            category = EventCategory.DIPLOMATIC,
                            priority = 9,
                            options = "Preemptive Strike|Negotiate|Seek Allies|Withdraw",
                            eventDate = System.currentTimeMillis()
                        )
                        repository.insertGameEvent(joinEvent)
                    }
                }
            }
        }
    }
    
    /**
     * Update alliance dynamics - countries join/leave based on interests
     */
    private suspend fun updateAllianceDynamics(playerCountryId: Long) {
        val alliances = repository.getAlliances(playerCountryId).first()
        
        for (alliance in alliances) {
            // Check if members are happy with alliance
            val memberCountries = alliance.members.split(",").map { it.toLong() }
            
            for (memberId in memberCountries) {
                val member = repository.getCountryById(memberId) ?: continue
                val memberAI = getCountryAI(member)
                
                // 5% chance per day to reconsider alliance
                if (random.nextDouble() < 0.05) {
                    val satisfaction = calculateAllianceSatisfaction(member, alliance)
                    
                    if (satisfaction < 30 && memberAI.reliability < 0.5) {
                        // Unreliable country might leave
                        val leaveEvent = GameEvent(
                            countryId = playerCountryId,
                            eventType = GameEventType.DIPLOMATIC_INCIDENT,
                            title = "Alliance Member Considering Exit",
                            description = "${member.name} unhappy with ${alliance.allianceName}",
                            category = EventCategory.DIPLOMATIC,
                            priority = 6,
                            options = "Offer Incentives|Pressure|Accept|Find Replacement",
                            eventDate = System.currentTimeMillis()
                        )
                        repository.insertGameEvent(leaveEvent)
                    }
                }
            }
        }
    }
    
    /**
     * Process world events that affect multiple countries
     */
    private suspend fun processWorldEvents(playerCountryId: Long) {
        // 1% chance of major world event per day
        if (random.nextDouble() < 0.01) {
            val worldEvent = generateWorldEvent(playerCountryId)
            repository.insertGameEvent(worldEvent)
        }
        
        // 5% chance of regional event
        if (random.nextDouble() < 0.05) {
            val regionalEvent = generateRegionalEvent(playerCountryId)
            repository.insertGameEvent(regionalEvent)
        }
    }
    
    /**
     * Generate major world event
     */
    private fun generateWorldEvent(playerCountryId: Long): GameEvent {
        val worldEvents = listOf(
            GameEvent(
                countryId = playerCountryId,
                eventType = GameEventType.ECONOMIC_SHIFT,
                title = "Global Recession",
                description = "World economy enters recession, affecting all trade",
                category = EventCategory.ECONOMIC,
                priority = 10,
                options = "Stimulus|Protectionism|International Coordination|Wait It Out",
                eventDate = System.currentTimeMillis()
            ),
            GameEvent(
                countryId = playerCountryId,
                eventType = GameEventType.CRISIS,
                title = "Pandemic Outbreak",
                description = "New disease spreads globally",
                category = EventCategory.SOCIAL,
                priority = 10,
                options = "Lockdown|Vaccination|Travel Ban|Public Info",
                eventDate = System.currentTimeMillis()
            ),
            GameEvent(
                countryId = playerCountryId,
                eventType = GameEventType.BREAKTHROUGH,
                title = "Climate Technology Breakthrough",
                description = "Major advancement in clean energy technology",
                category = EventCategory.ECONOMIC,
                priority = 7,
                options = "Invest|Subsidize|Export|Open Source",
                eventDate = System.currentTimeMillis()
            ),
            GameEvent(
                countryId = playerCountryId,
                eventType = GameEventType.DIPLOMATIC_INCIDENT,
                title = "New International Organization",
                description = "Countries propose new global governing body",
                category = EventCategory.DIPLOMATIC,
                priority = 6,
                options = "Join|Lead|Observe|Decline",
                eventDate = System.currentTimeMillis()
            ),
            GameEvent(
                countryId = playerCountryId,
                eventType = GameEventType.CRISIS,
                title = "Resource Discovery",
                description = "Massive resource deposit discovered in contested area",
                category = EventCategory.ECONOMIC,
                priority = 8,
                options = "Claim|Negotiate|Joint Development|Avoid Conflict",
                eventDate = System.currentTimeMillis()
            )
        )
        
        return worldEvents.random()
    }
    
    /**
     * Generate regional event
     */
    private fun generateRegionalEvent(playerCountryId: Long): GameEvent {
        val regionalEvents = listOf(
            GameEvent(
                countryId = playerCountryId,
                eventType = GameEventType.DIPLOMATIC_INCIDENT,
                title = "Regional Trade Bloc Formed",
                description = "Neighboring countries form new trade bloc",
                category = EventCategory.ECONOMIC,
                priority = 6,
                options = "Join|Compete|Sanction|Ignore",
                eventDate = System.currentTimeMillis()
            ),
            GameEvent(
                countryId = playerCountryId,
                eventType = GameEventType.CRISIS,
                title = "Border Skirmish",
                description = "Military clash on regional border",
                category = EventCategory.DIPLOMATIC,
                priority = 7,
                options = "Mediate|Support Side|Condemn Both|Stay Neutral",
                eventDate = System.currentTimeMillis()
            ),
            GameEvent(
                countryId = playerCountryId,
                eventType = GameEventType.SOCIAL_CHANGE,
                title = "Regional Democracy Movement",
                description = "Pro-democracy protests spread across region",
                category = EventCategory.SOCIAL,
                priority = 6,
                options = "Support|Oppose|Neutral|Monitor",
                eventDate = System.currentTimeMillis()
            )
        )
        
        return regionalEvents.random()
    }
    
    /**
     * Get or generate AI personality for country
     */
    private fun getCountryAI(country: Country): CountryAI {
        // Use country name as seed for consistent AI
        val seed = country.name.hashCode()
        val random = Random(seed)
        
        val leaderTypes = LeaderType.entries
        
        return CountryAI(
            countryId = country.id,
            aggression = random.nextDouble(),
            cooperation = random.nextDouble(),
            reliability = random.nextDouble(),
            ambition = random.nextDouble(),
            ideology = country.ideology.name,
            leaderPersonality = leaderTypes.random(random)
        )
    }
    
    /**
     * Update diplomatic relation
     */
    private suspend fun updateRelation(playerCountryId: Long, foreignCountryId: Long, change: Int) {
        val relations = repository.getRelations(playerCountryId).first()
        val relation = relations.find { it.foreignCountryId == foreignCountryId }
            ?: return
        
        val newLevel = (relation.relationLevel + change).coerceIn(-100, 100)
        
        repository.updateDiplomaticRelation(relation.copy(
            relationLevel = newLevel,
            lastContact = System.currentTimeMillis()
        ))
    }
    
    /**
     * Calculate alliance satisfaction for a member
     */
    private fun calculateAllianceSatisfaction(member: Country, alliance: Alliance): Double {
        // Base satisfaction
        var satisfaction = 50.0
        
        // Bonus for mutual defense
        if (alliance.mutualDefense) satisfaction += 15
        
        // Bonus for shared technology
        if (alliance.sharedTechnology) satisfaction += 10
        
        // Penalty if member is much stronger than others
        if (member.gdp > 1_000_000_000_000) satisfaction -= 10
        
        // Random variation
        satisfaction += (Random(member.id.hashCode()).nextDouble() - 0.5) * 20
        
        return satisfaction.coerceIn(0.0, 100.0)
    }
    
    /**
     * Get country ideology string
     */
    private suspend fun getCountryIdeology(countryId: Long): String {
        val playerCountry = repository.getPlayerCountrySync()
        return playerCountry?.let { 
            repository.getCountryById(it.countryId)?.ideology?.name ?: "Democracy"
        } ?: "Democracy"
    }
}

enum class CountryAction {
    // Hostile actions
    MILITARY_THREAT, ECONOMIC_SANCTIONS, CYBER_ATTACK, SUPPORT_REBELS,
    TERRITORIAL_CLAIM, MILITARY_BUILDUP, WAR_GAMES, ARMS_RACE,
    
    // Friendly actions
    TRADE_OFFER, MILITARY_COOPERATION, TECHNOLOGY_SHARING, DIPLOMATIC_SUPPORT,
    TRADE_DEAL, INVESTMENT_OFFER, ECONOMIC_AID, MARKET_OPENING,
    
    // Neutral actions
    DIPLOMATIC_VISIT, CULTURAL_EXCHANGE, DEMARCHE, NOTE_VERBALE,
    BASE_ACCESS, ARMS_DEAL, RESOURCE_GRAB, SPHERE_OF_INFLUENCE
}
