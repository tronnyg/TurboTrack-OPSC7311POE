package com.yugen.opsc7311_poe

import android.app.AlertDialog
import android.content.Context
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.yugen.opsc7311_poe.helpers.UserHelper
import com.yugen.opsc7311_poe.objects.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


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
    private lateinit var selectedTask : Task

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
        initializeUIComponents(view)
        loadPreferences()

        runBlocking(Dispatchers.IO){
            UserHelper.TaskList = DBHelper.getTaskCollection()
        }

        timerDisplay.text = formatTime(focusTime * 60 * 1000L)
        timeLeftInMillis = (focusTime * 60 * 1000L)

        playButton.setOnClickListener {
            if (::selectedTask.isInitialized) {
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
            } else {
                Toast.makeText(context, "Please select a task before starting the timer", Toast.LENGTH_SHORT).show()
            }
        }

        val filteredTasks = UserHelper.TaskList.filter { it.completed == false}
        val Tasks  = filteredTasks.map { it.taskName }.toTypedArray()
        val arrayAdapterStatus = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, Tasks)

        val selectTask = view.findViewById<Button>(R.id.select_button)
        lateinit var alertDialog: AlertDialog

        selectTask.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_custom, null)
            val statusDropdown = dialogView.findViewById<AutoCompleteTextView>(R.id.selectTask)
            val selectTaskButton = dialogView.findViewById<Button>(R.id.button_selectTask)

            val filteredTasks = UserHelper.TaskList.filter { !it.completed }
            val tasks = filteredTasks.map { it.taskName }.toTypedArray()
            val arrayAdapterStatus = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, tasks)

            statusDropdown.setAdapter(arrayAdapterStatus)

            val dialogBuilder = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
            alertDialog = dialogBuilder.create()
            alertDialog.setCanceledOnTouchOutside(true)

            alertDialog.setOnCancelListener {
                // Handle the state reset or return to timer page
                Log.d("Dialog", "Dialog canceled")
            }

            selectTaskButton.setOnClickListener {
                val taskName = statusDropdown.text.toString()
                val task = UserHelper.TaskList.find { it.taskName == taskName }
                if (task != null) {
                    selectedTask = task
                    Log.d("Selected Task", selectedTask.taskName)
                    selectTask.text = selectedTask.taskName
                    alertDialog.dismiss()
                } else {
                    Toast.makeText(context, "Please select a valid task", Toast.LENGTH_SHORT).show()
                }
            }
            alertDialog.show()
        }
        return view
    }

    private fun initializeUIComponents(view: View) {
        timerStatus = view.findViewById(R.id.timer_status)
        timerStatus.setImageResource(R.drawable.focus_icon)
        timerDisplay = view.findViewById(R.id.timerDisplay)
        playButton = view.findViewById(R.id.play_button)
        playButtonImage = playButton.findViewById(R.id.play_icon)

        view.findViewById<RelativeLayout>(R.id.timer_settings_button).setOnClickListener {
            replaceFragment(FragmentTimerSettings())
        }

        view.findViewById<RelativeLayout>(R.id.timer_skip_button).setOnClickListener {
            skipTimer()
        }
    }

    private fun loadPreferences() {
        activity?.getPreferences(Context.MODE_PRIVATE)?.apply {
            focusTime = getInt("focusTime", 25)
            pomoCount = getInt("pomoCount", 4)
            shortBreakTime = getInt("shortBreakTime", 5)
            longBreakTime = getInt("longBreakTime", 15)
            pomoCycleCount = getInt("pomoCycleCount", 2)
        }
        initialFocusTime = focusTime
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun startTimer(duration: Long) {
        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                onTimerFinish()
            }
        }.start()
        isTimerRunning = true
    }

    private fun onTimerFinish() {
        timer = null
        when (timerState) {
            TimerState.FOCUS -> handleFocusFinish()
            TimerState.SHORT_BREAK -> startNextTimer(TimerState.FOCUS, focusTime)
            TimerState.LONG_BREAK -> startNextTimer(TimerState.FOCUS, focusTime)
        }
    }

    private fun handleFocusFinish() {
        completedFocusSessions++
        selectedTask.duration += focusTime - (timeLeftInMillis / 1000 / 60).toInt()
        val index = UserHelper.TaskList.indexOfFirst { it.taskName == selectedTask.taskName }

        UserHelper.TaskList[index] = selectedTask
        CoroutineScope(Dispatchers.IO).launch {
            DBHelper.updatePersonTask(UserHelper.TaskList, UserHelper.loggedInUser)
        }

        Log.d("Task Duration", UserHelper.TaskList[index].duration.toString())
        if (completedFocusSessions % pomoCount == 0) {
            if (cycleCount < pomoCycleCount - 1) {
                startNextTimer(TimerState.LONG_BREAK, longBreakTime)
                cycleCount++
            } else {
                resetCycle()
            }
        } else {
            startNextTimer(TimerState.SHORT_BREAK, shortBreakTime)
        }
    }

    private fun resetCycle() {
        timerDisplay.text = formatTime(focusTime * 60 * 1000L)
        Toast.makeText(context, "All cycles completed!", Toast.LENGTH_SHORT).show()
        ringtone()
        timerState = TimerState.FOCUS
        pauseTimer()
        focusTime = initialFocusTime
        cycleCount = 0
        playButtonImage.setImageResource(R.drawable.play_icon)
        val selectTaskButton = view?.findViewById<Button>(R.id.select_button)
        selectTaskButton?.text = "Select Task"
        if (::selectedTask.isInitialized) {
            selectedTask = Task() // Reset the selectedTask variable
        }
    }

    private fun startNextTimer(state: TimerState, time: Int) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val autoResumeTimer = sharedPref?.getBoolean("autoResumeTimer", true) ?: true

        timerState = state
        updateTimerStatus(getIconResource(state))
        startTimer(time * 60 * 1000L)

        if (!autoResumeTimer) {
            // Pause the timer after a short delay
            Handler(Looper.getMainLooper()).postDelayed({
                pauseTimer()
            }, 500)// 500 milliseconds delay
            playButtonImage.setImageResource(R.drawable.play_icon)
        }

    }

    private fun getIconResource(state: TimerState) = when (state) {
        TimerState.FOCUS -> R.drawable.focus_icon
        TimerState.SHORT_BREAK -> R.drawable.short_break_icon
        TimerState.LONG_BREAK -> R.drawable.long_break_icon
    }

    private fun updateCountDownText() {
        timerDisplay.text = formatTime(timeLeftInMillis)
        timerDisplay.setTextColor(ContextCompat.getColor(requireContext(), getColorResource(timerState)))
    }

    private fun getColorResource(state: TimerState) = when (state) {
        TimerState.FOCUS -> R.color.focusColor
        TimerState.SHORT_BREAK -> R.color.shortBreakColor
        TimerState.LONG_BREAK -> R.color.longBreakColor
    }

    private fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%02d\n%02d", minutes, seconds)
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
        if (::selectedTask.isInitialized) {
            pauseTimer()
            onTimerFinish()
        } else {
            Toast.makeText(context, "Please select a task before skipping the timer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTimerStatus(resourceId: Int) {
        timerStatus.setImageResource(resourceId)
    }

    fun ringtone() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val soundSwitch = sharedPref?.getBoolean("sound_switch", true) ?: true

        if (soundSwitch) {
            try {
                val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(context, notification)
                r.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
