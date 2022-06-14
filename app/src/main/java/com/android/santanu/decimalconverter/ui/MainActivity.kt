package com.android.santanu.decimalconverter.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.android.santanu.decimalconverter.R
import com.android.santanu.decimalconverter.data.Converter
import com.android.santanu.decimalconverter.databinding.ActivityMainBinding
import com.android.santanu.decimalconverter.ui.base.BaseActivity
import com.android.santanu.decimalconverter.vm.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess


class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val TAG: String = MainActivity::class.java.simpleName

    private var formatData : String? = null

    private var decimalData : String = ""
    private var binaryData : String = ""
    private var octalData : String = ""
    private var hexadecimalData : String = ""

    private var inputNewData : String = ""
    private var toNewData : String = ""
    private var formNewData : String = ""

    private var mConverter: Converter? = null

    private lateinit var mLayout: ActivityMainBinding
    private val mMainViewModel: MainViewModel by viewModels<MainViewModel>()

    private val formateList: ArrayList<String> = ArrayList<String>().apply {
        this.add("Decimal")
        this.add("Binary")
        this.add("Octal")
        this.add("Hexadecimal")
    }

    private var isActive: Boolean = false

    private lateinit var clipboard : ClipboardManager

    override fun getLayoutId(): Int = R.layout.activity_main


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        mLayout = getViewDataBinding().apply {
            this.lifecycleOwner = this@MainActivity
        }
        onRefresh()
        initSpinner()

        if(mConverter == null) {
            mConverter = Converter()
        }

        clipboard  = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager


        /*
        mMainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(
            application
        ).create(MainViewModel::class.java)
        */

        mLayout.baseTextView.doOnTextChanged { text, _, _, _ ->
            formatData = text.toString()
            this.onRefresh()
        }

        mLayout.tvBaseTo.doOnTextChanged { text, _, _, _ ->
            toNewData = text.toString()
            this.onNewRefresh()
        }

        mLayout.tvBaseForm.doOnTextChanged { text, _, _, _ ->
            formNewData = text.toString()
            this.onNewRefresh()
        }

        mLayout.inputData.doOnTextChanged { _, _, _, _ ->
            /*Toast.makeText(this@MainActivity, "$text apply", Toast.LENGTH_SHORT).also {
                it.show()
            }*/
        }

        mLayout.swActive.setOnCheckedChangeListener { _, isChecked ->
            isActive = isChecked
            if (isChecked) {
                this.onRefresh()
                mLayout.tvActiveSegmentTextStatus.text = "Enable"
                mLayout.tvActiveSegmentTextStatus.setTextColor(
                    ContextCompat.getColor(this@MainActivity, R.color.green)
                )
                mLayout.tvBaseTo.isEnabled = true
                mLayout.tvBaseForm.isEnabled = true
                mLayout.inputNewData.isEnabled = true
            } else {
                mLayout.tvActiveSegmentTextStatus.text = "Disable"
                mLayout.tvActiveSegmentTextStatus.setTextColor(
                    ContextCompat.getColor(this@MainActivity, R.color.gray)
                )
                mLayout.tvBaseTo.isEnabled = false
                mLayout.tvBaseForm.isEnabled = false
                mLayout.inputNewData.isEnabled = false
            }
        }

        mLayout.convertButton.setOnClickListener {
            // formatData = mLayout.baseTextView.text.toString() ?: null

            if (isActive) {
                newProceed()
            } else {
                if (formatData != null && formatData!!.isNotEmpty()) {
                    if (formateList.contains(formatData)) {
                        proceed()
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

        mLayout.ivCopyDecimal.setOnClickListener {
            ClipData.newPlainText("decimal text", mLayout.tvTextDataDecimal.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Decimal Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyBinary.setOnClickListener {
            ClipData.newPlainText("binary text", mLayout.tvTextDataBinary.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Binary Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyOctal.setOnClickListener {
            ClipData.newPlainText("octal text", mLayout.tvTextDataOctal.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Octal Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

        mLayout.ivCopyOctal.setOnClickListener {
            ClipData.newPlainText("hexadecimal text", mLayout.tvTextDataHexadecimal.text.toString()).also {
                clipboard.setPrimaryClip(it)
            }
            Toast.makeText(this@MainActivity, "Hexadecimal Copy to Clipboard", Toast.LENGTH_SHORT).also {
                it.show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mConverter = null
    }

    private fun proceed() {
        if (formatData == "Decimal") {
            decimalData = mLayout.inputData.text.toString()
            if (decimalData.isNotEmpty()) {
                if(mConverter!!.checkDecimalNumberFormat(decimalData)) {
                    binaryData = mConverter!!.convertDecimalToBinary(decimalData.toLong()).toString()
                    octalData = mConverter!!.convertDecimalToOctal(decimalData.toLong()).toString()
                    hexadecimalData = mConverter!!.convertDecimalToHexadecimal(decimalData.toLong())

                    updateLayoutData(formatData!!)
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
        } else if((formatData == "Binary")) {
            binaryData = mLayout.inputData.text.toString()
            if (binaryData.isNotEmpty()) {
                if(mConverter!!.checkBinaryNumberFormat(binaryData)) {
                    decimalData = mConverter!!.convertBinaryToDecimal(binaryData.toLong()).toString()
                    octalData = mConverter!!.convertBinaryToOctal(binaryData.toLong()).toString()
                    hexadecimalData = mConverter!!.convertBinaryToHexadecimal(binaryData.toLong())

                    updateLayoutData(formatData!!)
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
        } else if((formatData == "Octal")) {
            octalData = mLayout.inputData.text.toString()
            if (octalData.isNotEmpty()) {
                if(mConverter!!.checkOctalNumberFormat(octalData)) {
                    decimalData = mConverter!!.convertOctalToDecimal(octalData.toLong()).toString()
                    binaryData = mConverter!!.convertOctalToBinary(octalData.toLong()).toString()
                    hexadecimalData = mConverter!!.convertOctalToHexadecimal(octalData.toLong())

                    updateLayoutData(formatData!!)
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
        } else if((formatData == "Hexadecimal")) {
            hexadecimalData = mLayout.inputData.text.toString()
            if (hexadecimalData.isNotEmpty()) {
                if(mConverter!!.checkHexadecimalNumberFormat(hexadecimalData)) {
                    decimalData = mConverter!!.convertHexadecimalToDecimal(hexadecimalData).toString()
                    binaryData = mConverter!!.convertHexadecimalToBinary(hexadecimalData).toString()
                    octalData = mConverter!!.convertHexadecimalToOctal(hexadecimalData).toString()

                    updateLayoutData(formatData!!)
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

    private fun newProceed() {
        inputNewData = mLayout.inputNewData.text.toString()
        // toNewData = mLayout.tvBaseTo.text.toString()
        // formNewData = mLayout.tvBaseForm.text.toString()

        if (toNewData == "Decimal") {
            if (formNewData == "Decimal") {
                Toast.makeText(this@MainActivity, "Both selection are same, select different", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else if(formNewData == "Binary") {
                if(mConverter!!.checkDecimalNumberFormat(inputNewData)) {
                    mLayout.constraintLayoutNew.visibility = View.VISIBLE
                    mLayout.tvTextLevelData.text = formNewData
                    mLayout.tvTextData.text = mConverter!!.convertDecimalToBinary(
                        inputNewData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Decimal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(formNewData == "Octal") {
                if(mConverter!!.checkDecimalNumberFormat(inputNewData)) {
                    mLayout.constraintLayoutNew.visibility = View.VISIBLE
                    mLayout.tvTextLevelData.text = formNewData
                    mLayout.tvTextData.text = mConverter!!.convertDecimalToOctal(
                        inputNewData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Decimal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(formNewData == "Hexadecimal") {
                if(mConverter!!.checkDecimalNumberFormat(inputNewData)) {
                    mLayout.constraintLayoutNew.visibility = View.VISIBLE
                    mLayout.tvTextLevelData.text = formNewData
                    mLayout.tvTextData.text = mConverter!!.convertDecimalToHexadecimal(
                        inputNewData.toLong()
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
        } else if(toNewData == "Binary") {
            if (formNewData == "Decimal") {
                if(mConverter!!.checkBinaryNumberFormat(inputNewData)) {
                    mLayout.constraintLayoutNew.visibility = View.VISIBLE
                    mLayout.tvTextLevelData.text = formNewData
                    mLayout.tvTextData.text = mConverter!!.convertBinaryToDecimal(
                        inputNewData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Binary Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(formNewData == "Binary") {
                Toast.makeText(this@MainActivity, "Both selection are same, select different", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else if(formNewData == "Octal") {
                if(mConverter!!.checkBinaryNumberFormat(inputNewData)) {
                    mLayout.constraintLayoutNew.visibility = View.VISIBLE
                    mLayout.tvTextLevelData.text = formNewData
                    mLayout.tvTextData.text = mConverter!!.convertBinaryToOctal(
                        inputNewData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Binary Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(formNewData == "Hexadecimal") {
                if(mConverter!!.checkBinaryNumberFormat(inputNewData)) {
                    mLayout.constraintLayoutNew.visibility = View.VISIBLE
                    mLayout.tvTextLevelData.text = formNewData
                    mLayout.tvTextData.text = mConverter!!.convertBinaryToHexadecimal(
                        inputNewData.toLong()
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
        } else if(toNewData == "Octal") {
            if (formNewData == "Decimal") {
                if(mConverter!!.checkOctalNumberFormat(inputNewData)) {
                    mLayout.constraintLayoutNew.visibility = View.VISIBLE
                    mLayout.tvTextLevelData.text = formNewData
                    mLayout.tvTextData.text = mConverter!!.convertOctalToDecimal(
                        inputNewData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Octal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(formNewData == "Binary") {
                if(mConverter!!.checkOctalNumberFormat(inputNewData)) {
                    mLayout.constraintLayoutNew.visibility = View.VISIBLE
                    mLayout.tvTextLevelData.text = formNewData
                    mLayout.tvTextData.text = mConverter!!.convertOctalToBinary(
                        inputNewData.toLong()
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Octal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(formNewData == "Octal") {
                Toast.makeText(this@MainActivity, "Both selection are same, select different", Toast.LENGTH_SHORT).also {
                    it.show()
                }
            } else if(formNewData == "Hexadecimal") {
                if(mConverter!!.checkOctalNumberFormat(inputNewData)) {
                    mLayout.constraintLayoutNew.visibility = View.VISIBLE
                    mLayout.tvTextLevelData.text = formNewData
                    mLayout.tvTextData.text = mConverter!!.convertOctalToHexadecimal(
                        inputNewData.toLong()
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
        } else if(toNewData == "Hexadecimal") {
            if (formNewData == "Decimal") {
                if(mConverter!!.checkHexadecimalNumberFormat(inputNewData)) {
                    mLayout.constraintLayoutNew.visibility = View.VISIBLE
                    mLayout.tvTextLevelData.text = formNewData
                    mLayout.tvTextData.text = mConverter!!.convertHexadecimalToDecimal(
                        inputNewData
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Hexadecimal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(formNewData == "Binary") {
                if(mConverter!!.checkHexadecimalNumberFormat(inputNewData)) {
                    mLayout.constraintLayoutNew.visibility = View.VISIBLE
                    mLayout.tvTextLevelData.text = formNewData
                    mLayout.tvTextData.text = mConverter!!.convertHexadecimalToBinary(
                        inputNewData
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Hexadecimal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(formNewData == "Octal") {
                if(mConverter!!.checkHexadecimalNumberFormat(inputNewData)) {
                    mLayout.constraintLayoutNew.visibility = View.VISIBLE
                    mLayout.tvTextLevelData.text = formNewData
                    mLayout.tvTextData.text = mConverter!!.convertHexadecimalToOctal(
                        inputNewData
                    )
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect Hexadecimal Data Formate", Toast.LENGTH_SHORT).also {
                        it.show()
                    }
                }
            } else if(formNewData == "Hexadecimal") {
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

    private fun updateLayoutData(formatData: String) {

        mLayout.tvTextDataDecimal.text = decimalData
        mLayout.tvTextDataBinary.text = binaryData
        mLayout.tvTextDataOctal.text = octalData
        mLayout.tvTextDataHexadecimal.text = hexadecimalData

        mLayout.constraintLayoutDecimal.visibility = View.VISIBLE
        mLayout.constraintLayoutBinary.visibility = View.VISIBLE
        mLayout.constraintLayoutOctal.visibility = View.VISIBLE
        mLayout.constraintLayoutHexadecimal.visibility = View.VISIBLE

        if (formatData == "Decimal") {
            // mLayout.constraintLayoutDecimal.visibility = View.GONE
            // mLayout.tvTextDataDecimal.visibility = View.INVISIBLE
        } else if((formatData == "Binary")) {
            // mLayout.constraintLayoutBinary.visibility = View.GONE
            // mLayout.tvTextDataBinary.visibility = View.INVISIBLE
        } else if((formatData == "Octal")) {
            // mLayout.constraintLayoutOctal.visibility = View.GONE
            // mLayout.tvTextDataOctal.visibility = View.INVISIBLE
        } else if((formatData == "Hexadecimal")) {
            // mLayout.constraintLayoutHexadecimal.visibility = View.GONE
            // mLayout.tvTextDataHexadecimal.visibility = View.INVISIBLE
        }else {
            Toast.makeText(this@MainActivity, "Something Went Wrong, Restart Application", Toast.LENGTH_LONG).also {
                it.show()
            }

            mLayout.constraintLayoutDecimal.visibility = View.GONE
            mLayout.constraintLayoutBinary.visibility = View.GONE
            mLayout.constraintLayoutOctal.visibility = View.GONE
            mLayout.constraintLayoutHexadecimal.visibility = View.GONE
        }
    }

    private fun onRefresh() {
        mLayout.inputData.setText("")
        mLayout.tvTextDataDecimal.text = ""
        mLayout.tvTextDataBinary.text = ""
        mLayout.tvTextDataOctal.text = ""
        mLayout.tvTextDataHexadecimal.text = ""

        decimalData = ""
        binaryData = ""
        octalData = ""
        hexadecimalData = ""

        mLayout.constraintLayoutDecimal.visibility = View.INVISIBLE
        mLayout.constraintLayoutBinary.visibility = View.INVISIBLE
        mLayout.constraintLayoutOctal.visibility = View.INVISIBLE
        mLayout.constraintLayoutHexadecimal.visibility = View.INVISIBLE
    }

    private fun onNewRefresh() {
        mLayout.inputNewData.setText("")

        inputNewData = ""

        mLayout.constraintLayoutNew.visibility = View.INVISIBLE
    }

    private fun initSpinner() {
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@MainActivity,
            android.R.layout.simple_spinner_dropdown_item,
            formateList
        )

        mLayout.baseTextView.setAdapter(spinnerAdapter)
        mLayout.tvBaseTo.setAdapter(spinnerAdapter)
        mLayout.tvBaseForm.setAdapter(spinnerAdapter)
    }


}