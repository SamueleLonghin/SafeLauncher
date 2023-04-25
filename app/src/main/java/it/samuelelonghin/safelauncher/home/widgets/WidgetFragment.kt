package it.samuelelonghin.safelauncher.home.widgets

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.WidgetFrameBinding
import it.samuelelonghin.safelauncher.settings.SettingsActivity
import it.samuelelonghin.safelauncher.support.*

class WidgetFragment :
    Fragment(R.layout.widget_frame) {


    private val sharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == WIDGET_NUMBER_ROWS || key == WIDGET_NUMBER_COLUMNS) {
                renderGrid()
            }
        }


    /**
     * View
     */
    private lateinit var binding: WidgetFrameBinding
    private lateinit var _view: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout
        binding = WidgetFrameBinding.inflate(inflater)
        _view = binding.root
        println("WidgetFragement :: CreateView")

        return _view
    }


    override fun onStart() {
        super.onStart()
        println("WidgetFragement :: Start")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("WidgetFragement :: Create")
    }

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        println("WidgetFragement :: Inflate")
    }

    override fun onResume() {
        super.onResume()
        launcherPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
        renderGrid()
    }

    private fun renderGrid() {
        val context = requireContext()

        binding.gridViewWidgets.numColumns = widgetNumberColumns


        // on below line we are adding data to
        // our course list with image and course name.
        widgetsList += WidgetInfo(
            "Settings",
            WidgetInfo.WidgetType.ACTIVITY,
            androidx.loader.R.drawable.notification_icon_background,
            SettingsActivity::class.java
        )


        // on below line we are initializing our course adapter
        // and passing course list and context.
        // on below line we are initializing our course adapter
        // and passing course list and context.
        val courseAdapter = WidgetGridAdapter(context, widgetsList)

        // on below line we are setting adapter to our grid view.
        binding.gridViewWidgets.adapter = courseAdapter

        // on below line we are adding on item
        // click listener for our grid view.
//        bin.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
//            // inside on click method we are simply displaying
//            // a toast message with course name.
//            Toast.makeText(
//                applicationContext, courseList[position].courseName + " selected",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
    }
}