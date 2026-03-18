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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.country.simulator.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiplomacyScreen(
    relations: List<DiplomaticRelation>,
    alliances: List<Alliance>,
    tradeBlocs: List<TradeBloc>,
    sanctions: List<Sanction>,
    onCountryClick: (DiplomaticRelation) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diplomacy") },
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
            // Diplomatic Relations
            item {
                Text(
                    text = "International Relations",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            
            items(relations) { relation ->
                CountryRelationCard(relation, onCountryClick)
            }
            
            // Alliances
            if (alliances.isNotEmpty()) {
                item {
                    Text(
                        text = "Alliances",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                
                items(alliances) { alliance ->
                    AllianceCard(alliance)
                }
            }
            
            // Trade Blocs
            if (tradeBlocs.isNotEmpty()) {
                item {
                    Text(
                        text = "Trade Blocs",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                
                items(tradeBlocs) { bloc ->
                    TradeBlocCard(bloc)
                }
            }
            
            // Sanctions
            if (sanctions.isNotEmpty()) {
                item {
                    Text(
                        text = "Active Sanctions",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                
                items(sanctions) { sanction ->
                    SanctionCard(sanction)
                }
            }
            
            // Diplomatic Actions
            item {
                DiplomaticActionsCard()
            }
        }
    }
}

@Composable
fun CountryRelationCard(
    relation: DiplomaticRelation,
    onClick: (DiplomaticRelation) -> Unit
) {
    Card(
        onClick = { onClick(relation) },
        modifier = Modifier.fillMaxWidth()
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
                Column {
                    Text(
                        text = relation.foreignCountryName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = if (relation.embassyEstablished) "Embassy Established" else "No Embassy",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                RelationBadge(relation.relationLevel)
            }
            
            // Relation meter
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Relations")
                    Text("${relation.relationLevel}%")
                }
                LinearProgressIndicator(
                    progress = ((relation.relationLevel + 100) / 200f).coerceIn(0f, 1f),
                    modifier = Modifier.fillMaxWidth(),
                    color = getRelationColor(relation.relationLevel)
                )
            }
            
            // Trust and Trade
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Trust", "${relation.trustLevel}%")
                StatItem("Trade", formatCurrency(relation.tradeVolume))
            }
            
            // Ambassador
            if (relation.ambassadorName != null) {
                Text(
                    text = "Ambassador: ${relation.ambassadorName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AllianceCard(alliance: Alliance) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
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
                    text = alliance.allianceName,
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = Icons.Default.Handshake,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            
            Text(
                text = alliance.allianceType.name.replace("_", " "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Alliance features
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (alliance.mutualDefense) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Defense Pact") },
                        leadingIcon = {
                            Icon(Icons.Default.Shield, contentDescription = null, Modifier.size(16.dp))
                        }
                    )
                }
                if (alliance.sharedTechnology) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Tech Share") },
                        leadingIcon = {
                            Icon(Icons.Default.Science, contentDescription = null, Modifier.size(16.dp))
                        }
                    )
                }
                if (alliance.jointExercises) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Joint Exercises") },
                        leadingIcon = {
                            Icon(Icons.Default.FitnessCenter, contentDescription = null, Modifier.size(16.dp))
                        }
                    )
                }
            }
            
            if (alliance.headquarters.isNotEmpty()) {
                Text(
                    text = "HQ: ${alliance.headquarters}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun TradeBlocCard(bloc: TradeBloc) {
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
                    text = bloc.blocName,
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
            
            Text(
                text = bloc.blocType.name.replace("_", " "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Bloc features
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FeatureItem("Common Tariff", "${bloc.commonTariff}%")
                FeatureItem("Free Movement", if (bloc.freeMovement) "Yes" else "No")
                FeatureItem("Common Currency", if (bloc.commonCurrency) "Yes" else "No")
            }
        }
    }
}

@Composable
fun SanctionCard(sanction: Sanction) {
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
                    text = sanction.targetCountryName,
                    style = MaterialTheme.typography.titleMedium
                )
                AssistChip(
                    onClick = { },
                    label = { Text("Severity: ${sanction.severity}/10") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                )
            }
            
            Text(
                text = sanction.sanctionType.name.replace("_", " "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            Text(
                text = sanction.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Econ. Impact", "${sanction.economicImpact}%")
                StatItem("Backlash", "${sanction.domesticBacklash.toInt()}%")
            }
        }
    }
}

@Composable
fun DiplomaticActionsCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Diplomatic Actions",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DiplomaticActionButton(
                    icon = Icons.Default.Phone,
                    label = "Call Leader",
                    onClick = { }
                )
                DiplomaticActionButton(
                    icon = Icons.Default.FlightTakeoff,
                    label = "Send Envoy",
                    onClick = { }
                )
                DiplomaticActionButton(
                    icon = Icons.Default.Handshake,
                    label = "Propose Deal",
                    onClick = { }
                )
                DiplomaticActionButton(
                    icon = Icons.Default.Warning,
                    label = "Sanction",
                    onClick = { }
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DiplomaticActionButton(
                    icon = Icons.Default.People,
                    label = "Assign Diplomat",
                    onClick = { }
                )
                DiplomaticActionButton(
                    icon = Icons.Default.Business,
                    label = "Trade Deal",
                    onClick = { }
                )
                DiplomaticActionButton(
                    icon = Icons.Default.Flag,
                    label = "Alliance",
                    onClick = { }
                )
                DiplomaticActionButton(
                    icon = Icons.Default.RemoveCircle,
                    label = "Expel",
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun RelationBadge(relationLevel: Int) {
    val (color, label) = when (relationLevel) {
        in 50..100 -> MaterialTheme.colorScheme.primary to "Ally"
        in 20..49 -> Color.Green to "Friendly"
        in -20..19 -> Color.Gray to "Neutral"
        in -49..-21 -> Color(0xFFFF9800) to "Tense"
        else -> MaterialTheme.colorScheme.error to "Hostile"
    }
    
    AssistChip(
        onClick = { },
        label = { Text(label) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = color
        )
    )
}

@Composable
fun FeatureItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DiplomaticActionButton(
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
            Icon(icon, contentDescription = label, modifier = Modifier.size(20.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

fun getRelationColor(relationLevel: Int): Color {
    return when (relationLevel) {
        in 50..100 -> MaterialTheme.colorScheme.primary
        in 20..49 -> Color.Green
        in -20..19 -> Color.Gray
        in -49..-21 -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.error
    }
}
