package com.country.simulator.engine

import com.country.simulator.model.*
import com.country.simulator.repositories.GameRepository
import kotlinx.coroutines.flow.first
import kotlin.random.Random

/**
 * Macro-Micro Cascade Engine
 * Handles the complete interconnection between macro decisions and micro tasks
 * Every macro decision generates specific micro tasks with full tracking
 */
class MacroMicroCascadeEngine(private val repository: GameRepository) {
    
    private val random = Random
    
    /**
     * Process ANY macro decision and generate complete micro task cascade
     */
    suspend fun processMacroDecision(macroDecision: MacroDecision): CascadeResult {
        val microTasks = when (macroDecision.decisionType) {
            MacroDecisionType.ELECTION -> processElectionMacro(macroDecision)
            MacroDecisionType.TRADE_DEAL -> processTradeDealMacro(macroDecision)
            MacroDecisionType.SANCTIONS -> processSanctionsMacro(macroDecision)
            MacroDecisionType.BUDGET_APPROVAL -> processBudgetMacro(macroDecision)
            MacroDecisionType.LAW_ENACTMENT -> processLawMacro(macroDecision)
            MacroDecisionType.WAR_DECLARATION -> processWarMacro(macroDecision)
            MacroDecisionType.ALLIANCE_JOIN -> processAllianceMacro(macroDecision)
            MacroDecisionType.BLOC_JOIN -> processBlocMacro(macroDecision)
            MacroDecisionType.CONSTITUTIONAL_CHANGE -> processConstitutionMacro(macroDecision)
            MacroDecisionType.EMERGENCY_DECLARATION -> processEmergencyMacro(macroDecision)
        }
        
        // Save all generated micro tasks
        microTasks.forEach { task ->
            repository.insertMicroTask(task)
        }
        
        // Create butterfly effects for delayed consequences
        val butterflyEffects = generateButterflyEffects(macroDecision, microTasks)
        butterflyEffects.forEach { effect ->
            repository.insertButterflyEffect(effect)
        }
        
        // Update macro decision
        val updatedMacro = macroDecision.copy(
            microTasksGenerated = microTasks.size,
            isCompleted = true,
            completedDate = System.currentTimeMillis(),
            consequences = """{"tasksGenerated": ${microTasks.size}, "effectsGenerated": ${butterflyEffects.size}}"""
        )
        
        return CascadeResult(
            macroDecision = updatedMacro,
            microTasks = microTasks,
            butterflyEffects = butterflyEffects,
            totalTasksGenerated = microTasks.size
        )
    }
    
    /**
     * SANCTION MACRO → Micro tasks
     * Example: Sanction Country → business permits, customs regulations, tax brackets
     */
    private suspend fun processSanctionsMacro(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        // 1. Business permits micro-task
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.APPROVE_PERMIT,
            title = "Review Affected Business Permits",
            description = "Companies with ties to sanctioned country need permit review",
            category = TaskCategory.ECONOMY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityType = "sanctions",
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000,
            options = "Revoke All,Review Individually,Grandfather Existing",
            effects = """{"businessImpact": -15, "tradeVolume": -20}"""
        ))
        
        // 2. Customs regulations micro-task
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.DECLARE_POLICY,
            title = "Update Customs Regulations",
            description = "Rewrite customs enforcement guidelines for sanctioned nation",
            category = TaskCategory.ECONOMY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityType = "sanctions",
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000,
            options = "Strict Enforcement,Moderate,Lenient",
            effects = """{"tradeFriction": 25, "smugglingRisk": 15}"""
        ))
        
        // 3. Tax bracket adjustment micro-task
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.ADJUST_RATE,
            title = "Adjust Tax Brackets for Sanction Members",
            description = "Modify tax treatment of transactions with sanctioned entities",
            category = TaskCategory.ECONOMY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityType = "sanctions",
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000,
            options = "Increase Tariffs,Add Penalties,Maintain Current",
            effects = """{"taxRevenue": 10, "complianceCost": 5}"""
        ))
        
        // 4. Launch audits micro-task
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.LAUNCH_AUDIT,
            title = "Audit Businesses Violating Sanctions",
            description = "Investigate companies potentially violating sanction terms",
            category = TaskCategory.ECONOMY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityType = "sanctions",
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000,
            options = "Aggressive Audit,Targeted Audit,Warning Only",
            effects = """{"compliance": 20, "businessConfidence": -10}"""
        ))
        
        // 5. Diplomatic micro-task
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.MAKE_CALL,
            title = "Coordinate with Allied Nations",
            description = "Ensure allies are enforcing similar sanctions",
            category = TaskCategory.DIPLOMACY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityType = "sanctions",
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000,
            effects = """{"allianceStrength": 5, "effectiveness": 15}"""
        ))
        
        // 6. Economic impact assessment micro-task
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.REVIEW_REPORT,
            title = "Review Economic Impact Assessment",
            description = "Analyze domestic economic impact of sanctions",
            category = TaskCategory.ECONOMY,
            priority = Priority.LOW,
            relatedMacroId = decision.id,
            relatedEntityType = "sanctions",
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000,
            effects = """{"accuracy": 10}"""
        ))
        
        return tasks
    }
    
    /**
     * TRADE DEAL MACRO → Micro tasks
     */
    private suspend fun processTradeDealMacro(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        // 1. Permit cascade
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.APPROVE_PERMIT,
            title = "Process New Trade Permits",
            description = "Issue permits for businesses under new trade deal terms",
            category = TaskCategory.ECONOMY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000,
            options = "Fast Track,Normal Processing,Strict Review",
            effects = """{"tradeVolume": 15, "processingCost": 5}"""
        ))
        
        // 2. Quota management
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.ALLOCATE_FUNDS,
            title = "Set Import/Export Quotas",
            description = "Establish quota limits per industry under trade deal",
            category = TaskCategory.ECONOMY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000,
            effects = """{"industryProtection": 10, "consumerChoice": 5}"""
        ))
        
        // 3. Customs inspection micro-task
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.ATTEND_MEETING,
            title = "Train Customs Inspectors",
            description = "Brief inspectors on new trade deal verification procedures",
            category = TaskCategory.ECONOMY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000,
            effects = """{"compliance": 15, "delayReduction": 10}"""
        ))
        
        // 4. Business notification
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.DECLARE_POLICY,
            title = "Notify Affected Businesses",
            description = "Publish trade deal terms and compliance requirements",
            category = TaskCategory.ECONOMY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000,
            effects = """{"compliance": 20, "businessConfidence": 10}"""
        ))
        
        return tasks
    }
    
    /**
     * BUDGET MACRO → Micro tasks
     */
    private suspend fun processBudgetMacro(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        // 1. Ministry allocation micro-tasks (one per ministry)
        MinistryType.entries.forEach { ministryType ->
            tasks.add(MicroTask(
                countryId = countryId,
                taskType = MicroTaskType.ALLOCATE_FUNDS,
                title = "Allocate Budget: ${ministryType.name}",
                description = "Distribute approved budget to ${ministryType.name}",
                category = TaskCategory.ECONOMY,
                priority = Priority.HIGH,
                relatedMacroId = decision.id,
                relatedEntityType = "ministry",
                status = TaskStatus.PENDING,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000,
                effects = """{"ministryEfficiency": 10, "serviceDelivery": 5}"""
            ))
        }
        
        // 2. Department-level micro-tasks
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.REVIEW_BUDGET,
            title = "Review Department Allocations",
            description = "Oversee department-level budget distribution",
            category = TaskCategory.ECONOMY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000,
            effects = """{"oversight": 15}"""
        ))
        
        // 3. Purchase order backlog
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.APPROVE_PERMIT,
            title = "Clear Purchase Order Backlog",
            description = "Process pending purchase orders with new budget",
            category = TaskCategory.ECONOMY,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000,
            effects = """{"efficiency": 10, "backlog": -20}"""
        ))
        
        return tasks
    }
    
    /**
     * LAW ENACTMENT MACRO → Micro tasks
     */
    private suspend fun processLawMacro(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        // 1. Sign law
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.SIGN_DOCUMENT,
            title = "Sign Enacted Law",
            description = "Officially sign the law into effect",
            category = TaskCategory.LAW,
            priority = Priority.URGENT,
            relatedMacroId = decision.id,
            relatedEntityType = "law",
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000,
            effects = """{"legitimacy": 10}"""
        ))
        
        // 2. Implementation plan
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.DECLARE_POLICY,
            title = "Announce Implementation Plan",
            description = "Publicize how the law will be implemented",
            category = TaskCategory.LAW,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            relatedEntityType = "law",
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000,
            effects = """{"compliance": 15, "publicAwareness": 20}"""
        ))
        
        // 3. Agency guidance
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.ATTEND_MEETING,
            title = "Brief Agency Heads",
            description = "Coordinate implementation with relevant agencies",
            category = TaskCategory.LAW,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            relatedEntityType = "law",
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000,
            effects = """{"coordination": 15}"""
        ))
        
        // 4. Potential court challenge preparation
        if (random.nextDouble() < 0.3) {
            tasks.add(MicroTask(
                countryId = countryId,
                taskType = MicroTaskType.REVIEW_REPORT,
                title = "Prepare for Legal Challenges",
                description = "Anticipate and prepare for constitutional challenges",
                category = TaskCategory.LAW,
                priority = Priority.MEDIUM,
                relatedMacroId = decision.id,
                relatedEntityType = "law",
                status = TaskStatus.PENDING,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000,
                effects = """{"defensePreparedness": 20}"""
            ))
        }
        
        return tasks
    }
    
    /**
     * WAR DECLARATION MACRO → Micro tasks
     */
    private suspend fun processWarMacro(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        // 1. Troop deployment
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.DEPLOY_TROOPS,
            title = "Authorize Military Deployment",
            description = "Order initial troop movements and positioning",
            category = TaskCategory.DEFENSE,
            priority = Priority.URGENT,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 12 * 60 * 60 * 1000,
            effects = """{"readiness": 25, "publicAlert": 30}"""
        ))
        
        // 2. National address
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.GIVE_SPEECH,
            title = "Address the Nation",
            description = "Explain war decision to citizens",
            category = TaskCategory.POLITICS,
            priority = Priority.URGENT,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 6 * 60 * 60 * 1000,
            effects = """{"publicSupport": 15, "morale": 10}"""
        ))
        
        // 3. Emergency funding
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.ALLOCATE_FUNDS,
            title = "Emergency War Budget",
            description = "Allocate emergency military funding",
            category = TaskCategory.ECONOMY,
            priority = Priority.URGENT,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000,
            effects = """{"militaryCapacity": 30, "deficit": -20}"""
        ))
        
        // 4. Diplomatic outreach
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.MAKE_CALL,
            title = "Contact Allied Leaders",
            description = "Coordinate with allies on war effort",
            category = TaskCategory.DIPLOMACY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 12 * 60 * 60 * 1000,
            effects = """{"allianceStrength": 10, "support": 15}"""
        ))
        
        // 5. Economic sanctions on enemy
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.DECLARE_POLICY,
            title = "Freeze Enemy Assets",
            description = "Seize or freeze assets of enemy nation and supporters",
            category = TaskCategory.ECONOMY,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000,
            effects = """{"enemyCapacity": -10, "revenue": 5}"""
        ))
        
        return tasks
    }
    
    /**
     * ELECTION MACRO → Micro tasks
     */
    private suspend fun processElectionMacro(decision: MacroDecision): List<MicroTask> {
        val tasks = mutableListOf<MicroTask>()
        val countryId = decision.countryId
        
        // 1. Victory speech
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.GIVE_SPEECH,
            title = "Victory Speech",
            description = "Deliver victory speech to supporters",
            category = TaskCategory.POLITICS,
            priority = Priority.URGENT,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 12 * 60 * 60 * 1000,
            effects = """{"approval": 5, "unity": 10}"""
        ))
        
        // 2. Cabinet reshuffle
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.ATTEND_MEETING,
            title = "Cabinet Reshuffle Meeting",
            description = "Review appointee positions based on election results",
            category = TaskCategory.POLITICS,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000,
            effects = """{"efficiency": 10, "loyalty": 5}"""
        ))
        
        // 3. Policy priorities
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.DECLARE_POLICY,
            title = "Announce Policy Priorities",
            description = "Declare key policy priorities for the new term",
            category = TaskCategory.POLITICS,
            priority = Priority.HIGH,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000,
            effects = """{"clarity": 15, "expectations": 20}"""
        ))
        
        // 4. Transition meetings
        tasks.add(MicroTask(
            countryId = countryId,
            taskType = MicroTaskType.ATTEND_MEETING,
            title = "Transition Briefings",
            description = "Receive briefings from outgoing administration",
            category = TaskCategory.POLITICS,
            priority = Priority.MEDIUM,
            relatedMacroId = decision.id,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000,
            effects = """{"preparedness": 20}"""
        ))
        
        return tasks
    }
    
    // Additional macro processors...
    
    private suspend fun processAllianceMacro(decision: MacroDecision): List<MicroTask> {
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
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
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
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000
        ))
        
        return tasks
    }
    
    private suspend fun processBlocMacro(decision: MacroDecision): List<MicroTask> {
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
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
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
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000
        ))
        
        return tasks
    }
    
    private suspend fun processConstitutionMacro(decision: MacroDecision): List<MicroTask> {
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
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
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
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000
        ))
        
        return tasks
    }
    
    private suspend fun processEmergencyMacro(decision: MacroDecision): List<MicroTask> {
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
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
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
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
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
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 6 * 60 * 60 * 1000
        ))
        
        return tasks
    }
    
    /**
     * Generate butterfly effects from macro decision
     */
    private suspend fun generateButterflyEffects(macro: MacroDecision, tasks: List<MicroTask>): List<ButterflyEffect> {
        val effects = mutableListOf<ButterflyEffect>()
        
        // Generate 1-3 delayed effects per macro decision
        val effectCount = random.nextInt(1, 4)
        
        for (i in 0 until effectCount) {
            effects.add(ButterflyEffect(
                countryId = macro.countryId,
                sourceTaskId = tasks.randomOrNull()?.id ?: 0,
                sourceTaskType = tasks.randomOrNull()?.taskType ?: MicroTaskType.REVIEW_REPORT,
                effectType = EffectType.DELAYED,
                targetEntity = getRandomTargetEntity(),
                targetEntityId = null,
                effectDescription = "Delayed consequence of ${macro.title}",
                effectMagnitude = (random.nextDouble() - 0.5) * 20,
                delayDays = random.nextInt(7, 60),
                chainLevel = 1
            ))
        }
        
        return effects
    }
    
    private fun getRandomTargetEntity(): String {
        return listOf(
            "player_countries",
            "diplomatic_relations",
            "businesses",
            "npcs",
            "opinion_polls"
        ).random()
    }
}

data class CascadeResult(
    val macroDecision: MacroDecision,
    val microTasks: List<MicroTask>,
    val butterflyEffects: List<ButterflyEffect>,
    val totalTasksGenerated: Int
)
