package com.logonlabs.android.app

import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class CallbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_callback)

        // https://developer.android.com/training/app-links/deep-linking

        val action: String? = intent?.action
        val data: Uri? = intent?.data

        val callbackTextView = findViewById<TextView>(R.id.callback_text_view_id);
        callbackTextView.movementMethod = ScrollingMovementMethod()
        callbackTextView.text = data?.query ?: data.toString();
    }
}