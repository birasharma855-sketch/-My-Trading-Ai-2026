package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Translate
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
import com.example.ui.TradeProUiState
import com.example.ui.components.ChatBubble
import com.example.ui.theme.*

@Composable
fun AiAssistantScreen(
    uiState: TradeProUiState,
    onSendMessage: (String) -> Unit,
    onToggleLanguage: () -> Unit,
    onSelectStock: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.chatMessages.size, uiState.isAiThinking) {
        if (uiState.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.chatMessages.size - 1)
        }
    }

    val currentStock = uiState.selectedStock
    val isHindi = uiState.aiLanguage == "hi"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NeutralBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            // Header
            Surface(
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(TradeProBluePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "AI Assistant",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isHindi) "ट्रेडप्रो AI - ट्रेडिंग गाइड" else "TRADEPRO AI INTELLIGENCE",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TradeProBlueDark
                                )
                                Text(
                                    text = if (isHindi) "सपोर्ट, रेजिस्टेंस, RSI, कैंडल पैटर्न व रिस्क गाइड" else "Candles, Indicators, Order Flow & Risk Copilot",
                                    fontSize = 11.sp,
                                    color = NeutralMuted
                                )
                            }
                        }

                        // Language Toggle Pill
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = TradeProBlueContainer,
                            border = androidx.compose.foundation.BorderStroke(1.dp, TradeProBluePrimary.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .clickable { onToggleLanguage() }
                                .testTag("ai_screen_lang_toggle")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = null,
                                    tint = TradeProBluePrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isHindi) "हिन्दी (HI)" else "English (EN)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TradeProBluePrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Stock selector chips for active context
                    Text(
                        text = if (isHindi) "सक्रिय स्टॉक संदर्भ चुनें:" else "Select Active Stock Context:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeutralMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(uiState.stocks) { stk ->
                            val isSelected = stk.symbol == currentStock?.symbol
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectStock(stk.symbol) },
                                label = { Text("${stk.symbol} (₹${stk.currentPrice})", fontSize = 11.sp) },
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }
            }

            // Quick Question Prompts Row
            val prompts = if (isHindi) {
                listOf(
                    "${currentStock?.symbol ?: "मार्केट"} का पूरा चार्ट विश्लेषण करें",
                    "RSI और MACD का क्या संकेत है?",
                    "कैंडलस्टिक पैटर्न और सपोर्ट/रेजिस्टेंस बताएं",
                    "स्टॉप लॉस और 1:2 रिस्क मैनेजमेंट नियम"
                )
            } else {
                listOf(
                    "Analyze full chart for ${currentStock?.symbol ?: "Market"}",
                    "Explain current RSI & MACD readings",
                    "Identify Candlestick Patterns & Levels",
                    "Explain Stop-Loss & 1:2 Risk Management"
                )
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(prompts) { p ->
                    SuggestionChip(
                        onClick = { onSendMessage(p) },
                        label = { Text(p, fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Chat Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.chatMessages, key = { it.id }) { msg ->
                    ChatBubble(message = msg, onFollowUpClick = { onSendMessage(it) })
                }

                if (uiState.isAiThinking) {
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
                                    text = if (isHindi) "ट्रेडप्रो AI डेटा का विश्लेषण कर रहा है..." else "TradePro AI is computing technical metrics...",
                                    fontSize = 12.sp,
                                    color = NeutralMuted
                                )
                            }
                        }
                    }
                }
            }

            // Chat Input Field
            Surface(
                color = Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                text = if (isHindi) "हिंदी या English में ट्रेडिंग सवाल पूछें..." else "Ask about chart, levels, indicators or risk...",
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
                            .testTag("ai_screen_input"),
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
                            .testTag("ai_screen_send_btn")
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
}
