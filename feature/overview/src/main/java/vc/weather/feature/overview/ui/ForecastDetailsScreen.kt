package vc.weather.feature.overview.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import vc.weather.core.ui.theme.WeatherTheme
import vc.weather.feature.overview.R

@Composable
fun ForecastDetailsScreen(
    viewModel: ForecastDetailsViewModel = hiltViewModel<ForecastDetailsViewModelImpl>(),
    onEvent: (ForecastDetailsEvent) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.event?.let { event ->
        LaunchedEffect(event) {
            viewModel.onUiEvent(ForecastDetailsUiEvent.ResetEvent)
            onEvent(event)
        }
    }

    ForecastDetailsContent(
        cityName = uiState.cityName,
        weather = uiState.weather,
        iconUrl = uiState.iconUrl,
        temperature = uiState.temperature,
        feelsLike = uiState.feelsLike,
        onNavigateBack = { viewModel.onUiEvent(ForecastDetailsUiEvent.BackPressed) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ForecastDetailsContent(
    cityName: String = "",
    weather: String = "",
    iconUrl: String = "",
    temperature: String = "",
    feelsLike: String = "",
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = {
                    Text(
                        text = cityName,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable { onNavigateBack() },
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.go_back_arrow),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = weather,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    AsyncImage(
                        modifier = Modifier.size(120.dp),
                        contentDescription = stringResource(R.string.weather_icon_description),
                        model = iconUrl,
                        placeholder = painterResource(id = R.drawable.cloud24),
                        fallback = painterResource(id = R.drawable.cloud24),
                        contentScale = ContentScale.FillHeight,
                        alignment = Alignment.Center
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = stringResource(R.string.details_temperature, temperature),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = stringResource(R.string.details_feels_like) + stringResource(R.string.details_temperature, feelsLike),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}


@Preview(showBackground = false)
@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun OverviewPreview() {
    WeatherTheme {
        ForecastDetailsContent(
            cityName = "Paris",
            iconUrl = "",
            weather = "Windy",
            feelsLike = "8",
            temperature = "12.0"
        )
    }
}
