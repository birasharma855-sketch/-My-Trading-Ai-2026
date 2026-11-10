package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.AiAssistantEngine
import com.example.data.repository.PaperTradingRepository
import com.example.data.repository.StockRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen {
    HOME,
    MARKETS,
    WATCHLIST,
    PORTFOLIO,
    AI_ASSISTANT,
    MORE,
    STOCK_DETAIL,
    OPTIONS_CHAIN,
    STRATEGY_BACKTEST,
    LIQUIDITY_ANALYZER,
    SCREENER
}

enum class MarketTab {
    GAINERS,
    LOSERS,
    UPTREND,
    DOWNTREND,
    LIQUIDITY,
    NEWS
}

data class TradeProUiState(
    val currentScreen: AppScreen = AppScreen.HOME,
    val screenStack: List<AppScreen> = listOf(AppScreen.HOME),
    val indices: List<MarketIndex> = emptyList(),
    val stocks: List<StockItem> = emptyList(),
    val selectedStock: StockItem? = null,
    val selectedTimeframe: String = "15m",
    val candles: List<CandleData> = emptyList(),
    val orderDepth: OrderDepth? = null,
    val optionChain: List<OptionStrike> = emptyList(),
    val latestNews: List<MarketNews> = emptyList(),
    val strategies: List<TradingStrategy> = emptyList(),
    val selectedStrategy: TradingStrategy? = null,
    val marketTab: MarketTab = MarketTab.GAINERS,
    val searchQuery: String = "",
    val searchResults: List<StockItem> = emptyList(),
    val isSearchActive: Boolean = false,
    val availableCash: Double = 1000000.0,
    val positions: List<PaperTradePosition> = emptyList(),
    val closedPositions: List<PaperTradePosition> = emptyList(),
    val aiLanguage: String = "en", // "en" or "hi"
    val chatMessages: List<ChatMessage> = emptyList(),
    val isAiDrawerOpen: Boolean = false,
    val isOrderSheetOpen: Boolean = false,
    val isNotificationDialogOpen: Boolean = false,
    val isAiThinking: Boolean = false,
    val showEma20: Boolean = true,
    val showEma50: Boolean = true,
    val showVwap: Boolean = true,
    val showRsi: Boolean = true,
    val showMacd: Boolean = false
)

class TradeProViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TradeProUiState())
    val uiState: StateFlow<TradeProUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
        startPeriodicDataStream()
        loadInitialAiGreeting()
    }

    private fun loadInitialData() {
        val initialStocks = StockRepository.getAllStocks()
        val defaultStock = initialStocks.firstOrNull() ?: StockRepository.getStock("RELIANCE")
        val initialCandles = StockRepository.getCandles(defaultStock.symbol, "15m")
        val depth = StockRepository.getOrderDepth(defaultStock.symbol)
        val chain = StockRepository.getOptionChain(defaultStock.symbol)
        val news = StockRepository.getLatestNews()
        val strategies = StockRepository.getTradingStrategies()

        _uiState.update { current ->
            current.copy(
                stocks = initialStocks,
                selectedStock = defaultStock,
                candles = initialCandles,
                orderDepth = depth,
                optionChain = chain,
                latestNews = news,
                strategies = strategies,
                selectedStrategy = strategies.firstOrNull(),
                availableCash = PaperTradingRepository.availableCash.value,
                positions = PaperTradingRepository.positions.value,
                closedPositions = PaperTradingRepository.closedPositions.value
            )
        }

        viewModelScope.launch {
            StockRepository.indicesFlow.collect { indicesList ->
                _uiState.update { it.copy(indices = indicesList) }
            }
        }

        viewModelScope.launch {
            StockRepository.stocksFlow.collect { stocksList ->
                _uiState.update { current ->
                    val updatedSelected = current.selectedStock?.let { sel ->
                        stocksList.find { it.symbol == sel.symbol } ?: sel
                    }
                    current.copy(
                        stocks = stocksList,
                        selectedStock = updatedSelected ?: current.selectedStock
                    )
                }
            }
        }

        viewModelScope.launch {
            PaperTradingRepository.availableCash.collect { cash ->
                _uiState.update { it.copy(availableCash = cash) }
            }
        }

        viewModelScope.launch {
            PaperTradingRepository.positions.collect { pos ->
                _uiState.update { it.copy(positions = pos) }
            }
        }
    }

    private fun loadInitialAiGreeting() {
        val defaultStock = _uiState.value.selectedStock ?: StockRepository.getStock("RELIANCE")
        val greetingEn = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = "👋 **Welcome to TradePro AI India!**\n\nI am your professional trading intelligence copilot for NSE & BSE stocks. I can analyze live candlestick structures, RSI, MACD, Moving Averages, Order Book liquidity and Support/Resistance levels.\n\n*Select any stock or ask me questions in English or हिन्दी!*",
            isUser = false,
            language = "en",
            suggestedFollowUps = listOf(
                "Analyze ${defaultStock.symbol} chart",
                "Explain RSI & MACD indicators",
                "What is Put-Call Ratio (PCR)?",
                "Suggest intraday risk rules"
            )
        )
        _uiState.update { it.copy(chatMessages = listOf(greetingEn)) }
    }

    private fun startPeriodicDataStream() {
        viewModelScope.launch {
            while (true) {
                delay(4000)
                // Minor realistic tick movement for selected stock and indices
                val current = _uiState.value
                val selStock = current.selectedStock
                if (selStock != null) {
                    val delta = (Math.random() - 0.49) * (selStock.currentPrice * 0.0008)
                    val newPrice = Math.round((selStock.currentPrice + delta) * 100.0) / 100.0
                    val updatedStock = selStock.copy(currentPrice = newPrice)
                    _uiState.update { state ->
                        state.copy(
                            selectedStock = updatedStock,
                            candles = StockRepository.getCandles(updatedStock.symbol, state.selectedTimeframe),
                            orderDepth = StockRepository.getOrderDepth(updatedStock.symbol)
                        )
                    }
                }
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        if (_uiState.value.currentScreen == screen) return
        _uiState.update {
            it.copy(
                currentScreen = screen,
                screenStack = it.screenStack + screen
            )
        }
    }

    fun handleBackPress(): Boolean {
        val currentStack = _uiState.value.screenStack
        if (currentStack.size > 1) {
            val newStack = currentStack.dropLast(1)
            val prevScreen = newStack.last()
            _uiState.update {
                it.copy(
                    currentScreen = prevScreen,
                    screenStack = newStack,
                    isSearchActive = false
                )
            }
            return true
        }
        return false
    }

    fun selectStock(symbol: String, openDetail: Boolean = false) {
        val stock = StockRepository.getStock(symbol)
        val timeframe = _uiState.value.selectedTimeframe
        val newCandles = StockRepository.getCandles(stock.symbol, timeframe)
        val depth = StockRepository.getOrderDepth(stock.symbol)
        val chain = StockRepository.getOptionChain(stock.symbol)

        _uiState.update {
            it.copy(
                selectedStock = stock,
                candles = newCandles,
                orderDepth = depth,
                optionChain = chain,
                isSearchActive = false,
                searchQuery = ""
            )
        }

        if (openDetail) {
            navigateTo(AppScreen.STOCK_DETAIL)
        }
    }

    fun selectTimeframe(tf: String) {
        val stock = _uiState.value.selectedStock ?: return
        val newCandles = StockRepository.getCandles(stock.symbol, tf)
        _uiState.update {
            it.copy(selectedTimeframe = tf, candles = newCandles)
        }
    }

    fun setMarketTab(tab: MarketTab) {
        _uiState.update { it.copy(marketTab = tab) }
    }

    fun onSearchQueryChanged(query: String) {
        val allStocks = _uiState.value.stocks
        val filtered = if (query.isBlank()) {
            emptyList()
        } else {
            allStocks.filter {
                it.symbol.contains(query, ignoreCase = true) ||
                it.name.contains(query, ignoreCase = true) ||
                it.sector.contains(query, ignoreCase = true)
            }
        }
        _uiState.update {
            it.copy(
                searchQuery = query,
                searchResults = filtered,
                isSearchActive = query.isNotBlank()
            )
        }
    }

    fun toggleSearch(active: Boolean) {
        _uiState.update {
            it.copy(
                isSearchActive = active,
                searchQuery = if (!active) "" else it.searchQuery,
                searchResults = if (!active) emptyList() else it.searchResults
            )
        }
    }

    fun toggleFavorite(symbol: String) {
        StockRepository.toggleFavorite(symbol)
    }

    fun toggleAiLanguage() {
        val nextLang = if (_uiState.value.aiLanguage == "en") "hi" else "en"
        _uiState.update { it.copy(aiLanguage = nextLang) }
    }

    fun setAiDrawer(open: Boolean) {
        _uiState.update { it.copy(isAiDrawerOpen = open) }
    }

    fun setOrderSheet(open: Boolean) {
        _uiState.update { it.copy(isOrderSheetOpen = open) }
    }

    fun setNotificationDialog(open: Boolean) {
        _uiState.update { it.copy(isNotificationDialogOpen = open) }
    }

    fun selectStrategy(strategy: TradingStrategy) {
        _uiState.update { it.copy(selectedStrategy = strategy) }
        navigateTo(AppScreen.STRATEGY_BACKTEST)
    }

    fun toggleIndicator(indicator: String) {
        _uiState.update { current ->
            when (indicator) {
                "EMA20" -> current.copy(showEma20 = !current.showEma20)
                "EMA50" -> current.copy(showEma50 = !current.showEma50)
                "VWAP" -> current.copy(showVwap = !current.showVwap)
                "RSI" -> current.copy(showRsi = !current.showRsi)
                "MACD" -> current.copy(showMacd = !current.showMacd)
                else -> current
            }
        }
    }

    fun sendAiPrompt(userText: String) {
        if (userText.isBlank()) return
        val current = _uiState.value
        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = userText,
            isUser = true,
            language = current.aiLanguage
        )

        _uiState.update {
            it.copy(
                chatMessages = it.chatMessages + userMsg,
                isAiThinking = true
            )
        }

        viewModelScope.launch {
            delay(500) // Realistic instant analysis latency
            val stock = current.selectedStock ?: StockRepository.getStock("RELIANCE")
            val depth = current.orderDepth ?: StockRepository.getOrderDepth(stock.symbol)
            val candles = current.candles.ifEmpty { StockRepository.getCandles(stock.symbol, current.selectedTimeframe) }

            val response = if (userText.lowercase().contains("analyze") ||
                userText.lowercase().contains("chart") ||
                userText.contains("विश्लेषण") ||
                userText.lowercase().contains(stock.symbol.lowercase())
            ) {
                AiAssistantEngine.generateStockAnalysis(
                    stock = stock,
                    candles = candles,
                    depth = depth,
                    language = current.aiLanguage,
                    prompt = userText
                )
            } else {
                AiAssistantEngine.answerGenericQuestion(
                    question = userText,
                    language = current.aiLanguage,
                    currentStock = stock
                )
            }

            _uiState.update {
                it.copy(
                    chatMessages = it.chatMessages + response,
                    isAiThinking = false
                )
            }
        }
    }

    fun askAiAboutCurrentStock() {
        val stock = _uiState.value.selectedStock ?: StockRepository.getStock("RELIANCE")
        val prompt = if (_uiState.value.aiLanguage == "hi") {
            "${stock.symbol} का चार्ट और कैंडलस्टिक विश्लेषण करें"
        } else {
            "Analyze current chart and indicators for ${stock.symbol}"
        }
        sendAiPrompt(prompt)
    }

    fun placePaperOrder(
        symbol: String,
        type: OrderType,
        productType: ProductType,
        quantity: Int,
        price: Double,
        stopLoss: Double?,
        target: Double?
    ): Boolean {
        val success = PaperTradingRepository.placeOrder(
            symbol = symbol,
            type = type,
            productType = productType,
            quantity = quantity,
            price = price,
            stopLoss = stopLoss,
            target = target
        )
        if (success) {
            _uiState.update { it.copy(isOrderSheetOpen = false) }
        }
        return success
    }

    fun closePaperPosition(positionId: String, currentPrice: Double) {
        PaperTradingRepository.closePosition(positionId, currentPrice)
    }

    fun resetPaperTrading() {
        PaperTradingRepository.resetPortfolio()
    }
}
