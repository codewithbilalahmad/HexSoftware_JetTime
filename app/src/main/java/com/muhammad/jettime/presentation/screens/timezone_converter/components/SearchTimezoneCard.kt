package com.muhammad.jettime.presentation.screens.timezone_converter.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.jettime.R
import com.muhammad.jettime.domain.model.WorldTime

@Composable
fun SearchTimezoneCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit, isCurrentTimezone: Boolean,
    timezone: WorldTime, shape: Shape,
    isSelected: Boolean,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        border = if (isSelected) BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.primary
        ) else BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.surfaceVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = timezone.abbreviation,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                    if(isCurrentTimezone){
                        Row(
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(
                                    width = 1.5.dp,
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = CircleShape
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.current),
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.surface)
                            )
                        }
                    }
                }
                Text(
                    text = "${timezone.city} (UTC ${timezone.utcOffset})",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.surface
                    )
                )
            }
            Text(
                text = timezone.time,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}