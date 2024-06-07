package com.yugen.opsc7311_poe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.yugen.opsc7311_poe.helpers.UserHelper
import com.yugen.opsc7311_poe.objects.Medals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val dbHelper = DBHelper

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentProfilePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProfilePage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var accLevel: TextView
    private lateinit var progressBarLvl: ProgressBar
    private lateinit var bronzeMedalCnt: TextView
    private lateinit var silverMedalCnt: TextView
    private lateinit var goldMedalCnt: TextView
    private lateinit var purpleMedalCnt: TextView
    private lateinit var rubyMedalCnt: TextView


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
        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)

        accLevel = view.findViewById(R.id.accLevel)
        progressBarLvl = view.findViewById(R.id.progressBarLvl)
        bronzeMedalCnt = view.findViewById(R.id.bronzeMedalCnt)
        silverMedalCnt = view.findViewById(R.id.silverMedalCnt)
        goldMedalCnt = view.findViewById(R.id.goldMedalCnt)
        purpleMedalCnt = view.findViewById(R.id.purpleMedalCnt)
        rubyMedalCnt = view.findViewById(R.id.rubyMedalCnt)
        runBlocking(Dispatchers.IO) {
            UserHelper.TaskList = dbHelper.getTaskCollection()
        }
        runBlocking (Dispatchers.IO){
            updateProfile()
        }

        return view
    }

    private fun updateProfile() {
        val userID = UserHelper.loggedInUser.userID
        lateinit var monthlyXPList: List<Int>
        runBlocking(Dispatchers.IO){
            monthlyXPList = dbHelper.calculateMonthlyXPAndUpdateMedals(userID)
        }
        val totalXP = monthlyXPList.sum()
        Log.d("Total XP", totalXP.toString())
        val level = totalXP / 1000
        val xpToNextLevel = 1000 - (totalXP % 1000)

        accLevel.text = "Level $level"
        progressBarLvl.max = 1000
        progressBarLvl.progress = totalXP % 1000

        var medals: Medals?
        runBlocking(Dispatchers.IO){
            medals = dbHelper.getMedalsCollection(userID)
        }
        medals?.let {
            bronzeMedalCnt.text = it.bronzeCnt.toString()
            silverMedalCnt.text = it.silverCnt.toString()
            goldMedalCnt.text = it.goldCnt.toString()
            purpleMedalCnt.text = it.purpleCnt.toString()
            rubyMedalCnt.text = it.rubyCnt.toString()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentProfilePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentProfilePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}