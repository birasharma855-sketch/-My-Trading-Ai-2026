package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun MainHeader(
    searchQuery: String,
    isSearchActive: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onToggleSearch: (Boolean) -> Unit,
    onNotificationClick: () -> Unit,
    onAiClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Logo & Subtitle: "TradePro AI" + "Trade • Learn • Grow"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onToggleSearch(false) }
                ) {
                    TradeProLogoIcon(modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "TradePro ",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0D3B66),
                                letterSpacing = (-0.3).sp
                            )
                            Text(
                                text = "AI",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF00A86B),
                                letterSpacing = (-0.3).sp
                            )
                        }
                        Text(
                            text = "Trade • Learn • Grow",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = NeutralMuted,
                            letterSpacing = 0.2.sp
                        )
                    }
                }

                // 2. Center Search Bar (compact)
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        onSearchQueryChange(it)
                        if (it.isNotBlank() && !isSearchActive) onToggleSearch(true)
                    },
                    placeholder = {
                        Text(
                            "Search Stock, News, Indicators...",
                            fontSize = 11.sp,
                            color = NeutralMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF0D3B66),
                            modifier = Modifier.size(17.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                onSearchQueryChange("")
                                onToggleSearch(false)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = NeutralMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFCBD5E1),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(42.dp)
                        .testTag("market_search_input")
                )

                // 3. Right Icons: Notification Bell (with red badge 3) and Indian Flag + NSE Pill
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onNotificationClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("notification_button")
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color(0xFFE11D48),
                                    contentColor = Color.White
                                ) {
                                    Text("3", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Indian Flag + NSE Pill
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            IndianFlagIcon(modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "NSE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TradeProLogoIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Draw green/cyan growing bar chart with rising arrow
        val barWidth = w * 0.16f
        val spacing = w * 0.08f

        // Bar 1 (lowest)
        drawRoundRect(
            color = Color(0xFF00C9A7),
            topLeft = Offset(w * 0.10f, h * 0.55f),
            size = androidx.compose.ui.geometry.Size(barWidth, h * 0.40f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
        )

        // Bar 2 (mid)
        drawRoundRect(
            color = Color(0xFF00A86B),
            topLeft = Offset(w * 0.10f + barWidth + spacing, h * 0.35f),
            size = androidx.compose.ui.geometry.Size(barWidth, h * 0.60f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
        )

        // Bar 3 (highest)
        drawRoundRect(
            color = Color(0xFF059669),
            topLeft = Offset(w * 0.10f + (barWidth + spacing) * 2, h * 0.15f),
            size = androidx.compose.ui.geometry.Size(barWidth, h * 0.80f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
        )

        // Rising Trendline over bars
        val path = Path().apply {
            moveTo(w * 0.08f, h * 0.65f)
            lineTo(w * 0.32f, h * 0.45f)
            lineTo(w * 0.58f, h * 0.30f)
            lineTo(w * 0.90f, h * 0.08f)
        }
        drawPath(
            path = path,
            color = Color(0xFF00E676),
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = 2.5.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        )
    }
}

@Composable
fun IndianFlagIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.clip(CircleShape)) {
        val w = size.width
        val h = size.height
        val stripeH = h / 3f

        // Saffron
        drawRect(Color(0xFFFF9933), topLeft = Offset(0f, 0f), size = androidx.compose.ui.geometry.Size(w, stripeH))
        // White
        drawRect(Color(0xFFFFFFFF), topLeft = Offset(0f, stripeH), size = androidx.compose.ui.geometry.Size(w, stripeH))
        // Green
        drawRect(Color(0xFF138808), topLeft = Offset(0f, stripeH * 2), size = androidx.compose.ui.geometry.Size(w, stripeH))
        // Blue Chakra Dot
        drawCircle(Color(0xFF000080), radius = stripeH * 0.35f, center = Offset(w / 2f, h / 2f))
    }
}
