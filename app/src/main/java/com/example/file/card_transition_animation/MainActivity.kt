package com.example.file.card_transition_animation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.file.card_transition_animation.mockData.setUpMockData
import com.example.file.card_transition_animation.ui.components.CardComponent
import com.example.file.card_transition_animation.ui.theme.CardTransitionAnimationTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardTransitionAnimationTheme {

                var floating by remember { mutableStateOf(true) }
                var floating2 by remember { mutableStateOf(true) }
                var floating3 by remember { mutableStateOf(true) }
                var default by remember { mutableStateOf(true) }
                // Animating the vertical offset based on the floating state
                val offsetY1 = animateDpAsState(
                    targetValue = if (floating) (-64).dp else 0.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = ""
                )
                val offsetY2 = animateDpAsState(
                    targetValue = if (floating) (-128).dp else 0.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = ""
                )

                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxSize()
                        .background(color = Color(0xfff6f7f9))
                ) {

                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        itemsIndexed(
                            items = if (floating) setUpMockData().take(3) else setUpMockData(),
                        ) { index, item ->
                            CardComponent(
                                modifier = Modifier
                                    .offset(
                                        y = when (index) {
                                            1 -> offsetY1.value
                                            2 -> offsetY2.value
                                            else -> 0.dp
                                        },
                                        x = 0.dp,
                                    )
                                    .zIndex(
                                        when (index) {
                                            1 -> if (floating2) -1f else 1f
                                            2 -> if (floating3) -2f else 1f
                                            else -> 1f
                                        }
                                    )
                                    .padding(
                                        horizontal = when (index) {
                                            1 -> if (floating) 8.dp else 0.dp
                                            2 -> if (floating) 16.dp else 0.dp
                                            else -> 0.dp
                                        }
                                    )
                                    .alpha(
                                        when (index) {
                                            0 -> 1f
                                            1 -> 1f
                                            2 -> 1f
                                            else -> if (default) 0f else 1f
                                        }
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                    ) {
                                        floating = !floating
                                    },
                                data = item,
                                visibleBlurEffect = when (index) {
                                    1 -> floating
                                    2 -> floating
                                    else -> false
                                }
                            )
                        }
                    }
                }

                LaunchedEffect(floating) {
                    if (!floating) {
                        delay(100)
                    }
                    default = floating

                    if (!floating) {
                        delay(1000)
                    }
                    floating2 = floating
                    if (!floating) {
                        delay(1500)
                    }
                    floating3 = floating
                }
            }
        }
    }
}