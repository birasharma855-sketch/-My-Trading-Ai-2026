package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.MarketNews
import com.example.data.model.StockItem
import com.example.ui.MarketTab
import com.example.ui.theme.*

@Composable
fun MarketSectionTabs(
    selectedTab: MarketTab,
    onTabSelected: (MarketTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        MarketTab.GAINERS to "Top Gainers",
        MarketTab.LOSERS to "Top Losers",
        MarketTab.UPTREND to "Uptrend",
        MarketTab.DOWNTREND to "Downtrend",
        MarketTab.LIQUIDITY to "Liquidity",
        MarketTab.NEWS to "Market News"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "MARKET PULSE & MOMENTUM",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = NeutralMuted,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tabs) { (tab, label) ->
                val isSelected = tab == selectedTab
                FilterChip(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    label = {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TradeProBluePrimary,
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = NeutralMedium
                    ),
                    shape = RoundedCornerShape(20.dp),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) TradeProBluePrimary else NeutralBorder
                    ),
                    modifier = Modifier.testTag("market_tab_${tab.name}")
                )
            }
        }
    }
}

@Composable
fun StockCardItem(
    stock: StockItem,
    onClick: () -> Unit,
    onAiClick: () -> Unit,
    onOrderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isUp = stock.isPositive
    val accentColor = if (isUp) BullGreen else BearRed
    val containerColor = if (isUp) BullGreenContainer else BearRedContainer

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("stock_item_${stock.symbol}")
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
                // Symbol, Name & Exchange
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stock.symbol,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
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
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "${stock.name} • ${stock.sector}",
                        fontSize = 11.sp,
                        color = NeutralMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Price & % Change
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${String.format("%,.2f", stock.currentPrice)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = NeutralDark
                    )
                    Surface(
                        color = containerColor,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "${if (isUp) "+" else ""}${String.format("%.2f", stock.changePercent)}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badges row: Trend, Pattern, Volume, RSI
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Pattern / Trend Pill
                Surface(
                    color = Color(0xFFEDE9FE),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF7C3AED),
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = stock.topPattern,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF7C3AED)
                        )
                    }
                }

                // RSI
                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "RSI: ${stock.rsi14.toInt()}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (stock.rsi14 >= 70) BearRed else if (stock.rsi14 <= 30) BullGreen else NeutralMedium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Volume
                Text(
                    text = "Vol: ${(stock.volume / 100000.0).toInt()}L",
                    fontSize = 10.sp,
                    color = NeutralMuted
                )

                Spacer(modifier = Modifier.weight(1f))

                // Action buttons: Ask AI and Trade
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilledTonalButton(
                        onClick = onAiClick,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("stock_ai_${stock.symbol}")
                    ) {
                        Text("Ask AI", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onOrderClick,
                        colors = ButtonDefaults.buttonColors(containerColor = TradeProBluePrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("stock_trade_${stock.symbol}")
                    ) {
                        Text("Trade", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsCardItem(
    news: MarketNews,
    modifier: Modifier = Modifier
) {
    val sentimentColor = when (news.sentiment) {
        "Bullish" -> BullGreen
        "Bearish" -> BearRed
        else -> TradeProBluePrimary
    }
    val sentimentBg = when (news.sentiment) {
        "Bullish" -> BullGreenContainer
        "Bearish" -> BearRedContainer
        else -> TradeProBlueContainer
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("news_card_${news.id}")
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
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = news.source,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TradeProBlueDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${news.timeAgo}",
                        fontSize = 11.sp,
                        color = NeutralMuted
                    )
                }

                Surface(
                    color = sentimentBg,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = news.sentiment,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = sentimentColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = news.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralDark,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = news.summary,
                fontSize = 12.sp,
                color = NeutralMedium,
                lineHeight = 17.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
