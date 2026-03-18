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

/**
 * Interactive Event Resolution Screen
 * Player makes choices that affect the nation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventResolutionScreen(
    event: GameEvent,
    onResolve: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = when (event.eventType) {
                    GameEventType.CRISIS -> Icons.Default.Warning
                    GameEventType.OPPORTUNITY -> Icons.Default.TrendingUp
                    GameEventType.DISASTER -> Icons.Default.Error
                    GameEventType.BREAKTHROUGH -> Icons.Default.Lightbulb
                    GameEventType.SCANDAL -> Icons.Default.Report
                    GameEventType.PROTEST -> Icons.Default.People
                    GameEventType.DIPLOMATIC_INCIDENT -> Icons.Default.Public
                    GameEventType.ECONOMIC_SHIFT -> Icons.Default.Assessment
                    GameEventType.SOCIAL_CHANGE -> Icons.Default.SocialDistance
                    GameEventType.TECHNOLOGY -> Icons.Default.Science
                    GameEventType.CULTURAL -> Icons.Default.ArtTrack
                    GameEventType.ELECTION -> Icons.Default.HowToVote
                },
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = when (event.eventType) {
                    GameEventType.CRISIS, GameEventType.DISASTER -> MaterialTheme.colorScheme.error
                    GameEventType.OPPORTUNITY, GameEventType.BREAKTHROUGH -> MaterialTheme.colorScheme.primary
                    GameEventType.SCANDAL -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.secondary
                }
            )
        },
        title = {
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Event description
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyLarge
                )
                
                // Event details
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
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
                            Text("Priority:", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = "${event.priority}/10",
                                style = MaterialTheme.typography.bodyMedium,
                                color = when (event.priority) {
                                    in 8..10 -> MaterialTheme.colorScheme.error
                                    in 5..7 -> MaterialTheme.colorScheme.tertiary
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Category:", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                text = event.category.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        if (event.expiryDate != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Expires in:", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = formatTimeRemaining(event.expiryDate!!),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
                
                // Options
                Text(
                    text = "Choose your response:",
                    style = MaterialTheme.typography.titleMedium
                )
                
                event.options.split("|").forEach { option ->
                    OptionCard(
                        option = option.trim(),
                        selected = selectedOption == option.trim(),
                        onClick = { selectedOption = option.trim() }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { selectedOption?.let { onResolve(it) } },
                enabled = selectedOption != null
            ) {
                Text("Confirm Decision")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Consider Later")
            }
        }
    )
}

@Composable
fun OptionCard(
    option: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = if (selected) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = option,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            RadioButton(
                selected = selected,
                onClick = onClick
            )
        }
    }
}

/**
 * Cabinet Meeting Screen - Interactive decision making
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CabinetMeetingScreen(
    appointees: List<Appointee>,
    ministries: List<Ministry>,
    onAppointeeClick: (Appointee) -> Unit,
    onMinistryClick: (Ministry) -> Unit,
    onAllocateBudget: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMinistry by remember { mutableStateOf<Ministry?>(null) }
    var showBudgetDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cabinet Meeting") },
                subtitle = { Text("Weekly executive briefing") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBudgetDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Allocate Budget")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Meeting summary
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Today's Agenda",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    AgendaItem("Budget Review", "3 ministries pending", Icons.Default.Assessment)
                    AgendaItem("Policy Approvals", "5 proposals awaiting", Icons.Default.Policy)
                    AgendaItem("Crisis Response", "2 active situations", Icons.Default.Warning)
                    AgendaItem("Appointments", "1 position vacant", Icons.Default.PersonAdd)
                }
            }
            
            // Key Appointees
            Text(
                text = "Key Appointees",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(appointees) { appointee ->
                    AppointeeBriefingCard(
                        appointee = appointee,
                        onClick = { onAppointeeClick(appointee) }
                    )
                }
            }
        }
    }
    
    if (showBudgetDialog) {
        BudgetAllocationDialog(
            ministries = ministries,
            onDismiss = { showBudgetDialog = false },
            onAllocate = onAllocateBudget
        )
    }
}

@Composable
fun AgendaItem(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AppointeeBriefingCard(
    appointee: Appointee,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = appointee.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = appointee.position,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Surface(
                    color = when {
                        appointee.approvalRating >= 70 -> MaterialTheme.colorScheme.primary
                        appointee.approvalRating >= 40 -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.error
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "${appointee.approvalRating.toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            // Performance metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MetricItem("Loyalty", appointee.loyalty.toInt())
                MetricItem("Competence", appointee.competence.toInt())
                MetricItem("Scandals", appointee.scandals, isError = appointee.scandals > 0)
            }
            
            // Ministry info
            Text(
                text = "Manages: ${appointee.ministry.name}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun MetricItem(label: String, value: Int, isError: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (isError && value > 0) "$value!" else "$value",
            style = MaterialTheme.typography.titleSmall,
            color = if (isError && value > 0) MaterialTheme.colorScheme.error 
                    else MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun BudgetAllocationDialog(
    ministries: List<Ministry>,
    onDismiss: () -> Unit,
    onAllocate: () -> Unit
) {
    var allocations by remember { mutableStateOf<Map<String, Double>>(emptyMap()) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Allocate Ministry Budgets") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Distribute budget across ministries",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                ministries.forEach { ministry ->
                    BudgetSlider(
                        ministry = ministry,
                        onValueChange = { value ->
                            allocations = allocations + (ministry.name to value)
                        }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onAllocate()
                onDismiss()
            }) {
                Text("Allocate")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun BudgetSlider(
    ministry: Ministry,
    onValueChange: (Double) -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf((ministry.allocatedBudget / ministry.budget).toFloat()) }
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = ministry.name,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${(sliderPosition * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Slider(
            value = sliderPosition,
            onValueChange = { 
                sliderPosition = it
                onValueChange(it * ministry.budget)
            },
            valueRange = 0f..1f,
            steps = 19 // 5% increments
        )
    }
}

/**
 * National Dashboard - Real-time nation status
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NationalDashboard(
    playerCountry: PlayerCountry?,
    resources: NationalResources?,
    notifications: List<Notification>,
    activeTasks: List<MicroTask>,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Tasks", "Alerts")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("National Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            
            when (selectedTab) {
                0 -> OverviewTab(playerCountry, resources)
                1 -> TasksTab(activeTasks)
                2 -> AlertsTab(notifications)
            }
        }
    }
}

@Composable
fun OverviewTab(
    playerCountry: PlayerCountry?,
    resources: NationalResources?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Approval & Stability
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Public Support",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    StatWithProgress(
                        label = "Approval Rating",
                        value = (playerCountry?.approvalRating ?: 0f).toInt(),
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    StatWithProgress(
                        label = "Stability",
                        value = (playerCountry?.stability ?: 0f).toInt(),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
        
        // Economic Indicators
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Economy",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        EconomicIndicator("GDP Growth", "${(playerCountry?.growthRate ?: 0f).toString().take(4)}%")
                        EconomicIndicator("Inflation", "${(playerCountry?.inflationRate ?: 0f).toString().take(4)}%")
                        EconomicIndicator("Unemployment", "${(playerCountry?.unemploymentRate ?: 0f).toString().take(4)}%")
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        EconomicIndicator("Treasury", formatCurrency(resources?.treasuryBalance ?: 0.0))
                        EconomicIndicator("Debt", formatCurrency(resources?.nationalDebt ?: 0.0))
                    }
                }
            }
        }
        
        // Social Indicators
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Society",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        EconomicIndicator("Happiness", "${(playerCountry?.happinessIndex ?: 0f).toInt()}%")
                        EconomicIndicator("Health", "${(playerCountry?.healthIndex ?: 0f).toInt()}%")
                        EconomicIndicator("Education", "${(playerCountry?.educationIndex ?: 0f).toInt()}%")
                    }
                }
            }
        }
    }
}

@Composable
fun TasksTab(activeTasks: List<MicroTask>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Pending Tasks (${activeTasks.size})",
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        items(activeTasks) { task ->
            TaskSummaryCard(task)
        }
        
        if (activeTasks.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No pending tasks",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun TaskSummaryCard(task: MicroTask) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = when (task.priority) {
            Priority.URGENT -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            Priority.HIGH -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            else -> CardDefaults.cardColors()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = task.category.name,
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

@Composable
fun AlertsTab(notifications: List<Notification>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Notifications (${notifications.size})",
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        items(notifications) { notification ->
            NotificationCard(notification)
        }
    }
}

@Composable
fun NotificationCard(notification: Notification) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = when (notification.notificationType) {
            NotificationType.ALERT -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            NotificationType.WARNING -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.warningContainer)
            NotificationType.INFO -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            else -> CardDefaults.cardColors()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (notification.notificationType) {
                    NotificationType.ALERT -> Icons.Default.Error
                    NotificationType.WARNING -> Icons.Default.Warning
                    NotificationType.INFO -> Icons.Default.Info
                    NotificationType.TASK -> Icons.Default.TaskAlt
                    NotificationType.CRISIS -> Icons.Default.Emergency
                    else -> Icons.Default.Notifications
                },
                contentDescription = null,
                tint = when (notification.notificationType) {
                    NotificationType.ALERT, NotificationType.CRISIS -> MaterialTheme.colorScheme.onErrorContainer
                    NotificationType.WARNING -> MaterialTheme.colorScheme.onWarningContainer
                    NotificationType.INFO -> MaterialTheme.colorScheme.onPrimaryContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = when (notification.notificationType) {
                        NotificationType.ALERT, NotificationType.CRISIS -> MaterialTheme.colorScheme.onErrorContainer
                        NotificationType.WARNING -> MaterialTheme.colorScheme.onWarningContainer
                        NotificationType.INFO -> MaterialTheme.colorScheme.onPrimaryContainer
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            
            if (!notification.isRead) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.size(8.dp)
                ) {
                }
            }
        }
    }
}

@Composable
fun EconomicIndicator(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
fun StatWithProgress(label: String, value: Int, color: androidx.compose.ui.graphics.Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label)
            Text("$value%")
        }
        LinearProgressIndicator(
            progress = (value / 100f).coerceIn(0f, 1f),
            modifier = Modifier.fillMaxWidth(),
            color = color
        )
    }
}

fun formatTimeRemaining(expiryDate: Long): String {
    val remaining = expiryDate - System.currentTimeMillis()
    val hours = remaining / (1000 * 60 * 60)
    val minutes = (remaining % (1000 * 60 * 60)) / (1000 * 60)
    
    return when {
        hours > 24 -> "${hours / 24}d ${hours % 24}h"
        hours > 0 -> "${hours}h ${minutes}m"
        else -> "${minutes}m"
    }
}

fun formatCurrency(value: Double): String {
    return when {
        value >= 1_000_000_000_000 -> "$${value / 1_000_000_000_000}T"
        value >= 1_000_000_000 -> "$${value / 1_000_000_000}B"
        value >= 1_000_000 -> "$${value / 1_000_000}M"
        value >= 1_000 -> "$${value / 1_000}K"
        else -> "$$value"
    }
}
