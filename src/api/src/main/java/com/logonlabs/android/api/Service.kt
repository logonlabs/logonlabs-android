package com.logonlabs.android.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
//import com.logonlabs.android.api.BuildConfig
//import kotlinx.serialization.json.JsonElement
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val API_BASE_URL =
    BuildConfig.API_BASE_URL // "https://api.logon-dev.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(API_BASE_URL)
    .build()

interface ApiService {
    @GET("ping")
    fun pingAsync(@Query("app_id") appId: String?): Deferred<PingResponse>

    @GET("providers")
    fun getProviders(
        @Query("app_id") appId: String,
        @Query("email_address") emailAddress: String?
    ): Call<GetProvidersResponse>

    @GET("providers")
    fun getProvidersAsync(
        @Query("app_id") appId: String,
        @Query("email_address") emailAddress: String?
    ): Deferred<GetProvidersResponse>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("start")
    fun startLogin(@Body request: StartLoginRequest): Call<TokenResponse>

    // https://medium.com/swlh/simplest-post-request-on-android-kotlin-using-retrofit-e0a9db81f11a
    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("start")
    fun startLoginAsync(@Body request: StartLoginRequest): Deferred<TokenResponse>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("login")
    fun loginAsync(@Body request: StartLoginRequest): Deferred<TokenResponse>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("login_local")
    fun loginWithPasswordAsync(@Body request: StartLoginWithPasswordRequest): Deferred<TokenResponse>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("register")
    fun registerAsync(@Body request: StartRegisterRequest): Deferred<TokenResponse>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("register_local")
    fun registerWithPasswordAsync(@Body request: StartRegisterWithPasswordRequest): Deferred<TokenResponse>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("confirm_user")
    fun confirmUserAsync(@Body request: ConfirmUserRequest): Deferred<TokenResponse>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("reset_password")
    fun resetPasswordAsync(@Body request: ResetPasswordRequest): Deferred<ErrorResponse>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("confirm_password")
    fun confirmRsetPasswordAsync(@Body request: ConfirmResetPasswordRequest): Deferred<ErrorResponse>

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("resend_confirmation")
    fun resendConfirmationEmailAsync(@Body request: ResendConfirmationEmailRequest): Deferred<ErrorResponse>
}

object Api {
    val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun redirectLogin(token: String) = API_BASE_URL + "redirect?token=" + token
}
