package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun NotificationDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = TradeProBluePrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Live Market Alerts",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = NeutralDark
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AlertItem(
                    title = "Nifty 50 Breakout",
                    message = "Nifty crosses 24,350 key resistance with institutional call unwinding.",
                    time = "10 mins ago",
                    isBullish = true
                )
                AlertItem(
                    title = "Tata Motors Volume Spike",
                    message = "Volume surge 2.4x above 20-day moving average. Strong buying detected.",
                    time = "32 mins ago",
                    isBullish = true
                )
                AlertItem(
                    title = "India VIX Drop",
                    message = "Volatility index drops 4.41% to 13.45. Options premiums normalizing.",
                    time = "1 hour ago",
                    isBullish = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("dismiss_notifications_btn")
            ) {
                Text("Dismiss", fontWeight = FontWeight.Bold, color = TradeProBluePrimary)
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White
    )
}

@Composable
fun AlertItem(
    title: String,
    message: String,
    time: String,
    isBullish: Boolean
) {
    Surface(
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, NeutralBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NeutralDark)
                Text(time, fontSize = 10.sp, color = NeutralMuted)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(message, fontSize = 11.sp, color = NeutralMedium, lineHeight = 15.sp)
        }
    }
}
