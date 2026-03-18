# Country Simulator - Setup & Quick Start Guide

## Prerequisites

### Required Software
- **Android Studio**: Hedgehog (2023.1.1) or newer
- **JDK**: Version 21 (bundled with Android Studio)
- **Android SDK**: API Level 34 (Android 14)
- **Minimum Android Version**: Android 7.0 (API 24)

### System Requirements
- **OS**: Windows 10/11, macOS 12+, or Linux
- **RAM**: 8GB minimum (16GB recommended)
- **Storage**: 5GB free space
- **Screen Resolution**: 1920x1080 minimum

## Project Setup

### Step 1: Clone/Download Project
```
CountrySimulator/
├── app/
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

### Step 2: Open in Android Studio
1. Launch Android Studio
2. Click "Open" and navigate to the `CountrySimulator` folder
3. Wait for Gradle sync to complete (may take 2-5 minutes first time)

### Step 3: Verify Dependencies
The project uses these key libraries:
- **Kotlin**: 1.9.20
- **Jetpack Compose**: 2023.10.01 BOM
- **Room Database**: 2.6.1
- **Paging 3**: 3.2.1
- **Material 3**: Latest

### Step 4: Build the Project
```
Build → Make Project (Ctrl+F9 / Cmd+F9)
```

### Step 5: Run on Device/Emulator
1. Connect Android device (USB debugging enabled) OR
2. Create AVD (Android Virtual Device) with:
   - Device: Pixel 6 or similar
   - System Image: Android 14 (API 34)
   - RAM: 2048MB minimum
3. Click "Run" (Shift+F10 / Ctrl+R)

## Project Architecture

### Package Structure
```
com.country.simulator/
├── model/                    # Data classes (90+ entities)
├── database/                 # Room database & DAOs
├── repositories/             # Data access layer
├── generation/               # Procedural content generators
├── engine/                   # Game logic engines
├── ui/                       # Jetpack Compose UI
│   ├── screens/             # Full screens
│   ├── components/          # Reusable components
│   └── theme/               # Material theme
└── viewmodel/               # ViewModels (MVVM)
```

### Key Components

#### 1. Database Layer (Room)
- **GameDatabase.kt**: Main database (90+ tables)
- **DAOs.kt**: Basic data access objects
- **PagingDAOs.kt**: Paging 3 enabled DAOs for large lists

#### 2. Generation Layer
- **ProceduralGenerator.kt**: Basic random content
- **DatabasePopulator.kt**: 500-1000 entries per category
- **ContentTablesGenerator.kt**: 2000+ name/place templates
- **EventGenerator.kt**: Random events & crises
- **GameWorldInitializer.kt**: Complete world setup

#### 3. Game Engines
- **ButterflyEffectEngine.kt**: Macro-micro cascades
- **ElectionEngine.kt**: Election simulation
- **GameStateManager.kt**: Game state & progression

#### 4. UI Layer (Compose)
- **HomeScreen.kt**: Main dashboard
- **TasksScreen.kt**: Micro-task management
- **EconomyScreen.kt**: Economic dashboard
- **PoliticsScreen.kt**: Politics & elections
- **ManagementScreens.kt**: Paginated lists (1000+ items)

## First Run Experience

### New Game Initialization
When you first run the app:

1. **World Generation** (2-5 seconds)
   - Creates player country
   - Generates 500+ NPCs
   - Creates 200+ businesses
   - Sets up 15+ foreign countries
   - Initializes ministries & appointees

2. **Starting Position**
   - Approval Rating: 50%
   - Stability: 75%
   - Treasury: $1-5 billion
   - Active Tasks: 4-8 micro tasks

3. **First Actions**
   - Check notifications
   - Complete introductory tasks
   - Review cabinet appointments
   - Address the nation (optional)

## Gameplay Systems

### 1. Task System (Micro/Macro)

**Macro Decisions** (Big choices):
- Elections
- Trade deals
- War declarations
- Budget approvals

**Micro Tasks** (Daily actions):
- Approve permits
- Review reports
- Make diplomatic calls
- Sign documents

Tasks cascade: Macro → generates → Micro

### 2. Election System

**Campaign Phase**:
- Give speeches (+approval)
- Hold rallies (+followers)
- Run ads (costs money)
- Respond to scandals

**Election Day**:
- Win → Executive Mode
- Loss → Opposition Mode (new gameplay)

### 3. Economy System

**Manage**:
- National budget (12 ministries)
- Tax brackets (6 income levels)
- Oil production & pricing
- Trade deals
- Business licenses

**Watch**:
- Inflation rate
- Unemployment
- GDP growth
- Trade balance

### 4. NPC System (1000+ characters)

**Important NPCs**:
- Cabinet members (7 appointees)
- Opposition leaders
- Business tycoons
- Media moguls

**Regular NPCs** (paginated):
- Workers, teachers, doctors
- Each with memories & opinions
- Approval tracked individually

### 5. Diplomacy System

**Relations** (-100 to +100):
- Allies (+50 to +100)
- Friendly (+20 to +49)
- Neutral (-20 to +19)
- Hostile (-100 to -20)

**Actions**:
- Assign diplomats
- Sign alliances
- Impose sanctions
- Declare war

## Performance Optimization

### Paging 3 Implementation
For lists with 1000+ items:
- Loads 20 items at a time
- Prefetches 10 ahead
- Max cache: 100 items
- Prevents memory issues

### Background Processing
- Butterfly effects calculated asynchronously
- Event generation runs in background
- Database operations use coroutines

### Memory Management
- Lazy loading for all large lists
- Flow-based data streams
- Automatic cleanup on navigation

## Debugging & Testing

### Enable Debug Mode
Add to `local.properties`:
```
debug.mode=true
```

### View Database
Location: `/data/data/com.country.simulator/databases/country_simulator_database`

Use Android Studio's **Database Inspector**:
1. Run app
2. View → Tool Windows → App Inspection
3. Select Database Inspector
4. Browse tables in real-time

### Common Issues

**Gradle Sync Failed**:
```
File → Invalidate Caches → Invalidate and Restart
```

**Database Migration Error**:
```
Uninstall app → Clean Project → Rebuild → Run
```

**Out of Memory**:
- Reduce emulator RAM allocation
- Close other apps
- Check for memory leaks in profiler

## Customization

### Adjust Procedural Generation
Edit `ContentTablesGenerator.kt`:
```kotlin
// Increase name pool
private val firstNamesMale = listOf(...) // Add more names

// Increase event variety
private fun generateEventsTable(count: Int) {
    // Add new event types
}
```

### Modify Game Balance
Edit `GameWorldInitializer.kt`:
```kotlin
// Starting treasury
treasuryBalance = (500_000_000..5_000_000_000).random()

// Starting approval
approvalRating = 50.0
```

### Add New Event Types
Edit `EventGenerator.kt`:
```kotlin
private fun generateNewEventType(countryId: Long): GameEvent {
    return GameEvent(
        countryId = countryId,
        eventType = GameEventType.CUSTOM,
        title = "New Event",
        // ...
    )
}
```

## Save System

### Current Implementation
- Room database persistence
- Auto-save on every action
- Single save slot per installation

### Future Enhancements
- Multiple save slots
- Cloud save (Firebase)
- Save import/export

## Building Release APK

### 1. Generate Keystore
```bash
keytool -genkey -v -keystore country-simulator.keystore -alias country-simulator -keyalg RSA -keysize 2048 -validity 10000
```

### 2. Add to `gradle.properties`
```properties
RELEASE_STORE_FILE=../country-simulator.keystore
RELEASE_STORE_PASSWORD=your_password
RELEASE_KEY_ALIAS=country-simulator
RELEASE_KEY_PASSWORD=your_password
```

### 3. Update `build.gradle.kts`
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("RELEASE_STORE_FILE") ?: "debug.keystore")
            storePassword = System.getenv("RELEASE_STORE_PASSWORD") ?: ""
            keyAlias = System.getenv("RELEASE_KEY_ALIAS") ?: "androiddebugkey"
            keyPassword = System.getenv("RELEASE_KEY_PASSWORD") ?: ""
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")
        }
    }
}
```

### 4. Build Release APK
```
Build → Generate Signed Bundle / APK → APK → Release → Finish
```

## Next Steps

### Recommended Enhancements
1. **Tutorial System**: Onboarding for new players
2. **Achievements**: Google Play Games integration
3. **Analytics**: Track player decisions
4. **Localization**: Multi-language support
5. **Accessibility**: TalkBack, large text support

### Content Expansion
1. More event types (50+)
2. Additional micro tasks (100+)
3. Expanded diplomacy options
4. Detailed military system
5. Climate change mechanics

### Technical Improvements
1. Unit tests for engines
2. UI tests for screens
3. Performance profiling
4. Battery optimization
5. Network features (cloud saves)

## Support & Documentation

### Code Documentation
- All Kotlin files have KDoc comments
- README.md: Project overview
- IMPLEMENTATION_SUMMARY.md: Technical details

### External Resources
- [Room Database Docs](https://developer.android.com/training/data-storage/room)
- [Paging 3 Guide](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)

## License

This project is created for educational and entertainment purposes.

---

**Ready to Play?**

1. Open project in Android Studio
2. Click Run
3. Start your presidency!

Good luck, Mr. President! 🎮
