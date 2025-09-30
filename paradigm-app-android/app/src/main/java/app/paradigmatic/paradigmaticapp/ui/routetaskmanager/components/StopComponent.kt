package app.paradigmatic.paradigmaticapp.ui.routetaskmanager.components

import app.paradigmatic.paradigmaticapp.R
import app.paradigmatic.paradigmaticapp.presentation.data.model.Stop
import app.paradigmatic.paradigmaticapp.ui.theme.LightBlue
import app.paradigmatic.paradigmaticapp.ui.theme.LightGreen
import app.paradigmatic.paradigmaticapp.ui.theme.LightPurple
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun StopComponent(
    stopItem: Stop,
    onClick: () -> Unit
) {
    val routeTaskColor = listOf(LightPurple, LightBlue, LightGreen).random()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(
            text = "  ${stopItem.orderingNumber}",
            fontFamily = FontFamily(Font(R.font.nunito_bold)),
            textAlign = TextAlign.Center,
            color = Color.White

        )
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .border(
                        border = BorderStroke(3.dp, Color.White),
                        shape = CircleShape
                    )
            )

            Divider(modifier = Modifier.width(6.dp), color = Color.Black)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(routeTaskColor)
                        .weight(0.9f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stopItem.title,
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                        modifier = Modifier
                            .padding(
                                top = 12.dp,
                                start = 12.dp
                            )
                    )

                    if (stopItem.description != null) {
                        Text(
                            text = stopItem.description,
                            fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            modifier = Modifier
                                .padding(
                                    start = 12.dp
                                ),
                            color = Color.Gray
                        )
                    }
                    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    val formattedOriginStart = LocalDateTime.parse(
                        stopItem.originWindowRangeStart.toString(), dateFormatter
                    )
                    val formattedDestinationStart = LocalDateTime.parse(
                        stopItem.destinationWindowRangeStart.toString(), dateFormatter
                    )
                    val resultOriginStart = DateTimeFormatter.ofPattern(
                        "MMMM dd, yyyy | hh:mma").format(formattedOriginStart)
                    val resultDestinationStart = DateTimeFormatter.ofPattern(
                        "MMMM dd, yyyy | hh:mma").format(formattedDestinationStart)
                    Text(
                        text = "Time Window: ${resultOriginStart} " +
                                "- ${resultDestinationStart}",
                        fontFamily = FontFamily(Font(R.font.nunito_bold)),
                        modifier = Modifier
                            .padding(
                                start = 12.dp,
                                bottom = 12.dp
                            ),
                    )
                }

                Divider(
                    modifier = Modifier
                        .width(6.dp)
                        .weight(0.1f),
                    color = Color.White
                )
            }
        }
    }
}

/*
@Preview
@Composable
fun StopComponentPreview() {
    StopComponent(
        stopItem = routeTaskList
    )
}
*/