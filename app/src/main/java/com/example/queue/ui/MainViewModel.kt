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
import com.example.queue.R
import com.example.queue.models.CreateQueueRequestModel
import com.example.queue.models.Key
import com.example.queue.models.LoginRequest
import com.example.queue.models.MemberModel
import com.example.queue.models.QueueModel
import com.example.queue.models.RegistrationRequest
import com.example.queue.models.UserModel
import com.example.queue.repository.Repository
import com.example.queue.util.Constants.Companion.AUTHORIZATION_TOKEN
import com.example.queue.util.Constants.Companion.LOGGED_IN_USER
import com.example.queue.util.Constants.Companion.LOGGED_IN_USER_NAME
import com.example.queue.util.Constants.Companion.SHARED_PREFERENCES
import com.example.queue.util.Constants.Companion.TOKEN_KEY
import com.example.queue.util.Resource
import com.example.queue.util.getAuthorization
import com.example.queue.util.hasInternetConnection
import com.example.queue.util.toUserModel
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(
    val app: Application,
    private val repository: Repository
) : AndroidViewModel(app) {

    private val sharedPreferences =
        app.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

    private val _authorizationToken = MutableLiveData<String?>().apply {
        value = sharedPreferences.getString(AUTHORIZATION_TOKEN, null)
    }
    val authorizationToken: LiveData<String?> = _authorizationToken

    private val _loggedInUser = MutableLiveData<UserModel?>().apply {
        val loggedInUserJson = sharedPreferences.getString(LOGGED_IN_USER, null)
        loggedInUserJson?.let {
            value = it.toUserModel()
        }
    }
    val loggedInUser: LiveData<UserModel?> = _loggedInUser

    private val _queues = MutableLiveData<Resource<List<QueueModel>>>()
    val queues: LiveData<Resource<List<QueueModel>>> = _queues

    private val _members = MutableLiveData<Resource<List<MemberModel>>>()
    val members: LiveData<Resource<List<MemberModel>>> = _members

    fun getMembers(queueId: Int) = viewModelScope.launch {
        _members.postValue(Resource.Loading())
        try {
            if (!hasInternetConnection(getApplication<Application>())) {
                _members.postValue(Resource.Error("No Internet"))
            } else {
                val response = repository.getMembers(queueId)
                if (!response.isSuccessful) {
                    _members.postValue(Resource.Error("error" + response.code()))
                } else {
                    response.body()?.let {
                        _members.postValue(Resource.Success(it))
                    }
                }
            }
        } catch (t: Throwable) {
            _members.postValue(Resource.Error(t.message.toString()))
        }
    }

    fun getQueues() = viewModelScope.launch {
        _queues.postValue(Resource.Loading())
        try {
            if (!hasInternetConnection(getApplication<Application>())) {
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

    fun login(loginRequest: LoginRequest): LiveData<Resource<Unit>> {
        val resource: MutableLiveData<Resource<Unit>> = MutableLiveData()
        viewModelScope.launch {
            resource.postValue(Resource.Loading())
            try {
                if (!hasInternetConnection(getApplication<Application>())) {
                    resource.postValue(Resource.Error("No Internet"))
                    return@launch
                }
                val response = repository.login(loginRequest)
                if (!response.isSuccessful) {
                    resource.postValue(Resource.Error("error" + response.code()))
                    return@launch
                }
                val loggedInUserResponse = repository.getLoggedInUser(getAuthorization(response.body()?.key))
                if(!loggedInUserResponse.isSuccessful) {
                    resource.postValue(Resource.Error("error" + loggedInUserResponse.code()))
                    return@launch
                }

                resource.postValue(Resource.Success(Unit))
                _authorizationToken.postValue(response.body()?.key)
                _loggedInUser.postValue(loggedInUserResponse.body())
            } catch (t: Throwable) {
                resource.postValue(Resource.Error(t.message.toString()))
            }
        }
        return resource
    }

    fun createQueue(createQueueRequestModel: CreateQueueRequestModel): LiveData<Resource<QueueModel>> {
        val resource: MutableLiveData<Resource<QueueModel>> = MutableLiveData()
        viewModelScope.launch {
            resource.postValue(Resource.Loading())
            try {
                if (!hasInternetConnection(getApplication<Application>())) {
                    resource.postValue(Resource.Error("No Internet"))
                    return@launch
                }
                val response = repository.createQueue(
                    createQueueRequestModel,
                    getAuthorization(authorizationToken.value)
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        resource.postValue(Resource.Success(it))
                    }
                } else {
                    resource.postValue(Resource.Error("error " + response.code()))
                }

            } catch (t: Throwable) {
                resource.postValue(Resource.Error("Exception"))
            }
        }
        return resource
    }

    fun deleteQueue(queueId: Int): LiveData<Resource<Unit>> {
        val res: MutableLiveData<Resource<Unit>> = MutableLiveData()
        viewModelScope.launch {
            res.postValue(Resource.Loading())
            try {
                if (!hasInternetConnection(getApplication<Application>())) {
                    res.postValue(Resource.Error("No Internet"))
                    return@launch
                }
                val response = repository.deleteQueue(
                    queueId,
                    getAuthorization(authorizationToken.value)
                )
                if (!response.isSuccessful) {
                    res.postValue(Resource.Error("error" + response.code()))
                    return@launch
                }
                res.postValue(Resource.Success(Unit))
            } catch (t: Throwable) {
                res.postValue(t.message?.let { Resource.Error(it) })
            }
        }
        return res
    }

    fun getQueue(code: String): LiveData<Resource<QueueModel>> {
        val resource: MutableLiveData<Resource<QueueModel>> = MutableLiveData()
        viewModelScope.launch {
            resource.postValue(Resource.Loading())
            try {
                if (!hasInternetConnection(getApplication<Application>())) {
                    resource.postValue(Resource.Error("No Internet"))
                    return@launch
                }
                val response = repository.getQueue(code)
                if (!response.isSuccessful) {
                    resource.postValue(Resource.Error("error " + response.code()))
                    return@launch
                }
                response.body()?.let {
                    resource.postValue(Resource.Success(it))
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
            resource.postValue(Resource.Loading())
            try {
                if (!hasInternetConnection(getApplication<Application>())) {
                    resource.postValue(Resource.Error("No Internet"))
                    return@launch
                }
                val response = repository.registration(registrationRequest)
                if (!response.isSuccessful) {
                    resource.postValue(Resource.Error("error" + response.code()))
                    return@launch
                }
                resource.postValue(Resource.Success(response))
            } catch (t: Throwable) {
                resource.postValue(Resource.Error(t.message.toString()))
            }
        }
        return resource
    }

    fun logout() = viewModelScope.launch {
        _authorizationToken.postValue(null)
        _loggedInUser.postValue(null)
        try {
            repository.logout(getAuthorization(authorizationToken.value))
        } catch (_: Throwable) {
        }
    }
}
