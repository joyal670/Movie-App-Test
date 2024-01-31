package com.movieapp.base

import android.content.res.Configuration
import android.os.Bundle
import android.os.StrictMode
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.movieapp.R
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<VM : ViewModel, B : ViewBinding> : AppCompatActivity() {

    lateinit var binding: B
    lateinit var viewModel: VM
    private var dialog: AlertDialog? = null
    abstract fun setOnClick()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        binding = getViewBinding()
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        setContentView(binding.root)
        setOnClick()
        initViews()
        adjustFontScale(resources.configuration)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


    }

    open fun adjustFontScale(configuration: Configuration) {
        if (configuration.fontScale > 1.1) {
            configuration.fontScale = 1.0f
            val metrics = resources.displayMetrics
            val wm = baseContext.getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
        if (configuration.fontScale < 1.1) {
            configuration.fontScale = 1.0f
            val metrics = resources.displayMetrics
            val wm = baseContext.getSystemService(WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }

    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<VM>
    }

    abstract fun initViews()
    abstract fun getViewBinding(): B


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showProgress() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        builder.setView(dialogView)
        builder.setCancelable(true)
        dialog = builder.create()
        dialog!!.setCancelable(true)
        dialog!!.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        dialog!!.show()
    }

    fun dismissProgress() {
        dialog!!.dismiss()

    }


}
