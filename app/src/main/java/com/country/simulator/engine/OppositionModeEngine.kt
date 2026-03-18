package com.country.simulator.engine

import com.country.simulator.model.*
import com.country.simulator.repositories.GameRepository
import kotlinx.coroutines.flow.first
import kotlin.random.Random

/**
 * Opposition Mode Engine - Complete gameplay after losing election
 * Handles filibuster, committee work, flipping government from inside
 */
class OppositionModeEngine(private val repository: GameRepository) {
    
    private val random = Random
    
    /**
     * Filibuster a bill - micro task
     */
    suspend fun filibusterBill(billId: Long, duration: Int, topic: String): FilibusterResult {
        val bill = repository.billDao().getBillById(billId) ?: return FilibusterResult.FAILURE
        
        // Calculate effectiveness based on preparation and topic relevance
        val baseEffectiveness = when (duration) {
            in 1..2 -> 0.3
            in 3..6 -> 0.5
            in 7..12 -> 0.7
            else -> 0.9
        }
        
        val randomFactor = random.nextDouble() * 0.2
        val totalEffectiveness = baseEffectiveness + randomFactor
        
        // Delay the bill
        val delayedBill = bill.copy(
            currentStage = if (totalEffectiveness > 0.5) BillStage.FLOOR else bill.currentStage,
            // Add filibuster to votes JSON
            votes = bill.votes + """{"filibuster": {"duration": $duration, "topic": "$topic", "effectiveness": $totalEffectiveness}}"""
        )
        
        repository.billDao().update(delayedBill)
        
        // Generate micro-task: Prepare filibuster speech
        val speechTask = MicroTask(
            countryId = bill.countryId,
            taskType = MicroTaskType.GIVE_SPEECH,
            title = "Prepare Filibuster Speech",
            description = "Research and prepare arguments against the bill on topic: $topic",
            category = TaskCategory.POLITICS,
            priority = Priority.URGENT,
            relatedMacroId = billId,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 12 * 60 * 60 * 1000
        )
        repository.insertMicroTask(speechTask)
        
        // Generate butterfly effect: Public opinion may shift
        val butterflyEffect = ButterflyEffect(
            countryId = bill.countryId,
            sourceTaskId = speechTask.id,
            sourceTaskType = MicroTaskType.GIVE_SPEECH,
            effectType = EffectType.DELAYED,
            targetEntity = "opinion_polls",
            targetEntityId = null,
            effectDescription = "Filibuster affects public opinion on the issue",
            effectMagnitude = (random.nextDouble() - 0.5) * 10,
            delayDays = 7,
            chainLevel = 1
        )
        repository.insertButterflyEffect(butterflyEffect)
        
        return FilibusterResult.SUCCESS(totalEffectiveness, duration, delayedBill.currentStage)
    }
    
    /**
     * Committee work - review and amend bills in committee
     */
    suspend fun committeeWork(committeeId: Long, billId: Long, action: CommitteeAction): CommitteeResult {
        val committee = repository.committeeDao().getCommitteeById(committeeId) 
            ?: return CommitteeResult.FAILURE
        val bill = repository.billDao().getBillById(billId) ?: return CommitteeResult.FAILURE
        
        return when (action) {
            is CommitteeAction.Amend -> {
                val amendedBill = bill.copy(
                    amendments = bill.amendments + """|${action.amendmentText}""",
                    currentStage = BillStage.MARKUP
                )
                repository.billDao().update(amendedBill)
                
                // Generate micro-task: Vote on amendment
                val voteTask = MicroTask(
                    countryId = bill.countryId,
                    taskType = MicroTaskType.ATTEND_MEETING,
                    title = "Committee Vote on Amendment",
                    description = "Vote on the proposed amendment to ${bill.title}",
                    category = TaskCategory.POLITICS,
                    priority = Priority.HIGH,
                    relatedMacroId = billId,
                    status = TaskStatus.PENDING,
                    createdDate = System.currentTimeMillis(),
                    dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000
                )
                repository.insertMicroTask(voteTask)
                
                CommitteeResult.AMENDED(amendedBill.amendments.split("|").size)
            }
            
            is CommitteeAction.Delay -> {
                val delayedBill = bill.copy(
                    currentStage = BillStage.SUBCOMMITTEE
                )
                repository.billDao().update(delayedBill)
                
                CommitteeResult.DELAYED(action.days)
            }
            
            is CommitteeAction.PushThrough -> {
                val advancedBill = bill.copy(
                    currentStage = BillStage.FLOOR
                )
                repository.billDao().update(advancedBill)
                
                CommitteeResult.PUSHED_THROUGH
            }
            
            is CommitteeAction.Kill -> {
                val killedBill = bill.copy(
                    currentStage = BillStage.REPEALED
                )
                repository.billDao().update(killedBill)
                
                CommitteeResult.KILLED
            }
        }
    }
    
    /**
     * Build coalition - lobby other MPs for support
     */
    suspend fun buildCoalition(targetNPCs: List<Long>, issue: String): CoalitionResult {
        val npcs = targetNPCs.mapNotNull { id -> 
            repository.npcDao().getNPCById(id) 
        }
        
        if (npcs.isEmpty()) return CoalitionResult.FAILURE
        
        var successCount = 0
        var totalInfluenceGain = 0.0
        
        for (npc in npcs) {
            // Calculate persuasion success based on political leanings and relationship
            val alignmentBonus = if (npc.politicalLeanings * getOppositionLeanings() > 0) 0.2 else 0.0
            val baseChance = 0.4 + alignmentBonus + (random.nextDouble() * 0.3)
            
            if (random.nextDouble() < baseChance) {
                successCount++
                totalInfluenceGain += 5.0
                
                // Update NPC relationship
                val updatedNPC = npc.copy(
                    // Would update relationship JSON
                    lastUpdated = System.currentTimeMillis()
                )
                repository.npcDao().update(updatedNPC)
            }
        }
        
        val successRate = successCount.toDouble() / npcs.size
        
        // Generate micro-task: Follow up with undecided MPs
        if (successRate < 1.0) {
            val followUpTask = MicroTask(
                countryId = npcs.first().countryId,
                taskType = MicroTaskType.MAKE_CALL,
                title = "Follow Up with Undecided MPs",
                description = "Contact MPs who haven't committed on: $issue",
                category = TaskCategory.POLITICS,
                priority = Priority.MEDIUM,
                status = TaskStatus.PENDING,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 48 * 60 * 60 * 1000
            )
            repository.insertMicroTask(followUpTask)
        }
        
        return CoalitionResult.SUCCESS(successCount, npcs.size, successRate, totalInfluenceGain)
    }
    
    /**
     * Vote of no confidence - attempt to flip government
     */
    suspend fun voteOfNoConfidence(reason: String, supporters: List<Long>): NoConfidenceResult {
        // Calculate support
        val totalSupporters = supporters.size
        val requiredVotes = 250 // Assuming 500 seat parliament
        
        // Add random factor for undecided MPs
        val swingVotes = (random.nextInt(-20, 21))
        val totalVotes = totalSupporters + swingVotes
        
        val success = totalVotes >= requiredVotes
        
        if (success) {
            // Government falls - trigger new election
            val newElection = Election(
                countryId = 0, // Would get actual countryId
                electionType = ElectionType.SPECIAL,
                scheduledDate = System.currentTimeMillis() + 60 * 24 * 60 * 60 * 1000, // 60 days
                isActive = true
            )
            repository.insertElection(newElection)
            
            // Generate cascade of micro-tasks for election preparation
            generateElectionPreparationTasks(0, newElection.id)
            
            return NoConfidenceResult.SUCCESS(totalVotes, requiredVotes)
        } else {
            // Failed - backlash
            val backlashTask = MicroTask(
                countryId = 0,
                taskType = MicroTaskType.RESPOND_TO_CRISIS,
                title = "Backlash from Failed No-Confidence Vote",
                description = "The failed vote has caused political backlash. Manage the fallout.",
                category = TaskCategory.POLITICS,
                priority = Priority.HIGH,
                status = TaskStatus.PENDING,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000
            )
            repository.insertMicroTask(backlashTask)
            
            return NoConfidenceResult.FAILED(totalVotes, requiredVotes)
        }
    }
    
    /**
     * Question Time - challenge government ministers
     */
    suspend fun questionTime(ministerId: Long, topic: String, aggression: AggressionLevel): QuestionTimeResult {
        val minister = repository.appointeeDao().getAppointeeById(ministerId) 
            ?: return QuestionTimeResult.FAILURE
        
        val effectiveness = when (aggression) {
            AggressionLevel.GENTLE -> 0.3 + random.nextDouble() * 0.3
            AggressionLevel.FIRM -> 0.5 + random.nextDouble() * 0.3
            AggressionLevel.AGGRESSIVE -> 0.7 + random.nextDouble() * 0.3
            AggressionLevel.DESTRUCTIVE -> 0.9 + random.nextDouble() * 0.1
        }
        
        // Calculate minister response effectiveness
        val ministerCompetence = minister.competence / 100.0
        val defenseEffectiveness = ministerCompetence * (1 - effectiveness)
        
        val publicOpinionShift = (effectiveness - defenseEffectiveness) * 10
        
        // Update minister approval
        val updatedMinister = minister.copy(
            approvalRating = (minister.approvalRating - publicOpinionShift).coerceIn(0.0, 100.0),
            lastUpdated = System.currentTimeMillis()
        )
        repository.appointeeDao().update(updatedMinister)
        
        // Generate micro-task: Prepare follow-up questions
        val followUpTask = MicroTask(
            countryId = 0,
            taskType = MicroTaskType.REVIEW_REPORT,
            title = "Prepare Follow-up Questions",
            description = "Research deeper questions on: $topic",
            category = TaskCategory.POLITICS,
            priority = Priority.LOW,
            status = TaskStatus.PENDING,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
        )
        repository.insertMicroTask(followUpTask)
        
        return QuestionTimeResult.SUCCESS(effectiveness, defenseEffectiveness, publicOpinionShift)
    }
    
    /**
     * Private Member's Bill - introduce own legislation
     */
    suspend fun privateMembersBill(title: String, summary: String, coSponsors: List<Long>): PrivateBillResult {
        val billNumber = "PM-${System.currentTimeMillis()}"
        
        val bill = Bill(
            countryId = 0,
            billNumber = billNumber,
            title = title,
            summary = summary,
            fullText = "Full text of private member's bill...",
            currentStage = BillStage.INTRODUCED,
            currentCommittee = "House Administration",
            introducedBy = "Opposition MP",
            introducedDate = System.currentTimeMillis(),
            committees = """["House Administration"]""",
            lobbySupport = 0.0,
            lobbyOpposition = 0.0
        )
        
        val billId = repository.billDao().insert(bill)
        
        // Generate micro-tasks for bill progression
        val tasks = listOf(
            MicroTask(
                countryId = 0,
                taskType = MicroTaskType.ATTEND_MEETING,
                title = "Committee Hearing Preparation",
                description = "Prepare for committee hearing on $title",
                category = TaskCategory.POLITICS,
                priority = Priority.MEDIUM,
                relatedMacroId = billId,
                status = TaskStatus.PENDING,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
            ),
            MicroTask(
                countryId = 0,
                taskType = MicroTaskType.MAKE_CALL,
                title = "Lobby Co-sponsors",
                description = "Contact potential co-sponsors for support",
                category = TaskCategory.POLITICS,
                priority = Priority.MEDIUM,
                relatedMacroId = billId,
                status = TaskStatus.PENDING,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000
            )
        )
        
        tasks.forEach { repository.insertMicroTask(it) }
        
        return PrivateBillResult.SUCCESS(billId, billNumber)
    }
    
    /**
     * Whip count - track support for issues
     */
    suspend fun whipCount(issue: String, targetedMPs: List<Long>): WhipCountResult {
        val npcs = targetedMPs.mapNotNull { id -> 
            repository.npcDao().getNPCById(id) 
        }
        
        val supporters = mutableListOf<String>()
        val opponents = mutableListOf<String>()
        val undecided = mutableListOf<String>()
        
        for (npc in npcs) {
            val alignment = npc.politicalLeanings * getOppositionLeanings()
            
            when {
                alignment > 30 -> supporters.add(npc.name)
                alignment < -30 -> opponents.add(npc.name)
                else -> undecided.add(npc.name)
            }
        }
        
        // Generate micro-task: Focus on undecided MPs
        if (undecided.isNotEmpty()) {
            val persuasionTask = MicroTask(
                countryId = npcs.first().countryId,
                taskType = MicroTaskType.MAKE_CALL,
                title = "Persuade Undecided MPs",
                description = "Focus on ${undecided.size} undecided MPs regarding: $issue",
                category = TaskCategory.POLITICS,
                priority = Priority.HIGH,
                status = TaskStatus.PENDING,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 48 * 60 * 60 * 1000
            )
            repository.insertMicroTask(persuasionTask)
        }
        
        return WhipCountResult.SUCCESS(
            issue = issue,
            supporters = supporters.size,
            opponents = opponents.size,
            undecided = undecided.size,
            supporterNames = supporters,
            opponentNames = opponents,
            undecidedNames = undecided
        )
    }
    
    // Helper functions
    
    private fun getOppositionLeanings(): Int {
        // Returns the political leaning of the opposition (-100 to 100)
        return -50 // Default center-left opposition
    }
    
    private fun generateElectionPreparationTasks(countryId: Long, electionId: Long) {
        val tasks = listOf(
            MicroTask(
                countryId = countryId,
                taskType = MicroTaskType.ATTEND_MEETING,
                title = "Strategy Meeting",
                description = "Plan election strategy for special election",
                category = TaskCategory.POLITICS,
                priority = Priority.URGENT,
                relatedMacroId = electionId,
                status = TaskStatus.PENDING,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000
            ),
            MicroTask(
                countryId = countryId,
                taskType = MicroTaskType.GIVE_SPEECH,
                title = "Announce Candidacy",
                description = "Publicly announce candidacy for special election",
                category = TaskCategory.POLITICS,
                priority = Priority.HIGH,
                relatedMacroId = electionId,
                status = TaskStatus.PENDING,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000
            )
        )
        
        tasks.forEach { repository.insertMicroTask(it) }
    }
}

// Enums for opposition mode

enum class AggressionLevel {
    GENTLE,
    FIRM,
    AGGRESSIVE,
    DESTRUCTIVE
}

sealed class CommitteeAction {
    data class Amend(val amendmentText: String) : CommitteeAction()
    data class Delay(val days: Int) : CommitteeAction()
    object PushThrough : CommitteeAction()
    object Kill : CommitteeAction()
}

// Result classes

sealed class FilibusterResult {
    object FAILURE : FilibusterResult()
    data class SUCCESS(val effectiveness: Double, val duration: Int, val billStage: BillStage) : FilibusterResult()
}

sealed class CommitteeResult {
    object FAILURE : CommitteeResult()
    data class AMENDED(val amendmentCount: Int) : CommitteeResult()
    data class DELAYED(val days: Int) : CommitteeResult()
    object PUSHED_THROUGH : CommitteeResult()
    object KILLED : CommitteeResult()
}

sealed class CoalitionResult {
    object FAILURE : CoalitionResult()
    data class SUCCESS(val successCount: Int, val totalCount: Int, val successRate: Double, val influenceGain: Double) : CoalitionResult()
}

sealed class NoConfidenceResult {
    data class SUCCESS(val totalVotes: Int, val required: Int) : NoConfidenceResult()
    data class FAILED(val totalVotes: Int, val required: Int) : NoConfidenceResult()
}

sealed class QuestionTimeResult {
    object FAILURE : QuestionTimeResult()
    data class SUCCESS(val effectiveness: Double, val defenseEffectiveness: Double, val publicOpinionShift: Double) : QuestionTimeResult()
}

sealed class PrivateBillResult {
    object FAILURE : PrivateBillResult()
    data class SUCCESS(val billId: Long, val billNumber: String) : PrivateBillResult()
}

sealed class WhipCountResult {
    object FAILURE : WhipCountResult()
    data class SUCCESS(
        val issue: String,
        val supporters: Int,
        val opponents: Int,
        val undecided: Int,
        val supporterNames: List<String>,
        val opponentNames: List<String>,
        val undecidedNames: List<String>
    ) : WhipCountResult()
}
