# 🎮 DYNAMIC GAMEPLAY SYSTEMS - LIVING WORLD

## ✅ NO STATIC SHIT - EVERYTHING ALIVE & REACTIVE

### 📊 Dynamic Systems Created (4 New Engines, ~2,000 LOC)

| System | Purpose | Dynamic Elements |
|--------|---------|------------------|
| **DynamicNPCSystem.kt** | NPCs with AI, memories, goals | 500+ NPCs acting daily |
| **DynamicEconomySystem.kt** | Living economy simulation | 10 sectors, 15 commodities, stock market |
| **DynamicWorldSystem.kt** | AI countries with goals | 15+ countries reacting |
| **InteractiveDecisionEngine.kt** | Player choices with consequences | 8 decision types |

**Total: ~2,000 lines of dynamic gameplay code**

---

## 🧠 DYNAMIC NPC SYSTEM

### NPCs That Remember, React, and Initiate

**Personality Matrix (5 Traits):**
```kotlin
- Openness: Traditional ↔ Innovative
- Conscientiousness: Careless ↔ Organized  
- Extraversion: Introverted ↔ Outgoing
- Agreeableness: Critical ↔ Compassionate
- Neuroticism: Stable ↔ Reactive
```

**NPC Goals (8 Types):**
```kotlin
WEALTH, POWER, JUSTICE, FREEDOM, 
SECURITY, EQUALITY, TRADITION, INNOVATION
```

**Daily NPC Actions (30% act each day):**
```kotlin
PROTEST          // Low approval + high neuroticism
ORGANIZE_GROUP   // Low approval + high conscientiousness
DONATE_TO_CAMPAIGN // High approval + rich + extraverted
START_BUSINESS   // Wealth goal + opportunity
ACTIVISM         // Justice goal + high agreeableness
RUN_FOR_OFFICE   // Power goal + high extraversion
LOBBY_GOVERNMENT // Business owners
WRITE_ARTICLE    // Journalists
RESEARCH_BREAKTHROUGH // Scientists
VOICE_OPINION    // Default
```

**Memory System:**
- NPCs remember government actions
- Memories decay over 365-730 days
- Relationships tracked (trust, respect, obligation)
- Interaction history stored

**Opinion Updates:**
```kotlin
// NPC opinion changes based on:
- Government performance on their issues
- Personality (neurotic = stronger reactions)
- Economic conditions (growth, unemployment)
- Social indicators (health, stability, environment)
```

**Example: Dynamic NPC Reaction**
```kotlin
// NPC: John Smith, Teacher, Approval: 35%
// Personality: High neuroticism, Justice goal
// Daily action: PROTEST (1000 people)
// Consequences:
- Approval -8, Stability -5
- News event generated
- Follow-up task: "Handle Protest Backlash"
- Memory added: "Government ignored education funding"
```

---

## 📈 DYNAMIC ECONOMY SYSTEM

### Living Economy with Supply/Demand

**10 Market Sectors:**
```kotlin
AGRICULTURE, MANUFACTURING, TECHNOLOGY, FINANCE, 
HEALTHCARE, RETAIL, ENERGY, CONSTRUCTION, 
TRANSPORTATION, HOSPITALITY
```

Each sector tracks:
- Health (0-100)
- Growth rate (-10 to 10)
- Employment
- Average wage
- Number of companies
- Demand (0-100)
- Supply (0-100)
- Inventory (days)

**15 Commodity Markets:**
```kotlin
OIL, NATURAL_GAS, GOLD, SILVER, COPPER, 
WHEAT, CORN, SOYBEANS, COFFEE, SUGAR, 
COTTON, LUMBER, IRON_ORE, COAL, URANIUM
```

Price dynamics:
- Random daily fluctuation (volatility-based)
- Inventory pressure (low stock = price up)
- Mean reversion (returns to average)
- 30-day price history

**Stock Market Simulation:**
- S&P 500 index
- P/E ratio
- Dividend yield
- Volume tracking
- Circuit breakers for extreme moves

**Business Lifecycle:**
- Birth rate (based on growth)
- Failure rate (based on compliance, size)
- Growth rate (inverse to size)
- Employment changes

**Daily Simulation:**
```kotlin
suspend fun simulateDay(countryId: Long) {
    updateMarketSectors(countryId)      // Supply/demand
    updateCommodityPrices(countryId)    // Market dynamics
    updateStockMarket(countryId)        // Index movements
    calculateInflation(countryId)       // Demand-pull, cost-push
    updateEmployment(countryId)         // Job creation/loss
    processBusinessLifecycle(countryId) // Births/deaths
    generateEconomicEvents(countryId)   // Crises, booms
}
```

**Economic Events (Dynamic):**
```kotlin
Banking Crisis    // 0.1% daily chance
Currency Crash    // 0.2% daily chance
Trade Boom        // When growth > 3%
Tech Unicorn      // 2% daily chance
Market Rally      // When index > 3%
Market Selloff    // When index < -3%
High Inflation    // When inflation > 5%
Job Growth        // When unemployment change > 0.3%
```

**Player Policy Effects:**
```kotlin
STIMULUS:
  +2% growth (intensity-based)
  +1.5% inflation
  
AUSTERITY:
  -1.5% growth
  -1% inflation
  
DEREGULATION:
  +1% growth
  -5 stability (risk)
  
TAX_CUTS:
  +1.5% growth
  +10B deficit
```

---

## 🌍 DYNAMIC WORLD SYSTEM

### AI Countries with Goals & Personalities

**Country AI Personality (5 Traits):**
```kotlin
Aggression:     Peaceful ↔ Aggressive (0-1)
Cooperation:    Isolationist ↔ Cooperative (0-1)
Reliability:    Unreliable ↔ Trustworthy (0-1)
Ambition:       Status Quo ↔ Expansionist (0-1)
Leader Type:    Diplomat, Warmonger, Economist, 
                Ideologue, Opportunist, Reformer
```

**Country Goals (9 Types):**
```kotlin
TERRITORIAL_EXPANSION
ECONOMIC_DOMINANCE
REGIONAL_HEGEMONY
IDEOLOGICAL_SPREAD
RESOURCE_SECURITY
ALLIANCE_BUILDING
MILITARY_MODERNIZATION
SOFT_POWER
TECHNOLOGY_LEADERSHIP
```

**Daily AI Actions (10% chance):**

*Hostile (aggression > 0.7, low relations):*
```kotlin
MILITARY_THREAT      // -15 relations, crisis event
ECONOMIC_SANCTIONS   // -25 relations, trade impact
CYBER_ATTACK         // -30 relations, security crisis
SUPPORT_REBELS       // Destabilize player
```

*Friendly (cooperation > 0.6, good relations):*
```kotlin
TRADE_OFFER          // +10 relations, trade deal
MILITARY_COOPERATION // +15 relations, alliance opportunity
TECHNOLOGY_SHARING   // +10 relations, tech boost
DIPLOMATIC_SUPPORT   // +5 relations, UN support
```

*Expansionist (ambition > 0.7):*
```kotlin
TERRITORIAL_CLAIM    // -40 relations, crisis
RESOURCE_GRAB        // Economic boost, relations hit
SPHERE_OF_INFLUENCE  // Regional dominance
ARMS_RACE            // Military buildup
```

**Leader Personality Effects:**
```kotlin
ECONOMIST:
  Focus: Trade deals, investment, aid
  
WARMONGER:
  Focus: Military buildup, war games, arms deals
  
DIPLOMAT:
  Focus: Visits, cultural exchange, negotiations
  
IDEOLOGUE:
  Focus: Spread ideology, support allies
  
OPPORTUNIST:
  Focus: Exploit weaknesses, switch sides
  
REFORMER:
  Focus: Internal improvements, democracy
```

**Dynamic Relation System:**
```kotlin
Daily drift based on:
- AI personality (cooperative = +0.5/day)
- Ideology compatibility (±0.2/day)
- Recent actions (±5 to ±40)
- Alliance membership (+15 if mutual defense)
- Trade volume (+0.1 per 1B trade)
```

**War Spread System:**
```kotlin
// Check if wars expand:
- Enemy's allies with mutual defense (50% join)
- Regional powers get involved
- UN/international response
- Player can preempt or negotiate
```

**Alliance Dynamics:**
```kotlin
// Members reconsider if:
- Satisfaction < 30%
- Unreliable leader (reliability < 0.5)
- Better opportunity elsewhere
- Alliance not delivering benefits

// Satisfaction factors:
+15% mutual defense
+10% shared technology
-10% if much stronger than others
±10% random variation
```

**World Events (1% daily):**
```kotlin
Global Recession   // Affects all trade
Pandemic Outbreak  // Health crisis worldwide
Climate Breakthrough // Clean energy tech
International Org  // New global body
Resource Discovery // Contested deposit
```

**Regional Events (5% daily):**
```kotlin
Trade Bloc Formed   // Neighbors create bloc
Border Skirmish     // Regional conflict
Democracy Movement  // Protests spread
```

---

## ⚡ INTERACTIVE DECISION ENGINE

### 8 Decision Types with Full Consequences

**1. BUDGET_CUT**
```kotlin
Options: Deep (20%), Moderate (10%), Light (5%), None
Effects:
  Deep:    -8 approval, -5 stability, +20% savings
  Moderate: -4 approval, -2 stability, +10% savings
  Light:   -2 approval, -1 stability, +5% savings
Delayed: Protests, efficiency drops
```

**2. TAX_CHANGE**
```kotlin
Options: +5%, +2%, Maintain, -2%, -5%
Effects:
  Increase: -5 approval, +revenue, class faith changes
  Decrease: +3 approval, -revenue, business confidence
Delayed: Economic growth/contraction
```

**3. PERMIT_APPROVAL**
```kotlin
Options: Approve, Reject, Approve with Conditions
Effects:
  Approve: +growth, -environment, inspection tasks
  Reject:  ±approval, no environmental cost
  Conditions: Balanced approach
Delayed: Compliance issues, protests
```

**4. DIPLOMATIC_RESPONSE**
```kotlin
Options: Strong Protest, Diplomatic, Dialogue, 
         Compromise, Ignore, Retaliate
Effects:
  Strong:    -15 relations, -trade
  Diplomatic: -5 relations
  Dialogue:   +5 relations
  Compromise: +10 relations
  Retaliate:  -20 relations, crisis risk
```

**5. CRISIS_RESPONSE**
```kotlin
Economic Crisis:
  Bailout: +5 approval, +10 stability, -50B
  Let Market: -10 approval, -15 stability, 0 cost
  Regulate: +3 approval, +5 stability, -10B
  Nationalize: +8 approval, -5 stability, -100B

Health Crisis:
  Lockdown: -5 approval, +20 health, -5B
  Vaccination: +10 approval, +15 health, -10B
  Public Info: +5 approval, +5 health, -1B
  Do Nothing: -20 approval, -30 health, 0 cost

Disaster Crisis:
  Emergency: +10 approval, +5 stability, -20B
  Military: +5 approval, +10 stability, -30B
  Int'l Aid: +8 approval, +3 stability, -5B
  Wait: -15 approval, -20 stability, 0 cost

Political Crisis:
  Resignation: +10 approval, +15 stability
  Investigation: +5 approval, +5 stability
  Deny: -5 approval, -10 stability
  Attack: -10 approval, -15 stability

Security Crisis:
  Military: +5 approval, +10 stability, -50B
  Diplomatic: +3 approval, +5 stability, -1B
  Sanctions: 0 approval, +3 stability, -10B
  Nothing: -20 approval, -30 stability, 0 cost
```

**6. SOCIAL_PROGRAM**
```kotlin
Options: Full, Partial (50%), Minimal (20%), Cut
Effects:
  Full:    +5 approval, +8 happiness, -100% budget
  Partial: +3 approval, +4 happiness, -50% budget
  Minimal: +1 approval, +1 happiness, -20% budget
  Cut:     -10 approval, -15 happiness, +budget
```

**7. MILITARY_ACTION**
```kotlin
Options: Full Invasion, Limited Strike, Drone, 
         Special Forces, Withdraw
Effects:
  Invasion: +10 approval, -10 stability, -100B, 1k-10k casualties
  Limited:  +5 approval, 0 stability, -20B, 10-500 casualties
  Drone:    +3 approval, +2 stability, -1B, 0-50 casualties
  Special:  +2 approval, +3 stability, -5B, 0-100 casualties
  Withdraw: -5 approval, +5 stability, -5B, 0 casualties
Delayed: Follow-up tasks, diplomatic calls
```

**8. ECONOMIC_POLICY**
```kotlin
Stimulus:
  +8 approval, +3% growth, +2% inflation
  
Austerity:
  -10 approval, -2% growth, -1% inflation
  
Tax Cuts:
  +5 approval, +2% growth, +1% inflation
  
Regulation:
  +3 approval, -1% growth, 0% inflation
  
Deregulation:
  +2 approval, +1% growth, +1% inflation
```

---

## 🎯 EMERGENT GAMEPLAY EXAMPLES

### Example 1: NPC-Driven Protest Cascade
```kotlin
Day 1: Teacher (approval 35%, high neuroticism) → PROTEST
Day 2: 1000 people protest, approval -8, stability -5
Day 3: Journalist writes critical article
Day 4: More NPCs join (approval < 40%)
Day 5: Activist forms "Education Reform Movement"
Day 6: Movement gains 5000 followers
Day 7: Crisis event: "Nationwide Education Strike"
Player must: Meet demands, police response, or concede
```

### Example 2: Economic Spiral
```kotlin
Day 1: Oil price spike (commodity market)
Day 2: Transport sector health -15
Day 3: Inflation +1.5% (cost-push)
Day 4: Consumer spending -5%
Day 5: Retail sector growth -3%
Day 6: Unemployment +0.5%
Day 7: Event: "Economic Slowdown Concerns"
Player must: Stimulus, rate hike, or do nothing
```

### Example 3: War Escalation
```kotlin
Day 1: AI country (aggression 0.8) → MILITARY_THREAT
Day 2: Player chooses "Protest" (-15 relations)
Day 3: AI country (reliability 0.3) → CYBER_ATTACK
Day 4: Player chooses "Counter-Attack"
Day 5: AI country activates mutual defense alliance
Day 6: Ally joins war (-40 relations with player)
Day 7: Regional event: "Border Skirmish Spreads"
Player must: Negotiate, escalate, or seek allies
```

### Example 4: Alliance Collapse
```kotlin
Day 1: Alliance member satisfaction 25% (low)
Day 2: Member (unreliable 0.4) considers exit
Day 3: Event: "Ally Considering Exit"
Day 4: Player chooses "Offer Incentives"
Day 5: Member stays (+20 satisfaction)
Day 6: Another member unhappy (domino effect)
Day 7: Alliance at risk of collapse
```

---

## ⚡ REAL-TIME SIMULATION LOOP

```kotlin
// Daily simulation (background worker)
suspend fun simulateDay() {
    // NPC system (30% of 500 NPCs act)
    npcSystem.processDailyNPCActions(countryId)
    
    // Economy system (all sectors update)
    economySystem.simulateDay(countryId)
    
    // World system (15+ AI countries act)
    worldSystem.simulateDay(playerCountryId)
    
    // Butterfly effects (delayed triggers)
    butterflyEngine.processButterflyEffects(countryId)
    
    // Generate new events
    eventGenerator.generateDailyEvents(countryId)
}
```

**Every day:**
- 150+ NPCs make decisions
- 10 market sectors update
- 15 commodity prices fluctuate
- 15+ AI countries act
- Stock market moves
- Employment changes
- Inflation calculates
- Business births/deaths
- Relations drift
- Alliances shift
- Events generated

**NOTHING IS STATIC - EVERYTHING IS ALIVE**

---

## 🎮 THIS IS THE GAME

**No static databases. No scripted events. No predetermined outcomes.**

- 500+ NPCs with AI personalities, memories, goals
- 10 economic sectors with supply/demand dynamics
- 15 commodity markets with price fluctuations
- 15+ AI countries with their own agendas
- Dynamic alliances that form and collapse
- Wars that spread regionally
- Economies that boom and crash
- NPCs that protest, organize, donate, run for office
- Every decision has immediate AND delayed consequences
- Butterfly effects cascade through the system

**Build this. Play it. Every game is unique.**

🎮 **DYNAMIC. ALIVE. UNPREDICTABLE.** 🎮
