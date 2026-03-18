package com.country.simulator.model

enum class GameMode {
    EXECUTIVE,        // Won election - full executive power
    PARLIAMENTARY,    // Lost election - opposition mode
    MONARCHY,         // Monarchy mode
    TECHNOCRACY       // Technocracy mode
}

enum class GovernmentType {
    DEMOCRACY,
    MONARCHY,
    TECHNOCRACY,
    AUTOCRACY,
    REPUBLIC
}

enum class ElectionCycle {
    EVERY_2_YEARS,
    EVERY_4_YEARS,
    EVERY_5_YEARS,
    LIFETIME
}

enum class NationalIdeology {
    ISOLATIONIST,
    FRIENDLY,
    AGGRESSIVE,
    NEUTRAL,
    EXPANSIONIST,
    PACIFIST
}
