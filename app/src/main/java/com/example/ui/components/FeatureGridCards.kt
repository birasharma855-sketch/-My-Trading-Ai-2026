package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen

data class ToolGridItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val bgColor: Color,
    val iconColor: Color,
    val targetScreen: AppScreen
)

@Composable
fun FeatureGridCards(
    onFeatureClick: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val row1 = listOf(
        ToolGridItem(
            id = "live_market",
            title = "Live Market",
            subtitle = "(Real-Time)",
            icon = Icons.Default.TrendingUp,
            bgColor = Color(0xFFE6F8F0),
            iconColor = Color(0xFF10B981),
            targetScreen = AppScreen.MARKETS
        ),
        ToolGridItem(
            id = "latest_news",
            title = "Latest\nNews",
            subtitle = "(India)",
            icon = Icons.Default.Newspaper,
            bgColor = Color(0xFFEBF5FF),
            iconColor = Color(0xFF2563EB),
            targetScreen = AppScreen.MORE
        ),
        ToolGridItem(
            id = "stock_screener",
            title = "Stock\nScreener",
            subtitle = "(Up/Down/LIQ)",
            icon = Icons.Default.Search,
            bgColor = Color(0xFFF3E8FF),
            iconColor = Color(0xFF9333EA),
            targetScreen = AppScreen.SCREENER
        ),
        ToolGridItem(
            id = "chart_indicators",
            title = "Chart &\nIndicators",
            subtitle = "",
            icon = Icons.Default.CandlestickChart,
            bgColor = Color(0xFFE6FAF0),
            iconColor = Color(0xFF059669),
            targetScreen = AppScreen.STOCK_DETAIL
        ),
        ToolGridItem(
            id = "liquidity_analyzer",
            title = "Liquidity\nAnalyzer",
            subtitle = "",
            icon = Icons.Default.WaterDrop,
            bgColor = Color(0xFFFEF3C7),
            iconColor = Color(0xFFD97706),
            targetScreen = AppScreen.LIQUIDITY_ANALYZER
        )
    )

    val row2 = listOf(
        ToolGridItem(
            id = "candlestick_patterns",
            title = "Candlestick\nPatterns",
            subtitle = "",
            icon = Icons.Default.ShowChart,
            bgColor = Color(0xFFFCE7F3),
            iconColor = Color(0xFFDB2777),
            targetScreen = AppScreen.STOCK_DETAIL
        ),
        ToolGridItem(
            id = "ai_tools",
            title = "AI Tools",
            subtitle = "(Analysis)",
            icon = Icons.Default.Psychology,
            bgColor = Color(0xFFE0F2FE),
            iconColor = Color(0xFF0284C7),
            targetScreen = AppScreen.AI_ASSISTANT
        ),
        ToolGridItem(
            id = "trading_strategies",
            title = "Trading\nStrategies",
            subtitle = "",
            icon = Icons.Default.AutoGraph,
            bgColor = Color(0xFFDCFCE7),
            iconColor = Color(0xFF16A34A),
            targetScreen = AppScreen.STRATEGY_BACKTEST
        ),
        ToolGridItem(
            id = "options_chain",
            title = "Options\nChain & OI",
            subtitle = "",
            icon = Icons.Default.Calculate,
            bgColor = Color(0xFFFFE4E6),
            iconColor = Color(0xFFE11D48),
            targetScreen = AppScreen.OPTIONS_CHAIN
        ),
        ToolGridItem(
            id = "strategy_backtest",
            title = "Strategy\nBacktest",
            subtitle = "",
            icon = Icons.Default.EmojiEvents,
            bgColor = Color(0xFFE0F2FE),
            iconColor = Color(0xFF2563EB),
            targetScreen = AppScreen.STRATEGY_BACKTEST
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            row1.forEach { item ->
                CompactToolSquareCard(
                    item = item,
                    onClick = { onFeatureClick(item.targetScreen) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            row2.forEach { item ->
                CompactToolSquareCard(
                    item = item,
                    onClick = { onFeatureClick(item.targetScreen) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun CompactToolSquareCard(
    item: ToolGridItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = item.bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, item.bgColor.copy(alpha = 0.9f)),
        modifier = modifier
            .height(82.dp)
            .clickable { onClick() }
            .testTag("tool_${item.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 2.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = item.iconColor,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center,
                lineHeight = 12.sp,
                maxLines = 2
            )

            if (item.subtitle.isNotEmpty()) {
                Text(
                    text = item.subtitle,
                    fontSize = 8.sp,
                    color = Color(0xFF64748B),
                    textAlign = TextAlign.Center,
                    lineHeight = 9.sp
                )
            }
        }
    }
}
