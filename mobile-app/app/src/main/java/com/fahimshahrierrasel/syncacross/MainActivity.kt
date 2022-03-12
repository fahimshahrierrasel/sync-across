package com.fahimshahrierrasel.syncacross

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fahimshahrierrasel.syncacross.config.FirebaseConfig
import com.fahimshahrierrasel.syncacross.ui.NavigationScreens
import com.fahimshahrierrasel.syncacross.ui.screens.Home
import com.fahimshahrierrasel.syncacross.ui.screens.Login
import com.fahimshahrierrasel.syncacross.ui.theme.PrimaryColor
import com.fahimshahrierrasel.syncacross.ui.theme.SyncAcrossTheme
import com.fahimshahrierrasel.syncacross.viewmodels.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            val navController = rememberNavController()
            val loginViewModel = LoginViewModel()
            val homeViewModel = HomeViewModel()

            if (intent?.action == Intent.ACTION_SEND) {
                intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    val title = intent.getStringExtra(Intent.EXTRA_SUBJECT) ?: ""
                    homeViewModel.onAction(HomeUIAction.IntentNewSyncItem(title, it))
                }
            }

            FirebaseConfig.auth.addAuthStateListener { auth ->
                if (auth.currentUser != null) {
                    loginViewModel.onAction(LoginUIAction.Navigate(LoginNavigate.GoHome))
                } else {
                    if (navController.currentDestination?.route == NavigationScreens.Home.route) {
                        navController.navigate(NavigationScreens.Login.route) {
                            popUpTo(NavigationScreens.Home.route) { inclusive = true }
                        }
                    } else {
                        loginViewModel.onAction(LoginUIAction.Navigate(LoginNavigate.Stay))
                    }
                }
            }

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = PrimaryColor,
                    darkIcons = false,
                )
            }

            SyncAcrossTheme {
                NavHost(
                    navController = navController,
                    startDestination = NavigationScreens.Login.route
                ) {
                    composable(NavigationScreens.Login.route) {
                        Login(viewModel = loginViewModel, navController = navController)
                    }
                    composable(NavigationScreens.Home.route) {
                        Home(viewModel = homeViewModel)
                    }
                }
            }
        }
    }
}