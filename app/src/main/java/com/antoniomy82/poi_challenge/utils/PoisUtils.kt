package com.antoniomy82.poi_challenge.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.antoniomy82.poi_challenge.R

class PoisUtils {

    companion object {

        private var mDialog: AlertDialog?=null

        /**
         * @brief: Function that load a generic "Alert Dialog"
         * @param activity: Fragment activity
         * @param context: Fragment Context
         * @param layout: R.id.xml_resource
         */
        fun genericDialog(
            activity: Activity,
            context: Context, layout:Int ): ViewDataBinding? {

            //Set dialog
            val mBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)

            val viewDataBinding: ViewDataBinding? = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                layout,
                null,
                false
            )

            mBuilder.setView(viewDataBinding?.root)
            mDialog = mBuilder.create()
            mDialog?.show()

            return viewDataBinding
        }


        @JvmStatic
        fun cancelDialog() {
            mDialog?.cancel()
        }

        fun replaceFragment(fragment: Fragment? ,fragmentManager: FragmentManager) {
            try {
                val transaction = fragmentManager.beginTransaction()
                fragment?.let { transaction.replace(R.id.frame_container, it) }
                transaction.commit()
            } catch (e: Exception) {
                Log.e("__replaceFragment", e.toString())
            }
        }
    }
}