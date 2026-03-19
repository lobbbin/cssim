package com.country.simulator.generation

import com.country.simulator.model.*
import com.country.simulator.repositories.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * Database Populator - Generates thousands of procedural entries for the game world
 * Uses Paging 3 compatible data structure for efficient loading
 */
class DatabasePopulator(private val repository: GameRepository) {
    
    private val random = Random
    
    /**
     * Populate the entire game database with procedural content
     */
    suspend fun populateAllData(countryId: Long, seed: Long = System.currentTimeMillis()) {
        Random.setSeed(seed)
        
        withContext(Dispatchers.IO) {
            // Generate all data categories
            generatePoliticalParties(countryId)
            generateNPCs(countryId, count = 500)
            generateBusinesses(countryId, count = 200)
            generateLaws(countryId, count = 50)
            generateCourtCases(countryId, count = 30)
            generateMinistries(countryId)
            generateAppointees(countryId)
            generateInfrastructureProjects(countryId, count = 20)
            generateDiplomaticRelations(countryId, count = 15)
            generateHealthPrograms(countryId)
            generateSportsTeams(countryId)
            generateStadiums(countryId, count = 10)
            generateSocialGroups(countryId)
            generateLobbyists(countryId, count = 15)
            generateContractors(countryId, count = 25)
            generateInitialTasks(countryId)
            generateProceduralTables(countryId)
        }
    }
    
    /**
     * Generate political parties
     */
    private suspend fun generatePoliticalParties(countryId: Long) {
        val partyData = listOf(
            PartyData("Progressive Alliance", "PA", "Center-Left", "Social progress and economic equality"),
            PartyData("National Conservative Party", "NCP", "Right-Wing", "Traditional values and strong borders"),
            PartyData("Liberal Democratic Front", "LDF", "Centrist", "Free markets with social safety nets"),
            PartyData("Green Ecology Party", "GEP", "Environmental", "Climate action and sustainability"),
            PartyData("Workers United", "WU", "Left-Wing", "Labor rights and wealth redistribution"),
            PartyData("Freedom Movement", "FM", "Libertarian", "Minimal government, maximum liberty"),
            PartyData("Christian Democratic Union", "CDU", "Center-Right", "Faith-based social policies"),
            PartyData("Technocratic Reform Party", "TRP", "Technocratic", "Data-driven governance"),
            PartyData("National Independence Party", "NIP", "Nationalist", "Sovereignty first"),
            PartyData("Social Justice Coalition", "SJC", "Progressive", "Equality and systemic reform")
        )
        
        partyData.forEachIndexed { index, data ->
            val party = PoliticalParty(
                countryId = countryId,
                name = data.name,
                abbreviation = data.abbreviation,
                ideology = data.ideology,
                leaderName = generateNPCName(),
                foundingYear = (1945..2020).random(),
                seatsInParliament = (5..100).random(),
                membershipCount = (10000..500000).random(),
                funds = (1_000_000..50_000_000).random().toDouble(),
                approvalRating = (5.0..35.0).random(),
                isRulingParty = index == 0,
                isOppositionParty = index > 0
            )
            // Would insert via DAO - placeholder for actual implementation
        }
    }
    
    /**
     * Generate NPCs with diverse backgrounds
     */
    private suspend fun generateNPCs(countryId: Long, count: Int) {
        val occupations = listOf(
            OccupationData("Teacher", OccupationType.TEACHER, 35000.0, 60000.0),
            OccupationData("Doctor", OccupationType.DOCTOR, 100000.0, 300000.0),
            OccupationData("Engineer", OccupationType.ENGINEER, 60000.0, 150000.0),
            OccupationData("Lawyer", OccupationType.LAWYER, 80000.0, 250000.0),
            OccupationData("Nurse", OccupationType.WORKER, 40000.0, 70000.0),
            OccupationData("Police Officer", OccupationType.POLICE, 45000.0, 75000.0),
            OccupationData("Soldier", OccupationType.MILITARY, 35000.0, 65000.0),
            OccupationData("Civil Servant", OccupationType.CIVIL_SERVANT, 40000.0, 80000.0),
            OccupationData("Business Owner", OccupationType.BUSINESS_OWNER, 50000.0, 500000.0),
            OccupationData("CEO", OccupationType.CORPORATE_EXEC, 200000.0, 5000000.0),
            OccupationData("Factory Worker", OccupationType.WORKER, 25000.0, 50000.0),
            OccupationData("Farmer", OccupationType.FARMER, 20000.0, 80000.0),
            OccupationData("Scientist", OccupationType.SCIENTIST, 70000.0, 150000.0),
            OccupationData("Journalist", OccupationType.JOURNALIST, 35000.0, 80000.0),
            OccupationData("Artist", OccupationType.ARTIST, 20000.0, 100000.0),
            OccupationData("Athlete", OccupationType.ATHLETE, 50000.0, 10000000.0),
            OccupationData("Entertainer", OccupationType.ENTERTAINER, 40000.0, 5000000.0),
            OccupationData("Student", OccupationType.STUDENT, 0.0, 5000.0),
            OccupationData("Retired", OccupationType.RETIRED, 15000.0, 40000.0),
            OccupationData("Unemployed", OccupationType.UNEMPLOYED, 0.0, 2000.0),
            OccupationData("Activist", OccupationType.ACTIVIST, 15000.0, 50000.0),
            OccupationData("Politician", OccupationType.POLITICIAN, 80000.0, 300000.0),
            OccupationData("Criminal", OccupationType.CRIMINAL, 5000.0, 200000.0)
        )
        
        val ethnicities = listOf(
            "European", "African", "Asian", "Middle Eastern", 
            "Latin American", "Pacific Islander", "Indigenous", "Mixed"
        )
        
        val religions = listOf(
            "Christian", "Muslim", "Jewish", "Hindu", "Buddhist", 
            "Sikh", "Atheist", "Agnostic", "Other", "None"
        )
        
        val regions = listOf(
            "Capital Region", "Northern Province", "Southern Province",
            "Eastern Province", "Western Province", "Central Province",
            "Coastal Region", "Mountain Region", "Valley Region"
        )
        
        val cities = listOf(
            "Springfield", "Riverside", "Lakewood", "Hillcrest", "Fairview",
            "Greenville", "Madison", "Georgetown", "Clinton", "Franklin",
            "Washington", "Lincoln", "Jefferson", "Hamilton", "Jackson"
        )
        
        for (i in 1..count) {
            val occ = occupations.random()
            val isMale = random.nextBoolean()
            val age = (18..85).random()
            
            val npc = NPC(
                name = generateNPCName(isMale),
                age = age,
                gender = if (isMale) Gender.MALE else Gender.FEMALE,
                occupation = occ.name,
                occupationType = occ.type,
                income = (occ.minIncome..occ.maxIncome).random().toDouble(),
                wealth = (0..10_000_000).random().toDouble(),
                education = (8..20).random(),
                location = "${cities.random()} ${regions.random()}",
                region = regions.random(),
                personality = generatePersonality(),
                politicalLeanings = (-100..100).random(),
                ethnicity = ethnicities.random(),
                religion = religions.random(),
                maritalStatus = listOf("Single", "Married", "Divorced", "Widowed").random(),
                children = (0..6).random(),
                happiness = (20.0..100.0).random(),
                health = (30.0..100.0).random(),
                approvalOfGovernment = (0.0..100.0).random(),
                trustInInstitutions = (0.0..100.0).random(),
                countryId = countryId,
                isImportant = i <= 50 // First 50 are important NPCs
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate businesses
     */
    private suspend fun generateBusinesses(countryId: Long, count: Int) {
        val businessNames = listOf(
            "Tech", "Solutions", "Industries", "Corp", "Enterprises",
            "Group", "Holdings", "Systems", "Dynamics", "Global",
            "International", "National", "United", "Associated", "Consolidated"
        )
        
        val industries = listOf(
            "Technology", "Finance", "Manufacturing", "Retail", "Healthcare",
            "Energy", "Agriculture", "Transportation", "Media", "Construction",
            "Telecommunications", "Pharmaceuticals", "Automotive", "Food & Beverage",
            "Real Estate", "Insurance", "Mining", "Aerospace", "Entertainment"
        )
        
        val locations = listOf(
            "Industrial District", "Business Park", "Downtown", "Suburban Area",
            "Port District", "Airport Zone", "Technology Hub", "Financial District"
        )
        
        for (i in 1..count) {
            val business = Business(
                countryId = countryId,
                ownerId = (1..500).random().toLong(),
                ownerName = generateNPCName(),
                businessName = "${generateLastName()} ${businessNames.random()}",
                businessType = BusinessType.entries.random(),
                industry = industries.random(),
                location = locations.random(),
                employees = (1..50000).random(),
                revenue = (100_000..10_000_000_000).random().toDouble(),
                taxPaid = 0.0,
                licenseStatus = LicenseStatus.ACTIVE,
                complianceRating = (40.0..100.0).random()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate laws
     */
    private suspend fun generateLaws(countryId: Long, count: Int) {
        val lawPrefixes = listOf(
            "National", "Federal", "Public", "Economic", "Social",
            "Environmental", "Healthcare", "Education", "Infrastructure",
            "Defense", "Consumer", "Financial", "Labor", "Immigration",
            "Criminal Justice", "Tax", "Energy", "Transportation", "Housing"
        )
        
        val lawSuffixes = listOf(
            "Act", "Reform Act", "Protection Act", "Investment Act",
            "Modernization Act", "Emergency Act", "Regulation Act",
            "Support Act", "Development Act", "Accountability Act",
            "Transparency Act", "Security Act", "Welfare Act", "Rights Act"
        )
        
        val sponsors = listOf(
            "Ministry of Justice", "Ministry of Treasury", "Ministry of Health",
            "Ministry of Education", "Ministry of Defense", "Ministry of Labor",
            "Parliamentary Committee", "Presidential Office"
        )
        
        for (i in 1..count) {
            val lawType = LawType.entries.random()
            val status = LawStatus.entries.random()
            
            val law = Law(
                countryId = countryId,
                title = "${lawPrefixes.random()} ${lawSuffixes.random()}",
                description = generateLawDescription(lawType),
                lawType = lawType,
                status = status,
                introducedDate = System.currentTimeMillis() - (0..365 * 5).random() * 24 * 60 * 60 * 1000,
                passedDate = if (status == LawStatus.ENACTED) System.currentTimeMillis() - (0..365 * 3).random() * 24 * 60 * 60 * 1000 else null,
                sponsorName = sponsors.random(),
                fullText = generateLawText(),
                publicSupport = (20.0..80.0).random(),
                economicImpact = (-10.0..10.0).random(),
                socialImpact = (-10.0..10.0).random()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate court cases
     */
    private suspend fun generateCourtCases(countryId: Long, count: Int) {
        val caseTypes = listOf(
            CaseType.CIVIL, CaseType.CRIMINAL, CaseType.CONSTITUTIONAL,
            CaseType.ADMINISTRATIVE, CaseType.CORPORATE, CaseType.TAX
        )
        
        val courts = listOf(
            "Supreme Court", "High Court", "District Court",
            "Appeals Court", "Constitutional Court", "Administrative Tribunal"
        )
        
        val statuses = listOf(
            CaseStatus.FILED, CaseStatus.PRE_TRIAL, CaseStatus.TRIAL,
            CaseStatus.DELIBERATION, CaseStatus.VERDICT
        )
        
        for (i in 1..count) {
            val case = CourtCase(
                countryId = countryId,
                caseNumber = "CASE-${2020 + (0..4).random()}-${(1..9999).random()}",
                caseName = "${generateLastName()} v. ${generateLastName()}",
                caseType = caseTypes.random(),
                court = courts.random(),
                plaintiff = generateNPCName(),
                defendant = generateNPCName(),
                description = generateCaseDescription(),
                filingDate = System.currentTimeMillis() - (0..730).random() * 24 * 60 * 60 * 1000,
                status = statuses.random(),
                judge = generateNPCName(),
                damages = (0..100_000_000).random().toDouble()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate ministries and appointees
     */
    private suspend fun generateMinistries(countryId: Long) {
        val ministryData = listOf(
            MinistryData(MinistryType.DEFENSE, "Ministry of Defense", "National security and armed forces"),
            MinistryData(MinistryType.FOREIGN_AFFAIRS, "Ministry of Foreign Affairs", "International relations"),
            MinistryData(MinistryType.TREASURY, "Ministry of Treasury", "Economic policy and finance"),
            MinistryData(MinistryType.JUSTICE, "Ministry of Justice", "Legal system and law enforcement"),
            MinistryData(MinistryType.INTERIOR, "Ministry of Interior", "Domestic affairs and security"),
            MinistryData(MinistryType.HEALTH, "Ministry of Health", "Public health and healthcare"),
            MinistryData(MinistryType.EDUCATION, "Ministry of Education", "Schools and universities"),
            MinistryData(MinistryType.TRANSPORT, "Ministry of Transport", "Infrastructure and transit"),
            MinistryData(MinistryType.ENERGY, "Ministry of Energy", "Power and natural resources"),
            MinistryData(MinistryType.COMMERCE, "Ministry of Commerce", "Business and trade"),
            MinistryData(MinistryType.LABOR, "Ministry of Labor", "Employment and workers"),
            MinistryData(MinistryType.ENVIRONMENT, "Ministry of Environment", "Environmental protection")
        )
        
        ministryData.forEach { data ->
            val ministry = Ministry(
                countryId = countryId,
                name = data.name,
                ministerName = "Pending",
                budget = (50_000_000..5_000_000_000).random().toDouble(),
                allocatedBudget = 0.0,
                spentBudget = 0.0,
                employeeCount = (100..50000).random(),
                efficiency = (40.0..95.0).random(),
                corruptionLevel = (0.0..30.0).random(),
                publicApproval = (30.0..80.0).random()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate appointees (7 key positions)
     */
    private suspend fun generateAppointees(countryId: Long) {
        val keyPositions = listOf(
            AppointeeData("Chief of Staff", MinistryType.INTERIOR, "Coordinates executive operations"),
            AppointeeData("National Security Advisor", MinistryType.DEFENSE, "Advises on security matters"),
            AppointeeData("Economic Advisor", MinistryType.TREASURY, "Shapes economic policy"),
            AppointeeData("Press Secretary", MinistryType.INTERIOR, "Handles media relations"),
            AppointeeData("Legal Counsel", MinistryType.JUSTICE, "Provides legal advice"),
            AppointeeData("Policy Director", MinistryType.INTERIOR, "Develops policy agenda"),
            AppointeeData("Legislative Affairs", MinistryType.INTERIOR, "Liaises with parliament")
        )
        
        keyPositions.forEach { data ->
            val appointee = Appointee(
                countryId = countryId,
                name = generateNPCName(),
                position = data.position,
                ministry = data.ministry,
                appointmentDate = System.currentTimeMillis() - (0..365).random() * 24 * 60 * 60 * 1000,
                termLength = 4,
                qualifications = generateQualifications(),
                previousExperience = generateExperience(),
                approvalRating = (30.0..90.0).random(),
                loyalty = (40.0..100.0).random(),
                competence = (40.0..100.0).random()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate infrastructure projects
     */
    private suspend fun generateInfrastructureProjects(countryId: Long, count: Int) {
        val projectTypes = listOf(
            InfrastructureType.POWER_STATION to "Power Plant",
            InfrastructureType.HIGHWAY to "Highway Extension",
            InfrastructureType.BRIDGE to "Bridge Construction",
            InfrastructureType.RAILWAY to "Rail Line",
            InfrastructureType.AIRPORT to "Airport Expansion",
            InfrastructureType.SEAPORT to "Port Modernization",
            InfrastructureType.HOSPITAL to "New Hospital",
            InfrastructureType.SCHOOL to "School Complex",
            InfrastructureType.WATER_TREATMENT to "Water Treatment Facility",
            InfrastructureType.TELECOM_TOWER to "Telecom Network"
        )
        
        val statuses = listOf(
            ProjectStatus.PLANNING, ProjectStatus.APPROVED,
            ProjectStatus.CONSTRUCTION, ProjectStatus.COMPLETED
        )
        
        val locations = listOf(
            "Northern Region", "Southern Region", "Eastern Region",
            "Western Region", "Central Region", "Coastal Area",
            "Capital District", "Industrial Zone"
        )
        
        val contractors = listOf(
            "BuildCorp International", "ConstructCo", "Infrastructure Ltd",
            "Engineering Solutions", "National Builders", "Global Construction"
        )
        
        for (i in 1..count) {
            val projectType = projectTypes.random()
            val project = InfrastructureProject(
                countryId = countryId,
                projectType = projectType.first,
                name = "${projectType.second} #${(1..999).random()}",
                description = generateProjectDescription(projectType.first),
                location = locations.random(),
                contractor = contractors.random(),
                contractorRating = (50.0..100.0).random(),
                budget = (10_000_000..10_000_000_000).random().toDouble(),
                spent = 0.0,
                startDate = System.currentTimeMillis() - (0..730).random() * 24 * 60 * 60 * 1000,
                estimatedEndDate = System.currentTimeMillis() + (30..730).random() * 24 * 60 * 60 * 1000,
                progress = (0.0..100.0).random(),
                status = statuses.random(),
                environmentalImpact = (1.0..10.0).random(),
                publicSupport = (20.0..90.0).random(),
                workerCount = (50..5000).random()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate diplomatic relations
     */
    private suspend fun generateDiplomaticRelations(countryId: Long, count: Int) {
        val countryNames = listOf(
            "United Republics", "Federal States", "Kingdom of Nordland",
            "People's Republic of Eastaria", "Commonwealth of Westland",
            "Union of Southern States", "Confederation of Northland",
            "Empire of Solaria", "Democratic Federation", "Islamic Republic",
            "Grand Duchy", "Principality", "Free State", "Socialist Republic"
        )
        
        val allianceTypes = listOf(
            AllianceType.MUTUAL_DEFENSE, AllianceType.NON_AGGRESSION,
            AllianceType.NEUTRALITY, AllianceType.ENTENTE
        )
        
        for (i in 1..count) {
            val relationLevel = (-100..100).random()
            
            val relation = DiplomaticRelation(
                countryId = countryId,
                foreignCountryId = (1..100).random().toLong(),
                foreignCountryName = "${countryNames.random()} ${('A'..'Z').random()}",
                relationLevel = relationLevel,
                trustLevel = ((relationLevel + 100) / 2).coerceIn(0, 100),
                tradeVolume = (1_000_000..100_000_000_000).random().toDouble(),
                ambassadorName = if (random.nextBoolean()) generateNPCName() else null,
                embassyEstablished = relationLevel > -50,
                lastContact = System.currentTimeMillis() - (0..30).random() * 24 * 60 * 60 * 1000
            )
            // Would insert via DAO
            
            // Some countries have alliances
            if (relationLevel > 50 && random.nextDouble() < 0.3) {
                val alliance = Alliance(
                    countryId = countryId,
                    allianceName = "${allianceTypes.random().name} Pact",
                    allianceType = allianceTypes.random(),
                    members = "[${relation.foreignCountryId}]",
                    foundedDate = System.currentTimeMillis() - (365 * (1..10).random()) * 24 * 60 * 60 * 1000,
                    headquarters = "Capital City",
                    mutualDefense = allianceTypes.random() == AllianceType.MUTUAL_DEFENSE,
                    sharedTechnology = random.nextBoolean(),
                    jointExercises = random.nextBoolean()
                )
                // Would insert via DAO
            }
        }
    }
    
    /**
     * Generate health programs
     */
    private suspend fun generateHealthPrograms(countryId: Long) {
        val programs = listOf(
            HealthProgramData("National Vaccination Initiative", HealthProgramType.VACCINATION, "Countrywide immunization"),
            HealthProgramData("Disease Prevention Campaign", HealthProgramType.DISEASE_PREVENTION, "Public health education"),
            HealthProgramData("Maternal Health Program", HealthProgramType.MATERNAL_HEALTH, "Prenatal and postnatal care"),
            HealthProgramData("Mental Health Support", HealthProgramType.MENTAL_HEALTH, "Counseling services"),
            HealthProgramData("Addiction Treatment", HealthProgramType.ADDICTION_TREATMENT, "Rehabilitation programs"),
            HealthProgramData("Emergency Response", HealthProgramType.EMERGENCY_RESPONSE, "Disaster medical response"),
            HealthProgramData("Medical Research Fund", HealthProgramType.RESEARCH, "Disease research grants")
        )
        
        programs.forEach { data ->
            val program = HealthProgram(
                countryId = countryId,
                programName = data.name,
                programType = data.type,
                description = data.description,
                targetPopulation = "General Population",
                budget = (1_000_000..500_000_000).random().toDouble(),
                spent = 0.0,
                startDate = System.currentTimeMillis(),
                coverage = (10000..1000000).random(),
                effectiveness = (50.0..95.0).random()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate sports teams
     */
    private suspend fun generateSportsTeams(countryId: Long) {
        val sports = listOf(
            SportType.FOOTBALL, SportType.BASKETBALL, SportType.TENNIS,
            SportType.SWIMMING, SportType.ATHLETICS, SportType.CYCLING
        )
        
        val teamNames = listOf(
            "National", "United", "Athletic", "Sports", "Olympic"
        )
        
        sports.forEach { sport ->
            val team = SportsTeam(
                countryId = countryId,
                sportType = sport,
                teamName = "${teamNames.random()} ${sport.name}",
                coachName = generateNPCName(),
                ranking = (1..100).random(),
                wins = (0..50).random(),
                losses = (0..30).random(),
                draws = (0..20).random(),
                budget = (1_000_000..100_000_000).random().toDouble(),
                homeStadium = "National Stadium",
                fanBase = (10000..5000000).random(),
                morale = (40.0..100.0).random()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate stadiums
     */
    private suspend fun generateStadiums(countryId: Long, count: Int) {
        val stadiumNames = listOf(
            "National Stadium", "Central Arena", "Grand Stadium",
            "Metropolitan Stadium", "City Arena", "Regional Stadium",
            "Memorial Stadium", "Olympic Stadium", "Unity Arena"
        )
        
        val locations = listOf(
            "Capital City", "Northern District", "Southern District",
            "Eastern District", "Western District", "Coastal Area"
        )
        
        for (i in 1..count) {
            val stadium = Stadium(
                countryId = countryId,
                stadiumName = "${stadiumNames.random()} ${i}",
                location = locations.random(),
                capacity = (5000..100000).random(),
                sportType = SportType.entries.random(),
                yearBuilt = (1950..2024).random(),
                condition = (40.0..100.0).random(),
                averageAttendance = (1000..80000).random(),
                revenue = (100_000..50_000_000).random().toDouble()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate social groups
     */
    private suspend fun generateSocialGroups(countryId: Long) {
        val groups = listOf(
            SocialGroupData("Working Class", GroupType.ECONOMIC_CLASS, 45, 35000.0),
            SocialGroupData("Middle Class", GroupType.ECONOMIC_CLASS, 35, 75000.0),
            SocialGroupData("Upper Class", GroupType.ECONOMIC_CLASS, 5, 500000.0),
            SocialGroupData("Ethnic Minority A", GroupType.ETHNIC, 8, 28000.0),
            SocialGroupData("Ethnic Minority B", GroupType.ETHNIC, 5, 32000.0),
            SocialGroupData("Religious Group A", GroupType.RELIGIOUS, 15, 40000.0),
            SocialGroupData("Religious Group B", GroupType.RELIGIOUS, 10, 45000.0),
            SocialGroupData("Youth (18-29)", GroupType.CULTURAL, 20, 25000.0),
            SocialGroupData("Seniors (65+)", GroupType.CULTURAL, 15, 20000.0)
        )
        
        groups.forEach { data ->
            val group = SocialGroup(
                countryId = countryId,
                groupName = data.name,
                groupType = data.type,
                population = (100000..5000000).random().toLong(),
                percentage = data.percentage.toDouble(),
                averageIncome = data.avgIncome,
                unemploymentRate = (2.0..25.0).random(),
                educationLevel = (30.0..95.0).random(),
                satisfactionLevel = (20.0..80.0).random(),
                tensionLevel = (0.0..50.0).random()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate lobbyists
     */
    private suspend fun generateLobbyists(countryId: Long, count: Int) {
        val industries = listOf(
            "Pharmaceuticals", "Oil & Gas", "Technology", "Finance",
            "Defense", "Agriculture", "Telecommunications", "Automotive",
            "Healthcare", "Energy", "Real Estate", "Insurance"
        )
        
        val orgNames = listOf(
            "Association", "Council", "Institute", "Alliance",
            "Federation", "Coalition", "League", "Board"
        )
        
        for (i in 1..count) {
            val lobbyist = Lobbyist(
                countryId = countryId,
                name = generateNPCName(),
                organization = "${industries.random()} ${orgNames.random()}",
                industry = industries.random(),
                fundsAvailable = (100_000..50_000_000).random().toDouble(),
                influence = (20.0..100.0).random(),
                successRate = (30.0..90.0).random()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate contractors
     */
    private suspend fun generateContractors(countryId: Long, count: Int) {
        val companyNames = listOf(
            "Build", "Construct", "Engineering", "Infrastructure",
            "Development", "Contracting", "Industrial", "Civil Works"
        )
        
        val suffixes = listOf(
            "Corp", "Ltd", "Inc", "LLC", "Group", "Solutions", "Systems"
        )
        
        for (i in 1..count) {
            val contractor = Contractor(
                countryId = countryId,
                companyName = "${companyNames.random()} ${suffixes.random()}",
                ownerName = generateNPCName(),
                industry = "Construction",
                rating = (40.0..100.0).random(),
                pastProjects = (5..500).random(),
                completedOnTime = (50..100).random(),
                completedOnBudget = (50..100).random(),
                safetyRecord = (50.0..100.0).random(),
                totalValue = (1_000_000..10_000_000_000).random().toDouble()
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate initial micro tasks
     */
    private suspend fun generateInitialTasks(countryId: Long) {
        val tasks = listOf(
            MicroTaskData(MicroTaskType.ATTEND_MEETING, "First Cabinet Meeting", "Meet with cabinet members", TaskCategory.POLITICS, Priority.HIGH),
            MicroTaskData(MicroTaskType.GIVE_SPEECH, "Inaugural Address", "Address the nation", TaskCategory.POLITICS, Priority.HIGH),
            MicroTaskData(MicroTaskType.REVIEW_BUDGET, "Review National Budget", "Examine treasury reports", TaskCategory.ECONOMY, Priority.HIGH),
            MicroTaskData(MicroTaskType.MAKE_CALL, "Call Allied Leaders", "Establish diplomatic contact", TaskCategory.DIPLOMACY, Priority.NORMAL),
            MicroTaskData(MicroTaskType.SIGN_DOCUMENT, "Sign Executive Orders", "Review pending orders", TaskCategory.LAW, Priority.NORMAL),
            MicroTaskData(MicroTaskType.APPROVE_HIRE, "Appoint Advisors", "Select key staff", TaskCategory.POLITICS, Priority.HIGH),
            MicroTaskData(MicroTaskType.REVIEW_BUDGET, "Defense Budget Review", "Analyze military spending", TaskCategory.DEFENSE, Priority.NORMAL),
            MicroTaskData(MicroTaskType.ALLOCATE_FUNDS, "Emergency Fund Allocation", "Reserve contingency funds", TaskCategory.ECONOMY, Priority.NORMAL)
        )
        
        tasks.forEach { data ->
            val task = MicroTask(
                countryId = countryId,
                taskType = data.type,
                title = data.title,
                description = data.description,
                category = data.category,
                priority = data.priority,
                status = TaskStatus.PENDING,
                relatedEntityId = null,
                relatedEntityType = null,
                selectedOption = null,
                completedDate = null,
                createdDate = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis() + (1..7).random() * 24 * 60 * 60 * 1000
            )
            // Would insert via DAO
        }
    }
    
    /**
     * Generate procedural tables for random content
     */
    private suspend fun generateProceduralTables(countryId: Long) {
        // Names table
        val names = (1..1000).map { i ->
            mapOf(
                "id" to i,
                "first_male" to firstNamesMale.random(),
                "first_female" to firstNamesFemale.random(),
                "last" to lastNames.random(),
                "title" to listOf("Mr.", "Mrs.", "Ms.", "Dr.", "Prof.").random()
            )
        }
        
        // Places table
        val places = (1..500).map { i ->
            mapOf(
                "id" to i,
                "city" to cityNames.random(),
                "region" to regions.random(),
                "country" to countryNames.random(),
                "street" to listOf("Main St", "Oak Ave", "Park Blvd", "River Rd", "Hill Dr").random()
            )
        }
        
        // Organizations table
        val organizations = (1..300).map { i ->
            mapOf(
                "id" to i,
                "name" to "${adjectives.random()} ${nouns.random()} ${orgTypes.random()}",
                "type" to listOf("Corporation", "NGO", "Government", "Union", "Association").random(),
                "industry" to industries.random()
            )
        }
        
        // Events table
        val events = (1..200).map { i ->
            mapOf(
                "id" to i,
                "name" to eventNames.random(),
                "type" to listOf("Crisis", "Opportunity", "Disaster", "Breakthrough", "Scandal").random(),
                "severity" to (1..10).random()
            )
        }
        
        // Would store these in ProceduralTable entity
    }
    
    // Helper functions
    private fun generateNPCName(isMale: Boolean = true): String {
        val first = if (isMale) firstNamesMale.random() else firstNamesFemale.random()
        val last = lastNames.random()
        return "$first $last"
    }
    
    private fun generateLastName(): String = lastNames.random()
    
    private fun generatePersonality(): String {
        val traits = listOf(
            "Ambitious", "Honest", "Cunning", "Loyal", "Greedy", "Idealistic",
            "Pragmatic", "Charismatic", "Introverted", "Aggressive", "Diplomatic",
            "Corrupt", "Principled", "Opportunistic", "Compassionate", "Ruthless"
        )
        return traits.shuffled().take(3).joinToString(", ")
    }
    
    private fun generateLawDescription(type: LawType): String {
        return when (type) {
            LawType.TAX -> "Legislation to modify tax code and revenue collection"
            LawType.CRIMINAL -> "Criminal justice reform and sentencing guidelines"
            LawType.CIVIL -> "Civil rights and liberties protection"
            LawType.REGULATORY -> "Industry regulations and compliance requirements"
            LawType.CONSTITUTIONAL -> "Constitutional amendments and interpretations"
            LawType.EMERGENCY -> "Emergency powers and crisis response"
            LawType.BUDGET -> "Budget allocation and fiscal responsibility"
            LawType.SOCIAL -> "Social welfare and public services"
            LawType.ECONOMIC -> "Economic policy and market regulation"
            LawType.ENVIRONMENTAL -> "Environmental protection and sustainability"
        }
    }
    
    private fun generateLawText(): String {
        return """
            BE IT ENACTED by the Parliament as follows:
            
            SECTION 1. SHORT TITLE
            This Act may be cited as the "National Reform Act".
            
            SECTION 2. FINDINGS AND PURPOSES
            (a) FINDINGS.—The Parliament finds that—
                (1) current conditions require legislative action;
                (2) the public interest necessitates reform;
                (3) this legislation serves the national good.
            
            (b) PURPOSES.—The purposes of this Act are—
                (1) to improve existing systems;
                (2) to promote fairness and efficiency;
                (3) to protect public welfare.
            
            SECTION 3. IMPLEMENTATION
            The relevant Ministry shall implement this Act within 180 days.
            
            SECTION 4. FUNDING
            Such sums as may be necessary are authorized.
            
            SECTION 5. EFFECTIVE DATE
            This Act takes effect upon enactment.
        """.trimIndent()
    }
    
    private fun generateCaseDescription(): String {
        val descriptions = listOf(
            "Dispute over contract terms and breach of agreement",
            "Constitutional challenge to government action",
            "Criminal prosecution for alleged offenses",
            "Civil lawsuit seeking damages",
            "Administrative appeal of agency decision",
            "Corporate merger antitrust review",
            "Tax dispute with revenue authority"
        )
        return descriptions.random()
    }
    
    private fun generateProjectDescription(type: InfrastructureType): String {
        return when (type) {
            InfrastructureType.POWER_STATION -> "Large-scale power generation facility"
            InfrastructureType.HIGHWAY -> "Multi-lane highway expansion project"
            InfrastructureType.BRIDGE -> "Major bridge construction over waterway"
            InfrastructureType.RAILWAY -> "High-speed rail line development"
            InfrastructureType.AIRPORT -> "International airport terminal expansion"
            InfrastructureType.SEAPORT -> "Deep-water port modernization"
            InfrastructureType.HOSPITAL -> "Regional medical center construction"
            InfrastructureType.SCHOOL -> "Educational facility complex"
            InfrastructureType.WATER_TREATMENT -> "Municipal water treatment plant"
            InfrastructureType.TELECOM_TOWER -> "Telecommunications network infrastructure"
            else -> "Infrastructure development project"
        }
    }
    
    private fun generateQualifications(): String {
        val quals = listOf(
            "PhD in Public Policy", "MBA from Top University",
            "JD Law Degree", "MA International Relations",
            "MS Economics", "BA Political Science",
            "Former Government Official", "Industry Executive",
            "Military Veteran", "Academic Researcher"
        )
        return quals.shuffled().take(2).joinToString(", ")
    }
    
    private fun generateExperience(): String {
        return "${(5..30).random()} years experience in public service and policy"
    }
    
    // Data classes for generation
    data class PartyData(val name: String, val abbreviation: String, val ideology: String, val description: String)
    data class OccupationData(val name: String, val type: OccupationType, val minIncome: Double, val maxIncome: Double)
    data class MinistryData(val type: MinistryType, val name: String, val description: String)
    data class AppointeeData(val position: String, val ministry: MinistryType, val description: String)
    data class HealthProgramData(val name: String, val type: HealthProgramType, val description: String)
    data class SocialGroupData(val name: String, val type: GroupType, val percentage: Int, val avgIncome: Double)
    data class MicroTaskData(val type: MicroTaskType, val title: String, val description: String, val category: TaskCategory, val priority: Priority)
    
    // Name and content pools
    private val firstNamesMale = listOf(
        "James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph",
        "Thomas", "Charles", "Christopher", "Daniel", "Matthew", "Anthony", "Donald",
        "Ahmed", "Mohammed", "Ali", "Hassan", "Ibrahim", "Wei", "Li", "Zhang",
        "Vladimir", "Dmitri", "Alexander", "Pierre", "Hans", "Giovanni", "Carlos",
        "Raj", "Kumar", "Singh", "Takeshi", "Hiroshi", "Kim", "Park", "Lee"
    )
    
    private val firstNamesFemale = listOf(
        "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica",
        "Sarah", "Karen", "Nancy", "Lisa", "Betty", "Margaret", "Sandra",
        "Fatima", "Aisha", "Zainab", "Mariam", "Wei", "Li", "Ying",
        "Natasha", "Ekaterina", "Anastasia", "Marie", "Hans", "Giovanna", "Maria", "Sofia",
        "Priya", "Sunita", "Anjali", "Yuki", "Sakura", "Ji-Young", "Min-Ji", "Soo-Jin"
    )
    
    private val lastNames = listOf(
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
        "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
        "Ahmed", "Mohammed", "Ali", "Hassan", "Khan", "Patel", "Kumar", "Singh",
        "Wang", "Li", "Zhang", "Chen", "Liu", "Kim", "Park", "Lee", "Choi",
        "Ivanov", "Petrov", "Sidorov", "Dubois", "Martin", "Bernard", "Mueller", "Schmidt",
        "Rossi", "Bianchi", "Romano", "Serra", "Colombo", "Ricci", "Ferrari", "Russo"
    )
    
    private val cityNames = listOf(
        "Springfield", "Riverside", "Lakewood", "Hillcrest", "Fairview",
        "Greenville", "Madison", "Georgetown", "Clinton", "Franklin",
        "Washington", "Lincoln", "Jefferson", "Hamilton", "Jackson",
        "Arlington", "Columbus", "Indianapolis", "Charlotte", "Seattle"
    )
    
    private val regions = listOf(
        "Northern Province", "Southern Province", "Eastern Province",
        "Western Province", "Central Province", "Capital Region",
        "Coastal Region", "Mountain Region", "Valley Region"
    )
    
    private val countryNames = listOf(
        "United", "Federal", "Democratic", "People's", "Republic",
        "Kingdom", "Commonwealth", "Union", "Confederation", "Empire"
    )
    
    private val adjectives = listOf(
        "National", "International", "Global", "United", "Federal",
        "Central", "American", "European", "Asian", "World",
        "Advanced", "Modern", "Dynamic", "Progressive", "Strategic"
    )
    
    private val nouns = listOf(
        "Technology", "Solutions", "Industries", "Systems", "Dynamics",
        "Enterprises", "Holdings", "Group", "Corp", "Associates"
    )
    
    private val orgTypes = listOf(
        "Association", "Organization", "Foundation", "Institute",
        "Corporation", "Company", "Alliance", "Union", "Coalition"
    )
    
    private val industries = listOf(
        "Technology", "Finance", "Manufacturing", "Retail", "Healthcare",
        "Energy", "Agriculture", "Transportation", "Media", "Construction"
    )
    
    private val eventNames = listOf(
        "Economic Summit", "Trade Negotiation", "Military Exercise",
        "Cultural Festival", "Sports Tournament", "Political Crisis",
        "Natural Disaster", "Scientific Breakthrough", "Diplomatic Incident"
    )
}
