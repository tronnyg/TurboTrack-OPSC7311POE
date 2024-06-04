package com.yugen.opsc7311_poe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.widget.Button
import com.yugen.opsc7311_poe.helpers.openIntent

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
        // For Focus
        val FocusNumberDisplay = view.findViewById<TextView>(R.id.FocusNumberDisplay)
        val FocusButtonDecrease = view.findViewById<Button>(R.id.FocusButtonDecrease)
        val FocusButtonIncrease = view.findViewById<Button>(R.id.FocusButtonIncrease)

        var currentValue = 25
        FocusNumberDisplay.text = currentValue.toString()

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
        val PomoCycleButtonDecrease = view.findViewById<Button>(R.id.PomoCycleButtonDecrease)
        val PomoCycleButtonIncrease = view.findViewById<Button>(R.id.PomoCycleButtonIncrease)

        var pomoCycleCurrentValue = 4
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

        //Button
        val saveSettingsButton = view.findViewById<Button>(R.id.save_settings_button)

        saveSettingsButton.setOnClickListener()
        {
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