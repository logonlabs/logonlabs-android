package com.logonlabs.android.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.logonlabs.android.api.ApiVewModel
import com.logonlabs.android.api.Tag
import com.logonlabs.android.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiViewModel = ViewModelProvider(this).get(ApiVewModel::class.java)

        // Inflate view and obtain an instance of the binding class.
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        binding.lifecycleOwner = this

        // Assign the component to a property in the binding class.
        binding.viewModel = apiViewModel

        //apiViewModel.getProvidersAsync("029E592F-C214-4DD0-B882-03CF387F5485")

        apiViewModel.startLoginAsync("029E592F-C214-4DD0-B882-03CF387F5485",
            identityProvider = "google",
            callbackUrl = "https://dev-logon-builder-dotnet-sample.azurewebsites.net/callback",
            destinationUrl = "https://logonlabs.com",
            tags = listOf(Tag("k1", "v1")))
    }
}