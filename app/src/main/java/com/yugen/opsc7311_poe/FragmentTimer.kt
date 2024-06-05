package com.yugen.opsc7311_poe

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentTimer.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTimer : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0
    private var isTimerRunning: Boolean = false
    private var focusTime = 0
    private var currentPomoCount = 0
    private var pomoCount = 0
    private var shortBreakTime = 0
    private var longBreakTime = 0
    private var pomoCycleCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timer, container, false)


        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        focusTime = sharedPref?.getInt("focusTime", 25) ?: 25
        pomoCount = sharedPref?.getInt("pomoCount", 4) ?: 4
        shortBreakTime = sharedPref?.getInt("shortBreakTime", 5) ?: 5
        longBreakTime = sharedPref?.getInt("longBreakTime", 15) ?: 15
        pomoCycleCount = sharedPref?.getInt("pomoCycleCount", 2) ?: 2
        val timerDisplay: TextView = view.findViewById(R.id.timerDisplay)
        timerDisplay.text = String.format("%02d\n00", focusTime)
        // Find the timer_settings_button and set an OnClickListener
        val timerSettingsButton: RelativeLayout = view.findViewById(R.id.timer_settings_button)
        timerSettingsButton.setOnClickListener {
            // Create an Intent to start EditTimerActivity
           replaceFragment(FragmentTimerSettings())
        }


        val playButton: RelativeLayout = view.findViewById(R.id.play_button)
        // Get the time from timerDisplay
        val timeString = timerDisplay?.text.toString().split("\n")
        val minutes = timeString[0].toInt()
        val seconds = timeString[1].toInt()

        // Convert time to milliseconds and assign it to timeLeftInMillis
        timeLeftInMillis = (focusTime*60) * 1000L

        playButton.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
            } else {
                if (timer == null) {
                    startTimer(timeLeftInMillis)
                } else {
                    resumeTimer()
                }
            }
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    private fun startTimer(timeInMillis: Long) {
        timer = object: CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val remainingMinutes = millisUntilFinished / 1000 / 60
                val remainingSeconds = millisUntilFinished / 1000 % 60
                val timerDisplay: TextView? = view?.findViewById(R.id.timerDisplay)
                timerDisplay?.text = String.format("%02d\n%02d", remainingMinutes, remainingSeconds)
            }

            override fun onFinish() {
                timer = null
                currentPomoCount++
                if (currentPomoCount == pomoCount) {
                    timeLeftInMillis = (longBreakTime * 60) * 1000L
                    currentPomoCount = 0
                } else {
                    timeLeftInMillis = (shortBreakTime * 60) * 1000L
                }
                startTimer(timeLeftInMillis)
            }
        }.start()

        isTimerRunning = true
    }

    private fun pauseTimer() {
        timer?.cancel()
        timer = null
        isTimerRunning = false
    }

    private fun resumeTimer() {
        startTimer(timeLeftInMillis)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentTimer.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentTimer().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}