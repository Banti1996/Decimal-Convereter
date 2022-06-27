package com.android.santanu.decimalconverter.data

import kotlin.math.pow
import kotlin.math.roundToInt

class NumberConverter {

    fun isDecimalNumber(decimal: String, checkFraction: Boolean = false): Boolean {
        return if (checkFraction) {
            val regex = "^[0-9]*[.]?[0-9]*\$".toRegex()
            decimal.matches(regex)
        } else {
            val regex = "^[0-9][0-9]*\$".toRegex()
            decimal.matches(regex)
        }
    }
    fun isBinaryNumber(binary: String, checkFraction: Boolean = false): Boolean {
        return if (checkFraction) {
            val regex = "^[0-1]*[.]?[0-1]*\$".toRegex()
            binary.matches(regex)
        } else {
            val regex = "^[0-1][0-1]*\$".toRegex()
            binary.matches(regex)
        }
    }
    fun isOctalNumber(octal: String, checkFraction: Boolean = false): Boolean {
        return if (checkFraction) {
            val regex = "^[0-7]*[.]?[0-7]*\$".toRegex()
            octal.matches(regex)
        } else {
            val regex = "^[0-7][0-7]*\$".toRegex()
            octal.matches(regex)
        }

    }
    fun isHexadecimalNumber(hexadecimal: String, checkFraction: Boolean = false): Boolean {
        return if (checkFraction) {
            val regex = "^[0-9A-Fa-f]*[.]?[0-9A-Fa-f]+\$".toRegex()
            hexadecimal.matches(regex)
        } else {
            val regex = "^[0-9A-Fa-f]+\$".toRegex()
            hexadecimal.matches(regex)
        }

    }

    fun convertDecimalToBinary(decimalData: Long) : String {
        var decimal: Long = decimalData
        val binaryNumber: StringBuilder = StringBuilder()
        var remainder: Int

        while(decimal > 1) {
            remainder = (decimal % 2).toInt()
            decimal /= 2
            binaryNumber.append(remainder.toString())

        }
        binaryNumber.append(decimal.toString())

        return binaryNumber.toString().reversed()
    }
    fun convertDecimalToOctal(decimalData: Long) : String {
        var decimal: Long = decimalData
        val octalNumber: StringBuilder = StringBuilder()
        var remainder: Int

        while(decimal > 7) {
            remainder = (decimal % 8).toInt()
            decimal /= 8
            octalNumber.append(remainder.toString())

        }
        octalNumber.append(decimal.toString())

        return octalNumber.toString().reversed()
    }
    fun convertDecimalToHexadecimal(decimalData: Long) : String {
        var decimal: Long = decimalData
        val hexadecimalNumber: StringBuilder = StringBuilder()
        var remainder: Int

        while(decimal > 15) {
            remainder = (decimal % 16).toInt()
            decimal /= 16
            when (val data: String = remainder.toString()) {
                "15" -> {
                    hexadecimalNumber.append("F")
                }
                "14" -> {
                    hexadecimalNumber.append("E")
                }
                "13" -> {
                    hexadecimalNumber.append("D")
                }
                "12" -> {
                    hexadecimalNumber.append("C")
                }
                "11" -> {
                    hexadecimalNumber.append("B")
                }
                "10" -> {
                    hexadecimalNumber.append("A")
                }
                else -> {
                    hexadecimalNumber.append(data)
                }
            }
        }

        when (val data: String = decimal.toString()) {
            "15" -> {
                hexadecimalNumber.append("F")
            }
            "14" -> {
                hexadecimalNumber.append("E")
            }
            "13" -> {
                hexadecimalNumber.append("D")
            }
            "12" -> {
                hexadecimalNumber.append("C")
            }
            "11" -> {
                hexadecimalNumber.append("B")
            }
            "10" -> {
                hexadecimalNumber.append("A")
            }
            else -> {
                hexadecimalNumber.append(data)
            }
        }

        return hexadecimalNumber.toString().reversed()
    }

    fun convertBinaryToDecimal(binaryData: Long) : String {
        val base: Int = 2
        val binary: String = binaryData.toString().reversed()
        var decimal: Long = 0

        for (i in binary.indices) {
            decimal += (base.toDouble().pow(i.toDouble()).toLong() * binary[i].toString().toLong()).toLong()
        }

        return decimal.toString()
    }
    fun convertBinaryToOctal(binaryData: Long) : String {
        return this.convertDecimalToOctal(
            this.convertBinaryToDecimal(binaryData).toLong()
        )
    }
    fun convertBinaryToHexadecimal(binaryData: Long) : String {
        return this.convertDecimalToHexadecimal(
            this.convertBinaryToDecimal(binaryData).toLong()
        )
    }

    fun convertOctalToDecimal(octalData: Long) : String {
        val base: Int = 8
        val octal: String = octalData.toString().reversed()
        var decimal: Long = 0

        for (i in octal.indices) {
            decimal += (base.toDouble().pow(i.toDouble()).toLong() * octal[i].toString().toLong()).toLong()
        }

        return decimal.toString()
    }
    fun convertOctalToBinary(octalData: Long) : String {
        return this.convertDecimalToBinary(
            this.convertOctalToDecimal(octalData).toLong()
        )
    }
    fun convertOctalToHexadecimal(octalData: Long) : String {
        return this.convertDecimalToHexadecimal(
            this.convertOctalToDecimal(octalData).toLong()
        )
    }

    fun convertHexadecimalToDecimal(hexadecimalData: String) : String {
        val base: Int = 16
        val hexadecimal: String = hexadecimalData.reversed()
        var decimal: Long = 0
        var data: String = ""

        for (i in hexadecimal.indices) {

            data = hexadecimal[i].toString()

            when (data) {
                "F" -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * 15L).toLong()
                }
                "E" -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * 14L).toLong()
                }
                "D" -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * 13L).toLong()
                }
                "C" -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * 12L).toLong()
                }
                "B" -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * 11L).toLong()
                }
                "A" -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * 10L).toLong()
                }
                else -> {
                    decimal += (base.toDouble().pow(i.toDouble()).toLong() * data.toLong()).toLong()
                }
            }

        }

        return decimal.toString()
    }
    fun convertHexadecimalToBinary(hexadecimalData: String) : String {
        return this.convertDecimalToBinary(
            this.convertHexadecimalToDecimal(hexadecimalData).toLong()
        )
    }
    fun convertHexadecimalToOctal(hexadecimalData: String) : String {
        return this.convertDecimalToOctal(
            this.convertHexadecimalToDecimal(hexadecimalData).toLong()
        )
    }


    private fun decimalToOtherNumberFormat(
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

    private fun otherToDecimalNumberFormat(
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


    fun decimalToBinary(decimalData: String, isFraction: Boolean = true) : String {
        return this.decimalToOtherNumberFormat(decimalData, NumberFormatUtils.BINARY_FORMAT, isFraction)
    }
    fun decimalToOctal(decimalData: String, isFraction: Boolean = true) : String {
        return this.decimalToOtherNumberFormat(decimalData, NumberFormatUtils.OCTAL_FORMAT, isFraction)
    }
    fun decimalToHexadecimal(decimalData: String, isFraction: Boolean = true) : String {
        return this.decimalToOtherNumberFormat(decimalData, NumberFormatUtils.HEXADECIMAL_FORMAT, isFraction)
    }

    fun binaryToDecimal(binaryData: String, isFraction: Boolean = true) : String {
        return this.otherToDecimalNumberFormat(binaryData, NumberFormatUtils.BINARY_FORMAT, isFraction)
    }
    fun binaryToOctal(binaryData: String, isFraction: Boolean = true) : String {
        return this.decimalToOtherNumberFormat(
            this.otherToDecimalNumberFormat(
                binaryData, NumberFormatUtils.BINARY_FORMAT, isFraction
            ), NumberFormatUtils.OCTAL_FORMAT, isFraction
        )
    }
    fun binaryToHexadecimal(binaryData: String, isFraction: Boolean = true) : String {
        return this.decimalToOtherNumberFormat(
            this.otherToDecimalNumberFormat(
                binaryData, NumberFormatUtils.BINARY_FORMAT, isFraction
            ), NumberFormatUtils.HEXADECIMAL_FORMAT, isFraction
        )
    }

    fun octalToDecimal(octalData: String, isFraction: Boolean = true) : String {
        return this.otherToDecimalNumberFormat(octalData, NumberFormatUtils.OCTAL_FORMAT, isFraction)
    }
    fun octalToBinary(octalData: String, isFraction: Boolean = true) : String {
        return this.decimalToOtherNumberFormat(
            this.otherToDecimalNumberFormat(
                octalData, NumberFormatUtils.OCTAL_FORMAT, isFraction
            ), NumberFormatUtils.OCTAL_FORMAT, isFraction
        )
    }
    fun octalToHexadecimal(octalData: String, isFraction: Boolean = true) : String {
        return this.decimalToOtherNumberFormat(
            this.otherToDecimalNumberFormat(
                octalData, NumberFormatUtils.OCTAL_FORMAT, isFraction
            ), NumberFormatUtils.HEXADECIMAL_FORMAT, isFraction
        )
    }

    fun hexadecimalToDecimal(hexadecimalData: String, isFraction: Boolean = true) : String {
        return this.otherToDecimalNumberFormat(hexadecimalData, NumberFormatUtils.HEXADECIMAL_FORMAT, isFraction)
    }
    fun hexadecimalToBinary(hexadecimalData: String, isFraction: Boolean = true) : String {
        return this.decimalToOtherNumberFormat(
            this.otherToDecimalNumberFormat(
                hexadecimalData, NumberFormatUtils.HEXADECIMAL_FORMAT, isFraction
            ), NumberFormatUtils.BINARY_FORMAT, isFraction
        )
    }
    fun hexadecimalToOctal(hexadecimalData: String, isFraction: Boolean = true) : String {
        return this.decimalToOtherNumberFormat(
            this.otherToDecimalNumberFormat(
                hexadecimalData, NumberFormatUtils.HEXADECIMAL_FORMAT, isFraction
            ), NumberFormatUtils.OCTAL_FORMAT, isFraction
        )
    }
}
