package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a7minutesworkout.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {
  private lateinit var binding: ActivityFinishBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityFinishBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbarFinishActivity)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    binding.toolbarFinishActivity.setNavigationOnClickListener {
      @Suppress("DEPRECATION")
      onBackPressed()
    }
    binding.btnFinish.setOnClickListener{
      finish()
    }

  }
}