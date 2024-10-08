package com.duongnh.shapetest.data.repository

import com.duongnh.shapetest.data.data_source.remote.api.ColourApi
import com.duongnh.shapetest.data.mapper.toModel
import com.duongnh.shapetest.domain.MyResult
import com.duongnh.shapetest.domain.model.ColourItem
import com.duongnh.shapetest.domain.repository.ColourRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ColourRepositoryImpl(private val colourApi: ColourApi) : ColourRepository {

    override suspend fun getRandomColor(format: String): Flow<MyResult<ColourItem, String>> = flow {
        val response = colourApi.getRandomColor(format)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(MyResult.Success(it[0].toModel()))
            }
        } else {
            emit(MyResult.Error(response.errorBody()?.string() ?: "Error loading cats"))
        }
    }

    override suspend fun getRandomImageUrl(format: String): Flow<MyResult<ColourItem, String>> =
        flow {
            val response = colourApi.getRandomImageUrl(format)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(MyResult.Success(it[0].toModel()))
                }
            } else {
                emit(MyResult.Error(response.errorBody()?.string() ?: "Error loading cats"))
            }
        }

}