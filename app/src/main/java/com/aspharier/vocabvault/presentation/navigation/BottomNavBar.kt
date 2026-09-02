package com.aspharier.vocabvault.presentation.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.aspharier.vocabvault.presentation.dictionary.DictionaryViewModel
import com.aspharier.vocabvault.presentation.theme.DMMono
import com.aspharier.vocabvault.presentation.theme.VocabTheme

@Composable
fun BottomNavBar(
    navController: NavController,
    dictionaryViewModel: DictionaryViewModel = hiltViewModel()
) {
    val items = listOf(
        BottomNavItem.Search,
        BottomNavItem.Vault,
        BottomNavItem.Settings
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: BottomNavItem.Search.route

    val dictUiState by dictionaryViewModel.uiState.collectAsState()
    val vaultCount = dictUiState.words.size

    val colors = VocabTheme.colors

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.bg)
    ) {
        // Hairline top border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.rule)
                .align(Alignment.TopCenter)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(top = 1.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = item.route == currentRoute
                val interactionSource = remember { MutableInteractionSource() }

                val indicatorWidth by animateDpAsState(
                    targetValue = if (selected) 34.dp else 0.dp,
                    animationSpec = tween(durationMillis = 220, easing = FastOutSlowInEasing),
                    label = "nav_tab_indicator_${item.route}"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (item.route != currentRoute) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                    contentAlignment = Alignment.TopCenter
                ) {
                    // Active hairline indicator bar on top
                    Box(
                        modifier = Modifier
                            .width(indicatorWidth)
                            .height(1.dp)
                            .background(colors.accent)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 13.dp, bottom = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = if (selected) colors.ink else colors.ink3,
                                modifier = Modifier.size(19.dp)
                            )

                            // Word count badge on Vault
                            if (item == BottomNavItem.Vault && vaultCount > 0) {
                                Text(
                                    text = "$vaultCount",
                                    fontFamily = DMMono,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = colors.accent,
                                    modifier = Modifier.offset(x = 16.dp, y = (-7).dp)
                                )
                            }
                        }

                        Text(
                            text = item.label.uppercase(),
                            fontFamily = DMMono,
                            fontSize = 9.sp,
                            letterSpacing = 0.14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (selected) colors.ink else colors.ink3,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                    }
                }
            }
        }
    }
}
