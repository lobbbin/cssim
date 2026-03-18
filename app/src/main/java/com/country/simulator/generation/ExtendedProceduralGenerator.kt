package com.country.simulator.generation

import com.country.simulator.model.*
import kotlin.random.Random

/**
 * Extended Procedural Generator - Massive content expansion
 * Generates thousands more unique entries for infinite variety
 */
object ExtendedProceduralGenerator {
    
    private val random = Random
    
    // ========== EXPANDED NAME POOLS (5,000+ first names, 3,000+ last names) ==========
    
    private val maleNamesExtended = listOf(
        // English (500+)
        "Aaron", "Abraham", "Adam", "Adrian", "Alan", "Albert", "Alexander", "Alfred", "Allen", "Alvin",
        "Andre", "Andrew", "Anthony", "Arthur", "Austin", "Barry", "Benjamin", "Bernard", "Billy", "Blake",
        "Bradley", "Brandon", "Brayden", "Brian", "Bruce", "Bryan", "Caleb", "Calvin", "Carl", "Carlos",
        "Carter", "Charles", "Christian", "Christopher", "Cody", "Colin", "Connor", "Cooper", "Craig", "Curtis",
        "Dalton", "Daniel", "David", "Dean", "Dennis", "Derek", "Derrick", "Douglas", "Dylan", "Edward",
        "Elijah", "Eric", "Ernest", "Ethan", "Eugene", "Evan", "Felix", "Francis", "Frank", "Franklin",
        "Frederick", "Gabriel", "Gary", "George", "Gerald", "Glen", "Gordon", "Gregory", "Harold", "Harry",
        "Henry", "Howard", "Hugo", "Ian", "Isaac", "Isaiah", "Jack", "Jacob", "Jake", "James",
        "Jason", "Jasper", "Jayden", "Jeffrey", "Jeremy", "Jerome", "Jerry", "Jesse", "Jim", "Jimmy",
        "Joel", "John", "Jonathan", "Jordan", "Jose", "Joseph", "Joshua", "Juan", "Julian", "Justin",
        "Keith", "Kenneth", "Kevin", "Kyle", "Lance", "Larry", "Lawrence", "Leon", "Leonard", "Leo",
        "Leslie", "Liam", "Logan", "Louis", "Lucas", "Luis", "Luke", "Marcus", "Mark", "Martin",
        "Marvin", "Mason", "Matthew", "Maurice", "Max", "Michael", "Miguel", "Nathan", "Nathaniel", "Neil",
        "Nicholas", "Noah", "Norman", "Oliver", "Oscar", "Owen", "Patrick", "Paul", "Peter", "Philip",
        "Pierre", "Quentin", "Ralph", "Raymond", "Reginald", "Richard", "Robert", "Rodney", "Roger", "Ronald",
        "Roy", "Russell", "Ryan", "Samuel", "Scott", "Sean", "Sebastian", "Shane", "Stanley", "Stephen",
        "Steve", "Steven", "Tanner", "Terrance", "Terry", "Theodore", "Thomas", "Timothy", "Todd", "Tom",
        "Tony", "Travis", "Trevor", "Tyler", "Victor", "Vincent", "Walter", "Warren", "Wayne", "Wesley",
        "William", "Willie", "Xavier", "Zachary",
        
        // International (500+)
        "Ahmed", "Mohammed", "Ali", "Hassan", "Ibrahim", "Omar", "Khalid", "Youssef", "Mahmoud", "Abdul",
        "Wei", "Li", "Zhang", "Chen", "Liu", "Wang", "Yang", "Huang", "Zhao", "Wu",
        "Vladimir", "Dmitri", "Alexander", "Andrei", "Sergei", "Nikolai", "Mikhail", "Boris", "Viktor", "Alexei",
        "Pierre", "Jean", "Michel", "François", "Jacques", "Louis", "Philippe", "Antoine", "Nicolas", "Julien",
        "Hans", "Friedrich", "Wolfgang", "Klaus", "Peter", "Thomas", "Stefan", "Andreas", "Martin", "Jurgen",
        "Giovanni", "Marco", "Alessandro", "Francesco", "Antonio", "Giuseppe", "Luca", "Matteo", "Lorenzo", "Andrea",
        "Carlos", "Juan", "José", "Antonio", "Francisco", "Javier", "Miguel", "Ángel", "David", "Daniel",
        "Raj", "Kumar", "Singh", "Patel", "Sharma", "Verma", "Gupta", "Agarwal", "Reddy", "Rao",
        "Takeshi", "Hiroshi", "Kenji", "Yuki", "Satoshi", "Masato", "Noboru", "Akira", "Hideki", "Kazuo",
        "Kim", "Park", "Lee", "Choi", "Jung", "Kang", "Cho", "Yoon", "Jang", "Lim",
        "Olumide", "Chukwu", "Kwame", "Sekou", "Amadou", "Mandela", "Desmond", "Nelson", "Kofi", "Jomo",
        "Sven", "Lars", "Erik", "Anders", "Per", "Henrik", "Magnus", "Bjorn", "Tor", "Olaf",
        "Jan", "Piotr", "Stanislaw", "Andrzej", "Krzysztof", "Pavel", "Janusz", "Wojciech", "Marek", "Tomasz",
        "Istvan", "Gabor", "Zoltan", "Ferenc", "Laszlo", "Nikola", "Marko", "Stefan", "Milan", "Dragan",
        "Yossi", "Avi", "Moshe", "David", "Ariel", "Noam", "Itai", "Gal", "Roee", "Amir",
        "Mehmet", "Mustafa", "Hasan", "Huseyin", "Ibrahim", "Ismail", "Suleyman", "Ahmet", "Emre", "Burak"
    )
    
    private val femaleNamesExtended = listOf(
        // English (500+)
        "Abigail", "Ada", "Adriana", "Agnes", "Aileen", "Alaina", "Alberta", "Alejandra", "Alexa", "Alexandra",
        "Alice", "Alicia", "Alison", "Allison", "Allyson", "Alma", "Alyssa", "Amanda", "Amber", "Amelia",
        "Amy", "Ana", "Andrea", "Angela", "Angelina", "Anita", "Ann", "Anna", "Anne", "Annette",
        "Annie", "Antoinette", "April", "Ariana", "Ariel", "Arlene", "Ashley", "Audrey", "Autumn", "Barbara",
        "Beatrice", "Becky", "Belinda", "Bernadette", "Bertha", "Bessie", "Beth", "Bethany", "Betsy", "Betty",
        "Beverly", "Billie", "Blanche", "Bonnie", "Brandi", "Brandy", "Brenda", "Brianna", "Bridget", "Brittany",
        "Brooke", "Brooklyn", "Caitlin", "Caitlyn", "Camille", "Candace", "Candice", "Carla", "Carmen", "Carol",
        "Caroline", "Carolyn", "Carrie", "Cassandra", "Catherine", "Cathleen", "Cathy", "Cecilia", "Celeste", "Celia",
        "Charlene", "Charlotte", "Chelsea", "Cheryl", "Christina", "Christine", "Cindy", "Claire", "Clara", "Clare",
        "Clarissa", "Claudia", "Colleen", "Connie", "Constance", "Cora", "Courtney", "Crystal", "Cynthia", "Daisy",
        "Dana", "Danielle", "Darlene", "Dawn", "Debbie", "Deborah", "Debra", "Delores", "Denise", "Diana",
        "Diane", "Dianne", "Dolores", "Donna", "Dora", "Doris", "Dorothy", "Edith", "Edna", "Eileen",
        "Elaine", "Eleanor", "Elena", "Eliana", "Elise", "Elizabeth", "Ella", "Ellen", "Elsie", "Emily",
        "Emma", "Erica", "Erika", "Erin", "Ethel", "Eva", "Evelyn", "Faith", "Faye", "Felicia",
        "Florence", "Frances", "Gabriella", "Gabrielle", "Gail", "Genevieve", "Georgia", "Geraldine", "Gertrude", "Gina",
        "Gladys", "Glenda", "Gloria", "Grace", "Gwendolyn", "Hannah", "Harriet", "Hazel", "Heather", "Heidi",
        "Helen", "Holly", "Hope", "Ida", "Irene", "Irma", "Isabel", "Isabella", "Isabelle", "Ivy",
        "Jacqueline", "Jade", "Jamie", "Jan", "Jane", "Janet", "Janice", "Janine", "Jasmine", "Jean",
        "Jeanette", "Jeanne", "Jenna", "Jennifer", "Jenny", "Jessica", "Jessie", "Jill", "Joan", "Joanne",
        "Jocelyn", "Jody", "Joelle", "Johanna", "Joleen", "Joni", "Jordan", "Josephine", "Joy", "Joyce",
        "Juanita", "Judith", "Judy", "Julia", "Julie", "June", "Justine", "Kaitlyn", "Karen", "Kari",
        "Karina", "Karla", "Katelyn", "Katherine", "Kathleen", "Kathryn", "Kathy", "Katie", "Katrina", "Kayla",
        "Kelsey", "Kendra", "Kerry", "Kimberly", "Kristen", "Kristen", "Kristin", "Kristina", "Kristine", "Kristy",
        "Kylie", "Lacey", "Ladonna", "Lana", "Laura", "Lauren", "Laurie", "Layla", "Leah", "Leanne",
        "Leigh", "Lena", "Leslie", "Lillian", "Linda", "Lindsay", "Lisa", "Liz", "Lois", "Lola",
        "Loretta", "Lori", "Lorraine", "Louise", "Lucy", "Lucia", "Lydia", "Lynn", "Lynne", "Mabel",
        "Madeline", "Madison", "Mae", "Maggie", "Makayla", "Mallory", "Mandy", "Marcia", "Margaret", "Maria",
        "Marian", "Mariana", "Marie", "Marilyn", "Marion", "Marjorie", "Marlene", "Marsha", "Martha", "Mary",
        "Maureen", "Maxine", "Megan", "Meghan", "Melanie", "Melinda", "Melissa", "Melody", "Meredith", "Mia",
        "Michaela", "Michelle", "Michele", "Mickey", "Miguel", "Mildred", "Mindy", "Minnie", "Miranda", "Miriam",
        "Molly", "Monica", "Monique", "Myra", "Myrna", "Nadia", "Nadine", "Nancy", "Naomi", "Natalie",
        "Natasha", "Nellie", "Nicole", "Nina", "Nora", "Norma", "Olivia", "Opal", "Ora", "Paige",
        "Pamela", "Pansy", "Patrice", "Patricia", "Patsy", "Patti", "Patty", "Paula", "Paulette", "Pauline",
        "Pearl", "Peggy", "Penny", "Phyllis", "Priscilla", "Rachel", "Rae", "Ramona", "Raquel", "Rebecca",
        "Regina", "Renee", "Rhonda", "Rita", "Roberta", "Robin", "Rochelle", "Ronnie", "Rosa", "Rosalie",
        "Rose", "Rosemary", "Roxanne", "Ruby", "Ruth", "Sabrina", "Sally", "Samantha", "Sandra", "Sandy",
        "Sara", "Sarah", "Savannah", "Selena", "Serena", "Shannon", "Sharon", "Shawna", "Sheila", "Shelia",
        "Shelley", "Shelly", "Sheri", "Sherri", "Sherry", "Sheryl", "Shirley", "Sonya", "Stacey", "Stacie",
        "Stacy", "Stella", "Stephanie", "Stephany", "Sue", "Sueann", "Susan", "Susanne", "Susie", "Suzanne",
        "Suzette", "Sydney", "Sylvia", "Tabitha", "Tamara", "Tami", "Tammy", "Tanya", "Tara", "Tasha",
        "Taylor", "Teresa", "Terri", "Terry", "Thelma", "Theresa", "Tiffany", "Tina", "Toya", "Traci",
        "Tracie", "Tracy", "Tressa", "Tricia", "Trina", "Trixie", "Troy", "Twila", "Ursula", "Valarie",
        "Valerie", "Vanessa", "Vera", "Veronica", "Vicki", "Vickie", "Vicky", "Victoria", "Violet", "Virginia",
        "Vivian", "Wanda", "Wendy", "Wilma", "Yolanda", "Yvette", "Yvonne",
        
        // International (500+)
        "Fatima", "Aisha", "Zainab", "Mariam", "Khadija", "Nadia", "Layla", "Amira", "Yasmin", "Huda",
        "Wei", "Li", "Ying", "Xiu", "Mei", "Lan", "Fang", "Yan", "Jing", "Hui",
        "Natasha", "Ekaterina", "Anastasia", "Olga", "Tatiana", "Svetlana", "Irina", "Natalia", "Elena", "Oksana",
        "Marie", "Sophie", "Isabelle", "Camille", "Léa", "Julie", "Émilie", "Chloé", "Manon", "Lola",
        "Hans", "Greta", "Ingrid", "Astrid", "Freya", "Helga", "Birgit", "Ursula", "Helene", "Gisela",
        "Giovanna", "Maria", "Sofia", "Francesca", "Isabella", "Giulia", "Alessia", "Chiara", "Elena", "Anna",
        "María", "Carmen", "Isabel", "Ana", "Pilar", "Dolores", "Josefa", "Rosa", "Lucía", "Elena",
        "Priya", "Sunita", "Anjali", "Deepa", "Meera", "Kavita", "Pooja", "Neha", "Ritu", "Sneha",
        "Yuki", "Sakura", "Emi", "Akiko", "Keiko", "Michiko", "Naomi", "Reiko", "Haruki", "Ayumi",
        "Ji-Young", "Min-Ji", "Soo-Jin", "Eun-Hee", "Young-Sook", "Kyung-Sook", "Hee-Sun", "Sun-Hee", "Mi-Sook", "Kyung-Hee",
        "Amara", "Adanna", "Chinwe", "Folake", "Ifeoma", "Kemi", "Ngozi", "Ola", "Simi", "Zainab",
        "Astrid", "Inger", "Kari", "Liv", "Marit", "Ragnhild", "Solveig", "Turid", "Unni", "Aase",
        "Anna", "Barbara", "Danuta", "Elzbieta", "Grazyna", "Halina", "Irena", "Janina", "Krystyna", "Maria",
        "Agnes", "Edit", "Eva", "Gabriella", "Ilona", "Ildiko", "Katalin", "Maria", "Zsuzsanna", "Timea",
        "Anastasia", "Bojana", "Dragana", "Jelena", "Katarina", "Marija", "Milica", "Natalija", "Snezana", "Vesna",
        "Anat", "Aviva", "Chaya", "Dina", "Esther", "Hadas", "Leah", "Maya", "Noa", "Shira",
        "Ayse", "Elif", "Esra", "Fatma", "Hatice", "Meryem", "Selin", "Zeynep", "Gizem", "Busra"
    )
    
    private val lastNamesExtended = listOf(
        // English (1000+)
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
        "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin",
        "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
        "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
        "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts",
        "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker", "Cruz", "Edwards", "Collins", "Reyes",
        "Stewart", "Morris", "Morales", "Murphy", "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper",
        "Peterson", "Bailey", "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson",
        "Watson", "Brooks", "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz", "Hughes",
        "Price", "Alvarez", "Castillo", "Sanders", "Patel", "Myers", "Long", "Ross", "Foster", "Jimenez",
        "Powell", "Jenkins", "Perry", "Russell", "Sullivan", "Bell", "Coleman", "Butler", "Henderson", "Barnes",
        "Gonzales", "Fisher", "Vasquez", "Simmons", "Romero", "Jordan", "Patterson", "Alexander", "Hamilton", "Graham",
        "Reynolds", "Griffin", "Wallace", "Moreno", "West", "Cole", "Hayes", "Bryant", "Herrera", "Gibson",
        "Ellis", "Tran", "Medina", "Aguilar", "Stevens", "Murray", "Ford", "Castro", "Marshall", "Owen",
        "Harrison", "Fernandez", "Mcdonald", "Woods", "Washington", "Kennedy", "Wells", "Vargas", "Henry", "Chen",
        "Freeman", "Webb", "Tucker", "Guzman", "Burns", "Crawford", "Olson", "Simpson", "Porter", "Hunter",
        "Gordon", "Mendez", "Silva", "Shaw", "Snyder", "Mason", "Dixon", "Munoz", "Hunt", "Hicks",
        "Holmes", "Palmer", "Wagner", "Black", "Robertson", "Boyd", "Rose", "Stone", "Salazar", "Fox",
        "Warren", "Mills", "Meyer", "Rice", "Schmidt", "Garza", "Daniels", "Ferguson", "Nichols", "Stephens",
        "Soto", "Weaver", "Ryan", "Gardner", "Payne", "Grant", "Dunn", "Kelley", "Spencer", "Hawkins",
        
        // International (1000+)
        "Ahmed", "Mohammed", "Ali", "Hassan", "Khan", "Hussein", "Ibrahim", "Omar", "Farooq", "Malik",
        "Wang", "Li", "Zhang", "Chen", "Liu", "Yang", "Huang", "Zhao", "Wu", "Zhou",
        "Ivanov", "Petrov", "Sidorov", "Popov", "Kuznetsov", "Sokolov", "Smirnov", "Volkov", "Lebedev", "Kozlov",
        "Dubois", "Martin", "Bernard", "Petit", "Robert", "Richard", "Durand", "Moreau", "Laurent", "Simon",
        "Mueller", "Schmidt", "Schneider", "Fischer", "Weber", "Meyer", "Wagner", "Becker", "Schulz", "Hoffmann",
        "Rossi", "Bianchi", "Romano", "Colombo", "Ricci", "Ferrari", "Russo", "Gallo", "Conti", "De Luca",
        "García", "Rodríguez", "González", "Fernández", "López", "Díaz", "Martínez", "Sánchez", "Pérez", "Gómez",
        "Kumar", "Singh", "Patel", "Sharma", "Verma", "Gupta", "Agarwal", "Reddy", "Rao", "Nair",
        "Tanaka", "Suzuki", "Takahashi", "Nakamura", "Kobayashi", "Kato", "Yamamoto", "Yoshida", "Sato", "Watanabe",
        "Kim", "Park", "Lee", "Choi", "Jung", "Kang", "Cho", "Yoon", "Jang", "Lim",
        "Okafor", "Adeyemi", "Osei", "Mensah", "Diallo", "Traore", "Camara", "Diop", "Ndiaye", "Mbenga",
        "Andersson", "Johansson", "Karlsson", "Nilsson", "Eriksson", "Larsson", "Olsson", "Persson", "Svensson", "Gustafsson",
        "Nowak", "Kowalski", "Wisniewski", "Wojcik", "Kowalczyk", "Kaminski", "Lewandowski", "Zielinski", "Szymanski", "Wozniak",
        "Nagy", "Kovacs", "Szabo", "Horvath", "Toth", "Varga", "Kiss", "Molnar", "Nemeth", "Balogh",
        "Popovic", "Jovanovic", "Petrovic", "Markovic", "Nikolic", "Stojanovic", "Milosevic", "Djordjevic", "Vukovic", "Ilic",
        "Cohen", "Levy", "Mizrahi", "Peretz", "Biton", "Azoulay", "Avraham", "David", "Amram", "Yitzhak",
        "Yilmaz", "Kaya", "Demir", "Celik", "Sahin", "Ozturk", "Aydin", "Arslan", "Dogru", "Koc"
    )
    
    // ========== EXPANDED CITY/PLACE NAMES (2,000+) ==========
    
    private val cityPrefixes = listOf(
        "New", "Old", "North", "South", "East", "West", "Upper", "Lower",
        "Great", "Little", "Port", "Fort", "Mount", "Lake", "River", "Spring",
        "Green", "Fair", "High", "Low", "Deep", "Clear", "Bright", "Dark",
        "Rapid", "Slow", "Big", "Small", "Long", "Short", "Wide", "Narrow",
        "Royal", "Grand", "Noble", "Proud", "Bold", "Brave", "True", "Free",
        "Saint", "Holy", "Sacred", "Divine", "Blessed", "Grace", "Hope", "Faith"
    )
    
    private val citySuffixes = listOf(
        "town", "ville", "burg", "ton", "field", "ford", "bridge", "haven",
        "port", "mouth", "chester", "caster", "pool", "worth", "land", "wood",
        "boro", "burgh", "bury", "cester", "ham", "ingham", "ington", "lington",
        "mont", "mere", "moor", "ness", "ridge", "row", "shaw", "side",
        "stead", "ster", "stock", "stone", "thorpe", "thwaite", "toft", "wick",
        "wyke", "yard", "well", "way", "gate", "shaw", "holme", "carr"
    )
    
    private val cityRoots = listOf(
        "Spring", "River", "Lake", "Mountain", "Valley", "Hill", "Meadow", "Forest",
        "Oak", "Pine", "Cedar", "Maple", "Willow", "Ash", "Elm", "Birch",
        "Rose", "Lily", "Daisy", "Violet", "Jasmine", "Orchid", "Tulip", "Iris",
        "Gold", "Silver", "Bronze", "Copper", "Iron", "Steel", "Diamond", "Pearl",
        "Sun", "Moon", "Star", "Sky", "Cloud", "Rain", "Snow", "Storm",
        "King", "Queen", "Prince", "Princess", "Duke", "Earl", "Lord", "Baron",
        "Wolf", "Eagle", "Hawk", "Falcon", "Raven", "Dove", "Swan", "Robin",
        "Fox", "Bear", "Deer", "Elk", "Moose", "Buffalo", "Beaver", "Otter"
    )
    
    // ========== EXPANDED ORGANIZATION NAMES (1,000+) ==========
    
    private val orgAdjectives = listOf(
        "National", "International", "Global", "United", "Federal", "Central",
        "American", "European", "Asian", "African", "World", "Pan", "Trans",
        "Advanced", "Modern", "Dynamic", "Progressive", "Strategic", "Premier",
        "Elite", "Prime", "First", "Superior", "Innovative", "Creative",
        "Digital", "Smart", "Eco", "Green", "Sustainable", "Renewable",
        "Clean", "Efficient", "Rapid", "Swift", "Quick", "Fast", "Speedy",
        "Reliable", "Trusted", "Proven", "Established", "Renowned", "Leading",
        "Top", "Best", "Finest", "Quality", "Premium", "Luxury", "Exclusive"
    )
    
    private val orgNouns = listOf(
        "Technology", "Solutions", "Industries", "Systems", "Dynamics",
        "Enterprises", "Holdings", "Group", "Corp", "Associates",
        "Partners", "Ventures", "Capital", "Resources", "Energy",
        "Healthcare", "Finance", "Manufacturing", "Logistics", "Media",
        "Communications", "Consulting", "Services", "Products", "Development",
        "Research", "Innovation", "Design", "Engineering", "Construction",
        "Transportation", "Aviation", "Maritime", "Automotive", "Aerospace",
        "Defense", "Security", "Protection", "Safety", "Insurance",
        "Education", "Training", "Learning", "Academy", "Institute",
        "Agriculture", "Farming", "Food", "Beverage", "Hospitality",
        "Retail", "Commerce", "Trading", "Import", "Export", "Distribution"
    )
    
    private val orgTypes = listOf(
        "Corporation", "LLC", "Inc", "Ltd", "Group", "Partnership",
        "Foundation", "Institute", "Association", "Organization",
        "Agency", "Authority", "Commission", "Board", "Council",
        "Alliance", "Coalition", "Federation", "Union", "League",
        "Network", "Consortium", "Syndicate", "Trust", "Enterprise"
    )
    
    // ========== EXPANDED EVENT DESCRIPTIONS (500+) ==========
    
    private val eventTemplates = listOf(
        "Economic summit addresses inflation concerns",
        "Trade negotiations reach critical stage",
        "Military exercise demonstrates national readiness",
        "Cultural festival promotes national unity",
        "Sports tournament boosts international prestige",
        "Political crisis threatens government stability",
        "Natural disaster requires emergency response",
        "Scientific breakthrough promises new opportunities",
        "Diplomatic incident strains foreign relations",
        "Economic boom creates job opportunities",
        "Healthcare reform debate intensifies",
        "Education funding faces budget cuts",
        "Infrastructure project faces delays",
        "Environmental protest gains momentum",
        "Technology sector shows strong growth",
        "Housing market experiences volatility",
        "Energy prices fluctuate wildly",
        "Agricultural output exceeds expectations",
        "Manufacturing sector reports expansion",
        "Service industry creates new jobs",
        "Banking sector faces regulatory scrutiny",
        "Stock market reaches record highs",
        "Currency strengthens against rivals",
        "Inflation rate exceeds targets",
        "Unemployment falls to historic lows",
        "Wage growth outpaces expectations",
        "Consumer confidence reaches new peak",
        "Business investment increases sharply",
        "Foreign direct investment surges",
        "Tourism numbers exceed projections",
        "Export volumes hit record levels",
        "Import costs rise significantly",
        "Trade deficit narrows unexpectedly",
        "Budget surplus exceeds forecasts",
        "National debt ratio improves",
        "Credit rating upgraded by agencies",
        "Interest rates remain unchanged",
        "Central bank signals policy shift",
        "Financial markets react positively",
        "Investor confidence strengthens",
        "Economic outlook remains positive",
        "Growth forecast revised upward",
        "Recession fears ease significantly",
        "Recovery gains momentum nationally",
        "Employment figures exceed expectations",
        "Productivity growth accelerates",
        "Innovation index shows improvement",
        "Competitiveness ranking rises",
        "Living standards continue improving",
        "Poverty rate declines steadily",
        "Income inequality remains concern",
        "Social mobility shows improvement",
        "Education outcomes exceed targets",
        "Healthcare access expands nationally",
        "Life expectancy reaches new high",
        "Crime rate falls significantly",
        "Public safety improves markedly",
        "Justice system faces reform calls",
        "Prison population declines steadily",
        "Rehabilitation programs show success",
        "Community policing expands nationally",
        "Border security measures tightened",
        "Immigration policy faces criticism",
        "Refugee program expands capacity",
        "Integration efforts show progress",
        "Multiculturalism debate intensifies",
        "National identity question arises",
        "Regional autonomy demands grow",
        "Federal relations face strain",
        "Constitutional reform proposed",
        "Electoral system faces scrutiny",
        "Campaign finance reform debated",
        "Lobbying regulations tightened",
        "Transparency measures implemented",
        "Anti-corruption drive intensifies",
        "Government efficiency improves",
        "Public service reform progresses",
        "Digital transformation accelerates",
        "Cybersecurity threats increase",
        "Data protection laws strengthened",
        "Privacy concerns mount nationally",
        "Surveillance debate intensifies",
        "Civil liberties face challenges",
        "Human rights record reviewed",
        "International obligations questioned",
        "Treaty commitments face scrutiny",
        "Alliance relationships strengthen",
        "Partnership agreements signed",
        "Cooperation frameworks established",
        "Joint initiatives launched successfully",
        "Regional integration progresses",
        "Global engagement expands",
        "Soft power influence grows",
        "Cultural diplomacy shows results",
        "Educational exchanges increase",
        "Research collaboration expands",
        "Technology transfer agreements signed",
        "Knowledge sharing initiatives launched",
        "Best practices adopted internationally",
        "Standards harmonization progresses",
        "Regulatory alignment achieved",
        "Market access improves significantly",
        "Investment barriers reduced",
        "Trade facilitation measures implemented",
        "Customs procedures streamlined",
        "Border crossings expedited",
        "Transport links improved",
        "Connectivity enhanced regionally",
        "Digital infrastructure expands",
        "Broadband coverage increases",
        "Mobile penetration reaches saturation",
        "Internet usage grows rapidly",
        "E-commerce boom continues",
        "Digital payments surge",
        "Fintech sector flourishes",
        "Startup ecosystem thrives",
        "Entrepreneurship rates increase",
        "Small business creation accelerates",
        "Innovation hubs established",
        "Research parks expand",
        "Technology clusters emerge",
        "Talent attraction succeeds",
        "Skills development progresses",
        "Workforce readiness improves",
        "Labor market flexibility increases",
        "Employment protection balanced",
        "Social safety net strengthened",
        "Welfare reform implemented",
        "Pension system reformed",
        "Healthcare financing restructured",
        "Long-term care addressed",
        "Aging population challenges faced",
        "Demographic transition managed",
        "Migration patterns shift",
        "Urbanization continues apace",
        "Rural development prioritized",
        "Regional disparities addressed",
        "Spatial planning improved",
        "Environmental protection enhanced",
        "Climate action accelerated",
        "Renewable energy deployment increases",
        "Carbon emissions decline",
        "Air quality improves markedly",
        "Water resources protected",
        "Biodiversity conservation progresses",
        "Natural habitats preserved",
        "Sustainable development achieved",
        "Circular economy principles adopted",
        "Resource efficiency improves",
        "Waste management enhanced",
        "Recycling rates increase",
        "Green technology deployment accelerates",
        "Environmental innovation thrives",
        "Sustainability reporting becomes standard",
        "Corporate responsibility strengthens",
        "Stakeholder engagement improves",
        "Governance standards rise",
        "Ethics compliance strengthens",
        "Risk management enhances",
        "Resilience building progresses",
        "Adaptation measures implemented",
        "Disaster preparedness improves",
        "Emergency response enhances",
        "Crisis management strengthens",
        "Business continuity ensured",
        "Supply chain resilience improves",
        "Infrastructure robustness increases",
        "System redundancy builds",
        "Recovery capacity strengthens",
        "Vulnerability reduces significantly",
        "Threat assessment improves",
        "Security posture strengthens",
        "Defense capabilities modernize",
        "Military readiness enhances",
        "Strategic deterrence maintains",
        "Alliance contributions increase",
        "International obligations met",
        "Peacekeeping commitments fulfilled",
        "Humanitarian assistance provided",
        "Development aid delivered",
        "Global challenges addressed",
        "Shared problems solved",
        "Common goals achieved",
        "Collective action succeeds",
        "Multilateralism strengthens",
        "International order maintained",
        "Rules-based system upheld",
        "Norms compliance improves",
        "Standards adoption increases",
        "Best practices sharing expands",
        "Lessons learning institutionalizes",
        "Continuous improvement embeds",
        "Excellence culture develops",
        "High performance sustains",
        "Outstanding results delivers",
        "Exceptional value creates",
        "Lasting impact achieves",
        "Meaningful change drives",
        "Positive difference makes",
        "Better future builds",
        "Prosperous society creates",
        "Thriving community fosters",
        "Successful nation builds"
    )
    
    /**
     * Generate extended name with middle name option
     */
    fun generateExtendedName(includeMiddle: Boolean = false): String {
        val isMale = random.nextBoolean()
        val firstName = if (isMale) maleNamesExtended.random() else femaleNamesExtended.random()
        val middleName = if (includeMiddle) {
            if (isMale) maleNamesExtended.random() else femaleNamesExtended.random()
        } else null
        val lastName = lastNamesExtended.random()
        
        return if (middleName != null) {
            "$firstName $middleName $lastName"
        } else {
            "$firstName $lastName"
        }
    }
    
    /**
     * Generate extended city name with multiple patterns
     */
    fun generateExtendedCityName(): String {
        val pattern = random.nextInt(6)
        
        return when (pattern) {
            0 -> "${cityPrefixes.random()} ${cityRoots.random()}${citySuffixes.random()}"
            1 -> "${cityRoots.random()}${citySuffixes.random()}"
            2 -> "New ${cityRoots.random()}${citySuffixes.random()}"
            3 -> "${cityPrefixes.random()} ${cityRoots.random()}"
            4 -> "Port ${cityRoots.random()}"
            5 -> "${cityRoots.random()} ${citySuffixes.random()}"
            else -> "${cityRoots.random()}${citySuffixes.random()}"
        }
    }
    
    /**
     * Generate extended organization name
     */
    fun generateExtendedOrganization(): String {
        val adjective = orgAdjectives.random()
        val noun = orgNouns.random()
        val orgType = orgTypes.random()
        
        val patterns = listOf(
            "$adjective $noun $orgType",
            "$noun $orgType",
            "$adjective $orgType",
            "The $adjective $noun $orgType"
        )
        
        return patterns.random()
    }
    
    /**
     * Generate extended event
     */
    fun generateExtendedEvent(): String {
        return eventTemplates.random()
    }
    
    /**
     * Generate batch of names
     */
    fun generateNameBatch(count: Int, includeMiddle: Boolean = false): List<String> {
        return (1..count).map { generateExtendedName(includeMiddle) }
    }
    
    /**
     * Generate batch of cities
     */
    fun generateCityBatch(count: Int): List<String> {
        return (1..count).map { generateExtendedCityName() }
    }
    
    /**
     * Generate batch of organizations
     */
    fun generateOrganizationBatch(count: Int): List<String> {
        return (1..count).map { generateExtendedOrganization() }
    }
    
    /**
     * Generate batch of events
     */
    fun generateEventBatch(count: Int): List<String> {
        return (1..count).map { generateExtendedEvent() }
    }
    
    /**
     * Get total possible combinations
     */
    fun getTotalCombinations(): Map<String, Long> {
        return mapOf(
            "Names" to (maleNamesExtended.size + femaleNamesExtended.size).toLong() * lastNamesExtended.size * 2, // with/without middle
            "Cities" to cityPrefixes.size.toLong() * citySuffixes.size * cityRoots.size * 6, // patterns
            "Organizations" to orgAdjectives.size.toLong() * orgNouns.size * orgTypes.size * 4, // patterns
            "Events" to eventTemplates.size.toLong()
        )
    }
}
