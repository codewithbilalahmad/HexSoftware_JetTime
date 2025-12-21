package com.muhammad.jettime.presentation.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.jettime.R
import com.muhammad.jettime.domain.model.WorldTime

@Composable
fun WorldTimezoneCard(modifier: Modifier = Modifier, worldZonetime: WorldTime) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val icon = if (worldZonetime.isDay) R.drawable.ic_day else R.drawable.ic_night
                    Icon(
                        imageVector = ImageVector.vectorResource(icon),
                        contentDescription = null, modifier = Modifier.size(26.dp)
                    )
                    Text(
                        text = worldZonetime.time,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Light)
                    )
                }
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_menu),
                    contentDescription = null, modifier = Modifier.size(30.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_location),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = worldZonetime.timeDifference.day,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${worldZonetime.city}, ${worldZonetime.country}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${worldZonetime.abbreviation} (UTC ${worldZonetime.utcOffset})",
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.surface)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val diffTime = worldZonetime.timeDifference.diffTime
                    val icon = if (diffTime.startsWith("+")) R.drawable.ic_forward else R.drawable.ic_backward
                    Icon(
                        imageVector = ImageVector.vectorResource(icon),
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.surface
                    )
                    Text(
                        text = worldZonetime.timeDifference.diffTime,
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.surface)
                    )
                }
            }
        }
    }
}