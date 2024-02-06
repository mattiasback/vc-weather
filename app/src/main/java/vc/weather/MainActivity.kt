package vc.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import vc.weather.core.ui.theme.WeatherTheme
import vc.weather.feature.overview.ui.ForecastDetailsEvent
import vc.weather.feature.overview.ui.ForecastDetailsScreen
import vc.weather.feature.overview.ui.OverviewEvent
import vc.weather.feature.overview.ui.OverviewScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                WeatherApp()
            }
        }
    }
}

@Composable
fun WeatherApp() {

    val navController = rememberNavController()

    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Overview.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(
                route = Screens.Overview.route
            ) {
                OverviewScreen(
                    onEvent = { event ->
                        when (event) {
                            is OverviewEvent.OpenForecast -> {
                                navController.navigate(Screens.ForecastDetails.route.replace("{cityId}", event.cityId.toString()))
                            }
                        }
                    }
                )
            }
            composable(
                route = Screens.ForecastDetails.route,
                arguments = listOf(navArgument("cityId") { type = NavType.IntType })
            ) {
                ForecastDetailsScreen(
                    onEvent = { event ->
                        when (event) {
                            ForecastDetailsEvent.GoBack -> {
                                navController.navigateUp()
                            }
                        }
                    }
                )
            }

        }
    }
}

enum class Screens(val route: String) {
    Overview(route = "overview"),
    ForecastDetails(route = "forecast/{cityId}")
}