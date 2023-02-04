package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
  private var binding: ActivityExerciseBinding? = null
  private var restTimer: CountDownTimer? = null
  private var restProgress = 0
  private var exerciseProgress = 0
  private var exerciseTimer: CountDownTimer? = null
  private var startTime: Long = 10000
  private var exerciseList: ArrayList<ExerciseModel>? = null
  private var currentExercisePosition = -1
  private var tts: TextToSpeech? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityExerciseBinding.inflate(layoutInflater)
    setContentView(binding?.root)
    setSupportActionBar(binding?.toolbarExercise)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    exerciseList = Constants.defaultExerciseList()
    binding?.toolbarExercise?.setNavigationOnClickListener {
      @Suppress("DEPRECATION")
      onBackPressed()
    }
    tts = TextToSpeech(this, this)
    binding?.progressBarLayoutExercise?.visibility = View.GONE
    setRestView()
  }

  private fun setRestView() {
    binding?.progressBarLayout?.visibility = View.VISIBLE
    binding?.title?.visibility = View.VISIBLE
    binding?.exerciseName?.visibility = View.INVISIBLE
    binding?.progressBarLayoutExercise?.visibility = View.INVISIBLE
    binding?.exerciseImage?.visibility = View.INVISIBLE
    binding?.upcomingLabel?.visibility = View.VISIBLE
    binding?.upcomingExerciseName?.visibility = View.VISIBLE
    binding?.upcomingExerciseName?.text = exerciseList!![currentExercisePosition+1].getName()
    restTimer?.let {
      it.cancel()
      restProgress = 0
    }
    setRestProgressBar()
  }

  private fun setupExerciseView() {
    binding?.progressBarLayout?.visibility = View.INVISIBLE
    binding?.title?.visibility = View.INVISIBLE
    binding?.exerciseName?.visibility = View.VISIBLE
    binding?.progressBarLayoutExercise?.visibility = View.VISIBLE
    binding?.exerciseImage?.visibility = View.VISIBLE
    binding?.upcomingLabel?.visibility = View.INVISIBLE
    binding?.upcomingExerciseName?.visibility = View.INVISIBLE
    exerciseTimer?.let {
      it.cancel()
      exerciseProgress = 0
    }
    val exerciseName = exerciseList!![currentExercisePosition].getName()
    speakOut(exerciseName)
    binding?.exerciseImage?.setImageResource((exerciseList!![currentExercisePosition].getImage()))
    binding?.exerciseName?.text = exerciseName
    setExerciseProgressBar()
  }


  private fun setExerciseProgressBar() {
    binding?.progressBarExercise?.progress = exerciseProgress
    val countProgress = 30
    exerciseTimer = object : CountDownTimer(countProgress.toLong() * 1000, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        exerciseProgress++
        val progress = countProgress - exerciseProgress
        binding?.progressBarExercise?.progress = progress
        binding?.timerExercise?.text = progress.toString()
      }

      override fun onFinish() {
        if (currentExercisePosition < exerciseList?.size!! - 1) {
          setRestView()
        } else {
          Toast.makeText(
            this@ExerciseActivity,
            "Congratulations! You have completed the 7 minutes workout.",
            Toast.LENGTH_LONG
          ).show()
        }
      }
    }.start()
  }

  private fun setRestProgressBar() {
    binding?.progressBar?.progress = restProgress
    restTimer = object : CountDownTimer(startTime, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        restProgress++
        val progress = (startTime / 1000).toInt() - restProgress
        binding?.progressBar?.progress = progress
        binding?.timer?.text = progress.toString()
      }

      override fun onFinish() {
        currentExercisePosition++
        setupExerciseView()
      }
    }.start()
  }

  override fun onDestroy() {
    super.onDestroy()
    restTimer?.let {
      it.cancel()
      restProgress = 0
    }
    exerciseTimer?.let {
      it.cancel()
      exerciseProgress = 0
    }
    tts?.let {
      it.stop()
      it.shutdown()
    }
    binding = null
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS){
      val result = tts?.setLanguage(Locale.US)
      if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
        Log.e("TTS", "The language specified is not supported!")
      }
    } else {
      Log.e("TTS", "Initialization failed!")
    }
  }

  private fun speakOut(text: String){
    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
  }
}