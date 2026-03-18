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
fun EconomyScreen(
    resources: NationalResources?,
    budget: NationalBudget?,
    taxBrackets: List<TaxBracket>,
    businesses: List<Business>,
    onNavigateToScreen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Economy") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Overview") },
                    label = { Text("Overview") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountBalance, contentDescription = "Budget") },
                    label = { Text("Budget") },
                    selected = false,
                    onClick = { onNavigateToScreen(Screen.ECONOMY) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.TrendingUp, contentDescription = "Trade") },
                    label = { Text("Trade") },
                    selected = false,
                    onClick = { onNavigateToScreen(Screen.DIPLOMACY) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Business, contentDescription = "Business") },
                    label = { Text("Business") },
                    selected = false,
                    onClick = { }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Treasury Overview
            item {
                TreasuryCard(resources)
            }
            
            // Budget Summary
            item {
                BudgetSummaryCard(budget)
            }
            
            // Economic Indicators
            item {
                EconomicIndicatorsCard(resources)
            }
            
            // Oil & Energy
            item {
                OilEnergyCard(resources)
            }
            
            // Tax Brackets
            if (taxBrackets.isNotEmpty()) {
                item {
                    TaxBracketsCard(taxBrackets)
                }
            }
            
            // Top Businesses
            if (businesses.isNotEmpty()) {
                item {
                    BusinessesCard(businesses.take(5))
                }
            }
        }
    }
}

@Composable
fun TreasuryCard(resources: NationalResources?) {
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
                    text = "Treasury",
                    style = MaterialTheme.typography.titleLarge
                )
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Text(
                text = formatCurrency(resources?.treasuryBalance ?: 0.0),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EconomicIndicator("Debt", formatCurrency(resources?.nationalDebt ?: 0.0))
                EconomicIndicator("Reserves", formatCurrency(resources?.foreignReserves ?: 0.0))
            }
        }
    }
}

@Composable
fun BudgetSummaryCard(budget: NationalBudget?) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Budget Summary",
                style = MaterialTheme.typography.titleMedium
            )
            
            if (budget != null) {
                BudgetRow("Revenue", formatCurrency(budget.totalRevenue), true)
                BudgetRow("Expenses", formatCurrency(budget.totalExpenses), false)
                
                Divider()
                
                val isDeficit = budget.deficit > 0
                BudgetRow(
                    if (isDeficit) "Deficit" else "Surplus",
                    formatCurrency(if (isDeficit) budget.deficit else budget.surplus),
                    !isDeficit
                )
                
                Divider()
                
                Text(
                    text = "Spending by Category",
                    style = MaterialTheme.typography.titleSmall
                )
                
                BudgetBreakdownRow("Defense", budget.defenseSpending, budget.totalExpenses)
                BudgetBreakdownRow("Healthcare", budget.healthcareSpending, budget.totalExpenses)
                BudgetBreakdownRow("Education", budget.educationSpending, budget.totalExpenses)
                BudgetBreakdownRow("Infrastructure", budget.infrastructureSpending, budget.totalExpenses)
                BudgetBreakdownRow("Welfare", budget.socialWelfareSpending, budget.totalExpenses)
            } else {
                Text(
                    text = "No budget data available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EconomicIndicatorsCard(resources: NationalResources?) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Economic Indicators",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                EconomicIndicator("Exports", formatCurrency(resources?.exports ?: 0.0))
                EconomicIndicator("Imports", formatCurrency(resources?.imports ?: 0.0))
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val tradeBalance = (resources?.tradeBalance ?: 0.0)
                val isPositive = tradeBalance >= 0
                EconomicIndicator(
                    "Trade Balance",
                    formatCurrency(tradeBalance),
                    isPositive
                )
            }
        }
    }
}

@Composable
fun OilEnergyCard(resources: NationalResources?) {
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
                    text = "Oil & Energy",
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    imageVector = Icons.Default.LocalGasStation,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Oil Price",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${resources?.oilPrice ?: 0.0}/bbl",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Column {
                    Text(
                        text = "Production",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(resources?.oilProduction ?: 0.0).toInt()} bpd",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Column {
                    Text(
                        text = "Consumption",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(resources?.oilConsumption ?: 0.0).toInt()} bpd",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun TaxBracketsCard(taxBrackets: List<TaxBracket>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Tax Brackets",
                style = MaterialTheme.typography.titleMedium
            )
            
            taxBrackets.forEach { bracket ->
                TaxBracketRow(bracket)
            }
        }
    }
}

@Composable
fun BusinessesCard(businesses: List<Business>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Top Businesses",
                style = MaterialTheme.typography.titleMedium
            )
            
            businesses.forEach { business ->
                BusinessRow(business)
            }
        }
    }
}

@Composable
fun BudgetRow(label: String, value: String, positive: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(
            text = value,
            color = if (positive) MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun BudgetBreakdownRow(label: String, amount: Double, total: Double) {
    val percentage = if (total > 0) (amount / total * 100) else 0.0
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${percentage.toInt()}%",
                style = MaterialTheme.typography.bodySmall
            )
        }
        LinearProgressIndicator(
            progress = (percentage / 100).toFloat(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun EconomicIndicator(label: String, value: String, positive: Boolean = true) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = if (positive) MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.error
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TaxBracketRow(bracket: TaxBracket) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = bracket.bracketName,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "${bracket.taxRate}%",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun BusinessRow(business: Business) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = business.businessName,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = business.industry,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = formatCurrency(business.revenue),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
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
