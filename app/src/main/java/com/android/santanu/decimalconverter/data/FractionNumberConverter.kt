package com.android.santanu.decimalconverter.data

import android.util.Log
import kotlin.math.pow
import kotlin.math.roundToInt

class FractionNumberConverter {
    private val TAG: String = FractionNumberConverter::class.java.simpleName

    fun convertFractionDecimalToBinary(fractionDecimal: Double, fractionRepeatLimit: Int = 5, limitOverride: Boolean = false) : String {
        var limit = fractionRepeatLimit
        val fractionBinary: StringBuilder = StringBuilder()

        var integralPart: Long = fractionDecimal.toLong()
        var fractionalPart: Double = (fractionDecimal - integralPart)
        val binary: StringBuilder = StringBuilder()
        var remainder: Int

        while(integralPart > 1) {
            remainder = (integralPart % 2).toInt()
            integralPart /= 2
            binary.append(remainder.toString())
        }
        binary.append(integralPart.toString())

        fractionBinary.append(binary.toString().reversed())
        fractionBinary.append(".")

        while (limit-- > 0) {
            fractionalPart *= 2.0
            val fractionBit: Int = fractionalPart.toInt()
            if (fractionBit == 1) {
                fractionalPart -= fractionBit.toDouble()
                fractionBinary.append(fractionBit.toString())
            } else {
                fractionBinary.append(fractionBit.toString())
            }
        }

        return fractionBinary.toString()
    }

    fun convertBinaryToDecimal(binary: Double, isFraction: Boolean = true, fractionRepeatLimit: Int = 5, limitOverride: Boolean = false) : String {
        val baseFormat = NumberFormatUtils.BINARY_FORMAT
        val result: StringBuilder = StringBuilder()

        val splitData: List<String> = binary.toString().split(".")
        val integralBinaryPart: Long = splitData[0].toLong()
        val fractionalBinaryPart: Long = splitData[1].toLong()

        val decimalPart: String = integralBinaryPart.toString().reversed()
        var decimalPartResult: Long = 0

        for (i in decimalPart.indices) {
            decimalPartResult += (baseFormat.toDouble().pow(i.toDouble()).toLong() * decimalPart[i].toString().toLong())
        }

        result.append(decimalPartResult.toString())


        val fractionPart: String = fractionalBinaryPart.toString()
        var fractionPartResult: Double = 0.0
        if (limitOverride) {
            for (i in fractionPart.indices) {
                fractionPartResult += (fractionPart[i].toString().toDouble() * (1/(baseFormat.toDouble().pow((i+1).toDouble()))))
            }
        } else {
            val length = fractionPart.length
            if(length <= fractionRepeatLimit) {
                for (i in 0 until length) {
                    fractionPartResult += (fractionPart[i].toString().toDouble() * (1/(baseFormat.toDouble().pow((i+1).toDouble()))))
                }
            } else  {
                for (i in 0 until 4) {
                    fractionPartResult += (fractionPart[i].toString().toDouble() * (1/(baseFormat.toDouble().pow((i+1).toDouble()))))
                }
            }
        }

        val fractionRoundOff: Double = ((fractionPartResult * 100.0).roundToInt() / 100.0) + 0.02

        return (result.toString().toDouble() + fractionRoundOff).toString()
    }
}