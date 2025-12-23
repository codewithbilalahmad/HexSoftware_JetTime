package com.muhammad.jettime.presentation.screens.timezone_converter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.muhammad.jettime.R
import com.muhammad.jettime.presentation.screens.timezone_converter.components.ConverterDividerSection
import com.muhammad.jettime.presentation.screens.timezone_converter.components.FromTimezoneSection
import com.muhammad.jettime.presentation.screens.timezone_converter.components.PrimaryButton
import com.muhammad.jettime.presentation.screens.timezone_converter.components.SecondaryButton
import com.muhammad.jettime.presentation.screens.timezone_converter.components.TimezoneConversationSection
import com.muhammad.jettime.presentation.screens.timezone_converter.components.TimezonePickerBottomSheet
import com.muhammad.jettime.presentation.screens.timezone_converter.components.ToTimezoneSection
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TimezoneConverterScreen(
    navHostController: NavHostController,
    viewModel: TimezoneConverterViewModel = koinViewModel(),
) {
    val layoutDirection = LocalLayoutDirection.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        Column(modifier = Modifier.fillMaxWidth()) {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.timezone_converter))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.navigateUp()
                    }, shapes = IconButtonDefaults.shapes()) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_menu),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.5.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(
                start = paddingValues.calculateStartPadding(layoutDirection),
                end = paddingValues.calculateEndPadding(layoutDirection),
                top = paddingValues.calculateTopPadding() + 16.dp,
                bottom = paddingValues.calculateBottomPadding() + 32.dp
            ), verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item("FromTimezoneSection") {
                FromTimezoneSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .animateItem(),
                    fromTimezone = state.selectedFromTimezone,
                    fromDate = state.selectedFromDate,
                    fromTime = state.selectedFromTime,
                    onSelectFromDate = { date ->
                        viewModel.onAction(TimezoneConverterAction.OnSelectFromDate(date))
                    },
                    onPickFromDateClick = {
                        viewModel.onAction(TimezoneConverterAction.OnToggleFromDatePicker)
                    },
                    onPickFromTimeClick = {
                        viewModel.onAction(TimezoneConverterAction.OnToggleFromTimePicker)
                    },
                    onDismissFromDatePicker = {
                        viewModel.onAction(TimezoneConverterAction.OnToggleFromDatePicker)
                    },
                    showFromDatePicker = state.showFromDatePicker,
                    onSelectFromTime = { time ->
                        viewModel.onAction(TimezoneConverterAction.OnSelectFromTime(time))
                    },
                    onDismissFromTimePicker = {
                        viewModel.onAction(TimezoneConverterAction.OnToggleFromTimePicker)
                    },
                    onToggleFromTimezoneBottomSheet = {
                        viewModel.onAction(TimezoneConverterAction.OnToggleFromTimezonePicker)
                    },
                    showFromTimePicker = state.showFromTimePicker,
                    showFromTimezonePicker = state.showFromTimezonePicker,
                    isCurrentTimeZone = state.selectedFromTimezone == state.worldTimezones.firstOrNull()
                )
            }
            item("ConverterDividerSection") {
                ConverterDividerSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                )
            }
            item("ToTimezoneSection") {
                ToTimezoneSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .animateItem(),
                    showToTimezonePicker = state.showToTimezonePicker,
                    toTimezone = state.selectedToTimezone,
                    onToggleToTimezoneBottomSheet = {
                        viewModel.onAction(TimezoneConverterAction.OnToggleToTimezonePicker)
                    },
                    isCurrentTimeZone = state.selectedToTimezone == state.worldTimezones.firstOrNull(),
                )
            }
            if (state.conversionResult != null) {
                item("TimezoneConversationSection") {
                    TimezoneConversationSection(
                        modifier = Modifier
                            .fillMaxWidth().padding(horizontal = 12.dp)
                            .animateItem(),
                        result = state.conversionResult ?: return@item
                    )
                }
            }
            item("ConvertControlSection") {
                if (state.conversionResult != null) {
                    SecondaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .animateItem(),
                        text = R.string.reset,
                        contentPadding = PaddingValues(vertical = 16.dp),
                        onClick = {
                            viewModel.onAction(TimezoneConverterAction.OnResetConversion)
                        },
                        textStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                } else {
                    PrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .animateItem(),
                        enabled = state.selectedFromTimezone != null && state.selectedToTimezone != null,
                        text = R.string.convert,
                        contentPadding = PaddingValues(vertical = 16.dp),
                        onClick = {
                            viewModel.onAction(TimezoneConverterAction.ConvertTimezone)
                        },
                        textStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
    TimezonePickerBottomSheet(
        selectedTimeZone = state.selectedFromTimezone,
        onToggleTimezoneBottomSheet = {
            viewModel.onAction(TimezoneConverterAction.OnToggleFromTimezonePicker)
        },
        onSelectTimeZone = { timezone ->
            viewModel.onAction(TimezoneConverterAction.OnSelectFromTimezone(timezone))
        }, showTimezoneBottomSheet = state.showFromTimezonePicker,
        worldTimezones = state.selectedToTimezone?.let { timezone ->
            state.worldTimezones - timezone
        } ?: state.worldTimezones
    )
    TimezonePickerBottomSheet(
        selectedTimeZone = state.selectedToTimezone,
        onToggleTimezoneBottomSheet = {
            viewModel.onAction(TimezoneConverterAction.OnToggleToTimezonePicker)
        },
        onSelectTimeZone = { timezone ->
            viewModel.onAction(TimezoneConverterAction.OnSelectToTimezone(timezone))
        }, showTimezoneBottomSheet = state.showToTimezonePicker,
        worldTimezones = state.selectedFromTimezone?.let { timezone ->
            state.worldTimezones - timezone
        } ?: state.worldTimezones
    )
}