package com.example.data.repository

import com.example.data.model.OrderType
import com.example.data.model.PaperTradePosition
import com.example.data.model.ProductType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object PaperTradingRepository {

    private const val INITIAL_BALANCE = 1000000.0 // 10 Lakh INR

    private val _availableCash = MutableStateFlow(INITIAL_BALANCE)
    val availableCash: StateFlow<Double> = _availableCash.asStateFlow()

    private val _positions = MutableStateFlow<List<PaperTradePosition>>(
        listOf(
            PaperTradePosition(
                id = "pos_1",
                symbol = "RELIANCE",
                type = OrderType.BUY,
                productType = ProductType.DELIVERY,
                quantity = 50,
                avgPrice = 2910.00,
                currentPrice = 2945.60,
                stopLoss = 2850.00,
                target = 3100.00
            ),
            PaperTradePosition(
                id = "pos_2",
                symbol = "TATAMOTORS",
                type = OrderType.BUY,
                productType = ProductType.INTRADAY,
                quantity = 150,
                avgPrice = 985.00,
                currentPrice = 1012.30,
                stopLoss = 970.00,
                target = 1040.00
            )
        )
    )
    val positions: StateFlow<List<PaperTradePosition>> = _positions.asStateFlow()

    private val _closedPositions = MutableStateFlow<List<PaperTradePosition>>(emptyList())
    val closedPositions: StateFlow<List<PaperTradePosition>> = _closedPositions.asStateFlow()

    fun placeOrder(
        symbol: String,
        type: OrderType,
        productType: ProductType,
        quantity: Int,
        price: Double,
        stopLoss: Double? = null,
        target: Double? = null
    ): Boolean {
        val totalCost = quantity * price
        if (type == OrderType.BUY && _availableCash.value < totalCost) {
            return false // Insufficient funds
        }

        if (type == OrderType.BUY) {
            _availableCash.value -= totalCost
        } else {
            // For intraday shorting, margin required
            _availableCash.value -= (totalCost * 0.20)
        }

        val newPos = PaperTradePosition(
            id = UUID.randomUUID().toString(),
            symbol = symbol,
            type = type,
            productType = productType,
            quantity = quantity,
            avgPrice = price,
            currentPrice = price,
            stopLoss = stopLoss,
            target = target
        )

        _positions.value = _positions.value + newPos
        return true
    }

    fun closePosition(positionId: String, currentPrice: Double) {
        val pos = _positions.value.find { it.id == positionId } ?: return
        val finalPnl = if (pos.type == OrderType.BUY) {
            (currentPrice - pos.avgPrice) * pos.quantity
        } else {
            (pos.avgPrice - currentPrice) * pos.quantity
        }

        if (pos.type == OrderType.BUY) {
            _availableCash.value += (pos.investedAmount + finalPnl)
        } else {
            _availableCash.value += ((pos.investedAmount * 0.20) + finalPnl)
        }

        val closed = pos.copy(currentPrice = currentPrice)
        _positions.value = _positions.value.filter { it.id != positionId }
        _closedPositions.value = listOf(closed) + _closedPositions.value
    }

    fun resetPortfolio() {
        _availableCash.value = INITIAL_BALANCE
        _positions.value = emptyList()
        _closedPositions.value = emptyList()
    }
}
