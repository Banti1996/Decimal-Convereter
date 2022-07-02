package com.android.santanu.decimalconverter.data.logic

import kotlin.math.pow
import kotlin.math.roundToInt

class GeneralNumberConverter {
    private val TAG: String = GeneralNumberConverter::class.java.simpleName

    fun decimalToOtherNumberFormat(
        decimalData: String,
        convertFormat: Int,
        isFraction: Boolean = false,
        pointLimit: Int = 10
    ) : String {
        val baseFormat: Int = convertFormat
        val splitData: List<String> = decimalData.split(".")

        val integralPart: String
        val fractionPart: String

        val integralResult: StringBuilder = StringBuilder()
        val fractionResult: StringBuilder = StringBuilder()

        if (splitData.size == 1) {
            /** given decimal data is only integer, there is no fraction  part... **/

            /** integral part converter into => binary **/
            integralPart = splitData[0] /** integral data **/
            var decimal: Long = integralPart.toLong()
            var reminder: Int
            while(decimal > (baseFormat-1)) {
                reminder = (decimal % baseFormat).toInt()
                decimal /= baseFormat
                if (reminder < 10) {
                    integralResult.append(reminder.toString())
                } else {
                    integralResult.append(
                        NumberFormatUtils.numberExchanger(
                            reminder.toString(), numToChar = true
                        )
                    )
                }
            }
            if (decimal < 10) {
                integralResult.append(decimal.toString())
            } else {
                integralResult.append(
                    NumberFormatUtils.numberExchanger(
                        decimal.toString(), numToChar = true
                    )
                )
            }
            val revData: String = integralResult.toString().reversed()
            integralResult.clear()
            integralResult.append(revData) /** this is contain binary data [integral part] **/

            return integralResult.toString()
        } else {
            /** given decimal data is fraction and integer both **/
            integralPart = splitData[0] /** integral data **/
            fractionPart = splitData[1] /** fraction data **/

            /** integral part converter into => binary **/
            var decimal: Long = integralPart.toLong()
            var reminder: Int
            while(decimal > (baseFormat-1)) {
                reminder = (decimal % baseFormat).toInt()
                decimal /= baseFormat
                if (reminder < 10) {
                    integralResult.append(reminder.toString())
                } else {
                    integralResult.append(
                        NumberFormatUtils.numberExchanger(
                            reminder.toString(), numToChar = true
                        )
                    )
                }
            }
            if (decimal < 10) {
                integralResult.append(decimal.toString())
            } else {
                integralResult.append(
                    NumberFormatUtils.numberExchanger(
                        decimal.toString(), numToChar = true
                    )
                )
            }
            val revData: String = integralResult.toString().reversed()
            integralResult.clear()
            integralResult.append(revData) /** this is contain binary data [integral part] **/


            /** fraction part converter into => binary **/
            var fData: Double = decimalData.toDouble() - integralPart.toInt()
            // var fData: Double = "0.$fractionPart".toDouble()
            var iData: Int

            for(i in 0 until pointLimit){
                fData *= baseFormat
                iData = fData.toInt()
                fData -=  iData

                if (iData < 10) {
                    fractionResult.append(iData.toString())
                } else {
                    fractionResult.append(
                        NumberFormatUtils.numberExchanger(
                            iData.toString(), numToChar = true
                        )
                    )
                }
            }

            val result: StringBuilder = StringBuilder()
            result.append(integralResult)
            result.append(".")
            result.append(fractionResult)

            return result.toString()
        }
    }

    fun otherToDecimalNumberFormat(
        otherData: String,
        convertFormat: Int,
        isFraction: Boolean = false,
        pointLimit: Int = 10
    ) : String {
        val baseFormat: Int = convertFormat
        val splitData: List<String> = otherData.split(".")

        val integralPart: String
        val fractionPart: String

        val integralResult: StringBuilder = StringBuilder()
        val fractionResult: StringBuilder = StringBuilder()

        if (splitData.size == 1) {
            /** given decimal data is only integer, there is no fraction  part... **/
            /** integral part converter into => decimal **/
            integralPart = splitData[0].reversed() /** integral data **/
            var integralPartResult: Long = 0
            for (i in integralPart.indices) {
                val exeData: String = NumberFormatUtils.numberExchanger(integralPart[i].toString(), false)
                integralPartResult += (baseFormat.toDouble().pow(i.toDouble()).toLong() * exeData.toLong())
            }

            integralResult.append(integralPartResult.toString())

            return integralResult.toString()
        } else {
            /** given decimal data is fraction and integer both **/
            integralPart = splitData[0].reversed() /** integral data **/
            fractionPart = splitData[1] /** fraction data **/

            /** integral part converter into => decimal **/
            var integralPartResult: Long = 0
            for (i in integralPart.indices) {
                val exeData: String = NumberFormatUtils.numberExchanger(integralPart[i].toString(), false)
                integralPartResult += (baseFormat.toDouble().pow(i.toDouble()).toLong() * exeData.toLong())
            }
            integralResult.append(integralPartResult.toString())

            /** fraction part converter into => decimal **/
            var fractionPartResult: Double = 0.0
            for (i in fractionPart.indices) {
                val exeData: String = NumberFormatUtils.numberExchanger(fractionPart[i].toString(), false)
                fractionPartResult += (exeData.toDouble() * (1/(baseFormat.toDouble().pow((i+1).toDouble()))))
            }
            fractionResult.append(fractionPartResult.toString())


            /** add little bit of noise[only for binary] to round off value **/
            val fractionRoundOff: Double = if (baseFormat == NumberFormatUtils.BINARY_FORMAT) {
                ((fractionPartResult * 1000.0).roundToInt() / 1000.0) + 0.001
            } else {
                ((fractionPartResult * 1000.0).roundToInt() / 1000.0)
            }

            val result = integralPartResult + fractionRoundOff

            return result.toString()
        }

    }
}