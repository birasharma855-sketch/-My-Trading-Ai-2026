package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StockItem
import com.example.ui.AppScreen
import com.example.ui.MarketTab
import com.example.ui.TradeProUiState
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    uiState: TradeProUiState,
    onNavigate: (AppScreen) -> Unit,
    onSelectStock: (String, Boolean) -> Unit,
    onTabSelected: (MarketTab) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onToggleSearch: (Boolean) -> Unit,
    onNotificationClick: () -> Unit,
    onOpenAiSheet: () -> Unit,
    onOpenOrderSheet: (StockItem) -> Unit,
    onAskAiStock: (StockItem) -> Unit,
    onSendAiPrompt: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isAlertVisible by remember { mutableStateOf(true) }

    val gainers = remember(uiState.stocks) {
        uiState.stocks.filter { it.changePercent > 0 }.sortedByDescending { it.changePercent }
    }
    val losers = remember(uiState.stocks) {
        uiState.stocks.filter { it.changePercent < 0 }.sortedBy { it.changePercent }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        val isWide = maxWidth >= 620.dp

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                // 1. Header (Logo, search, notification, Indian Flag + NSE)
                item {
                    MainHeader(
                        searchQuery = uiState.searchQuery,
                        isSearchActive = uiState.isSearchActive,
                        onSearchQueryChange = onSearchQueryChange,
                        onToggleSearch = onToggleSearch,
                        onNotificationClick = onNotificationClick,
                        onAiClick = onOpenAiSheet
                    )
                }

                // Search Results Dropdown if active
                if (uiState.isSearchActive && uiState.searchResults.isNotEmpty()) {
                    item {
                        Surface(
                            color = Color.White,
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            Column {
                                uiState.searchResults.take(6).forEach { stock ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onSelectStock(stock.symbol, true) }
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(stock.symbol, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text("${stock.name} • ${stock.sector}", fontSize = 11.sp, color = NeutralMuted)
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("₹${stock.currentPrice}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text(
                                                "${if (stock.isPositive) "+" else ""}${stock.changePercent}%",
                                                fontSize = 11.sp,
                                                color = if (stock.isPositive) BullGreen else BearRed
                                            )
                                        }
                                    }
                                    Divider(color = NeutralBorder)
                                }
                            }
                        }
                    }
                }

                // 2. Indices Ticker Row (NIFTY 50, SENSEX, BANKNIFTY, INDIA VIX)
                item {
                    IndexTickerRow(
                        indices = uiState.indices,
                        onIndexClick = { onNavigate(AppScreen.OPTIONS_CHAIN) }
                    )
                }

                if (isWide) {
                    // Wide screen: Exact side-by-side layout matching screenshot
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Left Section: 10 Tools Grid + Cards below
                            Column(modifier = Modifier.weight(1.85f)) {
                                FeatureGridCards(
                                    onFeatureClick = { onNavigate(it) },
                                    modifier = Modifier.padding(horizontal = 0.dp)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Gainers, Losers, Trend, News in 3 columns
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    TopGainersCard(
                                        gainers = gainers,
                                        onSeeAllClick = { onNavigate(AppScreen.MARKETS) },
                                        onStockClick = { onSelectStock(it.symbol, true) },
                                        modifier = Modifier.weight(1.1f)
                                    )

                                    TopLosersCard(
                                        losers = losers,
                                        onSeeAllClick = { onNavigate(AppScreen.MARKETS) },
                                        onStockClick = { onSelectStock(it.symbol, true) },
                                        modifier = Modifier.weight(1.1f)
                                    )

                                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        MarketTrendCard()
                                        MarketNewsCard(onSeeAllClick = { onNavigate(AppScreen.MORE) })
                                    }
                                }
                            }

                            // Right Section: Professional AI Assistant Card
                            Column(modifier = Modifier.weight(1f)) {
                                ProfessionalAiAssistantCard(
                                    onPromptClick = { onSendAiPrompt(it) },
                                    onSendMessage = { onSendAiPrompt(it) },
                                    onExpandClick = { onNavigate(AppScreen.AI_ASSISTANT) }
                                )
                            }
                        }
                    }
                } else {
                    // Mobile Portrait Layout: Stacked seamlessly preserving every element
                    // 10 Tools Grid
                    item {
                        FeatureGridCards(
                            onFeatureClick = { onNavigate(it) }
                        )
                    }

                    // Top Gainers & Top Losers row
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TopGainersCard(
                                gainers = gainers,
                                onSeeAllClick = { onNavigate(AppScreen.MARKETS) },
                                onStockClick = { onSelectStock(it.symbol, true) },
                                modifier = Modifier.weight(1f)
                            )
                            TopLosersCard(
                                losers = losers,
                                onSeeAllClick = { onNavigate(AppScreen.MARKETS) },
                                onStockClick = { onSelectStock(it.symbol, true) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Market Trend & Market News row
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            MarketTrendCard(modifier = Modifier.weight(1f))
                            MarketNewsCard(
                                onSeeAllClick = { onNavigate(AppScreen.MORE) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Professional AI Assistant Card
                    item {
                        ProfessionalAiAssistantCard(
                            onPromptClick = { onSendAiPrompt(it) },
                            onSendMessage = { onSendAiPrompt(it) },
                            onExpandClick = { onNavigate(AppScreen.AI_ASSISTANT) },
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Bottom Alert Banner
            if (isAlertVisible) {
                BottomAlertBanner(
                    alertText = "Alert: NIFTY 50 crosses 24,800! (+0.45%)",
                    onViewDetails = { onNavigate(AppScreen.OPTIONS_CHAIN) },
                    onDismiss = { isAlertVisible = false }
                )
            }
        }
    }
}
