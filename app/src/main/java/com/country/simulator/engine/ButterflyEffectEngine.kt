package com.country.simulator.engine

import com.country.simulator.model.*
import com.country.simulator.repositories.GameRepository
import kotlinx.coroutines.flow.first
import kotlin.random.Random

class ButterflyEffectEngine(private val repository: GameRepository) {
    
    private val random = Random
    
    /**
     * Process a macro decision and generate cascading micro tasks
     */
    suspend fun processMacroDecision(decision: MacroDecision): List<MicroTask> {
        val microTasks = mutableListOf<MicroTask>()
        
        when (decision.decisionType) {
            MacroDecisionType.ELECTION -> {
                microTasks.addAll(generateElectionTasks(decision))
            }
            MacroDecisionType.TRADE_DEAL -> {
                microTasks.addAll(generateTradeDealTasks(decision))
            }
            MacroDecisionType.SANCTIONS -> {
                microTasks.addAll(generateSanctionsTasks(decision))
            }
            MacroDecisionType.BUDGET_APPROVAL -> {
                microTasks.addAll(generateBudgetTasks(decision))
            }
            MacroDecisionType.LAW_ENACTMENT -> {
                microTasks.addAll(generateLawTasks(decision))
            }
            MacroDecisionType.WAR_DECLARATION -> {
                microTasks.addAll(generateWarTasks(decision))
            }
            MacroDecisionType.ALLIANCE_JOIN -> {
                microTasks.addAll(generateAllianceTasks(decision))
            }
            MacroDecisionType.BLOC_JOIN -> {
                microTasks.addAll(generateBlocTasks(decision))
            }
            MacroDecisionType.CONSTITUTIONAL_CHANGE -> {
                microTasks.addAll(generateConstitutionTasks(decision))
            }
            MacroDecisionType.EMERGENCY_DECLARATION -> {
                microTasks.addAll(generateEmergencyTasks(decision))
            }
        }
        
        // Save all generated tasks
        microTasks.forEach { task ->
            repository.insertMicroTask(task)
        }
        
        // Update the macro decision with task count
        val updatedDecision = decision.copy(
            microTasksGenerated = microTasks.size,
            isCompleted = true,
            completedDate = System.currentTimeMillis()
        )
        
        return microTasks
    }
    
    /**
     * Generate micro tasks from election macro decision
     */
    private fun generateElectionTasks(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.GIVE_SPEECH,
            title = "Victory Speech",
            description = "Deliver victory speech to supporters",
            category = TaskCategory.POLITICS,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000,
            effects = """{"approvalRating": 5, "happiness": 3}"""
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.ATTEND_MEETING,
            title = "Cabinet Reshuffle Meeting",
            description = "Review appointee positions based on election results",
            category = TaskCategory.POLITICS,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.DECLARE_POLICY,
            title = "Announce Policy Priorities",
            description = "Declare key policy priorities for the new term",
            category = TaskCategory.POLITICS,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000,
            effects = """{"promisesMade": 3}"""
        ))
        
        return tasks
    }
    
    /**
     * Generate micro tasks from trade deal macro decision
     */
    private fun generateTradeDealTasks(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.REVIEW_BUDGET,
            title = "Review Tariff Impact",
            description = "Analyze budget impact of new tariff structures",
            category = TaskCategory.ECONOMY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.APPROVE_PERMIT,
            title = "Update Business Permits",
            description = "Review and update affected business licenses",
            category = TaskCategory.ECONOMY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.NEGOTIATE_DEAL,
            title = "Implement Quota System",
            description = "Set up import/export quota management",
            category = TaskCategory.ECONOMY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000
        ))
        
        // Generate butterfly effects
        tasks.add(createButterflyTask(
            countryId = countryId,
            macroId = decision.id,
            title = "Local Industry Complaints",
            description = "Domestic manufacturers protest increased competition",
            delayDays = 30,
            effectMagnitude = -5.0
        ))
        
        return tasks
    }
    
    /**
     * Generate micro tasks from sanctions macro decision
     */
    private fun generateSanctionsTasks(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.REVIEW_BUDGET,
            title = "Review Trade Revenue Impact",
            description = "Calculate lost revenue from sanctioned trade",
            category = TaskCategory.ECONOMY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.APPROVE_PERMIT,
            title = "Revoke Business Licenses",
            description = "Review companies doing business with sanctioned nation",
            category = TaskCategory.ECONOMY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.DECLARE_POLICY,
            title = "Announce Customs Regulations",
            description = "Publish new customs enforcement guidelines",
            category = TaskCategory.ECONOMY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.LAUNCH_AUDIT,
            title = "Audit Affected Businesses",
            description = "Investigate businesses for sanction violations",
            category = TaskCategory.ECONOMY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000
        ))
        
        return tasks
    }
    
    /**
     * Generate micro tasks from budget approval
     */
    private fun generateBudgetTasks(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.ALLOCATE_FUNDS,
            title = "Allocate Ministry Budgets",
            description = "Distribute approved budget to ministries",
            category = TaskCategory.ECONOMY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.REVIEW_BUDGET,
            title = "Review Department Allocations",
            description = "Oversee department-level budget distribution",
            category = TaskCategory.ECONOMY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000
        ))
        
        return tasks
    }
    
    /**
     * Generate micro tasks from law enactment
     */
    private fun generateLawTasks(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.SIGN_DOCUMENT,
            title = "Sign Enacted Law",
            description = "Officially sign the law into effect",
            category = TaskCategory.LAW,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.DECLARE_POLICY,
            title = "Announce Implementation Plan",
            description = "Publicize how the law will be implemented",
            category = TaskCategory.LAW,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000
        ))
        
        // Potential court challenge
        if (random.nextDouble() < 0.3) {
            tasks.add(createButterflyTask(
                countryId = countryId,
                macroId = decision.id,
                title = "Constitutional Challenge Filed",
                description = "Opposition group files court challenge",
                delayDays = 14,
                effectMagnitude = -3.0
            ))
        }
        
        return tasks
    }
    
    /**
     * Generate micro tasks from war declaration
     */
    private fun generateWarTasks(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.DEPLOY_TROOPS,
            title = "Authorize Military Deployment",
            description = "Order initial troop movements",
            category = TaskCategory.DEFENSE,
            priority = Priority.URGENT,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.GIVE_SPEECH,
            title = "Address the Nation",
            description = "Explain war decision to citizens",
            category = TaskCategory.POLITICS,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 12 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.ALLOCATE_FUNDS,
            title = "Emergency War Budget",
            description = "Allocate emergency military funding",
            category = TaskCategory.ECONOMY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000
        ))
        
        return tasks
    }
    
    /**
     * Generate micro tasks from alliance join
     */
    private fun generateAllianceTasks(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.SIGN_TREATY,
            title = "Sign Alliance Treaty",
            description = "Formally sign the alliance agreement",
            category = TaskCategory.DIPLOMACY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.ATTEND_MEETING,
            title = "Alliance Strategy Meeting",
            description = "Attend first alliance coordination meeting",
            category = TaskCategory.DIPLOMACY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000
        ))
        
        return tasks
    }
    
    /**
     * Generate micro tasks from trade bloc join
     */
    private fun generateBlocTasks(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.REVIEW_BUDGET,
            title = "Review Bloc Contribution",
            description = "Calculate membership fees and contributions",
            category = TaskCategory.ECONOMY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.APPROVE_PERMIT,
            title = "Update Work Permit Rules",
            description = "Implement common market work permit system",
            category = TaskCategory.DIPLOMACY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000
        ))
        
        return tasks
    }
    
    /**
     * Generate micro tasks from constitutional change
     */
    private fun generateConstitutionTasks(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.SIGN_DOCUMENT,
            title = "Sign Constitutional Amendment",
            description = "Officially enact the constitutional change",
            category = TaskCategory.LAW,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.GIVE_SPEECH,
            title = "Constitutional Address",
            description = "Address the nation on constitutional changes",
            category = TaskCategory.POLITICS,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000
        ))
        
        return tasks
    }
    
    /**
     * Generate micro tasks from emergency declaration
     */
    private fun generateEmergencyTasks(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.GIVE_SPEECH,
            title = "Emergency Address",
            description = "Address the nation about the emergency",
            category = TaskCategory.POLITICS,
            priority = Priority.URGENT,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 6 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.ALLOCATE_FUNDS,
            title = "Emergency Fund Allocation",
            description = "Release emergency response funds",
            category = TaskCategory.ECONOMY,
            priority = Priority.URGENT,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 12 * 60 * 60 * 1000
        ))
        
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.DEPLOY_TROOPS,
            title = "Deploy Emergency Services",
            description = "Mobilize national guard and emergency responders",
            category = TaskCategory.DEFENSE,
            priority = Priority.URGENT,
            relatedMacroId = decision.id,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + 6 * 60 * 60 * 1000
        ))
        
        return tasks
    }
    
    /**
     * Create a delayed butterfly effect task
     */
    private fun createButterflyTask(
        countryId: Long,
        macroId: Long,
        title: String,
        description: String,
        delayDays: Int,
        effectMagnitude: Double
    ): MicroTask {
        return MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.RESPOND_TO_CRISIS,
            title = title,
            description = description,
            category = TaskCategory.entries.random(),
            priority = Priority.MEDIUM,
            relatedMacroId = macroId,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            completedDate = null,
            dueDate = System.currentTimeMillis() + delayDays * 24 * 60 * 60 * 1000,
            butterflyEffects = """{"magnitude": $effectMagnitude, "delay": $delayDays}"""
        )
    }
    
    /**
     * Process butterfly effects and apply them to the game state
     */
    suspend fun processButterflyEffects(countryId: Long) {
        val pendingEffects = repository.getPendingEffects(countryId).first()
        val currentTime = System.currentTimeMillis()
        
        for (effect in pendingEffects) {
            if (currentTime >= effect.triggeredDate ?: continue) {
                // Apply the effect based on target entity
                applyEffect(effect)
                
                // Mark as triggered
                repository.updateButterflyEffect(effect.copy(isTriggered = true, triggeredDate = currentTime))
                
                // Generate secondary effects (chain reactions)
                if (effect.chainLevel < 3 && random.nextDouble() < 0.3) {
                    generateSecondaryEffects(effect)
                }
            }
        }
    }
    
    /**
     * Apply a butterfly effect to the game state
     */
    private suspend fun applyEffect(effect: ButterflyEffect) {
        val playerCountry = repository.getPlayerCountrySync() ?: return
        
        // Parse effect magnitude and apply to appropriate stats
        val magnitude = effect.effectMagnitude
        
        when (effect.targetEntity) {
            "player_countries" -> {
                val updated = playerCountry.copy(
                    approvalRating = (playerCountry.approvalRating + magnitude * 0.1).coerceIn(0.0, 100.0),
                    stability = (playerCountry.stability + magnitude * 0.05).coerceIn(0.0, 100.0),
                    lastUpdated = System.currentTimeMillis()
                )
                repository.updatePlayerCountry(updated)
            }
            "diplomatic_relations" -> {
                // Update diplomatic relations
                effect.targetEntityId?.let { id ->
                    // Implementation would fetch and update relation
                }
            }
            "businesses" -> {
                // Update business confidence
                effect.targetEntityId?.let { id ->
                    // Implementation would fetch and update business
                }
            }
        }
    }
    
    /**
     * Generate secondary effects from a triggered effect
     */
    private suspend fun generateSecondaryEffects(sourceEffect: ButterflyEffect) {
        val secondaryEffects = listOf(
            ButterflyEffect(
                countryId = sourceEffect.countryId,
                sourceTaskId = sourceEffect.sourceTaskId,
                sourceTaskType = sourceEffect.sourceTaskType,
                effectType = EffectType.DELAYED,
                targetEntity = "npcs",
                targetEntityId = null,
                effectDescription = "Secondary effect: ${sourceEffect.effectDescription}",
                effectMagnitude = sourceEffect.effectMagnitude * 0.5,
                delayDays = 7 + random.nextInt(14),
                chainLevel = sourceEffect.chainLevel + 1
            )
        )
        
        secondaryEffects.forEach { effect ->
            repository.insertButterflyEffect(effect)
        }
    }
}
