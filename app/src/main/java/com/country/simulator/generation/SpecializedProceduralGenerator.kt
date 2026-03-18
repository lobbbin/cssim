package com.country.simulator.generation

import com.country.simulator.model.*
import kotlin.random.Random

/**
 * Specialized Procedural Generators
 * Generates specific content types for various game systems
 */
object SpecializedProceduralGenerator {
    
    private val random = Random
    
    // ========== SCANDAL GENERATOR (200+ templates) ==========
    
    private val scandalTypes = listOf(
        "Corruption", "Bribery", "Embezzlement", "Fraud", "Nepotism",
        "Affair", "Cover-up", "Abuse of Power", "Conflict of Interest",
        "Tax Evasion", "Money Laundering", "Insider Trading", "Leak",
        "Surveillance", "Wiretapping", "Blackmail", "Extortion",
        "Kickbacks", "Cronyism", "Favoritism", "Misconduct", "Negligence",
        "Dereliction", "Incompetence", "Waste", "Mismanagement",
        "Obstruction", "Perjury", "Contempt", "Intimidation",
        "Retaliation", "Discrimination", "Harassment", "Bullying",
        "Profiteering", "Graft", "Racketeering", "Fraud", "Deception"
    )
    
    private val scandalTargets = listOf(
        "Minister", "Senator", "Governor", "Mayor", "Ambassador",
        "CEO", "Director", "Official", "Advisor", "Secretary",
        "Judge", "General", "Chief", "Commissioner", "Representative",
        "Attorney", "Prosecutor", "Sheriff", "Inspector", "Auditor",
        "Regulator", "Banker", "Executive", "Contractor", "Consultant"
    )
    
    private val scandalContexts = listOf(
        "in government contracting",
        "in defense procurement",
        "in healthcare administration",
        "in financial regulation",
        "in environmental oversight",
        "in immigration services",
        "in tax collection",
        "in law enforcement",
        "in judicial proceedings",
        "in electoral processes",
        "in public works",
        "in education funding",
        "in social programs",
        "in trade negotiations",
        "in diplomatic appointments"
    )
    
    fun generateScandal(): ScandalData {
        val type = scandalTypes.random()
        val target = scandalTargets.random()
        val context = scandalContexts.random()
        val severity = random.nextInt(1, 11)
        
        return ScandalData(
            title = "$type Scandal",
            description = "$target involved in ${type.lowercase()} allegations $context",
            type = type,
            target = target,
            severity = severity,
            context = context
        )
    }
    
    // ========== CRISIS GENERATOR (100+ templates) ==========
    
    private val crisisTypes = listOf(
        "Banking Collapse", "Currency Crash", "Stock Market Crash",
        "Debt Default", "Inflation Spike", "Recession", "Depression",
        "Political Assassination", "Coup Attempt", "Government Shutdown",
        "Constitutional Crisis", "Succession Crisis", "Coalition Collapse",
        "Terrorist Attack", "Mass Shooting", "Hostage Crisis",
        "Kidnapping", "Bombing", "Chemical Attack", "Biological Threat",
        "Earthquake", "Hurricane", "Flood", "Drought", "Wildfire",
        "Volcanic Eruption", "Tsunami", "Landslide", "Avalanche",
        "Pandemic", "Epidemic", "Contamination", "Poisoning",
        "Nuclear Accident", "Chemical Spill", "Oil Spill", "Gas Leak",
        "Power Grid Failure", "Water Supply Crisis", "Food Shortage",
        "Fuel Shortage", "Medical Supply Shortage",
        "Cyber Attack", "Data Breach", "Ransomware", "System Failure",
        "Transportation Disaster", "Plane Crash", "Train Derailment",
        "Bridge Collapse", "Building Collapse", "Dam Failure",
        "Refugee Influx", "Mass Migration", "Border Crisis",
        "Diplomatic Incident", "Embassy Attack", "Spy Scandal",
        "Trade War", "Sanctions Crisis", "Resource War",
        "Military Confrontation", "Naval Incident", "Airspace Violation"
    )
    
    private val crisisSeverities = listOf(
        CrisisSeverity.MINOR,
        CrisisSeverity.MODERATE,
        CrisisSeverity.SERIOUS,
        CrisisSeverity.SEVERE,
        CrisisSeverity.CATASTROPHIC
    )
    
    fun generateCrisis(): CrisisData {
        val type = crisisTypes.random()
        val severity = crisisSeverities.random()
        
        return CrisisData(
            type = type,
            title = "$type Crisis",
            description = generateCrisisDescription(type),
            severity = severity,
            duration = random.nextInt(1, 31), // days
            affectedRegions = random.nextInt(1, 6)
        )
    }
    
    private fun generateCrisisDescription(type: String): String {
        return when {
            type.contains("Banking") || type.contains("Currency") || type.contains("Stock") ->
                "Financial markets in turmoil as $type threatens economic stability"
            type.contains("Political") || type.contains("Government") || type.contains("Constitutional") ->
                "Political system under stress as $type challenges governance"
            type.contains("Terrorist") || type.contains("Attack") || type.contains("Bombing") ->
                "Security forces respond to $type targeting civilians"
            type.contains("Earthquake") || type.contains("Hurricane") || type.contains("Flood") ->
                "Emergency services mobilize as $type strikes region"
            type.contains("Pandemic") || type.contains("Epidemic") || type.contains("Contamination") ->
                "Health authorities battle $type spreading nationally"
            type.contains("Cyber") || type.contains("Data") || type.contains("Ransomware") ->
                "Digital infrastructure compromised by $type"
            else -> "$type creates challenges for national response"
        }
    }
    
    // ========== LAW GENERATOR (300+ templates) ==========
    
    private val lawPrefixes = listOf(
        "National", "Federal", "Public", "Economic", "Social",
        "Environmental", "Healthcare", "Education", "Infrastructure",
        "Defense", "Consumer", "Financial", "Labor", "Immigration",
        "Criminal Justice", "Tax", "Energy", "Transportation", "Housing",
        "Digital", "Privacy", "Security", "Trade", "Agricultural",
        "Industrial", "Commercial", "Corporate", "Small Business",
        "Family", "Civil Rights", "Human Rights", "Voting Rights",
        "Religious Freedom", "Press Freedom", "Speech", "Assembly",
        "Scientific", "Medical", "Pharmaceutical", "Biotechnology",
        "Space", "Maritime", "Aviation", "Railway", "Highway"
    )
    
    private val lawSuffixes = listOf(
        "Act", "Reform Act", "Protection Act", "Investment Act",
        "Modernization Act", "Emergency Act", "Regulation Act",
        "Support Act", "Development Act", "Accountability Act",
        "Transparency Act", "Security Act", "Welfare Act", "Rights Act",
        "Enhancement Act", "Improvement Act", "Amendment Act",
        "Compliance Act", "Enforcement Act", "Oversight Act",
        "Authorization Act", "Appropriations Act", "Budget Act",
        "Recovery Act", "Stimulus Act", "Relief Act", "Assistance Act"
    )
    
    private val lawPurposes = listOf(
        "to improve public safety",
        "to promote economic growth",
        "to protect consumer rights",
        "to ensure national security",
        "to advance social welfare",
        "to protect the environment",
        "to enhance education",
        "to improve healthcare",
        "to strengthen infrastructure",
        "to promote innovation",
        "to ensure fair competition",
        "to protect civil liberties",
        "to reduce inequality",
        "to combat discrimination",
        "to promote transparency",
        "to prevent corruption",
        "to ensure accountability",
        "to improve efficiency",
        "to reduce bureaucracy",
        "to modernize systems"
    )
    
    fun generateLaw(): LawData {
        val prefix = lawPrefixes.random()
        val suffix = lawSuffixes.random()
        val purpose = lawPurposes.random()
        val category = LawType.entries.random()
        
        return LawData(
            title = "$prefix $suffix",
            shortTitle = "${prefix.split(" ").joinToString("") { it.first() }}${suffix.replace(" Act", "")}",
            description = "Legislation $purpose",
            purpose = purpose,
            category = category,
            complexity = random.nextInt(1, 11),
            cost = random.nextDouble(0.0, 10_000_000_000.0)
        )
    }
    
    // ========== NPC GENERATOR (1000+ combinations) ==========
    
    private val occupations = listOf(
        "Teacher", "Professor", "Principal", "Administrator", "Counselor",
        "Doctor", "Nurse", "Surgeon", "Therapist", "Pharmacist",
        "Lawyer", "Judge", "Prosecutor", "Defender", "Paralegal",
        "Engineer", "Architect", "Designer", "Developer", "Technician",
        "Scientist", "Researcher", "Analyst", "Consultant", "Advisor",
        "Accountant", "Auditor", "Banker", "Investor", "Broker",
        "Manager", "Director", "Executive", "Supervisor", "Coordinator",
        "Salesperson", "Marketer", "Advertiser", "Representative", "Agent",
        "Writer", "Editor", "Journalist", "Author", "Publisher",
        "Artist", "Musician", "Actor", "Director", "Producer",
        "Athlete", "Coach", "Trainer", "Referee", "Scout",
        "Chef", "Baker", "Server", "Bartender", "Manager",
        "Police Officer", "Detective", "Sheriff", "Marshal", "Agent",
        "Soldier", "Sailor", "Airman", "Marine", "Officer",
        "Firefighter", "Paramedic", "Dispatcher", "Inspector", "Chief",
        "Driver", "Pilot", "Captain", "Conductor", "Operator",
        "Mechanic", "Electrician", "Plumber", "Carpenter", "Builder",
        "Farmer", "Rancher", "Fisherman", "Logger", "Miner",
        "Factory Worker", "Assembly Worker", "Machine Operator", "Inspector", "Supervisor",
        "Retail Worker", "Cashier", "Clerk", "Stock Clerk", "Manager",
        "Office Worker", "Secretary", "Receptionist", "Assistant", "Specialist",
        "Civil Servant", "Administrator", "Inspector", "Analyst", "Clerk",
        "Social Worker", "Counselor", "Therapist", "Advocate", "Organizer",
        "Religious Leader", "Minister", "Priest", "Rabbi", "Imam",
        "Student", "Intern", "Apprentice", "Trainee", "Volunteer"
    )
    
    private val personalityTraits = listOf(
        "Ambitious", "Honest", "Cunning", "Loyal", "Greedy",
        "Idealistic", "Pragmatic", "Charismatic", "Introverted", "Aggressive",
        "Diplomatic", "Corrupt", "Principled", "Opportunistic", "Compassionate",
        "Ruthless", "Generous", "Selfish", "Brave", "Cowardly",
        "Intelligent", "Foolish", "Wise", "Naive", "Experienced",
        "Optimistic", "Pessimistic", "Realistic", "Dreamer", "Practical",
        "Patient", "Impatient", "Calm", "Volatile", "Steady",
        "Creative", "Conventional", "Innovative", "Traditional", "Progressive",
        "Friendly", "Hostile", "Warm", "Cold", "Welcoming",
        "Trustworthy", "Deceitful", "Reliable", "Unreliable", "Dependable"
    )
    
    private val politicalIssues = listOf(
        "Economy", "Healthcare", "Education", "Immigration", "Crime",
        "Environment", "Taxes", "Jobs", "Housing", "Infrastructure",
        "Defense", "Foreign Policy", "Social Security", "Energy",
        "Abortion", "Gun Control", "Drug Policy", "Poverty",
        "Inequality", "Corruption", "Civil Rights", "Religious Freedom"
    )
    
    fun generateNPC(countryId: Long, isImportant: Boolean = false): NPCData {
        val isMale = random.nextBoolean()
        val age = random.nextInt(18, 86)
        val occupation = occupations.random()
        val traits = personalityTraits.shuffled().take(3).joinToString(", ")
        val issue = politicalIssues.random()
        val stance = random.nextInt(-100, 101)
        
        return NPCData(
            name = ExtendedProceduralGenerator.generateExtendedName(isMale),
            age = age,
            gender = if (isMale) "Male" else "Female",
            occupation = occupation,
            location = ExtendedProceduralGenerator.generateExtendedCityName(),
            personality = traits,
            mainIssue = issue,
            issueStance = stance,
            approvalOfGovernment = random.nextDouble(0.0, 100.0),
            trustInInstitutions = random.nextDouble(0.0, 100.0),
            isImportant = isImportant
        )
    }
    
    // ========== DIPLOMATIC CABLE GENERATOR (200+ templates) ==========
    
    private val cableTypes = listOf(
        "INFORMATION", "ACTION", "PRIORITY", "IMMEDIATE", "FLASH",
        "ROUTINE", "CONFIDENTIAL", "SECRET", "TOP SECRET", "NOFORN"
    )
    
    private val cableSubjects = listOf(
        "Political Situation", "Economic Developments", "Military Movements",
        "Trade Negotiations", "Security Cooperation", "Intelligence Sharing",
        "Human Rights", "Democratic Reform", "Counter-terrorism",
        "Nuclear Program", "Missile Testing", "Border Dispute",
        "Election Monitoring", "Government Stability", "Leadership Change",
        "Protest Movement", "Civil Unrest", "Refugee Situation",
        "Energy Cooperation", "Resource Access", "Pipeline Security",
        "Cyber Threats", "Espionage Activity", "Diplomatic Expulsion",
        "Alliance Coordination", "Joint Exercises", "Defense Agreement"
    )
    
    fun generateDiplomaticCable(): CableData {
        val type = cableTypes.random()
        val subject = cableSubjects.random()
        val classification = random.nextInt(1, 5) // 1=Unclassified, 5=Top Secret
        
        return CableData(
            cableNumber = "CBL-${System.currentTimeMillis()}",
            type = type,
            subject = subject,
            classification = classification,
            priority = random.nextInt(1, 6),
            summary = generateCableSummary(subject),
            actionRequired = random.nextBoolean()
        )
    }
    
    private fun generateCableSummary(subject: String): String {
        return "Re: $subject - Situation report and recommended actions for consideration."
    }
    
    // ========== SPEECH GENERATOR (100+ templates) ==========
    
    private val speechTypes = listOf(
        "Inaugural Address", "State of the Nation", "Victory Speech",
        "Concession Speech", "Campaign Rally", "Town Hall",
        "Press Conference", "Emergency Address", "Memorial Service",
        "Award Ceremony", "Graduation", "Fundraising Gala",
        "International Summit", "UN Address", "Parliamentary Debate",
        "Policy Announcement", "Budget Address", "Crisis Response"
    )
    
    private val speechThemes = listOf(
        "Hope and Change", "Unity and Progress", "Strength and Security",
        "Freedom and Democracy", "Prosperity for All", "Justice and Equality",
        "Innovation and Growth", "Tradition and Values", "Reform and Renewal",
        "Peace and Cooperation", "Defense and Deterrence", "Opportunity and Success"
    )
    
    private val speechOpenings = listOf(
        "My fellow citizens,", "Friends and neighbors,", "Honored guests,",
        "Distinguished colleagues,", "Members of the press,", "Ladies and gentlemen,",
        "My dear friends,", "Fellow countrymen,", "People of our great nation,"
    )
    
    fun generateSpeech(): SpeechData {
        val type = speechTypes.random()
        val theme = speechThemes.random()
        val opening = speechOpenings.random()
        
        return SpeechData(
            type = type,
            theme = theme,
            opening = opening,
            duration = random.nextInt(5, 61), // minutes
            keyPoints = generateSpeechPoints(theme),
            expectedImpact = random.nextDouble(-10.0, 10.0)
        )
    }
    
    private fun generateSpeechPoints(theme: String): List<String> {
        val points = mutableListOf<String>()
        
        when (theme) {
            "Hope and Change" -> {
                points.add("Vision for the future")
                points.add("Call for unity")
                points.add("Promise of reform")
            }
            "Strength and Security" -> {
                points.add("National defense priorities")
                points.add("Security challenges")
                points.add("Alliance commitments")
            }
            "Prosperity for All" -> {
                points.add("Economic growth plans")
                points.add("Job creation")
                points.add("Tax policy")
            }
            else -> {
                points.add("Current challenges")
                points.add("Proposed solutions")
                points.add("Call to action")
            }
        }
        
        return points
    }
    
    // ========== CONTRACTOR GENERATOR (200+ templates) ==========
    
    private val contractorSpecialties = listOf(
        "General Construction", "Road Building", "Bridge Construction",
        "Building Construction", "Infrastructure", "Electrical Work",
        "Plumbing", "HVAC", "Landscaping", "Demolition",
        "Renovation", "Steel Work", "Concrete", "Excavation",
        "Roofing", "Painting", "Flooring", "Carpentry",
        "Masonry", "Welding", "Engineering", "Architecture",
        "Project Management", "Consulting", "Design-Build"
    )
    
    private val contractorRatings = listOf(
        "Excellent", "Very Good", "Good", "Average", "Below Average",
        "Poor", "Outstanding", "Superior", "Satisfactory", "Unsatisfactory"
    )
    
    fun generateContractor(): ContractorData {
        val name = ExtendedProceduralGenerator.generateExtendedOrganization()
        val specialty = contractorSpecialties.random()
        val rating = random.nextDouble(1.0, 5.0)
        val hasAllegations = random.nextDouble() < 0.15 // 15% chance
        
        return ContractorData(
            name = name,
            specialty = specialty,
            rating = rating,
            yearsInBusiness = random.nextInt(1, 51),
            employees = random.nextInt(5, 5001),
            hasCorruptionAllegations = hasAllegations,
            governmentContracts = random.nextInt(0, 51)
        )
    }
    
    // Data classes for generated content
    
    data class ScandalData(
        val title: String,
        val description: String,
        val type: String,
        val target: String,
        val severity: Int,
        val context: String
    )
    
    data class CrisisData(
        val type: String,
        val title: String,
        val description: String,
        val severity: CrisisSeverity,
        val duration: Int,
        val affectedRegions: Int
    )
    
    data class LawData(
        val title: String,
        val shortTitle: String,
        val description: String,
        val purpose: String,
        val category: LawType,
        val complexity: Int,
        val cost: Double
    )
    
    data class NPCData(
        val name: String,
        val age: Int,
        val gender: String,
        val occupation: String,
        val location: String,
        val personality: String,
        val mainIssue: String,
        val issueStance: Int,
        val approvalOfGovernment: Double,
        val trustInInstitutions: Double,
        val isImportant: Boolean
    )
    
    data class CableData(
        val cableNumber: String,
        val type: String,
        val subject: String,
        val classification: Int,
        val priority: Int,
        val summary: String,
        val actionRequired: Boolean
    )
    
    data class SpeechData(
        val type: String,
        val theme: String,
        val opening: String,
        val duration: Int,
        val keyPoints: List<String>,
        val expectedImpact: Double
    )
    
    data class ContractorData(
        val name: String,
        val specialty: String,
        val rating: Double,
        val yearsInBusiness: Int,
        val employees: Int,
        val hasCorruptionAllegations: Boolean,
        val governmentContracts: Int
    )
}

enum class CrisisSeverity {
    MINOR,
    MODERATE,
    SERIOUS,
    SEVERE,
    CATASTROPHIC
}
