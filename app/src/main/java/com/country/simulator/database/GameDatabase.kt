package com.country.simulator.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.country.simulator.model.*

@Database(
    entities = [
        // Core models
        Country::class,
        PlayerCountry::class,
        NationalResources::class,
        
        // Politics
        Election::class,
        Campaign::class,
        CampaignEvent::class,
        PoliticalParty::class,
        Scandal::class,
        OpinionPoll::class,
        
        // Economy
        NationalBudget::class,
        TaxBracket::class,
        TradeDeal::class,
        Business::class,
        OilOperation::class,
        EconomicReport::class,
        
        // Law
        Law::class,
        Bill::class,
        Committee::class,
        CourtCase::class,
        Judge::class,
        SentencingGuideline::class,
        LegalAid::class,
        
        // Ministry
        Ministry::class,
        Appointee::class,
        GovernmentDepartment::class,
        AgencyHead::class,
        PurchaseOrder::class,
        ExpenseReport::class,
        InterDepartmentDispute::class,
        OutreachPlan::class,
        
        // Infrastructure
        InfrastructureProject::class,
        PowerGrid::class,
        EnergySource::class,
        TransportRoute::class,
        BusSchedule::class,
        TrafficProject::class,
        ConstructionPermit::class,
        PollutionControl::class,
        
        // Diplomacy
        DiplomaticRelation::class,
        Alliance::class,
        TradeBloc::class,
        Diplomat::class,
        EmbassyProject::class,
        Spy::class,
        Sanction::class,
        War::class,
        WorkPermit::class,
        
        // Demographics
        Demographic::class,
        ImmigrationCase::class,
        VisaQuota::class,
        HealthProgram::class,
        MedicineStockpile::class,
        DiseaseOutbreak::class,
        SocialGroup::class,
        WelfarePayment::class,
        CommunityProject::class,
        JobTrainingProgram::class,
        
        // Sports
        SportsTeam::class,
        Player::class,
        Stadium::class,
        StadiumDesign::class,
        SportsEvent::class,
        DopingCase::class,
        StadiumVendor::class,
        EliteAthlete::class,
        FitnessPolicy::class,
        SportsSponsor::class,
        
        // NPCs
        NPC::class,
        NPCMemory::class,
        NPCRelationship::class,
        Lobbyist::class,
        Activist::class,
        Witness::class,
        Juror::class,
        Donor::class,
        Contractor::class,
        ForeignLeader::class,
        
        // Butterfly Effect System
        MacroDecision::class,
        MicroTask::class,
        ButterflyEffect::class,
        TaskChain::class,
        ProceduralTable::class,
        GameEvent::class,
        Notification::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GameDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
    abstract fun playerCountryDao(): PlayerCountryDao
    abstract fun resourcesDao(): ResourcesDao
    abstract fun electionDao(): ElectionDao
    abstract fun campaignDao(): CampaignDao
    abstract fun campaignEventDao(): CampaignEventDao
    abstract fun scandalDao(): ScandalDao
    abstract fun budgetDao(): BudgetDao
    abstract fun taxBracketDao(): TaxBracketDao
    abstract fun lawDao(): LawDao
    abstract fun billDao(): BillDao
    abstract fun courtCaseDao(): CourtCaseDao
    abstract fun ministryDao(): MinistryDao
    abstract fun appointeeDao(): AppointeeDao
    abstract fun infrastructureDao(): InfrastructureDao
    abstract fun diplomacyDao(): DiplomacyDao
    abstract fun npcDao(): NPCDao
    abstract fun microTaskDao(): MicroTaskDao
    abstract fun butterflyEffectDao(): ButterflyEffectDao
    abstract fun gameEventDao(): GameEventDao
    abstract fun notificationDao(): NotificationDao
}
