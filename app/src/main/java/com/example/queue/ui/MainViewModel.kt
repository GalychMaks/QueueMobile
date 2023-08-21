package com.example.queue.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.queue.models.Key
import com.example.queue.models.LoginRequest
import com.example.queue.models.RegistrationRequest
import com.example.queue.repository.Repository
import com.example.queue.util.Constants.Companion.LOGGED_IN_USER_NAME
import com.example.queue.util.Constants.Companion.SHARED_PREFERENCES
import com.example.queue.util.Constants.Companion.TOKEN_KEY
import com.example.queue.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(
    val app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    private val sharedPreferences = app.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _loggedInUserName = MutableLiveData<String>().apply {
        value = sharedPreferences.getString(LOGGED_IN_USER_NAME, "")
    }
    val loggedInUserName: LiveData<String> = _loggedInUserName

    fun login(loginRequest: LoginRequest, onLoginResponse: suspend (resource: Resource<Response<Key>>) -> Unit) = viewModelScope.launch {
        try {
            if(!hasInternetConnection()) {
                onLoginResponse(Resource.Error("No Internet"))
            } else {
                val response = repository.login(loginRequest)
                if(response.isSuccessful) {
                    sharedPreferences.edit().apply {
                        this.putString(TOKEN_KEY, response.body()?.key)
                        this.apply()
                    }
                    _loggedInUserName.postValue(loginRequest.username)
                    onLoginResponse(Resource.Success(response))
                } else {
                    onLoginResponse(Resource.Error("error" + response.code()))
                }
            }
        } catch(t: Throwable) {
            onLoginResponse(Resource.Error(t.message.toString()))
        }
    }

    fun logout() = viewModelScope.launch {
        try {
            if(hasInternetConnection()) {
                _loggedInUserName.postValue("")
                repository.logout("Token ${sharedPreferences.getString(TOKEN_KEY, "")}")
            }
        } catch (t: Throwable) {
        }
    }

    fun registration(registrationRequest: RegistrationRequest, onRegistrationResponse: suspend (resource: Resource<Response<Void>>) -> Unit) = viewModelScope.launch {
        try {
            if(hasInternetConnection()) {
                val response = repository.registration(registrationRequest)
                if(response.isSuccessful) {

                    onRegistrationResponse(Resource.Success(response))
                } else {
                    onRegistrationResponse(Resource.Error("no internet connection"))
                }
            }
        } catch (t: Throwable) {
            onRegistrationResponse(Resource.Error("exception"))
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}