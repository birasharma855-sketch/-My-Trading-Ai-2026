package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderType
import com.example.data.model.PaperTradePosition
import com.example.ui.AppScreen
import com.example.ui.TradeProUiState
import com.example.ui.theme.*

@Composable
fun PortfolioScreen(
    uiState: TradeProUiState,
    onNavigate: (AppScreen) -> Unit,
    onClosePosition: (String, Double) -> Unit,
    onResetPortfolio: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Open, 1: History
    val openPositions = uiState.positions
    val totalUnrealizedPnl = openPositions.sumOf { it.pnl }
    val isOverallProfit = totalUnrealizedPnl >= 0

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NeutralBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Header
            item {
                Surface(
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "PAPER TRADING PORTFOLIO",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralMuted,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Virtual Margin Account (DEMO)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = TradeProBlueDark
                            )
                        }

                        IconButton(
                            onClick = onResetPortfolio,
                            modifier = Modifier.testTag("reset_portfolio_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Demo Capital",
                                tint = NeutralMuted
                            )
                        }
                    }
                }
            }

            // Portfolio Balance & P&L Hero Card
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = TradeProBluePrimary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Available Margin (Cash)",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "₹${String.format("%,.2f", uiState.availableCash)}",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }

                            // Unrealized P&L Badge
                            Surface(
                                color = if (isOverallProfit) BullGreen else BearRed,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = "UNREALIZED P&L",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                    Text(
                                        text = "${if (isOverallProfit) "+" else ""}₹${String.format("%,.2f", totalUnrealizedPnl)}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = Color.White.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Open Positions: ${openPositions.size}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
                            Text("Starting Capital: ₹10,00,000", fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                    }
                }
            }

            // Tabs: Open Positions vs Closed History
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        label = { Text("Open Positions (${openPositions.size})", fontWeight = FontWeight.Bold) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        label = { Text("Trade History (${uiState.closedPositions.size})", fontWeight = FontWeight.Bold) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Position Items or Empty State
            if (selectedTab == 0) {
                if (openPositions.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = TradeProBluePrimary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "No Open Positions",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = NeutralDark
                                )
                                Text(
                                    text = "Practice risk-free trading with ₹10,00,000 virtual demo money.",
                                    fontSize = 12.sp,
                                    color = NeutralMuted,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                                )
                                Button(
                                    onClick = { onNavigate(AppScreen.MARKETS) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = TradeProBluePrimary)
                                ) {
                                    Text("Explore Markets & Trade", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    items(openPositions, key = { it.id }) { pos ->
                        PositionCardItem(
                            pos = pos,
                            onSquareOff = { onClosePosition(pos.id, pos.currentPrice) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            } else {
                if (uiState.closedPositions.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No completed trades yet.", color = NeutralMuted, fontSize = 13.sp)
                        }
                    }
                } else {
                    items(uiState.closedPositions, key = { it.id }) { pos ->
                        PositionCardItem(
                            pos = pos,
                            onSquareOff = null,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PositionCardItem(
    pos: PaperTradePosition,
    onSquareOff: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val isProfit = pos.isProfit
    val accentColor = if (isProfit) BullGreen else BearRed
    val containerColor = if (isProfit) BullGreenContainer else BearRedContainer

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = pos.symbol,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = if (pos.type == OrderType.BUY) BullGreenContainer else BearRedContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "${pos.type.name} • ${pos.productType.name}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (pos.type == OrderType.BUY) BullGreen else BearRed,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // P&L Badge
                Surface(
                    color = containerColor,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${if (isProfit) "+" else ""}₹${String.format("%.2f", pos.pnl)} (${if (isProfit) "+" else ""}${String.format("%.2f", pos.pnlPercent)}%)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Details row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Qty", fontSize = 10.sp, color = NeutralMuted)
                    Text("${pos.quantity}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
                }
                Column {
                    Text("Avg Buy", fontSize = 10.sp, color = NeutralMuted)
                    Text("₹${pos.avgPrice}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
                }
                Column {
                    Text("LTP", fontSize = 10.sp, color = NeutralMuted)
                    Text("₹${pos.currentPrice}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TradeProBlueDark)
                }
                if (pos.stopLoss != null) {
                    Column {
                        Text("SL", fontSize = 10.sp, color = NeutralMuted)
                        Text("₹${pos.stopLoss}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BearRed)
                    }
                }
            }

            if (onSquareOff != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onSquareOff,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9), contentColor = NeutralDark),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                ) {
                    Text("Square Off Position", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
