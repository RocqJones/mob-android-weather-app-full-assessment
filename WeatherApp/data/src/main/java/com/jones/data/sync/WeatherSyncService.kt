package com.jones.data.sync

import com.jones.core.network.NetworkConnectivityService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WeatherSyncService(
    private val networkConnectivityService: NetworkConnectivityService,
    private val scope: CoroutineScope,
) {
    private var networkMonitorJob: Job? = null
    private var onNetworkAvailable: (suspend () -> Unit)? = null

    /**
     * Start monitoring network connectivity & trigger sync when network available
     */
    fun startNetworkMonitoring(onConnected: suspend () -> Unit) {
        onNetworkAvailable = onConnected

        networkMonitorJob = networkConnectivityService.observeNetworkConnectivity().onEach { isConnected ->
            if (isConnected) {
                scope.launch {
                    onNetworkAvailable?.invoke()
                }
            }
        }.launchIn(scope)
    }

    /**
     * Stop monitoring network conn
     */
    fun stopNetworkMonitoring() {
        networkMonitorJob?.cancel()
        networkMonitorJob = null
        onNetworkAvailable = null
    }

    /**
     * Check network availability
     */
    fun isNetworkAvailable(): Boolean = networkConnectivityService.isNetworkAvailable()
}

