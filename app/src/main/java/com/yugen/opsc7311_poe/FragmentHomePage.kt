package com.yugen.opsc7311_poe

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.yugen.opsc7311_poe.helpers.UserHelper
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentHomePage.newInstance] factory method to
 * create an instance of this fragment.
 */

class FragmentHomePage : Fragment() {

    private lateinit var btnUpdateHours: Button
    private lateinit var btnMinHours: EditText
    private lateinit var btnMaxHours: EditText
    private lateinit var minGoal: TextView
    private lateinit var maxGoal: TextView
    private lateinit var btnSaveGoals: Button
    lateinit var alertDialog: AlertDialog

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
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        minGoal = view.findViewById<TextView>(R.id.min_goal)
        maxGoal = view.findViewById<TextView>(R.id.max_goal)

        minGoal.text = UserHelper.currentDayHours.toString() + "/"+ UserHelper.minHours.toString()
        maxGoal.text = UserHelper.currentDayHours.toString() + "/"+ UserHelper.maxHours.toString()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Views
        btnUpdateHours = view.findViewById(R.id.btn_update_hours)

        val btnProfilePage = view.findViewById<ImageView>(R.id.profile_picture)

        btnProfilePage.setOnClickListener {
            replaceFragment(FragmentProfilePage())
        }

        // Set OnClickListener for the button
        btnUpdateHours.setOnClickListener {

            // Handle button click event here
            val dialogView = layoutInflater.inflate(R.layout.dialog_daily_goals, null)
            val dialogBuilder = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
            alertDialog = dialogBuilder.create()
            alertDialog.setCanceledOnTouchOutside(true)
            alertDialog.show()
            alertDialog.setOnCancelListener {
                // Handle the state reset or return to timer page
                Log.d("Dialog", "Dialog canceled")
            }

            btnMinHours = dialogView.findViewById(R.id.btn_min_hours)
            btnMaxHours = dialogView.findViewById(R.id.btn_max_hours)
            btnSaveGoals = dialogView.findViewById(R.id.btn_submit_hours)

            btnSaveGoals.setOnClickListener {

                if (btnMinHours.text.toString().isEmpty() || btnMaxHours.text.toString().isEmpty()) {
                    val toastMessage = "Please enter both minimum and maximum hours."
                    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                UserHelper.minHours = btnMinHours.text.toString().toInt()
                UserHelper.maxHours = btnMaxHours.text.toString().toInt()
                UserHelper.DailyGoalMin = btnMinHours.text.toString().toInt() * 60
                UserHelper.DailyGoalMax = btnMaxHours.text.toString().toInt() * 60

                // Display toast message
                val toastMessage = "Goals have been Updated."
                Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                btnMinHours.text.clear()
                btnMaxHours.text.clear()
                minGoal.text = UserHelper.currentDayHours.toString() + "/"+ UserHelper.minHours.toString()
                maxGoal.text = UserHelper.currentDayHours.toString() + "/"+ UserHelper.maxHours.toString()
                alertDialog.dismiss()
            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
            commit()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentHomePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentHomePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}