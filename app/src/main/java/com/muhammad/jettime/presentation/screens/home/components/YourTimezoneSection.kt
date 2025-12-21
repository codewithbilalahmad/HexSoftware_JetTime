package com.muhammad.jettime.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.jettime.R
import com.muhammad.jettime.domain.model.WorldTime

@Composable
fun YourTimezoneSection(modifier: Modifier = Modifier, currentTimezone: WorldTime) {
    val density = LocalDensity.current
    var dividerHeight by remember { mutableIntStateOf(0) }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "${stringResource(R.string.your_actual_time)}:",
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.surface)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.onSizeChanged{size ->
                dividerHeight = size.height
            }
        ) {
            Text(
                text = "${currentTimezone.abbreviation} (UTC ${currentTimezone.utcOffset})",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(with(density){
                        dividerHeight.toDp()
                    })
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(Modifier.width(10.dp))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_time),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = currentTimezone.time,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
