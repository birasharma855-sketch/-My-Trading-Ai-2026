package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StockItem
import com.example.ui.AppScreen
import com.example.ui.TradeProUiState
import com.example.ui.components.StockCardItem
import com.example.ui.theme.*

@Composable
fun WatchlistScreen(
    uiState: TradeProUiState,
    onNavigate: (AppScreen) -> Unit,
    onSelectStock: (String, Boolean) -> Unit,
    onOpenOrderSheet: (StockItem) -> Unit,
    onAskAiStock: (StockItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteStocks = uiState.stocks.filter { it.isFavorite }

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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Text(
                            text = "CUSTOM WATCHLIST",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralMuted,
                            letterSpacing = 0.5.sp
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nifty 50 High Conviction",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = TradeProBlueDark
                            )
                            Surface(
                                color = TradeProBlueContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${favoriteStocks.size} Tracked",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TradeProBluePrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            if (favoriteStocks.isEmpty()) {
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
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = TradeProBluePrimary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Your Watchlist is Empty",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = NeutralDark
                            )
                            Text(
                                text = "Bookmark your preferred stocks from the Home or Markets screen to track real-time moves here.",
                                fontSize = 12.sp,
                                color = NeutralMuted,
                                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                            )
                            Button(
                                onClick = { onNavigate(AppScreen.MARKETS) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TradeProBluePrimary)
                            ) {
                                Text("Add Stocks to Watchlist", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                items(favoriteStocks, key = { it.symbol }) { stock ->
                    StockCardItem(
                        stock = stock,
                        onClick = { onSelectStock(stock.symbol, true) },
                        onAiClick = {
                            onSelectStock(stock.symbol, false)
                            onAskAiStock(stock)
                        },
                        onOrderClick = { onOpenOrderSheet(stock) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}
