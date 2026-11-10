package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen

data class NavItem(
    val screen: AppScreen,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun TradeProBottomNavigation(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems = listOf(
        NavItem(
            screen = AppScreen.HOME,
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        NavItem(
            screen = AppScreen.MARKETS,
            title = "Markets",
            selectedIcon = Icons.Filled.BarChart,
            unselectedIcon = Icons.Outlined.BarChart
        ),
        NavItem(
            screen = AppScreen.WATCHLIST,
            title = "Watchlist",
            selectedIcon = Icons.Filled.Star,
            unselectedIcon = Icons.Outlined.StarOutline
        ),
        NavItem(
            screen = AppScreen.PORTFOLIO,
            title = "Portfolio",
            selectedIcon = Icons.Filled.Work,
            unselectedIcon = Icons.Outlined.WorkOutline
        ),
        NavItem(
            screen = AppScreen.AI_ASSISTANT,
            title = "AI Assistant",
            selectedIcon = Icons.Filled.SmartToy,
            unselectedIcon = Icons.Outlined.SmartToy
        ),
        NavItem(
            screen = AppScreen.MORE,
            title = "More",
            selectedIcon = Icons.Filled.MoreHoriz,
            unselectedIcon = Icons.Outlined.MoreHoriz
        )
    )

    Surface(
        color = Color.White,
        shadowElevation = 6.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                val isSelected = currentScreen == item.screen
                val activeColor = Color(0xFF2563EB)
                val inactiveColor = Color(0xFF64748B)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable { onNavigate(item.screen) }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .testTag("nav_item_${item.title.lowercase().replace(" ", "_")}")
                ) {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        tint = if (isSelected) activeColor else inactiveColor,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = item.title,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) activeColor else inactiveColor
                    )

                    // Active blue underline indicator
                    if (isSelected) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(2.dp)
                                .background(activeColor)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}
