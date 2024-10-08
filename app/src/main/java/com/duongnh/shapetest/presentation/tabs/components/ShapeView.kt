package com.duongnh.shapetest.presentation.tabs.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.duongnh.shapetest.domain.model.Shape
import com.duongnh.shapetest.domain.model.ShapeType


@Composable
fun ShapeView(shape: Shape) {

    val color = shape.color?.let {
        if (it.isNotEmpty()) {
            println("haha $it")
            Color(android.graphics.Color.parseColor(it))
        } else {
            Color.White
        }
    } ?: Color.White

    when (shape.type) {
        ShapeType.SQUARE -> {
            if (shape.imageUrl?.isNotEmpty() == true) {
                SubcomposeAsyncImage(
                    model = shape.imageUrl,
                    modifier = Modifier.size(shape.size.pxToDp()),
                    contentScale = ContentScale.Crop,
                    loading = {
                        ProgressBar()
                    },
                    contentDescription = null
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(shape.size.pxToDp())
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .background(color)
                )
            }
        }

        ShapeType.CIRCLE -> {
            Box(
                modifier = Modifier
                    .size(shape.size.pxToDp())
                    .fillMaxSize()
                    .aspectRatio(1f)
                    .background(
                        color,
                        shape = CircleShape
                    )
            )
        }

        ShapeType.TRIANGLE -> {
            DrawTriangle(shape.imageUrl, shape.size, color)
        }
    }
}

@Composable
fun DrawTriangle(imageUrl: String?, size: Float, color: Color) {
    Box(modifier = Modifier.size(size.pxToDp())) {
        val path = Path().apply {
            moveTo(size / 2, 0f)
            lineTo(0f, size)
            lineTo(size, size)
            close()
        }

        if (imageUrl?.isNotEmpty() == true) {
            SubcomposeAsyncImage(
                model = imageUrl,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CustomShape(path)),
                contentScale = ContentScale.Crop,
                loading = {
                    ProgressBar()
                },
                contentDescription = null
            )
        } else {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawPath(
                    path,
                    color = color
                )
            }
        }
    }
}

@Composable
fun ProgressBar() {
    Box {
        CircularProgressIndicator(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Center),
            color = Color.LightGray,
            strokeWidth = 5.dp
        )
    }
}

data class CustomShape(val path: Path) : androidx.compose.ui.graphics.Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(path)
    }
}