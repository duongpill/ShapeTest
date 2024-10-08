package com.duongnh.shapetest.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.duongnh.shapetest.domain.model.ShapeType
import java.util.UUID
import kotlin.random.Random

object Utils {
    fun generateRandomColor(): String {
        val r = Random.nextInt(256)
        val g = Random.nextInt(256)
        val b = Random.nextInt(256)
        return String.format("#%02X%02X%02X", r, g, b)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun getUUID(): String {
        val uuid = UUID.randomUUID().toString()
        return uuid
    }

    fun getRandomSize(width: Float, height: Float): Float {
        val minSize = (width * 0.1).coerceAtLeast((height * 0.1))
        val maxSize = (width * 0.45).coerceAtMost((height * 0.45))
        val size = (minSize.toInt()..maxSize.toInt()).random()
        return size.toFloat()
    }

    fun getRandomType(): ShapeType {
        return ShapeType.entries.toTypedArray().random()
    }

    fun getRandomNumber(): Int {
        return (1..2).random()
    }
}