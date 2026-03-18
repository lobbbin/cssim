package com.country.simulator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.country.simulator.CountrySimulatorApp
import com.country.simulator.engine.GameStateManager
import com.country.simulator.generation.*
import com.country.simulator.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    
    private val app = application as CountrySimulatorApp
    private val repository = app.repository
    private val gameStateManager = GameStateManager(repository)
    private val worldInitializer = GameWorldInitializer(repository)
    
    // Game state
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()
    
    // Player country
    private val _playerCountry = MutableStateFlow<PlayerCountry?>(null)
    val playerCountry: StateFlow<PlayerCountry?> = _playerCountry.asStateFlow()
    
    // Resources
    private val _resources = MutableStateFlow<NationalResources?>(null)
    val resources: StateFlow<NationalResources?> = _resources.asStateFlow()
    
    // Tasks with pagination
    private val _tasks = MutableStateFlow<List<MicroTask>>(emptyList())
    val tasks: StateFlow<List<MicroTask>> = _tasks.asStateFlow()
    
    // Elections
    private val _elections = MutableStateFlow<List<Election>>(emptyList())
    val elections: StateFlow<List<Election>> = _elections.asStateFlow()
    
    // Campaigns
    private val _campaigns = MutableStateFlow<List<Campaign>>(emptyList())
    val campaigns: StateFlow<List<Campaign>> = _campaigns.asStateFlow()
    
    // Scandals
    private val _scandals = MutableStateFlow<List<Scandal>>(emptyList())
    val scandals: StateFlow<List<Scandal>> = _scandals.asStateFlow()
    
    // Ministries
    private val _ministries = MutableStateFlow<List<Ministry>>(emptyList())
    val ministries: StateFlow<List<Ministry>> = _ministries.asStateFlow()
    
    // Appointees
    private val _appointees = MutableStateFlow<List<Appointee>>(emptyList())
    val appointees: StateFlow<List<Appointee>> = _appointees.asStateFlow()
    
    // Budget
    private val _budget = MutableStateFlow<NationalBudget?>(null)
    val budget: StateFlow<NationalBudget?> = _budget.asStateFlow()
    
    // Tax brackets
    private val _taxBrackets = MutableStateFlow<List<TaxBracket>>(emptyList())
    val taxBrackets: StateFlow<List<TaxBracket>> = _taxBrackets.asStateFlow()
    
    // Businesses
    private val _businesses = MutableStateFlow<List<Business>>(emptyList())
    val businesses: StateFlow<List<Business>> = _businesses.asStateFlow()
    
    // Diplomatic relations
    private val _relations = MutableStateFlow<List<DiplomaticRelation>>(emptyList())
    val relations: StateFlow<List<DiplomaticRelation>> = _relations.asStateFlow()
    
    // Alliances
    private val _alliances = MutableStateFlow<List<Alliance>>(emptyList())
    val alliances: StateFlow<List<Alliance>> = _alliances.asStateFlow()
    
    // Trade blocs
    private val _tradeBlocs = MutableStateFlow<List<TradeBloc>>(emptyList())
    val tradeBlocs: StateFlow<List<TradeBloc>> = _tradeBlocs.asStateFlow()
    
    // Sanctions
    private val _sanctions = MutableStateFlow<List<Sanction>>(emptyList())
    val sanctions: StateFlow<List<Sanction>> = _sanctions.asStateFlow()
    
    // Laws
    private val _laws = MutableStateFlow<List<Law>>(emptyList())
    val laws: StateFlow<List<Law>> = _laws.asStateFlow()
    
    // Bills
    private val _bills = MutableStateFlow<List<Bill>>(emptyList())
    val bills: StateFlow<List<Bill>> = _bills.asStateFlow()
    
    // Court cases
    private val _courtCases = MutableStateFlow<List<CourtCase>>(emptyList())
    val courtCases: StateFlow<List<CourtCase>> = _courtCases.asStateFlow()
    
    // Notifications
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()
    
    // Game events
    private val _gameEvents = MutableStateFlow<List<GameEvent>>(emptyList())
    val gameEvents: StateFlow<List<GameEvent>> = _gameEvents.asStateFlow()
    
    // NPCs (paginated)
    private val _npcs = MutableStateFlow<List<NPC>>(emptyList())
    val npcs: StateFlow<List<NPC>> = _npcs.asStateFlow()
    
    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    // Current country ID
    private var currentCountryId: Long = 0
    
    init {
        loadGameData()
        startEventGenerator()
    }
    
    fun loadGameData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val playerCountry = repository.getPlayerCountrySync()
                
                if (playerCountry == null) {
                    startNewGame()
                } else {
                    currentCountryId = playerCountry.countryId
                    loadExistingGame(currentCountryId)
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Start a new game with full procedural generation
     */
    fun startNewGame(countryName: String? = null, seed: Long = System.currentTimeMillis()) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = worldInitializer.initializeWorld(countryName, seed)
                when (result) {
                    is WorldInitializationResult.Success -> {
                        currentCountryId = result.countryId
                        loadExistingGame(result.countryId)
                    }
                    is WorldInitializationResult.Failure -> {
                        _error.value = result.error
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to initialize world: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun loadExistingGame(countryId: Long) {
        currentCountryId = countryId
        
        viewModelScope.launch {
            launch {
                repository.getPlayerCountry().collect { country ->
                    _playerCountry.value = country
                    _gameState.value = GameState(country, countryId)
                }
            }
            
            launch {
                repository.getResources(countryId).collect { resources ->
                    _resources.value = resources
                }
            }
            
            launch {
                repository.getTasksPaged(countryId, 50, 0).collect { tasks ->
                    _tasks.value = tasks
                }
            }
            
            launch {
                repository.getActiveElection(countryId).collect { election ->
                    _elections.value = listOfNotNull(election)
                }
            }
            
            launch {
                repository.getActiveScandals(countryId).collect { scandals ->
                    _scandals.value = scandals
                }
            }
            
            launch {
                repository.getMinistries(countryId).collect { ministries ->
                    _ministries.value = ministries
                }
            }
            
            launch {
                repository.getAppointees(countryId).collect { appointees ->
                    _appointees.value = appointees
                }
            }
            
            launch {
                repository.getCurrentBudget(countryId).collect { budget ->
                    _budget.value = budget
                }
            }
            
            launch {
                repository.getTaxBrackets(countryId).collect { brackets ->
                    _taxBrackets.value = brackets
                }
            }
            
            launch {
                repository.getRelations(countryId).collect { relations ->
                    _relations.value = relations
                }
            }
            
            launch {
                repository.getAlliances(countryId).collect { alliances ->
                    _alliances.value = alliances
                }
            }
            
            launch {
                repository.getTradeBlocs(countryId).collect { blocs ->
                    _tradeBlocs.value = blocs
                }
            }
            
            launch {
                repository.getSanctions(countryId).collect { sanctions ->
                    _sanctions.value = sanctions
                }
            }
            
            launch {
                repository.getLaws(countryId).collect { laws ->
                    _laws.value = laws
                }
            }
            
            launch {
                repository.getBills(countryId).collect { bills ->
                    _bills.value = bills
                }
            }
            
            launch {
                repository.getNotifications(countryId).collect { notifications ->
                    _notifications.value = notifications
                }
            }
            
            launch {
                repository.getActiveEvents(countryId).collect { events ->
                    _gameEvents.value = events
                }
            }
            
            launch {
                repository.getNPCs(countryId, 20, 0).collect { npcs ->
                    _npcs.value = npcs
                }
            }
        }
    }
    
    /**
     * Complete a micro task
     */
    fun completeTask(task: MicroTask, selectedOption: String?) {
        viewModelScope.launch {
            try {
                val result = gameStateManager.completeTask(task.id, selectedOption)
                when (result) {
                    is com.country.simulator.engine.TaskCompletionResult.SUCCESS -> {
                        // Task completed successfully - refresh data
                        loadGameData()
                    }
                    is com.country.simulator.engine.TaskCompletionResult.INVALID_TASK -> {
                        _error.value = "Invalid task"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    /**
     * Resolve a game event
     */
    fun resolveEvent(event: GameEvent, selectedOption: String?) {
        viewModelScope.launch {
            try {
                val updatedEvent = event.copy(
                    selectedOption = selectedOption,
                    isResolved = true,
                    resolvedDate = System.currentTimeMillis()
                )
                repository.updateGameEvent(updatedEvent)
                
                // Apply event effects
                applyEventEffects(event, selectedOption)
                
                // Generate butterfly effects
                generateEventButterflyEffects(event)
                
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    /**
     * Apply effects from event resolution
     */
    private suspend fun applyEventEffects(event: GameEvent, selectedOption: String?) {
        val playerCountry = repository.getPlayerCountrySync() ?: return
        
        // Parse and apply effects based on event type and selection
        val approvalChange = when (event.eventType) {
            GameEventType.CRISIS -> -2.0
            GameEventType.OPPORTUNITY -> 3.0
            GameEventType.SCANDAL -> -5.0
            GameEventType.DISASTER -> -3.0
            GameEventType.BREAKTHROUGH -> 5.0
            GameEventType.PROTEST -> -2.0
            GameEventType.DIPLOMATIC_INCIDENT -> -1.0
            GameEventType.ECONOMIC_SHIFT -> when {
                event.description.contains("Boom") || event.description.contains("Strengthening") -> 3.0
                else -> -3.0
            }
            else -> 0.0
        }
        
        val updatedCountry = playerCountry.copy(
            approvalRating = (playerCountry.approvalRating + approvalChange).coerceIn(0.0, 100.0),
            stability = (playerCountry.stability + (approvalChange * 0.5)).coerceIn(0.0, 100.0),
            lastUpdated = System.currentTimeMillis()
        )
        
        repository.updatePlayerCountry(updatedCountry)
    }
    
    /**
     * Generate butterfly effects from event
     */
    private suspend fun generateEventButterflyEffects(event: GameEvent) {
        val effects = mutableListOf<ButterflyEffect>()
        
        // 40% chance to generate secondary effects
        if (kotlin.random.Random.nextDouble() < 0.4) {
            val effect = ButterflyEffect(
                countryId = event.countryId,
                sourceTaskId = event.id,
                sourceTaskType = MicroTaskType.RESPOND_TO_CRISIS,
                effectType = EffectType.DELAYED,
                targetEntity = "player_countries",
                targetEntityId = null,
                effectDescription = "Delayed effect from: ${event.title}",
                effectMagnitude = (kotlin.random.Random.nextDouble() - 0.5) * 15,
                delayDays = 7 + kotlin.random.Random.nextInt(21),
                chainLevel = 0
            )
            effects.add(effect)
        }
        
        effects.forEach { effect ->
            repository.insertButterflyEffect(effect)
        }
    }
    
    /**
     * Start background event generator
     */
    private fun startEventGenerator() {
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(60000) // Check every minute
                
                // 10% chance to generate new event each minute
                if (kotlin.random.Random.nextDouble() < 0.1 && currentCountryId > 0) {
                    generateRandomEvent()
                }
                
                // Process pending butterfly effects
                processButterflyEffects()
            }
        }
    }
    
    /**
     * Generate a random event
     */
    private suspend fun generateRandomEvent() {
        try {
            val event = EventGenerator.generateEvent(currentCountryId)
            repository.insertGameEvent(event)
            
            // Also create a notification
            repository.insertNotification(
                Notification(
                    countryId = currentCountryId,
                    notificationType = when (event.eventType) {
                        GameEventType.CRISIS, GameEventType.DISASTER -> NotificationType.ALERT
                        GameEventType.OPPORTUNITY, GameEventType.BREAKTHROUGH -> NotificationType.INFO
                        GameEventType.SCANDAL -> NotificationType.WARNING
                        else -> NotificationType.INFO
                    },
                    title = event.title,
                    message = event.description,
                    priority = when (event.priority) {
                        in 8..10 -> Priority.URGENT
                        in 5..7 -> Priority.HIGH
                        else -> Priority.MEDIUM
                    },
                    createdDate = System.currentTimeMillis(),
                    actionRequired = true
                )
            )
        } catch (e: Exception) {
            // Silently fail - event generation is not critical
        }
    }
    
    /**
     * Process pending butterfly effects
     */
    private suspend fun processButterflyEffects() {
        if (currentCountryId > 0) {
            gameStateManager.processButterflyEffects(currentCountryId)
        }
    }
    
    /**
     * Load more NPCs (for pagination)
     */
    fun loadMoreNPCs(offset: Int) {
        viewModelScope.launch {
            repository.getNPCs(currentCountryId, 20, offset).collect { npcs ->
                _npcs.value = npcs
            }
        }
    }
    
    /**
     * Load more tasks (for pagination)
     */
    fun loadMoreTasks(offset: Int) {
        viewModelScope.launch {
            repository.getTasksPaged(currentCountryId, 50, offset).collect { tasks ->
                _tasks.value = tasks
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun refreshData() {
        loadGameData()
    }
    
    /**
     * Get NPC by ID
     */
    suspend fun getNPCById(npcId: Long): NPC? {
        return repository.getNPCById(npcId)
    }
    
    /**
     * Get important NPCs
     */
    fun getImportantNPCs(): Flow<List<NPC>> {
        return repository.getImportantNPCs(currentCountryId)
    }
}

data class GameState(
    val playerCountry: PlayerCountry?,
    val countryId: Long
)
