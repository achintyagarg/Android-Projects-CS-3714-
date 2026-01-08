package edu.vt.cs3714.spring2023.tutorial03

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class Model : ViewModel() {
    private var coroutineJob: Job? = null
    private val delay: Long = 1_000
    private val repetitions = 10

    private val valuesLocal = mutableListOf(0, 0, 0)
    private val valuesLiveData = MutableLiveData<List<Int>>()
    val values: LiveData<List<Int>> get() = valuesLiveData

    init {
        valuesLiveData.value = valuesLocal
    }

    fun start() {
        coroutineJob = viewModelScope.launch {
            val leftDeferred = async { randomInt(0) }
            val middleDeferred = async { randomInt(1) }
            val rightDeferred = async { randomInt(2) }
            leftDeferred.await()
            middleDeferred.await()
            rightDeferred.await()
        }
    }

    fun stop() {
        coroutineJob?.cancel()
        coroutineJob = null
    }

    private suspend fun randomInt(index: Int) {
        for (i: Int in 0..repetitions) {
            delay(delay)
            val rand = (0..10).random()
            valuesLocal[index] = rand
            valuesLiveData.postValue(valuesLocal.toList())
        }
    }
}
