package com.yugen.opsc7311_poe

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

enum class TimerState {
    FOCUS, SHORT_BREAK, LONG_BREAK
}

class FragmentTimer : Fragment() {
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
    private var cycleCount = 0
    private lateinit var timerDisplay: TextView
    private var timerState = TimerState.FOCUS
    private var completedFocusSessions = 0
    private var initialFocusTime = 0
    private lateinit var playButton: RelativeLayout
    private lateinit var playButtonImage: ImageView
    private lateinit var timerStatus: ImageView

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
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        focusTime = sharedPref?.getInt("focusTime", 25) ?: 25
        pomoCount = sharedPref?.getInt("pomoCount", 4) ?: 4
        shortBreakTime = sharedPref?.getInt("shortBreakTime", 5) ?: 5
        longBreakTime = sharedPref?.getInt("longBreakTime", 15) ?: 15
        pomoCycleCount = sharedPref?.getInt("pomoCycleCount", 2) ?: 2

        timerStatus = view.findViewById(R.id.timer_status)
        timerStatus.setImageResource(R.drawable.focus_icon)

        timerDisplay = view.findViewById(R.id.timerDisplay)
        timerDisplay.text = String.format("%02d\n00", focusTime)

        val timerSettingsButton: RelativeLayout = view.findViewById(R.id.timer_settings_button)
        timerSettingsButton.setOnClickListener {
            replaceFragment(FragmentTimerSettings())
        }
        val skipButton: RelativeLayout = view.findViewById(R.id.timer_skip_button)
        skipButton.setOnClickListener {
            skipTimer()
        }

        playButton = view.findViewById(R.id.play_button)
        playButtonImage = playButton.findViewById(R.id.play_icon)

        timeLeftInMillis = (focusTime * 60) * 1000L

        playButton.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
                playButtonImage.setImageResource(R.drawable.play_icon)
            } else {
                if (timer == null) {
                    startTimer(timeLeftInMillis)
                } else {
                    resumeTimer()
                }
                playButtonImage.setImageResource(R.drawable.pause_icon)
            }
        }
        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun startTimer(duration: Long) {
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                timer = null
                when (timerState) {
                    TimerState.FOCUS -> {
                        completedFocusSessions++
                        if (completedFocusSessions % pomoCount == 0) {
                            if (cycleCount < pomoCycleCount - 1) {
                                timerState = TimerState.LONG_BREAK
                                updateTimerStatus(R.drawable.long_break_icon)
                                startTimer(longBreakTime * 60 * 1000L)
                                cycleCount++
                            } else {
                                Toast.makeText(context, "All cycles completed!", Toast.LENGTH_SHORT).show()
                                timerState = TimerState.FOCUS
                                pauseTimer()
                                focusTime = initialFocusTime
                                cycleCount = 0
                                playButtonImage.setImageResource(R.drawable.play_icon)
                            }
                        } else {
                            timerState = TimerState.SHORT_BREAK
                            updateTimerStatus(R.drawable.short_break_icon)
                            startTimer(shortBreakTime * 60 * 1000L)
                        }
                    }
                    TimerState.SHORT_BREAK -> {
                        timerState = TimerState.FOCUS
                        updateTimerStatus(R.drawable.focus_icon)
                        startTimer(focusTime * 60 * 1000L)
                    }
                    TimerState.LONG_BREAK -> {
                        timerState = TimerState.FOCUS
                        updateTimerStatus(R.drawable.focus_icon)
                        startTimer(focusTime * 60 * 1000L)
                    }
                }
            }
        }.start()
        isTimerRunning = true
    }

    private fun updateCountDownText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeLeftFormatted = String.format("%02d\n%02d", minutes, seconds)
        timerDisplay.text = timeLeftFormatted

        // Change text color and background color based on timer state
        val textColor: Int
        when (timerState) {
            TimerState.FOCUS -> {
                textColor = R.color.focusColor
            }
            TimerState.SHORT_BREAK -> {
                textColor = R.color.shortBreakColor
            }
            TimerState.LONG_BREAK -> {
                textColor = R.color.longBreakColor
            }
        }
        timerDisplay.setTextColor(ContextCompat.getColor(requireContext(), textColor))
    }

    private fun pauseTimer() {
        timer?.cancel()
        timer = null
        isTimerRunning = false
    }

    private fun resumeTimer() {
        startTimer(timeLeftInMillis)
    }

    private fun skipTimer() {
        timer?.cancel()
        timer = null
        isTimerRunning = false
        when (timerState) {
            TimerState.FOCUS -> {
                completedFocusSessions++
                if (completedFocusSessions % pomoCount == 0) {
                    if (cycleCount < pomoCycleCount - 1) {
                        timerState = TimerState.LONG_BREAK
                        updateTimerStatus(R.drawable.long_break_icon)
                        startTimer(longBreakTime * 60 * 1000L)
                        cycleCount++
                    } else {
                        Toast.makeText(context, "All cycles completed!", Toast.LENGTH_SHORT).show()
                        timerState = TimerState.FOCUS
                        updateTimerStatus(R.drawable.focus_icon)
                        cycleCount = 0
                    }
                } else {
                    timerState = TimerState.SHORT_BREAK
                    updateTimerStatus(R.drawable.short_break_icon)
                    startTimer(shortBreakTime * 60 * 1000L)
                }
            }
            TimerState.SHORT_BREAK -> {
                timerState = TimerState.FOCUS
                updateTimerStatus(R.drawable.focus_icon)
                startTimer(focusTime * 60 * 1000L)
            }
            TimerState.LONG_BREAK -> {
                timerState = TimerState.FOCUS
                updateTimerStatus(R.drawable.focus_icon)
                startTimer(focusTime * 60 * 1000L)
            }
        }
    }

    private fun updateTimerStatus(resourceId: Int) {
        timerStatus.setImageResource(0)  // Clear the current image
        timerStatus.setImageResource(resourceId)
    }

    companion object {
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
