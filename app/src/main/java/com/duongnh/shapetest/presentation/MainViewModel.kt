package com.duongnh.shapetest.presentation

import android.content.Context
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnh.shapetest.domain.MyResult
import com.duongnh.shapetest.domain.model.Shape
import com.duongnh.shapetest.domain.model.ShapeType
import com.duongnh.shapetest.domain.use_case.GetRandomColorUseCase
import com.duongnh.shapetest.domain.use_case.GetRandomImageUrlUseCase
import com.duongnh.shapetest.presentation.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getRandomColorUseCase: GetRandomColorUseCase,
    private val getRandomImageUrlUseCase: GetRandomImageUrlUseCase
) : ViewModel() {

    private val TAG = "MainViewModel"

    private val _uiState = MutableStateFlow(MainUIState(isLoading = false))

    val uiState: StateFlow<MainUIState> = _uiState

    private fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun createShape(
        type: ShapeType,
        screenWidth: Float,
        screenHeight: Float,
        x: Float,
        y: Float
    ): Shape {
        val size = Utils.getRandomSize(screenWidth, screenHeight)
        // To add them to center of the pointer
        val correctX = x - (size / 2)
        val correctY = y - (size / 2)
        val shape = Shape(Utils.getUUID(), type, correctX, correctY, size, "", "")
        return shape
    }

    fun updateShapeInAllScreen(context: Context, offset: Offset) {
        val tappedShape =
            _uiState.value.allShapes.find { item ->
                item.x <= offset.x && item.x + item.size >= offset.x
                        && item.y <= offset.y && item.y + item.size >= offset.y
            } ?: return

        if (!Utils.isNetworkAvailable(context)) {
            val color = Utils.generateRandomColor()
            tappedShape.color = color
            _uiState.update { state ->
                state.copy(allShapes = toNewList(state.allShapes, tappedShape.id, color = color))
            }
        } else {
            when (tappedShape.type) {
                ShapeType.CIRCLE -> {
                    getRandomColor { color, error ->
                        tappedShape.color = color
                        _uiState.update { state ->
                            state.copy(
                                allShapes = toNewList(
                                    state.allShapes,
                                    tappedShape.id,
                                    color = color
                                ),
                                shape = tappedShape,
                                errorMessage = error
                            )
                        }
                    }
                }

                ShapeType.SQUARE -> {
                    getRandomImageUrl { imageUrl, error ->
                        tappedShape.imageUrl = imageUrl
                        _uiState.update { state ->
                            state.copy(
                                allShapes = toNewList(
                                    state.allShapes,
                                    tappedShape.id,
                                    imageUrl = imageUrl
                                ),
                                shape = tappedShape,
                                errorMessage = error
                            )
                        }
                    }
                }

                ShapeType.TRIANGLE -> {

                }
            }
        }
    }

    fun drawShapeInAllScreen(context: Context, shape: Shape, offset: Offset) {
        val tappedShape =
            _uiState.value.allShapes.find { item ->
                item.x <= offset.x && item.x + item.size >= offset.x
                        && item.y <= offset.y && item.y + item.size >= offset.y
            }
        if (tappedShape != null) {
            return
        }

        if (!Utils.isNetworkAvailable(context)) {
            val color = Utils.generateRandomColor()
            shape.color = color
            _uiState.update { state ->
                state.copy(allShapes = state.allShapes + shape)
            }
        } else {
            when (shape.type) {
                ShapeType.CIRCLE -> {
                    getRandomColor { color, error ->
                        shape.color = color
                        _uiState.update { state ->
                            state.copy(
                                allShapes = state.allShapes + shape,
                                shape = shape,
                                errorMessage = error
                            )
                        }
                    }
                }

                ShapeType.SQUARE -> {
                    getRandomImageUrl { imageUrl, error ->
                        shape.imageUrl = imageUrl
                        _uiState.update { state ->
                            state.copy(
                                allShapes = state.allShapes + shape,
                                shape = shape,
                                errorMessage = error
                            )
                        }
                    }
                }

                ShapeType.TRIANGLE -> {
                    val newShape = updateTriangleShape(shape)
                    _uiState.update { state ->
                        state.copy(allShapes = state.allShapes + newShape, shape = newShape)
                    }
                }
            }
        }
    }

    fun updateShape(context: Context, shapeType: ShapeType, offset: Offset) {
        val tappedShape = checkTappedShape(shapeType, offset) ?: return

        if (!Utils.isNetworkAvailable(context)) {
            val color = Utils.generateRandomColor()
            _uiState.update { state ->
                when (shapeType) {
                    ShapeType.CIRCLE -> {
                        state.copy(
                            circles = toNewList(
                                state.circles,
                                tappedShape.id,
                                color = color
                            )
                        )
                    }

                    ShapeType.SQUARE -> {
                        state.copy(
                            squares = toNewList(
                                state.squares,
                                tappedShape.id,
                                color = color
                            )
                        )
                    }

                    ShapeType.TRIANGLE -> {
                        state.copy(
                            triangles = toNewList(
                                state.triangles,
                                tappedShape.id,
                                color = color
                            )
                        )
                    }
                }
            }
        } else {
            when (shapeType) {
                ShapeType.CIRCLE -> {
                    getRandomColor { color, error ->
                        tappedShape.color = color
                        _uiState.update { state ->
                            state.copy(
                                circles = toNewList(state.circles, tappedShape.id, color = color),
                                shape = tappedShape,
                                errorMessage = error
                            )
                        }
                    }
                }

                ShapeType.SQUARE -> {
                    getRandomImageUrl { imageUrl, error ->
                        tappedShape.imageUrl = imageUrl
                        _uiState.update { state ->
                            state.copy(
                                squares = toNewList(
                                    state.squares,
                                    tappedShape.id,
                                    imageUrl = imageUrl
                                ),
                                shape = tappedShape,
                                errorMessage = error
                            )
                        }
                    }
                }

                ShapeType.TRIANGLE -> {

                }
            }
        }
    }

    fun drawShape(context: Context, shape: Shape, offset: Offset) {
        val tappedShape = checkTappedShape(shape.type, offset)
        if (tappedShape != null) {
            return
        }

        if (!Utils.isNetworkAvailable(context)) {
            shape.color = Utils.generateRandomColor()
            _uiState.update {
                when (shape.type) {
                    ShapeType.CIRCLE -> {
                        it.copy(circles = it.circles + shape, shape = shape)
                    }

                    ShapeType.SQUARE -> {
                        it.copy(squares = it.squares + shape, shape = shape)
                    }

                    ShapeType.TRIANGLE -> {
                        shape.color = _uiState.value.shape.color
                        it.copy(triangles = it.triangles + shape, shape = shape)
                    }
                }
            }
        } else {
            when (shape.type) {
                ShapeType.CIRCLE -> {
                    getRandomColor { color, error ->
                        shape.color = color
                        _uiState.update { state ->
                            state.copy(
                                circles = state.circles + shape,
                                shape = shape,
                                errorMessage = error
                            )
                        }
                    }
                }

                ShapeType.SQUARE -> {
                    getRandomImageUrl { imageUrl, error ->
                        shape.imageUrl = imageUrl
                        _uiState.update { state ->
                            state.copy(
                                squares = state.squares + shape,
                                shape = shape,
                                errorMessage = error
                            )
                        }
                    }
                }

                ShapeType.TRIANGLE -> {
                    val newShape = updateTriangleShape(shape)
                    _uiState.update {
                        it.copy(
                            triangles = it.triangles + newShape,
                            shape = newShape
                        )
                    }
                }
            }
        }
    }

    private fun getRandomColor(callBackShape: (String, String) -> Unit) {
        viewModelScope.launch {
            getRandomColorUseCase.invoke("json")
                .onStart { setLoading(true) }
                .catch { ex ->
                    setLoading(false)
                    Log.e(TAG, ex.message.toString())
                }.collect { result ->
                    setLoading(false)
                    when (result) {
                        is MyResult.Success -> {
                            val color = "#${result.data.hex ?: "FFFFFF"}"
                            callBackShape(color, "")
                        }

                        is MyResult.Error -> {
                            val color = Utils.generateRandomColor()
                            callBackShape(color, result.rawResponse)
                        }
                    }
                }
        }
    }

    private fun getRandomImageUrl(callBackShape: (String, String) -> Unit) {
        viewModelScope.launch {
            getRandomImageUrlUseCase.invoke("json")
                .onStart { setLoading(true) }
                .catch { ex ->
                    setLoading(false)
                    Log.e(TAG, ex.message.toString())
                }.collect { result ->
                    setLoading(false)
                    when (result) {
                        is MyResult.Success -> {
                            val imageUrl = result.data.imageUrl ?: ""
                            callBackShape(imageUrl, "")
                        }

                        is MyResult.Error -> {
                            val color = Utils.generateRandomColor()
                            callBackShape(color, result.rawResponse)
                        }
                    }
                }
        }
    }

    private fun checkTappedShape(shapeType: ShapeType, offset: Offset): Shape? {
        val shapes = when (shapeType) {
            ShapeType.CIRCLE -> _uiState.value.circles
            ShapeType.SQUARE -> _uiState.value.squares
            ShapeType.TRIANGLE -> _uiState.value.triangles
        }
        val tappedShape =
            shapes.find { item ->
                item.x <= offset.x && item.x + item.size >= offset.x
                        && item.y <= offset.y && item.y + item.size >= offset.y
            }
        return tappedShape
    }

    private fun toNewList(
        shapes: List<Shape>,
        id: String,
        color: String? = null,
        imageUrl: String? = null
    ): List<Shape> {
        return shapes.map { item ->
            if (item.id == id) {
                item.copy(color = color, imageUrl = imageUrl)
            } else {
                item
            }
        }
    }

    /**
     * Check to get the color or imageUrl
     */
    private fun updateTriangleShape(shape: Shape): Shape {
        if (uiState.value.shape.color?.isNotEmpty() == true && uiState.value.shape.imageUrl?.isNotEmpty() == true) {
            val randomNumber = Utils.getRandomNumber()
            if (randomNumber == 1) {
                shape.color = uiState.value.shape.color
            } else {
                shape.imageUrl = uiState.value.shape.imageUrl
            }
        } else {
            if (uiState.value.shape.color?.isNotEmpty() == true) {
                shape.color = uiState.value.shape.color
            } else if (uiState.value.shape.imageUrl?.isNotEmpty() == true) {
                shape.imageUrl = uiState.value.shape.imageUrl
            } else {
                shape.color = Utils.generateRandomColor()
            }
        }
        return shape
    }
}