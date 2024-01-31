package com.movieapp.utils


import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

import com.google.android.material.snackbar.Snackbar

import com.movieapp.R


class CommonUtils {
    companion object {


        fun showErrorSnackBar(data: String, root: View, activity: Activity) {
            val snackBar = Snackbar.make(root, data, Snackbar.LENGTH_SHORT)
            val view: View = snackBar.view
            val tv =
                view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            tv.setTextColor(Color.WHITE)
            snackBar.setBackgroundTint(ContextCompat.getColor(activity, R.color.bullet_shell))
            snackBar.show()
        }


    }
}