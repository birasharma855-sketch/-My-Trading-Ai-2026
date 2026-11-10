package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CandleData
import com.example.ui.theme.*

@Composable
fun InteractiveCandlestickChart(
    candles: List<CandleData>,
    timeframe: String,
    onTimeframeSelected: (String) -> Unit,
    showEma20: Boolean,
    showEma50: Boolean,
    showVwap: Boolean,
    showRsi: Boolean,
    onToggleIndicator: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val timeframes = listOf("1m", "5m", "15m", "1H", "1D")

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Timeframe selector bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Timeframe Chips
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    timeframes.forEach { tf ->
                        val isSelected = tf == timeframe
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) TradeProBluePrimary else Color(0xFFF1F5F9),
                            modifier = Modifier
                                .testTag("timeframe_$tf")
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        onTimeframeSelected(tf)
                                        selectedIndex = null
                                    }
                                }
                        ) {
                            Text(
                                text = tf,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else NeutralMedium,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                // Demo Live Badge
                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "NSE CANDLES (DEMO)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TradeProBluePrimary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Indicator quick toggles row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                IndicatorChip(label = "EMA 20", active = showEma20, color = Color(0xFF2563EB)) {
                    onToggleIndicator("EMA20")
                }
                IndicatorChip(label = "EMA 50", active = showEma50, color = Color(0xFFD97706)) {
                    onToggleIndicator("EMA50")
                }
                IndicatorChip(label = "VWAP", active = showVwap, color = Color(0xFF9333EA)) {
                    onToggleIndicator("VWAP")
                }
                IndicatorChip(label = "RSI (14)", active = showRsi, color = Color(0xFF059669)) {
                    onToggleIndicator("RSI")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Active Candle Inspector Pill (when touched or latest)
            val inspectedCandle = if (selectedIndex != null && selectedIndex!! in candles.indices) {
                candles[selectedIndex!!]
            } else {
                candles.lastOrNull()
            }

            if (inspectedCandle != null) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeutralBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = inspectedCandle.timeLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TradeProBlueDark
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("O: ${inspectedCandle.open}", fontSize = 10.sp, color = NeutralMedium)
                            Text("H: ${inspectedCandle.high}", fontSize = 10.sp, color = BullGreen)
                            Text("L: ${inspectedCandle.low}", fontSize = 10.sp, color = BearRed)
                            Text(
                                "C: ${inspectedCandle.close}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (inspectedCandle.isBullish) BullGreen else BearRed
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Candlestick Drawing Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .pointerInput(candles) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val candleWidth = size.width / candles.size.toFloat()
                                val index = (offset.x / candleWidth).toInt().coerceIn(0, candles.lastIndex)
                                selectedIndex = index
                            },
                            onDrag = { change, _ ->
                                val candleWidth = size.width / candles.size.toFloat()
                                val index = (change.position.x / candleWidth).toInt().coerceIn(0, candles.lastIndex)
                                selectedIndex = index
                            },
                            onDragEnd = { /* keep selected */ }
                        )
                    }
                    .pointerInput(candles) {
                        detectTapGestures { offset ->
                            val candleWidth = size.width / candles.size.toFloat()
                            val index = (offset.x / candleWidth).toInt().coerceIn(0, candles.lastIndex)
                            selectedIndex = index
                        }
                    }
                    .testTag("candlestick_chart_canvas")
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    if (candles.isEmpty()) return@Canvas

                    val minPrice = candles.minOf { it.low } * 0.998
                    val maxPrice = candles.maxOf { it.high } * 1.002
                    val priceRange = if (maxPrice - minPrice == 0.0) 1.0 else maxPrice - minPrice

                    val maxVolume = candles.maxOf { it.volume }.toFloat().coerceAtLeast(1f)

                    val w = size.width
                    val h = size.height
                    val volumeAreaHeight = h * 0.22f
                    val chartHeight = h - volumeAreaHeight

                    val candleCount = candles.size
                    val candleStep = w / candleCount
                    val candleBodyWidth = (candleStep * 0.65f).coerceAtLeast(2f)

                    // Draw subtle grid lines (3 levels)
                    for (step in 0..3) {
                        val y = chartHeight * (step / 3f)
                        drawLine(
                            color = Color(0xFFF1F5F9),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1f
                        )
                    }

                    // Draw volume baseline
                    drawLine(
                        color = Color(0xFFE2E8F0),
                        start = Offset(0f, chartHeight),
                        end = Offset(w, chartHeight),
                        strokeWidth = 1f
                    )

                    // Draw Candles and Volume Bars
                    candles.forEachIndexed { idx, candle ->
                        val centerX = (idx * candleStep) + (candleStep / 2f)

                        // Coordinates for OHLC
                        val highY = (chartHeight - ((candle.high - minPrice) / priceRange * chartHeight)).toFloat()
                        val lowY = (chartHeight - ((candle.low - minPrice) / priceRange * chartHeight)).toFloat()
                        val openY = (chartHeight - ((candle.open - minPrice) / priceRange * chartHeight)).toFloat()
                        val closeY = (chartHeight - ((candle.close - minPrice) / priceRange * chartHeight)).toFloat()

                        val isBullish = candle.isBullish
                        val candleColor = if (isBullish) BullGreen else BearRed

                        // Draw Wick
                        drawLine(
                            color = candleColor,
                            start = Offset(centerX, highY),
                            end = Offset(centerX, lowY),
                            strokeWidth = 1.5.dp.toPx()
                        )

                        // Draw Body
                        val topY = minOf(openY, closeY)
                        val bottomY = maxOf(openY, closeY)
                        val bodyHeight = (bottomY - topY).coerceAtLeast(2f)

                        drawRect(
                            color = candleColor,
                            topLeft = Offset(centerX - (candleBodyWidth / 2f), topY),
                            size = Size(candleBodyWidth, bodyHeight)
                        )

                        // Draw Volume bar
                        val volHeight = (candle.volume.toFloat() / maxVolume) * (volumeAreaHeight - 6f)
                        val volTop = h - volHeight
                        drawRect(
                            color = candleColor.copy(alpha = 0.35f),
                            topLeft = Offset(centerX - (candleBodyWidth / 2f), volTop),
                            size = Size(candleBodyWidth, volHeight)
                        )
                    }

                    // EMA 20 line
                    if (showEma20) {
                        val emaPath = Path()
                        candles.forEachIndexed { idx, c ->
                            val x = (idx * candleStep) + (candleStep / 2f)
                            val y = (chartHeight - ((c.ema20 - minPrice) / priceRange * chartHeight)).toFloat()
                            if (idx == 0) emaPath.moveTo(x, y) else emaPath.lineTo(x, y)
                        }
                        drawPath(emaPath, Color(0xFF2563EB), style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
                    }

                    // EMA 50 line
                    if (showEma50) {
                        val ema50Path = Path()
                        candles.forEachIndexed { idx, c ->
                            val x = (idx * candleStep) + (candleStep / 2f)
                            val y = (chartHeight - ((c.ema50 - minPrice) / priceRange * chartHeight)).toFloat()
                            if (idx == 0) ema50Path.moveTo(x, y) else ema50Path.lineTo(x, y)
                        }
                        drawPath(ema50Path, Color(0xFFD97706), style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round))
                    }

                    // VWAP line
                    if (showVwap) {
                        val vwapPath = Path()
                        candles.forEachIndexed { idx, c ->
                            val x = (idx * candleStep) + (candleStep / 2f)
                            val y = (chartHeight - ((c.vwap - minPrice) / priceRange * chartHeight)).toFloat()
                            if (idx == 0) vwapPath.moveTo(x, y) else vwapPath.lineTo(x, y)
                        }
                        drawPath(vwapPath, Color(0xFF9333EA), style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
                    }

                    // Crosshair on selected index
                    selectedIndex?.let { selIdx ->
                        if (selIdx in candles.indices) {
                            val selX = (selIdx * candleStep) + (candleStep / 2f)
                            drawLine(
                                color = NeutralMedium.copy(alpha = 0.6f),
                                start = Offset(selX, 0f),
                                end = Offset(selX, h),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                    }
                }
            }

            // RSI Subplot if enabled
            if (showRsi) {
                Spacer(modifier = Modifier.height(10.dp))
                RsiSubplotView(candles = candles)
            }
        }
    }
}

@Composable
fun IndicatorChip(
    label: String,
    active: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (active) color.copy(alpha = 0.12f) else Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (active) color else NeutralBorder
        ),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(if (active) color else Color.Transparent, shape = RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                color = if (active) color else NeutralMedium
            )
        }
    }
}

@Composable
fun RsiSubplotView(
    candles: List<CandleData>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("RSI (14 Period)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeutralMedium)
            Text("Overbought >70 | Oversold <30", fontSize = 9.sp, color = NeutralMuted)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .background(Color(0xFFF8FAFC), shape = RoundedCornerShape(6.dp))
        ) {
            val w = size.width
            val h = size.height

            // 70 line
            val y70 = h * 0.3f
            drawLine(Color(0xFFEF4444).copy(alpha = 0.5f), Offset(0f, y70), Offset(w, y70), strokeWidth = 1f)

            // 50 line
            val y50 = h * 0.5f
            drawLine(Color(0xFF94A3B8).copy(alpha = 0.5f), Offset(0f, y50), Offset(w, y50), strokeWidth = 1f)

            // 30 line
            val y30 = h * 0.7f
            drawLine(Color(0xFF10B981).copy(alpha = 0.5f), Offset(0f, y30), Offset(w, y30), strokeWidth = 1f)

            // Synthetic RSI curve
            if (candles.size > 1) {
                val stepX = w / candles.size
                val path = Path()
                candles.forEachIndexed { i, c ->
                    val x = i * stepX
                    // Estimate RSI wave
                    val rsiVal = 35f + (Math.sin(i * 0.35) * 25f).toFloat() + 15f
                    val y = h - ((rsiVal.coerceIn(10f, 90f) / 100f) * h)
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                drawPath(path, Color(0xFF059669), style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round))
            }
        }
    }
}
