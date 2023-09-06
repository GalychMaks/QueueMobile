package com.example.queue.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.queue.models.UserModel
import com.google.gson.Gson

fun UserModel?.toJson(): String? {
    if (this == null) return null
    return Gson().toJson(this)
}

fun String.toUserModel(): UserModel {
    return Gson().fromJson(this, UserModel::class.java)
}

fun getAuthorization(authorizationToken: String?): String {
    return "Token $authorizationToken"
}

fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}