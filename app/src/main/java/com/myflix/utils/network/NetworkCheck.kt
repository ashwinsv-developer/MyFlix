package com.myflix.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * 1. The Core Boolean Check (Extension Function)
 * Checks if the device has any active internet transport (WiFi, Cellular, etc.)
 */
fun Context.isConnectedToInternet(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
    } else {
        // Fallback for older Android versions
        @Suppress("DEPRECATION")
        connectivityManager.activeNetworkInfo?.isConnected ?: false
    }
}

/**
 * 2. The Lambda Wrapper (Higher-Order Function)
 * This is the version you use to wrap your API calls or navigation logic.
 * @param context Required to access ConnectivityManager
 * @param onNoInternet Optional callback to trigger if internet is NOT available
 * @param action The block of code to execute if internet is available
 * @return Boolean status of the connection
 */
fun isConnectedToInternet(
    context: Context,
    onNoInternet: (() -> Unit)? = null,
    action: () -> Unit
): Boolean {
    return if (context.isConnectedToInternet()) {
        action.invoke() // Execute the lambda
        true
    } else {
        onNoInternet?.invoke() // Trigger the failure UI logic (like a Snackbar)
        false
    }
}
