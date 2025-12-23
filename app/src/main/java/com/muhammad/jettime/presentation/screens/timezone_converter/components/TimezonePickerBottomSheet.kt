package com.muhammad.jettime.presentation.screens.timezone_converter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.muhammad.jettime.R
import com.muhammad.jettime.domain.model.WorldTime
import com.muhammad.jettime.presentation.components.AppTextField
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TimezonePickerBottomSheet(
    modifier: Modifier = Modifier,
    selectedTimeZone: WorldTime?,
    worldTimezones: List<WorldTime>, showTimezoneBottomSheet: Boolean,
    onToggleTimezoneBottomSheet: () -> Unit,
    onSelectTimeZone: (WorldTime) -> Unit,
) {
    if (showTimezoneBottomSheet) {
        var searchQuery by remember { mutableStateOf("") }
        var filteredTimezones by remember { mutableStateOf(worldTimezones) }
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(searchQuery) {
            delay(300L)
            filteredTimezones = if (searchQuery.isNotEmpty()) {
                worldTimezones.filter {
                    it.city.contains(
                        searchQuery,
                        ignoreCase = true
                    )
                } + worldTimezones.filter { it.country.contains(searchQuery, ignoreCase = true) }
            } else worldTimezones
        }
        ModalBottomSheet(
            onDismissRequest = onToggleTimezoneBottomSheet,
            sheetState = bottomSheetState,
            modifier = modifier.fillMaxWidth(),
            dragHandle = {}, containerColor = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .padding(top = 4.dp, start = 12.dp, end = 12.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onToggleTimezoneBottomSheet,
                        modifier = Modifier.align(Alignment.CenterStart),
                        shapes = IconButtonDefaults.shapes()
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_cancel),
                            contentDescription = null
                        )
                    }
                    Text(
                        text = stringResource(R.string.timezone_picker),
                        style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                    TextButton(
                        onClick = {
                            onSelectTimeZone(selectedTimeZone ?: return@TextButton)
                        },
                        modifier = Modifier.align(Alignment.CenterEnd),
                        enabled = selectedTimeZone != null,
                        shapes = ButtonDefaults.shapes()
                    ) {
                        Text(
                            text = stringResource(R.string.done),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item("timezone_searchBar") {
                        AppTextField(
                            value = searchQuery,
                            onValueChange = { newValue ->
                                searchQuery = newValue
                            },
                            modifier = Modifier.fillMaxWidth().animateItem(),
                            leadingIcon = R.drawable.ic_search,
                            hint = R.string.search_timezones,
                            trailingIcon = R.drawable.ic_sort
                        )
                    }
                    item("list_spacer") {
                        Spacer(Modifier.height(16.dp))
                    }
                    if(filteredTimezones.isNotEmpty()){
                        itemsIndexed(
                            filteredTimezones, contentType = {_ , _ ->
                                "filteredTimezones"
                            },
                            key = { _, timezone -> timezone.zoneId }) { index, timezone ->
                            val isSelected = selectedTimeZone == timezone
                            val shape = when (index) {
                                0 -> RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 4.dp,
                                    bottomEnd = 4.dp
                                )

                                worldTimezones.size - 1 -> RoundedCornerShape(
                                    topStart = 4.dp,
                                    topEnd = 4.dp,
                                    bottomStart = 16.dp,
                                    bottomEnd = 16.dp
                                )

                                else -> RoundedCornerShape(4.dp)
                            }
                            SearchTimezoneCard(
                                timezone = timezone,
                                modifier = Modifier.fillMaxWidth().animateItem(),
                                onClick = {
                                    onSelectTimeZone(timezone)
                                }, isCurrentTimezone = timezone.isCurrentTimezone,
                                shape = shape,
                                isSelected = isSelected
                            )
                        }
                    } else{
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
    }
}