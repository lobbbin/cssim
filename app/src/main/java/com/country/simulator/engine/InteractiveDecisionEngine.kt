package com.country.simulator.engine

import com.country.simulator.model.*
import com.country.simulator.repositories.GameRepository
import kotlinx.coroutines.flow.first
import kotlin.random.Random

/**
 * Interactive Decision Engine
 * Handles all player choices with immediate and delayed consequences
 */
class InteractiveDecisionEngine(private val repository: GameRepository) {
    
    private val random = Random
    
    /**
     * Make a decision with full consequence tracking
     */
    suspend fun makeDecision(
        countryId: Long,
        decisionType: DecisionType,
        selectedOption: String,
        context: Map<String, Any> = emptyMap()
    ): DecisionResult {
        return when (decisionType) {
            DecisionType.BUDGET_CUT -> handleBudgetCut(countryId, selectedOption, context)
            DecisionType.TAX_CHANGE -> handleTaxChange(countryId, selectedOption, context)
            DecisionType.PERMIT_APPROVAL -> handlePermitApproval(countryId, selectedOption, context)
            DecisionType.DIPLOMATIC_RESPONSE -> handleDiplomaticResponse(countryId, selectedOption, context)
            DecisionType.CRISIS_RESPONSE -> handleCrisisResponse(countryId, selectedOption, context)
            DecisionType.SOCIAL_PROGRAM -> handleSocialProgram(countryId, selectedOption, context)
            DecisionType.MILITARY_ACTION -> handleMilitaryAction(countryId, selectedOption, context)
            DecisionType.ECONOMIC_POLICY -> handleEconomicPolicy(countryId, selectedOption, context)
        }
    }
    
    /**
     * Handle budget cut decisions
     */
    private suspend fun handleBudgetCut(
        countryId: Long,
        option: String,
        context: Map<String, Any>
    ): DecisionResult {
        val ministry = context["ministry"] as? String ?: return DecisionResult.FAILURE
        val amount = context["amount"] as? Double ?: return DecisionResult.FAILURE
        
        val playerCountry = repository.getPlayerCountrySync() ?: return DecisionResult.FAILURE
        
        // Calculate consequences
        val approvalChange = when (option) {
            "Deep Cuts (20%)" -> -8.0
            "Moderate Cuts (10%)" -> -4.0
            "Light Cuts (5%)" -> -2.0
            "No Cuts" -> 0.0
            else -> 0.0
        }
        
        val stabilityChange = when (option) {
            "Deep Cuts (20%)" -> -5.0
            "Moderate Cuts (10%)" -> -2.0
            "Light Cuts (5%)" -> -1.0
            "No Cuts" -> 0.0
            else -> 0.0
        }
        
        val budgetSavings = when (option) {
            "Deep Cuts (20%)" -> amount * 0.20
            "Moderate Cuts (10%)" -> amount * 0.10
            "Light Cuts (5%)" -> amount * 0.05
            "No Cuts" -> 0.0
            else -> 0.0
        }
        
        // Update player country
        val updatedCountry = playerCountry.copy(
            approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
            stability = (playerCountry.stability + stabilityChange).coerceIn(0.0, 100.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
        
        // Generate butterfly effects
        val effects = mutableListOf<ButterflyEffect>()
        
        if (option.contains("Deep")) {
            effects.add(ButterflyEffect(
                countryId = countryId,
                sourceTaskId = 0,
                sourceTaskType = MicroTaskType.REVIEW_BUDGET,
                effectType = EffectType.DELAYED,
                targetEntity = "npcs",
                targetEntityId = null,
                effectDescription = "Budget cuts cause protests",
                effectMagnitude = -5.0,
                delayDays = 14,
                triggeredDate = null,
                expiryDate = null,
                chainLevel = 1
            ))
            
            effects.add(ButterflyEffect(
                countryId = countryId,
                sourceTaskId = 0,
                sourceTaskType = MicroTaskType.REVIEW_BUDGET,
                effectType = EffectType.DELAYED,
                targetEntity = "ministries",
                targetEntityId = null,
                effectDescription = "Ministry efficiency drops",
                effectMagnitude = -10.0,
                delayDays = 30,
                triggeredDate = null,
                expiryDate = null,
                chainLevel = 1
            ))
        }
        
        effects.forEach { repository.insertButterflyEffect(it) }
        
        // Generate follow-up task
        if (option.contains("Cuts")) {
            val followUpTask = MicroTask(
                countryId = countryId,
                taskType = MicroTaskType.RESPOND_TO_CRISIS,
                title = "Handle Budget Cut Backlash",
                description = "$ministry faces backlash from budget cuts",
                category = TaskCategory.POLITICS,
                priority = Priority.NORMAL,
                status = TaskStatus.PENDING,
                relatedEntityId = null,
                relatedEntityType = null,
                selectedOption = null,
                completedDate = null,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
            )
            repository.insertMicroTask(followUpTask)
        }
        
        return DecisionResult.SUCCESS(
            immediateEffects = mapOf(
                "approval" to approvalChange,
                "stability" to stabilityChange,
                "savings" to budgetSavings
            ),
            delayedEffects = effects.size,
            message = "Budget cuts approved. Expect political fallout."
        )
    }
    
    /**
     * Handle tax change decisions
     */
    private suspend fun handleTaxChange(
        countryId: Long,
        option: String,
        context: Map<String, Any>
    ): DecisionResult {
        val bracket = context["bracket"] as? String ?: return DecisionResult.FAILURE
        val currentRate = context["currentRate"] as? Double ?: return DecisionResult.FAILURE
        
        val playerCountry = repository.getPlayerCountrySync() ?: return DecisionResult.FAILURE
        
        val newRate = when (option) {
            "Increase by 5%" -> currentRate + 5.0
            "Increase by 2%" -> currentRate + 2.0
            "Maintain" -> currentRate
            "Decrease by 2%" -> (currentRate - 2.0).coerceAtLeast(0.0)
            "Decrease by 5%" -> (currentRate - 5.0).coerceAtLeast(0.0)
            else -> currentRate
        }
        
        // Calculate economic impact
        val revenueChange = when {
            newRate > currentRate -> (newRate - currentRate) * 1_000_000_000
            newRate < currentRate -> (newRate - currentRate) * 1_000_000_000
            else -> 0.0
        }
        
        val approvalChange = when {
            newRate > currentRate -> -5.0 // People hate tax increases
            newRate < currentRate -> 3.0 // People like tax cuts
            else -> 0.0
        }
        
        val classFaithChange = when (bracket.lowercase()) {
            "wealthy", "high income" -> if (newRate > currentRate) 10.0 else -5.0
            "middle", "lower middle" -> if (newRate > currentRate) 8.0 else -3.0
            "basic", "low" -> if (newRate > currentRate) 15.0 else -8.0
            else -> 0.0
        }
        
        val updatedCountry = playerCountry.copy(
            approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
        
        // Update tax bracket in database
        val brackets = repository.getTaxBrackets(countryId).first()
        val targetBracket = brackets.find { it.bracketName == bracket }
        if (targetBracket != null) {
            repository.updateTaxBracket(targetBracket.copy(taxRate = newRate))
        }
        
        return DecisionResult.SUCCESS(
            immediateEffects = mapOf(
                "approval" to approvalChange,
                "revenue" to revenueChange,
                "classFaith" to classFaithChange,
                "newRate" to newRate
            ),
            delayedEffects = 0,
            message = "Tax rate updated to $newRate%"
        )
    }
    
    /**
     * Handle permit approval decisions
     */
    private suspend fun handlePermitApproval(
        countryId: Long,
        option: String,
        context: Map<String, Any>
    ): DecisionResult {
        val permitType = context["type"] as? String ?: return DecisionResult.FAILURE
        val permitId = context["id"] as? Long ?: return DecisionResult.FAILURE
        
        val playerCountry = repository.getPlayerCountrySync() ?: return DecisionResult.FAILURE
        
        val result = when (option) {
            "Approve" -> {
                // Economic boost, potential environmental cost
                val economicBoost = random.nextDouble(1.0, 5.0)
                val environmentalCost = random.nextDouble(0.0, 10.0)
                
                val updatedCountry = playerCountry.copy(
                    growthRate = (playerCountry.growthRate + economicBoost * 0.1).coerceIn(-5.0, 10.0),
                    environmentalRating = (playerCountry.environmentalRating - environmentalCost * 0.5).coerceIn(0.0, 100.0),
                    lastUpdated = System.currentTimeMillis()
                )
                repository.updatePlayerCountry(updatedCountry)
                
                DecisionResult.SUCCESS(
                    immediateEffects = mapOf(
                        "growth" to economicBoost,
                        "environment" to -environmentalCost
                    ),
                    delayedEffects = if (environmentalCost > 5.0) 1 else 0,
                    message = "Permit approved. Expect economic growth."
                )
            }
            "Reject" -> {
                // Environmental groups happy, businesses unhappy
                val approvalChange = random.nextDouble(-2.0, 2.0)
                
                val updatedCountry = playerCountry.copy(
                    approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
                    lastUpdated = System.currentTimeMillis()
                )
                repository.updatePlayerCountry(updatedCountry)
                
                DecisionResult.SUCCESS(
                    immediateEffects = mapOf(
                        "approval" to approvalChange
                    ),
                    delayedEffects = 0,
                    message = "Permit rejected."
                )
            }
            "Approve with Conditions" -> {
                // Balanced approach
                val economicBoost = random.nextDouble(0.5, 2.0)
                val environmentalCost = random.nextDouble(0.0, 3.0)
                
                val updatedCountry = playerCountry.copy(
                    growthRate = (playerCountry.growthRate + economicBoost * 0.05).coerceIn(-5.0, 10.0),
                    environmentalRating = (playerCountry.environmentalRating - environmentalCost * 0.2).coerceIn(0.0, 100.0),
                    lastUpdated = System.currentTimeMillis()
                )
                repository.updatePlayerCountry(updatedCountry)
                
                DecisionResult.SUCCESS(
                    immediateEffects = mapOf(
                        "growth" to economicBoost * 0.5,
                        "environment" to -environmentalCost * 0.5
                    ),
                    delayedEffects = 0,
                    message = "Permit approved with environmental conditions."
                )
            }
            else -> DecisionResult.FAILURE
        }
        
        // Generate follow-up inspection task
        if (option.contains("Approve")) {
            val inspectionTask = MicroTask(
                countryId = countryId,
                taskType = MicroTaskType.REVIEW_REPORT,
                title = "Inspect Permit Compliance",
                description = "Verify compliance with $permitType permit conditions",
                category = TaskCategory.ECONOMY,
                priority = Priority.LOW,
                status = TaskStatus.PENDING,
                relatedEntityId = null,
                relatedEntityType = null,
                selectedOption = null,
                completedDate = null,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 90 * 24 * 60 * 60 * 1000
            )
            repository.insertMicroTask(inspectionTask)
        }
        
        return result
    }
    
    /**
     * Handle diplomatic response decisions
     */
    private suspend fun handleDiplomaticResponse(
        countryId: Long,
        option: String,
        context: Map<String, Any>
    ): DecisionResult {
        val foreignCountryId = context["foreignCountryId"] as? Long ?: return DecisionResult.FAILURE
        
        val playerCountry = repository.getPlayerCountrySync() ?: return DecisionResult.FAILURE
        
        // Get diplomatic relation
        val relations = repository.getRelations(countryId).first()
        val relation = relations.find { it.foreignCountryId == foreignCountryId }
            ?: return DecisionResult.FAILURE
        
        val relationChange = when (option) {
            "Strong Protest" -> -15
            "Diplomatic Protest" -> -5
            "Express Concern" -> -2
            "Seek Dialogue" -> 5
            "Offer Compromise" -> 10
            "Ignore" -> 0
            "Retaliate" -> -20
            else -> 0
        }
        
        val updatedRelation = relation.copy(
            relationLevel = (relation.relationLevel + relationChange).coerceIn(-100, 100),
            trustLevel = ((relation.trustLevel + relationChange / 2).coerceIn(0, 100)),
            lastContact = System.currentTimeMillis()
        )
        repository.updateDiplomaticRelation(updatedRelation)
        
        // Calculate trade impact
        val tradeImpact = relationChange * 0.5 // 1 relation point = 0.5% trade
        
        // Generate butterfly effects
        val effects = mutableListOf<ButterflyEffect>()
        
        if (relationChange < -10) {
            effects.add(ButterflyEffect(
                countryId = countryId,
                sourceTaskId = 0,
                sourceTaskType = MicroTaskType.MAKE_CALL,
                effectType = EffectType.DELAYED,
                targetEntity = "trade_deals",
                targetEntityId = null,
                effectDescription = "Diplomatic tension affects trade",
                effectMagnitude = tradeImpact,
                delayDays = 30,
                triggeredDate = null,
                expiryDate = null,
                chainLevel = 1
            ))
        }
        
        effects.forEach { repository.insertButterflyEffect(it) }
        
        return DecisionResult.SUCCESS(
            immediateEffects = mapOf(
                "relationChange" to relationChange,
                "newRelation" to updatedRelation.relationLevel,
                "tradeImpact" to tradeImpact
            ),
            delayedEffects = effects.size,
            message = "Diplomatic stance updated. Relation: ${updatedRelation.relationLevel}"
        )
    }
    
    /**
     * Handle crisis response decisions
     */
    private suspend fun handleCrisisResponse(
        countryId: Long,
        option: String,
        context: Map<String, Any>
    ): DecisionResult {
        val crisisType = context["crisisType"] as? String ?: return DecisionResult.FAILURE
        
        val playerCountry = repository.getPlayerCountrySync() ?: return DecisionResult.FAILURE
        
        val result = when {
            crisisType.contains("Economic") -> handleEconomicCrisis(countryId, option, playerCountry)
            crisisType.contains("Health") -> handleHealthCrisis(countryId, option, playerCountry)
            crisisType.contains("Disaster") -> handleDisasterCrisis(countryId, option, playerCountry)
            crisisType.contains("Political") -> handlePoliticalCrisis(countryId, option, playerCountry)
            crisisType.contains("Security") -> handleSecurityCrisis(countryId, option, playerCountry)
            else -> DecisionResult.FAILURE
        }
        
        return result
    }
    
    private suspend fun handleEconomicCrisis(
        countryId: Long,
        option: String,
        playerCountry: PlayerCountry
    ): DecisionResult {
        val (approvalChange, stabilityChange, economicChange, cost) = when (option) {
            "Bailout" -> 5.0 to 10.0 to -50_000_000_000.0
            "Let Market Correct" -> -10.0 to -15.0 to 0.0
            "Regulate" -> 3.0 to 5.0 to -10_000_000_000.0
            "Nationalize" -> 8.0 to -5.0 to -100_000_000_000.0
            else -> 0.0 to 0.0 to 0.0
        }
        
        val updatedCountry = playerCountry.copy(
            approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
            stability = (playerCountry.stability + stabilityChange).coerceIn(0.0, 100.0),
            treasuryBalance = playerCountry.let { (it as? dynamic).treasuryBalance ?: 0.0 } + economicChange,
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
        
        return DecisionResult.SUCCESS(
            immediateEffects = mapOf(
                "approval" to approvalChange,
                "stability" to stabilityChange,
                "cost" to economicChange
            ),
            delayedEffects = 0,
            message = "Crisis response implemented"
        )
    }
    
    private suspend fun handleHealthCrisis(
        countryId: Long,
        option: String,
        playerCountry: PlayerCountry
    ): DecisionResult {
        val (approvalChange, healthChange, cost) = when (option) {
            "Lockdown" -> -5.0 to 20.0 to -5_000_000_000.0
            "Vaccination Campaign" -> 10.0 to 15.0 to -10_000_000_000.0
            "Public Information" -> 5.0 to 5.0 to -1_000_000_000.0
            "Do Nothing" -> -20.0 to -30.0 to 0.0
            else -> 0.0 to 0.0 to 0.0
        }
        
        val updatedCountry = playerCountry.copy(
            approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
            healthIndex = (playerCountry.healthIndex + healthChange).coerceIn(0.0, 100.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
        
        return DecisionResult.SUCCESS(
            immediateEffects = mapOf(
                "approval" to approvalChange,
                "health" to healthChange,
                "cost" to cost
            ),
            delayedEffects = 0,
            message = "Health crisis response implemented"
        )
    }
    
    private suspend fun handleDisasterCrisis(
        countryId: Long,
        option: String,
        playerCountry: PlayerCountry
    ): DecisionResult {
        val (approvalChange, stabilityChange, cost) = when (option) {
            "Emergency Response" -> 10.0 to 5.0 to -20_000_000_000.0
            "Military Deployment" -> 5.0 to 10.0 to -30_000_000_000.0
            "International Aid" -> 8.0 to 3.0 to -5_000_000_000.0
            "Wait and See" -> -15.0 to -20.0 to 0.0
            else -> 0.0 to 0.0 to 0.0
        }
        
        val updatedCountry = playerCountry.copy(
            approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
            stability = (playerCountry.stability + stabilityChange).coerceIn(0.0, 100.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
        
        return DecisionResult.SUCCESS(
            immediateEffects = mapOf(
                "approval" to approvalChange,
                "stability" to stabilityChange,
                "cost" to cost
            ),
            delayedEffects = 0,
            message = "Disaster response implemented"
        )
    }
    
    private suspend fun handlePoliticalCrisis(
        countryId: Long,
        option: String,
        playerCountry: PlayerCountry
    ): DecisionResult {
        val (approvalChange, stabilityChange) = when (option) {
            "Resignation" -> 10.0 to 15.0
            "Investigation" -> 5.0 to 5.0
            "Deny" -> -5.0 to -10.0
            "Attack Accusers" -> -10.0 to -15.0
            else -> 0.0 to 0.0
        }
        
        val updatedCountry = playerCountry.copy(
            approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
            stability = (playerCountry.stability + stabilityChange).coerceIn(0.0, 100.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
        
        return DecisionResult.SUCCESS(
            immediateEffects = mapOf(
                "approval" to approvalChange,
                "stability" to stabilityChange
            ),
            delayedEffects = 0,
            message = "Political crisis response implemented"
        )
    }
    
    private suspend fun handleSecurityCrisis(
        countryId: Long,
        option: String,
        playerCountry: PlayerCountry
    ): DecisionResult {
        val (approvalChange, stabilityChange, cost) = when (option) {
            "Military Action" -> 5.0 to 10.0 to -50_000_000_000.0
            "Diplomatic Solution" -> 3.0 to 5.0 to -1_000_000_000.0
            "Sanctions" -> 0.0 to 3.0 to -10_000_000_000.0
            "Do Nothing" -> -20.0 to -30.0 to 0.0
            else -> 0.0 to 0.0 to 0.0
        }
        
        val updatedCountry = playerCountry.copy(
            approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
            stability = (playerCountry.stability + stabilityChange).coerceIn(0.0, 100.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
        
        return DecisionResult.SUCCESS(
            immediateEffects = mapOf(
                "approval" to approvalChange,
                "stability" to stabilityChange,
                "cost" to cost
            ),
            delayedEffects = 0,
            message = "Security crisis response implemented"
        )
    }
    
    /**
     * Handle social program decisions
     */
    private suspend fun handleSocialProgram(
        countryId: Long,
        option: String,
        context: Map<String, Any>
    ): DecisionResult {
        val programType = context["programType"] as? String ?: return DecisionResult.FAILURE
        val budget = context["budget"] as? Double ?: return DecisionResult.FAILURE
        
        val playerCountry = repository.getPlayerCountrySync() ?: return DecisionResult.FAILURE
        
        val (approvalChange, happinessChange, cost) = when (option) {
            "Full Funding" -> 5.0 to 8.0 to budget
            "Partial Funding" -> 3.0 to 4.0 to budget * 0.5
            "Minimal Funding" -> 1.0 to 1.0 to budget * 0.2
            "Cut Program" -> -10.0 to -15.0 to -budget
            else -> 0.0 to 0.0 to 0.0
        }
        
        val updatedCountry = playerCountry.copy(
            approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
            happinessIndex = (playerCountry.happinessIndex + happinessChange).coerceIn(0.0, 100.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
        
        return DecisionResult.SUCCESS(
            immediateEffects = mapOf(
                "approval" to approvalChange,
                "happiness" to happinessChange,
                "cost" to cost
            ),
            delayedEffects = 0,
            message = "Social program funding updated"
        )
    }
    
    /**
     * Handle military action decisions
     */
    private suspend fun handleMilitaryAction(
        countryId: Long,
        option: String,
        context: Map<String, Any>
    ): DecisionResult {
        val targetCountry = context["targetCountry"] as? String ?: return DecisionResult.FAILURE
        
        val playerCountry = repository.getPlayerCountrySync() ?: return DecisionResult.FAILURE
        
        val (approvalChange, stabilityChange, cost, casualties) = when (option) {
            "Full Invasion" -> 10.0 to -10.0 to -100_000_000_000.0 to random.nextInt(1000, 10000)
            "Limited Strike" -> 5.0 to 0.0 to -20_000_000_000.0 to random.nextInt(10, 500)
            "Drone Strike" -> 3.0 to 2.0 to -1_000_000_000.0 to random.nextInt(0, 50)
            "Special Forces" -> 2.0 to 3.0 to -5_000_000_000.0 to random.nextInt(0, 100)
            "Withdraw" -> -5.0 to 5.0 to -5_000_000_000.0 to 0
            else -> 0.0 to 0.0 to 0.0 to 0
        }
        
        val updatedCountry = playerCountry.copy(
            approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
            stability = (playerCountry.stability + stabilityChange).coerceIn(0.0, 100.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
        
        // Generate war-related micro tasks
        if (option.contains("Invasion") || option.contains("Strike")) {
            val tasks = listOf(
                MicroTask(
                    countryId = countryId,
                    taskType = MicroTaskType.GIVE_SPEECH,
                    title = "Address Nation on Military Action",
                    description = "Explain military action against $targetCountry",
                    category = TaskCategory.DEFENSE,
                    priority = Priority.URGENT,
                    status = TaskStatus.PENDING,
                    relatedEntityId = null,
                    relatedEntityType = null,
                    selectedOption = null,
                    completedDate = null,
                    createdDate = System.currentTimeMillis(),
                    dueDate = System.currentTimeMillis() + 6 * 60 * 60 * 1000
                ),
                MicroTask(
                    countryId = countryId,
                    taskType = MicroTaskType.MAKE_CALL,
                    title = "Contact Allied Leaders",
                    description = "Coordinate with allies on $targetCountry action",
                    category = TaskCategory.DIPLOMACY,
                    priority = Priority.HIGH,
                    status = TaskStatus.PENDING,
                    relatedEntityId = null,
                    relatedEntityType = null,
                    selectedOption = null,
                    completedDate = null,
                    createdDate = System.currentTimeMillis(),
                    dueDate = System.currentTimeMillis() + 12 * 60 * 60 * 1000
                )
            )
            tasks.forEach { repository.insertMicroTask(it) }
        }
        
        return DecisionResult.SUCCESS(
            immediateEffects = mapOf(
                "approval" to approvalChange,
                "stability" to stabilityChange,
                "cost" to cost,
                "casualties" to casualties
            ),
            delayedEffects = if (option.contains("Invasion")) 5 else 0,
            message = "Military action executed"
        )
    }
    
    /**
     * Handle economic policy decisions
     */
    private suspend fun handleEconomicPolicy(
        countryId: Long,
        option: String,
        context: Map<String, Any>
    ): DecisionResult {
        val policyType = context["policyType"] as? String ?: return DecisionResult.FAILURE
        
        val playerCountry = repository.getPlayerCountrySync() ?: return DecisionResult.FAILURE
        
        val (approvalChange, growthChange, inflationChange) = when (option) {
            "Stimulus Package" -> 8.0 to 3.0 to 2.0
            "Austerity Measures" -> -10.0 to -2.0 to -1.0
            "Tax Cuts" -> 5.0 to 2.0 to 1.0
            "Regulation" -> 3.0 to -1.0 to 0.0
            "Deregulation" -> 2.0 to 1.0 to 1.0
            "Status Quo" -> 0.0 to 0.0 to 0.0
            else -> 0.0 to 0.0 to 0.0
        }
        
        val updatedCountry = playerCountry.copy(
            approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
            growthRate = (playerCountry.growthRate + growthChange).coerceIn(-5.0, 10.0),
            inflationRate = (playerCountry.inflationRate + inflationChange).coerceIn(0.0, 50.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.updatePlayerCountry(updatedCountry)
        
        return DecisionResult.SUCCESS(
            immediateEffects = mapOf(
                "approval" to approvalChange,
                "growth" to growthChange,
                "inflation" to inflationChange
            ),
            delayedEffects = 0,
            message = "Economic policy implemented"
        )
    }
}

enum class DecisionType {
    BUDGET_CUT,
    TAX_CHANGE,
    PERMIT_APPROVAL,
    DIPLOMATIC_RESPONSE,
    CRISIS_RESPONSE,
    SOCIAL_PROGRAM,
    MILITARY_ACTION,
    ECONOMIC_POLICY
}

sealed class DecisionResult {
    object FAILURE : DecisionResult()
    data class SUCCESS(
        val immediateEffects: Map<String, Any>,
        val delayedEffects: Int,
        val message: String
    ) : DecisionResult()
}
