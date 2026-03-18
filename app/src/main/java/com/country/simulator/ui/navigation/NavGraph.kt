package com.country.simulator.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.country.simulator.model.*
import com.country.simulator.ui.screens.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun CountrySimulatorNavGraph(
    startDestination: String = "home",
    onNavigateToScreen: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.HOME) }
    
    // Placeholder state - in real app, these would come from ViewModel
    val playerCountry by remember { mutableStateOf<PlayerCountry?>(null) }
    val resources by remember { mutableStateOf<NationalResources?>(null) }
    val budget by remember { mutableStateOf<NationalBudget?>(null) }
    val taxBrackets by remember { mutableStateOf<List<TaxBracket>>(emptyList()) }
    val businesses by remember { mutableStateOf<List<Business>>(emptyList()) }
    val elections by remember { mutableStateOf<List<Election>>(emptyList()) }
    val campaigns by remember { mutableStateOf<List<Campaign>>(emptyList()) }
    val scandals by remember { mutableStateOf<List<Scandal>>(emptyList()) }
    val parties by remember { mutableStateOf<List<PoliticalParty>>(emptyList()) }
    val ministries by remember { mutableStateOf<List<Ministry>>(emptyList()) }
    val appointees by remember { mutableStateOf<List<Appointee>>(emptyList()) }
    val relations by remember { mutableStateOf<List<DiplomaticRelation>>(emptyList()) }
    val alliances by remember { mutableStateOf<List<Alliance>>(emptyList()) }
    val tradeBlocs by remember { mutableStateOf<List<TradeBloc>>(emptyList()) }
    val sanctions by remember { mutableStateOf<List<Sanction>>(emptyList()) }
    val tasks by remember { mutableStateOf<List<MicroTask>>(emptyList()) }
    
    when (currentScreen) {
        Screen.HOME -> HomeScreen(
            playerCountry = playerCountry,
            onNavigateToScreen = { screen -> currentScreen = screen }
        )
        Screen.ECONOMY -> EconomyScreen(
            resources = resources,
            budget = budget,
            taxBrackets = taxBrackets,
            businesses = businesses,
            onNavigateToScreen = { screen -> currentScreen = screen }
        )
        Screen.POLITICS -> PoliticsScreen(
            playerCountry = playerCountry,
            elections = elections,
            campaigns = campaigns,
            scandals = scandals,
            parties = parties,
            onNavigateToScreen = { screen -> currentScreen = screen }
        )
        Screen.LAW -> LawScreen(
            onNavigateToScreen = { screen -> currentScreen = screen }
        )
        Screen.DIPLOMACY -> DiplomacyScreen(
            relations = relations,
            alliances = alliances,
            tradeBlocs = tradeBlocs,
            sanctions = sanctions,
            onCountryClick = { },
            onNavigateToScreen = { screen -> currentScreen = screen }
        )
        Screen.MINISTRIES -> MinistriesScreen(
            ministries = ministries,
            appointees = appointees,
            onAppointeeClick = { },
            onMinistryClick = { }
        )
        Screen.INFRASTRUCTURE -> InfrastructureScreen(
            onNavigateToScreen = { screen -> currentScreen = screen }
        )
        Screen.DEMOGRAPHICS -> DemographicsScreen(
            onNavigateToScreen = { screen -> currentScreen = screen }
        )
        Screen.SPORTS -> SportsScreen(
            onNavigateToScreen = { screen -> currentScreen = screen }
        )
        Screen.SETTINGS -> SettingsScreen(
            onNavigateToScreen = { screen -> currentScreen = screen }
        )
    }
}
