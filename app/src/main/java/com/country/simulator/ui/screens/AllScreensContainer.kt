package com.country.simulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.country.simulator.model.*
import com.country.simulator.viewmodel.GameViewModel

/**
 * Main Screen Container - All screens accessible from here
 * This is the central hub for the entire game UI
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllScreensContainer(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.HOME) }
    
    // Collect all the data flows
    val playerCountry by viewModel.playerCountry.collectAsState()
    val resources by viewModel.resources.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val elections by viewModel.elections.collectAsState()
    val campaigns by viewModel.campaigns.collectAsState()
    val scandals by viewModel.scandals.collectAsState()
    val ministries by viewModel.ministries.collectAsState()
    val appointees by viewModel.appointees.collectAsState()
    val budget by viewModel.budget.collectAsState()
    val taxBrackets by viewModel.taxBrackets.collectAsState()
    val relations by viewModel.relations.collectAsState()
    val alliances by viewModel.alliances.collectAsState()
    val tradeBlocs by viewModel.tradeBlocs.collectAsState()
    val sanctions by viewModel.sanctions.collectAsState()
    val laws by viewModel.laws.collectAsState()
    val bills by viewModel.bills.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val gameEvents by viewModel.gameEvents.collectAsState()
    
    // Paging data
    val npcs = viewModel.repository.getNPCsPaged(playerCountry?.countryId ?: 0).collectAsLazyPagingItems()
    val businesses = viewModel.repository.getBusinessesPaged(playerCountry?.countryId ?: 0).collectAsLazyPagingItems()
    val immigrationCases = viewModel.repository.getImmigrationCasesPaged(playerCountry?.countryId ?: 0).collectAsLazyPagingItems()
    val workPermits = viewModel.repository.getWorkPermitsPaged(playerCountry?.countryId ?: 0).collectAsLazyPagingItems()
    val purchaseOrders = viewModel.repository.getPurchaseOrdersPaged(playerCountry?.countryId ?: 0).collectAsLazyPagingItems()
    val welfarePayments = viewModel.repository.getWelfarePaymentsPaged(playerCountry?.countryId ?: 0).collectAsLazyPagingItems()
    val stadiumVendors = viewModel.repository.getStadiumVendorsPaged(playerCountry?.countryId ?: 0).collectAsLazyPagingItems()
    val players = viewModel.repository.getPlayersPaged(playerCountry?.countryId ?: 0).collectAsLazyPagingItems()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    when (currentScreen) {
                        AppScreen.HOME -> Text("Country Simulator")
                        AppScreen.TASKS -> Text("Tasks")
                        AppScreen.ECONOMY -> Text("Economy")
                        AppScreen.POLITICS -> Text("Politics")
                        AppScreen.LAW -> Text("Law & Justice")
                        AppScreen.MINISTRIES -> Text("Cabinet")
                        AppScreen.DIPLOMACY -> Text("Diplomacy")
                        AppScreen.NPCS -> Text("Population")
                        AppScreen.BUSINESSES -> Text("Businesses")
                        AppScreen.IMMIGRATION -> Text("Immigration")
                        AppScreen.PURCHASE_ORDERS -> Text("Purchase Orders")
                        AppScreen.WELFARE -> Text("Welfare")
                        AppScreen.BUS_SCHEDULES -> Text("Transit")
                        AppScreen.MEDICINE -> Text("Medical")
                        AppScreen.SPORTS -> Text("Sports")
                        AppScreen.STADIUMS -> Text("Stadiums")
                    }
                },
                navigationIcon = {
                    if (currentScreen != AppScreen.HOME) {
                        IconButton(onClick = { currentScreen = AppScreen.HOME }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back to Home")
                        }
                    }
                },
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
                    selected = currentScreen == AppScreen.HOME,
                    onClick = { currentScreen = AppScreen.HOME }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Economy") },
                    label = { Text("Economy") },
                    selected = currentScreen == AppScreen.ECONOMY,
                    onClick = { currentScreen = AppScreen.ECONOMY }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Policy, contentDescription = "Politics") },
                    label = { Text("Politics") },
                    selected = currentScreen == AppScreen.POLITICS,
                    onClick = { currentScreen = AppScreen.POLITICS }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Gavel, contentDescription = "Law") },
                    label = { Text("Law") },
                    selected = currentScreen == AppScreen.LAW,
                    onClick = { currentScreen = AppScreen.LAW }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.People, contentDescription = "More") },
                    label = { Text("More") },
                    selected = false,
                    onClick = { /* Show more menu */ }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(
                    playerCountry = playerCountry,
                    onNavigateToScreen = { screen -> 
                        when (screen) {
                            Screen.HOME -> currentScreen = AppScreen.HOME
                            Screen.ECONOMY -> currentScreen = AppScreen.ECONOMY
                            Screen.POLITICS -> currentScreen = AppScreen.POLITICS
                            Screen.LAW -> currentScreen = AppScreen.LAW
                            Screen.DIPLOMACY -> currentScreen = AppScreen.DIPLOMACY
                            Screen.MINISTRIES -> currentScreen = AppScreen.MINISTRIES
                            Screen.INFRASTRUCTURE -> currentScreen = AppScreen.BUS_SCHEDULES
                            Screen.DEMOGRAPHICS -> currentScreen = AppScreen.NPCS
                            Screen.SPORTS -> currentScreen = AppScreen.SPORTS
                            Screen.SETTINGS -> { /* Handle settings */ }
                        }
                    }
                )
                
                AppScreen.TASKS -> TasksScreen(
                    tasks = tasks,
                    onTaskClick = { /* Handle task click */ },
                    onTaskComplete = { task, option -> viewModel.completeTask(task, option) }
                )
                
                AppScreen.ECONOMY -> EconomyScreen(
                    resources = resources,
                    budget = budget,
                    taxBrackets = taxBrackets,
                    businesses = businesses.itemSnapshotList.items,
                    onNavigateToScreen = { screen ->
                        when (screen) {
                            Screen.BUSINESSES -> currentScreen = AppScreen.BUSINESSES
                            Screen.PURCHASE_ORDERS -> currentScreen = AppScreen.PURCHASE_ORDERS
                            Screen.WELFARE -> currentScreen = AppScreen.WELFARE
                            else -> { }
                        }
                    }
                )
                
                AppScreen.POLITICS -> PoliticsScreen(
                    playerCountry = playerCountry,
                    elections = elections,
                    campaigns = campaigns,
                    scandals = scandals,
                    parties = emptyList(), // Would need to add to ViewModel
                    onNavigateToScreen = { /* Handle navigation */ }
                )
                
                AppScreen.LAW -> LawScreen(
                    laws = laws,
                    bills = bills,
                    cases = emptyList(), // Would need to add to ViewModel
                    onNavigateToScreen = { /* Handle navigation */ }
                )
                
                AppScreen.DIPLOMACY -> DiplomacyScreen(
                    relations = relations,
                    alliances = alliances,
                    tradeBlocs = tradeBlocs,
                    sanctions = sanctions,
                    onCountryClick = { /* Handle country click */ },
                    onNavigateToScreen = { /* Handle navigation */ }
                )
                
                AppScreen.MINISTRIES -> MinistriesScreen(
                    ministries = ministries,
                    appointees = appointees,
                    onAppointeeClick = { /* Handle appointee click */ },
                    onMinistryClick = { /* Handle ministry click */ }
                )
                
                AppScreen.NPCS -> NPCsManagementScreen(
                    npcs = npcs,
                    onNPCClick = { /* Handle NPC click */ }
                )
                
                AppScreen.BUSINESSES -> BusinessLicensesScreen(
                    businesses = businesses,
                    onBusinessClick = { /* Handle business click */ }
                )
                
                AppScreen.IMMIGRATION -> ImmigrationScreen(
                    cases = immigrationCases,
                    onCaseClick = { /* Handle case click */ }
                )
                
                AppScreen.PURCHASE_ORDERS -> PurchaseOrdersScreen(
                    orders = purchaseOrders,
                    onOrderClick = { /* Handle order click */ }
                )
                
                AppScreen.WELFARE -> WelfarePaymentsScreen(
                    payments = welfarePayments,
                    onPaymentClick = { /* Handle payment click */ }
                )
                
                AppScreen.BUS_SCHEDULES -> BusSchedulesScreen(
                    schedules = emptyList(), // Would need to add to ViewModel
                    onScheduleClick = { /* Handle schedule click */ },
                    onAdjustFrequency = { schedule, freq -> /* Handle frequency change */ }
                )
                
                AppScreen.MEDICINE -> MedicineStockpilesScreen(
                    stockpiles = emptyList(), // Would need to add to ViewModel
                    onStockpileClick = { /* Handle stockpile click */ },
                    onRestock = { stockpile, amount -> /* Handle restock */ }
                )
                
                AppScreen.SPORTS -> NationalTeamsScreen(
                    players = players,
                    onPlayerClick = { /* Handle player click */ }
                )
                
                AppScreen.STADIUMS -> StadiumVendorsScreen(
                    vendors = stadiumVendors,
                    onVendorClick = { /* Handle vendor click */ }
                )
            }
        }
    }
    
    // Show notifications if any
    if (notifications.isNotEmpty()) {
        NotificationBadge(count = notifications.size) {
            // Show notifications panel
        }
    }
}

/**
 * All available screens in the app
 */
enum class AppScreen {
    HOME,
    TASKS,
    ECONOMY,
    POLITICS,
    LAW,
    MINISTRIES,
    DIPLOMACY,
    NPCS,
    BUSINESSES,
    IMMIGRATION,
    PURCHASE_ORDERS,
    WELFARE,
    BUS_SCHEDULES,
    MEDICINE,
    SPORTS,
    STADIUMS
}

/**
 * Notification badge component
 */
@Composable
fun NotificationBadge(
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (count > 0) {
        FloatingActionButton(
            onClick = onClick,
            modifier = modifier
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.error
        ) {
            Box {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications"
                )
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .offset { 
                            androidx.compose.ui.unit.IntOffset(
                                x = 8.dp.roundToPx(),
                                y = (-8).dp.roundToPx()
                            )
                        }
                ) {
                    Text(
                        text = if (count > 99) "99+" else "$count",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * Quick access floating action button for tasks
 */
@Composable
fun QuickTaskButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .padding(16.dp),
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            imageVector = Icons.Default.AddTask,
            contentDescription = "New Task"
        )
    }
}
