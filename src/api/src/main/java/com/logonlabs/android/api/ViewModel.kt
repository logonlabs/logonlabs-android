package com.logonlabs.android.api

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class ApiStatus { LOADING, DONE, ERROR }

class ApiVewModel(application: Application) : AndroidViewModel(application){
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private val _tokenResponse = MutableLiveData<TokenResponse>()
    val tokenResponse: LiveData<TokenResponse>
        get() = _tokenResponse

    private val _errorResponse = MutableLiveData<ErrorResponse>()
    val errorResponse: LiveData<ErrorResponse>
        get() = _errorResponse

    private val _getProvidersResponse = MutableLiveData<GetProvidersResponse>()
    val getProvidersResponse: LiveData<GetProvidersResponse>
        get() = _getProvidersResponse

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    fun getProviders(appId: String, emailAddress: String? = null){
        Api.service.getProviders(appId, emailAddress).enqueue(
            object: Callback<GetProvidersResponse> {
                override fun onFailure(call: Call<GetProvidersResponse>, t: Throwable) {
                    _status.value = ApiStatus.ERROR
                    _message.value = "Failure: " + t.message
                }

                override fun onResponse(call: Call<GetProvidersResponse>, apiResponse: Response<GetProvidersResponse>) {
                    _getProvidersResponse.value = apiResponse.body()

                    _status.value = ApiStatus.DONE
                    _message.value = "Retrieved ${apiResponse.body()?.socialIdentityProviders?.size} social providers"
                }
            })
    }

    fun getProvidersAsync(appId: String, emailAddress: String? = null){
        coroutineScope.launch {
            _status.value = ApiStatus.LOADING

            var callAsync = Api.service.getProvidersAsync(appId, emailAddress)

            try {
                var result = callAsync.await();
                _getProvidersResponse.value = result

                _status.value = ApiStatus.DONE
                _message.value = "Retrieved ${result.socialIdentityProviders.size + result.enterpriseIdentityProviders.size} provider details"

            } catch (e: Exception) {
                _getProvidersResponse.value = GetProvidersResponse(
                    null,
                    ArrayList(),
                    ArrayList(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
                )

                _status.value = ApiStatus.ERROR
                _message.value = "Failure: ${e.message}"
            }
        }
    }

    fun startLogin(
        appId: String,
        identityProvider: String? = null,
        identityProviderId: String? = null,
        emailAddress: String? = null,
        clientData: String? = null,
        tags: List<Tag>? = null,
        destinationUrl: String? = null,
        callbackUrl: String? = null,
        forceReauthentication: String? = null
    ){
        val request = StartLoginRequest(
            appId,
            identityProvider,
            identityProviderId,
            emailAddress,
            clientData,
            tags,
            destinationUrl,
            callbackUrl,
            forceReauthentication
        )

        val call = Api.service.startLogin(request);

        call.enqueue(
            object: Callback<TokenResponse> {
                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    _status.value = ApiStatus.ERROR
                    _message.value = "Failure: " + t.message
                }

                override fun onResponse(call: Call<TokenResponse>, apiResponse: Response<TokenResponse>) {
                    if (apiResponse.code() != 200){
                        _status.value = ApiStatus.ERROR
                        _message.value = "Failure: " + apiResponse.raw();
                        return;
                    }

                    val result = apiResponse.body()!!;

                    try {
                        _status.value = ApiStatus.DONE
                        _message.value = "Use this token ${result.token} in RedirectLogin"
                        _tokenResponse.value = apiResponse.body()

                        val i = Intent(Intent.ACTION_VIEW)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        i.data = Uri.parse(
                            Api.redirectLogin(
                                result.token
                            )
                        )
                        getApplication<Application>().applicationContext.startActivity(i)

                    } catch (e: Exception) {
                        _status.value = ApiStatus.ERROR
                        _message.value = "Failure: ${e.message}"
                    }
                }
            })
    }

    fun startLoginAsync(
        appId: String,
        identityProvider: String? = null,
        identityProviderId: String? = null,
        emailAddress: String? = null,
        clientData: String? = null,
        tags: List<Tag>? = null,
        destinationUrl: String? = null,
        callbackUrl: String? = null,
        forceReauthentication: String? = null
    ){
        val request = StartLoginRequest(
            appId,
            identityProvider,
            identityProviderId,
            emailAddress,
            clientData,
            tags,
            destinationUrl,
            callbackUrl,
            forceReauthentication
        )

        coroutineScope.launch {
            _status.value = ApiStatus.LOADING

            var callAsync = Api.service.startLoginAsync(request)

            try {

                var result = callAsync.await()

                _status.value = ApiStatus.DONE
                _message.value = "Use this token ${result.token} in RedirectLogin"
                _tokenResponse.value = result

                val i = Intent(Intent.ACTION_VIEW)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.data = Uri.parse(
                    Api.redirectLogin(
                        result.token
                    )
                )
                getApplication<Application>().applicationContext.startActivity(i)

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _message.value = "Failure: ${e.message}"
            }
        }
    }

    fun loginAsync(
        appId: String,
        identityProvider: String? = null,
        identityProviderId: String? = null,
        emailAddress: String? = null,
        clientData: String? = null,
        tags: List<Tag>? = null,
        destinationUrl: String? = null,
        callbackUrl: String? = null,
        forceReauthentication: String? = null
    ){
        val request = StartLoginRequest(
            appId,
            identityProvider,
            identityProviderId,
            emailAddress,
            clientData,
            tags,
            destinationUrl,
            callbackUrl,
            forceReauthentication
        )

        coroutineScope.launch {
            _status.value = ApiStatus.LOADING

            var callAsync = Api.service.loginAsync(request)

            try {

                var result = callAsync.await()

                _status.value = ApiStatus.DONE
                _message.value = "Use this token ${result.token} in RedirectLogin"
                _tokenResponse.value = result

                val i = Intent(Intent.ACTION_VIEW)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.data = Uri.parse(
                    Api.redirectLogin(
                        result.token
                    )
                )
                getApplication<Application>().applicationContext.startActivity(i)

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _message.value = "Failure: ${e.message}"
            }
        }
    }

    fun loginWithPasswordAsync(
        appId: String,
        emailAddress: String? = null,
        password: String? = null,
        clientData: String? = null,
        tags: List<Tag>? = null,
        destinationUrl: String? = null,
        callbackUrl: String? = null,
    ){
        val request = StartLoginWithPasswordRequest(
            appId,
            emailAddress,
            password,
            clientData,
            tags,
            destinationUrl,
            callbackUrl
        )

        coroutineScope.launch {
            _status.value = ApiStatus.LOADING

            var callAsync = Api.service.loginWithPasswordAsync(request)

            try {

                var result = callAsync.await()

                _status.value = ApiStatus.DONE
                _message.value = "Use this token ${result.token} in RedirectLogin"
                _tokenResponse.value = result

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _message.value = "Failure: ${e.message}"
            }
        }
    }

    fun registerAsync(
        appId: String,
        identityProvider: String? = null,
        identityProviderId: String? = null,
        emailAddress: String? = null,
        clientData: String? = null,
        tags: List<Tag>? = null,
        destinationUrl: String? = null,
        callbackUrl: String? = null,
        forceReauthentication: String? = null
    ){
        val request = StartRegisterRequest(
            appId,
            identityProvider,
            identityProviderId,
            emailAddress,
            clientData,
            tags,
            destinationUrl,
            callbackUrl,
            forceReauthentication
        )

        coroutineScope.launch {
            _status.value = ApiStatus.LOADING

            var callAsync = Api.service.registerAsync(request)

            try {

                var result = callAsync.await()

                _status.value = ApiStatus.DONE
                _message.value = "Use this token ${result.token} in RedirectLogin"
                _tokenResponse.value = result

                val i = Intent(Intent.ACTION_VIEW)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.data = Uri.parse(
                    Api.redirectLogin(
                        result.token
                    )
                )
                getApplication<Application>().applicationContext.startActivity(i)

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _message.value = "Failure: ${e.message}"
            }
        }
    }

    fun registerWithPasswordAsync(
        appId: String,
        emailAddress: String? = null,
        password: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        clientData: String? = null,
        tags: List<Tag>? = null,
        destinationUrl: String? = null,
        callbackUrl: String? = null,
        pin: String? = null,
    ){
        val request = StartRegisterWithPasswordRequest(
            appId,
            emailAddress,
            password,
            firstName,
            lastName,
            clientData,
            tags,
            destinationUrl,
            callbackUrl,
            pin
        )

        coroutineScope.launch {
            _status.value = ApiStatus.LOADING

            var callAsync = Api.service.registerWithPasswordAsync(request)

            try {

                var result = callAsync.await()

                _status.value = ApiStatus.DONE
                _message.value = "Use this token ${result.token} in RedirectLogin"
                _tokenResponse.value = result

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _message.value = "Failure: ${e.message}"
            }
        }
    }

    fun confirmUserAsync(
        appId: String,
        emailAddress: String? = null,
        pin: String? = null,
    ){
        val request = ConfirmUserRequest(
            appId,
            emailAddress,
            pin
        )

        coroutineScope.launch {
            _status.value = ApiStatus.LOADING

            var callAsync = Api.service.confirmUserAsync(request)

            try {

                var result = callAsync.await()

                _status.value = ApiStatus.DONE
                _message.value = "User has been confirmed"
                _tokenResponse.value = result

                val i = Intent(Intent.ACTION_VIEW)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.data = Uri.parse(
                    Api.redirectLogin(
                        result.token
                    )
                )
                getApplication<Application>().applicationContext.startActivity(i)

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _message.value = "Failure: ${e.message}"
            }
        }
    }

    fun resetPasswordAsync(
        appId: String,
        emailAddress: String? = null
    ){
        val request = ResetPasswordRequest(
            appId,
            emailAddress
        )

        coroutineScope.launch {
            _status.value = ApiStatus.LOADING

            var callAsync = Api.service.resetPasswordAsync(request)

            try {

                var result = callAsync.await()

                _status.value = ApiStatus.DONE
                _message.value = "Password has been reset"
                _errorResponse.value = result

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _message.value = "Failure: ${e.message}"
            }
        }
    }

    fun confirmResetPasswordAsync(
        appId: String,
        emailAddress: String? = null,
        password: String? = null,
        pin: String? = null,
    ){
        val request = ConfirmResetPasswordRequest(
            appId,
            emailAddress,
            password,
            pin
        )

        coroutineScope.launch {
            _status.value = ApiStatus.LOADING

            var callAsync = Api.service.confirmRsetPasswordAsync(request)

            try {

                var result = callAsync.await()

                _status.value = ApiStatus.DONE
                _message.value = "Confirmed password has been reset"
                _errorResponse.value = result

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _message.value = "Failure: ${e.message}"
            }
        }
    }

    fun resendConfirmationEmailAsync(
        appId: String,
        emailAddress: String? = null
    ){
        val request = ResendConfirmationEmailRequest(
            appId,
            emailAddress
        )

        coroutineScope.launch {
            _status.value = ApiStatus.LOADING

            var callAsync = Api.service.resendConfirmationEmailAsync(request)

            try {

                var result = callAsync.await()

                _status.value = ApiStatus.DONE
                _message.value = "Confirmation email has been resent"
                _errorResponse.value = result

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _message.value = "Failure: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}