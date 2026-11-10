package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StockItem

@Composable
fun TopGainersCard(
    gainers: List<StockItem>,
    onSeeAllClick: () -> Unit,
    onStockClick: (StockItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🌲", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Top Gainers",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "▲",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }

                Text(
                    text = "See All >",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2563EB),
                    modifier = Modifier.clickable { onSeeAllClick() }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Stock Rows
            gainers.take(5).forEach { stock ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onStockClick(stock) }
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stock.symbol.take(1),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0284C7)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stock.symbol,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E293B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹${String.format("%,.2f", stock.currentPrice)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+${String.format("%.2f", stock.changePercent)}%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopLosersCard(
    losers: List<StockItem>,
    onSeeAllClick: () -> Unit,
    onStockClick: (StockItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Top Losers",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "▼",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444)
                    )
                }

                Text(
                    text = "See All >",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2563EB),
                    modifier = Modifier.clickable { onSeeAllClick() }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Stock Rows
            losers.take(5).forEach { stock ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onStockClick(stock) }
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFE4E6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stock.symbol.take(1),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE11D48)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stock.symbol,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E293B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹${String.format("%,.2f", stock.currentPrice)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${String.format("%.2f", stock.changePercent)}%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MarketTrendCard(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Market Trend",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⬆", fontSize = 10.sp, color = Color(0xFF10B981))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Uptrend", fontSize = 10.sp, color = Color(0xFF64748B))
                }
                Text("842", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⬇", fontSize = 10.sp, color = Color(0xFFEF4444))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Downtrend", fontSize = 10.sp, color = Color(0xFF64748B))
                }
                Text("703", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("↔", fontSize = 10.sp, color = Color(0xFF64748B))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Sideways", fontSize = 10.sp, color = Color(0xFF64748B))
                }
                Text("215", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
            }

            Divider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💧", fontSize = 10.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Liquidity",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF10B981)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("High Liquidity", fontSize = 10.sp, color = Color(0xFF64748B))
                }
                Text("1,260", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Low Liquidity", fontSize = 10.sp, color = Color(0xFF64748B))
                }
                Text("500", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
            }
        }
    }
}

@Composable
fun MarketNewsCard(
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Market News 🔥",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "See All >",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2563EB),
                    modifier = Modifier.clickable { onSeeAllClick() }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // News Item 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFE2E8F0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏛️", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "SEBI Tightens Rules on F&O Trading, New Guidelines Soon",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        lineHeight = 13.sp,
                        maxLines = 2
                    )
                    Text(
                        text = "2m ago • Economic Times",
                        fontSize = 9.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // News Item 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🪙", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "RBI Keeps Repo Rate Unchanged at 5.50%",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        lineHeight = 13.sp,
                        maxLines = 2
                    )
                    Text(
                        text = "5m ago • Business Today",
                        fontSize = 9.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomAlertBanner(
    alertText: String = "Alert: NIFTY 50 crosses 24,800! (+0.45%)",
    onViewDetails: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFFDCFCE7),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = alertText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF14532D)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "View Details →",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2563EB),
                    modifier = Modifier.clickable { onViewDetails() }
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color(0xFF64748B),
                    modifier = Modifier
                        .size(14.dp)
                        .clickable { onDismiss() }
                )
            }
        }
    }
}
