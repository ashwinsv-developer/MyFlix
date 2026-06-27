package com.myflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.myflix.navigation.AppNavHost
import com.myflix.navigation.BottomBar
import com.myflix.ui.component.AppExitDialog
import com.myflix.ui.theme.MyFlixTheme
import com.myflix.ui.viewmodel.MainActivityViewModel
import com.myflix.ui.viewmodel.SnackbarEventDuration
import com.myflix.utils.networkconnection.connectivityState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Details(val itemId: String)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Activity-scoped — same instance accessible anywhere via hiltViewModel(LocalActivity.current)
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFlixTheme {
                MainActivityScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen(viewModel: MainActivityViewModel) {

    val navController        = rememberNavController()
    val snackbarHostState    = remember { SnackbarHostState() }
    val navBackStackEntry    by navController.currentBackStackEntryAsState()
    val connectionState      by connectivityState()
    val activity = LocalActivity.current

    // String resources — kept in Composable since ViewModel shouldn't touch Context/strings directly
    val noInternetMessage = stringResource(R.string.no_internet_connection)
    val backOnlineMessage = stringResource(R.string.back_online)

    // Collect VM state
    val topBarTitle    by viewModel.topBarTitle.collectAsStateWithLifecycle()
    val showBackButton by viewModel.showBackButton.collectAsStateWithLifecycle()
    val showBottomBar  by viewModel.showBottomBar.collectAsStateWithLifecycle()
    val topBarActions  by viewModel.topBarActions.collectAsStateWithLifecycle()
    val showExitAlert  by viewModel.showExitAlert.collectAsStateWithLifecycle()

    // Notify VM whenever destination changes
    LaunchedEffect(navBackStackEntry) {
        viewModel.onDestinationChanged(
            route = navBackStackEntry?.destination?.route,
            hasPreviousBackStack = navController.previousBackStackEntry != null
        )
    }

    BackHandler(enabled = !showBackButton) {
     viewModel.setExitAlterDialog( true)
    }

    // Notify VM whenever network state changes
    LaunchedEffect(connectionState) {
        viewModel.onConnectionStateChanged(
            current = connectionState,
            noInternetMessage = noInternetMessage,
            backOnlineMessage = backOnlineMessage
        )
    }

    // React to one-shot snackbar events emitted by VM
    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { event ->
            if (event.dismissCurrent) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
            snackbarHostState.showSnackbar(
                message = event.message,
                duration = when (event.duration) {
                    SnackbarEventDuration.INDEFINITE -> SnackbarDuration.Indefinite
                    SnackbarEventDuration.SHORT      -> SnackbarDuration.Short
                }
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = topBarTitle,
                        style = MaterialTheme.typography.titleLarge
                    ) 
                },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = {
                    topBarActions.forEach { action ->
                        IconButton(onClick = action.onClick) {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = action.contentDescription
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            snackbarHostState = snackbarHostState,
            modifier = Modifier.padding(innerPadding)
        )

        if (showExitAlert) {
            AppExitDialog(
                onConfirm = { activity?.finish() },
                onDismiss = { viewModel.dismissExitDialog() }
            )
        }
    }
}