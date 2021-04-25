package com.antoniomy82.poi_challenge.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


class PopupUtil {

    companion object {

        private var mDialog:AlertDialog?=null

        /**
         * @brief: Function that load a generic "Alert Dialog"
         * @param activity: Fragment activity
         * @param context: Fragment Context
         * @param layout: R.id.xml_resource
         */
        fun genericDialog(
            activity: Activity,
            context: Context,layout:Int ): ViewDataBinding? {

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


    }
}