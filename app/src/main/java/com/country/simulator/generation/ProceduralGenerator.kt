package com.country.simulator.generation

import com.country.simulator.model.*
import kotlin.random.Random

object ProceduralGenerator {
    
    // Country name generators
    private val countryPrefixes = listOf(
        "United", "New", "South", "North", "East", "West", "Central", "Northern", 
        "Southern", "Eastern", "Western", "Republic of", "Kingdom of", "Grand", "Federal"
    )
    
    private val countryRoots = listOf(
        "land", "ia", "stan", "burg", "ton", "ville", "port", "mouth", 
        "field", "ford", "bridge", "haven", "castle", "mount", "river"
    )
    
    private val countryNames = listOf(
        "America", "Britain", "France", "Germany", "Italy", "Spain", "Russia", "China",
        "Japan", "India", "Brazil", "Argentina", "Mexico", "Canada", "Australia",
        "Egypt", "Nigeria", "Kenya", "Turkey", "Iran", "Saudi Arabia", "Israel",
        "Korea", "Vietnam", "Thailand", "Indonesia", "Malaysia", "Philippines",
        "Poland", "Ukraine", "Sweden", "Norway", "Denmark", "Finland", "Netherlands",
        "Belgium", "Switzerland", "Austria", "Portugal", "Greece", "Romania",
        "Colombia", "Chile", "Peru", "Venezuela", "Uruguay", "Paraguay", "Bolivia",
        "Morocco", "Algeria", "Tunisia", "Libya", "Sudan", "Ethiopia", "Ghana"
    )
    
    private val cityNames = listOf(
        "Washington", "London", "Paris", "Berlin", "Rome", "Madrid", "Moscow", "Beijing",
        "Tokyo", "Delhi", "Brasilia", "Buenos Aires", "Mexico City", "Ottawa", "Canberra",
        "Cairo", "Lagos", "Nairobi", "Ankara", "Tehran", "Riyadh", "Jerusalem",
        "Seoul", "Hanoi", "Bangkok", "Jakarta", "Kuala Lumpur", "Manila",
        "Warsaw", "Kiev", "Stockholm", "Oslo", "Copenhagen", "Helsinki", "Amsterdam",
        "Brussels", "Bern", "Vienna", "Lisbon", "Athens", "Bucharest",
        "Bogota", "Santiago", "Lima", "Caracas", "Montevideo", "Asuncion", "La Paz",
        "Rabat", "Algiers", "Tunis", "Tripoli", "Khartoum", "Addis Ababa", "Accra"
    )
    
    // NPC name generators
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
    
    // Organization names
    private val orgPrefixes = listOf(
        "National", "International", "Global", "United", "Federal", "Central",
        "American", "European", "Asian", "African", "World", "Pan"
    )
    
    private val orgTypes = listOf(
        "Association", "Organization", "Foundation", "Institute", "Corporation",
        "Company", "Group", "Alliance", "Union", "Coalition", "Federation", "Council"
    )
    
    // Random seed for reproducibility
    private var seed: Long = System.currentTimeMillis()
    
    fun setSeed(newSeed: Long) {
        seed = newSeed
    }
    
    private fun random(): Random = Random(seed.hashCode())
    
    // Country generation
    fun generateCountry(isPlayer: Boolean = false): Country {
        val name = generateCountryName()
        return Country(
            name = name,
            shortName = name.take(3).uppercase(),
            flag = generateFlagEmoji(),
            capital = generateCityName(),
            population = (1_000_000..100_000_000).random(),
            gdp = (1_000_000_000..10_000_000_000_000).random().toDouble(),
            governmentType = GovernmentType.entries.random(),
            ideology = NationalIdeology.entries.random(),
            electionCycle = ElectionCycle.entries.random(),
            oilReserves = (0.0..100_000_000_000).random(),
            naturalGas = (0.0..50_000_000_000).random(),
            minerals = (0.0..30_000_000_000).random(),
            agriculturalLand = (10.0..90.0).random(),
            diplomaticStanding = 50,
            tradeRelations = 50,
            isActive = true,
            isPlayerCountry = isPlayer
        )
    }
    
    fun generateCountryName(): String {
        return when {
            random().nextBoolean() -> {
                "${countryPrefixes.random()} ${countryNames.random()}"
            }
            random().nextBoolean() -> {
                "${countryNames.random().dropLast(1)}${countryRoots.random()}"
            }
            else -> {
                countryNames.random()
            }
        }
    }
    
    fun generateCityName(): String {
        return cityNames.random()
    }
    
    fun generateFlagEmoji(): String {
        // Generate random flag emoji from regional indicators
        val letters = ('A'..'Z').toList()
        val letter1 = letters.random(random())
        val letter2 = letters.random(random())
        return "\uD83C\uDD${letter1.code - 0x41}\uD83C\uDD${letter2.code - 0x41}"
    }
    
    // NPC generation
    fun generateNPC(countryId: Long, isImportant: Boolean = false): NPC {
        val isMale = random().nextBoolean()
        val firstName = if (isMale) firstNamesMale.random(random()) else firstNamesFemale.random(random())
        val lastName = lastNames.random(random())
        val name = "$firstName $lastName"
        
        val age = (18..80).random(random())
        val occupation = generateOccupation()
        
        return NPC(
            countryId = countryId,
            name = name,
            age = age,
            gender = if (isMale) Gender.MALE else Gender.FEMALE,
            occupation = occupation.first,
            occupationType = occupation.second,
            income = generateIncome(occupation.second),
            wealth = (0..10_000_000).random(random()).toDouble(),
            education = (8..20).random(random()),
            location = generateCityName(),
            region = "Region ${('A'..'Z').random(random())}",
            personality = generatePersonality(),
            politicalLeanings = (-100..100).random(random()),
            maritalStatus = listOf("Single", "Married", "Divorced", "Widowed").random(random()),
            children = (0..5).random(random()),
            happiness = (0.0..100.0).random(random()),
            health = (50.0..100.0).random(random()),
            approvalOfGovernment = (0.0..100.0).random(random()),
            trustInInstitutions = (0.0..100.0).random(random()),
            isImportant = isImportant
        )
    }
    
    private fun generateOccupation(): Pair<String, OccupationType> {
        val type = OccupationType.entries.random(random())
        return type.name.replace("_", " ") to type
    }
    
    private fun generateIncome(occupationType: OccupationType): Double {
        return when (occupationType) {
            OccupationType.POLITICIAN -> (50_000..500_000).random(random()).toDouble()
            OccupationType.BUSINESS_OWNER -> (100_000..10_000_000).random(random()).toDouble()
            OccupationType.CORPORATE_EXEC -> (200_000..5_000_000).random(random()).toDouble()
            OccupationType.WORKER -> (20_000..80_000).random(random()).toDouble()
            OccupationType.FARMER -> (15_000..100_000).random(random()).toDouble()
            OccupationType.TEACHER -> (35_000..80_000).random(random()).toDouble()
            OccupationType.DOCTOR -> (100_000..500_000).random(random()).toDouble()
            OccupationType.LAWYER -> (80_000..500_000).random(random()).toDouble()
            OccupationType.ENGINEER -> (60_000..200_000).random(random()).toDouble()
            OccupationType.UNEMPLOYED -> (0..10_000).random(random()).toDouble()
            else -> (30_000..150_000).random(random()).toDouble()
        }
    }
    
    private fun generatePersonality(): String {
        val traits = listOf(
            "Ambitious", "Honest", "Cunning", "Loyal", "Greedy", "Idealistic",
            "Pragmatic", "Charismatic", "Introverted", "Aggressive", "Diplomatic",
            "Corrupt", "Principled", "Opportunistic", "Compassionate", "Ruthless"
        )
        return traits.shuffled(random()).take(3).joinToString(", ")
    }
    
    // Political party generation
    fun generatePoliticalParty(countryId: Long): PoliticalParty {
        val ideologies = listOf(
            "Conservative", "Liberal", "Socialist", "Communist", "Green",
            "Libertarian", "Nationalist", "Centrist", "Populist", "Christian Democrat"
        )
        val ideology = ideologies.random(random())
        val name = "$ideology Party"
        
        return PoliticalParty(
            countryId = countryId,
            name = name,
            abbreviation = name.filter { it.isUpperCase() },
            ideology = ideology,
            leaderName = generateNPC(countryId, true).name,
            foundingYear = (1800..2024).random(random()),
            funds = (1_000_000..100_000_000).random(random()).toDouble(),
            approvalRating = (5.0..50.0).random(random())
        )
    }
    
    // Scandal generation
    fun generateScandal(countryId: Long): Scandal {
        val types = ScandalType.entries.random(random())
        val titles = listOf(
            "Corruption Allegations", "Financial Misconduct", "Abuse of Power",
            "Foreign Interference", "Cover-up Scandal", "Bribery Charges",
            "Embezzlement", "Nepotism", "Conflict of Interest", "Leak Investigation"
        )
        
        return Scandal(
            countryId = countryId,
            title = titles.random(random()),
            description = "Investigation ongoing into allegations of ${types.name.lowercase().replace("_", " ")}",
            severity = (1..10).random(random()),
            type = types,
            involvedPersons = listOf(generateNPC(countryId, true).name),
            dateExposed = System.currentTimeMillis() - (0..365).random(random()) * 24 * 60 * 60 * 1000,
            impactOnApproval = -(1.0..10.0).random(random())
        )
    }
    
    // Law generation
    fun generateLaw(countryId: Long): Law {
        val types = LawType.entries.random(random())
        val prefixes = listOf(
            "National", "Federal", "Public", "Economic", "Social", "Environmental",
            "Healthcare", "Education", "Infrastructure", "Defense"
        )
        val suffixes = listOf(
            "Act", "Reform Act", "Protection Act", "Investment Act", "Modernization Act",
            "Emergency Act", "Regulation Act", "Support Act", "Development Act"
        )
        
        return Law(
            countryId = countryId,
            title = "${prefixes.random(random())} ${suffixes.random(random())}",
            description = "Legislation to address ${types.name.lowercase().replace("_", " ")} matters",
            lawType = types,
            status = LawStatus.DRAFT,
            introducedDate = System.currentTimeMillis(),
            sponsorName = generateNPC(countryId, true).name,
            fullText = "BE IT ENACTED...",
            publicSupport = (30.0..70.0).random(random()),
            economicImpact = (-10.0..10.0).random(random()),
            socialImpact = (-10.0..10.0).random(random())
        )
    }
    
    // Business generation
    fun generateBusiness(countryId: Long): Business {
        val types = BusinessType.entries.random(random())
        val industries = listOf(
            "Technology", "Finance", "Manufacturing", "Retail", "Healthcare",
            "Energy", "Agriculture", "Transportation", "Media", "Construction"
        )
        val owner = generateNPC(countryId)
        
        return Business(
            countryId = countryId,
            ownerId = 0,
            ownerName = owner.name,
            businessName = "${owner.lastName}'s ${industries.random(random())}",
            businessType = types,
            industry = industries.random(random()),
            location = generateCityName(),
            employees = (1..10000).random(random()),
            revenue = (100_000..1_000_000_000).random(random()).toDouble(),
            taxPaid = 0.0,
            licenseStatus = LicenseStatus.ACTIVE,
            complianceRating = (50.0..100.0).random(random())
        )
    }
    
    // Micro task generation
    fun generateMicroTask(countryId: Long, type: MicroTaskType? = null): MicroTask {
        val taskType = type ?: MicroTaskType.entries.random(random())
        val titles = mapOf(
            MicroTaskType.APPROVE_PERMIT to "Approve Construction Permit",
            MicroTaskType.REVIEW_REPORT to "Review Economic Report",
            MicroTaskType.SIGN_DOCUMENT to "Sign Legislation",
            MicroTaskType.MAKE_CALL to "Make Diplomatic Call",
            MicroTaskType.ATTEND_MEETING to "Attend Cabinet Meeting",
            MicroTaskType.GIVE_SPEECH to "Give Public Address",
            MicroTaskType.RESPOND_TO_CRISIS to "Respond to Crisis",
            MicroTaskType.NEGOTIATE_DEAL to "Negotiate Trade Deal",
            MicroTaskType.APPROVE_HIRE to "Approve Appointment",
            MicroTaskType.REVIEW_BUDGET to "Review Budget Proposal",
            MicroTaskType.SIGN_TREATY to "Sign International Treaty",
            MicroTaskType.DECLARE_POLICY to "Declare National Policy",
            MicroTaskType.INVESTIGATE_SCANDAL to "Investigate Scandal",
            MicroTaskType.AWARD_CONTRACT to "Award Government Contract",
            MicroTaskType.VETO_LEGISLATION to "Consider Veto",
            MicroTaskType.GRANT_PARDON to "Review Pardon Request",
            MicroTaskType.DEPLOY_TROOPS to "Consider Military Deployment",
            MicroTaskType.EXPUL_DIPLOMAT to "Handle Diplomatic Incident",
            MicroTaskType.LAUNCH_AUDIT to "Launch Financial Audit",
            MicroTaskType.SET_PRICE to "Set Resource Price",
            MicroTaskType.ADJUST_RATE to "Adjust Interest Rate",
            MicroTaskType.ALLOCATE_FUNDS to "Allocate Emergency Funds",
            MicroTaskType.REVIEW_CASE to "Review Court Case",
            MicroTaskType.APPOINT_JUDGE to "Appoint Judge",
            MicroTaskType.SCHEDULE_EVENT to "Schedule State Event"
        )
        
        return MicroTask(
            countryId = countryId,
            taskType = taskType,
            title = titles[taskType] ?: "Complete Task",
            description = "Action required for ${taskType.name.lowercase().replace("_", " ")}",
            category = TaskCategory.entries.random(random()),
            priority = Priority.entries.random(random()),
            status = TaskStatus.PENDING,
            relatedEntityId = null,
            relatedEntityType = null,
            selectedOption = null,
            completedDate = null,
            createdDate = System.currentTimeMillis(),
            dueDate = System.currentTimeMillis() + (1..7).random(random()) * 24 * 60 * 60 * 1000
        )
    }
    
    // Generate flag emoji from country code
    fun generateFlagEmoji(countryCode: String): String {
        return countryCode.uppercase().map { 
            "\uD83C\uDD${it.code - 0x41}" 
        }.joinToString("")
    }
}
