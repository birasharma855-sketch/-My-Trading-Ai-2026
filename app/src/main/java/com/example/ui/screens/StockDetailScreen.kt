package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StockItem
import com.example.ui.TradeProUiState
import com.example.ui.components.InteractiveCandlestickChart
import com.example.ui.components.OrderBookDepthView
import com.example.ui.theme.*

@Composable
fun StockDetailScreen(
    uiState: TradeProUiState,
    onBack: () -> Unit,
    onTimeframeSelected: (String) -> Unit,
    onToggleIndicator: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onOpenOrderSheet: (StockItem) -> Unit,
    onAskAi: (StockItem) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val stock = uiState.selectedStock ?: return
    val isUp = stock.isPositive
    val accentColor = if (isUp) BullGreen else BearRed
    val containerColor = if (isUp) BullGreenContainer else BearRedContainer

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NeutralBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Top App Bar
            item {
                Surface(
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier.testTag("detail_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = NeutralDark
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = stock.symbol,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Black,
                                        color = NeutralDark
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = Color(0xFFF1F5F9),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = stock.exchange,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NeutralMuted,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = stock.name,
                                    fontSize = 11.sp,
                                    color = NeutralMuted
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { onToggleFavorite(stock.symbol) },
                                modifier = Modifier.testTag("detail_fav_button")
                            ) {
                                Icon(
                                    imageVector = if (stock.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (stock.isFavorite) TradeProBluePrimary else NeutralMuted
                                )
                            }
                        }
                    }
                }
            }

            // Price & Day Range Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "₹${String.format("%,.2f", stock.currentPrice)}",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Black,
                                    color = NeutralDark
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Text(
                                        text = "${if (isUp) "+" else ""}${String.format("%.2f", stock.change)} (${if (isUp) "+" else ""}${stock.changePercent}%)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = accentColor
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Today",
                                        fontSize = 11.sp,
                                        color = NeutralMuted
                                    )
                                }
                            }

                            // Pattern Recognition Pill
                            Surface(
                                color = Color(0xFFEDE9FE),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = "PATTERN DETECTED",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF7C3AED)
                                    )
                                    Text(
                                        text = stock.topPattern,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF6D28D9)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Day High / Low Range Bar
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Day Low: ₹${stock.dayLow}", fontSize = 11.sp, color = NeutralMuted)
                                Text("Day High: ₹${stock.dayHigh}", fontSize = 11.sp, color = NeutralMuted)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            val range = stock.dayHigh - stock.dayLow
                            val progress = if (range > 0) ((stock.currentPrice - stock.dayLow) / range).toFloat().coerceIn(0f, 1f) else 0.5f
                            LinearProgressIndicator(
                                progress = { progress },
                                color = TradeProBluePrimary,
                                trackColor = Color(0xFFE2E8F0),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }
                    }
                }
            }

            // Interactive Candlestick Chart
            item {
                InteractiveCandlestickChart(
                    candles = uiState.candles,
                    timeframe = uiState.selectedTimeframe,
                    onTimeframeSelected = onTimeframeSelected,
                    showEma20 = uiState.showEma20,
                    showEma50 = uiState.showEma50,
                    showVwap = uiState.showVwap,
                    showRsi = uiState.showRsi,
                    onToggleIndicator = onToggleIndicator,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Technical Analysis Summary Gauge
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "TECHNICAL MOMENTUM SUMMARY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralMuted,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val signal = if (stock.rsi14 > 60 && stock.changePercent > 1) "STRONG BUY"
                            else if (stock.changePercent > 0) "BUY"
                            else if (stock.changePercent < -1) "STRONG SELL"
                            else "NEUTRAL"

                            val signalColor = when (signal) {
                                "STRONG BUY" -> BullGreenDark
                                "BUY" -> BullGreen
                                "STRONG SELL" -> BearRedDark
                                else -> NeutralDark
                            }

                            Column {
                                Text(
                                    text = signal,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = signalColor
                                )
                                Text(
                                    text = "Based on 14 technical oscillators & EMAs",
                                    fontSize = 11.sp,
                                    color = NeutralMuted
                                )
                            }

                            Surface(
                                color = TradeProBlueContainer,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.clickable { onAskAi(stock) }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = "AI",
                                        tint = TradeProBluePrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "AI Review",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TradeProBluePrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 5-Level Order Depth & Liquidity Ladder
            item {
                Spacer(modifier = Modifier.height(10.dp))
                uiState.orderDepth?.let { depth ->
                    OrderBookDepthView(
                        depth = depth,
                        liquidityScore = stock.liquidityScore,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Key Statistics Grid (PE, Market Cap, 20 EMA, 50 EMA, 200 EMA, VWAP)
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "KEY METRICS & MOVING AVERAGES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralMuted,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            MetricCell("Market Cap", stock.marketCap, Modifier.weight(1f))
                            MetricCell("P/E Ratio", "${stock.peRatio}x", Modifier.weight(1f))
                            MetricCell("20 EMA", "₹${stock.ema20}", Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            MetricCell("50 EMA", "₹${stock.ema50}", Modifier.weight(1f))
                            MetricCell("200 EMA", "₹${stock.ema200}", Modifier.weight(1f))
                            MetricCell("VWAP", "₹${stock.vwap}", Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Sticky Bottom Execution Dock (Ask AI + Paper Trade)
        Surface(
            color = Color.White,
            shadowElevation = 12.dp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { onAskAi(stock) },
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, TradeProBluePrimary),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("detail_ask_ai_action")
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "AI",
                        tint = TradeProBluePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "AI Guide",
                        color = TradeProBluePrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = { onOpenOrderSheet(stock) },
                    colors = ButtonDefaults.buttonColors(containerColor = BullGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .height(46.dp)
                        .testTag("detail_paper_trade_action")
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = "Trade",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Paper Trade (DEMO)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MetricCell(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 10.sp, color = NeutralMuted)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
    }
}
