package com.duongnh.shapetest.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.duongnh.shapetest.presentation.navigation.NavItem
import com.duongnh.shapetest.presentation.tabs.AllScreen
import com.duongnh.shapetest.presentation.tabs.CirclesScreen
import com.duongnh.shapetest.presentation.tabs.SquaresScreen
import com.duongnh.shapetest.presentation.tabs.TrianglesScreen

@Composable
fun NavigationScreens(
    modifier: Modifier,
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()



    NavHost(navController, startDestination = NavItem.Squares.path, modifier = modifier) {
        composable(NavItem.Squares.path) {
            SquaresScreen(
                shapes = uiState.squares,
                createShape = { type, screenWidth, screenHeight, x, y ->
                    viewModel.createShape(type, screenWidth, screenHeight, x, y)
                },
                drawShape = { context, shape, offset ->
                    viewModel.drawShape(context, shape, offset)
                },
                updateShape = { context, shapeType, offset ->
                    viewModel.updateShape(context, shapeType, offset)
                }
            )
        }
        composable(NavItem.Circles.path) {
            CirclesScreen(
                shapes = uiState.circles,
                createShape = { type, screenWidth, screenHeight, x, y ->
                    viewModel.createShape(type, screenWidth, screenHeight, x, y)
                },
                drawShape = { context, shape, offset ->
                    viewModel.drawShape(context, shape, offset)
                },
                updateShape = { context, shapeType, offset ->
                    viewModel.updateShape(context, shapeType, offset)
                }
            )
        }
        composable(NavItem.Triangles.path) {
            TrianglesScreen(
                shapes = uiState.triangles,
                createShape = { type, screenWidth, screenHeight, x, y ->
                    viewModel.createShape(type, screenWidth, screenHeight, x, y)
                },
                drawShape = { context, shape, offset ->
                    viewModel.drawShape(context, shape, offset)
                },
                updateShape = { context, shapeType, offset ->
                    viewModel.updateShape(context, shapeType, offset)
                }
            )
        }
        composable(NavItem.All.path) {
            AllScreen(
                shapes = uiState.allShapes,
                createShape = { type, screenWidth, screenHeight, x, y ->
                    viewModel.createShape(type, screenWidth, screenHeight, x, y)
                },
                drawShapeInAllScreen = { context, shape, offset ->
                    viewModel.drawShapeInAllScreen(context, shape, offset)
                },
                updateShapeInAllScreen = { context, offset ->
                    viewModel.updateShapeInAllScreen(context, offset)
                }
            )
        }
    }

    if (uiState.isLoading) {
        FullScreenProgressBar()
    }
}

@Composable
fun FullScreenProgressBar() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.2f))
            .pointerInput(Unit) {
                detectTapGestures {
                    // Do nothing to disable clicks
                    false
                }
            }
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
        )
    }
}