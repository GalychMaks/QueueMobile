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
import com.example.queue.models.CreateQueueRequestModel
import com.example.queue.models.CreateQueueResponseModel
import com.example.queue.models.Key
import com.example.queue.models.LoginRequest
import com.example.queue.models.QueueModel
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

    private val sharedPreferences =
        app.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

    private val _loggedInUserName = MutableLiveData<String>().apply {
        value = sharedPreferences.getString(LOGGED_IN_USER_NAME, "")
    }
    val loggedInUserName: LiveData<String> = _loggedInUserName

    private val _queues = MutableLiveData<Resource<List<QueueModel>>>()
    val queues: LiveData<Resource<List<QueueModel>>> = _queues

    fun getQueues() = viewModelScope.launch {
        _queues.postValue(Resource.Loading())
        try {
            if (!hasInternetConnection()) {
                _queues.postValue(Resource.Error("No Internet"))
            } else {
                val response = repository.getQueues()
                if (!response.isSuccessful) {
                    _queues.postValue(Resource.Error("error" + response.code()))
                } else {
                    response.body()?.let {
                        _queues.postValue(Resource.Success(it))
                    }
                }
            }
        } catch (t: Throwable) {
            _queues.postValue(Resource.Error(t.message.toString()))
        }
    }

    fun login(loginRequest: LoginRequest): LiveData<Resource<Response<Key>>> {
        val resource: MutableLiveData<Resource<Response<Key>>> = MutableLiveData()
        viewModelScope.launch {
            makeLoginRequest(loginRequest, resource)
        }
        return resource
    }

    private suspend fun makeLoginRequest(
        loginRequest: LoginRequest,
        resource: MutableLiveData<Resource<Response<Key>>>
    ) {
        resource.postValue(Resource.Loading())
        try {
            if (!hasInternetConnection()) {
                resource.postValue(Resource.Error("No Internet"))
                return
            }
            val response = repository.login(loginRequest)
            if (!response.isSuccessful) {
                resource.postValue(Resource.Error("error" + response.code()))
                return
            }
            resource.postValue(Resource.Success(response))
            sharedPreferences.edit().apply {
                this.putString(TOKEN_KEY, response.body()?.key)
                this.apply()
            }
            _loggedInUserName.postValue(loginRequest.username)
        } catch (t: Throwable) {
            resource.postValue(Resource.Error(t.message.toString()))
        }
    }

    fun createQueue(createQueueRequestModel: CreateQueueRequestModel): LiveData<Resource<CreateQueueResponseModel>> {
        val resource: MutableLiveData<Resource<CreateQueueResponseModel>> = MutableLiveData()
        viewModelScope.launch {
            resource.postValue(Resource.Loading())
            try {
                if (!hasInternetConnection()) {
                    resource.postValue(Resource.Error("No Internet"))
                    return@launch
                }
                val responseGetUser = repository.getLoggedInUser(
                    "Token ${
                        sharedPreferences.getString(
                            TOKEN_KEY,
                            ""
                        )
                    }"
                )
                if (responseGetUser.isSuccessful) {
                    val response = repository.createQueue(CreateQueueRequestModel(
                        responseGetUser.body()?.pk ?: 0,
                        createQueueRequestModel.name,
                        createQueueRequestModel.description,
                    ))
                    if (response.isSuccessful) {
                        response.body()?.let {
                            resource.postValue(Resource.Success(it))
                        }
                    } else {
                        resource.postValue(Resource.Error("error " + responseGetUser.code()))
                    }
                } else {
                    resource.postValue(Resource.Error("error while getting user " + responseGetUser.code()))
                }
            } catch (t: Throwable) {
                resource.postValue(Resource.Error("Exception"))
            }
        }
        return resource
    }

    fun registration(registrationRequest: RegistrationRequest): LiveData<Resource<Response<Void>>> {
        val resource: MutableLiveData<Resource<Response<Void>>> = MutableLiveData()
        viewModelScope.launch {
            makeRegistrationRequest(registrationRequest, resource)
        }
        return resource
    }

    private suspend fun makeRegistrationRequest(
        registrationRequest: RegistrationRequest,
        resource: MutableLiveData<Resource<Response<Void>>>
    ) {
        resource.postValue(Resource.Loading())
        try {
            if (!hasInternetConnection()) {
                resource.postValue(Resource.Error("No Internet"))
                return
            }
            val response = repository.registration(registrationRequest)
            if (!response.isSuccessful) {
                resource.postValue(Resource.Error("error" + response.code()))
                return
            }
            resource.postValue(Resource.Success(response))
        } catch (t: Throwable) {
            resource.postValue(Resource.Error(t.message.toString()))
        }
    }

    fun logout() = viewModelScope.launch {
        try {
            _loggedInUserName.postValue("")
            repository.logout("Token ${sharedPreferences.getString(TOKEN_KEY, "")}")
        } catch (t: Throwable) {
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
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