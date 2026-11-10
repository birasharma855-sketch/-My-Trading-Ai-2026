package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.PaperTradingRepository
import com.example.data.repository.StockRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("TradePro AI", appName)
    }

    @Test
    fun `test stock repository data integrity`() {
        val stocks = StockRepository.getAllStocks()
        assertTrue("Stocks list should not be empty", stocks.isNotEmpty())
        val reliance = StockRepository.getStock("RELIANCE")
        assertNotNull("Reliance stock should exist", reliance)
        assertTrue("Symbol should match RELIANCE", reliance.symbol.equals("RELIANCE", ignoreCase = true))

        val candles = StockRepository.getCandles("RELIANCE", "15m")
        assertTrue("Candle list should not be empty", candles.isNotEmpty())

        val depth = StockRepository.getOrderDepth("RELIANCE")
        assertEquals(5, depth.bids.size)
        assertEquals(5, depth.asks.size)
    }

    @Test
    fun `test paper trading initial balance`() {
        val balance = PaperTradingRepository.availableCash.value
        assertEquals(1000000.0, balance, 0.01)
    }
}
