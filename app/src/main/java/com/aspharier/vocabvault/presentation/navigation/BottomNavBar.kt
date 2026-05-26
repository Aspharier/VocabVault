package com.aspharier.vocabvault.presentation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.aspharier.vocabvault.presentation.theme.LocalGradientColors

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Dictionary,
        BottomNavItem.Settings
    )

    val gradientColors = LocalGradientColors.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val selectedIndex = items.indexOfFirst { it.route == currentRoute }
        .coerceAtLeast(0)

    val itemWidth = 90 // dp per item
    val barWidth = items.size * itemWidth

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Glassmorphism nav bar
        Box(
            modifier = Modifier
                .width(barWidth.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(gradientColors.glassBackground)
                .border(
                    width = 1.dp,
                    color = gradientColors.glassBorder,
                    shape = RoundedCornerShape(32.dp)
                )
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f),
                    RoundedCornerShape(32.dp)
                )
        ) {
            // Sliding gradient indicator
            val indicatorOffset by animateDpAsState(
                targetValue = (selectedIndex * itemWidth).dp,
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                ),
                label = "indicator_offset"
            )

            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .padding(6.dp)
                    .width((itemWidth - 12).dp)
                    .height(52.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                gradientColors.gradientStart.copy(alpha = 0.15f),
                                gradientColors.gradientEnd.copy(alpha = 0.15f)
                            )
                        )
                    )
            )

            // Nav items
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    val selected = index == selectedIndex
                    val interactionSource = remember { MutableInteractionSource() }

                    val contentColor by animateColorAsState(
                        targetValue = if (selected)
                            gradientColors.gradientStart
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        animationSpec = tween(250),
                        label = "nav_color"
                    )

                    val iconScale by animateFloatAsState(
                        targetValue = if (selected) 1.1f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "nav_scale"
                    )

                    Column(
                        modifier = Modifier
                            .width(itemWidth.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = contentColor,
                            modifier = Modifier
                                .size(22.dp)
                                .scale(iconScale)
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = item.label,
                            color = contentColor,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}
