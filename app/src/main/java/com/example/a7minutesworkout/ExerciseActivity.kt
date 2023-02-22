package com.example.a7minutesworkout

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
  private var binding: ActivityExerciseBinding? = null
  private var restTimer: CountDownTimer? = null
  private var restProgress = 0
  private var exerciseProgress = 0
  private var exerciseTimer: CountDownTimer? = null
  private var exerciseList: ArrayList<ExerciseModel>? = null
  private var currentExercisePosition = -1
  private var tts: TextToSpeech? = null
  private var player: MediaPlayer? = null
  private lateinit var exerciseStatusAdapter: ExerciseStatusAdapter
  private var exerciseTime = 30
  private var restTime = 10

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
    setExerciseStatus()
  }

  private fun playSound() {
    try {
      val soundURI = R.raw.retro_game
      player = MediaPlayer.create(applicationContext, soundURI)
      player?.isLooping = false
      player?.start()
    } catch (err: Error) {
      err.message?.let { Log.e("player", it) }
    }
  }

  private fun setExerciseStatus() {
    exerciseStatusAdapter = ExerciseStatusAdapter(exerciseList!!)
    binding?.exerciseStatus?.adapter = exerciseStatusAdapter
    binding?.exerciseStatus?.layoutManager =
      LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
  }

  private fun setRestView() {
    playSound()
    binding?.progressBarLayout?.visibility = View.VISIBLE
    binding?.title?.visibility = View.VISIBLE
    binding?.exerciseName?.visibility = View.INVISIBLE
    binding?.progressBarLayoutExercise?.visibility = View.INVISIBLE
    binding?.exerciseImage?.visibility = View.INVISIBLE
    binding?.upcomingLabel?.visibility = View.VISIBLE
    binding?.upcomingExerciseName?.visibility = View.VISIBLE
    binding?.upcomingExerciseName?.text = exerciseList!![currentExercisePosition + 1].getName()
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

    exerciseTimer = object : CountDownTimer(exerciseTime.toLong() * 1000, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        exerciseProgress++
        val progress = exerciseTime - exerciseProgress
        binding?.progressBarExercise?.progress = progress
        binding?.timerExercise?.text = progress.toString()
      }

      override fun onFinish() {
        if (currentExercisePosition < exerciseList?.size!! - 1) {
          exerciseList!![currentExercisePosition].setIsSelected(false)
          exerciseList!![currentExercisePosition].setIsCompleted(true)
          exerciseStatusAdapter.notifyDataSetChanged()
          setRestView()
        } else {
          finish()
          val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
          startActivity(intent)
        }
      }
    }.start()
  }

  private fun setRestProgressBar() {
    binding?.progressBar?.progress = restProgress
    restTimer = object : CountDownTimer(restTime.toLong()*1000, 1000) {
      override fun onTick(millisUntilFinished: Long) {
        restProgress++
        val progress = restTime - restProgress
        binding?.progressBar?.progress = progress
        binding?.timer?.text = progress.toString()
      }

      override fun onFinish() {
        currentExercisePosition++
        exerciseList!![currentExercisePosition].setIsSelected(true)
        exerciseStatusAdapter.notifyDataSetChanged()
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
    player?.stop()
    binding = null
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      val result = tts?.setLanguage(Locale.US)
      if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        Log.e("TTS", "The language specified is not supported!")
      }
    } else {
      Log.e("TTS", "Initialization failed!")
    }
  }

  private fun speakOut(text: String) {
    tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
  }
}