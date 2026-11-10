package com.example.data.repository

import com.example.data.model.CandleData
import com.example.data.model.ChatMessage
import com.example.data.model.OrderDepth
import com.example.data.model.StockItem
import java.util.UUID

object AiAssistantEngine {

    fun generateStockAnalysis(
        stock: StockItem,
        candles: List<CandleData>,
        depth: OrderDepth,
        language: String, // "en" or "hi"
        prompt: String? = null
    ): ChatMessage {
        val lastCandle = candles.lastOrNull()
        val high20 = candles.takeLast(20).maxOfOrNull { it.high } ?: stock.dayHigh
        val low20 = candles.takeLast(20).minOfOrNull { it.low } ?: stock.dayLow
        val support = String.format("%.2f", low20)
        val resistance = String.format("%.2f", high20)
        val rsi = stock.rsi14
        val vwapDiff = stock.currentPrice - stock.vwap
        val isAboveVwap = vwapDiff >= 0
        val buyerPct = depth.buyerPercentage

        val rsiStatusEn = when {
            rsi >= 70 -> "Overbought (>70) - potential cooling off or consolidation expected"
            rsi <= 30 -> "Oversold (<30) - high probability reversal/bounce zone"
            else -> "Healthy momentum range ($rsi)"
        }

        val rsiStatusHi = when {
            rsi >= 70 -> "ओवरबॉट (>70) - यहाँ से थोड़ी गिरावट या कंसोलिडेशन संभव है"
            rsi <= 30 -> "ओवरसोल्ड (<30) - यहाँ से खरीदारी का उछाल आ सकता है"
            else -> "सामान्य और स्थिर मोमेंटम ($rsi)"
        }

        val trendEn = when (stock.trend) {
            "UPTREND" -> "Bullish Uptrend (Trading firmly above 20 & 50 EMA)"
            "DOWNTREND" -> "Bearish Downtrend (Trading under key moving averages)"
            else -> "Sideways Consolidation (Range-bound market)"
        }

        val trendHi = when (stock.trend) {
            "UPTREND" -> "बुलिश अपट्रेंड (20 और 50 EMA के ऊपर मजबूत कारोबार)"
            "DOWNTREND" -> "बेयरिश डाउनट्रेंड (की मूविंग एवरेज के नीचे दबाव)"
            else -> "कंसोलिडेशन (सीमित दायरे में कारोबार)"
        }

        val text: String
        val followUps: List<String>

        if (language == "hi") {
            text = buildString {
                appendLine("🤖 **ट्रेडप्रो AI विश्लेषण: ${stock.name} (${stock.symbol})**")
                appendLine("₹${stock.currentPrice} (${if (stock.change >= 0) "+" else ""}${stock.changePercent}%) [डेमो डेटा]")
                appendLine()
                appendLine("📊 **1. मार्केट ट्रेंड व मोमेंटम:**")
                appendLine("• $trendHi")
                appendLine("• MACD सिग्नल: ${stock.macdSignal}")
                appendLine("• VWAP स्थिति: कीमत VWAP (₹${stock.vwap}) के ${if (isAboveVwap) "ऊपर बुलिश" else "नीचे बेयरिश"} है।")
                appendLine()
                appendLine("📈 **2. टेक्निकल इंडिकेटर्स:**")
                appendLine("• RSI (14): $rsiStatusHi")
                appendLine("• 20 EMA: ₹${stock.ema20} | 50 EMA: ₹${stock.ema50} | 200 EMA: ₹${stock.ema200}")
                appendLine()
                appendLine("🕯️ **3. कैंडलस्टिक पैटर्न:**")
                appendLine("• हालिया पैटर्न: **${stock.topPattern}**")
                appendLine("• वॉल्यूम विश्लेषण: आज का वॉल्यूम ${(stock.volume / 100000.0).toInt()} लाख शेयर है।")
                appendLine()
                appendLine("🎯 **4. मुख्य स्तर (सपोर्ट व रेजिस्टेंस):**")
                appendLine("• निकटतम सपोर्ट (S1): ₹$support")
                appendLine("• निकटतम रेजिस्टेंस (R1): ₹$resistance")
                appendLine("• लिक्विडिटी: खरीदार ${buyerPct.toInt()}% बनाम विक्रेता ${(100 - buyerPct).toInt()}%")
                appendLine()
                appendLine("🛡️ **5. सख्त रिस्क मैनेजमेंट नियम:**")
                appendLine("• कभी भी 1:2 रिस्क-टू-रिवॉर्ड से कम में ट्रेड न लें।")
                appendLine("• संभावित स्टॉप लॉस: ₹${String.format("%.2f", stock.currentPrice * 0.985)}")
                appendLine("• संभावित लक्ष्य: ₹${String.format("%.2f", stock.currentPrice * 1.03)}")
                appendLine()
                appendLine("⚠️ *वैधानिक चेतावनी: यह विश्लेषण केवल शैक्षणिक उद्देश्य के लिए है। बाजार जोखिमों के अधीन है, लाभ की कोई गारंटी नहीं दी जाती।*")
            }
            followUps = listOf(
                "सपोर्ट टूटने पर क्या करें?",
                "RSI और MACD की व्याख्या करें",
                "ऑप्शन चेन में PCR का क्या अर्थ है?",
                "इंट्राडे स्टॉप लॉस कैसे तय करें?"
            )
        } else {
            text = buildString {
                appendLine("🤖 **TradePro AI Intelligence Report: ${stock.name} (${stock.symbol})**")
                appendLine("₹${stock.currentPrice} (${if (stock.change >= 0) "+" else ""}${stock.changePercent}%) [DEMO DATA]")
                appendLine()
                appendLine("📊 **1. Primary Trend & Momentum:**")
                appendLine("• $trendEn")
                appendLine("• MACD Status: ${stock.macdSignal.replace("_", " ")}")
                appendLine("• VWAP Relation: Trading ${if (isAboveVwap) "above VWAP (₹${stock.vwap}) indicating buyer dominance" else "below VWAP (₹${stock.vwap}) signaling intraday supply pressure"}")
                appendLine()
                appendLine("📈 **2. Indicator Analytics:**")
                appendLine("• RSI (14): $rsiStatusEn")
                appendLine("• Moving Averages: 20 EMA (₹${stock.ema20}), 50 EMA (₹${stock.ema50}), 200 EMA (₹${stock.ema200})")
                appendLine()
                appendLine("🕯️ **3. Candlestick Pattern Recognition:**")
                appendLine("• Detected Structure: **${stock.topPattern}** on 15m/1D timeframe")
                appendLine("• Institutional Volume: ${(stock.volume / 1000000.0).let { String.format("%.2f", it) }}M shares traded")
                appendLine()
                appendLine("🎯 **4. Key Price Levels:**")
                appendLine("• Immediate Support (S1): ₹$support")
                appendLine("• Key Overhead Resistance (R1): ₹$resistance")
                appendLine("• Order Book Depth: Buyers ${buyerPct.toInt()}% vs Sellers ${(100 - buyerPct).toInt()}%")
                appendLine()
                appendLine("🛡️ **5. Professional Risk Management:**")
                appendLine("• Minimum recommended Risk-to-Reward ratio: 1:2.0")
                appendLine("• Tactical Stop-Loss: ₹${String.format("%.2f", stock.currentPrice * 0.985)}")
                appendLine("• Target Objective: ₹${String.format("%.2f", stock.currentPrice * 1.03)}")
                appendLine()
                appendLine("⚠️ *Compliance Note: Purely educational analytics. Not SEBI registered investment advice. Profits are never guaranteed; protect your capital at all times.*")
            }
            followUps = listOf(
                "Explain RSI & MACD crossover",
                "How to trade this Candlestick Pattern?",
                "What does the Order Flow show?",
                "Options strategy for this stock"
            )
        }

        return ChatMessage(
            id = UUID.randomUUID().toString(),
            text = text,
            isUser = false,
            language = language,
            suggestedFollowUps = followUps
        )
    }

    fun answerGenericQuestion(question: String, language: String, currentStock: StockItem?): ChatMessage {
        val qLower = question.lowercase()
        val text: String
        val followUps: List<String>

        if (language == "hi") {
            when {
                qLower.contains("rsi") || qLower.contains("आरएसआई") -> {
                    text = """
                        📊 **RSI (रिलेटिव स्ट्रेंथ इंडेक्स) क्या है?**
                        • RSI 0 से 100 के बीच मोमेंटम मापता है।
                        • **70 से ऊपर (Overbought):** स्टॉक में अत्यधिक खरीदारी हो चुकी है। यहाँ फ्रेश बाइंग से बचें या ट्रेलिंग स्टॉप लॉस लगाएं।
                        • **30 से नीचे (Oversold):** स्टॉक में अत्यधिक बिकवाली हो चुकी है। यहाँ से बाउंस या रिवर्सल की संभावना रहती है।
                        • **50 लाइन:** 50 के ऊपर बुलिश और 50 के नीचे बेयरिश मोमेंटम माना जाता है।
                        
                        💡 **प्रो टिप:** केवल RSI देखकर ट्रेड न लें, साथ में सपोर्ट/रेजिस्टेंस और कैंडलस्टिक पैटर्न का कन्फर्मेशन जरूर लें।
                    """.trimIndent()
                    followUps = listOf("MACD कैसे काम करता है?", "VWAP क्या होता है?", "स्टॉप लॉस कहाँ रखें?")
                }
                qLower.contains("macd") -> {
                    text = """
                        📈 **MACD (मूविंग एवरेज कन्वर्जेंस डाइवर्जेंस) गाइड:**
                        • **बुलिश क्रॉसओवर:** जब MACD लाइन सिग्नल लाइन को नीचे से ऊपर काटती है और हिस्टोग्राम हरा होता है, यह खरीदारी का संकेत है।
                        • **बेयरिश क्रॉसओवर:** जब MACD लाइन सिग्नल लाइन को ऊपर से नीचे काटती है, यह कमजोरी का संकेत है।
                        • **जीरो लाइन:** जब MACD 0 लाइन के ऊपर क्रॉस करता है, तो ट्रेंड बहुत मजबूत होता है।
                    """.trimIndent()
                    followUps = listOf("20 और 50 EMA स्ट्रैटेजी बताएं", "कैंडलस्टिक हैमर क्या है?", "PCR रेश्यो क्या है?")
                }
                qLower.contains("stop loss") || qLower.contains("स्टॉप लॉस") || qLower.contains("risk") || qLower.contains("जोखिम") -> {
                    text = """
                        🛡️ **प्रोफेशनल रिस्क मैनेजमेंट नियम (पूंजी सुरक्षा):**
                        1. **1% नियम:** एक ट्रेड में अपनी कुल ट्रेडिंग पूंजी के 1% से 2% से अधिक का नुकसान कभी न होने दें।
                        2. **1:2 रिस्क-रिवॉर्ड:** यदि आपका रिस्क ₹10 है, तो कम से कम ₹20 का टारगेट होना चाहिए।
                        3. **सिस्टम स्टॉप लॉस:** स्टॉप लॉस हमेशा सिस्टम में पंच करें, दिमाग में नहीं।
                        4. **ओवरट्रेडिंग न करें:** दिन में 2 से 3 अच्छे सेटअप ही काफी हैं।
                    """.trimIndent()
                    followUps = listOf("सपोर्ट और रेजिस्टेंस कैसे खोजें?", "इंट्राडे के लिए टाइमफ्रेम?", "पेपर ट्रेडिंग कैसे करें?")
                }
                qLower.contains("candle") || qLower.contains("कैंडल") || qLower.contains("pattern") -> {
                    text = """
                        🕯️ **मुख्य कैंडलस्टिक पैटर्न्स की पहचान:**
                        • **हैमर (Hammer):** डाउनट्रेंड के बॉटम पर बनता है। लंबी निचली विक बताती है कि खरीदारों ने बिकवाली को खारिज कर दिया।
                        • **बुलिश इंगल्फिंग:** एक छोटी लाल कैंडल के बाद बड़ी हरी कैंडल जो पिछली कैंडल को पूरी तरह ढक ले।
                        • **डोजी (Doji):** ओपन और क्लोज लगभग बराबर। यह बाजार में अनिर्णय (Indecision) का संकेत है।
                        • **इवनिंग स्टार:** अपट्रेंड के टॉप पर बनने वाला बेयरिश रिवर्सल पैटर्न।
                    """.trimIndent()
                    followUps = listOf("वॉल्यूम ब्रेकआउट कैसे पहचानें?", "VWAP ट्रेडिंग क्या है?", "TradePro AI टूल्स")
                }
                else -> {
                    val stockRef = if (currentStock != null) "${currentStock.symbol} (₹${currentStock.currentPrice})" else "मार्केट"
                    text = """
                        🤖 **ट्रेडप्रो AI सहायक:**
                        आपके सवाल "$question" के संदर्भ में:
                        
                        भारतीय शेयर बाजार (NSE/BSE) में सफलता के 3 मूल स्तंभ हैं:
                        1. **ट्रेंड का सम्मान:** "Trend is your friend" - अपट्रेंड में बाय और डाउनट्रेंड में सेल करें।
                        2. **वॉल्यूम और लिक्विडिटी:** बिना बड़े वॉल्यूम के किसी भी ब्रेकआउट पर भरोसा न करें।
                        3. **अनुशासन:** हर ट्रेड से पहले स्टॉप-लॉस तय रखें। $stockRef पर विस्तृत विश्लेषण के लिए नीचे दिए गए बटन पर टैप करें।
                        
                        ⚠️ *स्मरण रहे: ट्रेडप्रो AI कभी लाभ की गारंटी नहीं देता। केवल सटीक डेटा आधारित विश्लेषण प्रदान करता है।*
                    """.trimIndent()
                    followUps = listOf(
                        "इस स्टॉक का विस्तृत विश्लेषण दें",
                        "RSI और MACD समझाएं",
                        "ऑप्शन चेन में मैक्स पेन क्या है?",
                        "स्टॉप लॉस नियम बताएं"
                    )
                }
            }
        } else {
            when {
                qLower.contains("rsi") -> {
                    text = """
                        📊 **RSI (Relative Strength Index) Essentials:**
                        • Measures momentum oscillating between 0 and 100.
                        • **Overbought (> 70):** Heavy buying pressure; prices may stall or consolidate. Avoid aggressive breakout chasing.
                        • **Oversold (< 30):** Deep selling exhaustion; prime hunting ground for reversal wicks and support bounces.
                        • **Bullish / Bearish Range Rules:** In strong uptrends, RSI often oscillates between 40 and 80. In downtrends, between 20 and 60.
                        
                        💡 *Always confirm RSI signals with Volume and Support/Resistance structure.*
                    """.trimIndent()
                    followUps = listOf("How to use MACD?", "Explain VWAP indicator", "Where to place Stop Loss?")
                }
                qLower.contains("macd") -> {
                    text = """
                        📈 **MACD (Moving Average Convergence Divergence) Guide:**
                        • **Bullish Crossover:** MACD line crosses above the 9-day Signal line, with green histogram expansion. Signals building buying momentum.
                        • **Bearish Crossover:** MACD crosses below Signal line; indicates supply entering the market.
                        • **Zero Line Crossing:** MACD moving above zero confirms the medium-term trend has turned positive.
                    """.trimIndent()
                    followUps = listOf("Explain 20/50 EMA Strategy", "What is Put-Call Ratio (PCR)?", "Support & Resistance tips")
                }
                qLower.contains("stop loss") || qLower.contains("risk") -> {
                    text = """
                        🛡️ **Institutional Risk Management Rules:**
                        1. **The 1-2% Rule:** Never risk more than 1-2% of total trading equity on any single trade.
                        2. **Asymmetrical Risk-Reward:** Demand at least 1:2.0 Risk-to-Reward. If your risk is ₹10/share, target ₹20+/share.
                        3. **Hard Stop-Loss:** Always place terminal Stop-Loss orders immediately upon fill. Do not rely on mental stops.
                        4. **Position Sizing:** Calculate: `Shares = (Account Risk in ₹) / (Entry Price - Stop Loss Price)`.
                    """.trimIndent()
                    followUps = listOf("How to identify Candlestick Patterns?", "Explain Order Book Liquidity", "Paper Trading guidelines")
                }
                qLower.contains("candle") || qLower.contains("pattern") -> {
                    text = """
                        🕯️ **High-Probability Candlestick Patterns:**
                        • **Hammer:** Bullish reversal at the bottom of a downtrend with a long lower shadow (at least 2x body) showing aggressive price rejection by buyers.
                        • **Bullish Engulfing:** Large green candle completely engulfs the prior red candle's body, indicating buyers taking complete charge.
                        • **Morning Star:** 3-candle bottom reversal: Bearish candle -> Small indecision star -> Strong bullish candle.
                        • **Doji:** Open and Close are virtually equal; signals equilibrium and impending directional volatility.
                    """.trimIndent()
                    followUps = listOf("How does VWAP work?", "Explain Option Chain PCR", "Analyze current stock")
                }
                else -> {
                    val stockRef = if (currentStock != null) "${currentStock.symbol} at ₹${currentStock.currentPrice}" else "Indian Markets"
                    text = """
                        🤖 **TradePro AI Intelligence:**
                        Regarding your query: "$question"
                        
                        When trading on NSE/BSE, key systematic principles apply:
                        • **Market Structure:** Align your trades with the daily and 1-hour trend direction.
                        • **Liquidity Confluence:** Look for candlestick confirmations near high-volume VWAP or EMA confluence levels.
                        • **Disciplined Capital Protection:** Every trade in $stockRef must have a predefined invalidation point (Stop-Loss).
                        
                        Select one of the quick prompts below for instantaneous deep-dive analytics!
                    """.trimIndent()
                    followUps = listOf(
                        "Analyze this stock chart now",
                        "Explain RSI & MACD",
                        "Show Option Chain Max Pain",
                        "Risk management rules"
                    )
                }
            }
        }

        return ChatMessage(
            id = UUID.randomUUID().toString(),
            text = text,
            isUser = false,
            language = language,
            suggestedFollowUps = followUps
        )
    }
}
