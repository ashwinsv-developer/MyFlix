package com.myflix.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myflix.navigation.Routes
import com.myflix.utils.networkconnection.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

    // ─── Top Bar Title ────────────────────────────────────────────────────────

    private val _topBarTitle = MutableStateFlow("MyFlix")
    val topBarTitle: StateFlow<String> = _topBarTitle.asStateFlow()

    private val _showBackButton = MutableStateFlow(false)
    val showBackButton: StateFlow<Boolean> = _showBackButton.asStateFlow()

    private val _showBottomBar = MutableStateFlow(false)
    val showBottomBar: StateFlow<Boolean> = _showBottomBar.asStateFlow()

    private val _topBarActions = MutableStateFlow<List<TopBarAction>>(emptyList())
    val topBarActions: StateFlow<List<TopBarAction>> = _topBarActions.asStateFlow()

    private val _showExitAlert = MutableStateFlow(false)
    val showExitAlert: StateFlow<Boolean> = _showExitAlert.asStateFlow()

    fun onBackPressed() { _showExitAlert.value = true }
    fun dismissExitDialog() { _showExitAlert.value = false }


    fun onDestinationChanged(route: String?, hasPreviousBackStack: Boolean) {
        _showBottomBar.value = route in listOf(Routes.Celebrity.route, Routes.Movie.route)
        // Reset top bar state for the new destination
        _topBarActions.value = emptyList()
    }


    // ── Called from each screen ──────────────────────────────────────
    fun setTopBarTitle(title: String) {
        _topBarTitle.value = title
    }

    fun setBackButtonVisible(visible: Boolean) {
        _showBackButton.value = visible
    }

    fun setTopBarActions(actions: List<TopBarAction>) {
        _topBarActions.value = actions
    }

    fun setExitAlterDialog( show: Boolean){
        _showExitAlert.value = show
    }





    // ─── Network / Snackbar ───────────────────────────────────────────────────

    // One-shot events for snackbar so they don't re-emit on recomposition
    private val _snackbarEvent = MutableSharedFlow<SnackbarEvent>()
    val snackbarEvent: SharedFlow<SnackbarEvent> = _snackbarEvent.asSharedFlow()

    private var previousConnectionState: ConnectionState? = null

    fun onConnectionStateChanged(
        current: ConnectionState,
        noInternetMessage: String,
        backOnlineMessage: String
    ) {
        viewModelScope.launch {
            when (current) {
                is ConnectionState.Unavailable -> {
                    _snackbarEvent.emit(
                        SnackbarEvent(
                            message = noInternetMessage,
                            duration = SnackbarEventDuration.INDEFINITE
                        )
                    )
                }
                is ConnectionState.Available -> {
                    if (previousConnectionState is ConnectionState.Unavailable) {
                        _snackbarEvent.emit(
                            SnackbarEvent(
                                message = backOnlineMessage,
                                duration = SnackbarEventDuration.SHORT,
                                dismissCurrent = true   // signal UI to dismiss previous snackbar first
                            )
                        )
                    }
                }
            }
            previousConnectionState = current
        }
    }
}

// ─── Models ───────────────────────────────────────────────────────────────────

data class SnackbarEvent(
    val message: String,
    val duration: SnackbarEventDuration = SnackbarEventDuration.SHORT,
    val dismissCurrent: Boolean = false
)

enum class SnackbarEventDuration { SHORT, INDEFINITE }

data class TopBarAction(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val contentDescription: String,
    val onClick: () -> Unit
)
