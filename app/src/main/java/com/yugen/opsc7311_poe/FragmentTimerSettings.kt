package com.yugen.opsc7311_poe

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentTimerSettings.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTimerSettings : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var focusTime = 0
    private var pomoCount = 0
    private var currentPomoCount = 0
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
        val view = inflater.inflate(R.layout.fragment_timer_settings, container, false)

        // Get the shared preferences
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        // Get the saved values from shared preferences
        focusTime = sharedPref?.getInt("focusTime", 25) ?: 25
        pomoCount = sharedPref?.getInt("pomoCount", 4) ?: 4
        shortBreakTime = sharedPref?.getInt("shortBreakTime", 5) ?: 5
        longBreakTime = sharedPref?.getInt("longBreakTime", 15) ?: 15
        pomoCycleCount = sharedPref?.getInt("pomoCycleCount", 2) ?: 2

        // For Focus
        val FocusNumberDisplay = view.findViewById<TextView>(R.id.FocusNumberDisplay)
        FocusNumberDisplay.text = focusTime.toString()
        val FocusButtonDecrease = view.findViewById<Button>(R.id.FocusButtonDecrease)
        val FocusButtonIncrease = view.findViewById<Button>(R.id.FocusButtonIncrease)

        var currentValue = focusTime

        FocusButtonIncrease.setOnClickListener {
            if (currentValue < 60) {
                currentValue += 1
                FocusNumberDisplay.text = currentValue.toString()
            }
        }

        FocusButtonDecrease.setOnClickListener {
            if (currentValue > 0) {
                currentValue -= 1
                FocusNumberDisplay.text = currentValue.toString()
            }
        }

        // For Pomodoro
        val PomoNumberDisplay = view.findViewById<TextView>(R.id.PomoNumberDisplay)
        PomoNumberDisplay.text = currentPomoCount.toString()
        val PomoButtonDecrease = view.findViewById<Button>(R.id.PomoButtonDecrease)
        val PomoButtonIncrease = view.findViewById<Button>(R.id.PomoButtonIncrease)

        var pomoCurrentValue = 4
        PomoNumberDisplay.text = pomoCurrentValue.toString()

        PomoButtonIncrease.setOnClickListener {
            if (pomoCurrentValue < 60) {
                pomoCurrentValue += 1
                PomoNumberDisplay.text = pomoCurrentValue.toString()
            }
        }

        PomoButtonDecrease.setOnClickListener {
            if (pomoCurrentValue > 0) {
                pomoCurrentValue -= 1
                PomoNumberDisplay.text = pomoCurrentValue.toString()
            }
        }

        // For Short Break
        val ShortBreakNumberDisplay = view.findViewById<TextView>(R.id.ShortBreakNumberDisplay)
        ShortBreakNumberDisplay.text = shortBreakTime.toString()
        val ShortBreakButtonDecrease = view.findViewById<Button>(R.id.ShortBreakButtonDecrease)
        val ShortBreakButtonIncrease = view.findViewById<Button>(R.id.ShortBreakButtonIncrease)

        var shortBreakCurrentValue = 5
        ShortBreakNumberDisplay.text = shortBreakCurrentValue.toString()

        ShortBreakButtonIncrease.setOnClickListener {
            if (shortBreakCurrentValue < 60) {
                shortBreakCurrentValue += 1
                ShortBreakNumberDisplay.text = shortBreakCurrentValue.toString()
            }
        }

        ShortBreakButtonDecrease.setOnClickListener {
            if (shortBreakCurrentValue > 0) {
                shortBreakCurrentValue -= 1
                ShortBreakNumberDisplay.text = shortBreakCurrentValue.toString()
            }
        }

        // For Long Break
        val LongBreakNumberDisplay = view.findViewById<TextView>(R.id.LongBreakNumberDisplay)
        LongBreakNumberDisplay.text = longBreakTime.toString()
        val LongBreakButtonDecrease = view.findViewById<Button>(R.id.LongBreakButtonDecrease)
        val LongBreakButtonIncrease = view.findViewById<Button>(R.id.LongBreakButtonIncrease)

        var longBreakCurrentValue = 15
        LongBreakNumberDisplay.text = longBreakCurrentValue.toString()

        LongBreakButtonIncrease.setOnClickListener {
            if (longBreakCurrentValue < 60) {
                longBreakCurrentValue += 1
                LongBreakNumberDisplay.text = longBreakCurrentValue.toString()
            }
        }

        LongBreakButtonDecrease.setOnClickListener {
            if (longBreakCurrentValue > 0) {
                longBreakCurrentValue -= 1
                LongBreakNumberDisplay.text = longBreakCurrentValue.toString()
            }
        }

        // For PomoCycle
        val PomoCycleNumberDisplay = view.findViewById<TextView>(R.id.PomoCycleNumberDisplay)
        PomoCycleNumberDisplay.text = pomoCycleCount.toString()
        val PomoCycleButtonDecrease = view.findViewById<Button>(R.id.PomoCycleButtonDecrease)
        val PomoCycleButtonIncrease = view.findViewById<Button>(R.id.PomoCycleButtonIncrease)

        var pomoCycleCurrentValue = pomoCycleCount
        PomoCycleNumberDisplay.text = pomoCycleCurrentValue.toString()

        PomoCycleButtonIncrease.setOnClickListener {
            if (pomoCycleCurrentValue < 100) {
                pomoCycleCurrentValue += 1
                PomoCycleNumberDisplay.text = pomoCycleCurrentValue.toString()
            }
        }

        PomoCycleButtonDecrease.setOnClickListener {
            if (pomoCycleCurrentValue > 0) {
                pomoCycleCurrentValue -= 1
                PomoCycleNumberDisplay.text = pomoCycleCurrentValue.toString()
            }
        }

        val autoResumeTimerSwitch = view.findViewById<SwitchCompat>(R.id.auto_resume_timer_switch)

        if (sharedPref != null) {
            if (sharedPref.contains("autoResumeTimer")) {
                // If the preference exists, set the switch to its value
                autoResumeTimerSwitch.isChecked = sharedPref.getBoolean("autoResumeTimer", false)
            } else {
                // If the preference doesn't exist, set the switch to true and save it
                autoResumeTimerSwitch.isChecked = true
                with(sharedPref.edit()) {
                    putBoolean("autoResumeTimer", true)
                    apply()
                }
            }
        }
        autoResumeTimerSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save the new value when the switch is toggled
            if (sharedPref != null) {
                with(sharedPref.edit()) {
                    putBoolean("autoResumeTimer", isChecked)
                    apply()
                }
            }
        }

        val soundSwitch = view.findViewById<SwitchCompat>(R.id.sound_switch)

        if (sharedPref != null) {
            if (sharedPref.contains("sound_switch")) {
                // If the preference exists, set the switch to its value
                soundSwitch.isChecked = sharedPref.getBoolean("sound_switch", false)
            } else {
                // If the preference doesn't exist, set the switch to true and save it
                soundSwitch.isChecked = true
                with(sharedPref.edit()) {
                    putBoolean("sound_switch", true)
                    apply()
                }
            }
        }
        soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save the new value when the switch is toggled
            if (sharedPref != null) {
                with(sharedPref.edit()) {
                    putBoolean("sound_switch", isChecked)
                    apply()
                }
            }
        }

        // Save Settings Button
        val saveSettingsButton = view.findViewById<Button>(R.id.save_settings_button)

        saveSettingsButton.setOnClickListener()
        {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
            with (sharedPref.edit()) {
                putInt("focusTime", currentValue)
                putInt("pomoCount", pomoCurrentValue)
                putInt("shortBreakTime", shortBreakCurrentValue)
                putInt("longBreakTime", longBreakCurrentValue)
                putInt("pomoCycleCount", pomoCycleCurrentValue)
                apply()
            }
            replaceFragment(FragmentTimer())
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentTimerSettings.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentTimerSettings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}