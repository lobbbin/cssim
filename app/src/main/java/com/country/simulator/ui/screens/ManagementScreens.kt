package com.country.simulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.country.simulator.model.*
import com.country.simulator.ui.components.*

/**
 * NPCs Management Screen - Handle 1000+ NPCs with pagination
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NPCsManagementScreen(
    npcs: LazyPagingItems<NPC>,
    onNPCClick: (NPC) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf<OccupationType?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Population Management") },
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
                    selected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = selectedFilter == OccupationType.BUSINESS_OWNER,
                    onClick = { selectedFilter = OccupationType.BUSINESS_OWNER },
                    label = { Text("Business") }
                )
                FilterChip(
                    selected = selectedFilter == OccupationType.POLITICIAN,
                    onClick = { selectedFilter = OccupationType.POLITICIAN },
                    label = { Text("Political") }
                )
                FilterChip(
                    selected = selectedFilter == OccupationType.CORPORATE_EXEC,
                    onClick = { selectedFilter = OccupationType.CORPORATE_EXEC },
                    label = { Text("Corporate") }
                )
            }
            
            // Stats summary
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
                    StatItem("Total NPCs", "${npcs.itemCount}")
                    StatItem("Important", "50")
                    StatItem("Avg Approval", "62%")
                }
            }
            
            // Paginated list
            PaginatedNPCList(
                npcs = npcs,
                onNPCClick = onNPCClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Business License Management Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessLicensesScreen(
    businesses: LazyPagingItems<Business>,
    onBusinessClick: (Business) -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterMenu by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf<LicenseStatus?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Business Licenses") },
                actions = {
                    IconButton(onClick = { showFilterMenu = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
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
            // Quick stats
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
                        text = "License Overview",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("Active", "156")
                        StatItem("Pending", "23")
                        StatItem("Under Audit", "5")
                    }
                }
            }
            
            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalIconButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.AddBusiness, contentDescription = null)
                        Text("New License", style = MaterialTheme.typography.labelSmall)
                    }
                }
                FilledTonalIconButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.FactCheck, contentDescription = null)
                        Text("Random Audit", style = MaterialTheme.typography.labelSmall)
                    }
                }
                FilledTonalIconButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.TrendingUp, contentDescription = null)
                        Text("Review Tax", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            
            // Paginated business list
            PaginatedBusinessList(
                businesses = businesses,
                onBusinessClick = onBusinessClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
    
    // Filter dropdown menu
    if (showFilterMenu) {
        DropdownMenu(
            expanded = showFilterMenu,
            onDismissRequest = { showFilterMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("All Statuses") },
                onClick = {
                    selectedStatus = null
                    showFilterMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Active") },
                onClick = {
                    selectedStatus = LicenseStatus.ACTIVE
                    showFilterMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Pending") },
                onClick = {
                    selectedStatus = LicenseStatus.PENDING
                    showFilterMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text("Under Audit") },
                onClick = {
                    showFilterMenu = false
                }
            )
        }
    }
}

/**
 * Immigration Cases Management Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImmigrationScreen(
    cases: LazyPagingItems<ImmigrationCase>,
    onCaseClick: (ImmigrationCase) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Immigration Cases") },
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
            // Immigration stats
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
                        text = "Immigration Overview",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("Pending", "234")
                        StatItem("Refugees", "89")
                        StatItem("High Talent", "45")
                    }
                    
                    Divider()
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Quota Remaining:")
                        Text(
                            text = "1,234 / 5,000",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    LinearProgressIndicator(
                        progress = 0.25f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Quick actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalIconButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null)
                        Text("Fast Track", style = MaterialTheme.typography.labelSmall)
                    }
                }
                FilledTonalIconButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.Gavel, contentDescription = null)
                        Text("Bulk Approve", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            
            // Paginated cases list
            PaginatedImmigrationCases(
                cases = cases,
                onCaseClick = onCaseClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Purchase Orders Management Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseOrdersScreen(
    orders: LazyPagingItems<PurchaseOrder>,
    onOrderClick: (PurchaseOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Purchase Orders") },
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
            // Budget summary
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
                            text = "Pending Orders",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("Pending", "23")
                        StatItem("Suspicious", "3")
                        StatItem("Urgent", "5")
                    }
                }
            }
            
            // Paginated orders
            PaginatedPurchaseOrders(
                orders = orders,
                onOrderClick = onOrderClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Welfare Payments Management Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelfarePaymentsScreen(
    payments: LazyPagingItems<WelfarePayment>,
    onPaymentClick: (WelfarePayment) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welfare Payments") },
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
            // Welfare stats
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
                        text = "Welfare Program Overview",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("Recipients", "45,678")
                        StatItem("Monthly Cost", "$12.5M")
                        StatItem("Fraud Rate", "2.3%")
                    }
                    
                    Divider()
                    
                    Text(
                        text = "By Category",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    WelfareCategoryRow("Unemployment", "12,345", "$4.2M")
                    WelfareCategoryRow("Pensions", "23,456", "$6.8M")
                    WelfareCategoryRow("Disability", "9,877", "$1.5M")
                }
            }
            
            // Paginated payments
            PaginatedWelfarePayments(
                payments = payments,
                onPaymentClick = onPaymentClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun WelfareCategoryRow(category: String, recipients: String, cost: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.bodySmall
        )
        Row {
            Text(
                text = recipients,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = cost,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Stadium Vendors Management Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StadiumVendorsScreen(
    vendors: LazyPagingItems<StadiumVendor>,
    onVendorClick: (StadiumVendor) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stadium Vendors") },
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
            // Vendor stats
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
                        text = "Vendor Management",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("Total Vendors", "${vendors.itemCount}")
                        StatItem("Avg Rating", "78%")
                        StatItem("Revenue", "$2.3M")
                    }
                }
            }
            
            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalIconButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Text("Add Vendor", style = MaterialTheme.typography.labelSmall)
                    }
                }
                FilledTonalIconButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.RateReview, contentDescription = null)
                        Text("Review", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            
            // Paginated vendors
            PaginatedStadiumVendors(
                vendors = vendors,
                onVendorClick = onVendorClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * National Sports Teams Management Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NationalTeamsScreen(
    players: LazyPagingItems<Player>,
    onPlayerClick: (Player) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSport by remember { mutableStateOf<SportType?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("National Teams") },
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
            // Sport filter
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedSport == null,
                        onClick = { selectedSport = null },
                        label = { Text("All Sports") }
                    )
                }
                SportType.entries.forEach { sport ->
                    item {
                        FilterChip(
                            selected = selectedSport == sport,
                            onClick = { selectedSport = sport },
                            label = { Text(sport.name) }
                        )
                    }
                }
            }
            
            // Team stats
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Team Overview",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("Total Players", "${players.itemCount}")
                        StatItem("Avg Performance", "72%")
                        StatItem("Suspended", "2")
                    }
                }
            }
            
            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalIconButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.SportsSoccer, contentDescription = null)
                        Text("Select Squad", style = MaterialTheme.typography.labelSmall)
                    }
                }
                FilledTonalIconButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.MedicalServices, contentDescription = null)
                        Text("Medical", style = MaterialTheme.typography.labelSmall)
                    }
                }
                FilledTonalIconButton(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(Icons.Default.Psychology, contentDescription = null)
                        Text("Morale", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            
            // Paginated players
            PaginatedPlayersList(
                players = players,
                onPlayerClick = onPlayerClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
