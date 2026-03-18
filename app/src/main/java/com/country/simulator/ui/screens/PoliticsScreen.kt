package com.country.simulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.country.simulator.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoliticsScreen(
    playerCountry: PlayerCountry?,
    elections: List<Election>,
    campaigns: List<Campaign>,
    scandals: List<Scandal>,
    parties: List<PoliticalParty>,
    onNavigateToScreen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Politics") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Government Status
            item {
                GovernmentStatusCard(playerCountry)
            }
            
            // Active Election
            if (elections.any { it.isActive }) {
                item {
                    ActiveElectionCard(elections.first { it.isActive }, campaigns)
                }
            }
            
            // Political Parties
            if (parties.isNotEmpty()) {
                item {
                    PoliticalPartiesCard(parties)
                }
            }
            
            // Active Scandals
            if (scandals.isNotEmpty()) {
                item {
                    ScandalsCard(scandals)
                }
            }
            
            // Campaign Actions
            item {
                CampaignActionsCard()
            }
        }
    }
}

@Composable
fun GovernmentStatusCard(playerCountry: PlayerCountry?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Government",
                    style = MaterialTheme.typography.titleLarge
                )
                
                AssistChip(
                    onClick = { },
                    label = { Text(playerCountry?.currentGameMode?.name ?: "N/A") }
                )
            }
            
            Text(
                text = playerCountry?.leaderName ?: "Loading...",
                style = MaterialTheme.typography.headlineSmall
            )
            
            Text(
                text = playerCountry?.leaderTitle ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Divider()
            
            // Approval Rating
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Approval Rating")
                    Text("${(playerCountry?.approvalRating ?: 0f).toInt()}%")
                }
                LinearProgressIndicator(
                    progress = (playerCountry?.approvalRating ?: 0f) / 100f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Stability
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Stability")
                    Text("${(playerCountry?.stability ?: 0f).toInt()}%")
                }
                LinearProgressIndicator(
                    progress = (playerCountry?.stability ?: 0f) / 100f,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Term", "${playerCountry?.currentTerm ?: 1}")
                StatItem("Promises", "${playerCountry?.promisesMade ?: 0}")
                StatItem("Scandals", "${playerCountry?.scandalsActive ?: 0}")
            }
        }
    }
}

@Composable
fun ActiveElectionCard(election: Election, campaigns: List<Campaign>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Active Election",
                    style = MaterialTheme.typography.titleLarge
                )
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
            
            Text(
                text = election.electionType.name,
                style = MaterialTheme.typography.titleMedium
            )
            
            // Campaign standings
            campaigns.sortedByDescending { it.pollRating }.forEach { campaign ->
                CampaignStandingRow(campaign)
            }
        }
    }
}

@Composable
fun CampaignStandingRow(campaign: Campaign) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = campaign.candidateName,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = campaign.party,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "${campaign.pollRating.toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            LinearProgressIndicator(
                progress = (campaign.pollRating / 100).toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Followers: ${formatCompactNumber(campaign.followers)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Spent: ${formatCurrency(campaign.spent)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun PoliticalPartiesCard(parties: List<PoliticalParty>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Political Parties",
                style = MaterialTheme.typography.titleMedium
            )
            
            parties.sortedByDescending { it.approvalRating }.forEach { party ->
                PartyRow(party)
            }
        }
    }
}

@Composable
fun PartyRow(party: PoliticalParty) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = party.name,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = party.leaderName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (party.isRulingParty) {
                AssistChip(
                    onClick = { },
                    label = { Text("Govt", style = MaterialTheme.typography.labelSmall) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            if (party.isOppositionParty) {
                AssistChip(
                    onClick = { },
                    label = { Text("Opp", style = MaterialTheme.typography.labelSmall) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                )
            }
            Text(
                text = "${party.approvalRating.toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ScandalsCard(scandals: List<Scandal>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Active Scandals",
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            
            scandals.forEach { scandal ->
                ScandalRow(scandal)
            }
        }
    }
}

@Composable
fun ScandalRow(scandal: Scandal) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = scandal.title,
                    style = MaterialTheme.typography.titleSmall
                )
                AssistChip(
                    onClick = { },
                    label = { Text("Severity: ${scandal.severity}/10") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when (scandal.severity) {
                            in 1..3 -> MaterialTheme.colorScheme.tertiary
                            in 4..6 -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.errorContainer
                        }
                    )
                )
            }
            
            Text(
                text = scandal.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "Impact: ${scandal.impactOnApproval}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun CampaignActionsCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Campaign Actions",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ActionButton(
                    icon = Icons.Default.Mic,
                    label = "Give Speech",
                    onClick = { }
                )
                ActionButton(
                    icon = Icons.Default.People,
                    label = "Hold Rally",
                    onClick = { }
                )
                ActionButton(
                    icon = Icons.Default.Tv,
                    label = "TV Ad",
                    onClick = { }
                )
                ActionButton(
                    icon = Icons.Default.Phone,
                    label = "Call",
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = Modifier.weight(1f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(icon, contentDescription = label)
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

fun formatCompactNumber(number: Int): String {
    return when {
        number >= 1_000_000 -> "${number / 1_000_000}M"
        number >= 1_000 -> "${number / 1_000}K"
        else -> number.toString()
    }
}
