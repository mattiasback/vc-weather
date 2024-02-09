package vc.weather.feature.overview.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import vc.weather.core.ui.theme.WeatherTheme
import vc.weather.data.forecast.models.City
import vc.weather.feature.overview.R

@Composable
fun OverviewScreen(
    viewModel: OverviewViewModel = hiltViewModel<OverviewViewModelImpl>(),
    onEvent: (OverviewEvent) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.event?.let { event ->
        LaunchedEffect(event) {
            viewModel.onUiEvent(OverviewUiEvent.ResetEvent)
            onEvent(event)
        }
    }

    OverviewContent(
        cities = uiState.cities,
        onCityClick = { id -> viewModel.onUiEvent(OverviewUiEvent.CityClicked(id)) },
        onRefresh = { viewModel.onUiEvent(OverviewUiEvent.Refresh) }
    )
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun OverviewContent(
    cities: List<UiCity> = emptyList(),
    onCityClick: (Int) -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    val refreshState = rememberPullToRefreshState()
    if (refreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            onRefresh()
            refreshState.endRefresh()
        }
    }

    Surface {
        Box(modifier = Modifier.nestedScroll(refreshState.nestedScrollConnection)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stickyHeader {
                    Text(
                        text = stringResource(R.string.overview_weather_title),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                items(items = cities) { city ->
                    CityCard(
                        modifier = Modifier.fillMaxWidth(),
                        onCityClick = onCityClick,
                        city = city
                    )
                }
            }
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState,
            )
        }
    }
}

@Composable
private fun CityCard(
    modifier: Modifier = Modifier,
    onCityClick: (Int) -> Unit,
    city: UiCity
) {
    ElevatedCard(
        modifier = modifier.clickable { onCityClick(city.id) },
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .minimumInteractiveComponentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            AsyncImage(
                contentDescription = stringResource(R.string.weather_icon_description),
                model = city.iconUrl,
                placeholder = painterResource(id = R.drawable.cloud24),
                fallback = painterResource(id = R.drawable.cloud24),
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun OverviewPreview() {
    WeatherTheme {
        OverviewContent(
            cities = City.entries.map {
                UiCity(
                    id = (1..1000).random(),
                    name = it.name,
                    iconUrl = "https://image.com"
                )
            }
        )
    }
}
