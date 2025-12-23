package com.muhammad.jettime.presentation.screens.timezone_converter.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.jettime.R
import com.muhammad.jettime.domain.model.ClockFormat
import com.muhammad.jettime.domain.model.WorldTime
import com.muhammad.jettime.utils.toClockFormat
import com.muhammad.jettime.utils.toFormattedTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class,
    ExperimentalTime::class
)
@Composable
fun FromTimezoneSection(
    modifier: Modifier = Modifier,
    fromTimezone: WorldTime?,
    fromDate: LocalDate, showFromTimezonePicker: Boolean,
    onToggleFromTimezoneBottomSheet: () -> Unit,
    fromTime: LocalTime, onPickFromDateClick: () -> Unit, onPickFromTimeClick: () -> Unit,
    onSelectFromDate: (LocalDate) -> Unit,
    onSelectFromTime: (LocalTime) -> Unit,
    onDismissFromDatePicker: () -> Unit,
    onDismissFromTimePicker: () -> Unit,
    showFromDatePicker: Boolean,
    showFromTimePicker: Boolean,
    isCurrentTimeZone: Boolean,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = fromTime.hour,
        initialMinute = fromTime.minute, is24Hour = false
    )
    val datePickerState = rememberDatePickerState(
        initialSelectedDate = fromDate.toJavaLocalDate()
    )
    if (showFromTimePicker) {
        TimePickerDialog(onDismissRequest = onDismissFromTimePicker, dismissButton = {
            TextButton(
                onClick = { onDismissFromTimePicker() },
                shapes = ButtonDefaults.shapes()
            ) {
                Text(text = stringResource(R.string.cancel))
            }
        }, confirmButton = {
            TextButton(
                onClick = {
                    val selectedTime = LocalTime(
                        hour = timePickerState.hour,
                        minute = timePickerState.minute
                    )
                    onSelectFromTime(selectedTime)
                },
                shapes = ButtonDefaults.shapes()
            ) {
                Text(text = stringResource(R.string.done))
            }
        }, title = {}) {
            TimePicker(state = timePickerState, layoutType = TimePickerLayoutType.Vertical)
        }
    }
    if (showFromDatePicker) {
        DatePickerDialog(
            onDismissRequest = onDismissFromDatePicker,
            dismissButton = {
                TextButton(
                    onClick = { onDismissFromDatePicker() },
                    shapes = ButtonDefaults.shapes()
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val selectedDate =
                                Instant.fromEpochMilliseconds(selectedDateMillis).toLocalDateTime(
                                    TimeZone.currentSystemDefault()
                                ).date
                            onSelectFromDate(selectedDate)
                        }
                    },
                    shapes = ButtonDefaults.shapes()
                ) {
                    Text(text = stringResource(R.string.done))
                }
            }) {
            DatePicker(state = datePickerState)
        }
    }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.from).uppercase(),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surface
                )
            )
            AnimatedVisibility(
                visible = isCurrentTimeZone,
                enter = scaleIn(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeIn(
                    animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
                ),
                exit = scaleOut(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeOut(
                    animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
                )
            ) {
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_current_timezone),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.current_timezone),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.surfaceVariant),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.timezone).uppercase(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surface
                        )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable(onClick = onToggleFromTimezoneBottomSheet)
                            .padding(vertical = 8.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val label =
                            if (fromTimezone != null) "${fromTimezone.abbreviation} (UTC ${fromTimezone.utcOffset})" else stringResource(
                                R.string.select_timezone
                            )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                        IconButton(
                            onClick = onToggleFromTimezoneBottomSheet,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ), shapes = IconButtonDefaults.shapes(),
                            modifier = Modifier.size(IconButtonDefaults.extraSmallContainerSize())
                        ) {
                            val rotation by animateFloatAsState(
                                targetValue = if (showFromTimezonePicker) 180f else 0f,
                                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                                label = "rotation"
                            )
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_down),
                                contentDescription = null,
                                modifier = Modifier.size(IconButtonDefaults.extraSmallIconSize).graphicsLayer{
                                    rotationZ = rotation
                                }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.date).uppercase(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.surface
                            )
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable(onClick = onPickFromDateClick)
                                .padding(vertical = 8.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(45.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.ic_date),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Column {
                                Text(
                                    text = fromDate.dayOfWeek.name.take(3).lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.surface)
                                )
                                Text(
                                    text = "${
                                        fromDate.month.name.take(3).lowercase()
                                            .replaceFirstChar { it.uppercase() }
                                    } ${fromDate.year}",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.time).uppercase(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.surface
                            )
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable(onClick = onPickFromTimeClick)
                                .padding(vertical = 8.dp, horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterHorizontally
                            )
                        ) {
                            Text(
                                text = fromTime.toFormattedTime(),
                                style = MaterialTheme.typography.titleLarge
                            )
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        MaterialTheme.colorScheme.surfaceVariant
                                    )
                            ) {
                                ClockFormat.entries.forEach { format ->
                                    val isSelectedFormat = format == fromTime.toClockFormat()
                                    val containerColor by animateColorAsState(
                                        targetValue = if (isSelectedFormat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                                        label = "containerColor"
                                    )
                                    val contentColor by animateColorAsState(
                                        targetValue = if (isSelectedFormat) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                                        label = "containerColor"
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(containerColor)
                                            .padding(vertical = 2.dp, horizontal = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = format.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Medium, color = contentColor
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
    }
}