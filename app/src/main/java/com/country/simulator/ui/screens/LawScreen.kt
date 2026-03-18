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
import com.country.simulator.model.Law
import com.country.simulator.model.Bill
import com.country.simulator.model.CourtCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LawScreen(
    laws: List<Law> = emptyList(),
    bills: List<Bill> = emptyList(),
    cases: List<CourtCase> = emptyList(),
    onNavigateToScreen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Laws", "Bills", "Court Cases")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Law & Justice") },
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
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            
            when (selectedTab) {
                0 -> LawsTab(laws)
                1 -> BillsTab(bills)
                2 -> CourtCasesTab(cases)
            }
        }
    }
}

@Composable
fun LawsTab(laws: List<Law>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Enacted Laws",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Manage and review laws passed by the legislature",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        if (laws.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Default.Gavel,
                    title = "No Laws",
                    description = "No laws have been enacted yet"
                )
            }
        } else {
            items(laws.size) { index ->
                LawCard(laws[index])
            }
        }
    }
}

@Composable
fun BillsTab(bills: List<Bill>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Pending Bills",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Review and vote on legislation in progress",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        if (bills.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Default.Description,
                    title = "No Pending Bills",
                    description = "All bills have been processed"
                )
            }
        } else {
            items(bills.size) { index ->
                BillCard(bills[index])
            }
        }
    }
}

@Composable
fun CourtCasesTab(cases: List<CourtCase>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Court Cases",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Review legal cases and judicial matters",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        if (cases.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Default.Balance,
                    title = "No Active Cases",
                    description = "No court cases require attention"
                )
            }
        } else {
            items(cases.size) { index ->
                CourtCaseCard(cases[index])
            }
        }
    }
}

@Composable
fun LawCard(law: Law) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = law.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = law.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text(law.lawType.name) }
                )
                AssistChip(
                    onClick = { },
                    label = { Text(law.status.name) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = when (law.status) {
                            com.country.simulator.model.LawStatus.ENACTED -> MaterialTheme.colorScheme.primary
                            com.country.simulator.model.LawStatus.VETOED -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                )
            }
        }
    }
}

@Composable
fun BillCard(bill: Bill) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = bill.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = bill.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = bill.billNumber,
                    style = MaterialTheme.typography.bodySmall
                )
                AssistChip(
                    onClick = { },
                    label = { Text(bill.currentStage.name) }
                )
            }
        }
    }
}

@Composable
fun CourtCaseCard(case: CourtCase) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = case.caseName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = case.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text(case.caseType.name) }
                )
                AssistChip(
                    onClick = { },
                    label = { Text(case.status.name) }
                )
            }
        }
    }
}

@Composable
fun EmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}
