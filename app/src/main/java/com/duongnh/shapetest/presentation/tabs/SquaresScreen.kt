package com.duongnh.shapetest.presentation.tabs

import android.content.Context
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.duongnh.shapetest.domain.model.Shape
import com.duongnh.shapetest.domain.model.ShapeType
import com.duongnh.shapetest.presentation.tabs.components.ShapeView
import com.duongnh.shapetest.presentation.tabs.components.dpToPx
import com.duongnh.shapetest.presentation.tabs.components.pxToDp

@Composable
fun SquaresScreen(
    shapes: List<Shape>,
    createShape: (ShapeType, Float, Float, Float, Float) -> Shape,
    drawShape: (Context, Shape, Offset) -> Unit,
    updateShape: (Context, ShapeType, Offset) -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp.dpToPx()
    val screenHeight = configuration.screenHeightDp.dp.dpToPx()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = { offset ->
                    updateShape(context, ShapeType.SQUARE, offset)
                }) { offset ->
                    val shape =
                        createShape(ShapeType.SQUARE, screenWidth, screenHeight, offset.x, offset.y)
                    drawShape(context, shape, offset)
                }
            }) {
        shapes.forEach { shape ->
            Box(
                modifier = Modifier
                    .offset(x = shape.x.pxToDp(), y = shape.y.pxToDp())
                    .size(shape.size.pxToDp())
            ) {
                ShapeView(shape = shape)
            }
        }
    }
}
