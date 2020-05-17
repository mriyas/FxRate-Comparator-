package com.riyas.fxrate.view.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wisilica.imsafe.logI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.riyas.fxrate.view.adapters.FxHistoricRateFragmentAdapter
import com.riyas.fxrate.view.receiver.ConnectivityReceiver
import com.github.mikephil.charting.data.LineData
import android.graphics.Color
import android.graphics.DashPathEffect
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.riyas.fxrate.model.FxRate
import com.github.mikephil.charting.components.Legend
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.riyas.fxrate.R
import com.riyas.fxrate.databinding.FragmentBottomGraphBinding
import com.riyas.fxrate.databinding.FragmentFxRateHomeBinding
import com.riyas.fxrate.view_model.FXViewModel


class GraphBottomSheetFragment : BottomSheetDialogFragment() {

    var mBinding: FragmentBottomGraphBinding? = null

    val mViewModel:FXViewModel by activityViewModels()

    lateinit var mChart:LineChart
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_bottom_graph,
            container,
            false
        ) as FragmentBottomGraphBinding


        mBinding?.lifecycleOwner = viewLifecycleOwner
        mBinding?.fr=this
        // Inflate the layout for this fragment
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mChart=mBinding?.chart!!
        setData()

    }





    private fun setData() {

        val it=mViewModel.mCurrentHistoricData
        val xVals = mViewModel.getXAxisValues(it.value!!)

        val yVals1 = mViewModel.getYAxisValues(it.value!!,0)
        val yVals2 = mViewModel.getYAxisValues(it.value!!,1)
        val set1: LineDataSet

        // create a dataset and give it a type
        set1 = LineDataSet(yVals1.second, yVals1.first)
        set1.fillAlpha = 110
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        // set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.color = Color.RED
        set1.setCircleColor(Color.RED)
        set1.lineWidth = 1f
        set1.circleRadius = 3f
        set1.setDrawCircleHole(false)
        set1.valueTextSize = 9f



        val set2: LineDataSet

        // create a dataset and give it a type
        set2 = LineDataSet(yVals2.second, yVals2.first)
        set2.fillAlpha = 110
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        // set1.enableDashedLine(10f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set2.color = Color.BLUE
        set2.setCircleColor(Color.BLUE)
        set2.lineWidth = 1f
        set2.circleRadius = 3f
        set2.setDrawCircleHole(false)
        set2.valueTextSize = 9f
       // set1.setDrawFilled(true)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1) // add the datasets
        dataSets.add(set2) // add the datasets

        // create a data object with the datasets
        val data = LineData(xVals, dataSets)

        // set data
        mChart.data = data
        val l = mChart.legend

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.form = Legend.LegendForm.LINE

        mChart.invalidate()

    }


}