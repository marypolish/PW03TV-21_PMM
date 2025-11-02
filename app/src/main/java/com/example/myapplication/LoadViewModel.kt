package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoadViewModel : ViewModel() {

    private val _pnInput = MutableLiveData("20.0")
    val pnInput: LiveData<String> = _pnInput

    private val _kvInput = MutableLiveData("0.8")
    val kvInput: LiveData<String> = _kvInput

    private val _tgphiInput = MutableLiveData("0.484")
    val tgphiInput: LiveData<String> = _tgphiInput

    private val _results = MutableLiveData<CalculationResults?>()
    val results: LiveData<CalculationResults?> = _results

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private fun filterNumber(input: String): String {
        var s = input.filter { it.isDigit() || it == '.' }
        val dot = s.indexOf('.')
        if (dot >= 0) {
            s = s.substring(0, dot + 1) + s.substring(dot + 1).replace(".", "")
        }
        return s
    }

    fun updatePnInput(newInput: String) {
        val v = filterNumber(newInput)
        if (_pnInput.value != v) _pnInput.value = v
    }

    fun updateKvInput(newInput: String) {
        val v = filterNumber(newInput)
        if (_kvInput.value != v) _kvInput.value = v
    }

    fun updateTgPhiInput(newInput: String) {
        val v = filterNumber(newInput)
        if (_tgphiInput.value != v) _tgphiInput.value = v
    }

    fun calculate() {
        _results.value = null
        _error.value = null

        val pn = _pnInput.value?.toDoubleOrNull()
        val kv = _kvInput.value?.toDoubleOrNull()
        val tg = _tgphiInput.value?.toDoubleOrNull()

        if (pn == null || pn <= 0.0) {
            _error.value = "Введіть Pn > 0"
            return
        }
        if (kv == null || tg == null) {
            _error.value = "Введіть всі коефіцієнти"
            return
        }

        try {
            _results.value = calculateLoad(pn, kv, tg)
        } catch (e: Exception) {
            _error.value = "Помилка розрахунку: ${e.message}"
        }
    }
}
