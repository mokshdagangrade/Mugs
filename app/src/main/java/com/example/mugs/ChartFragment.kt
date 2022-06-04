package com.example.mugs

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Toast
import com.example.mugs.databinding.ActivityMainBinding
import com.example.mugs.databinding.FragmentChartBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.util.*
import kotlin.collections.ArrayList
import java.time.LocalDate





class ChartFragment : Fragment() {

    private var scoreList = ArrayList<Score>()
    private var days = 31
    private lateinit var binding: FragmentChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChartBinding.inflate(inflater, container, false)

        setupMonthPicker()

        binding.getChart.setOnClickListener {
            initLineChart()
            setDataToLineChart(days)
        }

        return binding.root
    }

    private fun initLineChart() {

//        hide grid lines
        binding.chart1.axisLeft.setDrawGridLines(true)
        val xAxis: XAxis = binding.chart1.xAxis
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(false)
        val position = XAxisPosition.BOTTOM
        xAxis.position = position

        //remove right y-axis
        binding.chart1.axisRight.isEnabled = false

        //remove legend
        binding.chart1.legend.isEnabled = false

        //remove description label
        binding.chart1.description.isEnabled = false


        //add animation
        binding.chart1.animateX(1000, Easing.EaseInSine)

        // to draw label on xAxis
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = 90f
    }


    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < scoreList.size) {
                scoreList[index].day.toString()
            } else
                return ""
        }
    }


    private fun setDataToLineChart(days: Int) {
        //now draw bar chart with dynamic data
        val entries: ArrayList<Entry> = ArrayList()

        scoreList = getScoreList(days)

        //you can replace this data object with  your custom object
        for (i in scoreList.indices) {
            val score = scoreList[i]
            entries.add(Entry(i.toFloat(), score.score.toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "")

        val data = LineData(lineDataSet)
        data.notifyDataChanged()
        binding.chart1.data = data

        binding.chart1.invalidate()
    }

    // simulate api call
    // we are initialising it directly
    private fun getScoreList(day: Int): ArrayList<Score> {
        for (i in 1..day) {
            if (i % 2 == 0)
                scoreList.add(Score(i, 56))
            else
                scoreList.add(Score(i, 25))
        }
        return scoreList
    }

    private fun setupMonthPicker() {
        val values = arrayOf("Jan", "Feb", "March", "April", "May", "June")
        binding.numberPicker.minValue = 0
        binding.numberPicker.maxValue = values.size - 1
        binding.numberPicker.displayedValues = values
        binding.numberPicker.wrapSelectorWheel = true
        binding.numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            //val text = "Changed from " + values[oldVal] + " to " + values[newVal]
            // Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
            if (values[newVal] == "Jan") {
                val cal = Calendar.getInstance()
                days = cal.getActualMaximum(Calendar.JANUARY)

            }
            if (values[newVal] == "Feb") {
                val cal = Calendar.getInstance()
                days = cal.getActualMaximum(Calendar.FEBRUARY)

            }
        }
    }
}
