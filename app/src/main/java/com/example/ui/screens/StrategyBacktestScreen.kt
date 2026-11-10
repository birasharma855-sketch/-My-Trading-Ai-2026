package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TradingStrategy
import com.example.ui.TradeProUiState
import com.example.ui.theme.*

@Composable
fun StrategyBacktestScreen(
    uiState: TradeProUiState,
    onBack: () -> Unit,
    onSelectStrategy: (TradingStrategy) -> Unit,
    onAskAiStrategy: (TradingStrategy) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val strategies = uiState.strategies
    val activeStrat = uiState.selectedStrategy ?: strategies.firstOrNull()

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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
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
                                text = "TRADING STRATEGIES & BACKTEST",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralMuted
                            )
                            Text(
                                text = "Systematic Indian Market Setups",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = TradeProBlueDark
                            )
                        }
                    }
                }
            }

            // Strategy Selector Tabs
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "SELECT STRATEGY SETUP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    strategies.forEach { strat ->
                        val isSelected = strat.id == activeStrat?.id
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) TradeProBlueContainer else Color.White
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (isSelected) TradeProBluePrimary else NeutralBorder
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onSelectStrategy(strat) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = strat.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) TradeProBlueDark else NeutralDark
                                    )
                                    Text(
                                        text = "${strat.category} • R:R ${strat.riskReward}",
                                        fontSize = 11.sp,
                                        color = NeutralMuted
                                    )
                                }
                                Surface(
                                    color = if (strat.winRate >= 68) BullGreenContainer else Color(0xFFEFF6FF),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${strat.winRate}% Win Rate",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (strat.winRate >= 68) BullGreenDark else TradeProBluePrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Active Strategy Deep Dive Card
            if (activeStrat != null) {
                item {
                    Spacer(modifier = Modifier.height(14.dp))
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
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "STRATEGY BACKTEST METRICS (NSE 2-YEAR)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralMuted
                                )
                                Surface(
                                    color = BullGreenContainer,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "VERIFIED",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BullGreenDark,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Metric pills row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                BacktestStatCell("Win Rate", "${activeStrat.winRate}%", BullGreen)
                                BacktestStatCell("Risk / Reward", activeStrat.riskReward, TradeProBluePrimary)
                                BacktestStatCell("Profit Factor", "2.18", BullGreenDark)
                                BacktestStatCell("Max Drawdown", "4.2%", BearRed)
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Divider(color = NeutralBorder)
                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Strategy Mechanics & Rules:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralDark
                            )

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = activeStrat.description,
                                fontSize = 12.sp,
                                color = NeutralMedium,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Entry Rule
                            Surface(
                                color = Color(0xFFF0FDF4),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("🟢 Entry Rule:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = BullGreenDark)
                                    Text(activeStrat.entryRule, fontSize = 11.sp, color = NeutralDark)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Exit & SL Rule
                            Surface(
                                color = Color(0xFFFEF2F2),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("🔴 Exit & Stop-Loss Rule:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = BearRedDark)
                                    Text(activeStrat.exitRule, fontSize = 11.sp, color = NeutralDark)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Hindi Explanation Box
                            Surface(
                                color = Color(0xFFF8FAFC),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeutralBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("🇮🇳 हिंदी व्याख्या (Hindi Summary):", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TradeProBlueDark)
                                    Text(activeStrat.hindiSummary, fontSize = 11.sp, color = NeutralMedium)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = { onAskAiStrategy(activeStrat) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TradeProBluePrimary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "AI",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ask AI to Analyze Current Stocks with this Strategy", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BacktestStatCell(
    title: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 10.sp, color = NeutralMuted)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Black, color = color)
    }
}
