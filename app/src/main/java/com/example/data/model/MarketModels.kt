package com.example.data.model

data class MarketIndex(
    val symbol: String,
    val name: String,
    val currentPrice: Double,
    val change: Double,
    val changePercent: Double,
    val high: Double,
    val low: Double,
    val sparkline: List<Float> = emptyList()
) {
    val isPositive: Boolean get() = change >= 0
}

data class StockItem(
    val symbol: String,
    val name: String,
    val exchange: String = "NSE",
    val sector: String,
    val currentPrice: Double,
    val change: Double,
    val changePercent: Double,
    val volume: Long,
    val dayHigh: Double,
    val dayLow: Double,
    val openPrice: Double,
    val prevClose: Double,
    val peRatio: Double,
    val marketCap: String, // e.g. "₹20.4L Cr"
    val rsi14: Double,
    val macdSignal: String, // "BULLISH_CROSSOVER", "BEARISH_CROSSOVER", "NEUTRAL"
    val ema20: Double,
    val ema50: Double,
    val ema200: Double,
    val vwap: Double,
    val trend: String, // "UPTREND", "DOWNTREND", "SIDEWAYS"
    val liquidityScore: Int, // 1 to 100
    val topPattern: String = "None", // "Bullish Engulfing", "Hammer", "Morning Star", "Doji"
    val isFavorite: Boolean = false
) {
    val isPositive: Boolean get() = change >= 0
}

data class CandleData(
    val timestamp: Long,
    val timeLabel: String,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long,
    val ema20: Double = 0.0,
    val ema50: Double = 0.0,
    val vwap: Double = 0.0
) {
    val isBullish: Boolean get() = close >= open
}

data class OrderDepthItem(
    val orders: Int,
    val quantity: Int,
    val price: Double
)

data class OrderDepth(
    val bids: List<OrderDepthItem>,
    val asks: List<OrderDepthItem>,
    val totalBuyQty: Long,
    val totalSellQty: Long
) {
    val buyerPercentage: Float
        get() {
            val total = totalBuyQty + totalSellQty
            return if (total > 0) (totalBuyQty.toFloat() / total.toFloat()) * 100f else 50f
        }
}

data class OptionStrike(
    val strikePrice: Double,
    val isAtm: Boolean = false,
    val callLtp: Double,
    val callOi: Int,
    val callChangeOi: Int,
    val callIv: Double,
    val putLtp: Double,
    val putOi: Int,
    val putChangeOi: Int,
    val putIv: Double
)

data class MarketNews(
    val id: String,
    val title: String,
    val source: String,
    val timeAgo: String,
    val summary: String,
    val sentiment: String, // "Bullish", "Bearish", "Neutral"
    val relatedSymbol: String = "NIFTY"
)

enum class OrderType {
    BUY, SELL
}

enum class ProductType {
    INTRADAY, // MIS
    DELIVERY  // CNC
}

data class PaperTradePosition(
    val id: String,
    val symbol: String,
    val type: OrderType,
    val productType: ProductType,
    val quantity: Int,
    val avgPrice: Double,
    val currentPrice: Double,
    val stopLoss: Double? = null,
    val target: Double? = null,
    val entryTime: Long = System.currentTimeMillis()
) {
    val investedAmount: Double get() = quantity * avgPrice
    val currentValue: Double get() = quantity * currentPrice
    val pnl: Double get() = if (type == OrderType.BUY) (currentPrice - avgPrice) * quantity else (avgPrice - currentPrice) * quantity
    val pnlPercent: Double get() = if (investedAmount > 0) (pnl / investedAmount) * 100.0 else 0.0
    val isProfit: Boolean get() = pnl >= 0
}

data class TradingStrategy(
    val id: String,
    val title: String,
    val category: String, // "Intraday", "Swing", "Options"
    val winRate: Int,      // e.g. 68%
    val riskReward: String, // "1:2.5"
    val indicators: List<String>,
    val description: String,
    val entryRule: String,
    val exitRule: String,
    val hindiSummary: String
)

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val language: String = "en", // "en" or "hi"
    val suggestedFollowUps: List<String> = emptyList()
)
