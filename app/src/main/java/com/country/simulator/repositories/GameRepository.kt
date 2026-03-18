package com.country.simulator.repositories

import com.country.simulator.database.GameDatabase
import com.country.simulator.model.*
import kotlinx.coroutines.flow.Flow

class GameRepository(private val database: GameDatabase) {
    val countryDao = database.countryDao()
    val playerCountryDao = database.playerCountryDao()
    val resourcesDao = database.resourcesDao()
    val electionDao = database.electionDao()
    val campaignDao = database.campaignDao()
    val campaignEventDao = database.campaignEventDao()
    val scandalDao = database.scandalDao()
    val budgetDao = database.budgetDao()
    val taxBracketDao = database.taxBracketDao()
    val lawDao = database.lawDao()
    val billDao = database.billDao()
    val courtCaseDao = database.courtCaseDao()
    val ministryDao = database.ministryDao()
    val appointeeDao = database.appointeeDao()
    val infrastructureDao = database.infrastructureDao()
    val diplomacyDao = database.diplomacyDao()
    val npcDao = database.npcDao()
    val microTaskDao = database.microTaskDao()
    val butterflyEffectDao = database.butterflyEffectDao()
    val gameEventDao = database.gameEventDao()
    val notificationDao = database.notificationDao()
    
    // Player Country
    fun getPlayerCountry(): Flow<PlayerCountry?> = playerCountryDao.getPlayerCountry()
    suspend fun getPlayerCountrySync(): PlayerCountry? = playerCountryDao.getPlayerCountrySync()
    
    // Countries
    fun getAllCountries(): Flow<List<Country>> = countryDao.getAllActiveCountries()
    suspend fun getCountryById(id: Long): Country? = countryDao.getCountryById(id)
    
    // Resources
    fun getResources(countryId: Long): Flow<NationalResources?> = resourcesDao.getResourcesByCountry(countryId)
    
    // Elections
    fun getActiveElection(countryId: Long): Flow<Election?> = electionDao.getActiveElection(countryId)
    
    // Campaigns
    fun getCampaigns(electionId: Long): Flow<List<Campaign>> = campaignDao.getCampaignsForElection(electionId)
    suspend fun getCampaignById(id: Long): Campaign? = campaignDao.getCampaignById(id)
    
    // Scandals
    fun getActiveScandals(countryId: Long): Flow<List<Scandal>> = scandalDao.getActiveScandals(countryId)
    
    // Budget
    fun getCurrentBudget(countryId: Long): Flow<NationalBudget?> = budgetDao.getCurrentBudget(countryId)
    
    // Laws
    fun getLaws(countryId: Long): Flow<List<Law>> = lawDao.getLawsByCountry(countryId)
    suspend fun getLawById(id: Long): Law? = lawDao.getLawById(id)
    
    // Bills
    fun getBills(countryId: Long): Flow<List<Bill>> = billDao.getBillsByCountry(countryId)
    
    // Court Cases
    fun getCases(countryId: Long): Flow<List<CourtCase>> = courtCaseDao.getCasesByCountry(countryId)
    
    // Ministries
    fun getMinistries(countryId: Long): Flow<List<Ministry>> = ministryDao.getMinistries(countryId)
    
    // Appointees
    fun getAppointees(countryId: Long): Flow<List<Appointee>> = appointeeDao.getAppointees(countryId)
    
    // Infrastructure
    fun getProjects(countryId: Long): Flow<List<InfrastructureProject>> = infrastructureDao.getProjects(countryId)
    
    // Diplomacy
    fun getRelations(countryId: Long): Flow<List<DiplomaticRelation>> = diplomacyDao.getRelations(countryId)
    fun getAlliances(countryId: Long): Flow<List<Alliance>> = diplomacyDao.getAlliances(countryId)
    fun getTradeBlocs(countryId: Long): Flow<List<TradeBloc>> = diplomacyDao.getTradeBlocs(countryId)
    fun getSanctions(countryId: Long): Flow<List<Sanction>> = diplomacyDao.getSanctions(countryId)
    
    // NPCs - with pagination
    fun getNPCs(countryId: Long, limit: Int = 20, offset: Int = 0): Flow<List<NPC>> = 
        npcDao.getNPCs(countryId, limit, offset)
    fun getImportantNPCs(countryId: Long): Flow<List<NPC>> = npcDao.getImportantNPCs(countryId)
    suspend fun getNPCById(id: Long): NPC? = npcDao.getNPCById(id)
    
    // Micro Tasks - with pagination
    fun getPendingTasks(countryId: Long): Flow<List<MicroTask>> = microTaskDao.getPendingTasks(countryId)
    fun getTasksPaged(countryId: Long, limit: Int = 20, offset: Int = 0): Flow<List<MicroTask>> = 
        microTaskDao.getTasksPaged(countryId, limit, offset)
    fun getTasksForMacro(macroId: Long): Flow<List<MicroTask>> = microTaskDao.getTasksForMacro(macroId)
    
    // Butterfly Effects
    fun getPendingEffects(countryId: Long): Flow<List<ButterflyEffect>> = butterflyEffectDao.getPendingEffects(countryId)
    
    // Game Events
    fun getActiveEvents(countryId: Long): Flow<List<GameEvent>> = gameEventDao.getActiveEvents(countryId)
    
    // Notifications
    fun getUnreadNotifications(countryId: Long): Flow<List<Notification>> = notificationDao.getUnreadNotifications(countryId)
    fun getNotifications(countryId: Long): Flow<List<Notification>> = notificationDao.getNotifications(countryId)
    
    // Insert methods
    suspend fun insertCountry(country: Country): Long = countryDao.insert(country)
    suspend fun insertPlayerCountry(playerCountry: PlayerCountry): Long = playerCountryDao.insert(playerCountry)
    suspend fun insertResources(resources: NationalResources): Long = resourcesDao.insert(resources)
    suspend fun insertElection(election: Election): Long = electionDao.insert(election)
    suspend fun insertCampaign(campaign: Campaign): Long = campaignDao.insert(campaign)
    suspend fun insertCampaignEvent(event: CampaignEvent): Long = campaignEventDao.insert(event)
    suspend fun insertScandal(scandal: Scandal): Long = scandalDao.insert(scandal)
    suspend fun insertBudget(budget: NationalBudget): Long = budgetDao.insert(budget)
    suspend fun insertTaxBracket(bracket: TaxBracket): Long = taxBracketDao.insert(bracket)
    suspend fun insertLaw(law: Law): Long = lawDao.insert(law)
    suspend fun insertBill(bill: Bill): Long = billDao.insert(bill)
    suspend fun insertCourtCase(case_: CourtCase): Long = courtCaseDao.insert(case_)
    suspend fun insertMinistry(ministry: Ministry): Long = ministryDao.insert(ministry)
    suspend fun insertAppointee(appointee: Appointee): Long = appointeeDao.insert(appointee)
    suspend fun insertInfrastructureProject(project: InfrastructureProject): Long = infrastructureDao.insert(project)
    suspend fun insertDiplomaticRelation(relation: DiplomaticRelation): Long = diplomacyDao.insert(relation)
    suspend fun insertNPC(npc: NPC): Long = npcDao.insert(npc)
    suspend fun insertMicroTask(task: MicroTask): Long = microTaskDao.insert(task)
    suspend fun insertButterflyEffect(effect: ButterflyEffect): Long = butterflyEffectDao.insert(effect)
    suspend fun insertGameEvent(event: GameEvent): Long = gameEventDao.insert(event)
    suspend fun insertNotification(notification: Notification): Long = notificationDao.insert(notification)
    
    // Update methods
    suspend fun updateCountry(country: Country) = countryDao.update(country)
    suspend fun updatePlayerCountry(playerCountry: PlayerCountry) = playerCountryDao.update(playerCountry)
    suspend fun updateResources(resources: NationalResources) = resourcesDao.update(resources)
    suspend fun updateElection(election: Election) = electionDao.update(election)
    suspend fun updateCampaign(campaign: Campaign) = campaignDao.update(campaign)
    suspend fun updateCampaignEvent(event: CampaignEvent) = campaignEventDao.update(event)
    suspend fun updateScandal(scandal: Scandal) = scandalDao.update(scandal)
    suspend fun updateBudget(budget: NationalBudget) = budgetDao.update(budget)
    suspend fun updateTaxBracket(bracket: TaxBracket) = taxBracketDao.update(bracket)
    suspend fun updateLaw(law: Law) = lawDao.update(law)
    suspend fun updateBill(bill: Bill) = billDao.update(bill)
    suspend fun updateCourtCase(case_: CourtCase) = courtCaseDao.update(case_)
    suspend fun updateMinistry(ministry: Ministry) = ministryDao.update(ministry)
    suspend fun updateAppointee(appointee: Appointee) = appointeeDao.update(appointee)
    suspend fun updateInfrastructureProject(project: InfrastructureProject) = infrastructureDao.update(project)
    suspend fun updateDiplomaticRelation(relation: DiplomaticRelation) = diplomacyDao.update(relation)
    suspend fun updateNPC(npc: NPC) = npcDao.update(npc)
    suspend fun updateMicroTask(task: MicroTask) = microTaskDao.update(task)
    suspend fun updateButterflyEffect(effect: ButterflyEffect) = butterflyEffectDao.update(effect)
    suspend fun updateGameEvent(event: GameEvent) = gameEventDao.update(event)
    suspend fun updateNotification(notification: Notification) = notificationDao.update(notification)
    
    // Campaign helpers
    suspend fun addCampaignFollowers(campaignId: Long, increase: Int) = campaignDao.addFollowers(campaignId, increase)
    suspend fun addCampaignSpending(campaignId: Long, amount: Double) = campaignDao.addSpending(campaignId, amount)
}
