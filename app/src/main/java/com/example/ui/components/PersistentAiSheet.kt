package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.StockItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersistentAiSheet(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    currentStock: StockItem?,
    messages: List<ChatMessage>,
    isThinking: Boolean,
    language: String, // "en" or "hi"
    onToggleLanguage: () -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = modifier.testTag("persistent_ai_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(horizontal = 16.dp)
        ) {
            // Sheet Header with Language Toggle and Stock Context
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(TradeProBluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "AI Copilot",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (language == "hi") "ट्रेडप्रो AI गाइड" else "TradePro AI Assistant",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Text(
                            text = if (language == "hi") "हिंदी व English में विश्लेषण" else "Indian Markets Intelligence",
                            fontSize = 11.sp,
                            color = NeutralMuted
                        )
                    }
                }

                // Bilingual Toggle Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = TradeProBlueContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TradeProBluePrimary.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .clickable { onToggleLanguage() }
                        .testTag("ai_language_toggle")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Language",
                            tint = TradeProBluePrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == "hi") "हिन्दी (HI)" else "English (EN)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TradeProBluePrimary
                        )
                    }
                }
            }

            // Current Stock Context Banner
            if (currentStock != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(10.dp),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Active Context: ",
                                fontSize = 11.sp,
                                color = NeutralMuted
                            )
                            Text(
                                text = "${currentStock.symbol} (₹${currentStock.currentPrice})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TradeProBlueDark
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (currentStock.isPositive) BullGreenContainer else BearRedContainer
                        ) {
                            Text(
                                text = "${if (currentStock.isPositive) "+" else ""}${currentStock.changePercent}%",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (currentStock.isPositive) BullGreen else BearRed,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Prompt Chips
            val quickPrompts = if (language == "hi") {
                listOf(
                    "चार्ट और पैटर्न का विश्लेषण करें",
                    "RSI और MACD की स्थिति क्या है?",
                    "सपोर्ट और रेजिस्टेंस स्तर बताएं",
                    "स्टॉप लॉस और रिस्क मैनेजमेंट नियम",
                    "ऑर्डर बुक लिक्विडिटी कैसी है?"
                )
            } else {
                listOf(
                    "Analyze current chart & pattern",
                    "Explain RSI & MACD readings",
                    "Identify Support & Resistance",
                    "Recommend Stop-Loss & Risk rules",
                    "Evaluate Order Flow Liquidity"
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(quickPrompts) { prompt ->
                    SuggestionChip(
                        onClick = { onSendMessage(prompt) },
                        label = {
                            Text(
                                text = prompt,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0xFFF1F5F9),
                            labelColor = NeutralDark
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = NeutralBorder
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Divider(
                color = NeutralBorder,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 6.dp)
            )

            // Chat Messages History
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    ChatBubble(message = msg, onFollowUpClick = { onSendMessage(it) })
                }

                if (isThinking) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = TradeProBluePrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (language == "hi") "ट्रेडप्रो AI सोच रहा है..." else "TradePro AI is analyzing chart data...",
                                    fontSize = 12.sp,
                                    color = NeutralMuted
                                )
                            }
                        }
                    }
                }
            }

            // Chat Input Field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = if (language == "hi") "हिंदी या English में पूछें..." else "Ask about candles, RSI, VWAP, levels...",
                            fontSize = 12.sp,
                            color = NeutralMuted
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TradeProBluePrimary,
                        unfocusedBorderColor = NeutralBorder,
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_input_text_field"),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            onSendMessage(inputText.trim())
                            inputText = ""
                        }
                    },
                    containerColor = TradeProBluePrimary,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(46.dp)
                        .testTag("ai_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    onFollowUpClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isUser = message.isUser

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) TradeProBluePrimary else Color(0xFFF8FAFC),
            border = if (!isUser) androidx.compose.foundation.BorderStroke(1.dp, NeutralBorder) else null,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = if (isUser) Color.White else NeutralDark
                )
            }
        }

        // Suggested follow-ups
        if (!isUser && message.suggestedFollowUps.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                message.suggestedFollowUps.take(2).forEach { fu ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFEFF6FF),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TradeProBluePrimary.copy(alpha = 0.2f)),
                        modifier = Modifier.clickable { onFollowUpClick(fu) }
                    ) {
                        Text(
                            text = fu,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TradeProBluePrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
