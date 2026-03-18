package com.country.simulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfrastructureScreen(
    onNavigateToScreen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Infrastructure") },
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
            item {
                InfrastructureCategoryCard(
                    icon = Icons.Default.ElectricBolt,
                    title = "Power Grid",
                    description = "Manage power stations and energy distribution",
                    onClick = { }
                )
            }
            
            item {
                InfrastructureCategoryCard(
                    icon = Icons.Default.DirectionsCar,
                    title = "Transportation",
                    description = "Highways, railways, and public transit",
                    onClick = { }
                )
            }
            
            item {
                InfrastructureCategoryCard(
                    icon = Icons.Default.Construction,
                    title = "Construction Projects",
                    description = "Ongoing and planned infrastructure projects",
                    onClick = { }
                )
            }
            
            item {
                InfrastructureCategoryCard(
                    icon = Icons.Default.WaterDrop,
                    title = "Utilities",
                    description = "Water, sewage, and waste management",
                    onClick = { }
                )
            }
            
            item {
                InfrastructureCategoryCard(
                    icon = Icons.Default.Wifi,
                    title = "Telecommunications",
                    description = "Network infrastructure and connectivity",
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun InfrastructureCategoryCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemographicsScreen(
    onNavigateToScreen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Demographics") },
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
            item {
                DemographicsOverviewCard()
            }
            
            item {
                ImmigrationCard()
            }
            
            item {
                HealthProgramsCard()
            }
            
            item {
                SocialGroupsCard()
            }
            
            item {
                WelfareCard()
            }
        }
    }
}

@Composable
fun DemographicsOverviewCard() {
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
            Text(
                text = "Population Overview",
                style = MaterialTheme.typography.titleLarge
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Total", "12.5M")
                StatItem("Growth", "+1.2%")
                StatItem("Urban", "68%")
            }
            
            Divider()
            
            Text(
                text = "Age Distribution",
                style = MaterialTheme.typography.titleSmall
            )
            
            DemographicBar("0-14", 0.25f)
            DemographicBar("15-64", 0.65f)
            DemographicBar("65+", 0.10f)
        }
    }
}

@Composable
fun ImmigrationCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
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
                    text = "Immigration",
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Pending", "1,234")
                StatItem("Approved", "5,678")
                StatItem("Rejected", "234")
            }
        }
    }
}

@Composable
fun HealthProgramsCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Health Programs",
                style = MaterialTheme.typography.titleMedium
            )
            
            HealthProgramRow("Vaccination", "85%", true)
            HealthProgramRow("Disease Control", "92%", true)
            HealthProgramRow("Maternal Health", "78%", true)
        }
    }
}

@Composable
fun SocialGroupsCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Social Groups",
                style = MaterialTheme.typography.titleMedium
            )
            
            SocialGroupRow("Working Class", "45%", "Stable")
            SocialGroupRow("Middle Class", "35%", "Growing")
            SocialGroupRow("Upper Class", "5%", "Stable")
            SocialGroupRow("Minorities", "15%", "Integration")
        }
    }
}

@Composable
fun WelfareCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Welfare Programs",
                style = MaterialTheme.typography.titleMedium
            )
            
            WelfareRow("Unemployment", "23,456 recipients")
            WelfareRow("Pensions", "145,678 recipients")
            WelfareRow("Disability", "34,567 recipients")
        }
    }
}

@Composable
fun DemographicBar(label: String, proportion: Float) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall)
            Text("${(proportion * 100).toInt()}%", style = MaterialTheme.typography.bodySmall)
        }
        LinearProgressIndicator(
            progress = proportion,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun HealthProgramRow(name: String, coverage: String, active: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name)
        Row {
            if (active) {
                AssistChip(
                    onClick = { },
                    label = { Text("Active") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Text(coverage)
        }
    }
}

@Composable
fun SocialGroupRow(name: String, percentage: String, status: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name)
        Row {
            Text(percentage, modifier = Modifier.padding(end = 8.dp))
            AssistChip(
                onClick = { },
                label = { Text(status) }
            )
        }
    }
}

@Composable
fun WelfareRow(program: String, recipients: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(program)
        Text(recipients, color = MaterialTheme.colorScheme.primary)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportsScreen(
    onNavigateToScreen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sports & Culture") },
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
            item {
                SportsCategoryCard(
                    icon = Icons.Default.SportsSoccer,
                    title = "National Teams",
                    description = "Manage national sports teams and players",
                    onClick = { }
                )
            }
            
            item {
                SportsCategoryCard(
                    icon = Icons.Default.Stadium,
                    title = "Stadiums & Facilities",
                    description = "Build and manage sports venues",
                    onClick = { }
                )
            }
            
            item {
                SportsCategoryCard(
                    icon = Icons.Default.EmojiEvents,
                    title = "Events & Tournaments",
                    description = "Host and participate in sports events",
                    onClick = { }
                )
            }
            
            item {
                SportsCategoryCard(
                    icon = Icons.Default.FitnessCenter,
                    title = "Fitness Programs",
                    description = "Promote public health and fitness",
                    onClick = { }
                )
            }
            
            item {
                SportsCategoryCard(
                    icon = Icons.Default.ArtTrack,
                    title = "Cultural Programs",
                    description = "Support arts and cultural initiatives",
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun SportsCategoryCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToScreen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
            item {
                SettingsCategory("Game Settings") {
                    SettingsItem("Game Speed", "Normal", onClick = { })
                    SettingsItem("Difficulty", "Medium", onClick = { })
                    SettingsItem("Notifications", "On", onClick = { })
                    SettingsItem("Auto-Save", "On", onClick = { })
                }
            }
            
            item {
                SettingsCategory("Display") {
                    SettingsItem("Theme", "System", onClick = { })
                    SettingsItem("Font Size", "Medium", onClick = { })
                }
            }
            
            item {
                SettingsCategory("Data") {
                    SettingsItem("Save Game", "", onClick = { })
                    SettingsItem("Load Game", "", onClick = { })
                    SettingsItem("New Game", "", onClick = { })
                }
            }
            
            item {
                SettingsCategory("About") {
                    SettingsItem("Version", "1.0.0", onClick = { })
                    SettingsItem("Credits", "", onClick = { })
                }
            }
        }
    }
}

@Composable
fun SettingsCategory(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}

@Composable
fun SettingsItem(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (value.isNotEmpty()) {
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
