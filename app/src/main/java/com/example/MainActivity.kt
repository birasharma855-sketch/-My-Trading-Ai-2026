package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppScreen
import com.example.ui.TradeProViewModel
import com.example.ui.components.NotificationDialog
import com.example.ui.components.PaperTradingSheet
import com.example.ui.components.PersistentAiSheet
import com.example.ui.components.TradeProBottomNavigation
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TradeProApp()
            }
        }
    }
}

@Composable
fun TradeProApp(viewModel: TradeProViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle back press
    BackHandler(enabled = uiState.screenStack.size > 1) {
        viewModel.handleBackPress()
    }

    val showBottomBar = uiState.currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.MARKETS,
        AppScreen.WATCHLIST,
        AppScreen.PORTFOLIO,
        AppScreen.AI_ASSISTANT,
        AppScreen.MORE
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                TradeProBottomNavigation(
                    currentScreen = uiState.currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentScreen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        uiState = uiState,
                        onNavigate = { viewModel.navigateTo(it) },
                        onSelectStock = { sym, openDetail -> viewModel.selectStock(sym, openDetail) },
                        onTabSelected = { viewModel.setMarketTab(it) },
                        onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
                        onToggleSearch = { viewModel.toggleSearch(it) },
                        onNotificationClick = { viewModel.setNotificationDialog(true) },
                        onOpenAiSheet = { viewModel.setAiDrawer(true) },
                        onOpenOrderSheet = {
                            viewModel.selectStock(it.symbol, false)
                            viewModel.setOrderSheet(true)
                        },
                        onAskAiStock = {
                            viewModel.selectStock(it.symbol, false)
                            viewModel.askAiAboutCurrentStock()
                            viewModel.setAiDrawer(true)
                        },
                        onSendAiPrompt = {
                            viewModel.sendAiPrompt(it)
                            viewModel.setAiDrawer(true)
                        }
                    )
                }

                AppScreen.MARKETS, AppScreen.SCREENER -> {
                    MarketsScreen(
                        uiState = uiState,
                        onSelectStock = { sym, openDetail -> viewModel.selectStock(sym, openDetail) },
                        onOpenOrderSheet = {
                            viewModel.selectStock(it.symbol, false)
                            viewModel.setOrderSheet(true)
                        },
                        onAskAiStock = {
                            viewModel.selectStock(it.symbol, false)
                            viewModel.askAiAboutCurrentStock()
                            viewModel.setAiDrawer(true)
                        }
                    )
                }

                AppScreen.WATCHLIST -> {
                    WatchlistScreen(
                        uiState = uiState,
                        onNavigate = { viewModel.navigateTo(it) },
                        onSelectStock = { sym, openDetail -> viewModel.selectStock(sym, openDetail) },
                        onOpenOrderSheet = {
                            viewModel.selectStock(it.symbol, false)
                            viewModel.setOrderSheet(true)
                        },
                        onAskAiStock = {
                            viewModel.selectStock(it.symbol, false)
                            viewModel.askAiAboutCurrentStock()
                            viewModel.setAiDrawer(true)
                        }
                    )
                }

                AppScreen.PORTFOLIO -> {
                    PortfolioScreen(
                        uiState = uiState,
                        onNavigate = { viewModel.navigateTo(it) },
                        onClosePosition = { id, price -> viewModel.closePaperPosition(id, price) },
                        onResetPortfolio = { viewModel.resetPaperTrading() }
                    )
                }

                AppScreen.AI_ASSISTANT -> {
                    AiAssistantScreen(
                        uiState = uiState,
                        onSendMessage = { viewModel.sendAiPrompt(it) },
                        onToggleLanguage = { viewModel.toggleAiLanguage() },
                        onSelectStock = { viewModel.selectStock(it, false) }
                    )
                }

                AppScreen.MORE -> {
                    MoreScreen(
                        uiState = uiState,
                        onNavigate = { viewModel.navigateTo(it) },
                        onResetPortfolio = { viewModel.resetPaperTrading() },
                        onToggleLanguage = { viewModel.toggleAiLanguage() }
                    )
                }

                AppScreen.STOCK_DETAIL, AppScreen.LIQUIDITY_ANALYZER -> {
                    StockDetailScreen(
                        uiState = uiState,
                        onBack = { viewModel.handleBackPress() },
                        onTimeframeSelected = { viewModel.selectTimeframe(it) },
                        onToggleIndicator = { viewModel.toggleIndicator(it) },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onOpenOrderSheet = { viewModel.setOrderSheet(true) },
                        onAskAi = {
                            viewModel.askAiAboutCurrentStock()
                            viewModel.setAiDrawer(true)
                        }
                    )
                }

                AppScreen.OPTIONS_CHAIN -> {
                    OptionsChainScreen(
                        uiState = uiState,
                        onBack = { viewModel.handleBackPress() },
                        onSelectSymbol = { viewModel.selectStock(it, false) },
                        onAskAi = { q ->
                            viewModel.sendAiPrompt(q)
                            viewModel.setAiDrawer(true)
                        }
                    )
                }

                AppScreen.STRATEGY_BACKTEST -> {
                    StrategyBacktestScreen(
                        uiState = uiState,
                        onBack = { viewModel.handleBackPress() },
                        onSelectStrategy = { viewModel.selectStrategy(it) },
                        onAskAiStrategy = { strat ->
                            viewModel.sendAiPrompt("Explain how to apply the '${strat.title}' strategy on Nifty and Reliance right now.")
                            viewModel.setAiDrawer(true)
                        }
                    )
                }
            }

            // Global Persistent AI Copilot Sheet (can be summoned from anywhere)
            PersistentAiSheet(
                isOpen = uiState.isAiDrawerOpen,
                onDismiss = { viewModel.setAiDrawer(false) },
                currentStock = uiState.selectedStock,
                messages = uiState.chatMessages,
                isThinking = uiState.isAiThinking,
                language = uiState.aiLanguage,
                onToggleLanguage = { viewModel.toggleAiLanguage() },
                onSendMessage = { viewModel.sendAiPrompt(it) }
            )

            // Paper Trading Order Placement Modal
            PaperTradingSheet(
                isOpen = uiState.isOrderSheetOpen,
                onDismiss = { viewModel.setOrderSheet(false) },
                stock = uiState.selectedStock,
                availableCash = uiState.availableCash,
                onPlaceOrder = { type, prod, qty, price, sl, tp ->
                    val stockSym = uiState.selectedStock?.symbol ?: "STOCK"
                    viewModel.placePaperOrder(stockSym, type, prod, qty, price, sl, tp)
                }
            )

            // Notification Alerts Dialog
            NotificationDialog(
                isOpen = uiState.isNotificationDialogOpen,
                onDismiss = { viewModel.setNotificationDialog(false) }
            )
        }
    }
}
