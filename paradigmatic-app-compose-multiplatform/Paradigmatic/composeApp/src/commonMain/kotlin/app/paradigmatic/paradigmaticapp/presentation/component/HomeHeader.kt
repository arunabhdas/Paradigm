package app.paradigmatic.paradigmaticapp.presentation.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.paradigmatic.paradigmaticapp.domain.model.Currency
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyCode
import app.paradigmatic.paradigmaticapp.domain.model.RateStatus
import app.paradigmatic.paradigmaticapp.domain.model.CurrencyApiRequestState
import app.paradigmatic.paradigmaticapp.ui.theme.headerColor
import app.paradigmatic.paradigmaticapp.ui.theme.staleColor
import org.jetbrains.compose.resources.painterResource
import paradigmatic.composeapp.generated.resources.Res
import paradigmatic.composeapp.generated.resources.exchange_illustration
import app.paradigmatic.paradigmaticapp.util.displayCurrentDateTime
import paradigmatic.composeapp.generated.resources.refresh_ic
import paradigmatic.composeapp.generated.resources.switch_ic

@Composable
fun HomeHeader(
    status: RateStatus,
    source: CurrencyApiRequestState<Currency>,
    target: CurrencyApiRequestState<Currency>,
    amount: Double,
    onAmountChange: (Double) -> Unit,
    onRatesRefresh: () -> Unit,
    onSwitchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
            .background(headerColor)
            .padding(all = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        RatesStatusPanel(
            status = status,
            onRatesRefresh = onRatesRefresh
        )
        Spacer(modifier = Modifier.height(24.dp))
        CurrencyInputsPanel(
            source = source,
            target = target,
            onSwitchClick = onSwitchClick
        )
        Spacer(modifier = Modifier.height(24.dp))
        AmountInput(
            amount = amount,
            onAmountChange = onAmountChange
        )

    }

}

@Composable
fun RatesStatusPanel(
    status: RateStatus,
    onRatesRefresh: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(Res.drawable.exchange_illustration),
                contentDescription = "Exchange Rate Illustration"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = displayCurrentDateTime(),
                    color = Color.White
                )
                Text(
                    text = status.title,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = status.color
                )
            }
        }
       IconButton(onClick = onRatesRefresh) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.refresh_ic),
                contentDescription = "Refresh Icon",
                tint = staleColor
            )
        }
    }
}


@Composable
fun CurrencyInputsPanel(
    source: CurrencyApiRequestState<Currency>,
    target: CurrencyApiRequestState<Currency>,
    onSwitchClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrencyViewPanel(
            placeholder = "from",
            currency = source,
            onClick = {}
        )
        Spacer(modifier = Modifier.height(14.dp))
        IconButton(
            modifier = Modifier.padding(top = 24.dp),
            onClick = onSwitchClick
        ) {
            Icon(
                painter = painterResource(Res.drawable.switch_ic),
                contentDescription = "Switch Icon",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        CurrencyViewPanel(
            placeholder = "to",
            currency = target,
            onClick = {}
        )
    }
}

@Composable
fun RowScope.CurrencyViewPanel(
    placeholder: String,
    currency: CurrencyApiRequestState<Currency>,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.weight(1f)) {
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = placeholder,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(size = 8.dp))
                .background(Color.White.copy(alpha = 0.05f))
                .height(54.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (currency.isSuccess()) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(
                        CurrencyCode.valueOf(
                            currency.getSuccessData().code
                        ).flag
                    ),
                    tint = Color.Unspecified,
                    contentDescription = "Country Flag"
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = CurrencyCode.valueOf(currency.getSuccessData().code).name,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun AmountInput(
    amount: Double,
    onAmountChange: (Double) -> Unit
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(size = 8.dp))
            .animateContentSize()
            .height(54.dp),
        value = "$amount",
        onValueChange = { onAmountChange(it.toDouble()) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.05f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
            disabledContainerColor = Color.White.copy(alpha = 0.05f),
            errorContainerColor = Color.White.copy(alpha = 0.05f),
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White
        ),
        textStyle = TextStyle(
            color = Color.White,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        )
    )
}