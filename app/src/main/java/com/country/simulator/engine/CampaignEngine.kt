package com.country.simulator.engine

import com.country.simulator.model.*
import com.country.simulator.repositories.GameRepository
import kotlinx.coroutines.flow.first
import kotlin.random.Random

/**
 * Campaign Engine - Manages all campaign-related micro-tasks
 * Handles speeches, rallies, social media, scandals, polls, etc.
 */
class CampaignEngine(private val repository: GameRepository) {
    
    private val random = Random
    
    /**
     * Run a campaign speech
     */
    suspend fun runSpeech(campaignId: Long, location: String, speechType: SpeechType): SpeechResult {
        val campaign = repository.getCampaignById(campaignId) ?: return SpeechResult.FAILURE
        
        val baseImpact = when (speechType) {
            SpeechType.ECONOMIC -> 5.0
            SpeechType.SOCIAL -> 4.0
            SpeechType.FOREIGN -> 3.0
            SpeechType.RALLY -> 8.0
            SpeechType.TOWN_HALL -> 6.0
        }
        
        val randomFactor = (random.nextDouble() - 0.5) * 4
        val totalImpact = baseImpact + randomFactor
        
        // Update campaign stats
        val updatedCampaign = campaign.copy(
            speechesGiven = campaign.speechesGiven + 1,
            pollRating = (campaign.pollRating + totalImpact).coerceIn(0.0, 100.0),
            spent = campaign.spent + 10000 // $10k per speech
        )
        
        repository.updateCampaign(updatedCampaign)
        
        // Generate micro-task: Review speech feedback
        val feedbackTask = MicroTask(
            countryId = campaign.countryId,
            taskType = MicroTaskType.REVIEW_REPORT,
            title = "Review Speech Feedback",
            description = "Analyze public response to $location speech",
            category = TaskCategory.POLITICS,
            priority = Priority.LOW,
            relatedMacroId = campaignId,
            status = TaskStatus.PENDING,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            completedDate = null,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000
        )
        repository.insertMicroTask(feedbackTask)
        
        return SpeechResult.SUCCESS(totalImpact, updatedCampaign.pollRating)
    }
    
    /**
     * Organize a rally in a specific area
     */
    suspend fun organizeRally(campaignId: Long, area: RallyArea, promise: String): RallyResult {
        val campaign = repository.getCampaignById(campaignId) ?: return RallyResult.FAILURE
        
        val baseCost = when (area) {
            RallyArea.CAPITAL -> 100000.0
            RallyArea.INDUSTRIAL -> 75000.0
            RallyArea.RURAL -> 50000.0
            RallyArea.UNIVERSITY -> 40000.0
            RallyArea.SUBURBAN -> 60000.0
        }
        
        if (campaign.spent + baseCost > campaign.budget) {
            return RallyResult.BUDGET_EXCEEDED
        }
        
        val attendance = when (area) {
            RallyArea.CAPITAL -> (5000..20000).random()
            RallyArea.INDUSTRIAL -> (3000..15000).random()
            RallyArea.RURAL -> (1000..5000).random()
            RallyArea.UNIVERSITY -> (2000..10000).random()
            RallyArea.SUBURBAN -> (2000..12000).random()
        }
        
        val followerGain = (attendance * 0.3).toInt()
        val pollBoost = (attendance / 1000.0) * 0.5
        
        val updatedCampaign = campaign.copy(
            ralliesHeld = campaign.ralliesHeld + 1,
            followers = campaign.followers + followerGain,
            pollRating = (campaign.pollRating + pollBoost).coerceIn(0.0, 100.0),
            spent = campaign.spent + baseCost,
            promises = campaign.promises + "|$promise"
        )
        
        repository.updateCampaign(updatedCampaign)
        
        // Store promise for later tracking
        storePromise(campaign.countryId, area, promise)
        
        // Generate butterfly effects based on area
        generateRallyButterflyEffects(campaign.countryId, area, promise)
        
        return RallyResult.SUCCESS(attendance, followerGain, pollBoost, baseCost)
    }
    
    /**
     * Manage social media presence
     */
    suspend fun manageSocialMedia(campaignId: Long, action: SocialMediaAction): SocialMediaResult {
        val campaign = repository.getCampaignById(campaignId) ?: return SocialMediaResult.FAILURE
        
        return when (action) {
            SocialMediaAction.POST_POLICY -> {
                val followerGain = (500..5000).random()
                val cost = 0.0 // Free organic post
                val updatedCampaign = campaign.copy(
                    followers = campaign.followers + followerGain,
                    spent = campaign.spent + cost
                )
                repository.updateCampaign(updatedCampaign)
                SocialMediaResult.SUCCESS(followerGain, cost, "Policy post gained $followerGain followers")
            }
            
            SocialMediaAction.LIVE_STREAM -> {
                val followerGain = (1000..10000).random()
                val cost = 5000.0
                val updatedCampaign = campaign.copy(
                    followers = campaign.followers + followerGain,
                    spent = campaign.spent + cost
                )
                repository.updateCampaign(updatedCampaign)
                SocialMediaResult.SUCCESS(followerGain, cost, "Live stream gained $followerGain followers")
            }
            
            SocialMediaAction.RESPOND_CRITIC -> {
                val impact = (random.nextDouble() - 0.5) * 10
                val updatedCampaign = campaign.copy(
                    pollRating = (campaign.pollRating + impact).coerceIn(0.0, 100.0)
                )
                repository.updateCampaign(updatedCampaign)
                SocialMediaResult.SUCCESS(0, 0.0, if (impact > 0) "Effective response" else "Backlash occurred")
            }
            
            SocialMediaAction.VIRAL_CAMPAIGN -> {
                val followerGain = (10000..100000).random()
                val cost = 50000.0
                val updatedCampaign = campaign.copy(
                    followers = campaign.followers + followerGain,
                    spent = campaign.spent + cost
                )
                repository.updateCampaign(updatedCampaign)
                SocialMediaResult.SUCCESS(followerGain, cost, "Viral campaign gained $followerGain followers")
            }
        }
    }
    
    /**
     * Buy advertising slots
     */
    suspend fun buyAdSlot(campaignId: Long, slotType: AdSlotType, duration: Int): AdResult {
        val campaign = repository.getCampaignById(campaignId) ?: return AdResult.FAILURE
        
        val costPerSlot = when (slotType) {
            AdSlotType.RADIO_MORNING -> 15000.0
            AdSlotType.RADIO_EVENING -> 20000.0
            AdSlotType.TV_LOCAL -> 50000.0
            AdSlotType.TV_NATIONAL -> 200000.0
            AdSlotType.SOCIAL_MEDIA -> 25000.0
            AdSlotType.NEWSPAPER -> 30000.0
        }
        
        val totalCost = costPerSlot * duration
        
        if (campaign.spent + totalCost > campaign.budget) {
            return AdResult.BUDGET_EXCEEDED
        }
        
        val reach = when (slotType) {
            AdSlotType.RADIO_MORNING -> (10000..50000).random() * duration
            AdSlotType.RADIO_EVENING -> (15000..75000).random() * duration
            AdSlotType.TV_LOCAL -> (50000..200000).random() * duration
            AdSlotType.TV_NATIONAL -> (500000..2000000).random() * duration
            AdSlotType.SOCIAL_MEDIA -> (100000..500000).random() * duration
            AdSlotType.NEWSPAPER -> (20000..100000).random() * duration
        }
        
        val followerGain = (reach * 0.05).toInt()
        
        val updatedCampaign = campaign.copy(
            followers = campaign.followers + followerGain,
            spent = campaign.spent + totalCost
        )
        
        repository.updateCampaign(updatedCampaign)
        
        return AdResult.SUCCESS(reach, followerGain, totalCost, slotType)
    }
    
    /**
     * Conduct opposition research
     */
    suspend fun conductOppositionResearch(campaignId: Long, target: String): OppositionResearchResult {
        val campaign = repository.getCampaignById(campaignId) ?: return OppositionResearchResult.FAILURE
        
        val researchCost = 100000.0
        
        if (campaign.spent + researchCost > campaign.budget) {
            return OppositionResearchResult.BUDGET_EXCEEDED
        }
        
        // 60% chance to find something
        val foundDirt = random.nextDouble() < 0.6
        
        val scandalSeverity = if (foundDirt) {
            (1..10).random()
        } else {
            0
        }
        
        val updatedCampaign = campaign.copy(
            spent = campaign.spent + researchCost
        )
        
        repository.updateCampaign(updatedCampaign)
        
        if (foundDirt && scandalSeverity >= 5) {
            // Generate scandal for opponent
            val opponentScandal = Scandal(
                countryId = campaign.countryId,
                title = "Opposition $target Scandal",
                description = "Damaging information revealed about $target",
                severity = scandalSeverity,
                type = ScandalType.entries.random(),
                involvedPersons = "[\"$target\"]",
                dateExposed = System.currentTimeMillis(),
                impactOnApproval = -scandalSeverity.toDouble()
            )
            repository.insertScandal(opponentScandal)
            
            return OppositionResearchResult.SUCCESS(scandalSeverity, researchCost, true)
        }
        
        return OppositionResearchResult.SUCCESS(0, researchCost, false)
    }
    
    /**
     * Respond to a scandal
     */
    suspend fun respondToScandal(scandalId: Long, response: ScandalResponseStrategy): ScandalResponseResult {
        val scandal = repository.scandalDao().getScandalById(scandalId) 
            ?: return ScandalResponseResult.FAILURE
        
        val effectiveness = when (response) {
            ScandalResponseStrategy.DENY -> {
                if (scandal.severity <= 4) 0.7 else 0.3
            }
            ScandalResponseStrategy.APOLOGIZE -> {
                if (scandal.severity >= 6) 0.6 else 0.4
            }
            ScandalResponseStrategy.ATTACK -> {
                if (random.nextDouble() < 0.5) 0.6 else 0.2
            }
            ScandalResponseStrategy.IGNORE -> 0.3
            ScandalResponseStrategy.INVESTIGATE -> 0.5
            ScandalResponseStrategy.DISTRACT -> {
                if (random.nextDouble() < 0.4) 0.5 else 0.3
            }
        }
        
        val success = random.nextDouble() < effectiveness
        val resolutionDate = if (success) System.currentTimeMillis() else null
        
        val impactReduction = if (success) {
            scandal.impactOnApproval * 0.5
        } else {
            scandal.impactOnApproval * 1.2 // Makes it worse
        }
        
        val updatedScandal = scandal.copy(
            resolved = success,
            resolutionDate = resolutionDate,
            impactOnApproval = impactReduction,
            responseStrategy = response.name
        )
        
        repository.updateScandal(updatedScandal)
        
        // Generate micro-task: Follow up on scandal response
        if (!success) {
            val followUpTask = MicroTask(
                countryId = scandal.countryId,
                taskType = MicroTaskType.RESPOND_TO_CRISIS,
                title = "Scandal Escalating",
                description = "The ${scandal.title} scandal is getting worse. Take additional action.",
                category = TaskCategory.POLITICS,
                priority = Priority.URGENT,
                relatedMacroId = scandalId,
                status = TaskStatus.PENDING,
                relatedEntityId = null,
                relatedEntityType = null,
                selectedOption = null,
                completedDate = null,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 12 * 60 * 60 * 1000
            )
            repository.insertMicroTask(followUpTask)
        }
        
        return if (success) {
            ScandalResponseResult.RESOLVED
        } else {
            ScandalResponseResult.ESCALATED
        }
    }
    
    /**
     * Participate in opinion poll
     */
    suspend fun participateInPoll(campaignId: Long): PollResult {
        val campaign = repository.getCampaignById(campaignId) ?: return PollResult.FAILURE
        
        val sampleSize = (800..1500).random()
        val marginOfError = (2.5..4.0).random()
        
        // Calculate poll results
        val baseSupport = campaign.pollRating
        val randomVariance = (random.nextDouble() - 0.5) * marginOfError * 2
        val pollRating = (baseSupport + randomVariance).coerceIn(0.0, 100.0)
        
        val poll = OpinionPoll(
            countryId = campaign.countryId,
            pollDate = System.currentTimeMillis(),
            sampleSize = sampleSize,
            marginOfError = marginOfError,
            approvalRating = pollRating,
            disapprovalRating = (100 - pollRating) * (0.7 + random.nextDouble() * 0.3),
            undecidedPercentage = (100 - pollRating - ((100 - pollRating) * 0.85)).coerceIn(0.0, 100.0),
            topIssues = """["Economy", "Healthcare", "Education", "Security", "Environment"]""",
            demographicBreakdown = generateDemographicBreakdown(pollRating)
        )
        
        // Store poll (would use PollDao)
        
        // Generate micro-task: Review poll results
        val reviewTask = MicroTask(
            countryId = campaign.countryId,
            taskType = MicroTaskType.REVIEW_REPORT,
            title = "Review Latest Poll",
            description = "Analyze new polling data and adjust strategy",
            category = TaskCategory.POLITICS,
            priority = Priority.NORMAL,
            relatedMacroId = campaignId,
            status = TaskStatus.PENDING,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            completedDate = null,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000
        )
        repository.insertMicroTask(reviewTask)
        
        return PollResult.SUCCESS(pollRating, sampleSize, marginOfError, poll)
    }
    
    /**
     * Design campaign poster/logo
     */
    suspend fun designCampaignMaterial(campaignId: Long, materialType: MaterialType, design: String): MaterialResult {
        val campaign = repository.getCampaignById(campaignId) ?: return MaterialResult.FAILURE
        
        val cost = when (materialType) {
            MaterialType.POSTER -> 5000.0
            MaterialType.LOGO -> 10000.0
            MaterialType.BROCHURE -> 15000.0
            MaterialType.BANNER -> 3000.0
            MaterialType.VIDEO -> 50000.0
        }
        
        if (campaign.spent + cost > campaign.budget) {
            return MaterialResult.BUDGET_EXCEEDED
        }
        
        // Quality rating based on design choice (simplified)
        val qualityRating = (50..100).random().toDouble()
        
        val effectiveness = when {
            qualityRating >= 90 -> 1.5
            qualityRating >= 70 -> 1.2
            else -> 1.0
        }
        
        val updatedCampaign = campaign.copy(
            spent = campaign.spent + cost
        )
        
        repository.updateCampaign(updatedCampaign)
        
        return MaterialResult.SUCCESS(materialType, cost, qualityRating, effectiveness)
    }
    
    // Helper functions
    
    private fun storePromise(countryId: Long, area: RallyArea, promise: String) {
        // Store promise for later tracking and fulfillment checking
        // This would be stored in a Promises table
    }
    
    private fun generateRallyButterflyEffects(countryId: Long, area: RallyArea, promise: String) {
        // Generate butterfly effects based on rally location and promises made
        val effects = mutableListOf<ButterflyEffect>()
        
        when (area) {
            RallyArea.INDUSTRIAL -> {
                effects.add(ButterflyEffect(
                    countryId = countryId,
                    sourceTaskId = 0,
                    sourceTaskType = MicroTaskType.GIVE_SPEECH,
                    effectType = EffectType.DELAYED,
                    targetEntity = "businesses",
                    targetEntityId = null,
                    effectDescription = "Industrial area rally boosts local business confidence",
                    effectMagnitude = 5.0,
                    delayDays = 30,
                    triggeredDate = null,
                expiryDate = null,
                chainLevel = 1
                ))
            }
            RallyArea.RURAL -> {
                effects.add(ButterflyEffect(
                    countryId = countryId,
                    sourceTaskId = 0,
                    sourceTaskType = MicroTaskType.GIVE_SPEECH,
                    effectType = EffectType.DELAYED,
                    targetEntity = "demographics",
                    targetEntityId = null,
                    effectDescription = "Rural rally affects agricultural policy expectations",
                    effectMagnitude = 3.0,
                    delayDays = 60,
                    triggeredDate = null,
                expiryDate = null,
                chainLevel = 1
                ))
            }
            RallyArea.UNIVERSITY -> {
                effects.add(ButterflyEffect(
                    countryId = countryId,
                    sourceTaskId = 0,
                    sourceTaskType = MicroTaskType.GIVE_SPEECH,
                    effectType = EffectType.DELAYED,
                    targetEntity = "npcs",
                    targetEntityId = null,
                    effectDescription = "University rally influences youth vote",
                    effectMagnitude = 4.0,
                    delayDays = 90,
                    triggeredDate = null,
                expiryDate = null,
                chainLevel = 1
                ))
            }
            else -> {}
        }
        
        effects.forEach { effect ->
            // Would insert via ButterflyEffectDao
        }
    }
    
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

// Enums for campaign actions

enum class SpeechType {
    ECONOMIC,
    SOCIAL,
    FOREIGN,
    RALLY,
    TOWN_HALL
}

enum class RallyArea {
    CAPITAL,
    INDUSTRIAL,
    RURAL,
    UNIVERSITY,
    SUBURBAN
}

enum class SocialMediaAction {
    POST_POLICY,
    LIVE_STREAM,
    RESPOND_CRITIC,
    VIRAL_CAMPAIGN
}

enum class AdSlotType {
    RADIO_MORNING,
    RADIO_EVENING,
    TV_LOCAL,
    TV_NATIONAL,
    SOCIAL_MEDIA,
    NEWSPAPER
}

enum class MaterialType {
    POSTER,
    LOGO,
    BROCHURE,
    BANNER,
    VIDEO
}

// Result classes

sealed class SpeechResult {
    object FAILURE : SpeechResult()
    data class SUCCESS(val impact: Double, val newPollRating: Double) : SpeechResult()
}

sealed class RallyResult {
    object FAILURE : RallyResult()
    object BUDGET_EXCEEDED : RallyResult()
    data class SUCCESS(val attendance: Int, val followerGain: Int, val pollBoost: Double, val cost: Double) : RallyResult()
}

sealed class SocialMediaResult {
    object FAILURE : SocialMediaResult()
    data class SUCCESS(val followerGain: Int, val cost: Double, val message: String) : SocialMediaResult()
}

sealed class AdResult {
    object FAILURE : AdResult()
    object BUDGET_EXCEEDED : AdResult()
    data class SUCCESS(val reach: Int, val followerGain: Int, val cost: Double, val slotType: AdSlotType) : AdResult()
}

sealed class OppositionResearchResult {
    object FAILURE : OppositionResearchResult()
    object BUDGET_EXCEEDED : OppositionResearchResult()
    data class SUCCESS(val severity: Int, val cost: Double, val foundDirt: Boolean) : OppositionResearchResult()
}

sealed class ScandalResponseResult {
    object FAILURE : ScandalResponseResult()
    object RESOLVED : ScandalResponseResult()
    object ESCALATED : ScandalResponseResult()
}

sealed class PollResult {
    object FAILURE : PollResult()
    data class SUCCESS(val pollRating: Double, val sampleSize: Int, val marginOfError: Double, val poll: OpinionPoll) : PollResult()
}

sealed class MaterialResult {
    object FAILURE : MaterialResult()
    object BUDGET_EXCEEDED : MaterialResult()
    data class SUCCESS(val type: MaterialType, val cost: Double, val qualityRating: Double, val effectiveness: Double) : MaterialResult()
}
