package com.example.rentcountapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.core.designsystem.theme.RentCountAppTheme
import com.feature_analytics.presentation.navigation.AnalyticsPattern
import com.feature_analytics.presentation.navigation.analyticsGraph
import com.feature_home.presentation.navigation.HomePattern
import com.feature_home.presentation.navigation.homeGraph
import com.feature_home.presentation.navigation.homeRoute
import com.feature_transactions.presentation.navigation.TransactionPattern
import com.feature_transactions.presentation.navigation.transactionGraph
import com.feature_transactions.presentation.navigation.transactionRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentCountAppTheme {
                val items = listOf(
                    HomePattern,
                    TransactionPattern,
                    AnalyticsPattern
                )
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        if (currentDestination != null) {
                            BottomNavigation(
                                backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                            ) {
                                val iconsList = listOf(
                                    Icons.Filled.Home,
                                    ImageVector.vectorResource(id = R.drawable.baseline_assignment_24),
                                    ImageVector.vectorResource(id = R.drawable.baseline_assessment_24))
                                items.forEachIndexed { index, screen ->
                                    BottomNavigationItem(
                                        icon = { Icon(
                                            iconsList[index],
                                            contentDescription = null,
                                            tint = if(currentDestination.hierarchy.any { it.route == screen })
                                                MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        ) },
                                        selected = currentDestination.hierarchy.any { it.route == screen },
                                        onClick = {
                                            navController.navigate(screen) {
                                                // Pop up to the start destination of the graph to
                                                // avoid building up a large stack of destinations
                                                // on the back stack as users select items
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                // Avoid multiple copies of the same destination when
                                                // reselecting the same item
                                                launchSingleTop = true
                                                // Restore state when reselecting a previously selected item
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }


                    }
                ) { innerPadding ->

                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = HomePattern,
                    ){
                        transactionGraph(navController = navController)
                        homeGraph(navController = navController)
                        analyticsGraph(navController = navController)
                    }
                }
            }
        }
    }
}

