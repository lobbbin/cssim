package com.country.simulator.engine

import com.country.simulator.model.*
import com.country.simulator.repositories.GameRepository
import kotlinx.coroutines.flow.first
import kotlin.random.Random

/**
 * Dynamic NPC AI System
 * NPCs with memories, relationships, goals, and emergent behavior
 * NPCs react to player actions and influence the world
 */
class DynamicNPCSystem(private val repository: GameRepository) {
    
    private val random = Random
    
    // NPC personality matrices
    data class NPCPersonality(
        val openness: Double, // 0-1 (traditional vs innovative)
        val conscientiousness: Double, // 0-1 (careless vs organized)
        val extraversion: Double, // 0-1 (introverted vs outgoing)
        val agreeableness: Double, // 0-1 (critical vs compassionate)
        val neuroticism: Double // 0-1 (stable vs reactive)
    )
    
    // NPC goals and motivations
    data class NPCGoal(
        val goalType: GoalType,
        val priority: Int, // 1-10
        val progress: Double, // 0-1
        val deadline: Long?,
        val relatedIssue: String
    )
    
    enum class GoalType {
        WEALTH, POWER, JUSTICE, FREEDOM, SECURITY, EQUALITY, TRADITION, INNOVATION
    }
    
    // NPC relationship network
    data class NPCRelationship(
        val npcId: Long,
        val trust: Double, // -1 to 1
        val respect: Double, // -1 to 1
        val obligation: Double, // -1 to 1 (they owe me / I owe them)
        val lastInteraction: Long,
        val interactionHistory: List<Interaction>
    )
    
    data class Interaction(
        val type: InteractionType,
        val date: Long,
        val outcome: Double, // -1 to 1
        val notes: String
    )
    
    enum class InteractionType {
        BUSINESS, SOCIAL, POLITICAL, ROMANTIC, FAMILY, CONFLICT, COOPERATION
    }
    
    /**
     * Process all NPCs daily - thoughts, actions, reactions
     */
    suspend fun processDailyNPCActions(countryId: Long) {
        val npcs = repository.getNPCs(countryId, 50, 0).first()
        
        for (npc in npcs) {
            // 30% chance NPC takes action each day
            if (random.nextDouble() < 0.3) {
                val action = generateNPCAction(npc, countryId)
                executeNPCAction(action, npc, countryId)
            }
            
            // Update NPC memories (decay old ones)
            updateNPCMemories(npc.id)
            
            // Check if NPC's opinion of government changed
            updateGovernmentOpinion(npc, countryId)
        }
    }
    
    /**
     * Generate action based on NPC personality, goals, and current situation
     */
    private suspend fun generateNPCAction(npc: NPC, countryId: Long): NPCAction {
        val personality = getNPCPersonality(npc)
        val goals = getNPCGoals(npc)
        val playerCountry = repository.getPlayerCountrySync()
        
        // Action based on approval + personality
        val approval = npc.approvalOfGovernment
        
        return when {
            // Low approval + high neuroticism = protest
            approval < 40 && personality.neuroticism > 0.7 -> {
                NPCAction.PROTEST
            }
            
            // Low approval + high conscientiousness = organize
            approval < 40 && personality.conscientiousness > 0.7 -> {
                NPCAction.ORGANIZE_GROUP
            }
            
            // High approval + high extraversion = campaign donation
            approval > 70 && personality.extraversion > 0.7 && npc.income > 100000 -> {
                NPCAction.DONATE_TO_CAMPAIGN
            }
            
            // Wealth goal + opportunity = start business
            goals.any { it.goalType == GoalType.WEALTH } && 
            npc.wealth > 50000 && random.nextDouble() < 0.1 -> {
                NPCAction.START_BUSINESS
            }
            
            // Justice goal + high agreeableness = activism
            goals.any { it.goalType == GoalType.JUSTICE } && 
            personality.agreeableness > 0.7 -> {
                NPCAction.ACTIVISM
            }
            
            // Power goal + high extraversion = run for office
            goals.any { it.goalType == GoalType.POWER } && 
            personality.extraversion > 0.6 && random.nextDouble() < 0.05 -> {
                NPCAction.RUN_FOR_OFFICE
            }
            
            // Default: random action based on occupation
            else -> {
                when (npc.occupationType) {
                    OccupationType.BUSINESS_OWNER -> NPCAction.LOBBY_GOVERNMENT
                    OccupationType.JOURNALIST -> NPCAction.WRITE_ARTICLE
                    OccupationType.ACTIVIST -> NPCAction.PROTEST
                    OccupationType.SCIENTIST -> NPCAction.RESEARCH_BREAKTHROUGH
                    else -> NPCAction.VOICE_OPINION
                }
            }
        }
    }
    
    /**
     * Execute NPC action with consequences
     */
    private suspend fun executeNPCAction(action: NPCAction, npc: NPC, countryId: Long) {
        val playerCountry = repository.getPlayerCountrySync() ?: return
        
        when (action) {
            NPCAction.PROTEST -> {
                // Generate protest event
                val protestSize = when {
                    npc.approvalOfGovernment < 20 -> random.nextInt(1000, 10000)
                    npc.approvalOfGovernment < 40 -> random.nextInt(100, 1000)
                    else -> random.nextInt(10, 100)
                }
                
                val approvalImpact = -(protestSize / 1000.0)
                
                val updatedCountry = playerCountry.copy(
                    approvalRating = (playerCountry.approvalRating + approvalImpact).coerceIn(0.0, 100.0),
                    stability = (playerCountry.stability - 2.0).coerceIn(0.0, 100.0),
                    lastUpdated = System.currentTimeMillis()
                )
                repository.updatePlayerCountry(updatedCountry)
                
                // Generate news event
                val protestEvent = GameEvent(
                    countryId = countryId,
                    eventType = GameEventType.PROTEST,
                    title = "Protest in ${npc.location}",
                    description = "${protestSize} people protest government policies",
                    category = EventCategory.SOCIAL,
                    priority = if (protestSize > 1000) 8 else 5,
                    options = "Ignore|Address Concerns|Police Response|Concede Demands",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(protestEvent)
            }
            
            NPCAction.ORGANIZE_GROUP -> {
                // Create activist group
                val activist = Activist(
                    countryId = countryId,
                    name = npc.name,
                    cause = getNPCGoals(npc).firstOrNull()?.relatedIssue ?: "Reform",
                    organization = "${npc.location} Reform Movement",
                    followers = random.nextInt(100, 5000),
                    influence = npc.income / 100000.0,
                    tactics = "Peaceful Protest",
                    mediaAttention = random.nextInt(1, 10)
                )
                repository.insertActivist(activist)
                
                // Generate ongoing pressure tasks
                val pressureTask = MicroTask(
                    countryId = countryId,
                    taskType = MicroTaskType.ATTEND_MEETING,
                    title = "Meet with ${activist.organization}",
                    description = "${activist.name} demands meeting about ${activist.cause}",
                    category = TaskCategory.SOCIAL,
                    priority = Priority.MEDIUM,
                    status = TaskStatus.PENDING,
                    createdDate = System.currentTimeMillis(),
                    dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
                )
                repository.insertMicroTask(pressureTask)
            }
            
            NPCAction.DONATE_TO_CAMPAIGN -> {
                // Create donor
                val donationAmount = npc.income * 0.1
                val donor = Donor(
                    countryId = countryId,
                    name = npc.name,
                    donorType = DonorType.INDIVIDUAL,
                    totalDonated = donationAmount,
                    politicalLeanings = npc.politicalLeanings,
                    expectations = listOf("Tax cuts", "Deregulation").joinToString("|")
                )
                repository.insertDonor(donor)
                
                // Slight approval boost
                val updatedCountry = playerCountry.copy(
                    approvalRating = (playerCountry.approvalRating + 0.5).coerceIn(0.0, 100.0),
                    lastUpdated = System.currentTimeMillis()
                )
                repository.updatePlayerCountry(updatedCountry)
            }
            
            NPCAction.START_BUSINESS -> {
                // Create new business
                val business = Business(
                    countryId = countryId,
                    ownerId = npc.id,
                    ownerName = npc.name,
                    businessName = "${npc.lastName}'s ${getNPCGoals(npc).firstOrNull()?.relatedIssue ?: "Business"}",
                    businessType = BusinessType.SOLE_PROPRIETORSHIP,
                    industry = "Services",
                    location = npc.location,
                    employees = random.nextInt(1, 20),
                    revenue = npc.wealth * 0.2,
                    taxPaid = 0.0,
                    licenseStatus = LicenseStatus.PENDING,
                    complianceRating = 100.0
                )
                repository.insertBusiness(business)
                
                // Generate permit task
                val permitTask = MicroTask(
                    countryId = countryId,
                    taskType = MicroTaskType.APPROVE_PERMIT,
                    title = "Review Business License Application",
                    description = "${npc.name} applying for business license",
                    category = TaskCategory.ECONOMY,
                    priority = Priority.LOW,
                    status = TaskStatus.PENDING,
                    createdDate = System.currentTimeMillis(),
                    dueDate = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000
                )
                repository.insertMicroTask(permitTask)
                
                // Economic boost
                val updatedCountry = playerCountry.copy(
                    growthRate = (playerCountry.growthRate + 0.1).coerceIn(-5.0, 10.0),
                    unemploymentRate = (playerCountry.unemploymentRate - 0.1).coerceIn(0.0, 50.0),
                    lastUpdated = System.currentTimeMillis()
                )
                repository.updatePlayerCountry(updatedCountry)
            }
            
            NPCAction.ACTIVISM -> {
                // Generate ongoing activism pressure
                val activismImpact = when {
                    npc.income > 200000 -> 3.0 // Rich activist = more impact
                    npc.occupationType == OccupationType.JOURNALIST -> 5.0 // Media activist
                    else -> 1.0
                }
                
                // Generate scandal if targeting government
                if (npc.approvalOfGovernment < 30 && random.nextDouble() < 0.3) {
                    val scandal = Scandal(
                        countryId = countryId,
                        title = "Activist Allegations",
                        description = "${npc.name} alleges government misconduct",
                        severity = random.nextInt(3, 9),
                        type = ScandalType.ABUSE_OF_POWER,
                        involvedPersons = listOf("Government Official"),
                        dateExposed = System.currentTimeMillis(),
                        impactOnApproval = -activismImpact
                    )
                    repository.insertScandal(scandal)
                }
            }
            
            NPCAction.RUN_FOR_OFFICE -> {
                // Generate election challenge
                val electionTask = MicroTask(
                    countryId = countryId,
                    taskType = MicroTaskType.ATTEND_MEETING,
                    title = "Primary Challenge Announced",
                    description = "${npc.name} announces primary challenge",
                    category = TaskCategory.POLITICS,
                    priority = Priority.HIGH,
                    status = TaskStatus.PENDING,
                    createdDate = System.currentTimeMillis(),
                    dueDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000
                )
                repository.insertMicroTask(electionTask)
            }
            
            NPCAction.LOBBY_GOVERNMENT -> {
                // Generate lobbying task
                val lobbyTask = MicroTask(
                    countryId = countryId,
                    taskType = MicroTaskType.ATTEND_MEETING,
                    title = "Business Lobby Meeting",
                    description = "${npc.name} requests meeting on industry regulations",
                    category = TaskCategory.ECONOMY,
                    priority = Priority.MEDIUM,
                    status = TaskStatus.PENDING,
                    createdDate = System.currentTimeMillis(),
                    dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
                )
                repository.insertMicroTask(lobbyTask)
            }
            
            NPCAction.WRITE_ARTICLE -> {
                // Generate media event
                val sentiment = when {
                    npc.approvalOfGovernment < 40 -> "Critical"
                    npc.approvalOfGovernment > 70 -> "Supportive"
                    else -> "Mixed"
                }
                
                val mediaEvent = GameEvent(
                    countryId = countryId,
                    eventType = GameEventType.SOCIAL_CHANGE,
                    title = "Media Article Published",
                    description = "${npc.name} publishes $sentiment article about government",
                    category = EventCategory.SOCIAL,
                    priority = 4,
                    options = "Ignore|Respond|Pressure Editor|Embrace",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(mediaEvent)
            }
            
            NPCAction.RESEARCH_BREAKTHROUGH -> {
                // Generate breakthrough event
                val breakthroughEvent = GameEvent(
                    countryId = countryId,
                    eventType = GameEventType.BREAKTHROUGH,
                    title = "Scientific Breakthrough",
                    description = "${npc.name} announces research breakthrough",
                    category = EventCategory.ECONOMIC,
                    priority = 6,
                    options = "Fund Development|Patent|Open Source|Partner",
                    eventDate = System.currentTimeMillis()
                )
                repository.insertGameEvent(breakthroughEvent)
            }
            
            NPCAction.VOICE_OPINION -> {
                // Small opinion shift in community
                val communityImpact = (random.nextDouble() - 0.5) * 2
                // Would update community opinion tracking
            }
        }
    }
    
    /**
     * Update NPC opinion of government based on events and policies
     */
    private suspend fun updateGovernmentOpinion(npc: NPC, countryId: Long) {
        val playerCountry = repository.getPlayerCountrySync() ?: return
        
        // Get NPC's main issue
        val goals = getNPCGoals(npc)
        val mainGoal = goals.firstOrNull()
        
        // Calculate opinion change based on government performance on NPC's issues
        val opinionChange = when (mainGoal?.relatedIssue) {
            "Economy" -> {
                val growth = playerCountry.growthRate
                val unemployment = playerCountry.unemploymentRate
                when {
                    growth > 3 && unemployment < 5 -> 5.0
                    growth > 2 && unemployment < 6 -> 2.0
                    growth < 0 || unemployment > 8 -> -5.0
                    else -> 0.0
                }
            }
            "Healthcare" -> {
                val healthIndex = playerCountry.healthIndex
                when {
                    healthIndex > 70 -> 3.0
                    healthIndex < 40 -> -4.0
                    else -> 0.0
                }
            }
            "Security" -> {
                val stability = playerCountry.stability
                when {
                    stability > 70 -> 3.0
                    stability < 40 -> -4.0
                    else -> 0.0
                }
            }
            "Environment" -> {
                val envRating = playerCountry.environmentalRating
                when {
                    envRating > 70 -> 3.0
                    envRating < 40 -> -3.0
                    else -> 0.0
                }
            }
            else -> 0.0
        }
        
        // Apply personality modifier (neurotic people react more strongly)
        val personality = getNPCPersonality(npc)
        val modifiedChange = opinionChange * (1 + personality.neuroticism)
        
        // Update NPC approval
        val newApproval = (npc.approvalOfGovernment + modifiedChange).coerceIn(0.0, 100.0)
        
        if (newApproval != npc.approvalOfGovernment) {
            repository.updateNPC(npc.copy(
                approvalOfGovernment = newApproval,
                lastUpdated = System.currentTimeMillis()
            ))
        }
    }
    
    /**
     * Get or generate NPC personality
     */
    private fun getNPCPersonality(npc: NPC): NPCPersonality {
        // Use NPC name as seed for consistent personality
        val seed = npc.name.hashCode()
        val random = Random(seed)
        
        return NPCPersonality(
            openness = random.nextDouble(),
            conscientiousness = random.nextDouble(),
            extraversion = random.nextDouble(),
            agreeableness = random.nextDouble(),
            neuroticism = random.nextDouble()
        )
    }
    
    /**
     * Get NPC goals based on occupation and background
     */
    private fun getNPCGoals(npc: NPC): List<NPCGoal> {
        val goals = mutableListOf<NPCGoal>()
        
        // Primary goal based on occupation
        val primaryGoal = when (npc.occupationType) {
            OccupationType.BUSINESS_OWNER, OccupationType.CORPORATE_EXEC -> 
                NPCGoal(GoalType.WEALTH, 8, 0.0, null, "Economy")
            OccupationType.POLITICIAN -> 
                NPCGoal(GoalType.POWER, 9, 0.0, null, "Political Reform")
            OccupationType.ACTIVIST, OccupationType.JOURNALIST -> 
                NPCGoal(GoalType.JUSTICE, 8, 0.0, null, "Civil Rights")
            OccupationType.SCIENTIST -> 
                NPCGoal(GoalType.INNOVATION, 7, 0.0, null, "Research Funding")
            OccupationType.MILITARY, OccupationType.POLICE -> 
                NPCGoal(GoalType.SECURITY, 8, 0.0, null, "National Security")
            OccupationType.TEACHER -> 
                NPCGoal(GoalType.EQUALITY, 7, 0.0, null, "Education")
            OccupationType.WORKER, OccupationType.FARMER -> 
                NPCGoal(GoalType.WEALTH, 6, 0.0, null, "Jobs")
            else -> NPCGoal(GoalType.SECURITY, 5, 0.0, null, "Economy")
        }
        goals.add(primaryGoal)
        
        // Secondary goal based on age
        if (npc.age < 30) {
            goals.add(NPCGoal(GoalType.FREEDOM, 5, 0.0, null, "Social Issues"))
        } else if (npc.age > 50) {
            goals.add(NPCGoal(GoalType.TRADITION, 5, 0.0, null, "Healthcare"))
        }
        
        return goals
    }
    
    /**
     * Update NPC memories (decay old ones, add new ones)
     */
    private suspend fun updateNPCMemories(npcId: Long) {
        // Get NPC memories
        // Decay memories older than 365 days
        // Remove memories older than 730 days
        // This would use NPCMemoryDao
    }
    
    /**
     * Add memory to NPC
     */
    suspend fun addNPCMemory(npcId: Long, memory: NPCMemory) {
        // Insert memory into database
        // Would use NPCMemoryDao
    }
}

enum class NPCAction {
    PROTEST,
    ORGANIZE_GROUP,
    DONATE_TO_CAMPAIGN,
    START_BUSINESS,
    ACTIVISM,
    RUN_FOR_OFFICE,
    LOBBY_GOVERNMENT,
    WRITE_ARTICLE,
    RESEARCH_BREAKTHROUGH,
    VOICE_OPINION
}
