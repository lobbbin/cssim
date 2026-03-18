package com.country.simulator.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.country.simulator.database.GameDatabase
import com.country.simulator.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository extensions for Paging 3 support
 * Enables lazy loading of thousands of entries without memory issues
 */
class PagingRepository(private val database: GameDatabase) {
    
    private val npcDao = database.npcDao()
    private val businessDao = database.businessDao()
    private val immigrationCaseDao = database.immigrationCaseDao()
    private val workPermitDao = database.workPermitDao()
    private val constructionPermitDao = database.constructionPermitDao()
    private val busScheduleDao = database.busScheduleDao()
    private val stadiumVendorDao = database.stadiumVendorDao()
    private val playerDao = database.playerDao()
    private val medicineStockpileDao = database.medicineStockpileDao()
    private val welfarePaymentDao = database.welfarePaymentDao()
    private val purchaseOrderDao = database.purchaseOrderDao()
    private val expenseReportDao = database.expenseReportDao()
    private val witnessDao = database.witnessDao()
    private val jurorDao = database.jurorDao()
    private val donorDao = database.donorDao()
    private val lobbyistDao = database.lobbyistDao()
    private val activistDao = database.activistDao()
    private val contractorDao = database.contractorDao()
    private val foreignLeaderDao = database.foreignLeaderDao()
    private val spyDao = database.spyDao()
    private val diplomatDao = database.diplomatDao()
    private val eliteAthleteDao = database.eliteAthleteDao()
    private val jobTrainingProgramDao = database.jobTrainingProgramDao()
    private val communityProjectDao = database.communityProjectDao()
    
    // Paging config for all lists
    private val pagingConfig = PagingConfig(
        pageSize = 20,
        prefetchDistance = 10,
        enablePlaceholders = false,
        initialLoadSize = 40,
        maxSize = 100
    )
    
    /**
     * Get paginated NPCs
     */
    fun getNPCsPaged(countryId: Long): Flow<PagingData<NPC>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { npcDao.getNPCsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated businesses
     */
    fun getBusinessesPaged(countryId: Long): Flow<PagingData<Business>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { businessDao.getBusinessesPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated immigration cases
     */
    fun getImmigrationCasesPaged(countryId: Long): Flow<PagingData<ImmigrationCase>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { immigrationCaseDao.getImmigrationCasesPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated work permits
     */
    fun getWorkPermitsPaged(countryId: Long): Flow<PagingData<WorkPermit>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { workPermitDao.getWorkPermitsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated construction permits
     */
    fun getConstructionPermitsPaged(countryId: Long): Flow<PagingData<ConstructionPermit>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { constructionPermitDao.getConstructionPermitsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated bus schedules
     */
    fun getBusSchedulesPaged(countryId: Long): Flow<PagingData<BusSchedule>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { busScheduleDao.getBusSchedulesPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated stadium vendors
     */
    fun getStadiumVendorsPaged(countryId: Long): Flow<PagingData<StadiumVendor>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { stadiumVendorDao.getStadiumVendorsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated players
     */
    fun getPlayersPaged(countryId: Long): Flow<PagingData<Player>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { playerDao.getPlayersPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated medicine stockpiles
     */
    fun getMedicineStockpilesPaged(countryId: Long): Flow<PagingData<MedicineStockpile>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { medicineStockpileDao.getMedicineStockpilesPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated welfare payments
     */
    fun getWelfarePaymentsPaged(countryId: Long): Flow<PagingData<WelfarePayment>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { welfarePaymentDao.getWelfarePaymentsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated purchase orders
     */
    fun getPurchaseOrdersPaged(countryId: Long): Flow<PagingData<PurchaseOrder>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { purchaseOrderDao.getPurchaseOrdersPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated expense reports
     */
    fun getExpenseReportsPaged(countryId: Long): Flow<PagingData<ExpenseReport>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { expenseReportDao.getExpenseReportsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated witnesses
     */
    fun getWitnessesPaged(countryId: Long): Flow<PagingData<Witness>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { witnessDao.getWitnessesPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated jurors
     */
    fun getJurorsPaged(countryId: Long): Flow<PagingData<Juror>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { jurorDao.getJurorsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated donors
     */
    fun getDonorsPaged(countryId: Long): Flow<PagingData<Donor>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { donorDao.getDonorsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated lobbyists
     */
    fun getLobbyistsPaged(countryId: Long): Flow<PagingData<Lobbyist>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { lobbyistDao.getLobbyistsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated activists
     */
    fun getActivistsPaged(countryId: Long): Flow<PagingData<Activist>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { activistDao.getActivistsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated contractors
     */
    fun getContractorsPaged(countryId: Long): Flow<PagingData<Contractor>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { contractorDao.getContractorsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated foreign leaders
     */
    fun getForeignLeadersPaged(countryId: Long): Flow<PagingData<ForeignLeader>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { foreignLeaderDao.getForeignLeadersPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated spies
     */
    fun getSpiesPaged(countryId: Long): Flow<PagingData<Spy>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { spyDao.getSpiesPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated diplomats
     */
    fun getDiplomatsPaged(countryId: Long): Flow<PagingData<Diplomat>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { diplomatDao.getDiplomatsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated elite athletes
     */
    fun getEliteAthletesPaged(countryId: Long): Flow<PagingData<EliteAthlete>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { eliteAthleteDao.getEliteAthletesPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated job training programs
     */
    fun getJobTrainingProgramsPaged(countryId: Long): Flow<PagingData<JobTrainingProgram>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { jobTrainingProgramDao.getJobTrainingProgramsPaged(countryId) }
        ).flow
    }
    
    /**
     * Get paginated community projects
     */
    fun getCommunityProjectsPaged(countryId: Long): Flow<PagingData<CommunityProject>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { communityProjectDao.getCommunityProjectsPaged(countryId) }
        ).flow
    }
}
