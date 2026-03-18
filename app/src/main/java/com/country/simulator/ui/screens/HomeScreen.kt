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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.country.simulator.model.MicroTask
import com.country.simulator.model.Notification
import com.country.simulator.model.PlayerCountry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    playerCountry: PlayerCountry?,
    onNavigateToScreen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Country Simulator") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { onNavigateToScreen(Screen.HOME) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Economy") },
                    label = { Text("Economy") },
                    selected = false,
                    onClick = { onNavigateToScreen(Screen.ECONOMY) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Policy, contentDescription = "Politics") },
                    label = { Text("Politics") },
                    selected = false,
                    onClick = { onNavigateToScreen(Screen.POLITICS) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Gavel, contentDescription = "Law") },
                    label = { Text("Law") },
                    selected = false,
                    onClick = { onNavigateToScreen(Screen.LAW) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Public, contentDescription = "Diplomacy") },
                    label = { Text("Diplomacy") },
                    selected = false,
                    onClick = { onNavigateToScreen(Screen.DIPLOMACY) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { onNavigateToScreen(Screen.SETTINGS) }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Country Header
            CountryHeader(playerCountry)
            
            // Stats Overview
            StatsOverview(playerCountry)
            
            // Quick Actions
            QuickActions(onNavigateToScreen)
        }
    }
}

@Composable
fun CountryHeader(playerCountry: PlayerCountry?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = playerCountry?.leaderName ?: "Loading...",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = playerCountry?.leaderTitle ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Approval Rating
            LinearProgressIndicator(
                progress = (playerCountry?.approvalRating ?: 0f) / 100f,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Approval: ${(playerCountry?.approvalRating ?: 0f).toInt()}%",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun StatsOverview(playerCountry: PlayerCountry?) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "National Statistics",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Stability", "${(playerCountry?.stability ?: 0f).toInt()}%")
                StatItem("Growth", "${(playerCountry?.growthRate ?: 0f).toString().take(4)}%")
                StatItem("Happiness", "${(playerCountry?.happinessIndex ?: 0f).toInt()}%")
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Inflation", "${(playerCountry?.inflationRate ?: 0f).toString().take(4)}%")
                StatItem("Unemployment", "${(playerCountry?.unemploymentRate ?: 0f).toString().take(4)}%")
                StatItem("Debt", formatNumber(playerCountry?.let { it.treasuryBalance }) ?: "N/A")
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
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
fun QuickActions(onNavigateToScreen: (Screen) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionButton(
                    icon = Icons.Default.AccountBalance,
                    label = "Budget",
                    onClick = { onNavigateToScreen(Screen.ECONOMY) }
                )
                QuickActionButton(
                    icon = Icons.Default.People,
                    label = "Cabinet",
                    onClick = { onNavigateToScreen(Screen.MINISTRIES) }
                )
                QuickActionButton(
                    icon = Icons.Default.Flag,
                    label = "Election",
                    onClick = { onNavigateToScreen(Screen.POLITICS) }
                )
                QuickActionButton(
                    icon = Icons.Default.Handshake,
                    label = "Trade",
                    onClick = { onNavigateToScreen(Screen.DIPLOMACY) }
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
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

fun formatNumber(value: Double?): String {
    return when {
        value == null -> "N/A"
        value >= 1_000_000_000 -> "$${value / 1_000_000_000}B"
        value >= 1_000_000 -> "$${value / 1_000_000}M"
        value >= 1_000 -> "$${value / 1_000}K"
        else -> "$$value"
    }
}

enum class Screen {
    HOME,
    ECONOMY,
    POLITICS,
    LAW,
    DIPLOMACY,
    MINISTRIES,
    INFRASTRUCTURE,
    DEMOGRAPHICS,
    SPORTS,
    SETTINGS
}
