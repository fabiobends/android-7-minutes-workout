package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast

class MainActivity : AppCompatActivity() {
  private lateinit var startButton: FrameLayout

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    startButton = findViewById(R.id.start_button)
    startButton.setOnClickListener {
      Toast.makeText(this, "Pressed", Toast.LENGTH_LONG).show()
    }
  }
}