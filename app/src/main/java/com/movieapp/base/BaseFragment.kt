package com.movieapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : ViewModel, B : ViewBinding> : Fragment() {

    lateinit var binding: B
    lateinit var viewModel: VM
    var appCompatActivity: AppCompatActivity? = null
    abstract fun initViews()
    abstract fun setUpObserver()
    abstract fun setOnClick()
    var view = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appCompatActivity = activity as AppCompatActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()

        viewModel = ViewModelProvider(this).get(getViewModelClass())
        initViews()
        setOnClick()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserver()
    }


    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<VM>
    }

    abstract fun getViewBinding(): B


    open fun getLoadingWidth(): Int {
        val metrics = this.resources.displayMetrics
        return metrics.widthPixels
    }

    open fun getLoadingHeight(): Int {
        val metrics = this.resources.displayMetrics
        return metrics.heightPixels
    }
}
