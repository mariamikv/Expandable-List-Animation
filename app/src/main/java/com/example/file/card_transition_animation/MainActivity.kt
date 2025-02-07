package com.example.file.card_transition_animation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
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
import com.example.file.card_transition_animation.ui.components.HeaderSectionComponent
import com.example.file.card_transition_animation.ui.theme.CardTransitionAnimationTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardTransitionAnimationTheme {
                Content()
            }
        }
    }
}

@Composable
private fun Content() {
    var isFloating by remember { mutableStateOf(true) }
    var isFloatingIndexTwo by remember { mutableStateOf(true) }
    var isFloatingIndexThree by remember { mutableStateOf(true) }
    var isDefaultState by remember { mutableStateOf(true) }

    val firstOffsetY = animateDpAsState(
        targetValue = if (isFloating) (-64).dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "First Offset Animation"
    )

    val secondOffsetY = animateDpAsState(
        targetValue = if (isFloating) (-128).dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "Second Offset Animation"
    )

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .background(color = Color(0xFFF6F7F9))
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 24.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                HeaderSectionComponent(
                    floating = isFloating,
                    onClick = { isFloating = !isFloating }
                )
            }

            itemsIndexed(
                items = if (isFloating) setUpMockData().take(3) else setUpMockData()
            ) { index, item ->
                CardComponent(
                    modifier = Modifier
                        .offset(
                            y = when (index) {
                                1 -> firstOffsetY.value
                                2 -> secondOffsetY.value
                                else -> 0.dp
                            }
                        )
                        .zIndex(
                            when (index) {
                                1 -> if (isFloatingIndexTwo) -1f else 1f
                                2 -> if (isFloatingIndexThree) -2f else 1f
                                else -> 1f
                            }
                        )
                        .padding(
                            horizontal = when (index) {
                                1 -> if (isFloating) 8.dp else 0.dp
                                2 -> if (isFloating) 16.dp else 0.dp
                                else -> 0.dp
                            }
                        )
                        .alpha(
                            when (index) {
                                0, 1, 2 -> 1f
                                else -> if (isDefaultState) 0f else 1f
                            }
                        ),
                    data = item,
                    visibleBlurEffect = index in 1..2 && isFloating
                )
            }
        }
    }

    LaunchedEffect(isFloating) {
        if (!isFloating) {
            delay(100)
        }
        isDefaultState = isFloating

        if (!isFloating) {
            delay(1000)
        }
        isFloatingIndexTwo = isFloating

        if (!isFloating) {
            delay(1500)
        }
        isFloatingIndexThree = isFloating
    }
}
