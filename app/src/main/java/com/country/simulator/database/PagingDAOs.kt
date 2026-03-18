package com.country.simulator.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.country.simulator.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NPCDao {
    @Query("SELECT * FROM npcs WHERE countryId = :countryId AND isActive = 1 ORDER BY id ASC")
    fun getNPCsPaged(countryId: Long): PagingSource<Int, NPC>
    
    @Query("SELECT * FROM npcs WHERE countryId = :countryId AND isImportant = 1 ORDER BY id ASC")
    fun getImportantNPCs(countryId: Long): Flow<List<NPC>>
    
    @Query("SELECT * FROM npcs WHERE id = :id")
    suspend fun getNPCById(id: Long): NPC?
    
    @Query("SELECT * FROM npcs WHERE countryId = :countryId AND occupationType = :type ORDER BY id ASC")
    fun getNPCsByOccupation(countryId: Long, type: OccupationType): Flow<List<NPC>>
    
    @Query("SELECT * FROM npcs WHERE countryId = :countryId AND politicalLeanings < :threshold ORDER BY approvalOfGovernment ASC")
    fun getDiscontentNPCs(countryId: Long, threshold: Int = -30): Flow<List<NPC>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(npc: NPC): Long
    
    @Update
    suspend fun update(npc: NPC)
    
    @Query("UPDATE npcs SET approvalOfGovernment = approvalOfGovernment + :change WHERE countryId = :countryId")
    suspend fun adjustAllNPCApproval(countryId: Long, change: Double)
    
    @Query("UPDATE npcs SET approvalOfGovernment = approvalOfGovernment + :change WHERE id = :npcId")
    suspend fun adjustNPCApproval(npcId: Long, change: Double)
    
    @Query("SELECT COUNT(*) FROM npcs WHERE countryId = :countryId AND isActive = 1")
    suspend fun getNPCCountry(countryId: Long): Int
}

@Dao
interface BusinessDao {
    @Query("SELECT * FROM businesses WHERE countryId = :countryId ORDER BY revenue DESC")
    fun getBusinessesPaged(countryId: Long): PagingSource<Int, Business>
    
    @Query("SELECT * FROM businesses WHERE countryId = :countryId AND licenseStatus = :status ORDER BY id ASC")
    fun getBusinessesByLicenseStatus(countryId: Long, status: LicenseStatus): Flow<List<Business>>
    
    @Query("SELECT * FROM businesses WHERE countryId = :countryId AND isUnderAudit = 1")
    fun getBusinessesUnderAudit(countryId: Long): Flow<List<Business>>
    
    @Query("SELECT * FROM businesses WHERE id = :id")
    suspend fun getBusinessById(id: Long): Business?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(business: Business): Long
    
    @Update
    suspend fun update(business: Business)
    
    @Query("UPDATE businesses SET licenseStatus = :status WHERE id = :businessId")
    suspend fun updateLicenseStatus(businessId: Long, status: LicenseStatus)
    
    @Query("UPDATE businesses SET taxPaid = taxPaid + :amount WHERE id = :businessId")
    suspend fun addTaxPayment(businessId: Long, amount: Double)
}

@Dao
interface ImmigrationCaseDao {
    @Query("SELECT * FROM immigration_cases WHERE countryId = :countryId ORDER BY applicationDate ASC")
    fun getImmigrationCasesPaged(countryId: Long): PagingSource<Int, ImmigrationCase>
    
    @Query("SELECT * FROM immigration_cases WHERE countryId = :countryId AND status = :status ORDER BY applicationDate ASC")
    fun getCasesByStatus(countryId: Long, status: ImmigrationStatus): Flow<List<ImmigrationCase>>
    
    @Query("SELECT * FROM immigration_cases WHERE countryId = :countryId AND isRefugee = 1 AND status = 'PENDING'")
    fun getPendingRefugeeCases(countryId: Long): Flow<List<ImmigrationCase>>
    
    @Query("SELECT * FROM immigration_cases WHERE countryId = :countryId AND isHighTalent = 1")
    fun getHighTalentCases(countryId: Long): Flow<List<ImmigrationCase>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(case_: ImmigrationCase): Long
    
    @Update
    suspend fun update(case_: ImmigrationCase)
    
    @Query("UPDATE immigration_cases SET status = :status, decisionDate = :decisionDate, decision = :decision WHERE id = :caseId")
    suspend fun decideCase(caseId: Long, status: ImmigrationStatus, decisionDate: Long, decision: String)
}

@Dao
interface WorkPermitDao {
    @Query("SELECT * FROM work_permits WHERE countryId = :countryId ORDER BY startDate ASC")
    fun getWorkPermitsPaged(countryId: Long): PagingSource<Int, WorkPermit>
    
    @Query("SELECT * FROM work_permits WHERE countryId = :countryId AND status = :status")
    fun getPermitsByStatus(countryId: Long, status: PermitStatus): Flow<List<WorkPermit>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(permit: WorkPermit): Long
    
    @Update
    suspend fun update(permit: WorkPermit)
}

@Dao
interface ConstructionPermitDao {
    @Query("SELECT * FROM construction_permits WHERE countryId = :countryId ORDER BY applicationDate ASC")
    fun getConstructionPermitsPaged(countryId: Long): PagingSource<Int, ConstructionPermit>
    
    @Query("SELECT * FROM construction_permits WHERE countryId = :countryId AND status = :status")
    fun getPermitsByStatus(countryId: Long, status: PermitStatus): Flow<List<ConstructionPermit>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(permit: ConstructionPermit): Long
    
    @Update
    suspend fun update(permit: ConstructionPermit)
    
    @Query("UPDATE construction_permits SET status = :status, approvalDate = :approvalDate WHERE id = :permitId")
    suspend fun approvePermit(permitId: Long, status: PermitStatus, approvalDate: Long)
}

@Dao
interface BusScheduleDao {
    @Query("SELECT * FROM bus_schedules WHERE countryId = :countryId ORDER BY neighborhood ASC")
    fun getBusSchedulesPaged(countryId: Long): PagingSource<Int, BusSchedule>
    
    @Query("SELECT * FROM bus_schedules WHERE countryId = :countryId AND neighborhood = :neighborhood")
    fun getSchedulesByNeighborhood(countryId: Long, neighborhood: String): Flow<List<BusSchedule>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(schedule: BusSchedule): Long
    
    @Update
    suspend fun update(schedule: BusSchedule)
    
    @Query("UPDATE bus_schedules SET frequency = :frequency WHERE id = :scheduleId")
    suspend fun updateFrequency(scheduleId: Long, frequency: Int)
}

@Dao
interface StadiumVendorDao {
    @Query("SELECT * FROM stadium_vendors WHERE countryId = :countryId ORDER BY stadiumName ASC")
    fun getStadiumVendorsPaged(countryId: Long): PagingSource<Int, StadiumVendor>
    
    @Query("SELECT * FROM stadium_vendors WHERE countryId = :countryId AND stadiumId = :stadiumId")
    fun getVendorsByStadium(countryId: Long, stadiumId: Long): Flow<List<StadiumVendor>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vendor: StadiumVendor): Long
    
    @Update
    suspend fun update(vendor: StadiumVendor)
}

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players WHERE countryId = :countryId ORDER BY performance DESC")
    fun getPlayersPaged(countryId: Long): PagingSource<Int, Player>
    
    @Query("SELECT * FROM players WHERE countryId = :countryId AND sportType = :sport ORDER BY performance DESC")
    fun getPlayersBySport(countryId: Long, sport: SportType): Flow<List<Player>>
    
    @Query("SELECT * FROM players WHERE countryId = :countryId AND isSuspended = 1")
    fun getSuspendedPlayers(countryId: Long): Flow<List<Player>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(player: Player): Long
    
    @Update
    suspend fun update(player: Player)
}

@Dao
interface MedicineStockpileDao {
    @Query("SELECT * FROM medicine_stockpiles WHERE countryId = :countryId ORDER BY expiryDate ASC")
    fun getMedicineStockpilesPaged(countryId: Long): PagingSource<Int, MedicineStockpile>
    
    @Query("SELECT * FROM medicine_stockpiles WHERE countryId = :countryId AND isCritical = 1")
    fun getCriticalMedicines(countryId: Long): Flow<List<MedicineStockpile>>
    
    @Query("SELECT * FROM medicine_stockpiles WHERE countryId = :countryId AND quantity < criticalLevel")
    fun getLowStockMedicines(countryId: Long): Flow<List<MedicineStockpile>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stockpile: MedicineStockpile): Long
    
    @Update
    suspend fun update(stockpile: MedicineStockpile)
    
    @Query("UPDATE medicine_stockpiles SET quantity = quantity + :added WHERE id = :stockpileId")
    suspend fun addStock(stockpileId: Long, added: Int)
}

@Dao
interface WelfarePaymentDao {
    @Query("SELECT * FROM welfare_payments WHERE countryId = :countryId ORDER BY recipientName ASC")
    fun getWelfarePaymentsPaged(countryId: Long): PagingSource<Int, WelfarePayment>
    
    @Query("SELECT * FROM welfare_payments WHERE countryId = :countryId AND paymentType = :type")
    fun getPaymentsByType(countryId: Long, type: WelfareType): Flow<List<WelfarePayment>>
    
    @Query("SELECT * FROM welfare_payments WHERE countryId = :countryId AND status = 'ACTIVE'")
    fun getActivePayments(countryId: Long): Flow<List<WelfarePayment>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: WelfarePayment): Long
    
    @Update
    suspend fun update(payment: WelfarePayment)
    
    @Query("UPDATE welfare_payments SET status = :status WHERE id = :paymentId")
    suspend fun updatePaymentStatus(paymentId: Long, status: PaymentStatus)
}

@Dao
interface PurchaseOrderDao {
    @Query("SELECT * FROM purchase_orders WHERE countryId = :countryId ORDER BY requestedDate DESC")
    fun getPurchaseOrdersPaged(countryId: Long): PagingSource<Int, PurchaseOrder>
    
    @Query("SELECT * FROM purchase_orders WHERE countryId = :countryId AND status = 'PENDING'")
    fun getPendingPurchaseOrders(countryId: Long): Flow<List<PurchaseOrder>>
    
    @Query("SELECT * FROM purchase_orders WHERE countryId = :countryId AND isSuspicious = 1")
    fun getSuspiciousOrders(countryId: Long): Flow<List<PurchaseOrder>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: PurchaseOrder): Long
    
    @Update
    suspend fun update(order: PurchaseOrder)
    
    @Query("UPDATE purchase_orders SET status = :status, approvedDate = :approvedDate, approvedBy = :approver WHERE id = :orderId")
    suspend fun approveOrder(orderId: Long, status: PurchaseOrderStatus, approvedDate: Long, approver: String)
    
    @Query("UPDATE purchase_orders SET status = 'REJECTED' WHERE id = :orderId")
    suspend fun rejectOrder(orderId: Long)
}

@Dao
interface ExpenseReportDao {
    @Query("SELECT * FROM expense_reports WHERE countryId = :countryId ORDER BY reportDate DESC")
    fun getExpenseReportsPaged(countryId: Long): PagingSource<Int, ExpenseReport>
    
    @Query("SELECT * FROM expense_reports WHERE countryId = :countryId AND isFlagged = 1")
    fun getFlaggedExpenses(countryId: Long): Flow<List<ExpenseReport>>
    
    @Query("SELECT * FROM expense_reports WHERE countryId = :countryId AND status = 'PENDING'")
    fun getPendingExpenses(countryId: Long): Flow<List<ExpenseReport>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: ExpenseReport): Long
    
    @Update
    suspend fun update(report: ExpenseReport)
}

@Dao
interface WitnessDao {
    @Query("SELECT * FROM witnesses WHERE countryId = :countryId ORDER BY credibility DESC")
    fun getWitnessesPaged(countryId: Long): PagingSource<Int, Witness>
    
    @Query("SELECT * FROM witnesses WHERE countryId = :countryId AND relatedCaseId = :caseId")
    fun getWitnessesForCase(countryId: Long, caseId: Long): Flow<List<Witness>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(witness: Witness): Long
    
    @Update
    suspend fun update(witness: Witness)
}

@Dao
interface JurorDao {
    @Query("SELECT * FROM jurors WHERE countryId = :countryId ORDER BY id ASC")
    fun getJurorsPaged(countryId: Long): PagingSource<Int, Juror>
    
    @Query("SELECT * FROM jurors WHERE countryId = :countryId AND selectionStatus = 'POOL'")
    fun getJurorPool(countryId: Long): Flow<List<Juror>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(juror: Juror): Long
    
    @Update
    suspend fun update(juror: Juror)
}

@Dao
interface DonorDao {
    @Query("SELECT * FROM donors WHERE countryId = :countryId ORDER BY totalDonated DESC")
    fun getDonorsPaged(countryId: Long): PagingSource<Int, Donor>
    
    @Query("SELECT * FROM donors WHERE countryId = :countryId AND isControversial = 1")
    fun getControversialDonors(countryId: Long): Flow<List<Donor>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(donor: Donor): Long
    
    @Update
    suspend fun update(donor: Donor)
}

@Dao
interface LobbyistDao {
    @Query("SELECT * FROM lobbyists WHERE countryId = :countryId ORDER BY influence DESC")
    fun getLobbyistsPaged(countryId: Long): PagingSource<Int, Lobbyist>
    
    @Query("SELECT * FROM lobbyists WHERE countryId = :countryId AND industry = :industry")
    fun getLobbyistsByIndustry(countryId: Long, industry: String): Flow<List<Lobbyist>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lobbyist: Lobbyist): Long
    
    @Update
    suspend fun update(lobbyist: Lobbyist)
}

@Dao
interface ActivistDao {
    @Query("SELECT * FROM activists WHERE countryId = :countryId ORDER BY followers DESC")
    fun getActivistsPaged(countryId: Long): PagingSource<Int, Activist>
    
    @Query("SELECT * FROM activists WHERE countryId = :countryId AND cause = :cause")
    fun getActivistsByCause(countryId: Long, cause: String): Flow<List<Activist>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activist: Activist): Long
    
    @Update
    suspend fun update(activist: Activist)
}

@Dao
interface ContractorDao {
    @Query("SELECT * FROM contractors WHERE countryId = :countryId ORDER BY rating DESC")
    fun getContractorsPaged(countryId: Long): PagingSource<Int, Contractor>
    
    @Query("SELECT * FROM contractors WHERE countryId = :countryId AND corruptionAllegations > 0")
    fun getContractorsWithAllegations(countryId: Long): Flow<List<Contractor>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contractor: Contractor): Long
    
    @Update
    suspend fun update(contractor: Contractor)
}

@Dao
interface ForeignLeaderDao {
    @Query("SELECT * FROM foreign_leaders WHERE countryId = :countryId ORDER BY relationToPlayer DESC")
    fun getForeignLeadersPaged(countryId: Long): PagingSource<Int, ForeignLeader>
    
    @Query("SELECT * FROM foreign_leaders WHERE countryId = :countryId AND foreignCountryId = :foreignCountryId")
    fun getLeaderForCountry(countryId: Long, foreignCountryId: Long): Flow<ForeignLeader?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(leader: ForeignLeader): Long
    
    @Update
    suspend fun update(leader: ForeignLeader)
    
    @Query("UPDATE foreign_leaders SET relationToPlayer = relationToPlayer + :change WHERE id = :leaderId")
    suspend fun adjustRelation(leaderId: Long, change: Int)
}

@Dao
interface SpyDao {
    @Query("SELECT * FROM spies WHERE countryId = :countryId ORDER BY rank ASC")
    fun getSpiesPaged(countryId: Long): PagingSource<Int, Spy>
    
    @Query("SELECT * FROM spies WHERE countryId = :countryId AND isCompromised = 1")
    fun getCompromisedSpies(countryId: Long): Flow<List<Spy>>
    
    @Query("SELECT * FROM spies WHERE countryId = :countryId AND assignedCountry = :assignedCountry")
    fun getSpiesInCountry(countryId: Long, assignedCountry: Long): Flow<List<Spy>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(spy: Spy): Long
    
    @Update
    suspend fun update(spy: Spy)
    
    @Query("UPDATE spies SET isCompromised = 1, coverStatus = 'COMPROMISED' WHERE id = :spyId")
    suspend fun compromiseSpy(spyId: Long)
}

@Dao
interface DiplomatDao {
    @Query("SELECT * FROM diplomats WHERE countryId = :countryId ORDER BY effectiveness DESC")
    fun getDiplomatsPaged(countryId: Long): PagingSource<Int, Diplomat>
    
    @Query("SELECT * FROM diplomats WHERE countryId = :countryId AND assignedCountry = :assignedCountry")
    fun getDiplomatInCountry(countryId: Long, assignedCountry: Long): Flow<Diplomat?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diplomat: Diplomat): Long
    
    @Update
    suspend fun update(diplomat: Diplomat)
}

@Dao
interface EliteAthleteDao {
    @Query("SELECT * FROM elite_athletes WHERE countryId = :countryId ORDER BY worldRanking ASC")
    fun getEliteAthletesPaged(countryId: Long): PagingSource<Int, EliteAthlete>
    
    @Query("SELECT * FROM elite_athletes WHERE countryId = :countryId AND sportType = :sport")
    fun getAthletesBySport(countryId: Long, sport: SportType): Flow<List<EliteAthlete>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(athlete: EliteAthlete): Long
    
    @Update
    suspend fun update(athlete: EliteAthlete)
}

@Dao
interface JobTrainingProgramDao {
    @Query("SELECT * FROM job_training_programs WHERE countryId = :countryId ORDER BY programName ASC")
    fun getJobTrainingProgramsPaged(countryId: Long): PagingSource<Int, JobTrainingProgram>
    
    @Query("SELECT * FROM job_training_programs WHERE countryId = :countryId AND isActive = 1")
    fun getActivePrograms(countryId: Long): Flow<List<JobTrainingProgram>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(program: JobTrainingProgram): Long
    
    @Update
    suspend fun update(program: JobTrainingProgram)
}

@Dao
interface CommunityProjectDao {
    @Query("SELECT * FROM community_projects WHERE countryId = :countryId ORDER BY startDate DESC")
    fun getCommunityProjectsPaged(countryId: Long): PagingSource<Int, CommunityProject>
    
    @Query("SELECT * FROM community_projects WHERE countryId = :countryId AND neighborhood = :neighborhood")
    fun getProjectsByNeighborhood(countryId: Long, neighborhood: String): Flow<List<CommunityProject>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: CommunityProject): Long
    
    @Update
    suspend fun update(project: CommunityProject)
}
