package com.jones.di

import com.jones.core.network.NetworkConnectivityService
import com.jones.core.network.NetworkConnectivityServiceImpl
import com.jones.data.sync.WeatherSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module for core utilities and services.
 */
val coreModule =
    module {
        // Network conn Service
        single<NetworkConnectivityService> {
            NetworkConnectivityServiceImpl(androidContext())
        }

        // Scope for background tasks
        single<CoroutineScope> {
            CoroutineScope(SupervisorJob() + Dispatchers.IO)
        }

        // Weather Sync Service
        single {
            WeatherSyncService(
                networkConnectivityService = get(),
                scope = get(),
            )
        }
    }
