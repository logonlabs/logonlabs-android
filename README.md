# LogonLabs Android

The official LogonLabs Android Client library.

## LogonLabs API

- Prior to coding, some configuration is required at https://app.logonlabs.com/app/#/app-settings.

- For the full Developer Documentation please visit: https://app.logonlabs.com/api/

---
### Instantiating a new client

- Your `APP_ID` can be found in [App Settings](https://app.logonlabs.com/app/#/app-settings)
- The `LOGONLABS_API_ENDPOINT` should be set to `https://api.logonlabs.com`
- The `DESTINATION_URL` should be set to the custom url scheme for your application
- Note: this url must be added to the Destination Url Whitelist for your App via [App Settings](https://app.logonlabs.com/app/#/app-settings)

Create a new instance of `LogonClient`.  

```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiViewModel = ViewModelProvider(this).get(logonlabs.android.api.ApiVewModel::class.java)
    }
```
---
### SSO Login QuickStart

The StartLogin function in the Android library begins the LogonLabs managed SSO process. The `clientData` property is optional and is used to pass any data that is required after validating the request.  The `tags` property is an ArrayList of type Tag which is a simple object representing a key/value pair. The `forceReauthentication` property is an optional method to attempt or force an Identity Provider to reauthenticate with the user.

#### Step One

The following example demonstrates what to do once the `Callback Url` has been used by our system to redirect the user back to your page:

```kotlin
        apiViewModel.startLoginAsync("029E592F-C214-4DD0-B882-03CF387F5485",
            "google")
```

```xml
    <data>
        <variable
            name="viewModel"
            type="logonlabs.android.api.ApiVewModel" />
    </data>
	...

        <TextView
			...
            app:apiResponse="@{viewModel.response}"
			app:apiProviders="@{viewModel.providers}"
            />

        <ImageView
			...
            app:apiStatus="@{viewModel.status}" />
```

#### Step Two

The user will be redirected to their device browser and prompted to login with the specified identity provider.  At the end the user will be redirected to your backend server.  After calling ValidateLogin there will be a destination_url parameter that should be redirected back to with a query parameter called `payload`.  This can contain whatever required by your mobile application and should be Base64 encoded.



### Helper Methods
#### GetProviders
This method is used to retrieve a list of all providers enabled for the application.
If an email address is passed to the method, it will return the list of providers available for that email domain.
If any Enterprise Identity Providers have been configured a separate set of matching providers will also be returned in enterprise_identity_providers.

```kotlin
        apiViewModel.getProvidersAsync("029E592F-C214-4DD0-B882-03CF387F5485")
```