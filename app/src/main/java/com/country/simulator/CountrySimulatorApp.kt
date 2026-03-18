package com.country.simulator

import android.app.Application
import androidx.room.Room
import androidx.work.WorkManager
import com.country.simulator.database.GameDatabase
import com.country.simulator.repositories.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CountrySimulatorApp : Application() {

    val database: GameDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            GameDatabase::class.java,
            "country_simulator_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    val repository: GameRepository by lazy {
        GameRepository(database)
    }

    val workManager: WorkManager by lazy {
        WorkManager.getInstance(applicationContext)
    }

    val applicationScope = CoroutineScope(SupervisorJob())
}
