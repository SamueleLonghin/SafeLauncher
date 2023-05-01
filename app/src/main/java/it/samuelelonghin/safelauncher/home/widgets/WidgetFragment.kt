package it.samuelelonghin.safelauncher.home.widgets

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.WidgetFrameBinding
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo.WidgetType.ACTIVITY
import it.samuelelonghin.safelauncher.home.widgets.WidgetInfo.WidgetType.APP
import it.samuelelonghin.safelauncher.support.*

class WidgetFragment :
    Fragment(R.layout.widget_frame) {

    private lateinit var selectApp: ActivityResultLauncher<Intent>

    /**
     * View
     */
    private lateinit var binding: WidgetFrameBinding
    private lateinit var _view: View

    enum class Mode {
        PICK, USE
    }

    var mode: Mode = Mode.USE
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout
        binding = WidgetFrameBinding.inflate(inflater)
        _view = binding.root

//        val intento = savedInstanceState?.getString("intento", "view")!!


        println("WidgetFragement :: CreateView con mode: $mode")
//        println("WidgetFragement :: CreateView con intento: $intento")


        selectApp =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == REQUEST_CHOOSE_APP) {
                    val extras = result.data!!.extras!!

                    val i = extras.getInt("index")
                    val typeId = extras.getInt("type")
                    val t: WidgetInfo.WidgetType = WidgetInfo.WidgetType.values()[typeId]
                    val v = extras.getString("value")!!
                    val n = extras.getString("name")!!
//                    val icon = requireActivity().packageManager.getApplicationIcon(v)
                    println("RESULT: $result")
                    println("index: $i, value: $v, type: $t, name: $n")
                    val ws = WidgetSerial(i, v, t)
                    setWidgetListItem(i, ws)
                } else System.err.println("RESULT: $result")
            }


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
        println("WidgetFragement :: Resume")
        val sharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == WIDGET_NUMBER_ROWS || key == WIDGET_NUMBER_COLUMNS) {
                    renderGrid()
                }
            }

        launcherPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
        loadWidgets()
        renderGrid()
    }

    private fun getWidgets(): MutableList<WidgetInfo> {
        var foundSettings = false
        var foundApps = false

        val wl: MutableList<WidgetInfo> = mutableListOf()
        for (ws in widgetsList) {
            lateinit var wi: WidgetInfo
            if (ws.type == APP) {
                wi = WidgetInfo(
                    "Def",
                    ws.type,
                    ws.value
                )
            } else if (ws.type == ACTIVITY) {
                when (ws.value) {
                    ACTIVITY_APPS -> {
                        wi = WidgetInfo(ACTIVITY_APPS)
                        foundApps = true
                    }
                    ACTIVITY_SETTINGS -> {
                        wi = WidgetInfo(ACTIVITY_SETTINGS)
                        foundSettings = true
                    }
                    else -> wl.add(WidgetInfo(ws.value))
                }
            }
            wl.add(wi)
        }

        if (!foundSettings)
            wl.add(WidgetInfo(ACTIVITY_SETTINGS))
        if (!foundApps)
            wl.add(WidgetInfo(ACTIVITY_APPS))
        return wl
    }

    private fun renderGrid() {
        val context = requireContext()
        val widgetsRecycleView = binding.listWidgets

        val nCols = launcherPreferences.getInt(
            WIDGET_NUMBER_COLUMNS,
            WIDGET_NUMBER_COLUMNS_PREF
        )

        val isScrollable = launcherPreferences.getBoolean(
            CONTACTS_IS_SCROLLABLE, CONTACTS_IS_SCROLLABLE_PREF
        )

        val gridLayoutManager = object : GridLayoutManager(context, nCols) {
            override fun canScrollVertically() = isScrollable
        }


        val courseAdapter = WidgetAdapter(context, getWidgets(), mode, selectApp)

        // on below line we are setting adapter to our grid view.
        widgetsRecycleView.apply {
            // improve performance (since content changes don't change the layout size)
            layoutManager = gridLayoutManager
            adapter = courseAdapter
        }
    }
}