package com.country.simulator.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.country.simulator.model.*

/**
 * Paginated NPC List Component
 * Uses Paging 3 for efficient loading of 1000+ NPCs
 */
@Composable
fun PaginatedNPCList(
    npcs: LazyPagingItems<NPC>,
    onNPCClick: (NPC) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                count = npcs.itemCount,
                key = { index -> npcs[index]?.id ?: index }
            ) { index ->
                val npc = npcs[index]
                if (npc != null) {
                    NPCCard(npc = npc, onClick = { onNPCClick(npc) })
                }
            }
            
            npcs.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    loadState.append is LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    loadState.refresh is LoadState.Error -> {
                        item { ErrorItem((loadState.refresh as LoadState.Error).error) }
                    }
                    loadState.append is LoadState.Error -> {
                        item { ErrorItem((loadState.append as LoadState.Error).error) }
                    }
                }
            }
        }
    }
}

@Composable
fun NPCCard(npc: NPC, onClick: () -> Unit) {
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
                        text = npc.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${npc.occupation} • Age ${npc.age}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (npc.isImportant) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Important",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Income: $${npc.income.toInt()}",
                    style = MaterialTheme.typography.bodySmall
                )
                ApprovalIndicator(npc.approvalOfGovernment.toInt())
            }
            
            // Approval progress
            LinearProgressIndicator(
                progress = (npc.approvalOfGovernment / 100).toFloat(),
                modifier = Modifier.fillMaxWidth(),
                color = getApprovalColor(npc.approvalOfGovernment)
            )
        }
    }
}

/**
 * Paginated Business List
 */
@Composable
fun PaginatedBusinessList(
    businesses: LazyPagingItems<Business>,
    onBusinessClick: (Business) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                count = businesses.itemCount,
                key = { index -> businesses[index]?.id ?: index }
            ) { index ->
                val business = businesses[index]
                if (business != null) {
                    BusinessCard(business = business, onClick = { onBusinessClick(business) })
                }
            }
            
            businesses.apply {
                when {
                    loadState.refresh is LoadState.Loading -> item { LoadingItem() }
                    loadState.append is LoadState.Loading -> item { LoadingItem() }
                    loadState.refresh is LoadState.Error -> item { ErrorItem((loadState.refresh as LoadState.Error).error) }
                    loadState.append is LoadState.Error -> item { ErrorItem((loadState.append as LoadState.Error).error) }
                }
            }
        }
    }
}

@Composable
fun BusinessCard(business: Business, onClick: () -> Unit) {
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
                        text = business.businessName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = business.industry,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                LicenseStatusBadge(business.licenseStatus)
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Employees: ${business.employees}")
                Text("Revenue: $${business.revenue / 1_000_000}M")
            }
            
            if (business.isUnderAudit) {
                AssistChip(
                    onClick = { },
                    label = { Text("Under Audit") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                )
            }
        }
    }
}

/**
 * Paginated Immigration Cases List
 */
@Composable
fun PaginatedImmigrationCases(
    cases: LazyPagingItems<ImmigrationCase>,
    onCaseClick: (ImmigrationCase) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                count = cases.itemCount,
                key = { index -> cases[index]?.id ?: index }
            ) { index ->
                val case = cases[index]
                if (case != null) {
                    ImmigrationCaseCard(case = case, onClick = { onCaseClick(case) })
                }
            }
            
            cases.apply {
                when {
                    loadState.refresh is LoadState.Loading -> item { LoadingItem() }
                    loadState.append is LoadState.Loading -> item { LoadingItem() }
                    loadState.refresh is LoadState.Error -> item { ErrorItem((loadState.refresh as LoadState.Error).error) }
                    loadState.append is LoadState.Error -> item { ErrorItem((loadState.append as LoadState.Error).error) }
                }
            }
        }
    }
}

@Composable
fun ImmigrationCaseCard(case: ImmigrationCase, onClick: () -> Unit) {
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
                        text = case.applicantName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = case.applicantNationality,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                ImmigrationTypeBadge(case.applicationType)
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Family: ${case.familyMembers}",
                    style = MaterialTheme.typography.bodySmall
                )
                StatusChip(case.status.name)
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (case.isRefugee) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Refugee") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    )
                }
                if (case.isHighTalent) {
                    AssistChip(
                        onClick = { },
                        label = { Text("High Talent") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    }
}

/**
 * Paginated Purchase Orders List
 */
@Composable
fun PaginatedPurchaseOrders(
    orders: LazyPagingItems<PurchaseOrder>,
    onOrderClick: (PurchaseOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                count = orders.itemCount,
                key = { index -> orders[index]?.id ?: index }
            ) { index ->
                val order = orders[index]
                if (order != null) {
                    PurchaseOrderCard(order = order, onClick = { onOrderClick(order) })
                }
            }
            
            orders.apply {
                when {
                    loadState.refresh is LoadState.Loading -> item { LoadingItem() }
                    loadState.append is LoadState.Loading -> item { LoadingItem() }
                    loadState.refresh is LoadState.Error -> item { ErrorItem((loadState.refresh as LoadState.Error).error) }
                    loadState.append is LoadState.Error -> item { ErrorItem((loadState.append as LoadState.Error).error) }
                }
            }
        }
    }
}

@Composable
fun PurchaseOrderCard(order: PurchaseOrder, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = if (order.isSuspicious) CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ) else CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.orderNumber,
                    style = MaterialTheme.typography.titleMedium
                )
                StatusChip(order.status.name)
            }
            
            Text(
                text = order.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Vendor: ${order.vendor}")
                Text("Amount: $${order.amount / 1_000}K")
            }
            
            if (order.isSuspicious) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Flagged for Review",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            if (order.isUrgent) {
                AssistChip(
                    onClick = { },
                    label = { Text("Urgent") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    }
}

/**
 * Paginated Welfare Payments List
 */
@Composable
fun PaginatedWelfarePayments(
    payments: LazyPagingItems<WelfarePayment>,
    onPaymentClick: (WelfarePayment) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                count = payments.itemCount,
                key = { index -> payments[index]?.id ?: index }
            ) { index ->
                val payment = payments[index]
                if (payment != null) {
                    WelfarePaymentCard(payment = payment, onClick = { onPaymentClick(payment) })
                }
            }
            
            payments.apply {
                when {
                    loadState.refresh is LoadState.Loading -> item { LoadingItem() }
                    loadState.append is LoadState.Loading -> item { LoadingItem() }
                    loadState.refresh is LoadState.Error -> item { ErrorItem((loadState.refresh as LoadState.Error).error) }
                    loadState.append is LoadState.Error -> item { ErrorItem((loadState.append as LoadState.Error).error) }
                }
            }
        }
    }
}

@Composable
fun WelfarePaymentCard(payment: WelfarePayment, onClick: () -> Unit) {
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
                        text = payment.recipientName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = payment.paymentType.name.replace("_", " "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                StatusChip(payment.status.name)
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Amount: $${payment.amount}/month")
                Text("Total Paid: $${payment.totalPaid / 1_000}K")
            }
        }
    }
}

/**
 * Paginated Stadium Vendors List
 */
@Composable
fun PaginatedStadiumVendors(
    vendors: LazyPagingItems<StadiumVendor>,
    onVendorClick: (StadiumVendor) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                count = vendors.itemCount,
                key = { index -> vendors[index]?.id ?: index }
            ) { index ->
                val vendor = vendors[index]
                if (vendor != null) {
                    StadiumVendorCard(vendor = vendor, onClick = { onVendorClick(vendor) })
                }
            }
            
            vendors.apply {
                when {
                    loadState.refresh is LoadState.Loading -> item { LoadingItem() }
                    loadState.append is LoadState.Loading -> item { LoadingItem() }
                    loadState.refresh is LoadState.Error -> item { ErrorItem((loadState.refresh as LoadState.Error).error) }
                    loadState.append is LoadState.Error -> item { ErrorItem((loadState.append as LoadState.Error).error) }
                }
            }
        }
    }
}

@Composable
fun StadiumVendorCard(vendor: StadiumVendor, onClick: () -> Unit) {
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
                        text = vendor.vendorName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = vendor.vendorType.name.replace("_", " "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = "Rating: ${vendor.customerRating.toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Text(
                text = "Stadium: ${vendor.stadiumName}",
                style = MaterialTheme.typography.bodySmall
            )
            
            Text(
                text = "Location: ${vendor.location}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * Paginated Players List (Sports Teams)
 */
@Composable
fun PaginatedPlayersList(
    players: LazyPagingItems<Player>,
    onPlayerClick: (Player) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                count = players.itemCount,
                key = { index -> players[index]?.id ?: index }
            ) { index ->
                val player = players[index]
                if (player != null) {
                    PlayerCard(player = player, onClick = { onPlayerClick(player) })
                }
            }
            
            players.apply {
                when {
                    loadState.refresh is LoadState.Loading -> item { LoadingItem() }
                    loadState.append is LoadState.Loading -> item { LoadingItem() }
                    loadState.refresh is LoadState.Error -> item { ErrorItem((loadState.refresh as LoadState.Error).error) }
                    loadState.append is LoadState.Error -> item { ErrorItem((loadState.append as LoadState.Error).error) }
                }
            }
        }
    }
}

@Composable
fun PlayerCard(player: Player, onClick: () -> Unit) {
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
                        text = player.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${player.position} • Age ${player.age}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (player.isSuspended) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Suspended") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Performance", "${player.performance.toInt()}%")
                StatItem("Fitness", "${player.fitness.toInt()}%")
                StatItem("Value", "$${player.marketValue / 1_000_000}M")
            }
        }
    }
}

// Helper components

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorItem(error: Throwable) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = "Error loading data",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = error.message ?: "Unknown error",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ApprovalIndicator(approval: Int) {
    val color = getApprovalColor(approval.toDouble())
    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = "$approval%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun getApprovalColor(approval: Double): androidx.compose.ui.graphics.Color {
    return when {
        approval >= 70 -> MaterialTheme.colorScheme.primary
        approval >= 40 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.error
    }
}

@Composable
fun StatusChip(status: String) {
    AssistChip(
        onClick = { },
        label = { Text(status, style = MaterialTheme.typography.labelSmall) }
    )
}

@Composable
fun LicenseStatusBadge(status: LicenseStatus) {
    val color = when (status) {
        LicenseStatus.ACTIVE -> MaterialTheme.colorScheme.primary
        LicenseStatus.PENDING -> MaterialTheme.colorScheme.tertiary
        LicenseStatus.SUSPENDED, LicenseStatus.REVOKED -> MaterialTheme.colorScheme.error
        LicenseStatus.EXPIRED -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = status.name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ImmigrationTypeBadge(type: ImmigrationType) {
    val color = when (type) {
        ImmigrationType.REFUGEE, ImmigrationType.ASYLUM -> MaterialTheme.colorScheme.tertiary
        ImmigrationType.WORK -> MaterialTheme.colorScheme.primary
        ImmigrationType.HIGH_TALENT -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = type.name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleSmall)
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
