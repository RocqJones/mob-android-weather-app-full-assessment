package com.jones.di

/**
 * Aggregated Koin modules in our app.
 */
val appModules =
    listOf(
        coreModule,
        networkModule,
        databaseModule,
        repositoryModule,
    )
