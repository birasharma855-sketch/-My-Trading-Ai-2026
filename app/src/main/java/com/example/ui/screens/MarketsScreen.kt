package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StockItem
import com.example.ui.TradeProUiState
import com.example.ui.components.StockCardItem
import com.example.ui.theme.*

@Composable
fun MarketsScreen(
    uiState: TradeProUiState,
    onSelectStock: (String, Boolean) -> Unit,
    onOpenOrderSheet: (StockItem) -> Unit,
    onAskAiStock: (StockItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSector by remember { mutableStateOf("All") }
    var filterQuery by remember { mutableStateOf("") }

    val sectors = listOf("All", "Information Tech", "Private Sector Bank", "Automobiles", "Oil, Gas & Cons. Goods", "Internet & Consumer", "Metals & Mining")

    val filteredList = uiState.stocks.filter { stock ->
        val matchesSector = selectedSector == "All" || stock.sector == selectedSector
        val matchesQuery = filterQuery.isBlank() ||
                stock.symbol.contains(filterQuery, ignoreCase = true) ||
                stock.name.contains(filterQuery, ignoreCase = true)
        matchesSector && matchesQuery
    }

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
                            text = "NSE & BSE MARKET DIRECTORY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralMuted,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Live Stock Screener & Quotes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TradeProBlueDark
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Search Field
                        OutlinedTextField(
                            value = filterQuery,
                            onValueChange = { filterQuery = it },
                            placeholder = { Text("Filter by company, symbol or ticker...", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = TradeProBluePrimary)
                            },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("screener_search_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Sector filter chips
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(sectors) { sec ->
                                val isSelected = sec == selectedSector
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedSector = sec },
                                    label = { Text(sec, fontSize = 11.sp) },
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Sector Performance Heatmap Cards
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "SECTORAL PERFORMANCE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SectorMiniCard("Nifty IT", "+1.85%", true, Modifier.weight(1f))
                        SectorMiniCard("Nifty Bank", "-0.21%", false, Modifier.weight(1f))
                        SectorMiniCard("Nifty Auto", "+2.45%", true, Modifier.weight(1f))
                        SectorMiniCard("Nifty Metal", "-1.80%", false, Modifier.weight(1f))
                    }
                }
            }

            // Results count
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Stocks (${filteredList.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralDark
                    )
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "DEMO QUOTES",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralMuted,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Stocks List
            items(filteredList, key = { it.symbol }) { stock ->
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

@Composable
fun SectorMiniCard(
    name: String,
    change: String,
    isUp: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
            Text(
                change,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = if (isUp) BullGreen else BearRed
            )
        }
    }
}
