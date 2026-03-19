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
import com.country.simulator.ui.theme.success
import com.country.simulator.ui.theme.warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinistriesScreen(
    ministries: List<Ministry>,
    appointees: List<Appointee>,
    onAppointeeClick: (Appointee) -> Unit,
    onMinistryClick: (Ministry) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ministries & Cabinet") },
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
            // The 7 Key Appointees
            item {
                Text(
                    text = "Cabinet Appointees",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            
            items(appointees.take(7)) { appointee ->
                AppointeeCard(appointee, onAppointeeClick)
            }
            
            // All Ministries
            item {
                Text(
                    text = "Government Ministries",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            
            items(ministries) { ministry ->
                MinistryCard(ministry, onMinistryClick)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointeeCard(
    appointee: Appointee,
    onClick: (Appointee) -> Unit
) {
    Card(
        onClick = { onClick(appointee) },
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
                        text = appointee.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = appointee.position,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatWithProgressDetailed(
                    label = "Approval",
                    value = appointee.approvalRating.toInt(),
                    color = MaterialTheme.colorScheme.primary
                )
                StatWithProgressDetailed(
                    label = "Loyalty",
                    value = appointee.loyalty.toInt(),
                    color = MaterialTheme.colorScheme.tertiary
                )
                StatWithProgressDetailed(
                    label = "Competence",
                    value = appointee.competence.toInt(),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            // Additional info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ministry: ${appointee.ministry.name}",
                    style = MaterialTheme.typography.bodySmall
                )
                
                if (appointee.scandals > 0) {
                    AssistChip(
                        onClick = { },
                        label = { Text("${appointee.scandals} scandals") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinistryCard(
    ministry: Ministry,
    onClick: (Ministry) -> Unit
) {
    Card(
        onClick = { onClick(ministry) },
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
                Text(
                    text = ministry.name,
                    style = MaterialTheme.typography.titleMedium
                )
                
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            
            Text(
                text = "Minister: ${ministry.ministerName}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Budget usage
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Budget Usage")
                    Text("${(ministry.spentBudget / ministry.budget * 100).toInt()}%")
                }
                LinearProgressIndicator(
                    progress = (ministry.spentBudget / ministry.budget).toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Performance metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Efficiency", "${ministry.efficiency.toInt()}%")
                StatItem("Approval", "${ministry.publicApproval.toInt()}%")
                StatItem("Projects", "${ministry.activeProjects}")
            }
            
            // Corruption indicator
            if (ministry.corruptionLevel > 20) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Corruption Level",
                        style = MaterialTheme.typography.bodySmall
                    )
                    AssistChip(
                        onClick = { },
                        label = { Text("${ministry.corruptionLevel.toInt()}%") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = when (ministry.corruptionLevel) {
                                in 0.0..30.0 -> MaterialTheme.colorScheme.success
                                in 30.0..60.0 -> MaterialTheme.colorScheme.warning
                                else -> MaterialTheme.colorScheme.error
                            }
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun StatWithProgressDetailed(
    label: String,
    value: Int,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$value%",
            style = MaterialTheme.typography.titleMedium,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LinearProgressIndicator(
            progress = (value / 100f).coerceIn(0f, 1f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            color = color
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointeeDetailDialog(
    appointee: Appointee,
    onDismiss: () -> Unit,
    onReplace: () -> Unit,
    onReviewExpenses: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(appointee.name) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Position: ${appointee.position}",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Text(
                    text = "Ministry: ${appointee.ministry.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Divider()
                
                Text(
                    text = "Performance Metrics",
                    style = MaterialTheme.typography.titleSmall
                )
                
                StatRow("Approval Rating", "${appointee.approvalRating.toInt()}%")
                StatRow("Loyalty", "${appointee.loyalty.toInt()}%")
                StatRow("Competence", "${appointee.competence.toInt()}%")
                
                if (appointee.scandals > 0) {
                    StatRow("Scandals", "${appointee.scandals}")
                }
                
                Divider()
                
                Text(
                    text = "Qualifications",
                    style = MaterialTheme.typography.titleSmall
                )
                
                Text(
                    text = appointee.qualifications,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Button(onClick = onReplace) {
                Text("Replace")
            }
        },
        dismissButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = onReviewExpenses) {
                    Text("Review Expenses")
                }
                TextButton(onClick = onDismiss) {
                    Text("Close")
                }
            }
        }
    )
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
