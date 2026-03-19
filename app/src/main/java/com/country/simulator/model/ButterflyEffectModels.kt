package com.country.simulator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "macro_decisions")
data class MacroDecision(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val decisionType: MacroDecisionType,
    val title: String,
    val description: String,
    val category: DecisionCategory,
    val options: String, // JSON array of options
    val selectedOption: String?,
    val decisionDate: Long,
    val consequences: String = "", // JSON
    val microTasksGenerated: Int = 0,
    val isCompleted: Boolean = false,
    val completedDate: Long? = null
}

enum class MacroDecisionType {
    ELECTION,
    TRADE_DEAL,
    WAR_DECLARATION,
    SANCTIONS,
    BUDGET_APPROVAL,
    LAW_ENACTMENT,
    ALLIANCE_JOIN,
    BLOC_JOIN,
    CONSTITUTIONAL_CHANGE,
    EMERGENCY_DECLARATION
}

enum class DecisionCategory {
    POLITICAL,
    ECONOMIC,
    MILITARY,
    DIPLOMATIC,
    SOCIAL,
    ENVIRONMENTAL
}

@Entity(tableName = "micro_tasks")
data class MicroTask(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val taskType: MicroTaskType,
    val title: String,
    val description: String,
    val category: TaskCategory,
    val priority: Priority,
    val relatedMacroId: Long?,
    val relatedEntityId: Long?,
    val relatedEntityType: String?,
    
    // Task details
    val options: String = "", // JSON array
    val selectedOption: String?,
    val requirements: String = "", // JSON
    val rewards: String = "", // JSON
    
    // Status
    val status: TaskStatus,
    val dueDate: Long?,
    val createdDate: Long,
    val completedDate: Long?,
    
    // Effects
    val effects: String = "", // JSON
    val butterflyEffects: String = "" // JSON - cascading effects
}

enum class MicroTaskType {
    APPROVE_PERMIT,
    REVIEW_REPORT,
    SIGN_DOCUMENT,
    MAKE_CALL,
    ATTEND_MEETING,
    GIVE_SPEECH,
    RESPOND_TO_CRISIS,
    NEGOTIATE_DEAL,
    APPROVE_HIRE,
    REVIEW_BUDGET,
    SIGN_TREATY,
    DECLARE_POLICY,
    INVESTIGATE_SCANDAL,
    AWARD_CONTRACT,
    VETO_LEGISLATION,
    GRANT_PARDON,
    DEPLOY_TROOPS,
    EXPUL_DIPLOMAT,
    LAUNCH_AUDIT,
    SET_PRICE,
    ADJUST_RATE,
    ALLOCATE_FUNDS,
    REVIEW_CASE,
    APPOINT_JUDGE,
    SCHEDULE_EVENT
}

enum class TaskCategory {
    POLITICS,
    ECONOMY,
    LAW,
    DIPLOMACY,
    INFRASTRUCTURE,
    HEALTH,
    EDUCATION,
    DEFENSE,
    SOCIAL,
    ENVIRONMENT
}

enum class TaskStatus {
    PENDING,
    IN_PROGRESS,
    WAITING_APPROVAL,
    COMPLETED,
    CANCELLED,
    EXPIRED,
    FAILED
}

@Entity(tableName = "butterfly_effects")
data class ButterflyEffect(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val sourceTaskId: Long,
    val sourceTaskType: MicroTaskType,
    val effectType: EffectType,
    val targetEntity: String, // table name
    val targetEntityId: Long?,
    val effectDescription: String,
    val effectMagnitude: Double, // -100 to 100
    val delayDays: Int, // days until effect manifests
    val isTriggered: Boolean = false,
    val triggeredDate: Long?,
    val expiryDate: Long?,
    val chainLevel: Int = 0 // how deep in the cascade
}

enum class EffectType {
    POSITIVE,
    NEGATIVE,
    NEUTRAL,
    MIXED,
    DELAYED,
    PERMANENT,
    TEMPORARY
}

@Entity(tableName = "task_chains")
data class TaskChain(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val chainName: String,
    val chainType: ChainType,
    val initiatingDecision: String,
    val tasks: String, // JSON array of task IDs
    val currentTaskIndex: Int = 0,
    val status: ChainStatus,
    val startDate: Long,
    val completionDate: Long?,
    val successCriteria: String = "", // JSON
    val failureConsequences: String = "" // JSON
}

enum class ChainType {
    CAMPAIGN,
    LEGISLATION,
    NEGOTIATION,
    INVESTIGATION,
    CONSTRUCTION,
    CRISIS_RESPONSE,
    REFORM
}

enum class ChainStatus {
    ACTIVE,
    COMPLETED,
    FAILED,
    ABANDONED
}

@Entity(tableName = "procedural_tables")
data class ProceduralTable(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val tableName: String,
    val tableType: TableType,
    val entries: String, // JSON array
    val generationSeed: Long,
    val lastRegenerated: Long,
    val isActive: Boolean = true
}

enum class TableType {
    NAMES,
    PLACES,
    ORGANIZATIONS,
    EVENTS,
    SCANDALS,
    LAWS,
    POLICIES,
    CONTRACTORS,
    VENDORS,
    ISSUES
}

@Entity(tableName = "game_events")
data class GameEvent(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val eventType: GameEventType,
    val title: String,
    val description: String,
    val category: EventCategory,
    val priority: Int, // 1-10
    val options: String, // JSON array
    val selectedOption: String?,
    val immediateEffects: String = "", // JSON
    val delayedEffects: String = "", // JSON
    val eventDate: Long,
    val expiryDate: Long?,
    val isResolved: Boolean = false,
    val resolvedDate: Long?
}

enum class GameEventType {
    CRISIS,
    OPPORTUNITY,
    SCANDAL,
    DISASTER,
    BREAKTHROUGH,
    PROTEST,
    ELECTION,
    DIPLOMATIC_INCIDENT,
    ECONOMIC_SHIFT,
    SOCIAL_CHANGE,
    TECHNOLOGY,
    CULTURAL
}

enum class EventCategory {
    POLITICAL,
    ECONOMIC,
    MILITARY,
    SOCIAL,
    ENVIRONMENTAL,
    DIPLOMATIC,
    RANDOM
}

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val notificationType: NotificationType,
    val title: String,
    val message: String,
    val relatedEntityId: Long?,
    val relatedEntityType: String?,
    val priority: Priority,
    val isRead: Boolean = false,
    val createdDate: Long,
    val readDate: Long?,
    val actionRequired: Boolean = false,
    val actionUrl: String? = null
}

enum class NotificationType {
    INFO,
    WARNING,
    ALERT,
    TASK,
    DEADLINE,
    ACHIEVEMENT,
    CRISIS
}
