package com.country.simulator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "npcs")
data class NPC(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val age: Int,
    val gender: Gender,
    val occupation: String,
    val occupationType: OccupationType,
    val income: Double,
    val wealth: Double,
    val education: Int, // years
    val location: String,
    val region: String,
    
    // Characteristics
    val personality: String, // JSON
    val skills: String = "", // JSON array
    val relationships: String = "", // JSON array
    
    // Political
    val politicalAffiliation: Long?,
    val politicalLeanings: Int, // -100 to 100
    val votingHistory: String = "", // JSON array
    
    // Social
    val ethnicity: String?,
    val religion: String?,
    val maritalStatus: String,
    val children: Int = 0,
    
    // Status
    val happiness: Double, // 0-100
    val health: Double, // 0-100
    val approvalOfGovernment: Double, // 0-100
    val trustInInstitutions: Double, // 0-100
    
    // Memory
    val memories: String = "", // JSON array of events
    val grievances: String = "", // JSON array
    val favors: String = "", // JSON array
    
    val isActive: Boolean = true,
    val isImportant: Boolean = false, // Key NPC
    val lastUpdated: Long = System.currentTimeMillis()
)

enum class Gender {
    MALE,
    FEMALE,
    OTHER
}

enum class OccupationType {
    POLITICIAN,
    BUSINESS_OWNER,
    CORPORATE_EXEC,
    WORKER,
    FARMER,
    TEACHER,
    DOCTOR,
    LAWYER,
    ENGINEER,
    ARTIST,
    STUDENT,
    RETIRED,
    UNEMPLOYED,
    CIVIL_SERVANT,
    MILITARY,
    POLICE,
    JOURNALIST,
    ACTIVIST,
    CRIMINAL,
    SCIENTIST,
    ATHLETE,
    ENTERTAINER
}

@Entity(tableName = "npc_memories")
data class NPCMemory(
    @PrimaryKey val id: Long = 0,
    val npcId: Long,
    val countryId: Long,
    val memoryType: MemoryType,
    val eventDescription: String,
    val eventDate: Long,
    val emotionalImpact: Int, // -100 to 100
    val lastingEffect: Int, // -100 to 100
    val relatedEntity: String?, // law, policy, person, etc.
    val isForgotten: Boolean = false,
    val forgottenDate: Long?
)

enum class MemoryType {
    POSITIVE_EXPERIENCE,
    NEGATIVE_EXPERIENCE,
    POLITICAL_EVENT,
    ECONOMIC_EVENT,
    SOCIAL_EVENT,
    PERSONAL_EVENT,
    GOVERNMENT_ACTION,
    POLICY_CHANGE,
    SCANDAL,
    CRIME,
    ACHIEVEMENT,
    LOSS
}

@Entity(tableName = "npc_relationships")
data class NPCRelationship(
    @PrimaryKey val id: Long = 0,
    val npcId1: Long,
    val npcId2: Long,
    val relationshipType: RelationshipType,
    val strength: Int, // -100 to 100
    val establishedDate: Long,
    val lastInteraction: Long,
    val interactions: String = "", // JSON array
    val favorsOwed: String = "", // JSON
    val conflicts: String = "", // JSON array
    val isActive: Boolean = true
)

enum class RelationshipType {
    FAMILY,
    FRIEND,
    COLLEAGUE,
    BUSINESS_PARTNER,
    POLITICAL_ALLY,
    POLITICAL_OPPONENT,
    RIVAL,
    ENEMY,
    ROMANTIC,
    MENTOR,
    MENTEE,
    NEIGHBOR,
    ACQUAINTANCE
}

@Entity(tableName = "lobbyists")
data class Lobbyist(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val organization: String,
    val industry: String,
    val clients: String, // JSON array
    val fundsAvailable: Double,
    val influence: Double, // 0-100
    val connections: String = "", // JSON array of NPC IDs
    val activeCampaigns: Int = 0,
    val successRate: Double, // 0-100
    val scandals: Int = 0,
    val isActive: Boolean = true
)

@Entity(tableName = "activists")
data class Activist(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val cause: String,
    val organization: String?,
    val followers: Int,
    val influence: Double, // 0-100
    val tactics: String = "", // JSON array
    val eventsOrganized: Int = 0,
    val arrests: Int = 0,
    val mediaAttention: Int, // 1-10
    val isActive: Boolean = true
)

@Entity(tableName = "witnesses")
data class Witness(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val occupation: String,
    val credibility: Double, // 0-100
    val bias: Int, // -100 to 100
    val testimony: String,
    val evidence: String = "", // JSON array
    val isProtected: Boolean = false,
    val protectionProgram: Boolean = false,
    val relatedCaseId: Long? = null
)

@Entity(tableName = "jurors")
data class Juror(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val age: Int,
    val occupation: String,
    val education: Int,
    val background: String,
    val biases: String = "", // JSON array
    val selectionStatus: JurorStatus,
    val caseId: Long? = null,
    val notes: String? = null
)

enum class JurorStatus {
    POOL,
    SELECTED,
    CHALLENGED,
    DISMISSED,
    SEQUESTERED,
    COMPLETED
)

@Entity(tableName = "donors")
data class Donor(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val donorType: DonorType,
    val organization: String?,
    val totalDonated: Double,
    val recipients: String = "", // JSON array
    val politicalLeanings: Int, // -100 to 100
    val expectations: String = "", // JSON array
    val isControversial: Boolean = false,
    val isActive: Boolean = true
)

enum class DonorType {
    INDIVIDUAL,
    CORPORATION,
    UNION,
    PAC,
    FOREIGN,
    ANONYMOUS
}

@Entity(tableName = "contractors")
data class Contractor(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val companyName: String,
    val ownerName: String,
    val industry: String,
    val rating: Double, // 0-100
    val pastProjects: Int,
    val completedOnTime: Int,
    val completedOnBudget: Int,
    val safetyRecord: Double, // 0-100
    val corruptionAllegations: Int = 0,
    val activeContracts: Int = 0,
    val totalValue: Double = 0.0,
    val isActive: Boolean = true
)

@Entity(tableName = "foreign_leaders")
data class ForeignLeader(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val foreignCountryId: Long,
    val name: String,
    val title: String,
    val ideology: String,
    val personality: String,
    val relationToPlayer: Int, // -100 to 100
    val trustLevel: Int, // 0-100
    val agreements: String = "", // JSON array
    val disputes: String = "", // JSON array
    val stateVisits: String = "", // JSON array
    val isActive: Boolean = true
)
