package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderType
import com.example.data.model.ProductType
import com.example.data.model.StockItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaperTradingSheet(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    stock: StockItem?,
    availableCash: Double,
    onPlaceOrder: (OrderType, ProductType, Int, Double, Double?, Double?) -> Boolean,
    modifier: Modifier = Modifier
) {
    if (!isOpen || stock == null) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var orderType by remember { mutableStateOf(OrderType.BUY) }
    var productType by remember { mutableStateOf(ProductType.INTRADAY) }
    var quantityText by remember { mutableStateOf("25") }
    var stopLossText by remember { mutableStateOf(String.format("%.2f", stock.currentPrice * 0.985)) }
    var targetText by remember { mutableStateOf(String.format("%.2f", stock.currentPrice * 1.03)) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val qty = quantityText.toIntOrNull() ?: 0
    val totalRequired = if (productType == ProductType.INTRADAY) {
        qty * stock.currentPrice * 0.20 // 5x intraday leverage
    } else {
        qty * stock.currentPrice
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = modifier.testTag("paper_trading_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .navigationBarsPadding()
        ) {
            // Header with Stock and Demo Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stock.symbol,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "NSE DEMO",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeutralMuted,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "LTP: ₹${String.format("%,.2f", stock.currentPrice)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TradeProBlueDark
                    )
                }

                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Balance: ₹${String.format("%,.0f", availableCash)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TradeProBluePrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Order Type Toggle (BUY vs SELL)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { orderType = OrderType.BUY },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (orderType == OrderType.BUY) BullGreen else Color(0xFFF1F5F9),
                        contentColor = if (orderType == OrderType.BUY) Color.White else NeutralMedium
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("order_type_buy")
                ) {
                    Text("BUY / LONG", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { orderType = OrderType.SELL },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (orderType == OrderType.SELL) BearRed else Color(0xFFF1F5F9),
                        contentColor = if (orderType == OrderType.SELL) Color.White else NeutralMedium
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("order_type_sell")
                ) {
                    Text("SELL / SHORT", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Product Type (Intraday MIS vs Delivery CNC)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = productType == ProductType.INTRADAY,
                    onClick = { productType = ProductType.INTRADAY },
                    label = { Text("Intraday (MIS 5x Margin)") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = productType == ProductType.DELIVERY,
                    onClick = { productType = ProductType.DELIVERY },
                    label = { Text("Delivery (CNC Full Cash)") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quantity Field
            OutlinedTextField(
                value = quantityText,
                onValueChange = { quantityText = it },
                label = { Text("Quantity (Shares)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Preset quantity buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(10, 25, 50, 100, 250).forEach { pQty ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.clickable { quantityText = pQty.toString() }
                    ) {
                        Text(
                            text = "+$pQty",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NeutralDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Stop-Loss and Target Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = stopLossText,
                    onValueChange = { stopLossText = it },
                    label = { Text("Stop Loss (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = targetText,
                    onValueChange = { targetText = it },
                    label = { Text("Target (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Margin Required Summary Box
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeutralBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Margin Required", fontSize = 11.sp, color = NeutralMuted)
                        Text(
                            text = "₹${String.format("%,.2f", totalRequired)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = NeutralDark
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Available Margin", fontSize = 11.sp, color = NeutralMuted)
                        Text(
                            text = "₹${String.format("%,.0f", availableCash)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BullGreenDark
                        )
                    }
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = errorMessage!!,
                    fontSize = 11.sp,
                    color = BearRed,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Submit Button
            Button(
                onClick = {
                    if (qty <= 0) {
                        errorMessage = "Please enter a valid quantity."
                        return@Button
                    }
                    if (totalRequired > availableCash) {
                        errorMessage = "Insufficient demo margin available."
                        return@Button
                    }
                    val sl = stopLossText.toDoubleOrNull()
                    val tp = targetText.toDoubleOrNull()
                    val placed = onPlaceOrder(orderType, productType, qty, stock.currentPrice, sl, tp)
                    if (!placed) {
                        errorMessage = "Failed to place paper trade."
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (orderType == OrderType.BUY) BullGreen else BearRed
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_paper_order")
            ) {
                Text(
                    text = "EXECUTE PAPER ORDER (${if (orderType == OrderType.BUY) "BUY" else "SELL"})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "DEMO SIMULATOR: No real-money trades placed. Compliant with SEBI guidelines for investor education.",
                fontSize = 9.sp,
                color = NeutralMuted,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
