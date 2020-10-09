package com.logonlabs.android.api

import com.squareup.moshi.Json

data class Error(
    val code: String?,
    val message: String?,
    @Json(name = "validation_errors") val validationErrors: List<ValidationError>
)

data class ValidationError(
    val property: String?,
    val code: String?,
    val message: String?
)

data class PingResponse(
    val version: String?,
    val error: Error?
)

data class GetProvidersResponse(
    @Json(name="suggested_identity_provider") val suggestedIdentityProvider: String?,
    @Json(name = "social_identity_providers") val socialIdentityProviders: List<Provider>,
    @Json(name = "enterprise_identity_providers") val enterpriseIdentityProviders: List<EnterpriseProvider>,
    @Json(name = "suggested_identity_provider_id") val suggestedIdentityProviderId: String?,
    @Json(name = "error") val error: Error?,
    @Json(name = "invite_required") val inviteRequired: Boolean?,
    @Json(name = "user_passwords") val userPasswords: List<UserPassword>?,
    @Json(name = "user_details") val userDetails: List<UserDetails>?,
    @Json(name = "discovery_required") val discoveryRequired: Boolean?
)

data class Provider(
    val type: String
)

data class EnterpriseProvider(
    val name: String,
    @Json(name = "identity_provider_id") val identityProviderId: String,
    val type: String,
    @Json(name = "login_button_image_uri") val logonButtonImageUrl: String?,
    @Json(name = "login_background_hex_color") val logonBackgroundHexColor: String?,
    @Json(name = "login_icon_image_uri") val logonIconImageUrl: String?,
    @Json(name = "login_text_hex_color") val logonTextHexColor: String?
)

data class UserPassword(
    val enabled: Boolean,
    @Json(name="minimum_password_length") val minPasswordLength: Int?,
    @Json(name="minimum_password_uppercase_characters") val minimumPasswordUppercaseCharacters: Int?,
    @Json(name="minimum_password_lowercase_characters") val minimumPasswordLowercaseCharacters: Int?,
    @Json(name="minimum_password_symbol_characters") val minimumPasswordSymbolCharacters: Int?,
    @Json(name="minimum_password_numeric_characters") val minimumPasswordNumericCharacters: Int?,
)

data class UserDetails(
    val state: String?,
    @Json(name="first_name") val firstName: String?,
    @Json(name="reset_required") val passwordResetRequired: Boolean?,
    @Json(name="has_local") val hasLocalPassword: Boolean?
)

data class StartLoginRequest(
    @Json(name = "app_id") val appId: String,
    @Json(name = "identity_provider") val identityProvider: String? = null,
    @Json(name = "identity_provider_id") val identityProviderId: String? = null,
    @Json(name = "email_address") val emailAddress: String? = null,
    @Json(name = "client_data") val clientData: String? = null,
    @Json(name = "tags") val tags: List<Tag>? = null,
    @Json(name = "destination_url") val destinationUrl: String? = null,
    @Json(name = "callback_url") val callbackUrl: String? = null,
    @Json(name = "force_reauthentication") val forceReauthentication: String? = null
)

// https://kotlinlang.org/docs/reference/serialization.html
// https://github.com/Kotlin/kotlinx.serialization
data class Tag(
    val key: String,
    val value: String
)

data class StartLoginWithPasswordRequest(
    @Json(name = "app_id") val appId: String,
    @Json(name = "email_address") val emailAddress: String? = null,
    @Json(name = "password") val password: String? = null,
    @Json(name = "client_data") val clientData: String? = null,
    @Json(name = "tags") val tags: List<Tag>? = null,
    @Json(name = "destination_url") val destinationUrl: String? = null,
    @Json(name = "callback_url") val callbackUrl: String? = null
)

data class StartRegisterRequest(
    @Json(name = "app_id") val appId: String,
    @Json(name = "identity_provider") val identityProvider: String? = null,
    @Json(name = "identity_provider_id") val identityProviderId: String? = null,
    @Json(name = "email_address") val emailAddress: String? = null,
    @Json(name = "client_data") val clientData: String? = null,
    @Json(name = "tags") val tags: List<Tag>? = null,
    @Json(name = "destination_url") val destinationUrl: String? = null,
    @Json(name = "callback_url") val callbackUrl: String? = null,
    @Json(name = "force_reauthentication") val forceReauthentication: String? = null
)

data class StartRegisterWithPasswordRequest(
    @Json(name = "app_id") val appId: String,
    @Json(name = "email_address") val emailAddress: String? = null,
    @Json(name = "password") val password: String? = null,
    @Json(name = "first_name") val firstName: String? = null,
    @Json(name = "last_name") val lastName: String? = null,
    @Json(name = "client_data") val clientData: String? = null,
    @Json(name = "tags") val tags: List<Tag>? = null,
    @Json(name = "destination_url") val destinationUrl: String? = null,
    @Json(name = "callback_url") val callbackUrl: String? = null,
    @Json(name = "pin") val pin: String? = null
)

data class TokenResponse(
    val token: String,
    val error: Error?
)

data class ConfirmUserRequest(
    @Json(name = "app_id") val appId: String,
    @Json(name = "email_address") val emailAddress: String? = null,
    @Json(name = "pin") val pin: String? = null
)

data class ResetPasswordRequest(
    @Json(name = "app_id") val appId: String,
    @Json(name = "email_address") val emailAddress: String? = null
)

data class ConfirmResetPasswordRequest(
    @Json(name = "app_id") val appId: String,
    @Json(name = "email_address") val emailAddress: String? = null,
    @Json(name = "password") val password: String? = null,
    @Json(name = "pin") val pin: String? = null
)

data class ResendConfirmationEmailRequest(
    @Json(name = "app_id") val appId: String,
    @Json(name = "email_address") val emailAddress: String? = null
)

data class ErrorResponse(
    val error: Error?
)