package com.android.santanu.decimalconverter.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.doOnTextChanged
import com.android.santanu.decimalconverter.R
import com.android.santanu.decimalconverter.data.Converter
import com.android.santanu.decimalconverter.databinding.ActivityMainBinding
import com.android.santanu.decimalconverter.ui.base.BaseActivity
import com.android.santanu.decimalconverter.vm.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlin.system.exitProcess


class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val TAG: String = MainActivity::class.java.simpleName


    private lateinit var mLayout: ActivityMainBinding
    private val mMainViewModel: MainViewModel by viewModels<MainViewModel>()

    private val formateList: ArrayList<String> = ArrayList<String>().apply {
        this.add("Decimal")
        this.add("Binary")
        this.add("Octal")
        this.add("Hexadecimal")
    }


    private var upperFormatData : String? = null

    private var upperSegmentDecimalData : String = ""
    private var upperSegmentBinaryData : String = ""
    private var upperSegmentOctalData : String = ""
    private var upperSegmentHexadecimalData : String = ""

    private var lowerSegmentInputData : String = ""
    private var lowerToFormatData : String = ""
    private var lowerFormFormatData : String = ""

    private var isActive: Boolean = false

    private var mConverter: Converter? = null

    private lateinit var clipboard : ClipboardManager

    override fun getLayoutId(): Int = R.layout.activity_main


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(
                this@MainActivity, R.color.teal_200
            )
        }

        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        mLayout = getViewDataBinding().apply {
            this.lifecycleOwner = this@MainActivity
        }
        this.onUpperSegmentRefresh()
        this.onLowerSegmentRefresh()
        this.initializeFirstTime()
        this.initializeSpinner()

        if(mConverter == null) {
            mConverter = Converter()
        }

        clipboard  = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        mLayout.upperSpinner.showSoftInputOnFocus = false
        mLayout.lowerToSpinner.showSoftInputOnFocus = false
        mLayout.lowerFormSpinner.showSoftInputOnFocus = false


        /*
        mMainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
            application
        ).create(MainViewModel::class.java)
        */

        mLayout.upperSpinner.doOnTextChanged { text, _, _, _ ->
            upperFormatData = text.toString()
            this.onUpperSegmentRefresh()
        }

        mLayout.lowerToSpinner.doOnTextChanged { text, _, _, _ ->
            lowerToFormatData = text.toString()
            this.onLowerSegmentRefresh()
        }

        mLayout.lowerFormSpinner.doOnTextChanged { text, _, _, _ ->
            lowerFormFormatData = text.toString()
            this.onLowerSegmentRefresh()
        }

        mLayout.upperSegmentInputData.doOnTextChanged { text, _, _, _ ->
            if (!isActive) {
                if (upperFormatData != null && upperFormatData!!.isNotEmpty()) {
                    when (upperFormatData) {
                        "Decimal" -> {
                            if(mConverter!!.checkDecimalNumberFormat(text.toString())) {
                                // mLayout.upperSegmentInputLayout.helperText = null
                                mLayout.upperSegmentInputLayout.error = null

                            } else {
                                // mLayout.upperSegmentInputLayout.helperText = "Incorrect $upperFormatData Format"
                                mLayout.upperSegmentInputLayout.error = "Incorrect $upperFormatData Format"

                            }
                        }
                        "Binary" -> {
                            if(mConverter!!.checkDecimalNumberFormat(text.toString())) {
                                // mLayout.upperSegmentInputLayout.helperText = null
                                mLayout.upperSegmentInputLayout.error = null

                            } else {
                                // mLayout.upperSegmentInputLayout.helperText = "Incorrect $upperFormatData Format"
                                mLayout.upperSegmentInputLayout.error = "Incorrect $upperFormatData Format"

                            }
                        }
                        "Octal" -> {
                            if(mConverter!!.checkDecimalNumberFormat(text.toString())) {
                                // mLayout.upperSegmentInputLayout.helperText = null
                                mLayout.upperSegmentInputLayout.error = null

                            } else {
                                // mLayout.upperSegmentInputLayout.helperText = "Incorrect $upperFormatData Format"
                                mLayout.upperSegmentInputLayout.error = "Incorrect $upperFormatData Format"

                            }
                        }
                        "Hexadecimal" -> {
                            if(mConverter!!.checkDecimalNumberFormat(text.toString())) {
                                // mLayout.upperSegmentInputLayout.helperText = null
                                mLayout.upperSegmentInputLayout.error = null

                            } else {
                                // mLayout.upperSegmentInputLayout.helperText = "Incorrect $upperFormatData Format"
                                mLayout.upperSegmentInputLayout.error = "Incorrect $upperFormatData Format"

                            }
                        }
                        else -> {
                            Toast.makeText(this@MainActivity, "Something Went Wrong", Toast.LENGTH_SHORT).also {
                                it.show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Select Base Format First", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            }
        }

        mLayout.lowerSegmentInputData.doOnTextChanged { text, _, _, _ ->
            if (isActive) {
                if (lowerToFormatData.isNotEmpty() && lowerFormFormatData.isNotEmpty()) {
                    when (lowerToFormatData) {
                        "Decimal" -> {
                            if(mConverter!!.checkDecimalNumberFormat(text.toString())) {
                                // mLayout.lowerSegmentInputLayout.helperText = null
                                mLayout.lowerSegmentInputLayout.error = null

                            } else {
                                // mLayout.lowerSegmentInputLayout.helperText = "Incorrect $lowerToFormatData Format"
                                mLayout.lowerSegmentInputLayout.error = "Incorrect $lowerToFormatData Format"

                            }
                        }
                        "Binary" -> {
                            if(mConverter!!.checkDecimalNumberFormat(text.toString())) {
                                // mLayout.lowerSegmentInputLayout.helperText = null
                                mLayout.lowerSegmentInputLayout.error = null

                            } else {
                                // mLayout.lowerSegmentInputLayout.helperText = "Incorrect $lowerToFormatData Format"
                                mLayout.lowerSegmentInputLayout.error = "Incorrect $lowerToFormatData Format"

                            }
                        }
                        "Octal" -> {
                            if(mConverter!!.checkDecimalNumberFormat(text.toString())) {
                                // mLayout.lowerSegmentInputLayout.helperText = null
                                mLayout.lowerSegmentInputLayout.error = null

                            } else {
                                // mLayout.lowerSegmentInputLayout.helperText = "Incorrect $lowerToFormatData Format"
                                mLayout.lowerSegmentInputLayout.error = "Incorrect $lowerToFormatData Format"

                            }
                        }
                        "Hexadecimal" -> {
                            if(mConverter!!.checkDecimalNumberFormat(text.toString())) {
                                // mLayout.lowerSegmentInputLayout.helperText = null
                                mLayout.lowerSegmentInputLayout.error = null

                            } else {
                                // mLayout.lowerSegmentInputLayout.helperText = "Incorrect $lowerToFormatData Format"
                                mLayout.lowerSegmentInputLayout.error = "Incorrect $lowerToFormatData Format"

                            }
                        }
                        else -> {
                            Toast.makeText(this@MainActivity, "Something Went Wrong", Toast.LENGTH_SHORT).also {
                                it.show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Select Base Format First", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            }
        }

        mLayout.swActive.setOnCheckedChangeListener { _, isChecked ->
            isActive = isChecked
            if (isChecked) {
                this.onUpperSegmentRefresh()
                mLayout.tvLowerSegmentTextStatus.text = "Enable"
                mLayout.tvLowerSegmentTextStatus.setTextColor(
                    ContextCompat.getColor(this@MainActivity, R.color.green)
                )
                mLayout.tvUpperSegmentTextStatus.text = "Disable"
                mLayout.tvUpperSegmentTextStatus.setTextColor(
                    ContextCompat.getColor(this@MainActivity, R.color.gray)
                )

                mLayout.upperSegmentSpinnerLayout.isClickable = false
                mLayout.upperSegmentSpinnerLayout.isEnabled = false
                mLayout.upperSegmentInputLayout.isClickable = false
                mLayout.upperSegmentInputLayout.isEnabled = false

                mLayout.lowerSegmentInputLayout.isClickable = true
                mLayout.lowerSegmentInputLayout.isEnabled = true
                mLayout.toLowerToSpinnerLayout.isClickable = true
                mLayout.toLowerToSpinnerLayout.isEnabled = true
                mLayout.toLowerFormSpinnerLayout.isClickable = true
                mLayout.toLowerFormSpinnerLayout.isEnabled = true

                mLayout.upperSpinner.isEnabled = false
                mLayout.lowerToSpinner.isEnabled = true
                mLayout.lowerFormSpinner.isEnabled = true
                mLayout.lowerToSpinner.isClickable = true
                mLayout.lowerFormSpinner.isClickable = true

                mLayout.lowerSegmentInputData.isEnabled = true

                mLayout.upperSegmentSpinnerLayout.isClickable = false
                mLayout.upperSegmentInputData.isClickable = false
                mLayout.upperSegmentInputData.isEnabled = false
                mLayout.upperSegmentInputData.isEnabled = false
            } else {
                this.onLowerSegmentRefresh()
                mLayout.tvLowerSegmentTextStatus.text = "Disable"
                mLayout.tvLowerSegmentTextStatus.setTextColor(
                    ContextCompat.getColor(this@MainActivity, R.color.gray)
                )
                mLayout.tvUpperSegmentTextStatus.text = "Enable"
                mLayout.tvUpperSegmentTextStatus.setTextColor(
                    ContextCompat.getColor(this@MainActivity, R.color.green)
                )

                mLayout.upperSpinner.isEnabled = true
                mLayout.upperSpinner.isEnabled = true
                mLayout.lowerToSpinner.isEnabled = false
                mLayout.lowerFormSpinner.isEnabled = false
                mLayout.lowerToSpinner.isClickable = false
                mLayout.lowerFormSpinner.isClickable = false
                mLayout.lowerSegmentInputData.isEnabled = false

                mLayout.upperSegmentInputData.isClickable = true
                mLayout.upperSegmentInputData.isEnabled = true
                mLayout.upperSegmentInputData.isEnabled = true


                mLayout.upperSegmentSpinnerLayout.isClickable = true
                mLayout.upperSegmentSpinnerLayout.isEnabled = true
                mLayout.upperSegmentInputLayout.isClickable = true
                mLayout.upperSegmentInputLayout.isEnabled = true

                mLayout.lowerSegmentInputLayout.isClickable = false
                mLayout.lowerSegmentInputLayout.isEnabled = false
                mLayout.toLowerToSpinnerLayout.isClickable = false
                mLayout.toLowerToSpinnerLayout.isEnabled = false
                mLayout.toLowerFormSpinnerLayout.isClickable = false
                mLayout.toLowerFormSpinnerLayout.isEnabled = false

            }
        }

        mLayout.convertButton.setOnClickListener {

            if (isActive) {
                lowerSegmentInputData = mLayout.lowerSegmentInputData.text.toString()
                if (lowerSegmentInputData.isNotEmpty()) {
                    lowerSegmentDataProceed()
                } else {
                    Toast.makeText(this@MainActivity, "please enter $lowerToFormatData your value", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                if (upperFormatData != null && upperFormatData!!.isNotEmpty()) {
                    if (formateList.contains(upperFormatData)) {
                        upperSegmentDataProceed()
                    } else {
                        Toast.makeText(this@MainActivity, "Please Select Number Format From Dropdown List", Toast.LENGTH_SHORT).also {
                            it.show()
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Please Select Number Format", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            }
        }

        mLayout.ivCopyDecimalUpperData.setOnClickListener {
            ClipData.newPlainText("decimal text", mLayout.tvDecimalUpperOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Decimal Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyBinaryUpperData.setOnClickListener {
            ClipData.newPlainText("binary text", mLayout.tvBinaryUpperOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Binary Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyOctalUpperData.setOnClickListener {
            ClipData.newPlainText("octal text", mLayout.tvOctalUpperOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Octal Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyHexadecimalUpperData.setOnClickListener {
            ClipData.newPlainText("hexadecimal text", mLayout.tvHexadecimalUpperOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Hexadecimal Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyLowerOutput.setOnClickListener {
            ClipData.newPlainText("${mLayout.tvLowerOutputDataText.text.toString()} text", mLayout.tvLowerOutputData.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "${mLayout.tvLowerOutputDataText.text.toString().replace(":", "")}Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mConverter = null
    }

    private fun upperSegmentDataProceed() {
        if (upperFormatData == "Decimal") {
            upperSegmentDecimalData = mLayout.upperSegmentInputData.text.toString()
            if (upperSegmentDecimalData.isNotEmpty()) {
                if(mConverter!!.checkDecimalNumberFormat(upperSegmentDecimalData)) {
                    upperSegmentBinaryData = mConverter!!.convertDecimalToBinary(upperSegmentDecimalData.toLong()).toString()
                    upperSegmentOctalData = mConverter!!.convertDecimalToOctal(upperSegmentDecimalData.toLong()).toString()
                    upperSegmentHexadecimalData = mConverter!!.convertDecimalToHexadecimal(upperSegmentDecimalData.toLong())

                    updateUpperSegmentLayoutData(upperFormatData!!)
                } else {
                    Toast.makeText(this@MainActivity, "Decimal Data Format Incorrect", Toast.LENGTH_LONG).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Decimal Data Field Empty", Toast.LENGTH_LONG).also {
                    it.show()
                }
            }
        } else if((upperFormatData == "Binary")) {
            upperSegmentBinaryData = mLayout.upperSegmentInputData.text.toString()
            if (upperSegmentBinaryData.isNotEmpty()) {
                if(mConverter!!.checkBinaryNumberFormat(upperSegmentBinaryData)) {
                    upperSegmentDecimalData = mConverter!!.convertBinaryToDecimal(upperSegmentBinaryData.toLong()).toString()
                    upperSegmentOctalData = mConverter!!.convertBinaryToOctal(upperSegmentBinaryData.toLong()).toString()
                    upperSegmentHexadecimalData = mConverter!!.convertBinaryToHexadecimal(upperSegmentBinaryData.toLong())

                    updateUpperSegmentLayoutData(upperFormatData!!)
                } else {
                    Toast.makeText(this@MainActivity, "Binary Data Format Incorrect", Toast.LENGTH_LONG).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Binary Data Field Empty", Toast.LENGTH_LONG).also {
                    it.show()
                }
            }
        } else if((upperFormatData == "Octal")) {
            upperSegmentOctalData = mLayout.upperSegmentInputData.text.toString()
            if (upperSegmentOctalData.isNotEmpty()) {
                if(mConverter!!.checkOctalNumberFormat(upperSegmentOctalData)) {
                    upperSegmentDecimalData = mConverter!!.convertOctalToDecimal(upperSegmentOctalData.toLong()).toString()
                    upperSegmentBinaryData = mConverter!!.convertOctalToBinary(upperSegmentOctalData.toLong()).toString()
                    upperSegmentHexadecimalData = mConverter!!.convertOctalToHexadecimal(upperSegmentOctalData.toLong())

                    updateUpperSegmentLayoutData(upperFormatData!!)
                } else {
                    Toast.makeText(this@MainActivity, "Octal Data Format Incorrect", Toast.LENGTH_LONG).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Octal Data Field Empty", Toast.LENGTH_LONG).also {
                    it.show()
                }
            }
        } else if((upperFormatData == "Hexadecimal")) {
            upperSegmentHexadecimalData = mLayout.upperSegmentInputData.text.toString()
            if (upperSegmentHexadecimalData.isNotEmpty()) {
                if(mConverter!!.checkHexadecimalNumberFormat(upperSegmentHexadecimalData)) {
                    upperSegmentDecimalData = mConverter!!.convertHexadecimalToDecimal(upperSegmentHexadecimalData).toString()
                    upperSegmentBinaryData = mConverter!!.convertHexadecimalToBinary(upperSegmentHexadecimalData).toString()
                    upperSegmentOctalData = mConverter!!.convertHexadecimalToOctal(upperSegmentHexadecimalData).toString()

                    updateUpperSegmentLayoutData(upperFormatData!!)
                } else {
                    Toast.makeText(this@MainActivity, "Hexadecimal Data Format Incorrect", Toast.LENGTH_LONG).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Hexadecimal Data Field Empty", Toast.LENGTH_LONG).also {
                    it.show()
                }
            }
        }else {
            Toast.makeText(this@MainActivity, "Something Went Wrong, Restart Application", Toast.LENGTH_LONG).also {
                it.show()
            }
            Snackbar.make(mLayout.constraintLayout,"Something Went Wrong", Snackbar.LENGTH_LONG).also {
                it.setAction("Exit"){
                    Toast.makeText(this@MainActivity,"Restart Application", Toast.LENGTH_SHORT).show()
                    this.finish()
                    exitProcess(0)
                }
                it.show()
            }

        }
    }

    private fun lowerSegmentDataProceed() {

        if (lowerToFormatData == "Decimal") {
            if (lowerFormFormatData == "Decimal") {
                Toast.makeText(this@MainActivity, "Both selection are same, select different", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else if(lowerFormFormatData == "Binary") {
                if(mConverter!!.checkDecimalNumberFormat(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertDecimalToBinary(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Decimal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Octal") {
                if(mConverter!!.checkDecimalNumberFormat(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertDecimalToOctal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Decimal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Hexadecimal") {
                if(mConverter!!.checkDecimalNumberFormat(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertDecimalToHexadecimal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Decimal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "something went wrong", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if(lowerToFormatData == "Binary") {
            if (lowerFormFormatData == "Decimal") {
                if(mConverter!!.checkBinaryNumberFormat(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertBinaryToDecimal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Binary Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Binary") {
                Toast.makeText(this@MainActivity, "Both selection are same, select different", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else if(lowerFormFormatData == "Octal") {
                if(mConverter!!.checkBinaryNumberFormat(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertBinaryToOctal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Binary Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Hexadecimal") {
                if(mConverter!!.checkBinaryNumberFormat(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertBinaryToHexadecimal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Binary Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if(lowerToFormatData == "Octal") {
            if (lowerFormFormatData == "Decimal") {
                if(mConverter!!.checkOctalNumberFormat(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertOctalToDecimal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Octal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Binary") {
                if(mConverter!!.checkOctalNumberFormat(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertOctalToBinary(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Octal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Octal") {
                Toast.makeText(this@MainActivity, "Both selection are same, select different", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else if(lowerFormFormatData == "Hexadecimal") {
                if(mConverter!!.checkOctalNumberFormat(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertOctalToHexadecimal(
                        lowerSegmentInputData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Octal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else if(lowerToFormatData == "Hexadecimal") {
            if (lowerFormFormatData == "Decimal") {
                if(mConverter!!.checkHexadecimalNumberFormat(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertHexadecimalToDecimal(
                        lowerSegmentInputData
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Hexadecimal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Binary") {
                if(mConverter!!.checkHexadecimalNumberFormat(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertHexadecimalToBinary(
                        lowerSegmentInputData
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Hexadecimal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Octal") {
                if(mConverter!!.checkHexadecimalNumberFormat(lowerSegmentInputData)) {
                    mLayout.constraintLayoutLowerOutput.visibility = View.VISIBLE
                    mLayout.tvLowerOutputDataText.text = "$lowerFormFormatData : "
                    mLayout.tvLowerOutputData.text = mConverter!!.convertHexadecimalToOctal(
                        lowerSegmentInputData
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Hexadecimal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(lowerFormFormatData == "Hexadecimal") {
                Toast.makeText(this@MainActivity, "Both selection are same, select different", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else {
                Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            }
        } else {
            Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }


    }

    private fun updateUpperSegmentLayoutData(formatData: String) {

        mLayout.tvDecimalUpperOutputData.text = upperSegmentDecimalData
        mLayout.tvBinaryUpperOutputData.text = upperSegmentBinaryData
        mLayout.tvOctalUpperOutputData.text = upperSegmentOctalData
        mLayout.tvHexadecimalUpperOutputData.text = upperSegmentHexadecimalData

        mLayout.constraintLayoutDecimalUpperOutputText.visibility = View.VISIBLE
        mLayout.constraintLayoutBinaryUpperOutputText.visibility = View.VISIBLE
        mLayout.constraintLayoutOctalUpperOutputText.visibility = View.VISIBLE
        mLayout.constraintLayoutHexadecimalUpperOutputText.visibility = View.VISIBLE

        if (formatData == "Decimal") {
            // mLayout.constraintLayoutDecimalUpperOutputText.visibility = View.GONE
            // mLayout.tvDecimalUpperOutputData.visibility = View.INVISIBLE
        } else if((formatData == "Binary")) {
            // mLayout.constraintLayoutBinaryUpperOutputText.visibility = View.GONE
            // mLayout.tvBinaryUpperOutputData.visibility = View.INVISIBLE
        } else if((formatData == "Octal")) {
            // mLayout.constraintLayoutOctalUpperOutputText.visibility = View.GONE
            // mLayout.tvOctalUpperOutputData.visibility = View.INVISIBLE
        } else if((formatData == "Hexadecimal")) {
            // mLayout.constraintLayoutHexadecimalUpperOutputText.visibility = View.GONE
            // mLayout.tvHexadecimalUpperOutputData.visibility = View.INVISIBLE
        }else {
            Toast.makeText(this@MainActivity, "Something Went Wrong, Restart Application", Toast.LENGTH_LONG).also {
                it.show()
            }

            mLayout.constraintLayoutDecimalUpperOutputText.visibility = View.GONE
            mLayout.constraintLayoutBinaryUpperOutputText.visibility = View.GONE
            mLayout.constraintLayoutOctalUpperOutputText.visibility = View.GONE
            mLayout.constraintLayoutHexadecimalUpperOutputText.visibility = View.GONE
        }
    }

    private fun onUpperSegmentRefresh() {
        mLayout.upperSegmentInputData.setText("")
        mLayout.tvDecimalUpperOutputData.text = ""
        mLayout.tvBinaryUpperOutputData.text = ""
        mLayout.tvOctalUpperOutputData.text = ""
        mLayout.tvHexadecimalUpperOutputData.text = ""

        upperSegmentDecimalData = ""
        upperSegmentBinaryData = ""
        upperSegmentOctalData = ""
        upperSegmentHexadecimalData = ""

        mLayout.constraintLayoutDecimalUpperOutputText.visibility = View.INVISIBLE
        mLayout.constraintLayoutBinaryUpperOutputText.visibility = View.INVISIBLE
        mLayout.constraintLayoutOctalUpperOutputText.visibility = View.INVISIBLE
        mLayout.constraintLayoutHexadecimalUpperOutputText.visibility = View.INVISIBLE

        mLayout.upperSegmentInputLayout.error = null
    }

    private fun onLowerSegmentRefresh() {
        mLayout.lowerSegmentInputData.setText("")
        lowerSegmentInputData = ""
        mLayout.constraintLayoutLowerOutput.visibility = View.INVISIBLE
        mLayout.lowerSegmentInputLayout.error = null
    }

    private fun initializeSpinner() {
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@MainActivity,
            android.R.layout.simple_spinner_dropdown_item,
            formateList
        )

        mLayout.upperSpinner.setAdapter(spinnerAdapter)
        mLayout.lowerToSpinner.setAdapter(spinnerAdapter)
        mLayout.lowerFormSpinner.setAdapter(spinnerAdapter)
    }

    private fun initializeFirstTime() {
        mLayout.tvLowerSegmentTextStatus.text = "Disable"
        mLayout.tvLowerSegmentTextStatus.setTextColor(
            ContextCompat.getColor(this@MainActivity, R.color.gray)
        )
        mLayout.tvUpperSegmentTextStatus.text = "Enable"
        mLayout.tvUpperSegmentTextStatus.setTextColor(
            ContextCompat.getColor(this@MainActivity, R.color.green)
        )

        mLayout.upperSpinner.isEnabled = true
        mLayout.upperSpinner.isEnabled = true
        mLayout.lowerToSpinner.isEnabled = false
        mLayout.lowerFormSpinner.isEnabled = false
        mLayout.lowerToSpinner.isClickable = false
        mLayout.lowerFormSpinner.isClickable = false
        mLayout.lowerSegmentInputData.isEnabled = false

        mLayout.upperSegmentInputData.isClickable = true
        mLayout.upperSegmentInputData.isEnabled = true
        mLayout.upperSegmentInputData.isEnabled = true


        mLayout.upperSegmentSpinnerLayout.isClickable = true
        mLayout.upperSegmentSpinnerLayout.isEnabled = true
        mLayout.upperSegmentInputLayout.isClickable = true
        mLayout.upperSegmentInputLayout.isEnabled = true

        mLayout.lowerSegmentInputLayout.isClickable = false
        mLayout.lowerSegmentInputLayout.isEnabled = false
        mLayout.toLowerToSpinnerLayout.isClickable = false
        mLayout.toLowerToSpinnerLayout.isEnabled = false
        mLayout.toLowerFormSpinnerLayout.isClickable = false
        mLayout.toLowerFormSpinnerLayout.isEnabled = false
    }


}