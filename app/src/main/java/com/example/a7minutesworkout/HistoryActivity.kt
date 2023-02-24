package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
  private lateinit var binding: ActivityHistoryBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHistoryBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbarHistoryActivity)
    val actionbar = supportActionBar//actionbar
    actionbar?.setDisplayHomeAsUpEnabled(true) //set back button
    actionbar?.title = "HISTORY" // Setting a title in the action bar.
    binding.toolbarHistoryActivity.setNavigationOnClickListener {
      onBackPressed()
    }
  }
}