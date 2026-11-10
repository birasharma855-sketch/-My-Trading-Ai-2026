package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.ui.theme.*

@Composable
fun ProfessionalAiAssistantCard(
    onPromptClick: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var queryText by remember { mutableStateOf("") }

    val promptPills = listOf(
        "📈 क्या ये स्टॉक खरीदें या बेचें?",
        "🕯️ ये कैंडल कौन सा पैटर्न है?",
        "💧 इसमें लिक्विडिटी कैसी है?",
        "📰 आज की बड़ी खबरें?",
        "⚙️ मेरे लिए बेस्ट रणनीति क्या है?"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("professional_ai_assistant_card")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Blue bar with Robot, "Professional AI Assistant", Online green dot, Expand icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E40AF))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartToy,
                            contentDescription = "Robot",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = "Professional\nAI Assistant",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 14.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF10B981))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Online",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFA7F3D0)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onExpandClick,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInFull,
                        contentDescription = "Expand",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Body
            Column(modifier = Modifier.padding(12.dp)) {
                // Greeting Bubble
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFEFF6FF),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Hello Shubham! 👋",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "मैं आपका Professional Trading Guide हूँ।",
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            lineHeight = 15.sp
                        )
                        Text(
                            text = "कृपया बताएं, आप क्या जानना चाहते हैं?",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1E40AF),
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Prompt Pills
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    promptPills.forEach { pill ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPromptClick(pill) }
                        ) {
                            Text(
                                text = pill,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1E293B),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Checkmark Features
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF0FDF4), shape = RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    FeatureCheckRow("Live Analysis | Real-time Data")
                    FeatureCheckRow("Candlestick + Liquidity Insights")
                    FeatureCheckRow("Smart Suggestions")
                    FeatureCheckRow("Educational Mode Available")
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Input Bar: Attachment clip, input text field, blue circular send button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF1F5F9), shape = RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Attach",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    OutlinedTextField(
                        value = queryText,
                        onValueChange = { queryText = it },
                        placeholder = {
                            Text("Type your query...", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                    )

                    FloatingActionButton(
                        onClick = {
                            if (queryText.isNotBlank()) {
                                onSendMessage(queryText.trim())
                                queryText = ""
                            }
                        },
                        containerColor = Color(0xFF2563EB),
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureCheckRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF16A34A),
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF15803D)
        )
    }
}
