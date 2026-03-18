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
 * Bus Schedule Management - Granular neighborhood-level control
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusSchedulesScreen(
    schedules: List<BusSchedule>,
    onScheduleClick: (BusSchedule) -> Unit,
    onAdjustFrequency: (BusSchedule, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedNeighborhood by remember { mutableStateOf<String?>(null) }
    var showAdjustDialog by remember { mutableStateOf(false) }
    var selectedSchedule by remember { mutableStateOf<BusSchedule?>(null) }
    
    val neighborhoods = schedules.map { it.neighborhood }.distinct()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bus Schedules") },
                subtitle = { Text("Neighborhood Transit Management") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Neighborhood filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedNeighborhood == null,
                    onClick = { selectedNeighborhood = null },
                    label = { Text("All") }
                )
                neighborhoods.forEach { neighborhood ->
                    FilterChip(
                        selected = selectedNeighborhood == neighborhood,
                        onClick = { selectedNeighborhood = neighborhood },
                        label = { Text(neighborhood.take(15)) }
                    )
                }
            }
            
            // Summary stats
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem("Total Routes", "${schedules.size}")
                    StatItem("Avg Frequency", "${schedules.map { it.frequency }.average().toInt()} min")
                    StatItem("Daily Riders", "${schedules.sumOf { it.averageRidership }}")
                }
            }
            
            // Schedules list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filteredSchedules = if (selectedNeighborhood != null) {
                    schedules.filter { it.neighborhood == selectedNeighborhood }
                } else {
                    schedules
                }
                
                items(filteredSchedules, key = { it.id }) { schedule ->
                    BusScheduleCard(
                        schedule = schedule,
                        onClick = { 
                            selectedSchedule = schedule
                            showAdjustDialog = true
                        }
                    )
                }
            }
        }
    }
    
    // Adjust frequency dialog
    if (showAdjustDialog && selectedSchedule != null) {
        AdjustFrequencyDialog(
            schedule = selectedSchedule!!,
            onDismiss = { 
                showAdjustDialog = false
                selectedSchedule = null
            },
            onAdjust = { newFrequency ->
                onAdjustFrequency(selectedSchedule!!, newFrequency)
                showAdjustDialog = false
                selectedSchedule = null
            }
        )
    }
}

@Composable
fun BusScheduleCard(schedule: BusSchedule, onClick: () -> Unit) {
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
                        text = "Route ${schedule.routeName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = schedule.neighborhood,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Surface(
                    color = if (schedule.isActive) MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = if (schedule.isActive) "Active" else "Inactive",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Every ${schedule.frequency} min",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                
                Text(
                    text = "${schedule.firstBus} - ${schedule.lastBus}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Riders: ${schedule.averageRidership}/day",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Fare: $${schedule.fare}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Schedule type chip
            AssistChip(
                onClick = { },
                label = { Text(schedule.scheduleType.name.replace("_", " ")) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Route,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun AdjustFrequencyDialog(
    schedule: BusSchedule,
    onDismiss: () -> Unit,
    onAdjust: (Int) -> Unit
) {
    var selectedFrequency by remember { mutableIntStateOf(schedule.frequency) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adjust Bus Frequency") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Route ${schedule.routeName} - ${schedule.neighborhood}",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Text(
                    text = "Current: Every ${schedule.frequency} minutes",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Divider()
                
                Text(
                    text = "New Frequency:",
                    style = MaterialTheme.typography.titleSmall
                )
                
                // Frequency options
                val options = listOf(5, 10, 15, 20, 30, 45, 60)
                options.forEach { minutes ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedFrequency == minutes,
                            onClick = { selectedFrequency = minutes }
                        )
                        Text(
                            text = "Every $minutes minutes",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                
                Divider()
                
                // Impact preview
                Text(
                    text = "Impact:",
                    style = MaterialTheme.typography.titleSmall
                )
                
                when {
                    selectedFrequency < schedule.frequency -> {
                        Text(
                            text = "• Increased service (+${schedule.averageRidership * 0.2} riders est.)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "• Higher operating cost (-$${(schedule.frequency - selectedFrequency) * 100}/day)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    selectedFrequency > schedule.frequency -> {
                        Text(
                            text = "• Reduced service (-${schedule.averageRidership * 0.15} riders est.)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "• Lower operating cost (+$${(selectedFrequency - schedule.frequency) * 100}/day)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onAdjust(selectedFrequency) }) {
                Text("Apply Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Work Permits Management - Individual permit approval
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkPermitsScreen(
    permits: List<WorkPermit>,
    onPermitClick: (WorkPermit) -> Unit,
    onApprove: (WorkPermit) -> Unit,
    onReject: (WorkPermit) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedStatus by remember { mutableStateOf<PermitStatus?>(null) }
    var showDecisionDialog by remember { mutableStateOf(false) }
    var selectedPermit by remember { mutableStateOf<WorkPermit?>(null) }
    
    val statuses = permits.map { it.status }.distinct()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Work Permits") },
                subtitle = { Text("Common Market & Foreign Workers") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    label = { Text("All") }
                )
                statuses.forEach { status ->
                    FilterChip(
                        selected = selectedStatus == status,
                        onClick = { selectedStatus = status },
                        label = { Text(status.name) }
                    )
                }
            }
            
            // Summary
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem("Pending", "${permits.count { it.status == PermitStatus.PENDING }}")
                    StatItem("Active", "${permits.count { it.status == PermitStatus.ACTIVE }}")
                    StatItem("High Talent", "${permits.count { it.isHighTalent }}")
                }
            }
            
            // Permits list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filteredPermits = if (selectedStatus != null) {
                    permits.filter { it.status == selectedStatus }
                } else {
                    permits
                }
                
                items(filteredPermits, key = { it.id }) { permit ->
                    WorkPermitCard(
                        permit = permit,
                        onClick = {
                            selectedPermit = permit
                            showDecisionDialog = true
                        }
                    )
                }
            }
        }
    }
    
    // Decision dialog
    if (showDecisionDialog && selectedPermit != null) {
        WorkPermitDecisionDialog(
            permit = selectedPermit!!,
            onDismiss = {
                showDecisionDialog = false
                selectedPermit = null
            },
            onApprove = {
                onApprove(selectedPermit!!)
                showDecisionDialog = false
                selectedPermit = null
            },
            onReject = {
                onReject(selectedPermit!!)
                showDecisionDialog = false
                selectedPermit = null
            }
        )
    }
}

@Composable
fun WorkPermitCard(permit: WorkPermit, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = if (permit.isHighTalent) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else {
            CardDefaults.cardColors()
        }
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
                        text = permit.applicantName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = permit.applicantNationality,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                StatusChip(permit.status.name)
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = permit.jobTitle,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = permit.industry,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Salary: $${permit.salary.toInt()}/year",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Dependents: ${permit.dependents}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (permit.isHighTalent) {
                    AssistChip(
                        onClick = { },
                        label = { Text("High Talent") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                
                Text(
                    text = "Expires: ${android.icu.text.SimpleDateFormat("MMM yyyy").java.util.Date(permit.endDate)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun WorkPermitDecisionDialog(
    permit: WorkPermit,
    onDismiss: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Review Work Permit") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Applicant info
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
                            Text("Applicant:", style = MaterialTheme.typography.bodyMedium)
                            Text(permit.applicantName, style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Nationality:", style = MaterialTheme.typography.bodyMedium)
                            Text(permit.applicantNationality, style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Position:", style = MaterialTheme.typography.bodyMedium)
                            Text("${permit.jobTitle} at ${permit.employerName}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Salary:", style = MaterialTheme.typography.bodyMedium)
                            Text("$${permit.salary.toInt()}/year", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                
                // Decision factors
                Text(
                    text = "Decision Factors:",
                    style = MaterialTheme.typography.titleSmall
                )
                
                if (permit.isHighTalent) {
                    Text(
                        text = "✓ High-talent worker (scientist/engineer)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Text(
                    text = "• Labor market impact: ${if (permit.salary > 50000) "Low" else "Medium"}",
                    style = MaterialTheme.typography.bodySmall
                )
                
                Text(
                    text = "• Tax contribution: +$${permit.salary * 0.25}/year estimated",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                if (permit.dependents > 0) {
                    Text(
                        text = "• ${permit.dependents} dependents will accompany",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onApprove) {
                Text("Approve Permit")
            }
        },
        dismissButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = onReject) {
                    Text("Reject")
                }
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}

/**
 * Medicine Stockpiles Management - Health resource tracking
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineStockpilesScreen(
    stockpiles: List<MedicineStockpile>,
    onStockpileClick: (MedicineStockpile) -> Unit,
    onRestock: (MedicineStockpile, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCriticalOnly by remember { mutableStateOf(false) }
    var showRestockDialog by remember { mutableStateOf(false) }
    var selectedStockpile by remember { mutableStateOf<MedicineStockpile?>(null) }
    
    val displayStockpiles = if (showCriticalOnly) {
        stockpiles.filter { it.isCritical || it.quantity < it.criticalLevel }
    } else {
        stockpiles
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Medicine Stockpiles") },
                subtitle = { Text("National Medical Reserves") },
                actions = {
                    Row {
                        Text(
                            text = "Critical Only",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Switch(
                            checked = showCriticalOnly,
                            onCheckedChange = { showCriticalOnly = it }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Summary stats
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
                        text = "Stockpile Overview",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("Total Items", "${stockpiles.size}")
                        StatItem("Critical", "${stockpiles.count { it.isCritical }}")
                        StatItem("Low Stock", "${stockpiles.count { it.quantity < it.criticalLevel }}")
                    }
                    
                    if (stockpiles.any { it.quantity < it.criticalLevel }) {
                        Divider()
                        
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = "${stockpiles.count { it.quantity < it.criticalLevel }} items below critical level",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }
            }
            
            // Stockpiles list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(displayStockpiles, key = { it.id }) { stockpile ->
                    MedicineStockpileCard(
                        stockpile = stockpile,
                        onClick = {
                            selectedStockpile = stockpile
                            showRestockDialog = true
                        }
                    )
                }
            }
        }
    }
    
    // Restock dialog
    if (showRestockDialog && selectedStockpile != null) {
        RestockDialog(
            stockpile = selectedStockpile!!,
            onDismiss = {
                showRestockDialog = false
                selectedStockpile = null
            },
            onRestock = { amount ->
                onRestock(selectedStockpile!!, amount)
                showRestockDialog = false
                selectedStockpile = null
            }
        )
    }
}

@Composable
fun MedicineStockpileCard(stockpile: MedicineStockpile, onClick: () -> Unit) {
    val isLowStock = stockpile.quantity < stockpile.criticalLevel
    val isExpiringSoon = stockpile.expiryDate < (System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000)
    
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = when {
            isLowStock -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            isExpiringSoon -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.warningContainer)
            stockpile.isCritical -> CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            else -> CardDefaults.cardColors()
        }
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
                        text = stockpile.medicineName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stockpile.medicineType,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (stockpile.isCritical) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Critical",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Quantity: ${stockpile.quantity} ${stockpile.unit}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Critical: ${stockpile.criticalLevel} ${stockpile.unit}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            // Stock level indicator
            val stockPercentage = (stockpile.quantity.toFloat() / stockpile.criticalLevel).coerceIn(0f, 2f)
            LinearProgressIndicator(
                progress = stockPercentage / 2,
                modifier = Modifier.fillMaxWidth(),
                color = when {
                    stockPercentage < 0.5 -> MaterialTheme.colorScheme.error
                    stockPercentage < 1.0 -> MaterialTheme.colorScheme.warning
                    else -> MaterialTheme.colorScheme.primary
                }
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Location: ${stockpile.storageLocation}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Expires: ${android.icu.text.SimpleDateFormat("MMM yyyy").java.util.Date(stockpile.expiryDate)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            // Warning badges
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isLowStock) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Low Stock") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    )
                }
                if (isExpiringSoon) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Expiring Soon") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.warning
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun RestockDialog(
    stockpile: MedicineStockpile,
    onDismiss: () -> Unit,
    onRestock: (Int) -> Unit
) {
    var restockAmount by remember { mutableIntStateOf(0) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Restock Medicine") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "${stockpile.medicineName} (${stockpile.unit})",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Text(
                    text = "Current Stock: ${stockpile.quantity} ${stockpile.unit}",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Text(
                    text = "Critical Level: ${stockpile.criticalLevel} ${stockpile.unit}",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                val deficit = (stockpile.criticalLevel - stockpile.quantity).coerceAtLeast(0)
                if (deficit > 0) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Deficit: ${deficit} ${stockpile.unit} below critical level",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
                
                Divider()
                
                Text(
                    text = "Restock Amount:",
                    style = MaterialTheme.typography.titleSmall
                )
                
                // Quick select buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(100, 500, 1000, 5000).forEach { amount ->
                        FilterChip(
                            selected = restockAmount == amount,
                            onClick = { restockAmount = amount },
                            label = { Text("+${amount / stockpile.unit.lowercase().removeSuffix("s")} ${stockpile.unit}") }
                        )
                    }
                }
                
                // Cost estimate
                if (restockAmount > 0) {
                    Text(
                        text = "Estimated Cost: $${restockAmount * 0.5}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onRestock(restockAmount) },
                enabled = restockAmount > 0
            ) {
                Text("Order Restock")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
