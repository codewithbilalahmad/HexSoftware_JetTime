package com.muhammad.jettime.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.jettime.R
import com.muhammad.jettime.presentation.components.AppTextField
import com.muhammad.jettime.presentation.navigation.Destinations
import com.muhammad.jettime.presentation.screens.home.components.HomeTopBar
import com.muhammad.jettime.presentation.screens.home.components.WorldTimezoneCard
import com.muhammad.jettime.presentation.screens.home.components.YourTimezoneSection
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(naHostController: NavHostController, viewModel: HomeViewModel = koinViewModel()) {
    val layoutDirection = LocalLayoutDirection.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        FloatingActionButton(
            onClick = {
                naHostController.navigate(Destinations.TimezoneConverterScreen)
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_convert_time),
                contentDescription = null
            )
        }
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(
                start = paddingValues.calculateStartPadding(layoutDirection) + 12.dp,
                end = paddingValues.calculateEndPadding(layoutDirection) + 12.dp,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding() + 32.dp
            ), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item("HomeTopBar") {
                HomeTopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .animateItem()
                )
            }
            item("search_timezone_textField") {
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                        .padding(vertical = 8.dp),
                    value = state.searchQuery,
                    trailingIcon = R.drawable.ic_sort,
                    onValueChange = { newValue ->
                        viewModel.onAction(HomeAction.OnSearchQueryChange(newValue))
                    },
                    hint = R.string.search_timezones,
                    leadingIcon = R.drawable.ic_search,
                )
            }
            item("YourTimezoneSection") {
                state.currentTimezone?.let { timeZone ->
                    YourTimezoneSection(
                        currentTimezone = timeZone,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                            .padding(top = 4.dp, bottom = 16.dp)
                    )
                }
            }
            if (state.worldTimezones.isNotEmpty()) {
                items(state.worldTimezones, contentType = {
                    "worldTimezones"
                }, key = { it.zoneId }) { worldTimezone ->
                    WorldTimezoneCard(
                        worldZonetime = worldTimezone,
                        modifier = Modifier.fillMaxWidth().animateItem()
                    )
                }
            } else {
                item("EmptyTimezoneSection") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                            .padding(vertical = 32.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_logo),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.surface
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.no_timezone_found),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.no_timezone_found_desp),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                }
            }
        }
    }
}