package com.android.santanu.decimalconverter.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.santanu.decimalconverter.R
import com.android.santanu.decimalconverter.adapter.NumberRecyclerAdapter
import com.android.santanu.decimalconverter.data.CustomNumber
import com.android.santanu.decimalconverter.data.logic.NumberConverter
import com.android.santanu.decimalconverter.databinding.ActivityCustomFormatBinding
import com.android.santanu.decimalconverter.ui.base.BaseActivity
import java.util.*
import kotlin.collections.ArrayList

class CustomFormatActivity : BaseActivity<ActivityCustomFormatBinding>(),
    NumberRecyclerAdapter.RecyclerAdapterListener {
    private val TAG: String by lazy { CustomFormatActivity::class.java.simpleName }

    private lateinit var mLayout: ActivityCustomFormatBinding

    override fun getLayoutId(): Int = R.layout.activity_custom_format

    private val formateList: ArrayList<String> = ArrayList<String>().apply {
        for (i in 2..34) {
            this.add("Base Format : $i")
        }
    }
    private var baseFormat: String = ""
    private var isFractionActive: Boolean = false
    private var toRangeData: String = ""
    private var fromRangeData: String = ""
    private var inputData: String = ""

    private var mNumberConverter: NumberConverter? = null

    private var recyclerAdapter: NumberRecyclerAdapter? = null
    private var recyclerAdapterDataList: ArrayList<CustomNumber>? = null

    private lateinit var clipboard : ClipboardManager


    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(
                this@CustomFormatActivity, R.color.teal_200
            )
        }

        supportActionBar?.hide()

        super.onCreate(savedInstanceState)

        mLayout = getViewDataBinding().apply {
            this.lifecycleOwner = this@CustomFormatActivity
        }

        if(mNumberConverter == null) {
            mNumberConverter = NumberConverter()
        }
        mLayout.inputLayout.isEndIconVisible = false
        clipboard  = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        mLayout.swFractionActive.setOnCheckedChangeListener { _, isChecked ->
            isFractionActive = isChecked
        }

        mLayout.toInputData.doOnTextChanged { text, _, _, _ ->
            toRangeData = text.toString()

            if(text.toString().isEmpty()) {
                mLayout.toLayout.error = null
                mLayout.toLayout.isEndIconVisible = false
            } else {
                if(text.toString().toInt() in 2..36) {
                    mLayout.toLayout.error = null
                    mLayout.toLayout.isEndIconVisible = true
                } else {
                    mLayout.toLayout.error = "entered data out of range"
                    mLayout.toLayout.isEndIconVisible = false
                }
            }
        }

        mLayout.fromInputData.doOnTextChanged { text, _, _, _ ->
            fromRangeData = text.toString()

            if(text.toString().isEmpty()) {
                mLayout.fromLayout.error = null
                mLayout.fromLayout.isEndIconVisible = false
            } else {
                if(text.toString().toInt() in 2..36) {
                    mLayout.fromLayout.error = null
                    mLayout.fromLayout.isEndIconVisible = true
                } else {
                    mLayout.fromLayout.error = "entered data out of range"
                    mLayout.fromLayout.isEndIconVisible = false
                }
            }
        }

        mLayout.inputData.doOnTextChanged { text, _, _, _ ->
            if(text.toString().isEmpty()) {
                mLayout.inputLayout.error = null
                mLayout.inputLayout.isEndIconVisible = false
            } else {
                if(mNumberConverter!!.isDecimalNumber(text.toString(), isFractionActive)) {
                    mLayout.inputLayout.error = null
                    mLayout.inputLayout.isEndIconVisible = true
                } else {
                    mLayout.inputLayout.error = "incorrect ${baseFormat.lowercase(
                        Locale.getDefault()
                    )} number format"
                    mLayout.inputLayout.isEndIconVisible = false
                }
            }
        }

        mLayout.inputLayout.setEndIconOnClickListener {
            val copyText: String = mLayout.inputData.text.toString()
            if(copyText.isNotEmpty()) {
                ClipData.newPlainText("Decimal", copyText).also {
                    clipboard.setPrimaryClip(it)
                }
                Toast.makeText(this@CustomFormatActivity, "${"Decimal".lowercase(Locale.getDefault())} data copy to clipboard", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else {
                Toast.makeText(this@CustomFormatActivity, "sorry, can't copy content. ${"Decimal".lowercase(Locale.getDefault())} value is empty", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        }

        mLayout.convertButton.setOnClickListener {
            inputData = mLayout.inputData.text.toString()
            mLayout.pbLoading.visibility = View.VISIBLE
            if (toRangeData.isNotEmpty()) {
                if (fromRangeData.isNotEmpty()) {
                    if (toRangeData.toInt() in 2..36) {
                        if (fromRangeData.toInt() in 2..36) {
                            if (toRangeData.toInt() <= fromRangeData.toInt()) {
                                if (inputData.isNotEmpty()) {
                                    processed(toRangeData.toIntOrNull(), fromRangeData.toIntOrNull(), inputData)
                                } else {
                                    mLayout.pbLoading.visibility = View.INVISIBLE
                                    Toast.makeText(this@CustomFormatActivity, "empty input data, please enter decimal value", Toast.LENGTH_SHORT).also {
                                        it.show()
                                    }
                                }
                            } else {
                                mLayout.pbLoading.visibility = View.INVISIBLE
                                Toast.makeText(this@CustomFormatActivity, "\"To\" data value should be less than \"From\" data value", Toast.LENGTH_SHORT).also {
                                    it.show()
                                }
                            }
                        } else {
                            mLayout.pbLoading.visibility = View.INVISIBLE
                            Toast.makeText(this@CustomFormatActivity, "number format \"From\" out of range", Toast.LENGTH_SHORT).also {
                                it.show()
                            }
                        }
                    } else {
                        mLayout.pbLoading.visibility = View.INVISIBLE
                        Toast.makeText(this@CustomFormatActivity, "number format \"To\" out of range", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                } else {
                    mLayout.pbLoading.visibility = View.INVISIBLE
                    Toast.makeText(this@CustomFormatActivity, "empty \"From\" range input data", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                mLayout.pbLoading.visibility = View.INVISIBLE
                Toast.makeText(this@CustomFormatActivity, "empty \"To\" range input data", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        }

        mLayout.tvBack.setOnClickListener {
            Intent(this@CustomFormatActivity, MainActivity::class.java).apply {
                startActivity(this)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finishAffinity()
                finish()
            }
        }
    }

    private fun processed(toRange: Int?, fromRange: Int?, inputData: String) {
        if (toRange != null && fromRange != null) {
            if (recyclerAdapterDataList == null) {
                recyclerAdapterDataList = ArrayList<CustomNumber>()

                for (base: Int in toRange..fromRange) {
                    recyclerAdapterDataList!!.add(CustomNumber(
                        "Format $base",
                        mNumberConverter?.decimalToOtherNumberFormat(
                            inputData, base, isFractionActive
                        ).toString()
                    ))
                }
                mLayout.pbLoading.visibility = View.INVISIBLE
                initializeRecyclerData(recyclerAdapterDataList!!)
            } else {
                recyclerAdapterDataList?.clear()

                for (base: Int in toRange..fromRange) {
                    recyclerAdapterDataList!!.add(CustomNumber(
                        "BaseFormat $base",
                        mNumberConverter?.decimalToOtherNumberFormat(
                            inputData, base, isFractionActive
                        ).toString()
                    ))
                }
                mLayout.pbLoading.visibility = View.INVISIBLE
                initializeRecyclerData(recyclerAdapterDataList!!)
            }


        } else {
            mLayout.pbLoading.visibility = View.INVISIBLE
            Toast.makeText(this@CustomFormatActivity, "something wrong", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }


    }

    private fun initializeRecyclerData(dataList: ArrayList<CustomNumber>) {
        if (recyclerAdapter == null) {
            recyclerAdapter = NumberRecyclerAdapter(dataList, this@CustomFormatActivity)
            mLayout.recycler.layoutManager = LinearLayoutManager(this)
            mLayout.recycler.adapter = recyclerAdapter
        } else {
            recyclerAdapter!!.onRecyclerDataRefresh(dataList)
        }

    }

    override fun onItemClickedListener(item: CustomNumber, position: Int, isCopy: Boolean) {
        if (isCopy) {
            ClipData.newPlainText(item.title, item.data).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@CustomFormatActivity, "${item.title.lowercase(Locale.getDefault())} data copy to clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }
    }
}