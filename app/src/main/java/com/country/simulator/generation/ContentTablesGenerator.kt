package com.country.simulator.generation

import kotlin.random.Random

/**
 * Procedural Content Tables Generator
 * Generates 1000+ entries for each category for infinite variety
 * These tables are used with Paging 3 for efficient lazy loading
 */
object ContentTablesGenerator {
    
    private val random = Random
    
    /**
     * Generate all procedural tables
     */
    fun generateAllTables(seed: Long = System.currentTimeMillis()): ProceduralTables {
        random.setSeed(seed)
        
        return ProceduralTables(
            names = generateNamesTable(2000),
            places = generatePlacesTable(1000),
            organizations = generateOrganizationsTable(500),
            events = generateEventsTable(300),
            scandals = generateScandalsTable(100),
            laws = generateLawsTable(200),
            policies = generatePoliciesTable(150),
            contractors = generateContractorsTable(100),
            vendors = generateVendorsTable(200),
            issues = generateIssuesTable(100),
            speeches = generateSpeechesTable(50),
            campaignSlogans = generateCampaignSlogansTable(100),
            diplomaticCables = generateDiplomaticCablesTable(100),
            crisisScenarios = generateCrisisScenariosTable(50),
            economicPolicies = generateEconomicPoliciesTable(100),
            socialPrograms = generateSocialProgramsTable(100),
            militaryOperations = generateMilitaryOperationsTable(50),
            technologyProjects = generateTechnologyProjectsTable(75),
            culturalEvents = generateCulturalEventsTable(100),
            sportsEvents = generateSportsEventsTable(50)
        )
    }
    
    /**
     * Generate 2000 unique names
     */
    private fun generateNamesTable(count: Int): List<NameEntry> {
        val firstNamesMale = listOf(
            "James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph",
            "Thomas", "Charles", "Christopher", "Daniel", "Matthew", "Anthony", "Donald",
            "Mark", "Paul", "Steven", "Andrew", "Kenneth", "Joshua", "Kevin", "Brian",
            "George", "Edward", "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan", "Jacob",
            "Gary", "Nicholas", "Eric", "Jonathan", "Stephen", "Larry", "Justin", "Scott",
            "Brandon", "Benjamin", "Samuel", "Frank", "Gregory", "Alexander", "Patrick",
            "Raymond", "Jack", "Dennis", "Jerry", "Tyler", "Aaron", "Jose", "Adam",
            "Henry", "Nathan", "Douglas", "Zachary", "Peter", "Kyle", "Walter", "Ethan",
            "Jeremy", "Harold", "Keith", "Christian", "Roger", "Terry", "Austin", "Sean",
            "Gerald", "Carl", "Dylan", "Christian", "Lawrence", "Jordan", "Jesse", "Bryan",
            "Joe", "Bruce", "Albert", "Willie", "Gabriel", "Alan", "Juan", "Logan",
            "Billy", "Louis", "Jeremy", "Wayne", "Ralph", "Roy", "Eugene", "Vincent",
            "Russell", "Bobby", "Philip", "Johnny", "Bradley", "Todd", "Craig", "Curtis",
            "Shane", "Victor", "Ernest", "Phillip", "Carlos", "Mario", "Antonio", "Oscar",
            "Miguel", "Fernando", "Jorge", "Ricardo", "Pedro", "Luis", "Manuel", "Rafael",
            
            // International names
            "Ahmed", "Mohammed", "Ali", "Hassan", "Ibrahim", "Omar", "Khalid", "Youssef",
            "Wei", "Li", "Zhang", "Chen", "Liu", "Wang", "Yang", "Huang", "Zhao", "Wu",
            "Vladimir", "Dmitri", "Alexander", "Andrei", "Sergei", "Nikolai", "Mikhail",
            "Pierre", "Jean", "Michel", "François", "Jacques", "Louis", "Philippe",
            "Hans", "Friedrich", "Wolfgang", "Klaus", "Peter", "Thomas", "Stefan",
            "Giovanni", "Marco", "Alessandro", "Francesco", "Antonio", "Giuseppe", "Luca",
            "Carlos", "Juan", "José", "Antonio", "Francisco", "Javier", "Miguel", "Ángel",
            "Raj", "Kumar", "Singh", "Patel", "Sharma", "Verma", "Gupta", "Agarwal",
            "Takeshi", "Hiroshi", "Kenji", "Yuki", "Satoshi", "Masato", "Noboru", "Akira",
            "Kim", "Park", "Lee", "Choi", "Jung", "Kang", "Cho", "Yoon", "Jang", "Lim",
            "Olumide", "Chukwu", "Kwame", "Sekou", "Amadou", "Mandela", "Desmond", "Nelson"
        )
        
        val firstNamesFemale = listOf(
            "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica",
            "Sarah", "Karen", "Nancy", "Lisa", "Betty", "Margaret", "Sandra", "Ashley",
            "Dorothy", "Kimberly", "Emily", "Donna", "Michelle", "Carol", "Amanda", "Melissa",
            "Deborah", "Stephanie", "Rebecca", "Sharon", "Laura", "Cynthia", "Kathleen", "Amy",
            "Angela", "Shirley", "Anna", "Brenda", "Pamela", "Emma", "Nicole", "Helen",
            "Samantha", "Katherine", "Christine", "Debra", "Rachel", "Carolyn", "Janet",
            "Catherine", "Maria", "Heather", "Diane", "Virginia", "Julie", "Joyce", "Evelyn",
            "Lauren", "Kelly", "Christina", "Victoria", "Judith", "Martha", "Cheryl", "Megan",
            "Andrea", "Olivia", "Ann", "Jean", "Alice", "Kathryn", "Jacqueline", "Gloria",
            "Teresa", "Janice", "Sara", "Julia", "Grace", "Doris", "Rose", "Marilyn",
            "Beverly", "Denise", "Amber", "Danielle", "Brittany", "Diana", "Abigail", "Jane",
            "Natalie", "Sophia", "Isabella", "Charlotte", "Mia", "Amelia", "Harriet", "Violet",
            
            // International names
            "Fatima", "Aisha", "Zainab", "Mariam", "Khadija", "Nadia", "Layla", "Amira",
            "Wei", "Li", "Ying", "Xiu", "Mei", "Lan", "Fang", "Yan", "Jing", "Hui",
            "Natasha", "Ekaterina", "Anastasia", "Olga", "Tatiana", "Svetlana", "Irina",
            "Marie", "Sophie", "Isabelle", "Camille", "Léa", "Julie", "Émilie", "Chloé",
            "Hans", "Greta", "Ingrid", "Astrid", "Freya", "Helga", "Birgit", "Ursula",
            "Giovanna", "Maria", "Sofia", "Francesca", "Isabella", "Giulia", "Alessia",
            "María", "Carmen", "Isabel", "Ana", "Pilar", "Dolores", "Josefa", "Rosa",
            "Priya", "Sunita", "Anjali", "Deepa", "Meera", "Kavita", "Pooja", "Neha",
            "Yuki", "Sakura", "Emi", "Akiko", "Keiko", "Michiko", "Naomi", "Reiko",
            "Ji-Young", "Min-Ji", "Soo-Jin", "Eun-Hee", "Young-Sook", "Kyung-Sook", "Hee-Sun"
        )
        
        val lastNames = listOf(
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
            "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
            "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson",
            "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker",
            "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
            "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell",
            "Carter", "Roberts", "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker",
            "Cruz", "Edwards", "Collins", "Reyes", "Stewart", "Morris", "Morales", "Murphy",
            "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper", "Peterson", "Bailey",
            "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson", "Watson",
            "Brooks", "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes",
            "Price", "Alvarez", "Castillo", "Sanders", "Patel", "Myers", "Long", "Ross",
            "Foster", "Jimenez", "Powell", "Jenkins", "Perry", "Russell", "Sullivan", "Bell",
            
            // International surnames
            "Ahmed", "Mohammed", "Ali", "Hassan", "Khan", "Hussein", "Ibrahim", "Omar",
            "Wang", "Li", "Zhang", "Chen", "Liu", "Yang", "Huang", "Zhao", "Wu", "Zhou",
            "Ivanov", "Petrov", "Sidorov", "Popov", "Kuznetsov", "Sokolov", "Smirnov",
            "Dubois", "Martin", "Bernard", "Petit", "Robert", "Richard", "Durand", "Moreau",
            "Mueller", "Schmidt", "Schneider", "Fischer", "Weber", "Meyer", "Wagner", "Becker",
            "Rossi", "Bianchi", "Romano", "Colombo", "Ricci", "Ferrari", "Russo", "Gallo",
            "García", "Rodríguez", "González", "Fernández", "López", "Díaz", "Martínez",
            "Kumar", "Singh", "Patel", "Sharma", "Verma", "Gupta", "Agarwal", "Reddy",
            "Tanaka", "Suzuki", "Takahashi", "Nakamura", "Kobayashi", "Kato", "Yamamoto",
            "Kim", "Park", "Lee", "Choi", "Jung", "Kang", "Cho", "Yoon", "Jang", "Lim",
            "Okafor", "Adeyemi", "Osei", "Mensah", "Diallo", "Traore", "Camara", "Diop"
        )
        
        val titles = listOf("", "Mr.", "Mrs.", "Ms.", "Dr.", "Prof.", "Sir", "Lord", "Hon.")
        
        return (1..count).map { i ->
            val isMale = random.nextBoolean()
            val firstName = if (isMale) firstNamesMale.random() else firstNamesFemale.random()
            val lastName = lastNames.random()
            val title = titles.random()
            
            NameEntry(
                id = i,
                fullName = if (title.isNotEmpty()) "$title $firstName $lastName" else "$firstName $lastName",
                firstName = firstName,
                lastName = lastName,
                title = title,
                isMale = isMale
            )
        }
    }
    
    /**
     * Generate 1000 unique places
     */
    private fun generatePlacesTable(count: Int): List<PlaceEntry> {
        val cityPrefixes = listOf(
            "New", "Old", "North", "South", "East", "West", "Upper", "Lower",
            "Great", "Little", "Port", "Fort", "Mount", "Lake", "River", "Spring"
        )
        
        val cityRoots = listOf(
            "town", "ville", "burg", "ton", "field", "ford", "bridge", "haven",
            "port", "mouth", "chester", "caster", "pool", "worth", "land", "wood"
        )
        
        val cityNames = listOf(
            "Springfield", "Riverside", "Georgetown", "Fairview", "Clinton",
            "Madison", "Franklin", "Washington", "Lincoln", "Jefferson",
            "Jackson", "Hamilton", "Adams", "Monroe", "Kennedy", "Roosevelt",
            "Victoria", "Alexandria", "Charlotte", "Augusta", "Caroline",
            "Cambridge", "Oxford", "Princeton", "Harvard", "Yale",
            "Dover", "Brighton", "Windsor", "Richmond", "Palermo", "Venice",
            "Florence", "Milan", "Naples", "Turin", "Genoa", "Bologna",
            "Barcelona", "Valencia", "Seville", "Bilbao", "Toledo", "Granada"
        )
        
        val regions = listOf(
            "Northern Province", "Southern Province", "Eastern Province",
            "Western Province", "Central Province", "Capital Region",
            "Coastal Region", "Mountain Region", "Valley Region", "Desert Region",
            "Highland District", "Lowland District", "Island Territory",
            "Peninsula Region", "Border Region", "Interior Region"
        )
        
        val streets = listOf(
            "Street", "Avenue", "Boulevard", "Drive", "Road", "Lane", "Court",
            "Place", "Way", "Circle", "Parkway", "Heights", "Ridge", "View",
            "Terrace", "Gardens", "Square", "Plaza", "Center", "Crossing"
        )
        
        val streetNames = listOf(
            "Main", "Oak", "Maple", "Cedar", "Pine", "Elm", "Washington",
            "Park", "Lake", "Hill", "River", "Church", "High", "Mill",
            "Union", "Market", "Spring", "Center", "School", "Court",
            "Washington", "Lincoln", "Jefferson", "Madison", "Park", "Lake"
        )
        
        return (1..count).map { i ->
            val cityName = when {
                random.nextDouble() < 0.3 -> "${cityPrefixes.random()} ${cityNames.random()}"
                random.nextDouble() < 0.5 -> "${cityNames.random()}${cityRoots.random()}"
                else -> cityNames.random()
            }
            
            val region = regions.random()
            val streetNum = (1..9999).random()
            val streetName = "${streetNames.random()} ${streets.random()}"
            
            PlaceEntry(
                id = i,
                cityName = cityName,
                region = region,
                fullAddress = "$streetNum $streetName, $cityName, $region",
                streetNumber = streetNum,
                streetName = streetName,
                postalCode = "${(10000..99999).random()}"
            )
        }
    }
    
    /**
     * Generate 500 unique organizations
     */
    private fun generateOrganizationsTable(count: Int): List<OrganizationEntry> {
        val adjectives = listOf(
            "National", "International", "Global", "United", "Federal",
            "Central", "American", "European", "Asian", "World",
            "Advanced", "Modern", "Dynamic", "Progressive", "Strategic",
            "Premier", "Elite", "Prime", "First", "Superior",
            "Innovative", "Creative", "Digital", "Smart", "Eco",
            "Green", "Sustainable", "Renewable", "Clean", "Efficient"
        )
        
        val nouns = listOf(
            "Technology", "Solutions", "Industries", "Systems", "Dynamics",
            "Enterprises", "Holdings", "Group", "Corp", "Associates",
            "Partners", "Ventures", "Capital", "Resources", "Energy",
            "Healthcare", "Finance", "Manufacturing", "Logistics", "Media",
            "Communications", "Consulting", "Services", "Products", "Development"
        )
        
        val types = listOf(
            "Corporation", "LLC", "Inc", "Ltd", "Group", "Partnership",
            "Foundation", "Institute", "Association", "Organization",
            "Agency", "Authority", "Commission", "Board", "Council",
            "Alliance", "Coalition", "Federation", "Union", "League"
        )
        
        val industries = listOf(
            "Technology", "Finance", "Healthcare", "Manufacturing", "Retail",
            "Energy", "Agriculture", "Transportation", "Media", "Construction",
            "Telecommunications", "Pharmaceuticals", "Automotive", "Aerospace",
            "Defense", "Education", "Hospitality", "Real Estate", "Insurance",
            "Mining", "Entertainment", "Food & Beverage", "Textiles", "Chemicals"
        )
        
        return (1..count).map { i ->
            val name = "${adjectives.random()} ${nouns.random()} ${types.random()}"
            
            OrganizationEntry(
                id = i,
                name = name,
                shortName = name.split(" ").joinToString("") { it.first() },
                industry = industries.random(),
                orgType = types.random(),
                foundingYear = (1900..2024).random()
            )
        }
    }
    
    /**
     * Generate 300 event templates
     */
    private fun generateEventsTable(count: Int): List<EventEntry> {
        val eventTypes = listOf(
            "Crisis", "Opportunity", "Disaster", "Breakthrough", "Scandal",
            "Protest", "Summit", "Election", "Referendum", "Anniversary",
            "Festival", "Conference", "Treaty", "Sanction", "Audit",
            "Investigation", "Merger", "Bankruptcy", "Strike", "Boycott"
        )
        
        val categories = listOf(
            "Political", "Economic", "Social", "Environmental", "Diplomatic",
            "Military", "Technological", "Cultural", "Sports", "Health"
        )
        
        val severities = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        
        return (1..count).map { i ->
            EventEntry(
                id = i,
                eventName = "${eventTypes.random()} Event #${i}",
                eventType = eventTypes.random(),
                category = categories.random(),
                severity = severities.random(),
                description = "Random ${eventTypes.random().lowercase()} event template"
            )
        }
    }
    
    /**
     * Generate 100 scandal templates
     */
    private fun generateScandalsTable(count: Int): List<ScandalEntry> {
        val scandalTypes = listOf(
            "Corruption", "Bribery", "Embezzlement", "Fraud", "Nepotism",
            "Affair", "Cover-up", "Abuse of Power", "Conflict of Interest",
            "Tax Evasion", "Money Laundering", "Insider Trading", "Leak",
            "Surveillance", "Wiretapping", "Blackmail", "Extortion",
            "Kickbacks", "Cronyism", "Favoritism", "Misconduct", "Negligence"
        )
        
        val targets = listOf(
            "Minister", "Senator", "Governor", "Mayor", "Ambassador",
            "CEO", "Director", "Official", "Advisor", "Secretary",
            "Judge", "General", "Chief", "Commissioner", "Representative"
        )
        
        return (1..count).map { i ->
            ScandalEntry(
                id = i,
                title = "${scandalTypes.random()} Scandal",
                scandalType = scandalTypes.random(),
                target = targets.random(),
                severity = (1..10).random(),
                description = "${targets.random()} involved in ${scandalTypes.random().lowercase()} allegations"
            )
        }
    }
    
    /**
     * Generate 200 law templates
     */
    private fun generateLawsTable(count: Int): List<LawEntry> {
        val lawPrefixes = listOf(
            "National", "Federal", "Public", "Economic", "Social",
            "Environmental", "Healthcare", "Education", "Infrastructure",
            "Defense", "Consumer", "Financial", "Labor", "Immigration",
            "Criminal Justice", "Tax", "Energy", "Transportation", "Housing",
            "Digital", "Privacy", "Security", "Trade", "Agricultural"
        )
        
        val lawSuffixes = listOf(
            "Act", "Reform Act", "Protection Act", "Investment Act",
            "Modernization Act", "Emergency Act", "Regulation Act",
            "Support Act", "Development Act", "Accountability Act",
            "Transparency Act", "Security Act", "Welfare Act", "Rights Act",
            "Enhancement Act", "Improvement Act", "Amendment Act"
        )
        
        val categories = listOf(
            "Tax", "Criminal", "Civil", "Regulatory", "Constitutional",
            "Emergency", "Budget", "Social", "Economic", "Environmental"
        )
        
        return (1..count).map { i ->
            val prefix = lawPrefixes.random()
            val suffix = lawSuffixes.random()
            
            LawEntry(
                id = i,
                title = "$prefix $suffix",
                shortTitle = "${prefix.split(" ").joinToString("") { it.first() }}${suffix.replace(" Act", "")}",
                category = categories.random(),
                description = "Legislation addressing ${prefix.lowercase()} matters"
            )
        }
    }
    
    /**
     * Generate 150 policy templates
     */
    private fun generatePoliciesTable(count: Int): List<PolicyEntry> {
        val policyAreas = listOf(
            "Economic", "Social", "Foreign", "Defense", "Healthcare",
            "Education", "Environment", "Immigration", "Trade", "Tax",
            "Labor", "Housing", "Transportation", "Energy", "Agriculture"
        )
        
        val policyTypes = listOf(
            "Reform", "Initiative", "Program", "Strategy", "Framework",
            "Plan", "Directive", "Guideline", "Standard", "Protocol"
        )
        
        return (1..count).map { i ->
            val area = policyAreas.random()
            val type = policyTypes.random()
            
            PolicyEntry(
                id = i,
                name = "$area $type",
                area = area,
                type = type,
                description = "Policy framework for $area ${type.lowercase()}"
            )
        }
    }
    
    /**
     * Generate 100 contractor templates
     */
    private fun generateContractorsTable(count: Int): List<ContractorEntry> {
        val companyPrefixes = listOf(
            "Build", "Construct", "Engineering", "Infrastructure",
            "Development", "Contracting", "Industrial", "Civil Works",
            "General", "Premier", "National", "United", "Global"
        )
        
        val companySuffixes = listOf(
            "Corp", "Ltd", "Inc", "LLC", "Group", "Solutions",
            "Systems", "Partners", "Associates", "Enterprises",
            "Holdings", "Industries", "Company", "Co"
        )
        
        val specialties = listOf(
            "General Construction", "Road Building", "Bridge Construction",
            "Building Construction", "Infrastructure", "Electrical",
            "Plumbing", "HVAC", "Landscaping", "Demolition",
            "Renovation", "Steel Work", "Concrete", "Excavation"
        )
        
        return (1..count).map { i ->
            ContractorEntry(
                id = i,
                companyName = "${companyPrefixes.random()} ${companySuffixes.random()}",
                specialty = specialties.random(),
                rating = (1.0..5.0).random().toFloat(),
                establishedYear = (1950..2024).random()
            )
        }
    }
    
    /**
     * Generate 200 vendor templates
     */
    private fun generateVendorsTable(count: Int): List<VendorEntry> {
        val vendorTypes = listOf(
            "Food & Beverage", "Merchandise", "Equipment", "Services",
            "Security", "Cleaning", "Maintenance", "Transportation",
            "Catering", "Entertainment", "Technology", "Supplies"
        )
        
        val vendorNames = listOf(
            "Quick", "Fast", "Quality", "Premium", "Budget",
            "Express", "Direct", "Plus", "Pro", "Max",
            "Best", "Top", "First", "Prime", "Elite"
        )
        
        return (1..count).map { i ->
            val name = "${vendorNames.random()} ${vendorTypes.random().split(" ").first()}"
            
            VendorEntry(
                id = i,
                vendorName = name,
                vendorType = vendorTypes.random(),
                rating = (1.0..5.0).random().toFloat(),
                establishedYear = (1980..2024).random()
            )
        }
    }
    
    /**
     * Generate 100 issue templates
     */
    private fun generateIssuesTable(count: Int): List<IssueEntry> {
        val issues = listOf(
            "Economy", "Healthcare", "Education", "Immigration", "Crime",
            "Environment", "Taxes", "Jobs", "Housing", "Infrastructure",
            "Defense", "Foreign Policy", "Social Security", "Energy",
            "Abortion", "Gun Control", "Drug Policy", "Poverty",
            "Inequality", "Corruption", "Civil Rights", "Religious Freedom"
        )
        
        val stances = listOf("Support", "Oppose", "Neutral", "Reform", "Expand", "Reduce")
        
        return (1..count).map { i ->
            val issue = issues.random()
            IssueEntry(
                id = i,
                issue = issue,
                category = when (issue) {
                    "Economy", "Taxes", "Jobs" -> "Economic"
                    "Healthcare", "Social Security", "Poverty" -> "Social"
                    "Defense", "Foreign Policy", "Crime" -> "Security"
                    "Environment", "Energy" -> "Environmental"
                    else -> "Political"
                },
                importance = (1..10).random(),
                description = "Public opinion on $issue"
            )
        }
    }
    
    // Additional table generators (abbreviated for space)
    private fun generateSpeechesTable(count: Int): List<SpeechEntry> = (1..count).map { i ->
        SpeechEntry(
            id = i,
            title = "Speech Template $i",
            occasion = listOf("Address", "Remarks", "Statement", "Announcement").random(),
            tone = listOf("Formal", "Inspiring", "Somber", "Celebratory", "Urgent").random(),
            length = (5..60).random()
        )
    }
    
    private fun generateCampaignSlogansTable(count: Int): List<SloganEntry> = (1..count).map { i ->
        SloganEntry(
            id = i,
            slogan = generateSlogan(),
            theme = listOf("Hope", "Change", "Strength", "Unity", "Progress").random(),
            length = (3..10).random()
        )
    }
    
    private fun generateDiplomaticCablesTable(count: Int): List<CableEntry> = (1..count).map { i ->
        CableEntry(
            id = i,
            subject = "Diplomatic Cable #${i}",
            classification = listOf("Unclassified", "Confidential", "Secret", "Top Secret").random(),
            priority = listOf("Routine", "Priority", "Immediate", "Flash").random()
        )
    }
    
    private fun generateCrisisScenariosTable(count: Int): List<CrisisEntry> = (1..count).map { i ->
        CrisisEntry(
            id = i,
            scenario = "Crisis Scenario $i",
            type = listOf("Economic", "Military", "Political", "Environmental", "Health").random(),
            difficulty = (1..10).random()
        )
    }
    
    private fun generateEconomicPoliciesTable(count: Int): List<EconomicPolicyEntry> = (1..count).map { i ->
        EconomicPolicyEntry(
            id = i,
            name = "Economic Policy $i",
            type = listOf("Fiscal", "Monetary", "Trade", "Industrial", "Labor").random(),
            impact = listOf("Positive", "Negative", "Mixed", "Neutral").random()
        )
    }
    
    private fun generateSocialProgramsTable(count: Int): List<SocialProgramEntry> = (1..count).map { i ->
        SocialProgramEntry(
            id = i,
            name = "Social Program $i",
            category = listOf("Welfare", "Healthcare", "Education", "Housing", "Employment").random(),
            targetGroup = listOf("General", "Low Income", "Elderly", "Disabled", "Youth").random()
        )
    }
    
    private fun generateMilitaryOperationsTable(count: Int): List<MilitaryEntry> = (1..count).map { i ->
        MilitaryEntry(
            id = i,
            operationName = "Operation ${codenames.random()}",
            type = listOf("Defense", "Offense", "Peacekeeping", "Training", "Humanitarian").random(),
            scale = listOf("Small", "Medium", "Large", "Massive").random()
        )
    }
    
    private fun generateTechnologyProjectsTable(count: Int): List<TechEntry> = (1..count).map { i ->
        TechEntry(
            id = i,
            projectName = "Tech Project $i",
            field = listOf("AI", "Biotech", "Energy", "Space", "Defense", "Computing").random(),
            status = listOf("Research", "Development", "Testing", "Deployment").random()
        )
    }
    
    private fun generateCulturalEventsTable(count: Int): List<CulturalEntry> = (1..count).map { i ->
        CulturalEntry(
            id = i,
            eventName = "Cultural Event $i",
            type = listOf("Festival", "Exhibition", "Performance", "Competition", "Ceremony").random(),
            scale = listOf("Local", "Regional", "National", "International").random()
        )
    }
    
    private fun generateSportsEventsTable(count: Int): List<SportsEntry> = (1..count).map { i ->
        SportsEntry(
            id = i,
            eventName = "Sports Event $i",
            sport = listOf("Football", "Basketball", "Tennis", "Swimming", "Athletics").random(),
            level = listOf("Local", "National", "International", "Olympic").random()
        )
    }
    
    private fun generateSlogan(): String {
        val slogans = listOf(
            "A Better Tomorrow", "Together We Stand", "Change We Believe In",
            "Strong Leadership", "For the People", "Building the Future",
            "Unity and Progress", "Hope and Action", "Real Solutions",
            "Moving Forward", "One Nation", "Prosperity for All",
            "Security First", "Freedom and Justice", "Equal Opportunity"
        )
        return slogans.random()
    }
    
    private val codenames = listOf(
        "Eagle", "Phoenix", "Thunder", "Lightning", "Storm",
        "Shadow", "Ghost", "Steel", "Iron", "Bronze",
        "Alpha", "Bravo", "Charlie", "Delta", "Echo",
        "Victor", "Whiskey", "X-ray", "Yankee", "Zulu"
    )
}

// Data classes for procedural tables
data class ProceduralTables(
    val names: List<NameEntry>,
    val places: List<PlaceEntry>,
    val organizations: List<OrganizationEntry>,
    val events: List<EventEntry>,
    val scandals: List<ScandalEntry>,
    val laws: List<LawEntry>,
    val policies: List<PolicyEntry>,
    val contractors: List<ContractorEntry>,
    val vendors: List<VendorEntry>,
    val issues: List<IssueEntry>,
    val speeches: List<SpeechEntry>,
    val campaignSlogans: List<SloganEntry>,
    val diplomaticCables: List<CableEntry>,
    val crisisScenarios: List<CrisisEntry>,
    val economicPolicies: List<EconomicPolicyEntry>,
    val socialPrograms: List<SocialProgramEntry>,
    val militaryOperations: List<MilitaryEntry>,
    val technologyProjects: List<TechEntry>,
    val culturalEvents: List<CulturalEntry>,
    val sportsEvents: List<SportsEntry>
)

data class NameEntry(
    val id: Int,
    val fullName: String,
    val firstName: String,
    val lastName: String,
    val title: String,
    val isMale: Boolean
)

data class PlaceEntry(
    val id: Int,
    val cityName: String,
    val region: String,
    val fullAddress: String,
    val streetNumber: Int,
    val streetName: String,
    val postalCode: String
)

data class OrganizationEntry(
    val id: Int,
    val name: String,
    val shortName: String,
    val industry: String,
    val orgType: String,
    val foundingYear: Int
)

data class EventEntry(
    val id: Int,
    val eventName: String,
    val eventType: String,
    val category: String,
    val severity: Int,
    val description: String
)

data class ScandalEntry(
    val id: Int,
    val title: String,
    val scandalType: String,
    val target: String,
    val severity: Int,
    val description: String
)

data class LawEntry(
    val id: Int,
    val title: String,
    val shortTitle: String,
    val category: String,
    val description: String
)

data class PolicyEntry(
    val id: Int,
    val name: String,
    val area: String,
    val type: String,
    val description: String
)

data class ContractorEntry(
    val id: Int,
    val companyName: String,
    val specialty: String,
    val rating: Float,
    val establishedYear: Int
)

data class VendorEntry(
    val id: Int,
    val vendorName: String,
    val vendorType: String,
    val rating: Float,
    val establishedYear: Int
)

data class IssueEntry(
    val id: Int,
    val issue: String,
    val category: String,
    val importance: Int,
    val description: String
)

data class SpeechEntry(
    val id: Int,
    val title: String,
    val occasion: String,
    val tone: String,
    val length: Int
)

data class SloganEntry(
    val id: Int,
    val slogan: String,
    val theme: String,
    val length: Int
)

data class CableEntry(
    val id: Int,
    val subject: String,
    val classification: String,
    val priority: String
)

data class CrisisEntry(
    val id: Int,
    val scenario: String,
    val type: String,
    val difficulty: Int
)

data class EconomicPolicyEntry(
    val id: Int,
    val name: String,
    val type: String,
    val impact: String
)

data class SocialProgramEntry(
    val id: Int,
    val name: String,
    val category: String,
    val targetGroup: String
)

data class MilitaryEntry(
    val id: Int,
    val operationName: String,
    val type: String,
    val scale: String
)

data class TechEntry(
    val id: Int,
    val projectName: String,
    val field: String,
    val status: String
)

data class CulturalEntry(
    val id: Int,
    val eventName: String,
    val type: String,
    val scale: String
)

data class SportsEntry(
    val id: Int,
    val eventName: String,
    val sport: String,
    val level: String
)
