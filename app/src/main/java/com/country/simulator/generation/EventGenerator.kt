package com.country.simulator.generation

import com.country.simulator.model.*
import kotlin.random.Random

/**
 * Random Event Generator - Creates dynamic events, crises, and opportunities
 */
object EventGenerator {
    
    private val random = Random
    
    /**
     * Generate a random game event
     */
    fun generateEvent(countryId: Long): GameEvent {
        val eventType = GameEventType.entries.random()
        
        return when (eventType) {
            GameEventType.CRISIS -> generateCrisisEvent(countryId)
            GameEventType.OPPORTUNITY -> generateOpportunityEvent(countryId)
            GameEventType.SCANDAL -> generateScandalEvent(countryId)
            GameEventType.DISASTER -> generateDisasterEvent(countryId)
            GameEventType.BREAKTHROUGH -> generateBreakthroughEvent(countryId)
            GameEventType.PROTEST -> generateProtestEvent(countryId)
            GameEventType.DIPLOMATIC_INCIDENT -> generateDiplomaticIncidentEvent(countryId)
            GameEventType.ECONOMIC_SHIFT -> generateEconomicShiftEvent(countryId)
            GameEventType.SOCIAL_CHANGE -> generateSocialChangeEvent(countryId)
            GameEventType.TECHNOLOGY -> generateTechnologyEvent(countryId)
            GameEventType.CULTURAL -> generateCulturalEvent(countryId)
            GameEventType.ELECTION -> generateElectionEvent(countryId)
        }
    }
    
    /**
     * Generate crisis events
     */
    private fun generateCrisisEvent(countryId: Long): GameEvent {
        val crises = listOf(
            CrisisData(
                "Banking Collapse",
                "Major banks face insolvency amid loan defaults",
                listOf("Bail out banks", "Let them fail", "Nationalize banks", "Impose capital controls"),
                mapOf("bailout" to -500_000_000.0, "fail" to -10.0, "nationalize" to -5.0, "controls" to -3.0)
            ),
            CrisisData(
                "Currency Crash",
                "National currency loses 30% value overnight",
                listOf("Raise interest rates", "Buy back currency", "Seek IMF help", "Impose forex controls"),
                mapOf("rates" to -2.0, "buyback" to -1_000_000_000.0, "imf" to 0.0, "controls" to -5.0)
            ),
            CrisisData(
                "Political Assassination",
                "Key political figure assassinated",
                listOf("National mourning", "Security crackdown", "Investigation", "Emergency session"),
                mapOf("mourning" to 5.0, "crackdown" to -10.0, "investigation" to 2.0, "emergency" to 3.0)
            ),
            CrisisData(
                "Military Coup Attempt",
                "Faction of military attempts to seize power",
                listOf("Loyalist response", "Negotiate", "Flee country", "International intervention"),
                mapOf("loyalist" to -100_000_000.0, "negotiate" to -20.0, "flee" to -50.0, "international" to 0.0)
            ),
            CrisisData(
                "Mass Unemployment",
                "Major industry collapse causes job losses",
                listOf("Stimulus package", "Job programs", "Tax cuts", "Industry bailout"),
                mapOf("stimulus" to -2_000_000_000.0, "jobs" to -500_000_000.0, "taxcuts" to -1_000_000_000.0, "bailout" to -3_000_000_000.0)
            ),
            CrisisData(
                "Constitutional Crisis",
                "Government branches clash over authority",
                listOf("Supreme Court ruling", "Compromise", "Executive order", "Parliament vote"),
                mapOf("court" to 0.0, "compromise" to 5.0, "order" to -10.0, "vote" to 2.0)
            ),
            CrisisData(
                "Terrorist Attack",
                "Coordinated attacks on civilian targets",
                listOf("Military response", "Security measures", "Negotiation", "International help"),
                mapOf("military" to -500_000_000.0, "security" to -200_000_000.0, "negotiate" to -5.0, "help" to 0.0)
            ),
            CrisisData(
                "Refugee Influx",
                "Massive wave of refugees crosses border",
                listOf("Open borders", "Closed borders", "Camps only", "International aid"),
                mapOf("open" to -100_000_000.0, "closed" to -10.0, "camps" to -50_000_000.0, "aid" to 0.0)
            )
        )
        
        val crisis = crises.random()
        
        return GameEvent(
            countryId = countryId,
            eventType = GameEventType.CRISIS,
            title = crisis.title,
            description = crisis.description,
            category = EventCategory.POLITICAL,
            priority = 10,
            options = crisis.options.joinToString("|"),
            selectedOption = null,
            immediateEffects = """{"crisis": true, "type": "${crisis.title}"}""",
            eventDate = System.currentTimeMillis()
        )
    }
    
    /**
     * Generate opportunity events
     */
    private fun generateOpportunityEvent(countryId: Long): GameEvent {
        val opportunities = listOf(
            OpportunityData(
                "Resource Discovery",
                "Large oil reserves discovered offshore",
                listOf("State extraction", "Private contracts", "Joint venture", "Delay development"),
                mapOf("state" to 5_000_000_000.0, "private" to 2_000_000_000.0, "joint" to 3_000_000_000.0, "delay" to 0.0)
            ),
            OpportunityData(
                "Trade Deal Offer",
                "Major economic power offers preferential trade",
                listOf("Accept deal", "Negotiate better", "Decline", "Regional pact"),
                mapOf("accept" to 1_000_000_000.0, "negotiate" to 500_000_000.0, "decline" to 0.0, "regional" to 800_000_000.0)
            ),
            OpportunityData(
                "Technology Transfer",
                "Foreign nation offers advanced technology",
                listOf("Accept offer", "Co-develop", "License only", "Decline"),
                mapOf("accept" to 2.0, "codevelop" to 3.0, "license" to 1.0, "decline" to -1.0)
            ),
            OpportunityData(
                "Tourism Boom",
                "International interest in country surges",
                listOf("Invest in tourism", "Limit visitors", "Eco-tourism only", "No action"),
                mapOf("invest" to -500_000_000.0, "limit" to 100_000_000.0, "eco" to -200_000_000.0, "none" to 50_000_000.0)
            ),
            OpportunityData(
                "Foreign Investment",
                "Multinational corporation wants to invest",
                listOf("Full approval", "Conditional", "Partial approval", "Reject"),
                mapOf("full" to 3_000_000_000.0, "conditional" to 2_000_000_000.0, "partial" to 1_000_000_000.0, "reject" to 0.0)
            ),
            OpportunityData(
                "Alliance Invitation",
                "Invited to join powerful military alliance",
                listOf("Join alliance", "Observer status", "Partnership only", "Decline"),
                mapOf("join" to 0.0, "observer" to 0.0, "partnership" to 0.0, "decline" to 0.0)
            )
        )
        
        val opportunity = opportunities.random()
        
        return GameEvent(
            countryId = countryId,
            eventType = GameEventType.OPPORTUNITY,
            title = opportunity.title,
            description = opportunity.description,
            category = EventCategory.ECONOMIC,
            priority = 7,
            options = opportunity.options.joinToString("|"),
            selectedOption = null,
            immediateEffects = """{"opportunity": true, "type": "${opportunity.title}"}""",
            eventDate = System.currentTimeMillis()
        )
    }
    
    /**
     * Generate scandal events
     */
    private fun generateScandalEvent(countryId: Long): GameEvent {
        val scandals = listOf(
            ScandalData(
                "Corruption Allegations",
                "Minister accused of taking bribes",
                listOf("Fire minister", "Investigate", "Defend minister", "Ignore"),
                8
            ),
            ScandalData(
                "Affair Scandal",
                "High official caught in extramarital affair",
                listOf("Resignation demanded", "Private matter", "Investigate abuse", "No comment"),
                5
            ),
            ScandalData(
                "Tax Easion",
                "Government officials linked to tax haven accounts",
                listOf("Prosecute", "Tax amnesty", "Investigation", "Deny involvement"),
                7
            ),
            ScandalData(
                "Nepotism Claims",
                "Officials hiring family members exposed",
                listOf("New hiring rules", "Defend appointments", "Review cases", "Fire relatives"),
                6
            ),
            ScandalData(
                "Contract Scandal",
                "No-bid contracts awarded to donors",
                listOf("Cancel contracts", "Investigate", "Defend process", "New bidding"),
                8
            ),
            ScandalData(
                "Surveillance Scandal",
                "Illegal spying on citizens revealed",
                listOf("Stop program", "Investigate abuse", "Defend security", "New oversight"),
                7
            )
        )
        
        val scandal = scandals.random()
        
        return GameEvent(
            countryId = countryId,
            eventType = GameEventType.SCANDAL,
            title = scandal.title,
            description = scandal.description,
            category = EventCategory.POLITICAL,
            priority = 8,
            options = scandal.options.joinToString("|"),
            selectedOption = null,
            immediateEffects = """{"scandal": true, "severity": ${scandal.severity}}""",
            eventDate = System.currentTimeMillis()
        )
    }
    
    /**
     * Generate disaster events
     */
    private fun generateDisasterEvent(countryId: Long): GameEvent {
        val disasters = listOf(
            DisasterData(
                "Earthquake",
                "Major earthquake strikes populated area",
                listOf("Emergency response", "International aid", "Military deployment", "Evacuation"),
                9
            ),
            DisasterData(
                "Hurricane",
                "Devastating storm hits coastal regions",
                listOf("Emergency shelters", "Evacuation", "International help", "Rebuild fund"),
                8
            ),
            DisasterData(
                "Flood",
                "Severe flooding displaces thousands",
                listOf("Emergency aid", "Evacuation", "Dam release", "International help"),
                7
            ),
            DisasterData(
                "Drought",
                "Prolonged drought affects agriculture",
                listOf("Water rationing", "Import food", "Cloud seeding", "Emergency wells"),
                6
            ),
            DisasterData(
                "Wildfire",
                "Uncontrolled fires threaten communities",
                listOf("Firefighting", "Evacuation", "Military help", "International crews"),
                7
            ),
            DisasterData(
                "Pandemic",
                "New disease outbreak spreads rapidly",
                listOf("Lockdown", "Vaccination", "Travel ban", "Public info"),
                10
            ),
            DisasterData(
                "Industrial Accident",
                "Chemical plant explosion causes contamination",
                listOf("Evacuation", "Cleanup", "Investigation", "Compensation"),
                7
            )
        )
        
        val disaster = disasters.random()
        
        return GameEvent(
            countryId = countryId,
            eventType = GameEventType.DISASTER,
            title = disaster.title,
            description = disaster.description,
            category = EventCategory.ENVIRONMENTAL,
            priority = disaster.priority,
            options = disaster.options.joinToString("|"),
            selectedOption = null,
            immediateEffects = """{"disaster": true, "type": "${disaster.title}"}""",
            eventDate = System.currentTimeMillis()
        )
    }
    
    /**
     * Generate breakthrough events
     */
    private fun generateBreakthroughEvent(countryId: Long): GameEvent {
        val breakthroughs = listOf(
            BreakthroughData(
                "Medical Discovery",
                "Local scientists develop new treatment",
                listOf("Fund production", "Patent and sell", "Open source", "Partner with pharma"),
                500_000_000.0
            ),
            BreakthroughData(
                "Energy Innovation",
                "Breakthrough in renewable energy efficiency",
                listOf("Nationalize", "Subsidize", "Export technology", "Open research"),
                1_000_000_000.0
            ),
            BreakthroughData(
                "Tech Startup Success",
                "Local company becomes global leader",
                listOf("Tax incentives", "State stake", "Support expansion", "No intervention"),
                2_000_000_000.0
            ),
            BreakthroughData(
                "Agricultural Innovation",
                "New crop variety doubles yields",
                listOf("Distribute seeds", "Export technology", "Research more", "Patent"),
                300_000_000.0
            )
        )
        
        val breakthrough = breakthroughs.random()
        
        return GameEvent(
            countryId = countryId,
            eventType = GameEventType.BREAKTHROUGH,
            title = breakthrough.title,
            description = breakthrough.description,
            category = EventCategory.ECONOMIC,
            priority = 6,
            options = breakthrough.options.joinToString("|"),
            selectedOption = null,
            immediateEffects = """{"breakthrough": true, "value": ${breakthrough.value}}""",
            eventDate = System.currentTimeMillis()
        )
    }
    
    /**
     * Generate protest events
     */
    private fun generateProtestEvent(countryId: Long): GameEvent {
        val protests = listOf(
            ProtestData(
                "Labor Strike",
                "Workers demand higher wages",
                listOf("Meet demands", "Partial concession", "Hold firm", "Mediation"),
                "Economic"
            ),
            ProtestData(
                "Student Protests",
                "Students protest education cuts",
                listOf("Restore funding", "Partial restore", "Hold firm", "Dialogue"),
                "Education"
            ),
            ProtestData(
                "Environmental Rally",
                "Activists demand climate action",
                listOf("New regulations", "Committee study", "Hold firm", "Green investment"),
                "Environment"
            ),
            ProtestData(
                "Anti-Government March",
                "Opposition calls for resignation",
                listOf("Resign", "Reform cabinet", "Hold firm", "Dialogue"),
                "Political"
            ),
            ProtestData(
                "Housing Protest",
                "Citizens demand affordable housing",
                listOf("New programs", "Study issue", "Hold firm", "Tax incentives"),
                "Social"
            )
        )
        
        val protest = protests.random()
        
        return GameEvent(
            countryId = countryId,
            eventType = GameEventType.PROTEST,
            title = protest.title,
            description = protest.description,
            category = EventCategory.SOCIAL,
            priority = 7,
            options = protest.options.joinToString("|"),
            selectedOption = null,
            immediateEffects = """{"protest": true, "issue": "${protest.issue}"}""",
            eventDate = System.currentTimeMillis()
        )
    }
    
    /**
     * Generate diplomatic incident events
     */
    private fun generateDiplomaticIncidentEvent(countryId: Long): GameEvent {
        val incidents = listOf(
            DiplomaticData(
                "Embassy Attack",
                "Our embassy attacked abroad",
                listOf("Retaliate", "Protest", "Evacuate", "UN complaint"),
                -20
            ),
            DiplomaticData(
                "Diplomat Expelled",
                "Our diplomat expelled from foreign country",
                listOf("Tit for tat", "Protest", "Negotiate", "Ignore"),
                -10
            ),
            DiplomaticData(
                "Border Incident",
                "Military confrontation at border",
                listOf("Escalate", "De-escalate", "Negotiate", "International mediation"),
                -30
            ),
            DiplomaticData(
                "Trade Dispute",
                "Trading partner imposes tariffs",
                listOf("Retaliate", "WTO complaint", "Negotiate", "Accept"),
                -15
            ),
            DiplomaticData(
                "Spy Scandal",
                "Our spy caught abroad",
                listOf("Deny", "Exchange", "Negotiate release", "Disavow"),
                -25
            )
        )
        
        val incident = incidents.random()
        
        return GameEvent(
            countryId = countryId,
            eventType = GameEventType.DIPLOMATIC_INCIDENT,
            title = incident.title,
            description = incident.description,
            category = EventCategory.DIPLOMATIC,
            priority = 8,
            options = incident.options.joinToString("|"),
            selectedOption = null,
            immediateEffects = """{"incident": true, "relationImpact": ${incident.impact}}""",
            eventDate = System.currentTimeMillis()
        )
    }
    
    /**
     * Generate economic shift events
     */
    private fun generateEconomicShiftEvent(countryId: Long): GameEvent {
        val shifts = listOf(
            EconomicData(
                "Stock Market Crash",
                "Major indices drop 20%",
                listOf("Market intervention", "Let it correct", "Circuit breakers", "Investigate"),
                -15.0
            ),
            EconomicData(
                "Inflation Spike",
                "Prices rise faster than expected",
                listOf("Raise rates", "Price controls", "Subsidies", "Wait it out"),
                -10.0
            ),
            EconomicData(
                "Export Boom",
                "Unexpected surge in exports",
                listOf("Tax windfall", "Invest surplus", "Save reserves", "Cut taxes"),
                5.0
            ),
            EconomicData(
                "Currency Strengthening",
                "Currency gains significant value",
                listOf("Let it rise", "Intervene", "Build reserves", "Adjust policy"),
                3.0
            )
        )
        
        val shift = shifts.random()
        
        return GameEvent(
            countryId = countryId,
            eventType = GameEventType.ECONOMIC_SHIFT,
            title = shift.title,
            description = shift.description,
            category = EventCategory.ECONOMIC,
            priority = 8,
            options = shift.options.joinToString("|"),
            selectedOption = null,
            immediateEffects = """{"economicShift": true, "impact": ${shift.impact}}""",
            eventDate = System.currentTimeMillis()
        )
    }
    
    /**
     * Generate social change events
     */
    private fun generateSocialChangeEvent(countryId: Long): GameEvent {
        val changes = listOf(
            SocialData(
                "Demographic Shift",
                "Population aging accelerates",
                listOf("Raise pension age", "Encourage births", "Immigration", "No action"),
                "Demographics"
            ),
            SocialData(
                "Cultural Movement",
                "New social movement gains traction",
                listOf("Embrace", "Monitor", "Oppose", "Dialogue"),
                "Culture"
            ),
            SocialData(
                "Religious Tension",
                "Inter-faith relations deteriorate",
                listOf("Mediation", "Security", "Dialogue", "No action"),
                "Religion"
            ),
            SocialData(
                "Generational Divide",
                "Young vs old on key issues",
                listOf("Youth programs", "Town halls", "Commission", "No action"),
                "Generation"
            )
        )
        
        val change = changes.random()
        
        return GameEvent(
            countryId = countryId,
            eventType = GameEventType.SOCIAL_CHANGE,
            title = change.title,
            description = change.description,
            category = EventCategory.SOCIAL,
            priority = 6,
            options = change.options.joinToString("|"),
            selectedOption = null,
            immediateEffects = """{"socialChange": true, "area": "${change.area}"}""",
            eventDate = System.currentTimeMillis()
        )
    }
    
    /**
     * Generate technology events
     */
    private fun generateTechnologyEvent(countryId: Long): GameEvent {
        val techEvents = listOf(
            TechData(
                "AI Breakthrough",
                "Local company leads in AI development",
                listOf("Fund research", "Regulate", "Export control", "Partner"),
                800_000_000.0
            ),
            TechData(
                "Cyber Attack",
                "Major infrastructure targeted",
                listOf("Counter-attack", "Defend", "Investigate", "International help"),
                -200_000_000.0
            ),
            TechData(
                "Space Program Milestone",
                "Successful satellite launch",
                listOf("Expand program", "Commercialize", "International partnership", "Pause"),
                500_000_000.0
            )
        )
        
        val tech = techEvents.random()
        
        return GameEvent(
            countryId = countryId,
            eventType = GameEventType.TECHNOLOGY,
            title = tech.title,
            description = tech.description,
            category = EventCategory.ECONOMIC,
            priority = 7,
            options = tech.options.joinToString("|"),
            selectedOption = null,
            immediateEffects = """{"technology": true, "value": ${tech.value}}""",
            eventDate = System.currentTimeMillis()
        )
    }
    
    /**
     * Generate cultural events
     */
    private fun generateCulturalEvent(countryId: Long): GameEvent {
        val culturalEvents = listOf(
            CulturalData(
                "Film Festival Success",
                "National film wins international awards",
                listOf("Fund industry", "Tax incentives", "No action", "Promote tourism"),
                50_000_000.0
            ),
            CulturalData(
                "Sports Victory",
                "National team wins championship",
                listOf("Celebrate", "Fund sports", "Build stadium", "No action"),
                100_000_000.0
            ),
            CulturalData(
                "Heritage Site Designation",
                "UNESCO World Heritage status granted",
                listOf("Preserve", "Promote tourism", "Limit access", "Develop area"),
                30_000_000.0
            )
        )
        
        val cultural = culturalEvents.random()
        
        return GameEvent(
            countryId = countryId,
            eventType = GameEventType.CULTURAL,
            title = cultural.title,
            description = cultural.description,
            category = EventCategory.SOCIAL,
            priority = 5,
            options = cultural.options.joinToString("|"),
            selectedOption = null,
            immediateEffects = """{"cultural": true, "value": ${cultural.value}}""",
            eventDate = System.currentTimeMillis()
        )
    }
    
    /**
     * Generate election events
     */
    private fun generateElectionEvent(countryId: Long): GameEvent {
        val electionEvents = listOf(
            ElectionData(
                "Snap Election Called",
                "Early elections announced",
                listOf("Prepare campaign", "Form coalition", "Stay neutral", "Resign")
            ),
            ElectionData(
                "Election Interference",
                "Foreign meddling detected",
                listOf("Expose", "Protest", "Retaliate", "Ignore")
            ),
            ElectionData(
                "Voting System Change",
                "New electoral system proposed",
                listOf("Support", "Oppose", "Amend", "Referendum")
            )
        )
        
        val election = electionEvents.random()
        
        return GameEvent(
            countryId = countryId,
            eventType = GameEventType.ELECTION,
            title = election.title,
            description = election.description,
            category = EventCategory.POLITICAL,
            priority = 9,
            options = election.options.joinToString("|"),
            selectedOption = null,
            immediateEffects = """{"election": true}""",
            eventDate = System.currentTimeMillis()
        )
    }
    
    // Data classes
    data class CrisisData(
        val title: String,
        val description: String,
        val options: List<String>,
        val effects: Map<String, Double>
    )
    
    data class OpportunityData(
        val title: String,
        val description: String,
        val options: List<String>,
        val effects: Map<String, Double>
    )
    
    data class ScandalData(
        val title: String,
        val description: String,
        val options: List<String>,
        val severity: Int
    )
    
    data class DisasterData(
        val title: String,
        val description: String,
        val options: List<String>,
        val priority: Int
    )
    
    data class BreakthroughData(
        val title: String,
        val description: String,
        val options: List<String>,
        val value: Double
    )
    
    data class ProtestData(
        val title: String,
        val description: String,
        val options: List<String>,
        val issue: String
    )
    
    data class DiplomaticData(
        val title: String,
        val description: String,
        val options: List<String>,
        val impact: Int
    )
    
    data class EconomicData(
        val title: String,
        val description: String,
        val options: List<String>,
        val impact: Double
    )
    
    data class SocialData(
        val title: String,
        val description: String,
        val options: List<String>,
        val area: String
    )
    
    data class TechData(
        val title: String,
        val description: String,
        val options: List<String>,
        val value: Double
    )
    
    data class CulturalData(
        val title: String,
        val description: String,
        val options: List<String>,
        val value: Double
    )
    
    data class ElectionData(
        val title: String,
        val description: String,
        val options: List<String>
    )
}
