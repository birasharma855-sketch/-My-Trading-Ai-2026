package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

object StockRepository {

    private val initialIndices = listOf(
        MarketIndex(
            symbol = "NIFTY 50",
            name = "Nifty 50 Index",
            currentPrice = 24782.30,
            change = 215.60,
            changePercent = 0.88,
            high = 24820.00,
            low = 24650.00,
            sparkline = listOf(24650f, 24690f, 24710f, 24730f, 24760f, 24740f, 24782.30f)
        ),
        MarketIndex(
            symbol = "SENSEX",
            name = "BSE SENSEX",
            currentPrice = 81764.20,
            change = 689.15,
            changePercent = 0.85,
            high = 81900.00,
            low = 81200.00,
            sparkline = listOf(81200f, 81350f, 81480f, 81590f, 81680f, 81620f, 81764.20f)
        ),
        MarketIndex(
            symbol = "BANKNIFTY",
            name = "Bank Nifty Index",
            currentPrice = 55672.40,
            change = 412.80,
            changePercent = 0.75,
            high = 55800.00,
            low = 55300.00,
            sparkline = listOf(55300f, 55420f, 55510f, 55590f, 55640f, 55610f, 55672.40f)
        ),
        MarketIndex(
            symbol = "INDIA VIX",
            name = "India Volatility Index",
            currentPrice = 12.48,
            change = -0.21,
            changePercent = -1.65,
            high = 12.80,
            low = 12.30,
            sparkline = listOf(12.75f, 12.68f, 12.60f, 12.55f, 12.50f, 12.48f)
        )
    )

    private val rawStocks = mutableListOf(
        // Top Gainers from screenshot
        StockItem(
            symbol = "TCS",
            name = "Tata Consultancy Services",
            exchange = "NSE",
            sector = "Information Tech",
            currentPrice = 3512.60,
            change = 130.20,
            changePercent = 3.85,
            volume = 3450000L,
            dayHigh = 3530.00,
            dayLow = 3410.00,
            openPrice = 3420.00,
            prevClose = 3382.40,
            peRatio = 29.4,
            marketCap = "₹12.8L Cr",
            rsi14 = 66.4,
            macdSignal = "BULLISH_CROSSOVER",
            ema20 = 3460.00,
            ema50 = 3410.00,
            ema200 = 3320.00,
            vwap = 3495.00,
            trend = "UPTREND",
            liquidityScore = 95,
            topPattern = "Bullish Engulfing",
            isFavorite = true
        ),
        StockItem(
            symbol = "HDFCBANK",
            name = "HDFC Bank Ltd",
            exchange = "NSE",
            sector = "Private Sector Bank",
            currentPrice = 1987.40,
            change = 60.10,
            changePercent = 3.12,
            volume = 14200000L,
            dayHigh = 1998.00,
            dayLow = 1935.00,
            openPrice = 1940.00,
            prevClose = 1927.30,
            peRatio = 21.0,
            marketCap = "₹15.1L Cr",
            rsi14 = 64.2,
            macdSignal = "BULLISH_CROSSOVER",
            ema20 = 1950.00,
            ema50 = 1910.00,
            ema200 = 1780.00,
            vwap = 1978.00,
            trend = "UPTREND",
            liquidityScore = 98,
            topPattern = "Morning Star",
            isFavorite = true
        ),
        StockItem(
            symbol = "Reliance",
            name = "Reliance Industries Ltd",
            exchange = "NSE",
            sector = "Oil, Gas & Cons. Goods",
            currentPrice = 1452.70,
            change = 41.75,
            changePercent = 2.96,
            volume = 8900000L,
            dayHigh = 1465.00,
            dayLow = 1415.00,
            openPrice = 1420.00,
            prevClose = 1410.95,
            peRatio = 24.8,
            marketCap = "₹19.6L Cr",
            rsi14 = 62.8,
            macdSignal = "BULLISH_CROSSOVER",
            ema20 = 1430.00,
            ema50 = 1395.00,
            ema200 = 1310.00,
            vwap = 1446.00,
            trend = "UPTREND",
            liquidityScore = 96,
            topPattern = "Hammer Reversal",
            isFavorite = true
        ),
        StockItem(
            symbol = "Infosys",
            name = "Infosys Ltd",
            exchange = "NSE",
            sector = "Information Tech",
            currentPrice = 1542.80,
            change = 41.00,
            changePercent = 2.73,
            volume = 6700000L,
            dayHigh = 1555.00,
            dayLow = 1508.00,
            openPrice = 1512.00,
            prevClose = 1501.80,
            peRatio = 26.5,
            marketCap = "₹6.4L Cr",
            rsi14 = 61.5,
            macdSignal = "BULLISH_CROSSOVER",
            ema20 = 1520.00,
            ema50 = 1480.00,
            ema200 = 1410.00,
            vwap = 1538.00,
            trend = "UPTREND",
            liquidityScore = 94,
            topPattern = "Three White Soldiers",
            isFavorite = false
        ),
        StockItem(
            symbol = "Bharti Airtel",
            name = "Bharti Airtel Ltd",
            exchange = "NSE",
            sector = "Telecom",
            currentPrice = 1235.20,
            change = 28.95,
            changePercent = 2.40,
            volume = 5100000L,
            dayHigh = 1245.00,
            dayLow = 1210.00,
            openPrice = 1215.00,
            prevClose = 1206.25,
            peRatio = 48.0,
            marketCap = "₹7.2L Cr",
            rsi14 = 60.1,
            macdSignal = "BULLISH_CROSSOVER",
            ema20 = 1218.00,
            ema50 = 1180.00,
            ema200 = 1090.00,
            vwap = 1230.00,
            trend = "UPTREND",
            liquidityScore = 92,
            topPattern = "Breakout Volume",
            isFavorite = false
        ),

        // Top Losers from screenshot
        StockItem(
            symbol = "Zomato",
            name = "Zomato Ltd",
            exchange = "NSE",
            sector = "Internet & Consumer",
            currentPrice = 268.40,
            change = -10.75,
            changePercent = -3.85,
            volume = 32000000L,
            dayHigh = 282.00,
            dayLow = 266.00,
            openPrice = 280.00,
            prevClose = 279.15,
            peRatio = 115.0,
            marketCap = "₹2.4L Cr",
            rsi14 = 41.2,
            macdSignal = "BEARISH_CROSSOVER",
            ema20 = 276.00,
            ema50 = 282.00,
            ema200 = 240.00,
            vwap = 271.50,
            trend = "DOWNTREND",
            liquidityScore = 99,
            topPattern = "Bearish Engulfing",
            isFavorite = false
        ),
        StockItem(
            symbol = "Paytm",
            name = "One97 Communications Ltd",
            exchange = "NSE",
            sector = "Fintech",
            currentPrice = 1012.50,
            change = -33.60,
            changePercent = -3.21,
            volume = 6800000L,
            dayHigh = 1055.00,
            dayLow = 1005.00,
            openPrice = 1050.00,
            prevClose = 1046.10,
            peRatio = 42.0,
            marketCap = "₹64,000 Cr",
            rsi14 = 39.8,
            macdSignal = "BEARISH_CROSSOVER",
            ema20 = 1040.00,
            ema50 = 1065.00,
            ema200 = 980.00,
            vwap = 1022.00,
            trend = "DOWNTREND",
            liquidityScore = 88,
            topPattern = "Evening Star",
            isFavorite = false
        ),
        StockItem(
            symbol = "Tata Motors",
            name = "Tata Motors Ltd",
            exchange = "NSE",
            sector = "Automobiles",
            currentPrice = 682.70,
            change = -20.90,
            changePercent = -2.97,
            volume = 12500000L,
            dayHigh = 710.00,
            dayLow = 678.00,
            openPrice = 708.00,
            prevClose = 703.60,
            peRatio = 14.8,
            marketCap = "₹2.5L Cr",
            rsi14 = 42.5,
            macdSignal = "BEARISH_CROSSOVER",
            ema20 = 702.00,
            ema50 = 718.00,
            ema200 = 660.00,
            vwap = 688.00,
            trend = "DOWNTREND",
            liquidityScore = 95,
            topPattern = "Dark Cloud Cover",
            isFavorite = true
        ),
        StockItem(
            symbol = "Adani Ent.",
            name = "Adani Enterprises Ltd",
            exchange = "NSE",
            sector = "Conglomerate",
            currentPrice = 2351.30,
            change = -65.50,
            changePercent = -2.71,
            volume = 4300000L,
            dayHigh = 2430.00,
            dayLow = 2335.00,
            openPrice = 2420.00,
            prevClose = 2416.80,
            peRatio = 72.0,
            marketCap = "₹2.7L Cr",
            rsi14 = 44.0,
            macdSignal = "BEARISH_CROSSOVER",
            ema20 = 2410.00,
            ema50 = 2450.00,
            ema200 = 2520.00,
            vwap = 2370.00,
            trend = "DOWNTREND",
            liquidityScore = 91,
            topPattern = "Shooting Star",
            isFavorite = false
        ),
        StockItem(
            symbol = "BPCL",
            name = "Bharat Petroleum Corp Ltd",
            exchange = "NSE",
            sector = "Oil & Gas",
            currentPrice = 312.60,
            change = -7.95,
            changePercent = -2.49,
            volume = 8900000L,
            dayHigh = 324.00,
            dayLow = 310.00,
            openPrice = 322.00,
            prevClose = 320.55,
            peRatio = 5.8,
            marketCap = "₹67,800 Cr",
            rsi14 = 43.1,
            macdSignal = "BEARISH_CROSSOVER",
            ema20 = 321.00,
            ema50 = 326.00,
            ema200 = 305.00,
            vwap = 315.00,
            trend = "DOWNTREND",
            liquidityScore = 89,
            topPattern = "Doji at Resistance",
            isFavorite = false
        )
    )

    private val _indicesFlow = MutableStateFlow(initialIndices)
    val indicesFlow: Flow<List<MarketIndex>> = _indicesFlow.asStateFlow()

    private val _stocksFlow = MutableStateFlow<List<StockItem>>(rawStocks)
    val stocksFlow: Flow<List<StockItem>> = _stocksFlow.asStateFlow()

    fun getAllStocks(): List<StockItem> = _stocksFlow.value

    fun getStock(symbol: String): StockItem {
        return _stocksFlow.value.find { it.symbol.equals(symbol, ignoreCase = true) }
            ?: _stocksFlow.value.first()
    }

    fun toggleFavorite(symbol: String) {
        val updated = _stocksFlow.value.map {
            if (it.symbol.equals(symbol, ignoreCase = true)) {
                it.copy(isFavorite = !it.isFavorite)
            } else it
        }
        _stocksFlow.value = updated
    }

    fun getTopGainers(): List<StockItem> =
        _stocksFlow.value.filter { it.changePercent > 0 }.sortedByDescending { it.changePercent }

    fun getTopLosers(): List<StockItem> =
        _stocksFlow.value.filter { it.changePercent < 0 }.sortedBy { it.changePercent }

    fun getCandles(symbol: String, timeframe: String): List<CandleData> {
        val stock = getStock(symbol)
        val basePrice = stock.currentPrice
        val count = when (timeframe) {
            "1m" -> 30
            "5m" -> 35
            "15m" -> 40
            "1H" -> 30
            "1D" -> 45
            else -> 30
        }

        val list = mutableListOf<CandleData>()
        var lastClose = basePrice * (1.0 - (stock.changePercent / 100.0) * 0.8)
        val rng = Random(symbol.hashCode() + timeframe.hashCode())

        for (i in 0 until count) {
            val drift = (rng.nextDouble() - 0.48) * (basePrice * 0.008)
            val open = lastClose
            val close = open + drift
            val high = maxOf(open, close) + rng.nextDouble() * (basePrice * 0.005)
            val low = minOf(open, close) - rng.nextDouble() * (basePrice * 0.005)
            val vol = (50000 + rng.nextInt(250000)).toLong()
            val ema20 = close * 0.995
            val ema50 = close * 0.988
            val vwap = (high + low + close) / 3.0

            val label = when (timeframe) {
                "1m" -> "${9 + (i / 60)}:${String.format("%02d", (15 + i) % 60)}"
                "5m" -> "${9 + (i * 5 / 60)}:${String.format("%02d", (15 + i * 5) % 60)}"
                "15m" -> "${9 + (i * 15 / 60)}:${String.format("%02d", (15 + i * 15) % 60)}"
                "1H" -> "Day ${(i % 5) + 1} ${10 + (i % 6)}:00"
                else -> "Nov ${i + 1}"
            }

            list.add(
                CandleData(
                    timestamp = System.currentTimeMillis() - ((count - i) * 60000L),
                    timeLabel = label,
                    open = Math.round(open * 100.0) / 100.0,
                    high = Math.round(high * 100.0) / 100.0,
                    low = Math.round(low * 100.0) / 100.0,
                    close = Math.round(close * 100.0) / 100.0,
                    volume = vol,
                    ema20 = Math.round(ema20 * 100.0) / 100.0,
                    ema50 = Math.round(ema50 * 100.0) / 100.0,
                    vwap = Math.round(vwap * 100.0) / 100.0
                )
            )
            lastClose = close
        }

        val lastIdx = list.size - 1
        if (lastIdx >= 0) {
            val curr = list[lastIdx]
            list[lastIdx] = curr.copy(
                close = stock.currentPrice,
                high = maxOf(curr.high, stock.currentPrice),
                low = minOf(curr.low, stock.currentPrice)
            )
        }
        return list
    }

    fun getOrderDepth(symbol: String): OrderDepth {
        val stock = getStock(symbol)
        val ltp = stock.currentPrice
        val rng = Random(symbol.hashCode())

        val bids = (1..5).map { step ->
            val price = ltp - (step * 0.25)
            val qty = 1500 + rng.nextInt(6000)
            val orders = 12 + rng.nextInt(45)
            OrderDepthItem(orders, qty, Math.round(price * 100.0) / 100.0)
        }

        val asks = (1..5).map { step ->
            val price = ltp + (step * 0.25)
            val qty = 1400 + rng.nextInt(5800)
            val orders = 10 + rng.nextInt(40)
            OrderDepthItem(orders, qty, Math.round(price * 100.0) / 100.0)
        }

        val totalBuyQty = bids.sumOf { it.quantity.toLong() } * 32L
        val totalSellQty = asks.sumOf { it.quantity.toLong() } * 29L

        return OrderDepth(bids, asks, totalBuyQty, totalSellQty)
    }

    fun getOptionChain(symbol: String): List<OptionStrike> {
        val stock = getStock(symbol)
        val baseLtp = stock.currentPrice
        val step = when {
            baseLtp > 10000 -> 100.0
            baseLtp > 3000 -> 50.0
            baseLtp > 1000 -> 20.0
            baseLtp > 500 -> 10.0
            else -> 5.0
        }

        val roundedAtm = Math.round(baseLtp / step) * step
        val strikes = mutableListOf<OptionStrike>()
        val rng = Random(symbol.hashCode() + 42)

        for (i in -5..5) {
            val strike = roundedAtm + (i * step)
            val isAtm = i == 0
            val dist = strike - baseLtp
            val callLtp = maxOf(1.5, (baseLtp - strike) * 0.85 + (35.0 - kotlin.math.abs(dist) * 0.15))
            val putLtp = maxOf(1.5, (strike - baseLtp) * 0.85 + (35.0 - kotlin.math.abs(dist) * 0.15))

            strikes.add(
                OptionStrike(
                    strikePrice = strike,
                    isAtm = isAtm,
                    callLtp = Math.round(callLtp * 10.0) / 10.0,
                    callOi = 25000 + rng.nextInt(120000),
                    callChangeOi = rng.nextInt(15000) - 3000,
                    callIv = 14.5 + rng.nextDouble() * 3.0,
                    putLtp = Math.round(putLtp * 10.0) / 10.0,
                    putOi = 22000 + rng.nextInt(115000),
                    putChangeOi = rng.nextInt(14000) - 2000,
                    putIv = 15.2 + rng.nextDouble() * 3.5
                )
            )
        }
        return strikes
    }

    fun getLatestNews(): List<MarketNews> = listOf(
        MarketNews(
            id = "news_1",
            title = "SEBI Tightens Rules on F&O Trading, New Guidelines Soon",
            source = "Economic Times",
            timeAgo = "2m ago",
            summary = "Market regulator issues consultation framework tightening equity derivatives exposure, lot sizes and weekly expiry dynamics.",
            sentiment = "Neutral",
            relatedSymbol = "NIFTY 50"
        ),
        MarketNews(
            id = "news_2",
            title = "RBI Keeps Repo Rate Unchanged at 5.50%",
            source = "Business Today",
            timeAgo = "5m ago",
            summary = "Monetary Policy Committee retains status quo on benchmark policy rates, projecting resilient GDP growth and manageable CPI inflation.",
            sentiment = "Bullish",
            relatedSymbol = "BANKNIFTY"
        )
    )

    fun getTradingStrategies(): List<TradingStrategy> = listOf(
        TradingStrategy(
            id = "strat_vwap_breakout",
            title = "VWAP + 20 EMA Intraday Trend",
            category = "Intraday",
            winRate = 67,
            riskReward = "1:2.0",
            indicators = listOf("VWAP", "20 EMA", "Volume Surge"),
            description = "High-probability intraday strategy identifying institutional accumulation when price breaks above VWAP with above-average volume.",
            entryRule = "Buy when 5m candle closes firmly above VWAP and 20 EMA with volume > 1.5x 20-period average.",
            exitRule = "Exit when price closes below 20 EMA or reaches 1:2 Risk/Reward target. Stop-loss at previous swing low.",
            hindiSummary = "जब 5-मिनट की कैंडल VWAP और 20 EMA के ऊपर मजबूत वॉल्यूम के साथ बंद हो, तब Buy सिग्नल मिलता है। स्टॉप-लॉस पिछले स्विंग लो पर रखें।"
        ),
        TradingStrategy(
            id = "strat_rsi_reversal",
            title = "RSI Bullish Divergence at Key Support",
            category = "Swing",
            winRate = 71,
            riskReward = "1:2.8",
            indicators = listOf("RSI(14)", "50 EMA", "Support Zones"),
            description = "Captures powerful trend reversals when stock price makes a lower low while RSI makes a higher low near key daily support.",
            entryRule = "Enter long after a confirmation bullish candlestick (Hammer or Bullish Engulfing) forms following the RSI divergence.",
            exitRule = "Take profit at 50 EMA resistance. Trail stop-loss under the swing low candle.",
            hindiSummary = "जब कीमत नया लो बनाए लेकिन RSI ऊपर उठे (डायवर्जेंस), तो यह संभावित रिवर्सल का संकेत है। हैमर या बुलिश इंगल्फिंग कैंडल पर एंट्री लें।"
        ),
        TradingStrategy(
            id = "strat_options_pcr",
            title = "Option Chain PCR Reversal Strategy",
            category = "Options",
            winRate = 64,
            riskReward = "1:2.2",
            indicators = listOf("Put-Call Ratio (PCR)", "Max Pain", "ATM Straddle"),
            description = "Uses derivatives sentiment to identify market extremes. PCR < 0.70 signals oversold bottom, PCR > 1.40 signals overbought top.",
            entryRule = "Buy Call option when Index tests strong support and PCR drops below 0.65 with Put writing increasing at support strikes.",
            exitRule = "Exit when PCR normalizes above 1.0 or near calculated Max Pain strike level.",
            hindiSummary = "ऑप्शन चेन में जब PCR 0.70 से नीचे गिर जाए, तो बाजार ओवरसोल्ड होता है। यहाँ सपोर्ट पर कॉल बाय या बुल पुट स्प्रेड बनाना फायदेमंद होता है।"
        ),
        TradingStrategy(
            id = "strat_ema_crossover",
            title = "Golden Cross 50/200 EMA Trend Following",
            category = "Swing",
            winRate = 69,
            riskReward = "1:3.0",
            indicators = listOf("50 EMA", "200 EMA", "MACD"),
            description = "Classic institutional trend capture. Rides medium-to-long term upward momentum across Nifty 50 large caps.",
            entryRule = "50 EMA crosses above 200 EMA on 1D timeframe with MACD positive histogram confirmation.",
            exitRule = "Ride until 50 EMA slopes downward or price closes below 50 EMA.",
            hindiSummary = "जब 50 दिन का EMA 200 दिन के EMA को ऊपर की ओर काटता है (गोल्डन क्रॉस), तो यह मजबूत अपट्रेंड का संकेत है। बड़े स्विंग मुनाफे के लिए बेस्ट है।"
        )
    )
}
