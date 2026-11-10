package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderDepth
import com.example.ui.theme.*

@Composable
fun OrderBookDepthView(
    depth: OrderDepth,
    liquidityScore: Int = 94,
    modifier: Modifier = Modifier
) {
    val buyerPct = depth.buyerPercentage
    val sellerPct = 100f - buyerPct

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("liquidity_depth_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "MARKET DEPTH & LIQUIDITY (5-LEVEL)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralMuted,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Live NSE Limit Order Book",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TradeProBlueDark
                    )
                }

                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Score: $liquidityScore/100",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TradeProBluePrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Buyer vs Seller Volume Ratio Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Bids: ${String.format("%,d", depth.totalBuyQty)} (${buyerPct.toInt()}%)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BullGreen
                    )
                    Text(
                        text = "Total Asks: ${String.format("%,d", depth.totalSellQty)} (${sellerPct.toInt()}%)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BearRed
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Split Progress Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .weight(buyerPct.coerceAtLeast(1f))
                            .fillMaxHeight()
                            .background(BullGreen)
                    )
                    Box(
                        modifier = Modifier
                            .weight(sellerPct.coerceAtLeast(1f))
                            .fillMaxHeight()
                            .background(BearRed)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 5-Level Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), shape = RoundedCornerShape(6.dp))
                    .padding(vertical = 4.dp, horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Orders", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeutralMuted, modifier = Modifier.weight(0.8f))
                Text("Qty", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeutralMuted, modifier = Modifier.weight(1f))
                Text("Bid (₹)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BullGreen, modifier = Modifier.weight(1.2f))
                Text("Ask (₹)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BearRed, modifier = Modifier.weight(1.2f))
                Text("Qty", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeutralMuted, modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                Text("Orders", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NeutralMuted, modifier = Modifier.weight(0.8f), textAlign = TextAlign.End)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 5-Level Rows
            val count = maxOf(depth.bids.size, depth.asks.size)
            for (i in 0 until count) {
                val bid = depth.bids.getOrNull(i)
                val ask = depth.asks.getOrNull(i)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${bid?.orders ?: "-"}", fontSize = 11.sp, color = NeutralMedium, modifier = Modifier.weight(0.8f))
                    Text(text = "${bid?.quantity ?: "-"}", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = NeutralDark, modifier = Modifier.weight(1f))
                    Text(
                        text = if (bid != null) String.format("%.2f", bid.price) else "-",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BullGreen,
                        modifier = Modifier.weight(1.2f)
                    )
                    Text(
                        text = if (ask != null) String.format("%.2f", ask.price) else "-",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BearRed,
                        modifier = Modifier.weight(1.2f)
                    )
                    Text(
                        text = "${ask?.quantity ?: "-"}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = NeutralDark,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = "${ask?.orders ?: "-"}",
                        fontSize = 11.sp,
                        color = NeutralMedium,
                        modifier = Modifier.weight(0.8f),
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Imbalance Analysis Box
            Surface(
                color = if (buyerPct >= 52f) BullGreenContainer else if (sellerPct >= 52f) BearRedContainer else Color(0xFFF1F5F9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (buyerPct >= 52f) {
                        "🟢 Institutional Order Flow: Buyer demand absorption (+${(buyerPct - sellerPct).toInt()}% Net Bids)"
                    } else if (sellerPct >= 52f) {
                        "🔴 Institutional Order Flow: Overhead supply pressure (+${(sellerPct - buyerPct).toInt()}% Net Asks)"
                    } else {
                        "⚖️ Institutional Order Flow: Balanced Order Book"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (buyerPct >= 52f) BullGreenDark else if (sellerPct >= 52f) BearRedDark else NeutralMedium,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}
