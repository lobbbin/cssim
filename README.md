# 🎮 Country Simulator

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-purple.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Compose-2023.10.01-blue.svg)](https://developer.android.com/jetpack/compose)
[![Room](https://img.shields.io/badge/Room-2.6.1-orange.svg)](https://developer.android.com/training/data-storage/room)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## A Full Text-Based Country Simulator for Android

Manage **everything** from bus schedules in single neighborhoods to nuclear diplomacy. Every decision matters through the Butterfly Effect Engine.

> **"From hot dog vendors at stadiums to avoiding nuclear standoff"**

---

## 🎯 Features

### 10 Core Gameplay Systems

| System | Description |
|--------|-------------|
| 🗳️ **Elections & Politics** | Campaign speeches, rallies, scandals, opinion polls, branching (Executive/Opposition mode) |
| 💰 **Finances, Oil & Economy** | Budget, taxes, oil prices, trade deals, business licenses, audits |
| ⚖️ **Lawmaking & Courts** | Bills, amendments, committee voting, judges, juries, sentencing |
| 🏛️ **Ministries & Appointees** | 7 key appointees, purchase orders, expense reports, department management |
| 🏗️ **Infrastructure** | Power grid, bus schedules per neighborhood, traffic, construction |
| 🌍 **Diplomacy & Alliances** | Spies, embassies, trade blocs, alliances, wars, sanctions |
| 👥 **Demographics & Health** | Immigration, medicine stockpiles, welfare, disease outbreaks |
| 🏈 **Sports & Culture** | 23-player squads, stadium vendors, doping scandals, Olympics |
| 🎲 **Procedural Generation** | 10,000+ templates, infinite replayability |
| 🦋 **Butterfly Effect Engine** | Macro→Micro cascades, delayed consequences, chain reactions |

### 4 Dynamic AI Systems

- **Dynamic NPC System** - 500+ NPCs with personalities, memories, goals that act daily
- **Dynamic Economy System** - 10 sectors, 15 commodities, stock market, business lifecycle
- **Dynamic World System** - 15+ AI countries with personalities, goals, alliances
- **Interactive Decision Engine** - 8 decision types with immediate & delayed consequences

---

## 📊 Stats

| Metric | Count |
|--------|-------|
| **Lines of Code** | ~40,000+ |
| **Kotlin Files** | 80+ |
| **Data Entities** | 95+ |
| **DAOs** | 50+ |
| **Game Engines** | 11 |
| **UI Screens** | 20+ |
| **Procedural Templates** | 10,000+ |
| **Playable Micro-Tasks** | 100+ |
| **Macro Decisions** | 10 |

---

## 🚀 Quick Start

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 21
- Android SDK 34
- Minimum Android 7.0 (API 24)

### Build & Run

```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/CountrySimulator.git
cd CountrySimulator

# Open in Android Studio
# Sync Gradle files
# Build → Make Project
# Run on device/emulator
```

### First Launch
- World generation: 2-5 seconds
- 500+ NPCs generated
- 200+ businesses
- 15+ foreign countries
- 4-8 initial micro-tasks

---

## 🎮 Gameplay

### Interactive Decisions

Every macro decision generates 2-14 micro-tasks:

```
SANCTION COUNTRY →
├── Review business permits
├── Update customs regulations
├── Adjust tax brackets
├── Launch audits
├── Coordinate with allies
└── Review economic impact
```

### Dynamic NPCs

500+ NPCs act daily based on personality:

```kotlin
NPC: Teacher, Approval 35%, High Neuroticism
↓
Action: PROTEST (1,000 people)
↓
Consequences: -8 approval, -5 stability
↓
Follow-up: News event, protest tasks
```

### Living Economy

```kotlin
Oil Price Spike
↓
Transport sector health -15
↓
Inflation +1.5%
↓
Consumer spending -5%
↓
Retail growth -3%
↓
Unemployment +0.5%
```

### AI World

15+ countries with AI personalities:

```kotlin
Country: Aggression 0.8, Relations 25
↓
Action: MILITARY_THREAT
↓
Player Response: Protest (-15 relations)
↓
Escalation: CYBER_ATTACK (-30 relations)
↓
Crisis: Security event generated
```

---

## 🏗️ Architecture

### Tech Stack
- **Language**: Kotlin 1.9.20, Java 21
- **UI**: Jetpack Compose + Material 3
- **Database**: Room 2.6.1
- **Pagination**: Paging 3 3.2.1
- **Async**: Coroutines + Flow
- **Background**: WorkManager

### Architecture Pattern
```
MVVM (Model-View-ViewModel)
├── Model: 95+ data entities
├── View: 20+ Compose screens
├── ViewModel: GameViewModel with state flows
└── Repository: GameRepository + PagingRepository
```

### Project Structure
```
app/src/main/java/com/country/simulator/
├── model/           # 12 files, 95+ entities
├── database/        # 3 files, 50+ DAOs
├── repositories/    # 2 files
├── generation/      # 5 files, 10,000+ templates
├── engine/          # 11 game engines
├── utils/           # Game constants, helpers
├── ui/
│   ├── screens/    # 20+ screens
│   ├── components/ # Paginated lists
│   ├── navigation/ # NavGraph
│   └── theme/      # Material 3
└── viewmodel/      # GameViewModel
```

---

## 📝 Documentation

| File | Description |
|------|-------------|
| [README.md](README.md) | This file - project overview |
| [SETUP_GUIDE.md](SETUP_GUIDE.md) | Build instructions, troubleshooting |
| [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) | Technical architecture details |
| [FEATURE_CHECKLIST.md](FEATURE_CHECKLIST.md) | 100% feature verification |
| [DYNAMIC_GAMEPLAY_SUMMARY.md](DYNAMIC_GAMEPLAY_SUMMARY.md) | Dynamic systems documentation |
| [GAMEPLAY_SUMMARY.md](GAMEPLAY_SUMMARY.md) | Interactive gameplay systems |

---

## 🎯 Key Features Deep Dive

### Butterfly Effect Engine
```kotlin
Macro Decision: Sanction Country
↓
Micro Tasks Generated: 6
├── Business permits (approve/deny)
├── Customs regulations (rewrite)
├── Tax brackets (adjust)
├── Audits (launch)
├── Diplomatic coordination
└── Economic impact review
↓
Butterfly Effects: 2-3 delayed consequences
↓
Chain Reactions: Up to 3 levels deep
```

### Opposition Mode (Lost Election)
```kotlin
Gameplay Shifts To:
├── Filibuster bills (duration selection)
├── Committee work (amend/delay/kill)
├── Coalition building (lobby MPs)
├── No-confidence votes (flip government)
├── Question Time (challenge ministers)
└── Private Member's Bills
```

### Procedural Generation
```
Names:       5,000+ combinations
Cities:      2,000+ unique names
Orgs:        1,000+ companies
Events:      500+ templates
Scandals:    200+ types
Crises:      100+ scenarios
Laws:        300+ templates
Speeches:    100+ types
Occupations: 100+ jobs
Total:       10,000+ templates
```

---

## 🤝 Contributing

Contributions welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Areas for Contribution
- [ ] Unit tests for game engines
- [ ] UI tests for screens
- [ ] Additional event templates
- [ ] More procedural content
- [ ] Localization (i18n)
- [ ] Accessibility improvements
- [ ] Performance optimizations

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

Built with:
- [Android Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Material Design 3](https://m3.material.io/)

---

## 📱 Screenshots

*Coming soon - add screenshots of:*
- Home dashboard
- Event resolution dialog
- Cabinet meeting screen
- Economy screen
- Diplomacy screen
- NPC management (paginated)
- Bus schedule management
- Medicine stockpiles

---

## 🚧 Roadmap

### v1.0 (Current)
- ✅ All 10 core systems
- ✅ 4 dynamic AI systems
- ✅ Complete UI (20+ screens)
- ✅ Procedural generation (10,000+ templates)
- ✅ Butterfly Effect Engine

### v1.1 (Planned)
- [ ] Unit tests
- [ ] UI tests
- [ ] Tutorial system
- [ ] Additional event types (50+)

### v1.2 (Planned)
- [ ] Cloud saves (Firebase)
- [ ] Achievements (Google Play Games)
- [ ] Localization (5+ languages)
- [ ] Accessibility features

### v2.0 (Future)
- [ ] Multiplayer (asynchronous diplomacy)
- [ ] Mod support
- [ ] Scenario editor
- [ ] Expanded military system

---

## 📬 Contact

- **Repository**: [github.com/YOUR_USERNAME/CountrySimulator](https://github.com/YOUR_USERNAME/CountrySimulator)
- **Issues**: [Report bugs or request features](https://github.com/YOUR_USERNAME/CountrySimulator/issues)

---

<div align="center">

**Built with ❤️ for Android**

[⬆ Back to top](#-country-simulator)

</div>
