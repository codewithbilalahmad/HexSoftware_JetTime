package com.muhammad.jettime.presentation.screens.timezone_converter.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import com.muhammad.jettime.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.muhammad.jettime.domain.model.WorldTime
import com.muhammad.jettime.utils.toFormattedDate

@Composable
fun TimezoneConversationSection(modifier: Modifier = Modifier, result: WorldTime) {
    val timeParts = result.time.trim().split(" ")
    val timePart = timeParts[0]
    val amPmPart = timeParts[1]
    val timeAnnotatedString = buildAnnotatedString {
        withStyle(
            SpanStyle(
                fontSize = MaterialTheme.typography.displayLarge.fontSize,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        ) {
            append("$timePart ")
        }
        withStyle(
            SpanStyle(
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.surface
            )
        ) {
            append(amPmPart)
        }
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.surfaceVariant),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.conversation_result).uppercase(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.surface
                    )
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(text = timeAnnotatedString)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val icon = if (result.isDay) R.drawable.ic_day else R.drawable.ic_night
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.surface
                )
                Text(
                    text = "${result.city}, ${result.country}",
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.surface)
                )
            }
            Spacer(Modifier.height(12.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp), maxItemsInEachRow = 2
            ) {
                ConversationResultItem(
                    icon = R.drawable.ic_date,
                    label = R.string.date,
                    modifier = Modifier.weight(1f),
                    value = result.date.toFormattedDate()
                )
                ConversationResultItem(
                    icon = R.drawable.ic_time,
                    label = R.string.difference,
                    modifier = Modifier.weight(1f),
                    value = result.timeDifference.diffTime
                )
            }
        }
    }
}