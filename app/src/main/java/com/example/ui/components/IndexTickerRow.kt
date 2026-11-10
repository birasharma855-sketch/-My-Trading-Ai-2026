package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MarketIndex

@Composable
fun IndexTickerRow(
    indices: List<MarketIndex>,
    onIndexClick: (MarketIndex) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(indices, key = { it.symbol }) { index ->
            IndexMiniCard(
                index = index,
                onClick = { onIndexClick(index) }
            )
        }
    }
}

@Composable
fun IndexMiniCard(
    index: MarketIndex,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isUp = index.isPositive
    val accentColor = if (isUp) Color(0xFF10B981) else Color(0xFFEF4444)

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        modifier = modifier
            .width(155.dp)
            .clickable { onClick() }
            .testTag("index_card_${index.symbol.replace(" ", "_")}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            // Symbol Name
            Text(
                text = index.symbol,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Price & Sparkline
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = String.format("%,.2f", index.currentPrice),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A)
                )

                SparklineView(
                    points = index.sparkline,
                    lineColor = accentColor,
                    modifier = Modifier
                        .width(42.dp)
                        .height(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Change & % Change
            Text(
                text = "${if (isUp) "▲ +" else "▼ "}${String.format("%.2f", kotlin.math.abs(index.change))} (${if (isUp) "+" else ""}${String.format("%.2f", index.changePercent)}%)",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }
    }
}

@Composable
fun SparklineView(
    points: List<Float>,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        if (points.size < 2) return@Canvas
        val min = points.minOrNull() ?: 0f
        val max = points.maxOrNull() ?: 1f
        val range = if (max - min == 0f) 1f else (max - min)
        val w = size.width
        val h = size.height

        val stepX = w / (points.size - 1)
        val path = Path()

        points.forEachIndexed { i, p ->
            val x = i * stepX
            val normalized = (p - min) / range
            val y = h - (normalized * (h - 4f)) - 2f
            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
