package it.samuelelonghin.safelauncher.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import it.samuelelonghin.safelauncher.R
import it.samuelelonghin.safelauncher.databinding.ClockFrameBinding
import it.samuelelonghin.safelauncher.support.DATE_FORMAT
import it.samuelelonghin.safelauncher.support.TIME_FORMAT
import it.samuelelonghin.safelauncher.support.getColorFromAttr
import it.samuelelonghin.safelauncher.support.launcherPreferences
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer


class ClockFragment : Fragment(R.layout.clock_frame) {

    private lateinit var binding: ClockFrameBinding

    // timers
    private var clockTimer = Timer()

    // A UI Fragment must inflate its View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the fragment layout
        binding = ClockFrameBinding.inflate(inflater)
        println("ClockFragment :: CreateView")

        requireActivity().registerReceiver(
            mBatInfoReceiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        val bm = requireContext().getSystemService(BATTERY_SERVICE) as BatteryManager
        val level = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        binding.textViewBattery.text = String.format("%d%s", level, "%")
        return binding.root
    }

    private val mBatInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context?, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val status: Int = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
            binding.textViewBattery.text = String.format("%d%s", level, "%")
            if (isCharging)
                binding.imageViewBattery.setImageResource(R.drawable.ic_baseline_electrical_services_24)
            else if (level < 20)
                binding.imageViewBattery.setImageResource(R.drawable.ic_baseline_battery_0_bar_24)
            else if (level < 30)
                binding.imageViewBattery.setImageResource(R.drawable.ic_baseline_battery_1_bar_24)
            else if (level < 50)
                binding.imageViewBattery.setImageResource(R.drawable.ic_baseline_battery_2_bar_24)
            else if (level < 60)
                binding.imageViewBattery.setImageResource(R.drawable.ic_baseline_battery_3_bar_24)
            else if (level < 70)
                binding.imageViewBattery.setImageResource(R.drawable.ic_baseline_battery_4_bar_24)
            else if (level < 85)
                binding.imageViewBattery.setImageResource(R.drawable.ic_baseline_battery_5_bar_24)
            else if (level < 100)
                binding.imageViewBattery.setImageResource(R.drawable.ic_baseline_battery_6_bar_24)
            else
                binding.imageViewBattery.setImageResource(R.drawable.ic_baseline_battery_full_24)

            val d = DrawableCompat.wrap(binding.imageViewBattery.drawable)
            if (level < 30)
                d.setTint(resources.getColor(R.color.battery_low, requireActivity().theme))
            else if (level < 50)
                d.setTint(resources.getColor(R.color.battery_middle, requireActivity().theme))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (context != null)
                    d.colorFilter = BlendModeColorFilter(
                        context!!.getColorFromAttr(androidx.appcompat.R.attr.colorPrimary),
                        BlendMode.SRC_ATOP
                    )
                else System.err.println("Trovato context nullo in ClockFragment")
            }

        }
    }

    private fun setClock() {
        val locale = Locale.getDefault()

        val timeFormat = launcherPreferences.getInt(TIME_FORMAT, SimpleDateFormat.SHORT)
        val dateFormat = launcherPreferences.getInt(DATE_FORMAT, SimpleDateFormat.FULL)


        clockTimer = fixedRateTimer("clockTimer", true, 0L, 100) {
            requireActivity().runOnUiThread {
                val t = SimpleDateFormat.getTimeInstance(timeFormat, locale).format(Date())
                if (binding.textViewClock.text != t)
                    binding.textViewClock.text = t

                val d = SimpleDateFormat.getDateInstance(dateFormat, locale).format(Date())
                if (binding.textViewDate.text != d)
                    binding.textViewDate.text = d
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setClock()
    }

    override fun onPause() {
        super.onPause()
        clockTimer.cancel()
    }
}