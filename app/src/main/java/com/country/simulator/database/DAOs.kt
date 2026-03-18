package com.country.simulator.database

import androidx.room.*
import com.country.simulator.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Query("SELECT * FROM countries WHERE isPlayerCountry = 1 LIMIT 1")
    fun getPlayerCountry(): Flow<Country?>
    
    @Query("SELECT * FROM countries WHERE id = :id")
    suspend fun getCountryById(id: Long): Country?
    
    @Query("SELECT * FROM countries WHERE isActive = 1")
    fun getAllActiveCountries(): Flow<List<Country>>
    
    @Query("SELECT * FROM countries WHERE id != :playerCountryId AND isActive = 1")
    fun getForeignCountries(playerCountryId: Long): Flow<List<Country>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(country: Country): Long
    
    @Update
    suspend fun update(country: Country)
    
    @Delete
    suspend fun delete(country: Country)
}

@Dao
interface PlayerCountryDao {
    @Query("SELECT * FROM player_countries LIMIT 1")
    fun getPlayerCountry(): Flow<PlayerCountry?>
    
    @Query("SELECT * FROM player_countries LIMIT 1")
    suspend fun getPlayerCountrySync(): PlayerCountry?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playerCountry: PlayerCountry): Long
    
    @Update
    suspend fun update(playerCountry: PlayerCountry)
}

@Dao
interface ResourcesDao {
    @Query("SELECT * FROM resources WHERE countryId = :countryId LIMIT 1")
    fun getResourcesByCountry(countryId: Long): Flow<NationalResources?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resources: NationalResources): Long
    
    @Update
    suspend fun update(resources: NationalResources)
}

@Dao
interface ElectionDao {
    @Query("SELECT * FROM elections WHERE countryId = :countryId AND isActive = 1")
    fun getActiveElection(countryId: Long): Flow<Election?>
    
    @Query("SELECT * FROM elections WHERE countryId = :countryId ORDER BY scheduledDate DESC")
    fun getElectionsByCountry(countryId: Long): Flow<List<Election>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(election: Election): Long
    
    @Update
    suspend fun update(election: Election)
}

@Dao
interface CampaignDao {
    @Query("SELECT * FROM campaigns WHERE electionId = :electionId AND isActive = 1")
    fun getCampaignsForElection(electionId: Long): Flow<List<Campaign>>
    
    @Query("SELECT * FROM campaigns WHERE id = :id")
    suspend fun getCampaignById(id: Long): Campaign?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(campaign: Campaign): Long
    
    @Update
    suspend fun update(campaign: Campaign)
    
    @Query("UPDATE campaigns SET followers = followers + :increase WHERE id = :campaignId")
    suspend fun addFollowers(campaignId: Long, increase: Int)
    
    @Query("UPDATE campaigns SET spent = spent + :amount WHERE id = :campaignId")
    suspend fun addSpending(campaignId: Long, amount: Double)
}

@Dao
interface CampaignEventDao {
    @Query("SELECT * FROM campaign_events WHERE campaignId = :campaignId ORDER BY scheduledDate")
    fun getEventsForCampaign(campaignId: Long): Flow<List<CampaignEvent>>
    
    @Query("SELECT * FROM campaign_events WHERE completed = 0 AND scheduledDate <= :currentTime")
    fun getPendingEvents(currentTime: Long): Flow<List<CampaignEvent>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: CampaignEvent): Long
    
    @Update
    suspend fun update(event: CampaignEvent)
}

@Dao
interface ScandalDao {
    @Query("SELECT * FROM scandals WHERE countryId = :countryId AND resolved = 0")
    fun getActiveScandals(countryId: Long): Flow<List<Scandal>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scandal: Scandal): Long
    
    @Update
    suspend fun update(scandal: Scandal)
}

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets WHERE countryId = :countryId ORDER BY fiscalYear DESC LIMIT 1")
    fun getCurrentBudget(countryId: Long): Flow<NationalBudget?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: NationalBudget): Long
    
    @Update
    suspend fun update(budget: NationalBudget)
}

@Dao
interface TaxBracketDao {
    @Query("SELECT * FROM tax_brackets WHERE countryId = :countryId AND isActive = 1")
    fun getTaxBrackets(countryId: Long): Flow<List<TaxBracket>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bracket: TaxBracket): Long
    
    @Update
    suspend fun update(bracket: TaxBracket)
    
    @Delete
    suspend fun delete(bracket: TaxBracket)
}

@Dao
interface LawDao {
    @Query("SELECT * FROM laws WHERE countryId = :countryId ORDER BY introducedDate DESC")
    fun getLawsByCountry(countryId: Long): Flow<List<Law>>
    
    @Query("SELECT * FROM laws WHERE countryId = :countryId AND status = :status")
    fun getLawsByStatus(countryId: Long, status: LawStatus): Flow<List<Law>>
    
    @Query("SELECT * FROM laws WHERE id = :id")
    suspend fun getLawById(id: Long): Law?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(law: Law): Long
    
    @Update
    suspend fun update(law: Law)
}

@Dao
interface BillDao {
    @Query("SELECT * FROM bills WHERE countryId = :countryId ORDER BY introducedDate DESC")
    fun getBillsByCountry(countryId: Long): Flow<List<Bill>>
    
    @Query("SELECT * FROM bills WHERE currentStage = :stage")
    fun getBillsByStage(stage: BillStage): Flow<List<Bill>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bill: Bill): Long
    
    @Update
    suspend fun update(bill: Bill)
}

@Dao
interface CourtCaseDao {
    @Query("SELECT * FROM court_cases WHERE countryId = :countryId ORDER BY filingDate DESC")
    fun getCasesByCountry(countryId: Long): Flow<List<CourtCase>>
    
    @Query("SELECT * FROM court_cases WHERE status = :status")
    fun getCasesByStatus(status: CaseStatus): Flow<List<CourtCase>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(case_: CourtCase): Long
    
    @Update
    suspend fun update(case_: CourtCase)
}

@Dao
interface MinistryDao {
    @Query("SELECT * FROM ministries WHERE countryId = :countryId")
    fun getMinistries(countryId: Long): Flow<List<Ministry>>
    
    @Query("SELECT * FROM ministries WHERE countryId = :countryId AND name = :name LIMIT 1")
    suspend fun getMinistryByName(countryId: Long, name: String): Ministry?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ministry: Ministry): Long
    
    @Update
    suspend fun update(ministry: Ministry)
}

@Dao
interface AppointeeDao {
    @Query("SELECT * FROM appointees WHERE countryId = :countryId AND isActive = 1")
    fun getAppointees(countryId: Long): Flow<List<Appointee>>
    
    @Query("SELECT * FROM appointees WHERE countryId = :countryId AND ministry = :ministry")
    fun getAppointeeByMinistry(countryId: Long, ministry: MinistryType): Flow<Appointee?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appointee: Appointee): Long
    
    @Update
    suspend fun update(appointee: Appointee)
}

@Dao
interface InfrastructureDao {
    @Query("SELECT * FROM infrastructure_projects WHERE countryId = :countryId ORDER BY startDate DESC")
    fun getProjects(countryId: Long): Flow<List<InfrastructureProject>>
    
    @Query("SELECT * FROM infrastructure_projects WHERE countryId = :countryId AND status = :status")
    fun getProjectsByStatus(countryId: Long, status: ProjectStatus): Flow<List<InfrastructureProject>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: InfrastructureProject): Long
    
    @Update
    suspend fun update(project: InfrastructureProject)
}

@Dao
interface DiplomacyDao {
    @Query("SELECT * FROM diplomatic_relations WHERE countryId = :countryId")
    fun getRelations(countryId: Long): Flow<List<DiplomaticRelation>>
    
    @Query("SELECT * FROM alliances WHERE countryId = :countryId AND isActive = 1")
    fun getAlliances(countryId: Long): Flow<List<Alliance>>
    
    @Query("SELECT * FROM trade_blocs WHERE countryId = :countryId AND isActive = 1")
    fun getTradeBlocs(countryId: Long): Flow<List<TradeBloc>>
    
    @Query("SELECT * FROM sanctions WHERE countryId = :countryId AND isActive = 1")
    fun getSanctions(countryId: Long): Flow<List<Sanction>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(relation: DiplomaticRelation): Long
    
    @Update
    suspend fun update(relation: DiplomaticRelation)
}

@Dao
interface NPCDao {
    @Query("SELECT * FROM npcs WHERE countryId = :countryId AND isActive = 1 LIMIT :limit OFFSET :offset")
    fun getNPCs(countryId: Long, limit: Int, offset: Int): Flow<List<NPC>>
    
    @Query("SELECT * FROM npcs WHERE countryId = :countryId AND isImportant = 1")
    fun getImportantNPCs(countryId: Long): Flow<List<NPC>>
    
    @Query("SELECT * FROM npcs WHERE id = :id")
    suspend fun getNPCById(id: Long): NPC?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(npc: NPC): Long
    
    @Update
    suspend fun update(npc: NPC)
}

@Dao
interface MicroTaskDao {
    @Query("SELECT * FROM micro_tasks WHERE countryId = :countryId AND status = 'PENDING' ORDER BY createdDate")
    fun getPendingTasks(countryId: Long): Flow<List<MicroTask>>
    
    @Query("SELECT * FROM micro_tasks WHERE countryId = :countryId ORDER BY createdDate DESC LIMIT :limit OFFSET :offset")
    fun getTasksPaged(countryId: Long, limit: Int, offset: Int): Flow<List<MicroTask>>
    
    @Query("SELECT * FROM micro_tasks WHERE relatedMacroId = :macroId")
    fun getTasksForMacro(macroId: Long): Flow<List<MicroTask>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: MicroTask): Long
    
    @Update
    suspend fun update(task: MicroTask)
}

@Dao
interface ButterflyEffectDao {
    @Query("SELECT * FROM butterfly_effects WHERE countryId = :countryId AND isTriggered = 0 ORDER BY delayDays")
    fun getPendingEffects(countryId: Long): Flow<List<ButterflyEffect>>
    
    @Query("SELECT * FROM butterfly_effects WHERE sourceTaskId = :taskId")
    fun getEffectsForTask(taskId: Long): Flow<List<ButterflyEffect>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(effect: ButterflyEffect): Long
    
    @Update
    suspend fun update(effect: ButterflyEffect)
}

@Dao
interface GameEventDao {
    @Query("SELECT * FROM game_events WHERE countryId = :countryId AND isResolved = 0 ORDER BY priority DESC")
    fun getActiveEvents(countryId: Long): Flow<List<GameEvent>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: GameEvent): Long
    
    @Update
    suspend fun update(event: GameEvent)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE countryId = :countryId AND isRead = 0 ORDER BY createdDate DESC")
    fun getUnreadNotifications(countryId: Long): Flow<List<Notification>>
    
    @Query("SELECT * FROM notifications WHERE countryId = :countryId ORDER BY createdDate DESC LIMIT 50")
    fun getNotifications(countryId: Long): Flow<List<Notification>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: Notification): Long
    
    @Update
    suspend fun update(notification: Notification)
}
