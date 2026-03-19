package com.country.simulator.engine

import com.country.simulator.model.*
import com.country.simulator.repositories.GameRepository
import com.country.simulator.generation.ProceduralGenerator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameStateManager(private val repository: GameRepository) {
    
    private val butterflyEngine = ButterflyEffectEngine(repository)
    private val electionEngine = ElectionEngine(repository)
    
    // Game state flows
    fun getGameState(countryId: Long): Flow<GameState> {
        return repository.getPlayerCountry().map { playerCountry ->
            GameState(
                playerCountry = playerCountry,
                countryId = countryId
            )
        }
    }
    
    /**
     * Initialize a new game
     */
    suspend fun initializeNewGame(): GameInitializationResult {
        try {
            // Generate player country
            val playerCountry = ProceduralGenerator.generateCountry(isPlayer = true)
            val countryId = repository.insertCountry(playerCountry)
            
            // Create player country details
            val playerDetails = PlayerCountry(
                countryId = countryId,
                leaderName = generateLeaderName(),
                leaderTitle = when (playerCountry.governmentType) {
                    GovernmentType.MONARCHY -> "King/Queen"
                    GovernmentType.TECHNOCRACY -> "Chief Administrator"
                    else -> "President"
                },
                approvalRating = 50.0,
                stability = 75.0,
                currentGameMode = GameMode.EXECUTIVE
            )
            repository.insertPlayerCountry(playerDetails)
            
            // Initialize resources
            val resources = NationalResources(
                countryId = countryId,
                treasuryBalance = 1_000_000_000.0,
                oilProduction = playerCountry.oilReserves * 0.01,
                energyProduction = 10000.0,
                exports = 100_000_000.0,
                imports = 80_000_000.0
            )
            repository.insertResources(resources)
            
            // Generate initial ministries
            initializeMinistries(countryId)
            
            // Generate initial NPCs
            generateInitialNPCs(countryId)
            
            // Generate initial micro tasks
            generateInitialTasks(countryId)
            
            // Generate foreign countries
            generateForeignCountries(countryId)
            
            return GameInitializationResult.SUCCESS(countryId)
        } catch (e: Exception) {
            return GameInitializationResult.FAILURE(e.message ?: "Unknown error")
        }
    }
    
    /**
     * Initialize government ministries
     */
    private suspend fun initializeMinistries(countryId: Long) {
        val ministryTypes = listOf(
            MinistryType.DEFENSE to "Ministry of Defense",
            MinistryType.FOREIGN_AFFAIRS to "Ministry of Foreign Affairs",
            MinistryType.TREASURY to "Ministry of Treasury",
            MinistryType.JUSTICE to "Ministry of Justice",
            MinistryType.INTERIOR to "Ministry of Interior",
            MinistryType.HEALTH to "Ministry of Health",
            MinistryType.EDUCATION to "Ministry of Education",
            MinistryType.TRANSPORT to "Ministry of Transport",
            MinistryType.ENERGY to "Ministry of Energy",
            MinistryType.COMMERCE to "Ministry of Commerce",
            MinistryType.LABOR to "Ministry of Labor",
            MinistryType.ENVIRONMENT to "Ministry of Environment"
        )
        
        for ((type, name) in ministryTypes) {
            val ministry = Ministry(
                countryId = countryId,
                name = name,
                ministerName = "Pending Appointment",
                ministerId = null,
                budget = 100_000_000.0,
                efficiency = 70.0,
                publicApproval = 60.0
            )
            repository.insertMinistry(ministry)
        }
    }
    
    /**
     * Generate initial important NPCs
     */
    private suspend fun generateInitialNPCs(countryId: Long) {
        // Generate cabinet members
        val positions = listOf(
            "Defense Minister", "Foreign Minister", "Treasury Minister",
            "Justice Minister", "Interior Minister", "Health Minister",
            "Education Minister", "Energy Minister"
        )
        
        for (position in positions) {
            val npc = ProceduralGenerator.generateNPC(countryId, isImportant = true)
            val updatedNpc = npc.copy(occupation = position)
            repository.insertNPC(updatedNpc)
        }
        
        // Generate opposition leaders
        for (i in 1..3) {
            val npc = ProceduralGenerator.generateNPC(countryId, isImportant = true)
            val updatedNpc = npc.copy(
                occupation = "Opposition Leader $i",
                politicalLeanings = -npc.politicalLeanings
            )
            repository.insertNPC(updatedNpc)
        }
        
        // Generate business leaders
        for (i in 1..5) {
            val npc = ProceduralGenerator.generateNPC(countryId, isImportant = true)
            val updatedNpc = npc.copy(
                occupation = "CEO",
                occupationType = OccupationType.CORPORATE_EXEC,
                wealth = (100_000_000..10_000_000_000).random().toDouble()
            )
            repository.insertNPC(updatedNpc)
        }
        
        // Generate media figures
        for (i in 1..3) {
            val npc = ProceduralGenerator.generateNPC(countryId, isImportant = true)
            val updatedNpc = npc.copy(
                occupation = "Media Mogul",
                influence = (50.0..100.0).random()
            )
            repository.insertNPC(updatedNpc)
        }
    }
    
    /**
     * Generate initial micro tasks for new game
     */
    private suspend fun generateInitialTasks(countryId: Long) {
        val initialTasks = listOf(
            MicroTask(
                countryId = countryId,
                taskType = MicroTaskType.ATTEND_MEETING,
                title = "First Cabinet Meeting",
                description = "Meet with your newly appointed cabinet members",
                category = TaskCategory.POLITICS,
                priority = Priority.HIGH,
                status = TaskStatus.PENDING,
                relatedEntityId = null,
                relatedEntityType = null,
                selectedOption = null,
                completedDate = null,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000,
                effects = """{"approvalRating": 2}"""
            ),
            MicroTask(
                countryId = countryId,
                taskType = MicroTaskType.GIVE_SPEECH,
                title = "Inaugural Address",
                description = "Deliver your first speech to the nation",
                category = TaskCategory.POLITICS,
                priority = Priority.HIGH,
                status = TaskStatus.PENDING,
                relatedEntityId = null,
                relatedEntityType = null,
                selectedOption = null,
                completedDate = null,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000,
                effects = """{"approvalRating": 5, "happiness": 3}"""
            ),
            MicroTask(
                countryId = countryId,
                taskType = MicroTaskType.REVIEW_BUDGET,
                title = "Review National Budget",
                description = "Examine the current state of the national budget",
                category = TaskCategory.ECONOMY,
                priority = Priority.NORMAL,
                status = TaskStatus.PENDING,
                relatedEntityId = null,
                relatedEntityType = null,
                selectedOption = null,
                completedDate = null,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
            ),
            MicroTask(
                countryId = countryId,
                taskType = MicroTaskType.MAKE_CALL,
                title = "Call Allied Leaders",
                description = "Establish diplomatic contact with key allies",
                category = TaskCategory.DIPLOMACY,
                priority = Priority.NORMAL,
                status = TaskStatus.PENDING,
                relatedEntityId = null,
                relatedEntityType = null,
                selectedOption = null,
                completedDate = null,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000
            )
        )
        
        initialTasks.forEach { task ->
            repository.insertMicroTask(task)
        }
    }
    
    /**
     * Generate foreign countries for diplomacy
     */
    private suspend fun generateForeignCountries(playerCountryId: Long) {
        val regions = listOf("North", "South", "East", "West", "Central")
        val baseNames = ProceduralGenerator::class.java.declaredMethods
            .find { it.name == "generateCountryName" }
        
        for (i in 1..10) {
            val foreignCountry = ProceduralGenerator.generateCountry(isPlayer = false)
            val countryId = repository.insertCountry(foreignCountry)
            
            // Create diplomatic relation
            val relation = DiplomaticRelation(
                countryId = playerCountryId,
                foreignCountryId = countryId,
                foreignCountryName = foreignCountry.name,
                relationLevel = (0..100).random(),
                trustLevel = (30..80).random(),
                embassyEstablished = random.nextBoolean()
            )
            repository.insertDiplomaticRelation(relation)
        }
    }
    
    /**
     * Process a micro task completion
     */
    suspend fun completeTask(taskId: Long, selectedOption: String?): TaskCompletionResult {
        val tasks = repository.getTasksPaged(0, 100, 0).first()
        val task = tasks.find { it.id == taskId } ?: return TaskCompletionResult.INVALID_TASK
        
        val updatedTask = task.copy(
            selectedOption = selectedOption,
            status = TaskStatus.COMPLETED,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            completedDate = null,
            completedDate = System.currentTimeMillis()
        )
        
        repository.updateMicroTask(updatedTask)
        
        // Apply task effects
        val effects = applyTaskEffects(task, selectedOption)
        
        // Generate butterfly effects
        generateButterflyEffects(task)
        
        return TaskCompletionResult.SUCCESS(effects)
    }
    
    /**
     * Apply effects from task completion
     */
    private suspend fun applyTaskEffects(task: MicroTask, selectedOption: String?): TaskEffects {
        val playerCountry = repository.getPlayerCountrySync() ?: return TaskEffects.EMPTY
        
        var approvalChange = 0.0
        var stabilityChange = 0.0
        var economicChange = 0.0
        
        // Parse and apply effects from task
        when (task.taskType) {
            MicroTaskType.GIVE_SPEECH -> {
                approvalChange = 2.0
            }
            MicroTaskType.ALLOCATE_FUNDS -> {
                economicChange = 1.0
                approvalChange = 1.0
            }
            MicroTaskType.SIGN_DOCUMENT -> {
                stabilityChange = 1.0
            }
            MicroTaskType.RESPOND_TO_CRISIS -> {
                approvalChange = if (random.nextBoolean()) 3.0 else -2.0
                stabilityChange = if (random.nextBoolean()) 2.0 else -1.0
            }
            else -> {
                approvalChange = (random.nextDouble() - 0.5) * 2
            }
        }
        
        // Update player country stats
        val updatedCountry = playerCountry.copy(
            approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
            stability = (playerCountry.stability + stabilityChange).coerceIn(0.0, 100.0),
            growthRate = (playerCountry.growthRate + economicChange * 0.1).coerceIn(-5.0, 10.0),
            lastUpdated = System.currentTimeMillis()
        )
        
        repository.updatePlayerCountry(updatedCountry)
        
        return TaskEffects(approvalChange, stabilityChange, economicChange)
    }
    
    /**
     * Generate butterfly effects from a task
     */
    private suspend fun generateButterflyEffects(task: MicroTask) {
        val effects = mutableListOf<ButterflyEffect>()
        
        // 30% chance to generate a butterfly effect
        if (random.nextDouble() < 0.3) {
            val effect = ButterflyEffect(
                countryId = task.countryId,
                sourceTaskId = task.id,
                sourceTaskType = task.taskType,
                effectType = EffectType.DELAYED,
                targetEntity = "player_countries",
                targetEntityId = null,
                effectDescription = "Delayed effect from: ${task.title}",
                effectMagnitude = (random.nextDouble() - 0.5) * 10,
                delayDays = 7 + random.nextInt(21),
                triggeredDate = null,
                expiryDate = null,
                chainLevel = 0
            )
            effects.add(effect)
        }
        
        effects.forEach { effect ->
            repository.insertButterflyEffect(effect)
        }
    }
    
    /**
     * Change game mode based on election results
     */
    suspend fun changeGameMode(newMode: GameMode) {
        val playerCountry = repository.getPlayerCountrySync() ?: return
        
        val updated = playerCountry.copy(
            currentGameMode = newMode,
            lastUpdated = System.currentTimeMillis()
        )
        
        repository.updatePlayerCountry(updated)
    }

    /**
     * Process pending butterfly effects
     */
    suspend fun processButterflyEffects(countryId: Long) {
        butterflyEngine.processButterflyEffects(countryId)
    }
    
    /**
     * Generate leader name
     */
    private fun generateLeaderName(): String {
        val firstNames = listOf(
            "Alexander", "Victoria", "Marcus", "Elizabeth", "William",
            "Catherine", "Jonathan", "Margaret", "Christopher", "Anne"
        )
        val lastNames = listOf(
            "Blackwood", "Sterling", "Montgomery", "Ashford", "Kensington",
            "Worthington", "Beaumont", "Harrington", "Pembroke", "Carlisle"
        )
        
        return "${firstNames.random()} ${lastNames.random()}"
    }
    
    companion object {
        private val random = kotlin.random.Random
    }
}

data class GameState(
    val playerCountry: PlayerCountry?,
    val countryId: Long
)

data class TaskEffects(
    val approvalChange: Double,
    val stabilityChange: Double,
    val economicChange: Double
) {
    companion object {
        val EMPTY = TaskEffects(0.0, 0.0, 0.0)
    }
}

sealed class GameInitializationResult {
    data class SUCCESS(val countryId: Long) : GameInitializationResult()
    data class FAILURE(val error: String) : GameInitializationResult()
}

sealed class TaskCompletionResult {
    object INVALID_TASK : TaskCompletionResult()
    data class SUCCESS(val effects: TaskEffects) : TaskCompletionResult()
}
