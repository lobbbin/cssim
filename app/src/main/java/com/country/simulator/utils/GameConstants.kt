package com.country.simulator.utils

import kotlin.random.Random

/**
 * Game Constants and Configuration Values
 * Central location for all game balance numbers
 */
object GameConstants {
    
    // Election Constants
    const val ELECTION_CAMPAIGN_DURATION_DAYS = 30
    const val MIN_APPROVAL_TO_WIN = 50.0
    const val MAX_CAMPAIGN_BUDGET = 50_000_000.0
    const val SPEECH_BASE_COST = 10_000.0
    const val RALLY_BASE_COST = 50_000.0
    const val AD_SLOT_BASE_COST = 25_000.0
    const val SOCIAL_MEDIA_BASE_GAIN = 1000
    
    // Economy Constants
    const val STARTING_TREASURY = 1_000_000_000.0
    const val MAX_TAX_RATE = 70.0
    const val MIN_TAX_RATE = 5.0
    const val DEFAULT_INFLATION_RATE = 2.0
    const val DEFAULT_UNEMPLOYMENT_RATE = 5.0
    const val DEFAULT_GROWTH_RATE = 2.5
    const val OIL_PRICE_VOLATILITY = 0.15
    
    // Approval Rating Constants
    const val MAX_APPROVAL = 100.0
    const val MIN_APPROVAL = 0.0
    const val CRITICAL_APPROVAL_THRESHOLD = 30.0
    const val EXCELLENT_APPROVAL_THRESHOLD = 70.0
    
    // Stability Constants
    const val MAX_STABILITY = 100.0
    const val MIN_STABILITY = 0.0
    const val COUP_RISK_THRESHOLD = 20.0
    const val PROTEST_RISK_THRESHOLD = 40.0
    
    // Ministry Constants
    const val NUM_KEY_APPOINTEES = 7
    const val MINISTRY_BASE_BUDGET = 100_000_000.0
    const val MAX_CORRUPTION_THRESHOLD = 50.0
    const val MIN_EFFICIENCY_THRESHOLD = 40.0
    
    // Diplomacy Constants
    const val MAX_RELATION = 100
    const val MIN_RELATION = -100
    const val ALLY_THRESHOLD = 50
    const val FRIENDLY_THRESHOLD = 20
    const val HOSTILE_THRESHOLD = -50
    const val WAR_THRESHOLD = -80
    
    // NPC Constants
    const val MAX_NPC_MEMORY_ENTRIES = 50
    const val NPC_MEMORY_DECAY_DAYS = 365
    const val IMPORTANT_NPC_COUNT = 50
    const val MAX_NPC_RELATIONSHIPS = 100
    
    // Task Constants
    const val MAX_PENDING_TASKS = 100
    const val TASK_URGENCY_THRESHOLD_HOURS = 24
    const val BUTTERFLY_EFFECT_CHAIN_MAX = 3
    const val BUTTERFLY_EFFECT_CHANCE = 0.3
    
    // Pagination Constants
    const val PAGE_SIZE = 20
    const val PREFETCH_DISTANCE = 10
    const val MAX_CACHE_SIZE = 100
    const val INITIAL_LOAD_SIZE = 40
    
    // Time Constants
    const val MS_PER_HOUR = 3_600_000L
    const val MS_PER_DAY = 86_400_000L
    const val MS_PER_WEEK = 604_800_000L
    const val MS_PER_MONTH = 2_629_746_000L
    const val MS_PER_YEAR = 31_557_600_000L
    
    // Event Constants
    const val MAX_ACTIVE_EVENTS = 20
    const val EVENT_PRIORITY_URGENT = 10
    const val EVENT_PRIORITY_HIGH = 7
    const val EVENT_PRIORITY_MEDIUM = 4
    const val EVENT_PRIORITY_LOW = 1
    const val RANDOM_EVENT_CHANCE_PER_MINUTE = 0.1
    
    // Sports Constants
    const val NATIONAL_SQUAD_SIZE = 23
    const val MAX_STADIUM_CAPACITY = 100_000
    const val MIN_STADIUM_CAPACITY = 5_000
    const val DOPING_BAN_MONTHS = 24
    const val ELITE_TRAINING_BASE_COST = 1_000_000.0
    
    // Health Constants
    const val MAX_DISEASE_R0 = 5.0
    const val QUARANTINE_EFFECTIVENESS = 0.7
    const val VACCINE_EFFECTIVENESS = 0.9
    const val CRITICAL_MEDICINE_THRESHOLD = 1000
    
    // Immigration Constants
    const val DEFAULT_ANNUAL_VISA_QUOTA = 5000
    const val HIGH_TALENT_BONUS_MULTIPLIER = 2.0
    const val REFUGEE_ACCEPTANCE_BASE = 1000
    const val INTEGRATION_PROGRAM_BASE_COST = 10_000_000.0
    
    // Infrastructure Constants
    const val POWER_STATION_BASE_COST = 500_000_000.0
    const val HIGHWAY_KM_BASE_COST = 5_000_000.0
    const val BUS_ROUTE_BASE_COST = 1_000_000.0
    const val POLLUTION_CONTROL_BASE_COST = 50_000_000.0
    const val MAX_POLLUTION_LEVEL = 100.0
    
    // Law Constants
    const val MAX_SENTENCE_YEARS = 99
    const val MIN_SENTENCE_MONTHS = 1
    const val JURY_SIZE = 12
    const val SUPREME_COURT_JUSTICES = 9
    const val CONSTITUTIONAL_AMENDMENT_THRESHOLD = 0.67
    
    // Scandal Constants
    const val MAX_SCANDAL_SEVERITY = 10
    const val SCANDAL_DECAY_RATE = 0.1
    const val SCANDAL_RESPONSE_EFFECTIVENESS_DENY = 0.7
    const val SCANDAL_RESPONSE_EFFECTIVENESS_APOLOGIZE = 0.6
    const val SCANDAL_RESPONSE_EFFECTIVENESS_ATTACK = 0.5
    const val SCANDAL_RESPONSE_EFFECTIVENESS_IGNORE = 0.3
    
    // Trade Constants
    const val MAX_TARIFF_RATE = 100.0
    const val FREE_TRADE_TARIFF = 0.0
    const val CUSTOMS_UNION_COMMON_TARIFF = 10.0
    const val TRADE_DEAL_DURATION_YEARS = 5
    
    // Military Constants
    const val MIN_DEPLOYMENT_DAYS = 30
    const val WAR_SUPPORT_THRESHOLD = 50.0
    const val CASUALTY_IMPACT_ON_APPROVAL = 0.1
    const val DEFENSE_PACT_TRIGGER_THRESHOLD = 60
    
    // Budget Allocation Percentages
    object BudgetPercentages {
        const val DEFENSE = 0.15
        const val HEALTHCARE = 0.18
        const val EDUCATION = 0.12
        const val INFRASTRUCTURE = 0.10
        const val SOCIAL_WELFARE = 0.20
        const val DEBT_SERVICING = 0.08
        const val OTHER = 0.17
    }
    
    // Tax Bracket Defaults
    object TaxBrackets {
        val INCOME_BRACKETS = listOf(
            0.0 to 20000.0 to 0.10,
            20000.0 to 50000.0 to 0.15,
            50000.0 to 100000.0 to 0.22,
            100000.0 to 200000.0 to 0.28,
            200000.0 to 500000.0 to 0.35,
            500000.0 to null to 0.40
        )
        const val CORPORATE_TAX = 0.21
        const val SALES_TAX = 0.10
        const val LUXURY_TAX = 0.25
    }
    
    // Butterfly Effect Magnitudes
    object ButterflyMagnitudes {
        const val MICRO_TO_MACRO = 0.01
        const val MACRO_TO_MICRO = 5.0
        const val DELAYED_EFFECT_MULTIPLIER = 0.5
        const val CHAIN_EFFECT_DECAY = 0.5
    }
    
    // AI Difficulty Settings
    object Difficulty {
        const val EASY_AI_BONUS = 1.2
        const val NORMAL_AI_BONUS = 1.0
        const val HARD_AI_BONUS = 0.8
        const val IMPOSSIBLE_AI_BONUS = 0.6
    }
}

/**
 * Extension functions for common game calculations
 */
fun Double.coerceToPercentage(): Double = this.coerceIn(0.0, 100.0)

fun Int.coerceToRelation(): Int = this.coerceIn(-100, 100)

fun Double.calculateTax(amount: Double): Double = amount * this

fun Double.applyModifier(modifier: Double): Double = this * (1.0 + modifier)

fun Long.daysFromNow(): Long = this + System.currentTimeMillis()

fun Long.formatCurrency(): String {
    return when {
        this >= 1_000_000_000_000 -> "$${this / 1_000_000_000_000}T"
        this >= 1_000_000_000 -> "$${this / 1_000_000_000}B"
        this >= 1_000_000 -> "$${this / 1_000_000}M"
        this >= 1_000 -> "$${this / 1_000}K"
        else -> "$$this"
    }
}

fun Double.formatPercentage(): String = "${this.toInt()}%"

fun Int.formatNumber(): String {
    return when {
        this >= 1_000_000_000 -> "${this / 1_000_000_000}B"
        this >= 1_000_000 -> "${this / 1_000_000}M"
        this >= 1_000 -> "${this / 1_000}K"
        else -> "$this"
    }
}

/**
 * Random generators for game values
 */
object RandomGenerators {
    
    private val random = Random
    
    fun generateApprovalChange(base: Double = 5.0): Double {
        return base + (random.nextDouble() - 0.5) * base * 0.5
    }
    
    fun generateRelationChange(base: Int = 5): Int {
        return base + (random.nextInt(-2, 3))
    }
    
    fun generateEconomicValue(min: Double, max: Double): Double {
        return random.nextDouble(min, max)
    }
    
    fun generateNPCAge(): Int {
        return random.nextInt(18, 85)
    }
    
    fun generateSalary(occupation: String): Double {
        val baseSalary = when (occupation) {
            "CEO", "Business Owner" -> random.nextDouble(200_000.0, 5_000_000.0)
            "Doctor", "Lawyer" -> random.nextDouble(100_000.0, 300_000.0)
            "Engineer", "Teacher" -> random.nextDouble(50_000.0, 100_000.0)
            "Worker", "Farmer" -> random.nextDouble(20_000.0, 50_000.0)
            else -> random.nextDouble(30_000.0, 80_000.0)
        }
        return baseSalary
    }
    
    fun generateEventSeverity(): Int {
        return random.nextInt(1, 11)
    }
    
    fun generatePollMargin(): Double {
        return random.nextDouble(2.5, 4.5)
    }
    
    fun generateAttendance(base: Int): Int {
        return base + random.nextInt(-(base / 10), base / 10)
    }
    
    fun generateCost(base: Double): Double {
        return base + (random.nextDouble() - 0.5) * base * 0.2
    }
    
    fun generateQuality(): Double {
        return random.nextDouble(50.0, 100.0)
    }
    
    fun generateSuccess(chance: Double): Boolean {
        return random.nextDouble() < chance
    }
}

/**
 * Validation utilities for game inputs
 */
object ValidationUtils {
    
    fun validateApproval(value: Double): Boolean {
        return value in 0.0..100.0
    }
    
    fun validateRelation(value: Int): Boolean {
        return value in -100..100
    }
    
    fun validateTaxRate(value: Double): Boolean {
        return value in GameConstants.MIN_TAX_RATE..GameConstants.MAX_TAX_RATE
    }
    
    fun validateBudget(amount: Double, available: Double): Boolean {
        return amount >= 0 && amount <= available
    }
    
    fun validatePercentage(value: Double): Boolean {
        return value in 0.0..100.0
    }
    
    fun validateCount(value: Int, max: Int): Boolean {
        return value in 0..max
    }
    
    fun validateDate(future: Long, past: Long): Boolean {
        return future > past
    }
    
    fun validateName(name: String, maxLength: Int = 50): Boolean {
        return name.isNotBlank() && name.length <= maxLength
    }
}

/**
 * Time utilities for game calculations
 */
object TimeUtils {
    
    fun getDaysUntil(timestamp: Long): Long {
        val diff = timestamp - System.currentTimeMillis()
        return diff / GameConstants.MS_PER_DAY
    }
    
    fun getHoursUntil(timestamp: Long): Long {
        val diff = timestamp - System.currentTimeMillis()
        return diff / GameConstants.MS_PER_HOUR
    }
    
    fun addDays(days: Int): Long {
        return System.currentTimeMillis() + (days * GameConstants.MS_PER_DAY)
    }
    
    fun addMonths(months: Int): Long {
        return System.currentTimeMillis() + (months * GameConstants.MS_PER_MONTH)
    }
    
    fun addYears(years: Int): Long {
        return System.currentTimeMillis() + (years * GameConstants.MS_PER_YEAR)
    }
    
    fun isOverdue(timestamp: Long): Boolean {
        return System.currentTimeMillis() > timestamp
    }
    
    fun isDueSoon(timestamp: Long, thresholdHours: Int = 24): Boolean {
        val hoursUntil = getHoursUntil(timestamp)
        return hoursUntil in 0..thresholdHours
    }
    
    fun formatDuration(ms: Long): String {
        val days = ms / GameConstants.MS_PER_DAY
        val hours = (ms % GameConstants.MS_PER_DAY) / GameConstants.MS_PER_HOUR
        
        return when {
            days > 365 -> "${days / 365}y ${days % 365}d"
            days > 0 -> "${days}d ${hours}h"
            hours > 0 -> "${hours}h"
            else -> "< 1h"
        }
    }
}

/**
 * String formatting utilities
 */
object FormatUtils {
    
    fun formatCoordinate(lat: Double, lng: Double): String {
        val latDir = if (lat >= 0) "N" else "S"
        val lngDir = if (lng >= 0) "E" else "W"
        return "${Math.abs(lat).toInt()}°$latDir ${Math.abs(lng).toInt()}°$lngDir"
    }
    
    fun formatLargeNumber(number: Long): String {
        return when {
            number >= 1_000_000_000_000 -> "%.2fT".format(number / 1_000_000_000_000.0)
            number >= 1_000_000_000 -> "%.2fB".format(number / 1_000_000_000.0)
            number >= 1_000_000 -> "%.2fM".format(number / 1_000_000.0)
            number >= 1_000 -> "%.2fK".format(number / 1_000.0)
            else -> number.toString()
        }
    }
    
    fun formatCurrency(amount: Double): String {
        return when {
            amount >= 1_000_000_000_000 -> "$%.2fT".format(amount / 1_000_000_000_000.0)
            amount >= 1_000_000_000 -> "$%.2fB".format(amount / 1_000_000_000.0)
            amount >= 1_000_000 -> "$%.2fM".format(amount / 1_000_000.0)
            amount >= 1_000 -> "$%.2fK".format(amount / 1_000.0)
            else -> "$%.2f".format(amount)
        }
    }
    
    fun truncateText(text: String, maxLength: Int): String {
        return if (text.length > maxLength) {
            text.take(maxLength - 3) + "..."
        } else {
            text
        }
    }
    
    fun capitalizeFirst(text: String): String {
        return text.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
    
    fun formatList(items: List<String>, separator: String = ", ", lastSeparator: String = " & "): String {
        return when (items.size) {
            0 -> ""
            1 -> items[0]
            2 -> items.joinToString(lastSeparator)
            else -> items.dropLast(1).joinToString(separator) + lastSeparator + items.last()
        }
    }
}
