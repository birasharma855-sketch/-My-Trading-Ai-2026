package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OptionStrike
import com.example.ui.TradeProUiState
import com.example.ui.theme.*

@Composable
fun OptionsChainScreen(
    uiState: TradeProUiState,
    onBack: () -> Unit,
    onSelectSymbol: (String) -> Unit,
    onAskAi: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val currentStock = uiState.selectedStock ?: return
    val chain = uiState.optionChain

    val symbols = listOf("NIFTY 50", "BANK NIFTY", "RELIANCE", "TATAMOTORS", "HDFCBANK", "INFY")
    val totalCallOi = chain.sumOf { it.callOi }
    val totalPutOi = chain.sumOf { it.putOi }
    val pcr = if (totalCallOi > 0) totalPutOi.toDouble() / totalCallOi.toDouble() else 1.0
    val atmStrike = chain.find { it.isAtm } ?: chain.getOrNull(chain.size / 2)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NeutralBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
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
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = NeutralDark
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text(
                                    text = "DERIVATIVES OPTIONS CHAIN",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralMuted
                                )
                                Text(
                                    text = "${currentStock.symbol} (Spot: ₹${currentStock.currentPrice})",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TradeProBlueDark
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Selector row for symbols
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(symbols) { sym ->
                                val isSelected = sym.equals(currentStock.symbol, ignoreCase = true)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onSelectSymbol(sym) },
                                    label = { Text(sym, fontSize = 11.sp) },
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // PCR & Max Pain Analytics Card
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // PCR
                            Column {
                                Text("Put-Call Ratio (PCR)", fontSize = 11.sp, color = NeutralMuted)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = String.format("%.2f", pcr),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (pcr > 1.2) BullGreenDark else if (pcr < 0.8) BearRedDark else TradeProBlueDark
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = Color(0xFFF1F5F9),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = if (pcr > 1.2) "BULLISH BIAS" else if (pcr < 0.8) "OVERSOLD" else "NEUTRAL",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NeutralMedium,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            // Max Pain Strike
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Calculated Max Pain", fontSize = 11.sp, color = NeutralMuted)
                                Text(
                                    text = "₹${atmStrike?.strikePrice?.toInt() ?: 24350}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralDark
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // AI Analysis Prompt
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = TradeProBlueContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAskAi("What does the current Option Chain PCR of ${String.format("%.2f", pcr)} mean for ${currentStock.symbol}?") }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "💡 Ask AI: Explain PCR & Option Expiry Sentiment",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TradeProBluePrimary
                                )
                                Text("Analyze →", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TradeProBluePrimary)
                            }
                        }
                    }
                }
            }

            // Options Table Header
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
                            .padding(12.dp)
                    ) {
                        // Section headers (CALLS on Left, STRIKE in Center, PUTS on Right)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF8FAFC), shape = RoundedCornerShape(8.dp))
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("CALLS (LTP)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BullGreen, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Start)
                            Text("OI", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeutralMuted, modifier = Modifier.weight(0.9f), textAlign = TextAlign.Center)
                            Text("STRIKE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = TradeProBlueDark, modifier = Modifier.weight(1.1f), textAlign = TextAlign.Center)
                            Text("OI", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeutralMuted, modifier = Modifier.weight(0.9f), textAlign = TextAlign.Center)
                            Text("PUTS (LTP)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BearRed, modifier = Modifier.weight(1.2f), textAlign = TextAlign.End)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Strikes Ladder
                        chain.forEach { strike ->
                            val isAtm = strike.isAtm
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isAtm) Color(0xFFFEF3C7) else Color.Transparent,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(vertical = 5.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "₹${strike.callLtp}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BullGreen,
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = TextAlign.Start
                                )
                                Text(
                                    text = "${(strike.callOi / 1000)}k",
                                    fontSize = 10.sp,
                                    color = NeutralMedium,
                                    modifier = Modifier.weight(0.9f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "${strike.strikePrice.toInt()}${if (isAtm) " (ATM)" else ""}",
                                    fontSize = 11.sp,
                                    fontWeight = if (isAtm) FontWeight.Black else FontWeight.Bold,
                                    color = if (isAtm) Color(0xFF92400E) else NeutralDark,
                                    modifier = Modifier.weight(1.1f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "${(strike.putOi / 1000)}k",
                                    fontSize = 10.sp,
                                    color = NeutralMedium,
                                    modifier = Modifier.weight(0.9f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "₹${strike.putLtp}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BearRed,
                                    modifier = Modifier.weight(1.2f),
                                    textAlign = TextAlign.End
                                )
                            }
                            Divider(color = NeutralBorder.copy(alpha = 0.5f))
                        }
                    }
                }
            }

            // Compliance Note
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "DEMO OPTIONS CHAIN: For educational modeling of Greeks, Open Interest (OI) concentration and Put-Call Ratio dynamics.",
                    fontSize = 10.sp,
                    color = NeutralMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
        }
    }
}
