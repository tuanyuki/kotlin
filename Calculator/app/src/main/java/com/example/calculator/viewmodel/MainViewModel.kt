package com.example.calculator.viewmodel

import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
    private val TAG     = "1234"
    var unit            = MutableLiveData<String>()
    var textCaculator   = MutableLiveData<String>("")
    var canCalculator   = MutableLiveData<Boolean>()
    var useDot          = MutableLiveData<Boolean>()
    var result          = MutableLiveData<String>()

    init {
        canCalculator.value = true
        unit.value = "RAD"
        useDot.value = false
    }

    fun clickC(){
        textCaculator.value = ""
    }
    fun clickDel(){
        if (!textCaculator.value.isNullOrEmpty()) {
            val length = textCaculator.value.toString().length
            textCaculator.value = textCaculator.value!!.toString().substring(0,length-1)
        }
    }
    fun clickAdd(){
        if (canCalculator.value == false){
             clickDel()
        }
        if (textCaculator.value.toString().isNotEmpty()){
            useDot.value = false
            textCaculator.value += "+"
            canCalculator.value = false
        }
    }
    fun clickSub(){
        if (canCalculator.value == false){
            clickDel()
        }
        useDot.value = false
        textCaculator.value += "-"
        canCalculator.value = false
    }
    fun clickMul(){
        if (canCalculator.value == false){
            clickDel()
        }
        if (textCaculator.value.toString().isNotEmpty()){
            useDot.value = false
            textCaculator.value += "x"
            canCalculator.value = false
        }
    }
    fun clickDiv(){
        if (canCalculator.value == false){
            clickDel()
        }
        if (textCaculator.value.toString().isNotEmpty()){
            useDot.value = false
            textCaculator.value += "/"
            canCalculator.value = false
        }
    }
    fun clickDot(){
        if (useDot.value == false){
            useDot.value = true
            textCaculator.value += "."
            canCalculator.value = true
        }
    }

    fun clickDigit(view: View){
        val button = view as Button
        Log.d(TAG, "click_digit  : ${button.text}")
        textCaculator.value += button.text.toString()
        canCalculator.value = true
    }

    fun clickEqual() {
        if (textCaculator.value.toString().isNotEmpty()){
            if (textCaculator.value.toString().get(textCaculator.value.toString().length-1).isDigit()){
                useDot.value = false
                canCalculator.value = true

                result.value = calculateResults()
                Log.d(TAG, "click_equal: ${result.value}")
            }
        }
    }

    private fun calculateResults(): String
    {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in textCaculator.value.toString())
        {
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }
}
