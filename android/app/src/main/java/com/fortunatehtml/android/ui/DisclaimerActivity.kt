package com.fortunatehtml.android.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fortunatehtml.android.R
import com.fortunatehtml.android.data.PreferencesManager

class DisclaimerActivity : AppCompatActivity() {

    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(this)

        if (preferencesManager.disclaimerAccepted) {
            navigateToMain()
            return
        }

        setContentView(R.layout.activity_disclaimer)

        val disclaimerText = findViewById<TextView>(R.id.disclaimerText)
        val acceptCheckbox = findViewById<CheckBox>(R.id.acceptCheckbox)
        val acceptButton = findViewById<Button>(R.id.acceptButton)
        val declineButton = findViewById<Button>(R.id.declineButton)

        disclaimerText.text = getString(R.string.disclaimer_text)

        acceptButton.isEnabled = false
        acceptCheckbox.setOnCheckedChangeListener { _, isChecked ->
            acceptButton.isEnabled = isChecked
        }

        acceptButton.setOnClickListener {
            preferencesManager.disclaimerAccepted = true
            navigateToMain()
        }

        declineButton.setOnClickListener {
            finishAffinity()
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
