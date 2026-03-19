package com.country.simulator.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "elections")
data class Election(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val electionType: ElectionType,
    val scheduledDate: Long,
    val completedDate: Long? = null,
    val isActive: Boolean = false,
    val result: ElectionResult? = null
}

enum class ElectionType {
    PRESIDENTIAL,
    PARLIAMENTARY,
    LOCAL,
    REFERENDUM,
    SPECIAL
}

data class ElectionResult(
    val winnerId: Long,
    val winnerName: String,
    val winnerParty: String,
    val votePercentage: Double,
    val turnoutPercentage: Double,
    val totalVotes: Long,
    val opponents: List<ElectionOpponent>
}

data class ElectionOpponent(
    val name: String,
    val party: String,
    val votePercentage: Double
}

@Entity(tableName = "campaigns")
data class Campaign(
    @PrimaryKey val id: Long = 0,
    val electionId: Long,
    val candidateName: String,
    val party: String,
    val slogan: String,
    val budget: Double,
    val spent: Double = 0.0,
    val followers: Int = 0,
    val ralliesHeld: Int = 0,
    val speechesGiven: Int = 0,
    val scandals: Int = 0,
    val pollRating: Double = 0.0,
    val promises: String = "", // JSON array of promises
    val isActive: Boolean = true
}

@Entity(tableName = "campaign_events")
data class CampaignEvent(
    @PrimaryKey val id: Long = 0,
    val campaignId: Long,
    val eventType: CampaignEventType,
    val location: String,
    val description: String,
    val cost: Double,
    val impact: Double, // -100 to 100
    val completed: Boolean = false,
    val scheduledDate: Long
}

enum class CampaignEventType {
    RALLY,
    SPEECH,
    TV_AD,
    RADIO_AD,
    SOCIAL_MEDIA,
    DOOR_TO_DOOR,
    DEBATE,
    INTERVIEW,
    SCANDAL_RESPONSE,
    OPPOSITION_RESEARCH
}

@Entity(tableName = "political_parties")
data class PoliticalParty(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val name: String,
    val abbreviation: String,
    val ideology: String,
    val leaderName: String,
    val foundingYear: Int,
    val seatsInParliament: Int = 0,
    val membershipCount: Int = 0,
    val funds: Double = 0.0,
    val approvalRating: Double = 0.0,
    val isRulingParty: Boolean = false,
    val isOppositionParty: Boolean = false
}

@Entity(tableName = "scandals")
data class Scandal(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val title: String,
    val description: String,
    val severity: Int, // 1-10
    val type: ScandalType,
    val involvedPersons: String, // JSON array
    val dateExposed: Long,
    val resolved: Boolean = false,
    val resolutionDate: Long? = null,
    val impactOnApproval: Double = 0.0,
    val responseStrategy: String? = null
}

enum class ScandalType {
    CORRUPTION,
    AFFAIR,
    FINANCIAL,
    ABUSE_OF_POWER,
    LEAK,
    FOREIGN_INTERFERENCE,
    CRIMINAL
}

@Entity(tableName = "opinion_polls")
data class OpinionPoll(
    @PrimaryKey val id: Long = 0,
    val countryId: Long,
    val pollDate: Long,
    val sampleSize: Int,
    val marginOfError: Double,
    val approvalRating: Double,
    val disapprovalRating: Double,
    val undecidedPercentage: Double,
    val topIssues: String, // JSON array
    val demographicBreakdown: String? = null // JSON object
}
