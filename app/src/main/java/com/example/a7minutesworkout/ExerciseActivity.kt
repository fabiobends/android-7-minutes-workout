package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
  private var binding: ActivityExerciseBinding? = null
  private var timer: CountDownTimer? = null
  private var restProgress = 0
  private var startTime: Long = 10000

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityExerciseBinding.inflate(layoutInflater)
    setContentView(binding?.root)
    setSupportActionBar(binding?.toolbarExercise)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    binding?.toolbarExercise?.setNavigationOnClickListener {
      onBackPressed()
    }
    setRestView()
  }

  private fun setRestView(){
    timer?.let {
      it.cancel()
      restProgress = 0
    }
    setRestProgressBar()
  }


  private fun setRestProgressBar() {
    binding?.progressBar?.progress = restProgress
    timer = object : CountDownTimer(startTime, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        restProgress++
        val progress = (startTime/1000).toInt() - restProgress
        binding?.progressBar?.progress = progress
        binding?.timer?.text = progress.toString()
      }
      override fun onFinish() {
        Toast.makeText(
          this@ExerciseActivity,
          "Here now we will start the exercise",
          Toast.LENGTH_LONG
        ).show()
      }
    }.start()
  }

  override fun onDestroy() {
    super.onDestroy()
    timer?.let {
      it.cancel()
      restProgress = 0
    }
    binding = null
  }
}