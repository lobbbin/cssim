package com.country.simulator.engine

import com.country.simulator.model.*
import com.country.simulator.repositories.GameRepository
import kotlinx.coroutines.flow.first
import kotlin.random.Random

class ElectionEngine(private val repository: GameRepository) {
    
    private val random = Random
    
    /**
     * Start a new election cycle
     */
    suspend fun startElection(countryId: Long, type: ElectionType): Election {
        val election = Election(
            countryId = countryId,
            electionType = type,
            scheduledDate = System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000, // 30 days
            isActive = true
        )
        
        val electionId = repository.insertElection(election)
        
        // Generate candidates and campaigns
        generateCampaigns(electionId, countryId, type)
        
        return election.copy(id = electionId)
    }
    
    /**
     * Generate campaigns for an election
     */
    private suspend fun generateCampaigns(electionId: Long, countryId: Long, type: ElectionType) {
        val parties = listOf(
            "Progressive Party", "Conservative Alliance", "Liberal Democrats", 
            "National Front", "Green Coalition", "Workers Party"
        ).shuffled().take(4)
        
        val slogans = listOf(
            "A Better Tomorrow", "Strong Leadership", "Change We Need",
            "Prosperity for All", "Unity and Progress", "Security First"
        )
        
        for ((index, party) in parties.withIndex()) {
            val campaign = Campaign(
                electionId = electionId,
                candidateName = generateCandidateName(),
                party = party,
                slogan = slogans[index % slogans.size],
                budget = (1_000_000..50_000_000).random().toDouble(),
                followers = (10_000..5_000_000).random(),
                pollRating = (10.0..40.0).random(),
                promises = generatePromises()
            )
            
            repository.insertCampaign(campaign)
            
            // Generate initial campaign events
            generateCampaignEvents(campaign.id)
        }
    }
    
    /**
     * Generate campaign events for a campaign
     */
    private suspend fun generateCampaignEvents(campaignId: Long) {
        val eventTypes = CampaignEventType.entries
        val locations = listOf(
            "Capital City", "Industrial District", "Rural Area", "Coastal Region",
            "University Town", "Financial Center", "Working Class Neighborhood"
        )
        
        for (i in 1..5) {
            val eventType = eventTypes.random()
            val event = CampaignEvent(
                campaignId = campaignId,
                eventType = eventType,
                location = locations.random(),
                description = "${eventType.name} - Campaign Stop $i",
                cost = (10_000..500_000).random().toDouble(),
                impact = (-5.0..15.0).random(),
                scheduledDate = System.currentTimeMillis() + i * 3 * 24 * 60 * 60 * 1000
            )
            
            repository.insertCampaignEvent(event)
        }
    }
    
    /**
     * Execute a campaign event
     */
    suspend fun executeCampaignEvent(event: CampaignEvent): CampaignEventResult {
        val campaign = repository.getCampaignById(event.campaignId) ?: return CampaignEventResult.FAILURE
        
        // Calculate event success based on various factors
        val baseSuccess = 0.6
        val randomFactor = random.nextDouble() * 0.3
        val successChance = baseSuccess + randomFactor
        
        val actualImpact = if (random.nextDouble() < successChance) {
            event.impact * (1.0 + random.nextDouble() * 0.5)
        } else {
            event.impact * 0.5
        }
        
        // Update campaign stats
        repository.addCampaignSpending(event.campaignId, event.cost)
        repository.addCampaignFollowers(event.campaignId, (actualImpact * 1000).toInt())
        
        // Update campaign poll rating
        val updatedCampaign = campaign.copy(
            spent = campaign.spent + event.cost,
            followers = campaign.followers + (actualImpact * 1000).toInt(),
            pollRating = (campaign.pollRating + actualImpact * 0.5).coerceIn(0.0, 100.0),
            ralliesHeld = if (event.eventType == CampaignEventType.RALLY) campaign.ralliesHeld + 1 else campaign.ralliesHeld,
            speechesGiven = if (event.eventType == CampaignEventType.SPEECH) campaign.speechesGiven + 1 else campaign.speechesGiven
        )
        
        repository.updateCampaign(updatedCampaign)
        
        return CampaignEventResult.SUCCESS(actualImpact)
    }
    
    /**
     * Handle a campaign scandal
     */
    suspend fun handleCampaignScandal(campaignId: Long, scandal: Scandal): ScandalResponse {
        val campaign = repository.getCampaignById(campaignId) ?: return ScandalResponse.NO_EFFECT
        
        val severityMultiplier = scandal.severity / 10.0
        val pollDamage = severityMultiplier * 20 * (1 - random.nextDouble() * 0.3)
        
        // Update campaign with scandal impact
        val updatedCampaign = campaign.copy(
            scandals = campaign.scandals + 1,
            pollRating = (campaign.pollRating - pollDamage).coerceIn(0.0, 100.0)
        )
        
        repository.updateCampaign(updatedCampaign)
        
        return ScandalResponse.DAMAGE(pollDamage)
    }
    
    /**
     * Respond to a scandal with different strategies
     */
    suspend fun respondToScandal(scandal: Scandal, strategy: ScandalResponseStrategy): Scandal {
        val effectiveness = when (strategy) {
            ScandalResponseStrategy.DENY -> if (scandal.severity < 5) 0.7 else 0.3
            ScandalResponseStrategy.APOLOGIZE -> if (scandal.severity > 5) 0.6 else 0.4
            ScandalResponseStrategy.ATTACK -> if (random.nextDouble() < 0.5) 0.6 else 0.2
            ScandalResponseStrategy.IGNORE -> 0.3
            ScandalResponseStrategy.INVESTIGATE -> 0.5
        }
        
        val success = random.nextDouble() < effectiveness
        val resolutionDate = if (success) System.currentTimeMillis() else null
        
        val impactReduction = if (success) {
            scandal.impactOnApproval * 0.5
        } else {
            scandal.impactOnApproval * 1.2
        }
        
        val updatedScandal = scandal.copy(
            resolved = success,
            resolutionDate = resolutionDate,
            impactOnApproval = impactReduction,
            responseStrategy = strategy.name
        )
        
        repository.updateScandal(updatedScandal)
        
        return updatedScandal
    }
    
    /**
     * Conduct an opinion poll
     */
    suspend fun conductPoll(countryId: Long, campaignId: Long): OpinionPoll {
        val campaign = repository.getCampaignById(campaignId)
        val sampleSize = (800..1500).random()
        val marginOfError = (2.5..4.0).random()
        
        // Calculate poll results based on campaign performance
        val baseSupport = campaign?.pollRating ?: 50.0
        val randomVariance = (random.nextDouble() - 0.5) * marginOfError * 2
        
        val approvalRating = (baseSupport + randomVariance).coerceIn(0.0, 100.0)
        val disapprovalRating = ((100 - approvalRating) * (0.8 + random.nextDouble() * 0.4)).coerceIn(0.0, 100.0)
        val undecidedPercentage = (100 - approvalRating - disapprovalRating).coerceIn(0.0, 100.0)
        
        val poll = OpinionPoll(
            countryId = countryId,
            pollDate = System.currentTimeMillis(),
            sampleSize = sampleSize,
            marginOfError = marginOfError,
            approvalRating = approvalRating,
            disapprovalRating = disapprovalRating,
            undecidedPercentage = undecidedPercentage,
            topIssues = """["Economy", "Healthcare", "Education", "Security", "Environment"]""",
            demographicBreakdown = generateDemographicBreakdown(approvalRating)
        )
        
        return poll
    }
    
    /**
     * Calculate election results
     */
    suspend fun calculateElectionResults(electionId: Long, campaigns: List<Campaign>): ElectionResult {
        val totalVotes = (10_000_000..100_000_000).random()
        val turnoutPercentage = (40.0..80.0).random()
        
        // Calculate votes based on campaign performance
        val weights = campaigns.map { campaign ->
            campaign.pollRating * 0.4 + 
            (campaign.followers / 1_000_000.0) * 0.2 +
            (campaign.ralliesHeld * 2) * 0.2 +
            (100 - campaign.scandals * 10) * 0.2
        }
        
        val totalWeight = weights.sum()
        val normalizedWeights = weights.map { it / totalWeight }
        
        val opponents = campaigns.mapIndexed { index, campaign ->
            ElectionOpponent(
                name = campaign.candidateName,
                party = campaign.party,
                votePercentage = (normalizedWeights[index] * 100).coerceIn(0.0, 100.0)
            )
        }.sortedByDescending { it.votePercentage }
        
        val winner = opponents.first()
        
        val result = ElectionResult(
            winnerId = electionId,
            winnerName = winner.name,
            winnerParty = winner.party,
            votePercentage = winner.votePercentage,
            turnoutPercentage = turnoutPercentage,
            totalVotes = totalVotes,
            opponents = opponents
        )
        
        // Update election with result
        val election = Election(
            id = electionId,
            countryId = campaigns.firstOrNull()?.electionId ?: 0,
            electionType = ElectionType.PRESIDENTIAL,
            scheduledDate = System.currentTimeMillis(),
            completedDate = System.currentTimeMillis(),
            isActive = false,
            result = result
        )
        
        repository.updateElection(election)
        
        return result
    }
    
    /**
     * Process election outcome and trigger game mode changes
     */
    suspend fun processElectionOutcome(result: ElectionResult, playerCampaignId: Long): ElectionOutcome {
        val playerCampaign = repository.getCampaignById(playerCampaignId)
        val isVictory = playerCampaign?.let { 
            result.winnerName == it.candidateName 
        } ?: false
        
        return if (isVictory) {
            ElectionOutcome.VICTORY(result)
        } else {
            ElectionOutcome.DEFEAT(result)
        }
    }
    
    /**
     * Generate campaign promises
     */
    private fun generatePromises(): String {
        val promises = listOf(
            "Reduce unemployment by 5%",
            "Build 100 new schools",
            "Cut taxes for middle class",
            "Increase minimum wage",
            "Expand healthcare coverage",
            "Invest in renewable energy",
            "Reduce crime by 20%",
            "Improve public transport",
            "Create 50,000 new jobs",
            "Balance the budget"
        )
        
        return promises.shuffled().take(5).joinToString("|")
    }
    
    /**
     * Generate candidate name
     */
    private fun generateCandidateName(): String {
        val firstNames = listOf(
            "James", "John", "Robert", "Michael", "William", "David", "Richard",
            "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Susan"
        )
        val lastNames = listOf(
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller",
            "Davis", "Rodriguez", "Martinez", "Wilson", "Anderson", "Taylor"
        )
        
        return "${firstNames.random()} ${lastNames.random()}"
    }
    
    /**
     * Generate demographic breakdown for polls
     */
    private fun generateDemographicBreakdown(baseApproval: Double): String {
        val breakdown = mapOf(
            "18-29" to (baseApproval + (random.nextDouble() - 0.5) * 20).coerceIn(0.0, 100.0),
            "30-44" to (baseApproval + (random.nextDouble() - 0.5) * 15).coerceIn(0.0, 100.0),
            "45-64" to (baseApproval + (random.nextDouble() - 0.5) * 10).coerceIn(0.0, 100.0),
            "65+" to (baseApproval + (random.nextDouble() - 0.5) * 10).coerceIn(0.0, 100.0)
        )
        
        return breakdown.entries.joinToString(",") { "\"${it.key}\": ${it.value}" }
    }
}

sealed class CampaignEventResult {
    object FAILURE : CampaignEventResult()
    data class SUCCESS(val impact: Double) : CampaignEventResult()
}

sealed class ScandalResponse {
    object NO_EFFECT : ScandalResponse()
    data class DAMAGE(val pollDamage: Double) : ScandalResponse()
}

enum class ScandalResponseStrategy {
    DENY,
    APOLOGIZE,
    ATTACK,
    IGNORE,
    INVESTIGATE
}

sealed class ElectionOutcome {
    data class VICTORY(val result: ElectionResult) : ElectionOutcome()
    data class DEFEAT(val result: ElectionResult) : ElectionOutcome()
}
