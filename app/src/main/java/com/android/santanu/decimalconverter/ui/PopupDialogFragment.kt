package com.android.santanu.decimalconverter.ui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.android.santanu.decimalconverter.R
import com.android.santanu.decimalconverter.databinding.DialogLayoutBinding
import com.android.santanu.decimalconverter.ui.base.BaseDialogFragment

class PopupDialogFragment(
    private val mListener : PopupDialogFragmentListener,
    private val title : String,
    private val data : String
) : BaseDialogFragment<DialogLayoutBinding>() {
    private val TAG: String = PopupDialogFragment::class.java.simpleName

    override val layoutId: Int = R.layout.dialog_layout

    private lateinit var mLayout: DialogLayoutBinding

    companion object {
        @JvmStatic
        fun getInstance(
            mListener: PopupDialogFragmentListener, title: String, data: String
        ) = PopupDialogFragment(mListener, title, data)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        mLayout = getViewDataBinding()
        return mLayout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLayout.tvText.text = title
        mLayout.tvTextData.text = data

        mLayout.tvOk.setOnClickListener {
            mListener.onDialogExit()
        }
        mLayout.tvCancel.setOnClickListener {
            mListener.onDialogCancel()
        }
        mLayout.ivCopyData.setOnClickListener {
            mListener.onDialogDataCopy(title, data)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    override fun onDetach() {
        super.onDetach()
    }


    interface PopupDialogFragmentListener {
        fun onDialogExit()
        fun onDialogCancel()
        fun onDialogDataCopy(title: String, data: String)

    }

}