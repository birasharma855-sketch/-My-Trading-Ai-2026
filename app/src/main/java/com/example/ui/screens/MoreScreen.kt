package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.TradeProUiState
import com.example.ui.theme.*

@Composable
fun MoreScreen(
    uiState: TradeProUiState,
    onNavigate: (AppScreen) -> Unit,
    onResetPortfolio: () -> Unit,
    onToggleLanguage: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedTopic by remember { mutableStateOf<String?>(null) }

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
                            text = "TRADEPRO ACADEMY & HUB",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralMuted,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Education, Compliance & App Center",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TradeProBlueDark
                        )
                    }
                }
            }

            // Trading Education Modules
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "INDIAN STOCK MARKET EDUCATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralMuted
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    EducationAccordionItem(
                        title = "1. Intraday Trading Mastery (MIS Rules)",
                        subtitle = "NSE Timing (9:15 AM - 3:30 PM), 5x margin leverage, square-off timing, and stop-loss discipline.",
                        icon = Icons.Default.FlashOn,
                        isExpanded = expandedTopic == "intraday",
                        onToggle = { expandedTopic = if (expandedTopic == "intraday") null else "intraday" },
                        details = """
                            • **Market Hours:** 9:00 - 9:08 AM (Pre-market), 9:15 AM - 3:30 PM (Regular Trading).
                            • **Auto Square-Off:** Intraday (MIS) orders are automatically closed by brokers at 3:15-3:20 PM.
                            • **First 15-Minute Rule:** Avoid trading during 9:15-9:30 AM unless experienced; wait for the Initial Balance (IB) range to establish.
                            • **Strict Stop-Loss:** Never risk more than 1% of your intraday trading margin on any single position.
                        """.trimIndent()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    EducationAccordionItem(
                        title = "2. Swing & Delivery Trading (CNC)",
                        subtitle = "Multi-day positional setups, 50 & 200 EMA support zones, and fundamental momentum.",
                        icon = Icons.Default.Timeline,
                        isExpanded = expandedTopic == "swing",
                        onToggle = { expandedTopic = if (expandedTopic == "swing") null else "swing" },
                        details = """
                            • **Holding Period:** 3 days to 4 weeks, riding medium-term trend waves.
                            • **Golden Cross:** 50-day EMA crossing above the 200-day EMA signals institutional accumulation.
                            • **Risk/Reward:** Seek minimum 1:2.5 to 1:3.0 targets with wider stop-losses placed below major support swing lows.
                        """.trimIndent()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    EducationAccordionItem(
                        title = "3. Candlestick Patterns Decoded",
                        subtitle = "Hammer, Morning Star, Bullish Engulfing, Doji, and volume confirmation.",
                        icon = Icons.Default.CandlestickChart,
                        isExpanded = expandedTopic == "candles",
                        onToggle = { expandedTopic = if (expandedTopic == "candles") null else "candles" },
                        details = """
                            • **Hammer:** Long lower wick (2x body) forming at downswing bottoms denotes aggressive buyer absorption.
                            • **Bullish Engulfing:** Large green body fully engulfs the preceding red candle; signals strong shift in control.
                            • **Doji:** Indecision between bulls and bears; look for breakout confirmation on the subsequent candle.
                        """.trimIndent()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    EducationAccordionItem(
                        title = "4. Options Chain, Greeks & PCR",
                        subtitle = "Calls, Puts, Delta, Theta decay, Put-Call Ratio (PCR) and Max Pain.",
                        icon = Icons.Default.AltRoute,
                        isExpanded = expandedTopic == "options",
                        onToggle = { expandedTopic = if (expandedTopic == "options") null else "options" },
                        details = """
                            • **Put-Call Ratio (PCR):** Total Put Open Interest divided by Call Open Interest.
                              - PCR < 0.70: Extreme pessimism, often marks a market bottom / oversold bounce zone.
                              - PCR > 1.30: Euphoric call buying, risk of sudden profit-taking pullback.
                            • **Max Pain:** Strike price where option writers (institutions) experience minimum total financial loss on weekly expiry.
                        """.trimIndent()
                    )
                }
            }

            // Google Play Store Publishing Hub Card
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "GOOGLE PLAY STORE PUBLISHING WORKFLOW",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralMuted
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Shop, contentDescription = null, tint = TradeProBluePrimary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Ready for Google Play Release",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeutralDark
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Application ID: com.tradepro.aiindia\n• Target SDK: 36 (Android 15+ ready)\n• Custom Adaptive Icon: Generated & configured\n• Release AAB Build: gradle bundleRelease\n• Signing Configs: Pre-configured debug & upload keys",
                                fontSize = 12.sp,
                                color = NeutralMedium,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // SEBI Compliance & Risk Disclaimer
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WarningAmber, contentDescription = null, tint = BearRed)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SEBI Compliance & Risk Disclaimer",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BearRedDark
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "TRADEPRO AI INDIA is strictly an educational analysis and simulation application. It does NOT provide SEBI-registered investment advice or tip services. All market quotes and candlestick data are simulated demo feeds for learning purposes. No real money or actual trades are executed. Past algorithmic performance does not guarantee future market returns.",
                            fontSize = 11.sp,
                            color = NeutralDark,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // App Actions: Language, Reset, Info
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "APP SETTINGS & PREFERENCES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralMuted
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Toggle Language
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onToggleLanguage() }
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Translate, contentDescription = null, tint = TradeProBluePrimary)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("AI Language Preference", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                }
                                Text(
                                    if (uiState.aiLanguage == "hi") "हिन्दी (HI)" else "English (EN)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TradeProBluePrimary
                                )
                            }

                            Divider(color = NeutralBorder)

                            // Reset Demo Capital
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onResetPortfolio() }
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, tint = BearRed)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text("Reset Paper Portfolio (₹10 Lakh)", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NeutralMuted)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EducationAccordionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    details: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TradeProBluePrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NeutralDark)
                    Text(subtitle, fontSize = 11.sp, color = NeutralMuted)
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = NeutralMuted
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = details,
                        fontSize = 12.sp,
                        color = NeutralDark,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}
